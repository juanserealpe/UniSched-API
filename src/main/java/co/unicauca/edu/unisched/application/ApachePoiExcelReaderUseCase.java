package co.unicauca.edu.unisched.application;

import co.unicauca.edu.unisched.domain.model.ExcelRow;
import co.unicauca.edu.unisched.domain.ports.ExcelReaderPort;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Reads Excel files using Apache POI and converts them into {@link ExcelRow} objects.
 *
 * This application-level service implements the {@link ExcelReaderPort} and is
 * responsible for reading raw Excel data and exposing it in a generic, framework-
 * independent format.
 *
 * The reader:
 * <ul>
 *   <li>Processes the first sheet of the Excel file</li>
 *   <li>Uses the first row as column headers</li>
 *   <li>Converts each subsequent row into an {@link ExcelRow}</li>
 * </ul>
 *
 * This implementation isolates Apache POI from the domain layer, ensuring that
 * Excel parsing remains an infrastructure concern.
 *
 * Any low-level parsing error is wrapped into a {@link RuntimeException}
 * and propagated to the caller.
 */
@Service
public class ApachePoiExcelReaderUseCase implements ExcelReaderPort {
    /**
     * Reads an Excel file from the given input stream.
     *
     * The first row of the sheet is interpreted as the header row, defining
     * the column names. Each subsequent row is transformed into an {@link ExcelRow}
     * containing a map of column names to cell values.
     *
     * @param inputStream Input stream of the Excel file
     * @return List of parsed {@link ExcelRow} objects
     * @throws RuntimeException if an error occurs while reading the Excel file
     */
    @Override
    public List<ExcelRow> read(InputStream inputStream) {
        List<ExcelRow> rows = new ArrayList<>();

        try (Workbook workbook = WorkbookFactory.create(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0);
            Row header = sheet.getRow(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                ExcelRow excelRow = new ExcelRow();
                excelRow.setRowNumber(i);

                for (int j = 0; j < header.getLastCellNum(); j++) {
                    String columnName = header.getCell(j).getStringCellValue();
                    Cell cell = row.getCell(j);

                    excelRow.getCells().put(columnName, getCellValue(cell));
                }

                rows.add(excelRow);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error reading Excel file", e);
        }

        return rows;
    }
    /**
     * Extracts the value of a cell and converts it into a String representation.
     *
     * Supported cell types:
     * <ul>
     *   <li>STRING</li>
     *   <li>NUMERIC (converted to integer)</li>
     *   <li>BOOLEAN</li>
     * </ul>
     *
     * Unsupported or empty cells return {@code null}.
     *
     * @param cell Excel cell to read
     * @return String value of the cell or {@code null} if empty or unsupported
     */
    private String getCellValue(Cell cell) {
        if (cell == null) return null;
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue();
            case NUMERIC -> String.valueOf((int) cell.getNumericCellValue());
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            default -> null;
        };
    }
}

