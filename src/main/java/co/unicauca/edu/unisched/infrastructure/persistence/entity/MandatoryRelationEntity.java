package co.unicauca.edu.unisched.infrastructure.persistence.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "mandatory_relation")
public class MandatoryRelationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id", nullable = false)
    private SubjectEntity subject;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mandatory_subject_id", nullable = false)
    private SubjectEntity mandatorySubject;

    public MandatoryRelationEntity() {
    }

    public Long getId() {return id;}
    public SubjectEntity getSubject() {return subject;}
    public SubjectEntity getMandatorySubject() {return mandatorySubject;}
    public void setId(Long id) {this.id = id;}
    public void setSubject(SubjectEntity subject) {this.subject = subject;}
    public void setMandatorySubject(SubjectEntity mandatorySubject) {this.mandatorySubject = mandatorySubject;}
}