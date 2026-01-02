package co.unicauca.edu.unisched.domain.ports;

import co.unicauca.edu.unisched.domain.model.Subject;
import java.util.Optional;
import java.util.Set;

/**
 * Port for the subject repository.
 * Defines the interface that must be implemented by any persistence adapter.
 */
public interface ISubjectRepository {
    
    /**
     * Retrieves all subjects from the study plan.
     *
     * @return a set of all subjects
     */
    Set<Subject> findAll();
    
    /**
     * Finds a subject by its ID.
     *
     * @param id the ID of the subject
     * @return an Optional containing the subject if it exists
     */
    Optional<Subject> findById(Long id);
    
    /**
     * Finds subjects by their IDs.
     *
     * @param ids a set of subject IDs
     * @return a set of found subjects
     */
    Set<Subject> findByIds(Set<Long> ids);
}

