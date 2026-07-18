package co.unicauca.edu.unisched.infrastructure.persistence.repository;

import co.unicauca.edu.unisched.infrastructure.persistence.entity.SubjectGroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Set;

/**
 * Spring Data JPA repository for subject groups.
 * Provides database access using JPA.
 */
@Repository
public interface SubjectGroupJpaRepository extends JpaRepository<SubjectGroupEntity, Long> {

        /**
         * Finds all groups of a specific subject.
         * Fetches schedules eagerly to avoid N+1 queries.
         *
         * @param subjectId the ID of the subject
         * @return list of subject groups for the given subject
         */
        @Query("""
           SELECT DISTINCT sg
           FROM SubjectGroupEntity sg
           JOIN FETCH sg.subject s
           LEFT JOIN FETCH sg.schedules
           WHERE s.id = :subjectId
           """)
        List<SubjectGroupEntity> findBySubjectIdWithDetails(@Param("subjectId") Long subjectId);

        /**
         * Finds all groups for a set of subject IDs.
         * Uses eager fetching to optimize performance.
         *
         * @param subjectIds set of subject IDs
         * @return list of subject groups
         */
        @Query("""
           SELECT DISTINCT sg
           FROM SubjectGroupEntity sg
           JOIN FETCH sg.subject s
           LEFT JOIN FETCH sg.schedules
           WHERE s.id IN :subjectIds
           """)
        List<SubjectGroupEntity> findBySubjectIdsWithDetails(@Param("subjectIds") Set<Long> subjectIds);
}
