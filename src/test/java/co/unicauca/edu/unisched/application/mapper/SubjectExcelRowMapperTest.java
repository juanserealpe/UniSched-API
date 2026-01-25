package co.unicauca.edu.unisched.application.mapper;

import co.unicauca.edu.unisched.domain.model.ExcelRow;
import co.unicauca.edu.unisched.domain.model.Subject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class SubjectExcelRowMapperTest {

    private SubjectExcelRowMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new SubjectExcelRowMapper();
    }

    @Test
    void map_ValidRow_ReturnsSubject() {
        // Arrange
        ExcelRow row = new ExcelRow();
        Map<String, String> cells = new HashMap<>();
        cells.put("Id", "123");
        cells.put("Name", "Software Engineering");
        cells.put("Semester", "5");
        row.setCells(cells);

        // Act
        Subject subject = mapper.map(row);

        // Assert
        assertNotNull(subject);
        assertEquals(123L, subject.getId());
        assertEquals("Software Engineering", subject.getName());
        assertEquals((byte) 5, subject.getNumSemester());
    }

    @Test
    void map_MissingId_ThrowsException() {
        // Arrange
        ExcelRow row = new ExcelRow();
        Map<String, String> cells = new HashMap<>();
        cells.put("Name", "Software Engineering");
        cells.put("Semester", "5");
        row.setCells(cells);

        // Act & Assert
        // Expect IllegalArgumentException because "Id" is missing (getRequiredValue
        // throws)
        assertThrows(IllegalArgumentException.class, () -> mapper.map(row));
    }

    @Test
    void map_InvalidSemester_ThrowsException() {
        // Arrange
        ExcelRow row = new ExcelRow();
        Map<String, String> cells = new HashMap<>();
        cells.put("Id", "123");
        cells.put("Name", "Software Engineering"); // Name is valid
        cells.put("Semester", "invalid");
        row.setCells(cells);

        // Act & Assert
        // Expect IllegalArgumentException because "invalid" cannot be parsed to Byte
        assertThrows(IllegalArgumentException.class, () -> mapper.map(row));
    }
}
