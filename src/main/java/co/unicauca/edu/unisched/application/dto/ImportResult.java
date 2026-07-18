package co.unicauca.edu.unisched.application.dto;

import java.util.List;

/**
 * Result object containing import statistics.
 */
public record ImportResult(
        int successCount,
        int errorCount,
        List<String> errors
) {
    public boolean hasErrors() {return errorCount > 0;}
    public int getTotalProcessed() {return successCount + errorCount;}
}
