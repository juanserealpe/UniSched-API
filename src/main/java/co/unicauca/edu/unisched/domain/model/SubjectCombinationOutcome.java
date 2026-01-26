package co.unicauca.edu.unisched.domain.model;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Domain object representing the outcome of a subject combination request.
 * Contains validation status, errors, and the resulting grouped subjects.
 */
public class SubjectCombinationOutcome {
    private final boolean valid;
    private final List<String> errors;
    private final Map<Subject, List<SubjectGroup>> groupsBySubject;

    private SubjectCombinationOutcome(boolean valid, List<String> errors,
            Map<Subject, List<SubjectGroup>> groupsBySubject) {
        this.valid = valid;
        this.errors = errors != null ? errors : Collections.emptyList();
        this.groupsBySubject = groupsBySubject != null ? groupsBySubject : Collections.emptyMap();
    }

    public static SubjectCombinationOutcome valid(Map<Subject, List<SubjectGroup>> groupsBySubject) {
        return new SubjectCombinationOutcome(true, Collections.emptyList(), groupsBySubject);
    }

    public static SubjectCombinationOutcome invalid(List<String> errors) {
        return new SubjectCombinationOutcome(false, errors, Collections.emptyMap());
    }

    public boolean isValid() {
        return valid;
    }

    public List<String> getErrors() {
        return errors;
    }

    public Map<Subject, List<SubjectGroup>> getGroupsBySubject() {
        return groupsBySubject;
    }
}
