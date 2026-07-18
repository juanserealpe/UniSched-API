package co.unicauca.edu.unisched.application.usecases.schedules;

import co.unicauca.edu.unisched.domain.model.Subject;
import co.unicauca.edu.unisched.domain.model.SubjectCombinationOutcome;
import co.unicauca.edu.unisched.domain.model.SubjectGroup;
import co.unicauca.edu.unisched.domain.model.SubjectSelection;
import co.unicauca.edu.unisched.domain.ports.schedules.IStudyPlanRepository;
import co.unicauca.edu.unisched.domain.ports.schedules.ISubjectGroupRepository;
import co.unicauca.edu.unisched.domain.ports.schedules.ISubjectRepository;
import co.unicauca.edu.unisched.domain.ports.schedules.ISubjectValidationService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
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

    private static final Logger logger =
            LoggerFactory.getLogger(ValidateSubjectSelectionUseCase.class);

    private final ISubjectRepository subjectRepository;
    private final ISubjectValidationService validationService;
    private final ISubjectGroupRepository groupRepository;
    private final IStudyPlanRepository studyPlanRepository;

    public ValidateSubjectSelectionUseCase(
            @Qualifier("subjectRepositoryAdapter") ISubjectRepository subjectRepository,
            ISubjectValidationService validationService,
            ISubjectGroupRepository groupRepository,
            IStudyPlanRepository studyPlanRepository) {
        this.subjectRepository = subjectRepository;
        this.validationService = validationService;
        this.groupRepository = groupRepository;
        this.studyPlanRepository = studyPlanRepository;
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
    public SubjectCombinationOutcome validate(Long careerId,
                                              Set<Long> subjectIds) {

        logger.info(
                "Starting subject selection validation for career {} with {} selected subjects.",
                careerId,
                subjectIds.size());

        logger.debug("Loading study plan for career {}.", careerId);

        // Step 1: Load the study plan
        Set<Subject> studyPlan = studyPlanRepository.loadByCareer(careerId);

        logger.debug("Study plan loaded successfully. Total subjects: {}.",
                studyPlan.size());

        // Step 2: Create index by subject ID
        Map<Long, Subject> subjectsById = studyPlan.stream()
                .collect(Collectors.toMap(
                        Subject::getId,
                        Function.identity()
                ));

        logger.debug("Created study plan index.");

        // Step 3: Retrieve selected subjects
        Set<Subject> selectedSubjects = subjectIds.stream()
                .map(subjectsById::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        logger.debug("Resolved {} selected subjects from the study plan.",
                selectedSubjects.size());

        // Step 4: Verify all selected subjects belong to the study plan
        if (selectedSubjects.size() != subjectIds.size()) {

            logger.warn(
                    "Validation failed. One or more selected subjects do not belong to the study plan for career {}.",
                    careerId);

            return SubjectCombinationOutcome.invalid(
                    List.of("Una o más materias no pertenecen al pensum.")
            );
        }

        logger.debug("All selected subjects belong to the study plan.");

        // Step 5: Validate academic rules
        SubjectSelection selection = new SubjectSelection();
        selectedSubjects.forEach(selection::select);

        logger.debug("Validating academic rules for the selected subjects.");

        List<String> errors =
                validationService.validateCombinationWithErrors(selection);

        if (!errors.isEmpty()) {

            logger.warn(
                    "Academic validation failed with {} validation error(s).",
                    errors.size());

            logger.debug("Validation errors: {}", errors);

            return SubjectCombinationOutcome.invalid(errors);
        }

        logger.info("Academic validation completed successfully.");

        // Step 6: Retrieve subject groups
        logger.debug("Loading available subject groups.");

        List<SubjectGroup> allGroups =
                groupRepository.findBySubjectIds(subjectIds);

        logger.debug("Retrieved {} subject groups.", allGroups.size());

        Map<Subject, List<SubjectGroup>> groupsBySubject =
                allGroups.stream()
                        .collect(Collectors.groupingBy(SubjectGroup::getSubject));

        logger.info(
                "Subject selection validation completed successfully. {} subjects are ready for schedule generation.",
                groupsBySubject.size());

        return SubjectCombinationOutcome.valid(groupsBySubject);
    }
}