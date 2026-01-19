package co.unicauca.edu.unisched.infrastructure.persistence.adapter;

import co.unicauca.edu.unisched.domain.model.*;
import co.unicauca.edu.unisched.domain.ports.ISubjectGroupRepository;
import co.unicauca.edu.unisched.domain.ports.ISubjectRepository;
import co.unicauca.edu.unisched.infrastructure.persistence.entity.*;
import co.unicauca.edu.unisched.infrastructure.persistence.repository.SubjectGroupJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Adapter that implements the ISubjectGroupRepository port.
 * Converts between JPA entities and domain models.
 *
 * This adapter is part of the infrastructure layer and connects
 * the domain with JPA persistence.
 */
@Component
public class SubjectGroupRepositoryAdapter implements ISubjectGroupRepository {

    private final SubjectGroupJpaRepository jpaRepository;
    private final ISubjectRepository subjectRepository;

    public SubjectGroupRepositoryAdapter(
            SubjectGroupJpaRepository jpaRepository,
            ISubjectRepository subjectRepository) {
        this.jpaRepository = jpaRepository;
        this.subjectRepository = subjectRepository;
    }

    @Override
    public List<SubjectGroup> findBySubjectId(Long subjectId) {
        List<SubjectGroupEntity> entities = jpaRepository.findBySubjectIdWithDetails(subjectId);
        return entities.stream()
                .map(this::toDomainModel)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<SubjectGroup> findById(Long id) {
        return jpaRepository.findById(id)
                .map(this::toDomainModel);
    }

    @Override
    public List<SubjectGroup> findBySubjectIds(Set<Long> subjectIds) {
        List<SubjectGroupEntity> entities = jpaRepository.findBySubjectIdsWithDetails(subjectIds);
        return entities.stream()
                .map(this::toDomainModel)
                .collect(Collectors.toList());
    }

    @Override
    public SubjectGroup save(SubjectGroup subjectGroup) {
        SubjectGroupEntity entity = toEntity(subjectGroup);
        SubjectGroupEntity saved = jpaRepository.save(entity);
        return toDomainModel(saved);
    }

    /**
     * Converts a JPA entity to a domain model.
     * Retrieves the Subject from the curriculum repository (in-memory).
     *
     * @param entity the JPA entity
     * @return the domain model SubjectGroup
     */
    private SubjectGroup toDomainModel(SubjectGroupEntity entity) {
        // Retrieve the Subject from the curriculum (in-memory)
        Subject subject = subjectRepository.findById(entity.getSubjectId())
                .orElseThrow(() -> new IllegalStateException(
                        "Subject not found in study plan: " + entity.getSubjectId()));

        // Convert schedules
        List<Schedule> schedules = entity.getSchedules().stream()
                .map(this::toScheduleModel)
                .collect(Collectors.toList());

        return new SubjectGroup(
                entity.getId(),
                subject,
                entity.getGroupCode(),
                entity.getProfessors(),
                schedules);
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
     * Converts a domain model to a JPA entity.
     *
     * @param subjectGroup the domain model
     * @return the JPA entity
     */
    private SubjectGroupEntity toEntity(SubjectGroup subjectGroup) {
        SubjectGroupEntity entity = new SubjectGroupEntity();
        if (subjectGroup.getId() != null) {
            entity.setId(subjectGroup.getId());
        }
        entity.setSubjectId(subjectGroup.getSubject().getId());
        entity.setGroupCode(subjectGroup.getGroupCode());
        entity.setProfessors(subjectGroup.getProfessors());
        entity.setCapacity(30); // Default capacity
        entity.setEnrolled(0); // Default enrolled

        // Convert schedules
        List<ScheduleEntity> scheduleEntities = subjectGroup.getSchedules().stream()
                .map(schedule -> {
                    ScheduleEntity scheduleEntity = new ScheduleEntity();
                    scheduleEntity.setDayOfWeek(schedule.getDayOfWeek());
                    scheduleEntity.setStartTime(schedule.getStartTime());
                    scheduleEntity.setEndTime(schedule.getEndTime());
                    scheduleEntity.setSubjectGroup(entity);
                    return scheduleEntity;
                })
                .collect(Collectors.toList());

        entity.setSchedules(scheduleEntities);
        return entity;
    }
}
