package co.unicauca.edu.unisched.domain.model;

import java.util.HashMap;
import java.util.Map;

public class ExcelRow {
    private int rowNumber;
    private Map<String, String> cells = new HashMap<>();

    public int getRowNumber() {
        return rowNumber;
    }

    public void setRowNumber(int rowNumber) {
        this.rowNumber = rowNumber;
    }

    public Map<String, String> getCells() {
        return cells;
    }

    public void setCells(Map<String, String> cells) {
        this.cells = cells;
    }

    public String get(String column) {
        return cells.get(column);
    }
}
