package co.unicauca.edu.unisched.infrastructure.persistence.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

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

    public SubjectEntity(){}

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
    public byte getNumSemester() {return numSemester;}
    public void setNumSemester(byte numSemester) {this.numSemester = numSemester;}
    public void setId(Long id) {this.id = id;}
    public Long getId() {return id;}
    public List<SubjectGroupEntity> getSubjectGroups() {return subjectGroups;}
    public void setSubjectGroups(List<SubjectGroupEntity> subjectGroups) {this.subjectGroups = subjectGroups;}
}
