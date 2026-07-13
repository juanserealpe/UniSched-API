package co.unicauca.edu.unisched.application.usecases.schedules;

import co.unicauca.edu.unisched.domain.model.Subject;
import co.unicauca.edu.unisched.domain.model.SubjectCombinationOutcome;
import co.unicauca.edu.unisched.domain.model.SubjectGroup;
import co.unicauca.edu.unisched.domain.model.SubjectSelection;
import co.unicauca.edu.unisched.domain.ports.schedules.ISubjectGroupRepository;
import co.unicauca.edu.unisched.domain.ports.schedules.ISubjectRepository;
import co.unicauca.edu.unisched.domain.ports.schedules.ISubjectValidationService;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Application use case responsible for validating a subject selection
 * considering official subjects, custom groups, and excluded groups.
 *
 * It verifies business rules, applies exclusions, groups valid subject groups,
 * and returns a domain outcome representing either validation errors
 * or a valid grouped selection ready for schedule generation.
 */
@Service
public class ValidateWithExclusionsUseCase {

    private final ISubjectRepository subjectRepository;
    private final ISubjectValidationService validationService;
    private final ISubjectGroupRepository groupRepository;

    public ValidateWithExclusionsUseCase(@Qualifier("studyPlanService") ISubjectRepository subjectRepository,
            ISubjectValidationService validationService,
            ISubjectGroupRepository groupRepository) {
        this.subjectRepository = subjectRepository;
        this.validationService = validationService;
        this.groupRepository = groupRepository;
    }

    /**
     * Validates a subject selection with optional custom groups and excluded groups.
     *
     * This method:
     * 1. Validates selected subject IDs and business rules.
     * 2. Filters official subject groups based on exclusions.
     * 3. Ensures each subject has at least one available group.
     * 4. Merges official and custom groups into a final grouped result.
     *
     * @param subjectIds IDs of officially selected subjects
     * @param customGroups Custom subject groups defined by the user
     * @param excludedGroupIds IDs of groups to exclude from consideration
     * @return SubjectCombinationOutcome indicating validity, errors, or grouped subjects
     */
    public SubjectCombinationOutcome validate(Set<Long> subjectIds,
            List<SubjectGroup> customGroups,
            Set<Long> excludedGroupIds) {

        Set<Subject> selectedSubjects = new HashSet<>();

        // 1. Validate Official Subjects
        if (subjectIds != null && !subjectIds.isEmpty()) {
            selectedSubjects = subjectRepository.findByIds(subjectIds);
            if (selectedSubjects.size() != subjectIds.size()) {
                return SubjectCombinationOutcome.invalid(List.of("Una o más IDs son inválidas"));
            }

            SubjectSelection selection = new SubjectSelection();
            selectedSubjects.forEach(selection::select);
            List<String> errors = validationService.validateCombinationWithErrors(selection);
            if (!errors.isEmpty()) {
                return SubjectCombinationOutcome.invalid(errors);
            }
        }

        Map<Subject, List<SubjectGroup>> resultGroups = new HashMap<>();

        // 2. Process Official Groups with Exclusions
        if (!selectedSubjects.isEmpty()) {
            List<SubjectGroup> allGroups = groupRepository.findBySubjectIds(subjectIds);

            // Filter exclusions
            if (excludedGroupIds != null && !excludedGroupIds.isEmpty()) {
                allGroups = allGroups.stream()
                        .filter(group -> !excludedGroupIds.contains(group.getId()))
                        .collect(Collectors.toList());
            }

            Map<Subject, List<SubjectGroup>> groupsBySubject = allGroups.stream()
                    .collect(Collectors.groupingBy(SubjectGroup::getSubject));

            // Validate at least one group remains per subject
            for (Subject subject : selectedSubjects) {
                if (!groupsBySubject.containsKey(subject) || groupsBySubject.get(subject).isEmpty()) {
                    return SubjectCombinationOutcome.invalid(
                            List.of("La materia " + subject.getName()
                                    + " no tiene grupos disponibles después de aplicar los filtros"));
                }
            }
            resultGroups.putAll(groupsBySubject);
        }

        // 3. Add Custom Groups
        if (customGroups != null && !customGroups.isEmpty()) {
            Map<Subject, List<SubjectGroup>> customGroupsBySubject = customGroups.stream()
                    .collect(Collectors.groupingBy(SubjectGroup::getSubject));

            for (Map.Entry<Subject, List<SubjectGroup>> entry : customGroupsBySubject.entrySet()) {
                // Check if custom subject has groups (logic handled in mapper usually, but good
                // to check)
                if (entry.getValue().isEmpty()) {
                    return SubjectCombinationOutcome.invalid(
                            List.of("La materia personalizada " + entry.getKey().getName()
                                    + " no tiene grupos disponibles"));
                }
                resultGroups.put(entry.getKey(), entry.getValue());
            }
        }

        if (resultGroups.isEmpty()) {
            return SubjectCombinationOutcome.invalid(List.of("Debe seleccionar al menos una materia"));
        }

        return SubjectCombinationOutcome.valid(resultGroups);
    }
}
