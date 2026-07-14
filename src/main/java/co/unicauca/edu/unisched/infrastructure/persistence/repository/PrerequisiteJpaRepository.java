package co.unicauca.edu.unisched.infrastructure.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import co.unicauca.edu.unisched.infrastructure.persistence.entity.PrerequisiteEntity;

public interface PrerequisiteJpaRepository
        extends JpaRepository<PrerequisiteEntity, Long> {

    List<PrerequisiteEntity> findBySubjectCareerId(Long careerId);

}
