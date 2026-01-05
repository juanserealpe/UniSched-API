package co.unicauca.edu.unisched.interfaces.api;

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
     * Generates all valid schedules for a given set of selected subjects.
     *
     * This endpoint performs the following steps:
     * <ol>
     *   <li>Receives a list of subject IDs selected by the user.</li>
     *   <li>Validates that all provided IDs correspond to existing subjects.</li>
     *   <li>Validates the subject combination according to academic rules
     *       (prerequisites, unlock relationships, and mandatory associations).</li>
     *   <li>Retrieves all available groups for the selected subjects.</li>
     *   <li>Generates all possible schedules using a backtracking algorithm,
     *       ensuring that exactly one group per subject is selected and that
     *       no schedule conflicts exist.</li>
     *   <li>Transforms the resulting schedules into DTOs suitable for the API response.</li>
     * </ol>
     *
     * If the subject combination is invalid, a detailed validation response
     * is returned containing the corresponding error messages.
     *
     * @param request the request body containing the IDs of the selected subjects
     * @return a {@link ResponseEntity} containing either:
     * <ul>
     *   <li>a list of valid schedules if generation is successful, or</li>
     *   <li>a validation response with error messages if the selection is invalid</li>
     * </ul>
     */
    @PostMapping("/generate-schedules")
    public ResponseEntity<?> generateSchedules(
            @Valid @RequestBody SubjectSelectionRequest request
    ) {
        Set<Long> subjectIds = request.subjectIds();
        Set<Subject> selectedSubjects = subjectRepository.findByIds(subjectIds);

        if (selectedSubjects.size() != subjectIds.size()) {
            return ResponseEntity.badRequest()
                    .body(ValidationResponseDto.invalid(
                            List.of("Una o más IDs son inválidas")));
        }

        SubjectSelection selection = new SubjectSelection();
        selectedSubjects.forEach(selection::select);
        List<String> errors =
                validationService.validateCombinationWithErrors(selection);

        if (!errors.isEmpty()) {
            return ResponseEntity.ok(
                    ValidationResponseDto.invalid(errors));
        }

        List<SubjectGroup> allGroups =
                groupRepository.findBySubjectIds(subjectIds);

        Map<Subject, List<SubjectGroup>> groupsBySubject =
                allGroups.stream()
                        .collect(Collectors.groupingBy(
                                SubjectGroup::getSubject
                        ));

        List<List<SubjectGroup>> schedules =
                generateScheduleService.generateAllValidSchedules(groupsBySubject);

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

