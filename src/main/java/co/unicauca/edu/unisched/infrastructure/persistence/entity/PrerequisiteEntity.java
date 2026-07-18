package co.unicauca.edu.unisched.infrastructure.persistence.entity;

import jakarta.persistence.*;

@Entity
@Table(name="prerequisite")
public class PrerequisiteEntity {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name="subject_id")
    private SubjectEntity subject;

    @ManyToOne
    @JoinColumn(name="prerequisite_subject_id")
    private SubjectEntity prerequisite;

    public PrerequisiteEntity() {
    }

    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}
    public SubjectEntity getSubject() {return subject;}
    public void setSubject(SubjectEntity subject) {this.subject = subject;}
    public SubjectEntity getPrerequisite() {return prerequisite;}
    public void setPrerequisite(SubjectEntity prerequisite) {this.prerequisite = prerequisite;}
}