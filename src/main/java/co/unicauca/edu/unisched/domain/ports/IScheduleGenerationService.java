package co.unicauca.edu.unisched.domain.ports;

import co.unicauca.edu.unisched.domain.model.Subject;
import co.unicauca.edu.unisched.domain.model.SubjectGroup;
import java.util.List;
import java.util.Map;

/**
 * Domain port that defines the contract for generating academic schedules.
 *
 * This interface represents a use case responsible for producing all valid
 * schedule combinations based on a set of subjects and their available groups.
 * A valid schedule is one in which:
 * <ul>
 *   <li>Exactly one group is selected per subject</li>
 *   <li>No time conflicts exist between selected groups</li>
 * </ul>
 *
 * Implementations are expected to use a combinatorial approach (e.g. backtracking)
 * to explore all possible combinations while pruning invalid ones early.
 *
 * This port belongs to the domain layer and must not depend on any framework
 * or infrastructure-specific concerns.
 */
public interface IScheduleGenerationService {

    /**
     * Generates all valid schedules from the given subject-group mapping.
     *
     * @param groupsBySubject a map where each subject is associated with its
     *                        available groups
     * @return a list of schedules, where each schedule is a list of
     *         {@link SubjectGroup} instances with no schedule conflicts
     */
    List<List<SubjectGroup>> generateAllValidSchedules(
            Map<Subject, List<SubjectGroup>> groupsBySubject
    );
}
