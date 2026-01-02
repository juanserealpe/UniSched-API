package co.unicauca.edu.unisched.domain.model;

import java.util.Set;

/**
 * Represents the result of validating a subject selection.
 * Contains three sets: selected subjects, blocked subjects, and available subjects.
 */
public class ValidationResult {

    private final Set<Subject> selected;
    private final Set<Subject> blocked;
    private final Set<Subject> available;

    /**
     * Constructs a new ValidationResult with the specified sets.
     *
     * @param selected the set of selected subjects
     * @param blocked the set of blocked subjects (cannot be selected)
     * @param available the set of available subjects (can be selected)
     */
    public ValidationResult(Set<Subject> selected,
                            Set<Subject> blocked,
                            Set<Subject> available) {
        this.selected = selected;
        this.blocked = blocked;
        this.available = available;
    }

    /**
     * Returns the set of selected subjects.
     *
     * @return the selected subjects
     */
    public Set<Subject> getSelected() {
        return selected;
    }

    /**
     * Returns the set of blocked subjects.
     *
     * @return the blocked subjects
     */
    public Set<Subject> getBlocked() {
        return blocked;
    }

    /**
     * Returns the set of available subjects.
     *
     * @return the available subjects
     */
    public Set<Subject> getAvailable() {
        return available;
    }
}
