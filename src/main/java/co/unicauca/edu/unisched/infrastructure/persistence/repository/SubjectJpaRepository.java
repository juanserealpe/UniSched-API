package co.unicauca.edu.unisched.infrastructure.persistence.repository;

import co.unicauca.edu.unisched.infrastructure.persistence.entity.SubjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for Subject entities.
 * Provides database access methods for subjects and their related data.
 */
@Repository
public interface SubjectJpaRepository extends JpaRepository<SubjectEntity, Long> {

        /**
         * Retrieves all subjects that have groups in a given academic period.
         */
        @Query("""
        SELECT DISTINCT s
        FROM SubjectEntity s
        JOIN s.subjectGroups g
        JOIN g.academicPeriod p
        WHERE p.year = :year
          AND p.semester = :semester
    """)
        List<SubjectEntity> findAllByAcademicPeriod(
                @Param("year") Long year,
                @Param("semester") byte semester
        );

        /**
         * Retrieves a subject with its groups and schedules for a specific academic period.
         */
        @Query("""
        SELECT DISTINCT s
        FROM SubjectEntity s
        LEFT JOIN FETCH s.subjectGroups g
        LEFT JOIN FETCH g.schedules
        LEFT JOIN FETCH g.academicPeriod p
        WHERE s.id = :subjectId
          AND p.year = :year
          AND p.semester = :semester
    """)
        Optional<SubjectEntity> findByIdAndAcademicPeriod(
                @Param("subjectId") Long subjectId,
                @Param("year") Long year,
                @Param("semester") byte semester
        );

        List<SubjectEntity> findByCareerId(Long careerId);
}

