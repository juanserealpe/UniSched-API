package co.unicauca.edu.unisched.domain.ports.schedules;

import co.unicauca.edu.unisched.domain.model.SubjectSelection;
import java.util.List;

/**
 * Port for the subject selection validation service.
 * Defines the interface for the validation use case.
 */
public interface ISubjectValidationService {
    
    /**
     * Validates if a combination of subjects is valid.
     * A combination is invalid if subjects conflict with each other
     * (e.g., a subject and its prerequisite selected together).
     * 
     * @param selection the subject selection to validate
     * @return true if the combination is valid, false otherwise
     */
    boolean isValidCombination(SubjectSelection selection);
    
    /**
     * Validates a combination and returns detailed error messages if invalid.
     *
     * @param selection the subject selection to validate
     * @return a list of error messages, empty if valid
     */
    List<String> validateCombinationWithErrors(SubjectSelection selection);
}

