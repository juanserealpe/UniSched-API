package co.unicauca.edu.unisched.infrastructure.persistence.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "subject_groups")
public class SubjectGroupEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Referencia al ID de la materia del plan (StudyPlanService)
    @Column(nullable = false)
    private Long subjectId;

    @Column(nullable = false)
    private String groupCode;

    @Column(nullable = false)
    private String professors;

    @Column(nullable = false)
    private int capacity;

    @Column(nullable = false)
    private int enrolled;

    @OneToMany(mappedBy = "subjectGroup", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ScheduleEntity> schedules = new ArrayList<>();

    // Constructores, getters, setters
    public SubjectGroupEntity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
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

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getEnrolled() {
        return enrolled;
    }

    public void setEnrolled(int enrolled) {
        this.enrolled = enrolled;
    }

    public List<ScheduleEntity> getSchedules() {
        return schedules;
    }

    public void setSchedules(List<ScheduleEntity> schedules) {
        this.schedules = schedules;
    }
}