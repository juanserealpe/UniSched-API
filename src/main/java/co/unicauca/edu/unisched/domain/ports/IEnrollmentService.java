package co.unicauca.edu.unisched.domain.ports;

import co.unicauca.edu.unisched.domain.model.EnrollmentValidationResult;
import co.unicauca.edu.unisched.domain.model.SubjectGroup;
import co.unicauca.edu.unisched.domain.model.SubjectSelection;
import java.util.List;
import java.util.Map;

/**
 * Port for the enrollment service.
 * Orchestrates subject validation and retrieval of available groups.
 */
public interface IEnrollmentService {

    /**
     * Validates a subject selection and returns available groups if valid.
     *
     * @param selection the subject selection to validate
     * @return validation result with available groups
     */
    EnrollmentValidationResult validateAndGetGroups(SubjectSelection selection);
}
