package co.unicauca.edu.unisched.infrastructure.persistence.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

import co.unicauca.edu.unisched.domain.model.Career;

@Entity
@Table(name = "career")
public class CareerEntity {

    @Id
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @OneToMany(
    mappedBy = "career",
    cascade = CascadeType.ALL,
    orphanRemoval = true
    )
    private List<SubjectEntity> subjects = new ArrayList<>();

    public CareerEntity() {
    }

    public Long getId() {return id;}
    public String getName() {return name;}
    public List<SubjectEntity> getSubjects() {return subjects;}
    public void setId(Long id) {this.id = id;}
    public void setName(String name) {this.name = name;}
    public void setSubjects(List<SubjectEntity> subjects) {this.subjects = subjects;}

    public void addSubject(SubjectEntity subject) {
        subjects.add(subject);
        subject.setCareer(this);
    }
    public void removeSubject(SubjectEntity subject) {
        subjects.remove(subject);
        subject.setCareer(null);
    }
}