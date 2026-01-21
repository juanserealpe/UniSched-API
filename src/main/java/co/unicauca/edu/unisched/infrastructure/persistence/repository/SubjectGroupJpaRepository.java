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
         * Includes eager loading of professors and schedules to avoid N+1 queries.
         *
         * @param subjectId the ID of the subject
         * @return list of groups for that subject
         */
        @Query("SELECT DISTINCT sg FROM SubjectGroupEntity sg " +
                        "LEFT JOIN FETCH sg.schedules " +
                        "WHERE sg.subjectId = :subjectId")
        List<SubjectGroupEntity> findBySubjectIdWithDetails(@Param("subjectId") Long subjectId);

        /**
         * Finds all groups for a set of subject IDs.
         * Uses eager loading to optimize the query.
         *
         * @param subjectIds set of subject IDs
         * @return list of groups
         */
        @Query("SELECT DISTINCT sg FROM SubjectGroupEntity sg " +
                        "LEFT JOIN FETCH sg.schedules " +
                        "WHERE sg.subjectId IN :subjectIds")
        List<SubjectGroupEntity> findBySubjectIdsWithDetails(@Param("subjectIds") Set<Long> subjectIds);
}
