package co.unicauca.edu.unisched.domain.model;

import java.util.List;

/**
 * Represents a specific group of a subject in the curriculum.
 * For example: Calculus I can have multiple groups (A1, A2, B1) with different schedules.
 *
 * This is the concept of "academic offering" – concrete instances of subjects
 * with specific professors, schedules, and capacity.
 */
public class SubjectGroup {
    private final Long id;
    private final Subject subject;
    private final String groupCode;
    private final String professors;
    private final List<Schedule> schedules;
    private final AcademicPeriod academicPeriod;

    public SubjectGroup(Long id, Subject subject, String groupCode, String professors, List<Schedule> schedules, AcademicPeriod academicPeriod) {
        this.id = id;
        this.subject = subject;
        this.groupCode = groupCode;
        this.professors = professors;
        this.schedules = schedules;
        this.academicPeriod = academicPeriod;
    }

    public SubjectGroup(Long id, Subject subject, String groupCode, String professors, List<Schedule> schedules) {
        this.id = id;
        this.subject = subject;
        this.groupCode = groupCode;
        this.professors = professors;
        this.schedules = schedules;
        this.academicPeriod = null;
    }

    /**
     * Checks if the schedules of this group overlap with those of another group.
     *
     * @param other the other group to compare
     * @return true if there is a schedule conflict, false otherwise
     */
    public boolean hasScheduleConflictWith(SubjectGroup other) {
        for (Schedule thisSchedule : this.schedules) {
            for (Schedule otherSchedule : other.schedules)
                if (thisSchedule.overlapsWith(otherSchedule)) return true;
        }
        return false;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public String getProfessors() {
        return professors;
    }

    public List<Schedule> getSchedules() {
        return schedules;
    }

    public Long getId() {
        return id;
    }

    public Subject getSubject() {
        return subject;
    }

    public AcademicPeriod getAcademicPeriod() {
        return academicPeriod;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof SubjectGroup)) return false;
        return id.equals(((SubjectGroup) o).id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
