package co.unicauca.edu.unisched.infrastructure.persistence.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import co.unicauca.edu.unisched.infrastructure.persistence.entity.MandatoryRelationEntity;


public interface MandatoryRelationJpaRepository
        extends JpaRepository<MandatoryRelationEntity, Long> {

    List<MandatoryRelationEntity> findBySubjectCareerId(Long careerId);

}
