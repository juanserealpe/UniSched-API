package co.unicauca.edu.unisched.domain.model;

import java.util.HashMap;
import java.util.Map;
/**
 * Represents a single row extracted from an Excel file.
 *
 * This domain model stores the raw data of an Excel row in a generic way,
 * allowing the application to process different Excel formats without
 * being tightly coupled to a specific structure.
 *
 * Each row contains:
 * <ul>
 *   <li>The original row number in the Excel sheet</li>
 *   <li>A map of column names to cell values</li>
 * </ul>
 *
 * This abstraction is commonly used during Excel import processes,
 * where rows are read first and later mapped to specific domain entities
 * such as subjects, academic programs, or schedules.
 *
 * The {@link #get(String)} method provides convenient access to cell values
 * by column name.
 *
 * This class belongs to the domain layer and is independent of any
 * external libraries or Excel parsing frameworks.
 */
public class ExcelRow {
    private int rowNumber;
    private Map<String, String> cells = new HashMap<>();

    public int getRowNumber() {return rowNumber;}
    public void setRowNumber(int rowNumber) {this.rowNumber = rowNumber;}
    public Map<String, String> getCells() {return cells;}
    public void setCells(Map<String, String> cells) {this.cells = cells;}
    public String get(String column) {return cells.get(column);}
}
