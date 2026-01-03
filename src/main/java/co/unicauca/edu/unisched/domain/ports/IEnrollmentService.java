package co.unicauca.edu.unisched.domain.ports;

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

    /**
     * Class representing the validation result with groups.
     */
    class EnrollmentValidationResult {
        private final boolean isValid;
        private final List<String> errors;
        private final Map<Long, List<SubjectGroup>> groupsBySubject; // subjectId -> List<SubjectGroup>

        public EnrollmentValidationResult(boolean isValid, List<String> errors,
                                          Map<Long, List<SubjectGroup>> groupsBySubject) {
            this.isValid = isValid;
            this.errors = errors;
            this.groupsBySubject = groupsBySubject;
        }

        public boolean isValid() {
            return isValid;
        }

        public List<String> getErrors() {
            return errors;
        }

        public Map<Long, List<SubjectGroup>> getGroupsBySubject() {
            return groupsBySubject;
        }
    }
}
