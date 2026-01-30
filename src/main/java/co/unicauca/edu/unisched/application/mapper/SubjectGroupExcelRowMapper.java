package co.unicauca.edu.unisched.application.mapper;

import co.unicauca.edu.unisched.domain.model.AcademicPeriod;
import co.unicauca.edu.unisched.domain.model.ExcelRow;
import co.unicauca.edu.unisched.domain.model.Schedule;
import co.unicauca.edu.unisched.domain.model.Subject;
import co.unicauca.edu.unisched.domain.model.SubjectGroup;
import co.unicauca.edu.unisched.domain.ports.ExcelRowMapperPort;
import co.unicauca.edu.unisched.domain.ports.IAcademicPeriodRepository;
import co.unicauca.edu.unisched.domain.ports.ISubjectRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Maps an {@link ExcelRow} into a {@link SubjectGroup} domain entity.
 *
 * This mapper transforms Excel data containing academic schedules into
 * strongly-typed SubjectGroup objects with their associated schedules.
 *
 * Expected Excel columns:
 * <ul>
 *   <li><b>OIDMATERIA</b> – ID of the subject (foreign key to Subject)</li>
 *   <li><b>GRUPO</b> – Group code (e.g., "A", "B1", "C")</li>
 *   <li><b>LUNES</b> – Monday schedule (format: "HH:mm-HH:mm" or empty)</li>
 *   <li><b>MARTES</b> – Tuesday schedule</li>
 *   <li><b>MIERCOLES</b> – Wednesday schedule</li>
 *   <li><b>JUEVES</b> – Thursday schedule</li>
 *   <li><b>VIERNES</b> – Friday schedule</li>
 *   <li><b>DOCENTES</b> – Professor names (comma-separated)</li>
 * </ul>
 *
 * The mapper performs:
 * <ul>
 *   <li>Subject lookup and validation</li>
 *   <li>Schedule parsing from time range strings</li>
 *   <li>Academic period association</li>
 *   <li>Meaningful error reporting with row context</li>
 * </ul>
 */
@Component
public class SubjectGroupExcelRowMapper implements ExcelRowMapperPort<SubjectGroup> {

    private static final String COL_SUBJECT_ID = "OIDMATERIA";
    private static final String COL_GROUP_CODE = "GRUPO";
    private static final String COL_MONDAY = "LUNES";
    private static final String COL_TUESDAY = "MARTES";
    private static final String COL_WEDNESDAY = "MIERCOLES";
    private static final String COL_THURSDAY = "JUEVES";
    private static final String COL_FRIDAY = "VIERNES";
    private static final String COL_PROFESSORS = "DOCENTES";
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("H:mm");

    private final ISubjectRepository subjectRepository;
    private final IAcademicPeriodRepository academicPeriodRepository;

    public SubjectGroupExcelRowMapper(
            @Qualifier("studyPlanService") ISubjectRepository subjectRepository,
            IAcademicPeriodRepository academicPeriodRepository) {
        this.subjectRepository = subjectRepository;
        this.academicPeriodRepository = academicPeriodRepository;
    }

    /**
     * Maps an Excel row to a SubjectGroup domain entity.
     *
     * @param row Excel row containing subject group data
     * @param academicYear Academic year (e.g., 2026)
     * @param academicSemester Academic semester (1 or 2)
     * @return Mapped SubjectGroup entity
     */
    public SubjectGroup map(ExcelRow row, Long academicYear, byte academicSemester) {
        // 1. Obtener y validar la materia
        Long subjectId = parseLong(getRequiredValue(row, COL_SUBJECT_ID), row);
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("Subject with ID %d not found at row %d",
                                subjectId, row.getRowNumber())));


        String groupCode = getRequiredValue(row, COL_GROUP_CODE);
        String professors = row.get(COL_PROFESSORS);
        if (professors == null || professors.trim().isEmpty()) {
            professors = "Sin especificar";
        }

        List<Schedule> schedules = new ArrayList<>();
        parseAndAddSchedule(row, COL_MONDAY, DayOfWeek.MONDAY, schedules);
        parseAndAddSchedule(row, COL_TUESDAY, DayOfWeek.TUESDAY, schedules);
        parseAndAddSchedule(row, COL_WEDNESDAY, DayOfWeek.WEDNESDAY, schedules);
        parseAndAddSchedule(row, COL_THURSDAY, DayOfWeek.THURSDAY, schedules);
        parseAndAddSchedule(row, COL_FRIDAY, DayOfWeek.FRIDAY, schedules);

        AcademicPeriod academicPeriod = academicPeriodRepository
                .findByYearAndSemester(academicYear, academicSemester)
                .orElseGet(() -> academicPeriodRepository.save(
                        new AcademicPeriod(null, academicYear, academicSemester)));

        // 6. Crear el SubjectGroup (sin ID, se generará al guardar)
        return new SubjectGroup(
                null,
                subject,
                groupCode,
                professors,
                schedules,
                academicPeriod
        );
    }

    @Override
    public SubjectGroup map(ExcelRow row) {
        throw new UnsupportedOperationException(
                "Use map(ExcelRow, Long, byte) instead to specify academic period");
    }

    /**
     * Parses a schedule string and adds it to the schedules list if valid.
     *
     * Expected format: "HH:mm-HH:mm SALON XX" (e.g., "7:00-9:00 SALÓN 11", "14:00-16:00 SALÓN 4")
     * The classroom information after the time is ignored.
     * If the cell is empty or null, no schedule is added for that day.
     *
     * @param row       Excel row being processed
     * @param columnName Name of the day column
     * @param dayOfWeek Day of the week enum
     * @param schedules List to add the parsed schedule to
     */
    private void parseAndAddSchedule(ExcelRow row, String columnName,
                                     DayOfWeek dayOfWeek, List<Schedule> schedules) {
        String scheduleStr = row.get(columnName);

        // Si la celda está vacía, no hay clase ese día
        if (scheduleStr == null || scheduleStr.trim().isEmpty()) {
            return;
        }

        scheduleStr = scheduleStr.trim();

        String timeOnlyStr = scheduleStr;
        int spaceIndex = scheduleStr.indexOf(' ');
        if (spaceIndex != -1) {
            timeOnlyStr = scheduleStr.substring(0, spaceIndex).trim();
        }

        if (!timeOnlyStr.contains("-")) {
            throw new IllegalArgumentException(
                    String.format("Invalid schedule format '%s' for %s at row %d. Expected format: 'HH:mm-HH:mm'",
                            scheduleStr, columnName, row.getRowNumber()));
        }

        try {
            String[] parts = timeOnlyStr.split("-");
            if (parts.length != 2) {
                throw new IllegalArgumentException(
                        String.format("Invalid schedule format '%s' for %s at row %d",
                                scheduleStr, columnName, row.getRowNumber()));
            }

            LocalTime startTime = LocalTime.parse(parts[0].trim(), TIME_FORMATTER);
            LocalTime endTime = LocalTime.parse(parts[1].trim(), TIME_FORMATTER);

            // Validar que la hora de inicio sea antes que la de fin
            if (!startTime.isBefore(endTime)) {
                throw new IllegalArgumentException(
                        String.format("Start time must be before end time in '%s' for %s at row %d",
                                scheduleStr, columnName, row.getRowNumber()));
            }

            schedules.add(new Schedule(dayOfWeek, startTime, endTime));

        } catch (Exception e) {
            throw new IllegalArgumentException(
                    String.format("Error parsing schedule '%s' for %s at row %d: %s",
                            scheduleStr, columnName, row.getRowNumber(), e.getMessage()), e);
        }
    }

    /**
     * Gets a required value from the row, throwing an exception if missing.
     */
    private String getRequiredValue(ExcelRow row, String columnName) {
        String value = row.get(columnName);
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    String.format("Missing required value for column '%s' at row %d",
                            columnName, row.getRowNumber()));
        }
        return value.trim();
    }

    /**
     * Parses a string to Long with error handling.
     */
    private Long parseLong(String value, ExcelRow row) {
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    String.format("Invalid number format for OIDMATERIA: '%s' at row %d",
                            value, row.getRowNumber()));
        }
    }
}