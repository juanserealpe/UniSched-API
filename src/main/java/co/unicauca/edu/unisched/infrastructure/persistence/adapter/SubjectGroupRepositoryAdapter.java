package co.unicauca.edu.unisched.infrastructure.persistence.adapter;

import co.unicauca.edu.unisched.domain.model.*;
import co.unicauca.edu.unisched.domain.ports.ISubjectGroupRepository;
import co.unicauca.edu.unisched.infrastructure.persistence.entity.*;
import co.unicauca.edu.unisched.infrastructure.persistence.repository.SubjectGroupJpaRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * JPA-based adapter for managing {@link SubjectGroup} persistence.
 *
 * This class implements the {@link ISubjectGroupRepository} domain port and
 * acts as a bridge between the domain layer and the JPA persistence layer.
 *
 * Responsibilities:
 * <ul>
 *   <li>Retrieve subject groups and their related data from the database</li>
 *   <li>Convert JPA entities into domain models</li>
 *   <li>Persist domain models by converting them into JPA entities</li>
 * </ul>
 *
 * This adapter belongs to the infrastructure layer and isolates persistence
 * concerns from the domain and application layers.
 */
@Component
public class SubjectGroupRepositoryAdapter implements ISubjectGroupRepository {

    private final SubjectGroupJpaRepository subjectGroupJpaRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public SubjectGroupRepositoryAdapter(SubjectGroupJpaRepository subjectGroupJpaRepository) {
        this.subjectGroupJpaRepository = subjectGroupJpaRepository;
    }

    @Override
    public List<SubjectGroup> findBySubjectId(Long subjectId) {
        List<SubjectGroupEntity> entities = subjectGroupJpaRepository.findBySubjectIdWithDetails(subjectId);
        return entities.stream()
                .map(this::toDomainModel)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<SubjectGroup> findById(Long id) {
        return subjectGroupJpaRepository.findById(id)
                .map(this::toDomainModel);
    }

    @Override
    public List<SubjectGroup> findBySubjectIds(Set<Long> subjectIds) {
        List<SubjectGroupEntity> entities = subjectGroupJpaRepository.findBySubjectIdsWithDetails(subjectIds);
        return entities.stream()
                .map(this::toDomainModel)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public SubjectGroup save(SubjectGroup subjectGroup) {
        SubjectGroupEntity entity = toEntity(subjectGroup);
        SubjectGroupEntity saved = subjectGroupJpaRepository.save(entity);
        return toDomainModel(saved);
    }

    /**
     * Converts a JPA entity to a domain model.
     * Creates a simplified Subject (without relationships) from the entity.
     *
     * @param entity the JPA entity
     * @return the domain model SubjectGroup
     */
    private SubjectGroup toDomainModel(SubjectGroupEntity entity) {
        Subject subject = toSubjectDomain(entity.getSubject());

        List<Schedule> schedules = entity.getSchedules().stream()
                .map(this::toScheduleModel)
                .collect(Collectors.toList());

        AcademicPeriod academicPeriod = new AcademicPeriod(
                entity.getAcademicPeriod().getId(),
                entity.getAcademicPeriod().getYear(),
                entity.getAcademicPeriod().getSemester());

        return new SubjectGroup(
                entity.getId(),
                subject,
                entity.getGroupCode(),
                entity.getProfessors(),
                schedules,
                academicPeriod);
    }

    /**
     * Converts a schedule entity to a domain model.
     */
    private Schedule toScheduleModel(ScheduleEntity entity) {
        return new Schedule(
                entity.getDayOfWeek(),
                entity.getStartTime(),
                entity.getEndTime());
    }

    /**
     * Converts a subject entity to a domain model.
     * Creates a basic Subject without relationship data.
     */
    private Subject toSubjectDomain(SubjectEntity entity) {
        return new Subject(
                entity.getId(),
                entity.getName(),
                entity.getNumSemester());
    }

    /**
     * Converts a domain model to a JPA entity.
     * Uses EntityManager.getReference() to avoid unnecessary database queries
     * for Subject and AcademicPeriod.
     *
     * @param subjectGroup the domain model
     * @return the JPA entity
     */
    private SubjectGroupEntity toEntity(SubjectGroup subjectGroup) {
        SubjectGroupEntity entity;

        if (subjectGroup.getId() == null) {
            entity = new SubjectGroupEntity();
        } else {
            entity = subjectGroupJpaRepository.findById(subjectGroup.getId())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "SubjectGroup not found with ID: " + subjectGroup.getId()));
        }

        // Use getReference to create a proxy without querying the database
        // The subject must exist in DB (synchronized by StudyPlanService)
        SubjectEntity subjectRef = entityManager.getReference(
                SubjectEntity.class,
                subjectGroup.getSubject().getId());

        entity.setSubject(subjectRef);
        entity.setGroupCode(subjectGroup.getGroupCode());
        entity.setProfessors(subjectGroup.getProfessors());

        // Use getReference for AcademicPeriod as well
        AcademicPeriodEntity period = entityManager.getReference(
                AcademicPeriodEntity.class,
                subjectGroup.getAcademicPeriod().getId());

        entity.setAcademicPeriod(period);

        // Clear and rebuild schedules
        entity.getSchedules().clear();

        subjectGroup.getSchedules().forEach(schedule -> {
            ScheduleEntity se = new ScheduleEntity();
            se.setDayOfWeek(schedule.getDayOfWeek());
            se.setStartTime(schedule.getStartTime());
            se.setEndTime(schedule.getEndTime());
            se.setSubjectGroup(entity);
            entity.getSchedules().add(se);
        });

        return entity;
    }
}