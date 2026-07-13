package co.unicauca.edu.unisched.application.usecases.schedules;

import co.unicauca.edu.unisched.domain.model.Subject;
import co.unicauca.edu.unisched.domain.model.SubjectGroup;
import co.unicauca.edu.unisched.domain.ports.schedules.IScheduleGenerationService;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Application service that implements the schedule generation use case.
 *
 * This class is responsible for generating all valid academic schedules
 * for a given set of subjects and their available groups.
 *
 * The algorithm uses a backtracking approach to explore all possible
 * combinations, selecting exactly one group per subject while ensuring
 * that no time conflicts exist between the selected groups.
 *
 * To improve performance, subjects are processed in ascending order
 * based on the number of available groups (smallest domain first),
 * reducing the search space early (heuristic optimization).
 */
@Service
public class ScheduleGenerationUseCase implements IScheduleGenerationService {

    /**
     * Generates all valid schedules from the given subject-group mapping.
     *
     * Each resulting schedule:
     * <ul>
     *   <li>Contains exactly one {@link SubjectGroup} per {@link Subject}</li>
     *   <li>Has no overlapping time slots between groups</li>
     * </ul>
     *
     * @param groupsBySubject a map where each subject is associated with its
     *                        available groups
     * @return a list of valid schedules, where each schedule is represented
     *         as a list of {@link SubjectGroup}
     */
    @Override
    public List<List<SubjectGroup>> generateAllValidSchedules(
            Map<Subject, List<SubjectGroup>> groupsBySubject) {

        List<List<SubjectGroup>> solutions = new ArrayList<>();

        // Sort subjects by number of available groups (heuristic: smallest first)
        List<Subject> subjects = new ArrayList<>(groupsBySubject.keySet());
        subjects.sort(Comparator.comparingInt(
                s -> groupsBySubject.get(s).size()
        ));

        backtrack(0, subjects, groupsBySubject,
                new ArrayList<>(), solutions);

        return solutions;
    }

    /**
     * Recursive backtracking algorithm that builds valid schedules incrementally.
     *
     * At each recursion level, one subject is processed and each of its groups
     * is tested for compatibility with the current partial schedule.
     * If a group is compatible, it is added and the algorithm proceeds
     * to the next subject.
     *
     * When all subjects have been processed, a complete and valid schedule
     * is produced and added to the solutions list.
     *
     * @param index the current subject index being processed
     * @param subjects the ordered list of subjects to schedule
     * @param groupsBySubject mapping of subjects to their available groups
     * @param current the current partial schedule being built
     * @param solutions the list where valid schedules are collected
     */
    private void backtrack(
            int index,
            List<Subject> subjects,
            Map<Subject, List<SubjectGroup>> groupsBySubject,
            List<SubjectGroup> current,
            List<List<SubjectGroup>> solutions) {

        if (index == subjects.size()) {
            // A complete and valid schedule has been built
            solutions.add(new ArrayList<>(current));
            return;
        }

        Subject subject = subjects.get(index);

        for (SubjectGroup group : groupsBySubject.get(subject)) {

            if (isCompatible(group, current)) {
                current.add(group);
                backtrack(index + 1, subjects, groupsBySubject, current, solutions);
                current.remove(current.size() - 1); // BACKTRACK
            }
        }
    }

    /**
     * Checks whether a candidate group can be added to the current schedule
     * without causing time conflicts.
     *
     * @param candidate the group being evaluated
     * @param selected the list of already selected groups
     * @return true if the candidate has no schedule conflicts with
     *         the selected groups, false otherwise
     */
    private boolean isCompatible(
            SubjectGroup candidate,
            List<SubjectGroup> selected) {

        for (SubjectGroup group : selected) {
            if (candidate.hasScheduleConflictWith(group)) {
                return false;
            }
        }
        return true;
    }
}
