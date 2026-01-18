package co.unicauca.edu.unisched.domain.planner;

import co.unicauca.edu.unisched.domain.model.ValidationResult;
import co.unicauca.edu.unisched.domain.model.SubjectSelection;
import co.unicauca.edu.unisched.domain.model.Subject;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Validates subject selections and calculates which subjects are available,
 * blocked, or selected based on prerequisites and unlock relationships.
 */
public class SubjectValidator {

    private final Set<Subject> allSubjects;

    /**
     * Constructs a new SubjectValidator with the complete set of subjects.
     *
     * @param allSubjects the complete set of subjects in the study plan
     */
    public SubjectValidator(Set<Subject> allSubjects) {
        this.allSubjects = allSubjects;
    }

    /**
     * Recursively blocks all subjects that are unlocked by the given subject.
     * When a subject is selected, all subjects it unlocks become blocked.
     *
     * @param subject the subject whose descendants should be blocked
     * @param blocked the set to add blocked subjects to
     */
    private void blockDescendants(Subject subject, Set<Subject> blocked) {
        for (Subject child : subject.getUnlocks()) {
            if (blocked.add(child)) {
                blockDescendants(child, blocked);
            }
        }
    }

    /**
     * Collects all prerequisites for a given subject recursively.
     * A prerequisite is any subject that unlocks the given subject.
     *
     * @param subject the subject to find prerequisites for
     * @return a set of all prerequisite subjects
     */
    private Set<Subject> getAllPrerequisites(Subject subject) {
        Set<Subject> prerequisites = new HashSet<>();
        collectPrerequisites(subject, prerequisites);
        return prerequisites;
    }

    /**
     * Recursively collects all prerequisites for a subject.
     *
     * @param subject       the subject to collect prerequisites for
     * @param prerequisites the set to add prerequisites to
     */
    private void collectPrerequisites(Subject subject, Set<Subject> prerequisites) {
        for (Subject s : allSubjects) {
            if (s.getUnlocks().contains(subject)) {
                if (prerequisites.add(s)) {
                    collectPrerequisites(s, prerequisites);
                }
            }
        }
    }

    /**
     * Returns all subjects that are fulfilled (either selected or have all
     * prerequisites met).
     *
     * @param selected the set of selected subjects
     * @return a set of all fulfilled subjects (selected + their prerequisites)
     */
    private Set<Subject> getFulfilledSubjects(Set<Subject> selected) {
        Set<Subject> fulfilled = new HashSet<>(selected);
        for (Subject s : selected) {
            fulfilled.addAll(getAllPrerequisites(s));
        }
        return fulfilled;
    }

    /**
     * Checks if a subject has unmet prerequisites.
     *
     * @param subject   the subject to check
     * @param fulfilled the set of fulfilled subjects
     * @return true if the subject has unmet prerequisites, false otherwise
     */
    private boolean hasUnmetPrerequisites(Subject subject, Set<Subject> fulfilled) {
        for (Subject s : allSubjects) {
            if (s.getUnlocks().contains(subject)) {
                if (!fulfilled.contains(s)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Calculates which subjects should be blocked based on the selection.
     * Subjects are blocked if they are unlocked by any selected subject.
     *
     * @param selection the subject selection to validate
     * @return a set of blocked subjects
     */
    public Set<Subject> calculateBlocked(SubjectSelection selection) {
        Set<Subject> blocked = new HashSet<>();
        Set<Subject> selected = selection.getSelected();
        for (Subject subj : selected) {
            blockDescendants(subj, blocked);
        }
        blocked.removeAll(selected);
        return blocked;
    }

    /**
     * Calculates the complete validation result for a subject selection.
     * Determines which subjects are selected, blocked, or available based on
     * prerequisites, unlock relationships, and mandatory relationships.
     *
     * @param selection the subject selection to validate
     * @return a ValidationResult containing selected, blocked, and available
     *         subjects
     */
    public ValidationResult calculatePlan(SubjectSelection selection) {
        Set<Subject> selected = selection.getSelected();
        Set<Subject> blocked = calculateBlocked(selection);
        Set<Subject> fulfilled = getFulfilledSubjects(selected);
        Set<Subject> implicitPrereqs = new HashSet<>(fulfilled);
        implicitPrereqs.removeAll(selected);
        blocked.addAll(implicitPrereqs);
        Set<Subject> available = new HashSet<>(allSubjects);
        available.removeAll(blocked);
        available.removeAll(selected);
        Set<Subject> toBlock = new HashSet<>();

        // Block subjects whose mandatory partners are already selected
        // If A is selected and A.mandatoryWith(B), then B must be blocked (not
        // available)
        for (Subject selectedSubject : selected) {
            for (Subject mandatory : selectedSubject.getMandatoryWith()) {
                if (!selected.contains(mandatory)) {
                    toBlock.add(mandatory);
                }
            }
        }

        for (Subject subject : available) {
            for (Subject mandatory : subject.getMandatoryWith()) {
                if (blocked.contains(mandatory)) {
                    toBlock.add(subject);
                    break;
                }
                // If mandatory partner is selected, this subject must also be selected (blocked
                // from being available alone)
                if (selected.contains(mandatory)) {
                    toBlock.add(subject);
                    break;
                }
            }
            if (hasUnmetPrerequisites(subject, fulfilled))
                toBlock.add(subject);
        }

        available.removeAll(toBlock);
        blocked.addAll(toBlock);
        return new ValidationResult(selected, blocked, available);
    }

    /**
     * Validates if a combination of subjects is valid.
     * A combination is invalid if:
     * - A subject and its prerequisite are selected together (e.g., Cálculo I and
     * Cálculo II)
     * - A subject is selected without its prerequisites
     *
     * @param selection the subject selection to validate
     * @return true if the combination is valid, false otherwise
     */
    public boolean isValidCombination(SubjectSelection selection) {
        Set<Subject> selected = selection.getSelected();

        // Check if any selected subject has its prerequisite also selected
        for (Subject subject : selected) {
            Set<Subject> prerequisites = getAllPrerequisites(subject);
            for (Subject prerequisite : prerequisites) {
                if (selected.contains(prerequisite)) {
                    return false; // Invalid: subject and its prerequisite selected together
                }
            }
        }

        // Check if all prerequisites are met (implicitly checked above, but also check
        // if subject unlocks are selected)
        for (Subject subject : selected) {
            for (Subject unlocks : subject.getUnlocks()) {
                if (selected.contains(unlocks)) {
                    return false; // Invalid: subject and what it unlocks selected together
                }
            }
        }

        // Check if all prerequisites are fulfilled
        Set<Subject> fulfilled = getFulfilledSubjects(selected);
        for (Subject subject : selected) {
            if (hasUnmetPrerequisites(subject, fulfilled)) {
                return false; // Invalid: subject selected without prerequisites
            }
        }

        return true;
    }

    /**
     * Validates a combination and returns detailed error messages if invalid.
     *
     * @param selection the subject selection to validate
     * @return a list of error messages, empty if valid
     */
    public List<String> validateCombinationWithErrors(SubjectSelection selection) {
        List<String> errors = new ArrayList<>();
        Set<Subject> selected = selection.getSelected();
        Set<String> processedPairs = new HashSet<>();

        // Check if any selected subject has its prerequisite also selected
        // This covers both: subject with prerequisite, and subject with what it unlocks
        for (Subject subject : selected) {
            // Check prerequisites (subjects that unlock this one)
            Set<Subject> prerequisites = getAllPrerequisites(subject);
            for (Subject prerequisite : prerequisites) {
                if (selected.contains(prerequisite)) {
                    String pairKey = prerequisite.getId().compareTo(subject.getId()) < 0
                            ? prerequisite.getId() + "-" + subject.getId()
                            : subject.getId() + "-" + prerequisite.getId();
                    if (processedPairs.add(pairKey)) {
                        errors.add(String.format(
                                "Combinación inválida: '%s' y '%s' no pueden seleccionarse juntas (relación de prerequisito)",
                                prerequisite.getName(), subject.getName()));
                    }
                }
            }

            // Check what this subject unlocks
            for (Subject unlocks : subject.getUnlocks()) {
                if (selected.contains(unlocks)) {
                    String pairKey = subject.getId() + "-" + unlocks.getId();
                    if (processedPairs.add(pairKey)) {
                        errors.add(String.format(
                                "Combinación inválida: '%s' y '%s' no pueden seleccionarse juntas (relación de desbloqueo)",
                                subject.getName(), unlocks.getName()));
                    }
                }
            }
        }

        // Check if all prerequisites are fulfilled (subjects selected without their
        // prerequisites)
        Set<Subject> fulfilled = getFulfilledSubjects(selected);
        for (Subject subject : selected) {
            if (hasUnmetPrerequisites(subject, fulfilled)) {
                Set<Subject> missingPrereqs = new HashSet<>();
                for (Subject s : allSubjects) {
                    if (s.getUnlocks().contains(subject) && !fulfilled.contains(s)) {
                        missingPrereqs.add(s);
                    }
                }
                if (!missingPrereqs.isEmpty()) {
                    errors.add(String.format("'%s' no puede seleccionarse: faltan los prerrequisitos: %s",
                            subject.getName(),
                            missingPrereqs.stream()
                                    .map(Subject::getName)
                                    .collect(java.util.stream.Collectors.joining(", "))));
                }
            }
        }

        return errors;
    }
}