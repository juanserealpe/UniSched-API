package co.unicauca.edu.unisched.domain.ports.schedules;

import co.unicauca.edu.unisched.domain.model.AcademicPeriod;
import java.util.List;
import java.util.Optional;

/**
 * Domain port for managing academic periods.
 *
 * Defines the operations required by the domain layer to
 * retrieve and persist academic periods, without depending
 * on any specific persistence technology.
 */
public interface IAcademicPeriodRepository {

    /**
     * Finds an academic period by its year and semester.
     *
     * @param year academic year (e.g. 2026)
     * @param semester academic semester (e.g. 1 or 2)
     * @return optional academic period if it exists
     */
    Optional<AcademicPeriod> findByYearAndSemester(Long year, byte semester);

    /**
     * Finds an academic period by its unique identifier.
     *
     * @param id academic period ID
     * @return optional academic period
     */
    Optional<AcademicPeriod> findById(Long id);

    /**
     * Retrieves all academic periods stored in the system.
     *
     * @return list of academic periods
     */
    List<AcademicPeriod> findAll();

    /**
     * Persists a new academic period or updates an existing one.
     *
     * @param academicPeriod academic period to save
     * @return saved academic period with generated ID
     */
    AcademicPeriod save(AcademicPeriod academicPeriod);

    /**
     * Checks whether an academic period already exists
     * for a given year and semester.
     *
     * @param year academic year
     * @param semester academic semester
     * @return true if the period exists, false otherwise
     */
    boolean existsByYearAndSemester(Long year, byte semester);
}
