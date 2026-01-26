package co.unicauca.edu.unisched.infrastructure.persistence.adapter;

import co.unicauca.edu.unisched.domain.model.ExcelRow;
import co.unicauca.edu.unisched.domain.ports.ExcelReaderPort;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Component;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Apache POI–based adapter for reading Excel files.
 *
 * This infrastructure adapter implements the {@link ExcelReaderPort} and
 * provides a concrete mechanism for reading Excel files using the Apache POI
 * library.
 *
 * Responsibilities:
 * <ul>
 *   <li>Read the first sheet of an Excel file</li>
 *   <li>Use the first row as column headers</li>
 *   <li>Convert each data row into an {@link ExcelRow}</li>
 * </ul>
 *
 * This class belongs to the infrastructure layer and isolates third-party
 * Excel parsing libraries from the application and domain layers.
 */
@Component
public class ApachePoiExcelReaderAdapter implements ExcelReaderPort {
    /**
     * Reads an Excel file and converts its contents into a list of {@link ExcelRow}.
     *
     * The first row of the sheet is treated as the header row, defining the
     * column names. Each subsequent row is mapped into an {@link ExcelRow}
     * with its corresponding cell values.
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
            throw new RuntimeException("Error reading Excel", e);
        }

        return rows;
    }
    /**
     * Extracts the value of an Excel cell and converts it into a String.
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
     * @return String representation of the cell value or {@code null}
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