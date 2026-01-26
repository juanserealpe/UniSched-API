package co.unicauca.edu.unisched.domain.model;

/**
 * Domain model that represents an academic period.
 *
 * An academic period defines a specific timeframe in which
 * academic activities take place, usually identified by
 * a year and a semester (e.g. 2026-1 or 2026-2).
 *
 * This model is immutable and belongs purely to the domain
 * layer. It does not contain any persistence-related
 * annotations or framework dependencies.
 */
public class AcademicPeriod {
    private final Long id;
    private final Long year;
    private final byte semester;

    public AcademicPeriod(Long id, Long year, byte semester) {
        this.id = id;
        this.year = year;
        this.semester = semester;
    }

    public Long getId() {return id;}
    public Long getYear() {return year;}
    public byte getSemester() {return semester;}
}
