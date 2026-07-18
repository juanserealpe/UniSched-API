package co.unicauca.edu.unisched.domain.ports.schedules;

import co.unicauca.edu.unisched.domain.model.SubjectGroup;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Port for the subject group repository.
 * Defines operations to access the academic offering (available groups).
 */
public interface ISubjectGroupRepository {

    /**
     * Finds all available groups for a specific subject.
     *
     * @param subjectId the ID of the subject in the curriculum
     * @return list of available groups for that subject
     */
    List<SubjectGroup> findBySubjectId(Long subjectId);

    /**
     * Finds a specific group by its ID.
     *
     * @param id the ID of the group
     * @return an Optional containing the group if it exists
     */
    Optional<SubjectGroup> findById(Long id);

    /**
     * Finds all groups for a set of subject IDs.
     *
     * @param subjectIds set of subject IDs
     * @return list of all available groups for those subjects
     */
    List<SubjectGroup> findBySubjectIds(Set<Long> subjectIds);

    /**
     * Saves a new subject group.
     *
     * @param subjectGroup the group to save
     * @return the saved group with its generated ID
     */
    SubjectGroup save(SubjectGroup subjectGroup);
}
