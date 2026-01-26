package co.unicauca.edu.unisched.infrastructure.persistence.entity;

import jakarta.persistence.*;
import java.time.DayOfWeek;
import java.time.LocalTime;
/**
 * JPA entity representing a schedule entry for a subject group.
 *
 * This entity models a single time slot in which a subject group
 * takes place, defined by:
 * <ul>
 *   <li>Day of the week</li>
 *   <li>Start time</li>
 *   <li>End time</li>
 * </ul>
 *
 * Each schedule entry belongs to exactly one {@link SubjectGroupEntity}
 * and is persisted as part of the subject group's schedule configuration.
 *
 * This class is part of the infrastructure persistence layer and
 * should not contain any business logic.
 */
@Entity
@Table(name = "schedules")
public class ScheduleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DayOfWeek dayOfWeek;

    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private LocalTime endTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_group_id")
    private SubjectGroupEntity subjectGroup;

    public ScheduleEntity() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public DayOfWeek getDayOfWeek() { return dayOfWeek; }
    public void setDayOfWeek(DayOfWeek dayOfWeek) { this.dayOfWeek = dayOfWeek; }
    public LocalTime getStartTime() { return startTime; }
    public void setStartTime(LocalTime startTime) { this.startTime = startTime; }
    public LocalTime getEndTime() { return endTime; }
    public void setEndTime(LocalTime endTime) { this.endTime = endTime; }
    public SubjectGroupEntity getSubjectGroup() { return subjectGroup; }
    public void setSubjectGroup(SubjectGroupEntity subjectGroup) { this.subjectGroup = subjectGroup; }
}
