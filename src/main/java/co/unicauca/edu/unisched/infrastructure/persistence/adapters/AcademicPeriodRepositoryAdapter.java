package co.unicauca.edu.unisched.infrastructure.persistence.adapters;

import co.unicauca.edu.unisched.domain.model.AcademicPeriod;
import co.unicauca.edu.unisched.domain.ports.schedules.IAcademicPeriodRepository;
import co.unicauca.edu.unisched.infrastructure.persistence.entity.AcademicPeriodEntity;
import co.unicauca.edu.unisched.infrastructure.persistence.repository.AcademicPeriodJpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 * Persistence adapter that implements the academic period
 * domain repository using Spring Data JPA.
 *
 * This class acts as a bridge between the domain model
 * (AcademicPeriod) and the JPA entity (AcademicPeriodEntity),
 * handling all required mappings.
 */
@Repository
public class AcademicPeriodRepositoryAdapter
        implements IAcademicPeriodRepository {

    private final AcademicPeriodJpaRepository jpaRepository;

    public AcademicPeriodRepositoryAdapter(
            AcademicPeriodJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    /**
     * Finds an academic period by year and semester
     * using the underlying JPA repository.
     */
    @Override
    public Optional<AcademicPeriod> findByYearAndSemester(Long year, byte semester) {
        return jpaRepository
                .findByYearAndSemester(year, semester)
                .map(this::toDomain);
    }

    /**
     * Finds an academic period by its ID.
     */
    @Override
    public Optional<AcademicPeriod> findById(Long id) {
        return jpaRepository.findById(id)
                .map(this::toDomain);
    }

    /**
     * Retrieves all academic periods from the database.
     */
    @Override
    public List<AcademicPeriod> findAll() {
        return jpaRepository.findAll()
                .stream()
                .map(this::toDomain)
                .toList();
    }

    /**
     * Saves an academic period in the database.
     * Handles both creation and update operations.
     */
    @Override
    public AcademicPeriod save(AcademicPeriod academicPeriod) {
        AcademicPeriodEntity saved =
                jpaRepository.save(toEntity(academicPeriod));
        return toDomain(saved);
    }

    /**
     * Checks if an academic period exists for a given
     * year and semester.
     */
    @Override
    public boolean existsByYearAndSemester(Long year, byte semester) {
        return jpaRepository.existsByYearAndSemester(year, semester);
    }

    private AcademicPeriod toDomain(AcademicPeriodEntity e) {
        return new AcademicPeriod(
                e.getId(),
                e.getYear(),
                e.getSemester()
        );
    }

    private AcademicPeriodEntity toEntity(AcademicPeriod d) {
        AcademicPeriodEntity e = new AcademicPeriodEntity();
        e.setId(d.getId());
        e.setYear(d.getYear());
        e.setSemester(d.getSemester());
        return e;
    }
}
