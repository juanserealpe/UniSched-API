package co.unicauca.edu.unisched.application;

import co.unicauca.edu.unisched.domain.model.SubjectCombinationOutcome;
import co.unicauca.edu.unisched.domain.model.SubjectGroup;
import co.unicauca.edu.unisched.domain.ports.IScheduleGenerationService;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Set;
/**
 * Application use case responsible for generating all valid academic schedules.
 *
 * It validates the subject selection (including custom groups and exclusions)
 * and delegates the schedule generation logic to the domain scheduling service.
 *
 * This use case orchestrates validation and schedule generation, but does not
 * contain business rules or scheduling algorithms itself.
 */
@Service
public class GenerateAcademicSchedulesUseCase {

    private final ValidateWithExclusionsUseCase validateWithExclusionsUseCase;
    private final IScheduleGenerationService generationService;

    public GenerateAcademicSchedulesUseCase(ValidateWithExclusionsUseCase validateWithExclusionsUseCase,
            IScheduleGenerationService generationService) {
        this.validateWithExclusionsUseCase = validateWithExclusionsUseCase;
        this.generationService = generationService;
    }

    /**
     * Generates all valid schedules for the given subject selection.
     *
     * This method:
     * 1. Validates selected subjects, custom groups, and excluded groups.
     * 2. Throws an exception if the selection is invalid.
     * 3. Delegates the schedule generation to the domain scheduling service.
     *
     * @param subjectIds IDs of selected official subjects
     * @param customGroups Custom subject groups provided by the user
     * @param excludedGroupIds IDs of subject groups to exclude
     * @return A list of valid schedules, where each schedule is a list of SubjectGroup
     */
    public List<List<SubjectGroup>> generate(Set<Long> subjectIds,
            List<SubjectGroup> customGroups,
            Set<Long> excludedGroupIds) {
        SubjectCombinationOutcome outcome = validateWithExclusionsUseCase.validate(subjectIds, customGroups,
                excludedGroupIds);
        if (!outcome.isValid()) throw new IllegalArgumentException(String.join(", ", outcome.getErrors()));


        return generationService.generateAllValidSchedules(outcome.getGroupsBySubject());
    }
}
