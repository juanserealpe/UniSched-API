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
                        IScheduleGenerationService generateScheduleService) {
                this.validationService = validationService;
                this.subjectRepository = subjectRepository;
                this.groupRepository = groupRepository;
                this.generateScheduleService = generateScheduleService;
        }

        /**
         * Validates if a combination of subjects is valid and manages subject groups.
         * 
         * A combination is invalid if:
         * - A subject and its prerequisite are selected together (e.g., Cálculo I and
         * Cálculo II)
         * - A subject is selected without its prerequisites
         * - A subject is selected with another that has a mandatory relationship with a
         * blocked subject
         * 
         * The request can include:
         * - subjectIds: IDs to validate against the study plan
         * - newGroups: New groups to create (not validated against study plan)
         * 
         * @param request subject selection request containing subject IDs and optional
         *                new groups
         * @return ResponseEntity containing validation result with isValid flag, error
         *         messages, and available groups
         */
        @PostMapping("/validate")
        public ResponseEntity<ValidationResponseDto> validate(
                        @Valid @RequestBody SubjectSelectionRequest request) {
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
                                                                Collectors.toList())));

                if (errors.isEmpty()) {
                        return ResponseEntity.ok(ValidationResponseDto.valid(groupsBySubject));
                } else {
                        return ResponseEntity.ok(ValidationResponseDto.invalid(errors));
                }
        }

        /**
         * Validates if a combination of subjects and exclusions is valid without
         * generating schedules.
         * Useful for real-time validation.
         *
         * @param request subject selection request with excluded groups
         * @return ValidationResponseDto with result
         */
        @PostMapping("/validate-exclusions")
        public ResponseEntity<ValidationResponseDto> validateExclusions(
                        @Valid @RequestBody SubjectSelectionRequest request) {
                Set<Long> subjectIds = request.subjectIds();
                List<SubjectSelectionRequest.CustomSubjectDto> customSubjects = request.customSubjects();
                List<Long> excludedGroupIdsList = request.excludedGroupIds();
                Set<Long> excludedGroupIds = excludedGroupIdsList != null ? new HashSet<>(excludedGroupIdsList)
                                : Collections.emptySet();

                if ((subjectIds == null || subjectIds.isEmpty()) &&
                                (customSubjects == null || customSubjects.isEmpty())) {
                        return ResponseEntity.badRequest()
                                        .body(ValidationResponseDto
                                                        .invalid(List.of("Debe seleccionar al menos una materia")));
                }

                Set<Subject> selectedSubjects = new HashSet<>();
                if (subjectIds != null && !subjectIds.isEmpty()) {
                        selectedSubjects = subjectRepository.findByIds(subjectIds);
                        if (selectedSubjects.size() != subjectIds.size()) {
                                return ResponseEntity.badRequest()
                                                .body(ValidationResponseDto
                                                                .invalid(List.of("Una o más IDs son inválidas")));
                        }
                }

                if (!selectedSubjects.isEmpty()) {
                        SubjectSelection selection = new SubjectSelection();
                        selectedSubjects.forEach(selection::select);
                        List<String> errors = validationService.validateCombinationWithErrors(selection);
                        if (!errors.isEmpty()) {
                                return ResponseEntity.ok(ValidationResponseDto.invalid(errors));
                        }
                }

                Map<Long, List<ValidationResponseDto.SubjectGroupDto>> responseGroups = new HashMap<>();

                if (!selectedSubjects.isEmpty()) {
                        List<SubjectGroup> allGroups = groupRepository.findBySubjectIds(subjectIds);
                        if (!excludedGroupIds.isEmpty()) {
                                allGroups = allGroups.stream()
                                                .filter(group -> !excludedGroupIds.contains(group.getId()))
                                                .collect(Collectors.toList());
                        }

                        Map<Subject, List<SubjectGroup>> groupsBySubject = allGroups.stream()
                                        .collect(Collectors.groupingBy(SubjectGroup::getSubject));

                        for (Subject subject : selectedSubjects) {
                                if (!groupsBySubject.containsKey(subject) || groupsBySubject.get(subject).isEmpty()) {
                                        return ResponseEntity.ok(ValidationResponseDto.invalid(
                                                        List.of("La materia " + subject.getName()
                                                                        + " no tiene grupos disponibles después de aplicar los filtros")));
                                }
                        }

                        var officialDtos = allGroups.stream()
                                        .collect(Collectors.groupingBy(
                                                        group -> group.getSubject().getId(),
                                                        Collectors.mapping(
                                                                        ValidationResponseDto.SubjectGroupDto::fromDomain,
                                                                        Collectors.toList())));
                        responseGroups.putAll(officialDtos);
                }

                if (customSubjects != null && !customSubjects.isEmpty()) {
                        long customIdCounter = -1L;
                        for (SubjectSelectionRequest.CustomSubjectDto customDto : customSubjects) {
                                Subject customSubject = new Subject(customIdCounter--, customDto.name(),
                                                (byte) 0);
                                List<SubjectGroup> customGroups = new ArrayList<>();

                                for (SubjectSelectionRequest.CustomGroupDto groupDto : customDto.groups()) {
                                        if (groupDto.excluded())
                                                continue;

                                        List<Schedule> schedules = groupDto.schedules().stream()
                                                        .map(s -> new Schedule(s.dayOfWeek(), s.startTime(),
                                                                        s.endTime()))
                                                        .collect(Collectors.toList());

                                        SubjectGroup customGroup = new SubjectGroup(
                                                        customIdCounter--, customSubject, groupDto.groupCode(),
                                                        groupDto.professors() != null ? groupDto.professors()
                                                                        : "Sin especificar",
                                                        schedules);
                                        customGroups.add(customGroup);
                                }

                                if (customGroups.isEmpty()) {
                                        return ResponseEntity.ok(ValidationResponseDto.invalid(
                                                        List.of("La materia personalizada " + customDto.name()
                                                                        + " no tiene grupos disponibles después de aplicar los filtros")));
                                }

                                responseGroups.put(customSubject.getId(), customGroups.stream()
                                                .map(ValidationResponseDto.SubjectGroupDto::fromDomain)
                                                .collect(Collectors.toList()));
                        }
                }

                return ResponseEntity.ok(ValidationResponseDto.valid(responseGroups));
        }

        /**
         * Generates all valid schedules for selected subjects and custom subjects.
         *
         * This endpoint:
         * 1. Validates regular subject IDs against the study plan
         * 2. Creates temporary Subject/SubjectGroup objects for custom subjects with
         * multiple groups
         * 3. Combines both types of subjects for schedule generation
         * 4. Uses backtracking to find all valid non-conflicting schedules
         *
         * @param request contains subjectIds and optional customSubjects (with multiple
         *                groups each)
         * @return list of valid schedules or validation errors
         */
        @PostMapping("/generate-schedules")
        public ResponseEntity<?> generateSchedules(
                        @Valid @RequestBody SubjectSelectionRequest request) {
                Set<Long> subjectIds = request.subjectIds();
                List<SubjectSelectionRequest.CustomSubjectDto> customSubjects = request.customSubjects();
                // Step 1: Get excluded group IDs
                List<Long> excludedGroupIdsList = request.excludedGroupIds();
                Set<Long> excludedGroupIds = excludedGroupIdsList != null ? new HashSet<>(excludedGroupIdsList)
                                : Collections.emptySet();

                if ((subjectIds == null || subjectIds.isEmpty()) &&
                                (customSubjects == null || customSubjects.isEmpty())) {
                        return ResponseEntity.badRequest()
                                        .body(ValidationResponseDto.invalid(
                                                        List.of("Debe seleccionar al menos una materia")));
                }

                Set<Subject> selectedSubjects = new HashSet<>();
                if (subjectIds != null && !subjectIds.isEmpty()) {
                        selectedSubjects = subjectRepository.findByIds(subjectIds);

                        if (selectedSubjects.size() != subjectIds.size()) {
                                return ResponseEntity.badRequest()
                                                .body(ValidationResponseDto.invalid(
                                                                List.of("Una o más IDs son inválidas")));
                        }
                }

                if (!selectedSubjects.isEmpty()) {
                        SubjectSelection selection = new SubjectSelection();
                        selectedSubjects.forEach(selection::select);
                        List<String> errors = validationService.validateCombinationWithErrors(selection);

                        if (!errors.isEmpty()) {
                                return ResponseEntity.ok(ValidationResponseDto.invalid(errors));
                        }
                }

                Map<Subject, List<SubjectGroup>> groupsBySubject = new HashMap<>();

                if (!selectedSubjects.isEmpty()) {
                        List<SubjectGroup> allGroups = groupRepository.findBySubjectIds(subjectIds);

                        // Step 2: Filter excluded official groups
                        if (!excludedGroupIds.isEmpty()) {
                                allGroups = allGroups.stream()
                                                .filter(group -> !excludedGroupIds.contains(group.getId()))
                                                .collect(Collectors.toList());
                        }

                        groupsBySubject = allGroups.stream()
                                        .collect(Collectors.groupingBy(SubjectGroup::getSubject));

                        // Step 3: Validate that all selected official subjects have at least one group
                        for (Subject subject : selectedSubjects) {
                                if (!groupsBySubject.containsKey(subject) || groupsBySubject.get(subject).isEmpty()) {
                                        return ResponseEntity.badRequest()
                                                        .body(ValidationResponseDto.invalid(
                                                                        List.of("La materia " + subject.getName()
                                                                                        + " no tiene grupos disponibles después de aplicar los filtros")));
                                }
                        }
                }

                if (customSubjects != null && !customSubjects.isEmpty()) {
                        long customIdCounter = -1L;

                        for (SubjectSelectionRequest.CustomSubjectDto customDto : customSubjects) {
                                Subject customSubject = new Subject(
                                                customIdCounter--,
                                                customDto.name(),
                                                (byte) 0);

                                List<SubjectGroup> customGroups = new ArrayList<>();

                                for (SubjectSelectionRequest.CustomGroupDto groupDto : customDto.groups()) {
                                        // Step 4: Filter excluded custom groups
                                        if (groupDto.excluded()) {
                                                continue;
                                        }

                                        List<Schedule> schedules = groupDto.schedules().stream()
                                                        .map(s -> new Schedule(
                                                                        s.dayOfWeek(),
                                                                        s.startTime(),
                                                                        s.endTime()))
                                                        .collect(Collectors.toList());
                                        SubjectGroup customGroup = new SubjectGroup(
                                                        customIdCounter--,
                                                        customSubject,
                                                        groupDto.groupCode(),
                                                        groupDto.professors() != null ? groupDto.professors()
                                                                        : "Sin especificar",
                                                        schedules);

                                        customGroups.add(customGroup);
                                }

                                if (customGroups.isEmpty()) {
                                        return ResponseEntity.badRequest()
                                                        .body(ValidationResponseDto.invalid(
                                                                        List.of("La materia personalizada "
                                                                                        + customDto.name()
                                                                                        + " no tiene grupos disponibles después de aplicar los filtros")));
                                }

                                groupsBySubject.put(customSubject, customGroups);
                        }
                }

                List<List<SubjectGroup>> schedules = generateScheduleService.generateAllValidSchedules(groupsBySubject);

                var response = schedules.stream()
                                .map(schedule -> schedule.stream()
                                                .map(ValidationResponseDto.SubjectGroupDto::fromDomain)
                                                .toList())
                                .toList();

                return ResponseEntity.ok(response);
        }
}