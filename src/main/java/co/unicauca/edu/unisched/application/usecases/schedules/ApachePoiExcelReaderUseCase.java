package co.unicauca.edu.unisched.application.usecases.schedules;

import co.unicauca.edu.unisched.domain.model.RowData;
import co.unicauca.edu.unisched.domain.ports.excel.IExcelReaderPort;

import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Reads Excel files using Apache POI and converts them into {@link RowData} objects.
 *
 * This application-level service implements the {@link IExcelReaderPort} and is
 * responsible for reading raw Excel data and exposing it in a generic, framework-
 * independent format.
 *
 * The reader:
 * <ul>
 *   <li>Processes the first sheet of the Excel file</li>
 *   <li>Uses the first row as column headers</li>
 *   <li>Converts each subsequent row into a {@link RowData}</li>
 * </ul>
 *
 * This implementation isolates Apache POI from the domain layer, ensuring that
 * Excel parsing remains an infrastructure concern.
 *
 * Any low-level parsing error is wrapped into a {@link RuntimeException}
 * and propagated to the caller.
 */
@Service
public class ApachePoiExcelReaderUseCase implements IExcelReaderPort {

    private static final Logger logger =
            LoggerFactory.getLogger(ApachePoiExcelReaderUseCase.class);

    /**
     * Reads an Excel file from the given input stream.
     *
     * The first row of the sheet is interpreted as the header row, defining
     * the column names. Each subsequent row is transformed into a {@link RowData}
     * containing a map of column names to cell values.
     *
     * @param inputStream Input stream of the Excel file
     * @return List of parsed {@link RowData} objects
     * @throws RuntimeException if an error occurs while reading the Excel file
     */
    @Override
    public List<RowData> read(InputStream inputStream) {
        logger.info("Starting Excel file reading.");

        List<RowData> rows = new ArrayList<>();

        try (Workbook workbook = WorkbookFactory.create(inputStream)) {

            logger.debug("Workbook loaded successfully.");

            Sheet sheet = workbook.getSheetAt(0);

            if (sheet == null) {
                logger.warn("The workbook does not contain any sheets.");
                return rows;
            }

            Row header = sheet.getRow(0);

            if (header == null) {
                logger.warn("The first sheet does not contain a header row.");
                return rows;
            }

            logger.info("Processing {} data rows from the Excel file.", sheet.getLastRowNum());

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);

                if (row == null) {
                    logger.debug("Skipping empty row {}.", i);
                    continue;
                }

                RowData excelRow = new RowData();
                excelRow.setRowNumber(i);

                for (int j = 0; j < header.getLastCellNum(); j++) {
                    String columnName = header.getCell(j).getStringCellValue();
                    Cell cell = row.getCell(j);

                    excelRow.getCells().put(columnName, getCellValue(cell));
                }

                rows.add(excelRow);
                logger.debug("Row {} processed successfully.", i);
            }

            logger.info("Excel file read successfully. Total rows processed: {}.", rows.size());

        } catch (Exception e) {
            logger.error("Failed to read Excel file.", e);
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
        if (cell == null) {
            return null;
        }

        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue();
            case NUMERIC -> String.valueOf((int) cell.getNumericCellValue());
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            default -> {
                logger.warn("Unsupported cell type '{}' found.", cell.getCellType());
                yield null;
            }
        };
    }
}