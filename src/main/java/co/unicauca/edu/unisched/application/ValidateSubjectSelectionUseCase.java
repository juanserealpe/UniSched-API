package co.unicauca.edu.unisched.application;

import co.unicauca.edu.unisched.domain.model.Subject;
import co.unicauca.edu.unisched.domain.model.SubjectCombinationOutcome;
import co.unicauca.edu.unisched.domain.model.SubjectGroup;
import co.unicauca.edu.unisched.domain.model.SubjectSelection;
import co.unicauca.edu.unisched.domain.ports.ISubjectGroupRepository;
import co.unicauca.edu.unisched.domain.ports.ISubjectRepository;
import co.unicauca.edu.unisched.domain.ports.ISubjectValidationService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
/**
 * Application use case responsible for validating an official subject selection.
 *
 * It verifies that selected subject IDs exist, checks academic business rules
 * such as prerequisites and mandatory combinations, and groups the available
 * subject groups for each valid subject.
 *
 * This use case does not apply exclusions or custom groups.
 */
@Service
public class ValidateSubjectSelectionUseCase {

    private final ISubjectRepository subjectRepository;
    private final ISubjectValidationService validationService;
    private final ISubjectGroupRepository groupRepository;

    public ValidateSubjectSelectionUseCase(@Qualifier("studyPlanService") ISubjectRepository subjectRepository,
            ISubjectValidationService validationService,
            ISubjectGroupRepository groupRepository) {
        this.subjectRepository = subjectRepository;
        this.validationService = validationService;
        this.groupRepository = groupRepository;
    }

    /**
     * Validates a set of selected subject IDs.
     *
     * This method:
     * 1. Ensures all subject IDs exist.
     * 2. Validates academic rules (e.g. prerequisites, constraints).
     * 3. Retrieves and groups the available subject groups per subject.
     *
     * @param subjectIds IDs of the selected subjects
     * @return SubjectCombinationOutcome containing validation errors
     *         or grouped subject groups when valid
     */
    public SubjectCombinationOutcome validate(Set<Long> subjectIds) {
        // Step 1: Validate subject IDs
        Set<Subject> selectedSubjects = subjectRepository.findByIds(subjectIds);
        if (selectedSubjects.size() != subjectIds.size()) {
            return SubjectCombinationOutcome.invalid(List.of("Una o más IDs son inválidas"));
        }

        // Step 2: Validate combination logic (prerequisites, mandatory pairs)
        SubjectSelection selection = new SubjectSelection();
        selectedSubjects.forEach(selection::select);
        List<String> errors = validationService.validateCombinationWithErrors(selection);

        if (!errors.isEmpty()) {
            return SubjectCombinationOutcome.invalid(errors);
        }

        // Step 3: Fetch Groups
        List<SubjectGroup> allGroups = groupRepository.findBySubjectIds(subjectIds);
        Map<Subject, List<SubjectGroup>> groupsBySubject = allGroups.stream()
                .collect(Collectors.groupingBy(SubjectGroup::getSubject));

        return SubjectCombinationOutcome.valid(groupsBySubject);
    }
}
