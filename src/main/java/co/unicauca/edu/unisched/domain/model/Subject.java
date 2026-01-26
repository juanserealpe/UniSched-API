package co.unicauca.edu.unisched.domain.model;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents a subject in the study plan.
 * A subject has prerequisites (subjects that unlock it) and can unlock other
 * subjects.
 * Subjects can also be mandatory with other subjects (must be taken together).
 */
public class Subject {
    private final Long id;
    private final String name;
    private final byte numSemester;
    private final Set<Subject> unlocks = new HashSet<>();
    private final Set<Subject> mandatoryWith = new HashSet<>();

    public Subject(Long id, String name, byte numSemester) {
        this.id = id;
        this.name = name;
        this.numSemester = numSemester;
    }

    /**
     * Establishes that this subject unlocks the specified subject.
     * This means the specified subject becomes available after completing this one.
     *
     * @param subject the subject that this subject unlocks
     */
    public void unlock(Subject subject) {unlocks.add(subject);}

    /**
     * Establishes that this subject is mandatory with the specified subject.
     * This creates a bidirectional relationship, meaning both subjects must be
     * taken together.
     *
     * @param subject the subject that must be taken together with this one
     */
    public void mandatoryWith(Subject subject) {
        mandatoryWith.add(subject);
        subject.mandatoryWith.add(this);
    }

    public Set<Subject> getUnlocks() {return unlocks;}
    public Set<Subject> getMandatoryWith() {return mandatoryWith;}
    public String getName() {return name;}
    public Long getId() {return id;}
    public byte getNumSemester() {return numSemester;}

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Subject)) return false;
        return id.equals(((Subject) o).id);
    }

    @Override
    public int hashCode() {return id.hashCode();}
}
