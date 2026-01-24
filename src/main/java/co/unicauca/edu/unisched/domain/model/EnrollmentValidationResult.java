package co.unicauca.edu.unisched.domain.model;

import java.util.List;
import java.util.Map;

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
