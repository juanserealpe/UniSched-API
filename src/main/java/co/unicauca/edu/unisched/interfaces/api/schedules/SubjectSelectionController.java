package co.unicauca.edu.unisched.interfaces.api.schedules;

import co.unicauca.edu.unisched.application.usecases.schedules.GenerateAcademicSchedulesUseCase;
import co.unicauca.edu.unisched.application.usecases.schedules.ValidateSubjectSelectionUseCase;
import co.unicauca.edu.unisched.application.usecases.schedules.ValidateWithExclusionsUseCase;
import co.unicauca.edu.unisched.domain.model.SubjectCombinationOutcome;
import co.unicauca.edu.unisched.domain.model.SubjectGroup;
import co.unicauca.edu.unisched.interfaces.api.dto.schedules.SubjectSelectionRequest;
import co.unicauca.edu.unisched.interfaces.api.dto.schedules.ValidationResponseDto;
import co.unicauca.edu.unisched.mapper.api.SubjectRequestToSubjectMapper;
import co.unicauca.edu.unisched.mapper.api.SubjectSelectionRequestMapper;
import co.unicauca.edu.unisched.mapper.api.ValidationResultToResponseMapper;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Set;

/**
 * REST controller responsible for handling subject selection operations.
 *
 * This controller exposes endpoints to:
 * - Validate a subject selection
 * - Validate a subject selection with exclusions and custom subject groups
 * - Generate valid academic schedules based on subject constraints
 *
 * It acts as an entry point to the application layer and delegates
 * business logic to use cases, following Clean Architecture principles.
 */
@RestController
@RequestMapping("/api/subjects")
@CrossOrigin(origins = "*")
public class SubjectSelectionController {

        private final ValidateSubjectSelectionUseCase validateUseCase;
        private final ValidateWithExclusionsUseCase validateWithExclusionsUseCase;
        private final GenerateAcademicSchedulesUseCase generateSchedulesUseCase;

        private final SubjectSelectionRequestMapper requestMapper;
        private final ValidationResultToResponseMapper responseMapper;
        private final SubjectRequestToSubjectMapper customSubjectMapper;

        public SubjectSelectionController(ValidateSubjectSelectionUseCase validateUseCase,
                        ValidateWithExclusionsUseCase validateWithExclusionsUseCase,
                        GenerateAcademicSchedulesUseCase generateSchedulesUseCase,
                        SubjectSelectionRequestMapper requestMapper,
                        ValidationResultToResponseMapper responseMapper,
                        SubjectRequestToSubjectMapper customSubjectMapper) {
                this.validateUseCase = validateUseCase;
                this.validateWithExclusionsUseCase = validateWithExclusionsUseCase;
                this.generateSchedulesUseCase = generateSchedulesUseCase;
                this.requestMapper = requestMapper;
                this.responseMapper = responseMapper;
                this.customSubjectMapper = customSubjectMapper;
        }
        /**
         * Validates a subject selection without exclusions or custom groups.
         *
         * This endpoint checks whether the selected subjects form
         * a valid combination according to academic rules
         * (e.g., schedule conflicts, group compatibility).
         *
         * @param request Incoming request containing selected subject IDs
         * @param academicPeriod The academic period for which to validate the selection
         * @return Validation result indicating whether the selection is valid
         */
        @PostMapping("/validate")
        public ResponseEntity<ValidationResponseDto> validate(@Valid @RequestBody SubjectSelectionRequest request) {
                Set<Long> subjectIds = requestMapper.extractSubjectIds(request);
                SubjectCombinationOutcome outcome = validateUseCase.validate(subjectIds);
                return ResponseEntity.ok(responseMapper.toDto(outcome));
        }
        /**
         * Validates a subject selection considering exclusions and custom subject groups.
         *
         * This endpoint allows:
         * - Excluding specific subject groups
         * - Including user-defined (custom) subject groups
         *
         * Useful when the student wants to avoid certain schedules
         * or manually add custom alternatives.
         *
         * @param request Incoming request with subject IDs, exclusions, and custom groups
         * @return Validation result with detailed feedback
         */
        @PostMapping("/validate-exclusions")
        public ResponseEntity<ValidationResponseDto> validateExclusions(
                        @Valid @RequestBody SubjectSelectionRequest request) {
                Set<Long> subjectIds = requestMapper.extractSubjectIds(request);
                List<SubjectGroup> customGroups = customSubjectMapper.mapToDomain(request.customSubjects(), -1L);
                Set<Long> excludedGroupIds = requestMapper.extractExcludedGroupIds(request);
                SubjectCombinationOutcome outcome = validateWithExclusionsUseCase.validate(subjectIds, customGroups,
                                excludedGroupIds);
                return ResponseEntity.ok(responseMapper.toDto(outcome));
        }
        /**
         * Generates all valid academic schedules based on the given constraints.
         *
         * This endpoint computes all possible combinations of subject groups
         * that:
         * - Match the selected subjects
         * - Respect excluded groups
         * - Include custom subject groups
         * - Do not present time conflicts
         *
         * @param request Incoming request with subjects, exclusions, and custom groups
         * @return List of valid schedules, each schedule being a list of subject groups
         */
        @PostMapping("/generate-schedules")
        public ResponseEntity<?> generateSchedules(@Valid @RequestBody SubjectSelectionRequest request) {
                Set<Long> subjectIds = requestMapper.extractSubjectIds(request);
                List<SubjectGroup> customGroups = customSubjectMapper.mapToDomain(request.customSubjects(), -1L);
                Set<Long> excludedGroupIds = requestMapper.extractExcludedGroupIds(request);

                try {
                        List<List<SubjectGroup>> schedules = generateSchedulesUseCase.generate(subjectIds, customGroups,
                                        excludedGroupIds);
                        var response = schedules.stream()
                                        .map(schedule -> schedule.stream()
                                                        .map(ValidationResponseDto.SubjectGroupDto::fromDomain)
                                                        .toList())
                                        .toList();
                        return ResponseEntity.ok(response);
                } catch (IllegalArgumentException e) {
                        // Reusing ValidationResponseDto for error messaging
                        return ResponseEntity.ok(ValidationResponseDto.invalid(List.of(e.getMessage().split(", "))));
                }
        }
}