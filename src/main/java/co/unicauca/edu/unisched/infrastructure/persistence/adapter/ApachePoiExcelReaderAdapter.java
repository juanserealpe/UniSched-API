package co.unicauca.edu.unisched.infrastructure.persistence.adapter;

import co.unicauca.edu.unisched.domain.model.ExcelRow;
import co.unicauca.edu.unisched.domain.ports.ExcelReaderPort;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Component;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Component
public class ApachePoiExcelReaderAdapter implements ExcelReaderPort {

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