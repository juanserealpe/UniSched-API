package co.unicauca.edu.unisched.domain.model;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents a student's selection of subjects.
 * This class maintains the set of subjects that a student has selected.
 */
public class SubjectSelection {

    private final Set<Subject> selected = new HashSet<>();

    /**
     * Adds a subject to the selection.
     *
     * @param subject the subject to select
     */
    public void select(Subject subject) {selected.add(subject);}

    /**
     * Removes a subject from the selection.
     *
     * @param subject the subject to unselect
     */
    public void unselect(Subject subject) {selected.remove(subject);}

    /**
     * Returns the set of selected subjects.
     *
     * @return a set of all selected subjects
     */
    public Set<Subject> getSelected() {return selected;}

    /**
     * Checks if a subject is currently selected.
     *
     * @param subject the subject to check
     * @return true if the subject is selected, false otherwise
     */
    public boolean isSelected(Subject subject) {return selected.contains(subject);}
}
