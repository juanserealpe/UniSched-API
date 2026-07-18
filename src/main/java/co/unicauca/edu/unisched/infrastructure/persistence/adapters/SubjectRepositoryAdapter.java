package co.unicauca.edu.unisched.infrastructure.persistence.adapters;

import co.unicauca.edu.unisched.domain.model.Subject;
import co.unicauca.edu.unisched.domain.ports.schedules.ISubjectRepository;
import co.unicauca.edu.unisched.infrastructure.persistence.entity.SubjectEntity;
import co.unicauca.edu.unisched.infrastructure.persistence.repository.SubjectJpaRepository;
import co.unicauca.edu.unisched.mapper.subjects.CareerMapper;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Adapter that implements the ISubjectRepository port for JPA persistence.
 *
 * This adapter bridges the gap between domain Subject objects and
 * SubjectEntity JPA entities, handling all necessary conversions.
 */
@Component
public class SubjectRepositoryAdapter implements ISubjectRepository {

    private final SubjectJpaRepository subjectJpaRepository;
    private final CareerMapper careerMapper;

    public SubjectRepositoryAdapter(SubjectJpaRepository subjectJpaRepository, CareerMapper careerMapper) {
        this.subjectJpaRepository = subjectJpaRepository;
        this.careerMapper = careerMapper;
    }

    @Override
    public Set<Subject> findAll() {
        return subjectJpaRepository.findAll().stream()
                .map(this::toDomain)
                .collect(Collectors.toSet());
    }

    @Override
    public Optional<Subject> findById(Long id) {
        return subjectJpaRepository.findById(id)
                .map(this::toDomain);
    }

    @Override
    public Set<Subject> findByIds(Set<Long> ids) {
        return subjectJpaRepository.findAllById(ids).stream()
                .map(this::toDomain)
                .collect(Collectors.toSet());
    }

    @Override
    @Transactional
    public Subject save(Subject subject) {
        SubjectEntity entity = toEntity(subject);
        SubjectEntity saved = subjectJpaRepository.save(entity);
        return toDomain(saved);
    }

    /**
     * Converts a JPA entity to a domain model.
     * Note: This creates a simplified Subject without relationship data
     * to avoid circular dependencies. The full graph is maintained in
     * StudyPlanService.
     */
    private Subject toDomain(SubjectEntity entity) {
    return new Subject(
            entity.getId(),
            entity.getName(),
            entity.getNumSemester(),
            careerMapper.toDomain(entity.getCareer())
    );
}

    /**
     * Converts a domain Subject to a JPA entity.
     * Only persists basic attributes, not relationships.
     */
    private SubjectEntity toEntity(Subject subject) {
        SubjectEntity entity = new SubjectEntity();
        entity.setId(subject.getId());
        entity.setName(subject.getName());
        entity.setNumSemester(subject.getNumSemester());
        return entity;
    }
}