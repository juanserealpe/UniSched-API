package co.unicauca.edu.unisched.infrastructure.persistence.repository;

import co.unicauca.edu.unisched.infrastructure.persistence.entity.AcademicPeriodEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * Spring Data JPA repository for AcademicPeriodEntity.
 *
 * Provides database access methods for academic periods,
 * leveraging Spring Data query derivation.
 */
@Repository
public interface AcademicPeriodJpaRepository
        extends JpaRepository<AcademicPeriodEntity, Long> {

        /**
         * Finds an academic period by year and semester.
         *
         * @param year academic year
         * @param semester academic semester
         * @return optional academic period entity
         */
        Optional<AcademicPeriodEntity> findByYearAndSemester(Long year, byte semester);

        /**
         * Checks if an academic period exists for the given
         * year and semester.
         *
         * @param year academic year
         * @param semester academic semester
         * @return true if it exists, false otherwise
         */
        boolean existsByYearAndSemester(Long year, byte semester);
}
