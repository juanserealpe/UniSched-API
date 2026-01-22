package co.unicauca.edu.unisched.infrastructure.persistence.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * JPA entity that represents an academic period
 * (year and semester) in the database.
 *
 * An academic period is a reference entity that groups
 * subject offerings (subject groups) for a specific
 * academic timeframe.
 */
@Entity
@Table(
        name = "academic_period",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"academic_year", "semester"}
        )
)
public class AcademicPeriodEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "academic_year", nullable = false)
    private Long year;

    @Column(nullable = false)
    private byte semester;

    @OneToMany(
            mappedBy = "academicPeriod",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<SubjectGroupEntity> subjectGroups = new ArrayList<>();

    public AcademicPeriodEntity() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getYear() {
        return year;
    }

    public void setYear(Long year) {
        this.year = year;
    }

    public byte getSemester() {
        return semester;
    }

    public void setSemester(byte semester) {
        this.semester = semester;
    }

    public List<SubjectGroupEntity> getSubjectGroups() {
        return subjectGroups;
    }

    public void setSubjectGroups(List<SubjectGroupEntity> subjectGroups) {
        this.subjectGroups = subjectGroups;
    }
}

