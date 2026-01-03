package co.unicauca.edu.unisched.application;

import co.unicauca.edu.unisched.domain.model.SubjectGroup;
import co.unicauca.edu.unisched.domain.model.SubjectSelection;
import co.unicauca.edu.unisched.domain.ports.IEnrollmentService;
import co.unicauca.edu.unisched.domain.ports.ISubjectGroupRepository;
import co.unicauca.edu.unisched.domain.ports.ISubjectValidationService;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Use case to validate subject selection and obtain available groups.
 * Orchestrates two operations:
 * 1. Validate that the combination of subjects is valid (prerequisites, etc.)
 * 2. If valid, get the available groups for each subject
 */
@Service
public class EnrollmentValidationUseCase implements IEnrollmentService {

    private final ISubjectValidationService validationService;
    private final ISubjectGroupRepository groupRepository;

    public EnrollmentValidationUseCase(
            ISubjectValidationService validationService,
            ISubjectGroupRepository groupRepository) {
        this.validationService = validationService;
        this.groupRepository = groupRepository;
    }

    /**
     * Validates the selection and obtains the available groups.
     *
     * Flow:
     * 1. Validates the combination of subjects using the SubjectValidator
     * 2. If valid, queries the DB to get the groups for each subject
     * 3. Returns the result with groups organized by subject
     *
     * @param selection the student's subject selection
     * @return result with validation and available groups
     */
    @Override
    public EnrollmentValidationResult validateAndGetGroups(SubjectSelection selection) {
        // Step 1: Validate the combination of subjects
        List<String> errors = validationService.validateCombinationWithErrors(selection);

        // If there are errors, return an invalid result without querying groups
        if (!errors.isEmpty()) {
            return new EnrollmentValidationResult(false, errors, Collections.emptyMap());
        }

        // Step 2: Get the IDs of the selected subjects
        Set<Long> subjectIds = selection.getSelected().stream()
                .map(subject -> subject.getId())
                .collect(Collectors.toSet());

        // Step 3: Query available groups in the DB
        List<SubjectGroup> allGroups = groupRepository.findBySubjectIds(subjectIds);

        // Step 4: Organize groups by subject
        Map<Long, List<SubjectGroup>> groupsBySubject = allGroups.stream()
                .collect(Collectors.groupingBy(
                        group -> group.getSubject().getId()
                ));

        return new EnrollmentValidationResult(true, Collections.emptyList(), groupsBySubject);
    }
}
