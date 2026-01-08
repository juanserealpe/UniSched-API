package co.unicauca.edu.unisched.interfaces.api;

import co.unicauca.edu.unisched.domain.model.Schedule;
import co.unicauca.edu.unisched.domain.model.Subject;
import co.unicauca.edu.unisched.domain.model.SubjectGroup;
import co.unicauca.edu.unisched.domain.model.SubjectSelection;
import co.unicauca.edu.unisched.domain.ports.IScheduleGenerationService;
import co.unicauca.edu.unisched.domain.ports.ISubjectGroupRepository;
import co.unicauca.edu.unisched.domain.ports.ISubjectRepository;
import co.unicauca.edu.unisched.domain.ports.ISubjectValidationService;
import co.unicauca.edu.unisched.interfaces.dto.SubjectSelectionRequest;
import co.unicauca.edu.unisched.interfaces.dto.ValidationResponseDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * REST controller for subject selection validation.
 * Provides a single endpoint to validate if a combination of subjects is valid
 * and manage subject groups.
 */
@RestController
@RequestMapping("/api/subjects")
@CrossOrigin(origins = "*")
public class SubjectSelectionController {

    private final ISubjectValidationService validationService;
    private final ISubjectRepository subjectRepository;
    private final ISubjectGroupRepository groupRepository;
    private final IScheduleGenerationService generateScheduleService;

    public SubjectSelectionController(
            ISubjectValidationService validationService,
            ISubjectRepository subjectRepository,
            ISubjectGroupRepository groupRepository,
            IScheduleGenerationService generateScheduleService
    ) {
        this.validationService = validationService;
        this.subjectRepository = subjectRepository;
        this.groupRepository = groupRepository;
        this.generateScheduleService = generateScheduleService;
    }


    /**
     * Validates if a combination of subjects is valid and manages subject groups.
     * 
     * A combination is invalid if:
     * - A subject and its prerequisite are selected together (e.g., Cálculo I and Cálculo II)
     * - A subject is selected without its prerequisites
     * - A subject is selected with another that has a mandatory relationship with a blocked subject
     * 
     * The request can include:
     * - subjectIds: IDs to validate against the study plan
     * - newGroups: New groups to create (not validated against study plan)
     * 
     * @param request subject selection request containing subject IDs and optional new groups
     * @return ResponseEntity containing validation result with isValid flag, error messages, and available groups
     */
    @PostMapping("/validate")
    public ResponseEntity<ValidationResponseDto> validate(
            @Valid @RequestBody SubjectSelectionRequest request
    ) {
        // Step 1: Validate subject IDs against study plan
        Set<Long> subjectIds = request.subjectIds();
        Set<Subject> selectedSubjects = subjectRepository.findByIds(subjectIds);
        
        if (selectedSubjects.size() != subjectIds.size())
            return ResponseEntity.badRequest()
                    .body(ValidationResponseDto.invalid(List.of("Una o más IDs son inválidas")));

        // Step 2: Validate subject combination
        SubjectSelection selection = new SubjectSelection();
        selectedSubjects.forEach(selection::select);

        List<String> errors = validationService.validateCombinationWithErrors(selection);
        
        // Step 3: Get all available groups for the selected subjects
        List<SubjectGroup> allGroups = groupRepository.findBySubjectIds(subjectIds);
        
        // Step 4: Organize groups by subject ID
        Map<Long, List<ValidationResponseDto.SubjectGroupDto>> groupsBySubject = allGroups.stream()
                .collect(Collectors.groupingBy(
                        group -> group.getSubject().getId(),
                        Collectors.mapping(
                                ValidationResponseDto.SubjectGroupDto::fromDomain,
                                Collectors.toList()
                        )
                ));
        
        if (errors.isEmpty()) {
            return ResponseEntity.ok(ValidationResponseDto.valid(groupsBySubject));
        } else {
            return ResponseEntity.ok(ValidationResponseDto.invalid(errors));
        }
    }

    /**
     * Generates all valid schedules for selected subjects and custom subjects.
     *
     * This endpoint:
     * 1. Validates regular subject IDs against the study plan
     * 2. Creates temporary Subject/SubjectGroup objects for custom subjects
     * 3. Combines both types of subjects for schedule generation
     * 4. Uses backtracking to find all valid non-conflicting schedules
     *
     * @param request contains subjectIds and optional customSubjects
     * @return list of valid schedules or validation errors
     */
    @PostMapping("/generate-schedules")
    public ResponseEntity<?> generateSchedules(
            @Valid @RequestBody SubjectSelectionRequest request
    ) {
        Set<Long> subjectIds = request.subjectIds();
        List<SubjectSelectionRequest.CustomSubjectDto> customSubjects = request.customSubjects();

        // Validar que haya al menos una materia
        if ((subjectIds == null || subjectIds.isEmpty()) &&
                (customSubjects == null || customSubjects.isEmpty())) {
            return ResponseEntity.badRequest()
                    .body(ValidationResponseDto.invalid(
                            List.of("Debe seleccionar al menos una materia")));
        }

        // Step 1: Obtener materias normales de BD
        Set<Subject> selectedSubjects = new HashSet<>();
        if (subjectIds != null && !subjectIds.isEmpty()) {
            selectedSubjects = subjectRepository.findByIds(subjectIds);

            if (selectedSubjects.size() != subjectIds.size()) {
                return ResponseEntity.badRequest()
                        .body(ValidationResponseDto.invalid(
                                List.of("Una o más IDs son inválidas")));
            }
        }

        // Step 2: Validar combinación de materias normales (solo las de BD)
        if (!selectedSubjects.isEmpty()) {
            SubjectSelection selection = new SubjectSelection();
            selectedSubjects.forEach(selection::select);
            List<String> errors = validationService.validateCombinationWithErrors(selection);

            if (!errors.isEmpty()) {
                return ResponseEntity.ok(ValidationResponseDto.invalid(errors));
            }
        }

        // Step 3: Obtener grupos de materias normales
        Map<Subject, List<SubjectGroup>> groupsBySubject = new HashMap<>();

        if (!selectedSubjects.isEmpty()) {
            List<SubjectGroup> allGroups = groupRepository.findBySubjectIds(subjectIds);
            groupsBySubject = allGroups.stream()
                    .collect(Collectors.groupingBy(SubjectGroup::getSubject));
        }

        // Step 4: Agregar materias personalizadas
        if (customSubjects != null && !customSubjects.isEmpty()) {
            long customIdCounter = -1L; // IDs negativos para custom subjects

            for (SubjectSelectionRequest.CustomSubjectDto customDto : customSubjects) {
                // Crear Subject temporal
                Subject customSubject = new Subject(
                        customIdCounter--,
                        customDto.name(),
                        (byte) 0 // Semestre 0 para custom
                );

                // Crear Schedule objects
                List<Schedule> schedules = customDto.schedules().stream()
                        .map(s -> new Schedule(
                                s.dayOfWeek(),
                                s.startTime(),
                                s.endTime()
                        ))
                        .collect(Collectors.toList());

                // Crear SubjectGroup temporal
                SubjectGroup customGroup = new SubjectGroup(
                        customIdCounter, // Mismo ID negativo
                        customSubject,
                        customDto.groupCode() != null ? customDto.groupCode() : "CUSTOM",
                        "Usuario", // Profesor por defecto
                        schedules
                );

                // Agregar al mapa con un solo grupo por materia custom
                groupsBySubject.put(customSubject, List.of(customGroup));
            }
        }

        // Step 5: Generar horarios
        List<List<SubjectGroup>> schedules =
                generateScheduleService.generateAllValidSchedules(groupsBySubject);

        // Step 6: Convertir a DTO
        var response = schedules.stream()
                .map(schedule ->
                        schedule.stream()
                                .map(ValidationResponseDto.SubjectGroupDto::fromDomain)
                                .toList()
                )
                .toList();

        return ResponseEntity.ok(response);
    }
}