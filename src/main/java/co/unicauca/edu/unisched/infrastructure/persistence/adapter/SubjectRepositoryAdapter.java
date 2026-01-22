package co.unicauca.edu.unisched.infrastructure.persistence.adapter;

import co.unicauca.edu.unisched.domain.model.AcademicPeriod;
import co.unicauca.edu.unisched.domain.model.Schedule;
import co.unicauca.edu.unisched.domain.model.Subject;
import co.unicauca.edu.unisched.domain.model.SubjectGroup;
import co.unicauca.edu.unisched.domain.ports.ISubjectGroupRepository;
import co.unicauca.edu.unisched.domain.ports.ISubjectRepository;
import co.unicauca.edu.unisched.infrastructure.persistence.entity.AcademicPeriodEntity;
import co.unicauca.edu.unisched.infrastructure.persistence.entity.ScheduleEntity;
import co.unicauca.edu.unisched.infrastructure.persistence.entity.SubjectGroupEntity;
import co.unicauca.edu.unisched.infrastructure.persistence.repository.SubjectGroupJpaRepository;
import co.unicauca.edu.unisched.infrastructure.persistence.repository.SubjectJpaRepository;
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
public class SubjectRepositoryAdapter implements ISubjectRepository {

    private SubjectJpaRepository subjectJpaRepository;

    public SubjectRepositoryAdapter(SubjectJpaRepository subjectJpaRepository) {
        this.subjectJpaRepository = subjectJpaRepository;
    }

    @Override
    public Set<Subject> findAll() {
        return Set.of();
    }

    @Override
    public Optional<Subject> findById(Long id) {
        return null;
    }

    @Override
    public Set<Subject> findByIds(Set<Long> ids) {
        return Set.of();
    }

    @Override
    public Subject save(Subject subject) {
        return null;
    }
}
