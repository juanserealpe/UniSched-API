package co.unicauca.edu.unisched.application.mapper;

import co.unicauca.edu.unisched.domain.model.ExcelRow;
import co.unicauca.edu.unisched.domain.model.Subject;
import co.unicauca.edu.unisched.domain.ports.ExcelRowMapperPort;
import org.springframework.stereotype.Component;

@Component
public class SubjectExcelRowMapper implements ExcelRowMapperPort<Subject> {

    private static final String COL_ID = "Id";
    private static final String COL_NAME = "Name";
    private static final String COL_SEMESTER = "Semester";

    @Override
    public Subject map(ExcelRow row) {
        return new Subject(
                parseLong(getRequiredValue(row, COL_ID)),
                getRequiredValue(row, COL_NAME),
                parseByte(getRequiredValue(row, COL_SEMESTER)));
    }

    private String getRequiredValue(ExcelRow row, String columnName) {
        String value = row.get(columnName);
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    String.format("Missing required value for column '%s' at row %d", columnName, row.getRowNumber()));
        }
        return value.trim();
    }

    private Long parseLong(String value) {
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid number format for ID: " + value);
        }
    }

    private Byte parseByte(String value) {
        try {
            return Byte.parseByte(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid number format for Semester: " + value);
        }
    }
}
