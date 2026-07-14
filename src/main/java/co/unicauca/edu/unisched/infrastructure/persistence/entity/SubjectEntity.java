package co.unicauca.edu.unisched.infrastructure.persistence.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
/**
 * JPA entity representing an academic subject.
 *
 * This entity stores the core information of a subject, including:
 * <ul>
 *   <li>Unique identifier</li>
 *   <li>Name of the subject</li>
 *   <li>Academic semester in which it is offered</li>
 * </ul>
 *
 * A subject can have multiple associated {@link SubjectGroupEntity}
 * instances, each representing a different group or schedule option.
 *
 * This class belongs to the infrastructure persistence layer and
 * contains only mapping information required by JPA.
 */
@Entity
@Table(name = "subject")
public class SubjectEntity {
    @Id
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private byte numSemester;

    @OneToMany(mappedBy = "subject", cascade = CascadeType.ALL)
    private List<SubjectGroupEntity> subjectGroups = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "career_id", nullable = false)
    private CareerEntity career;

    public SubjectEntity(){}

    public SubjectEntity(Long id, String name, byte numSemester, CareerEntity career) {
        this.id = id;
        this.name = name;
        this.numSemester = numSemester;
        this.career = career;
    }

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
    public byte getNumSemester() {return numSemester;}
    public void setNumSemester(byte numSemester) {this.numSemester = numSemester;}
    public void setId(Long id) {this.id = id;}
    public Long getId() {return id;}
    public List<SubjectGroupEntity> getSubjectGroups() {return subjectGroups;}
    public void setSubjectGroups(List<SubjectGroupEntity> subjectGroups) {this.subjectGroups = subjectGroups;}
    public CareerEntity getCareer() {return this.career;}
    public void setCareer(CareerEntity career) {this.career = career;}
}
