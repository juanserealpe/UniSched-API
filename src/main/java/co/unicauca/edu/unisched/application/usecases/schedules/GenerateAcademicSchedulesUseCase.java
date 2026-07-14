package co.unicauca.edu.unisched.application.usecases.schedules;

import co.unicauca.edu.unisched.domain.model.SubjectCombinationOutcome;
import co.unicauca.edu.unisched.domain.model.SubjectGroup;
import co.unicauca.edu.unisched.domain.ports.schedules.IScheduleGenerationService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger =
            LoggerFactory.getLogger(GenerateAcademicSchedulesUseCase.class);

    private final ValidateWithExclusionsUseCase validateWithExclusionsUseCase;
    private final IScheduleGenerationService generationService;

    public GenerateAcademicSchedulesUseCase(
            ValidateWithExclusionsUseCase validateWithExclusionsUseCase,
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

        logger.info(
                "Starting academic schedule generation. Subjects: {}, Custom groups: {}, Excluded groups: {}.",
                subjectIds.size(),
                customGroups.size(),
                excludedGroupIds.size());

        logger.debug("Validating subject selection before schedule generation.");

        SubjectCombinationOutcome outcome =
                validateWithExclusionsUseCase.validate(
                        subjectIds,
                        customGroups,
                        excludedGroupIds);

        if (!outcome.isValid()) {

            logger.warn("Schedule generation aborted due to validation errors: {}",
                    outcome.getErrors());

            throw new IllegalArgumentException(
                    String.join(", ", outcome.getErrors()));
        }

        logger.info("Validation completed successfully.");

        logger.debug("Generating all valid academic schedules.");

        List<List<SubjectGroup>> schedules =
                generationService.generateAllValidSchedules(
                        outcome.getGroupsBySubject());

        logger.info(
                "Academic schedule generation completed successfully. Generated {} valid schedule(s).",
                schedules.size());

        return schedules;
    }
}