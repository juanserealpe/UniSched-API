package co.unicauca.edu.unisched.infrastructure.persistence.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
/**
 * JPA entity representing a subject group.
 *
 * A subject group corresponds to a specific offering of a subject
 * within an academic period. Each group defines:
 * <ul>
 *   <li>A unique group code</li>
 *   <li>The professors assigned to the group</li>
 *   <li>The academic period in which it is offered</li>
 *   <li>One or more schedule entries</li>
 * </ul>
 *
 * Each subject group is associated with exactly one {@link SubjectEntity}
 * and may contain multiple {@link ScheduleEntity} instances defining
 * its weekly timetable.
 *
 * This entity is part of the infrastructure persistence layer and
 * contains only JPA mapping configuration.
 */
@Entity
@Table(name = "subject_groups")
public class SubjectGroupEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "subject_id", nullable = false)
    private SubjectEntity subject;

    @Column(nullable = false)
    private String groupCode;

    @Column(nullable = false)
    private String professors;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "academic_period_id", nullable = false)
    private AcademicPeriodEntity academicPeriod;

    @OneToMany(mappedBy = "subjectGroup", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ScheduleEntity> schedules = new ArrayList<>();

    public SubjectGroupEntity() {
    }

    public AcademicPeriodEntity getAcademicPeriod() {
        return academicPeriod;
    }

    public void setAcademicPeriod(AcademicPeriodEntity academicPeriod) {
        this.academicPeriod = academicPeriod;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SubjectEntity getSubject() {
        return subject;
    }

    public void setSubject(SubjectEntity subject) {
        this.subject = subject;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    public String getProfessors() {
        return professors;
    }

    public void setProfessors(String professors) {
        this.professors = professors;
    }

    public List<ScheduleEntity> getSchedules() {
        return schedules;
    }

    public void setSchedules(List<ScheduleEntity> schedules) {
        this.schedules = schedules;
    }
}