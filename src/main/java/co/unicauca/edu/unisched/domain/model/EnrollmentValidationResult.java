package co.unicauca.edu.unisched.domain.model;

import java.util.List;
import java.util.Map;
/**
 * Represents the result of an enrollment validation process.
 *
 * This domain model encapsulates the outcome of validating a student's
 * subject enrollment selection. It provides:
 * <ul>
 *   <li>Whether the enrollment is valid or not</li>
 *   <li>A list of validation errors explaining why the enrollment failed</li>
 *   <li>The available subject groups for each subject involved in the validation</li>
 * </ul>
 *
 * This object is typically produced by domain services or use cases
 * responsible for enforcing academic rules such as schedule compatibility,
 * group availability, and exclusion constraints.
 *
 * It is a pure domain object and does not depend on any framework or
 * infrastructure-specific concerns.
 */
public class EnrollmentValidationResult {
    private final boolean isValid;
    private final List<String> errors;
    private final Map<Long, List<SubjectGroup>> groupsBySubject; // subjectId -> List<SubjectGroup>

    public EnrollmentValidationResult(boolean isValid, List<String> errors,
                                      Map<Long, List<SubjectGroup>> groupsBySubject) {
        this.isValid = isValid;
        this.errors = errors;
        this.groupsBySubject = groupsBySubject;
    }

    public boolean isValid() {return isValid;}
    public List<String> getErrors() {return errors;}
    public Map<Long, List<SubjectGroup>> getGroupsBySubject() {return groupsBySubject;}
}
