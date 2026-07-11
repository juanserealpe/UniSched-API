package co.unicauca.edu.unisched.interfaces.api.schedules;

import co.unicauca.edu.unisched.application.dto.ImportResult;
import co.unicauca.edu.unisched.application.usecases.schedules.ImportSubjectGroupsUseCase;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * REST controller for managing subject groups (academic schedules).
 *
 * This controller provides endpoints to:
 * - Import subject groups from Excel files
 * - View import results and error reports
 *
 * It acts as the HTTP interface to the subject group import functionality,
 * delegating business logic to the appropriate use cases.
 */
@RestController
@RequestMapping("/api/subject-groups")
@CrossOrigin(origins = "*")
public class SubjectGroupsController {

    private final ImportSubjectGroupsUseCase importSubjectGroupsUseCase;

    public SubjectGroupsController(ImportSubjectGroupsUseCase importSubjectGroupsUseCase) {
        this.importSubjectGroupsUseCase = importSubjectGroupsUseCase;
    }

    /**
     * Imports subject groups (academic schedules) from an Excel file.
     *
     * Expected Excel format:
     * - Column OIDMATERIA: Subject ID (must exist in the database)
     * - Column grupo: Group code (e.g., "A", "B1", "C")
     * - Columns Lunes, Martes, Miercoles, Jueves, Viernes: Schedule in format "HH:mm-HH:mm SALÓN XX"
     * - Column Docentes: Professor names
     *
     * The endpoint processes the file row by row and returns statistics
     * about successful imports and any errors encountered.
     *
     * Example request:
     * POST /api/subject-groups/import?academicPeriod=2026.1
     * Content-Type: multipart/form-data
     * file: horarios.xlsx
     *
     * Example response:
     * {
     *   "success": true,
     *   "message": "Import completed",
     *   "successCount": 45,
     *   "errorCount": 2,
     *   "totalProcessed": 47,
     *   "errors": [
     *     "Row 12: Subject with ID 999 not found",
     *     "Row 23: Invalid schedule format '25:00-26:00' for Lunes"
     *   ]
     * }
     *
     * @param file Excel file containing subject group data
     * @param academicPeriod Academic period in format "YYYY.S" (e.g., "2026.1", "2025.2")
     * @return ResponseEntity with import statistics and error details
     */
    @PostMapping("/import")
    public ResponseEntity<Map<String, Object>> importSubjectGroups(
            @RequestParam("file") MultipartFile file,
            @RequestParam("academicPeriod") String academicPeriod) {

        Map<String, Object> response = new HashMap<>();

        try {
            // Validate file is not empty
            if (file.isEmpty()) {
                response.put("success", false);
                response.put("message", "File is empty");
                return ResponseEntity.badRequest().body(response);
            }

            // Validate file extension
            String filename = file.getOriginalFilename();
            if (filename == null || (!filename.endsWith(".xlsx") && !filename.endsWith(".xls"))) {
                response.put("success", false);
                response.put("message", "File must be an Excel file (.xlsx or .xls)");
                return ResponseEntity.badRequest().body(response);
            }

            // Parse academic period (format: "2026.1" or "2025.2")
            Long academicYear;
            byte academicSemester;
            try {
                String[] parts = academicPeriod.split("\\.");
                if (parts.length != 2) {
                    response.put("success", false);
                    response.put("message", "Invalid academic period format. Expected format: 'YYYY.S' (e.g., '2026.1')");
                    return ResponseEntity.badRequest().body(response);
                }
                academicYear = Long.parseLong(parts[0]);
                academicSemester = Byte.parseByte(parts[1]);

                if (academicSemester < 1 || academicSemester > 2) {
                    response.put("success", false);
                    response.put("message", "Academic semester must be 1 or 2");
                    return ResponseEntity.badRequest().body(response);
                }
            } catch (NumberFormatException e) {
                response.put("success", false);
                response.put("message", "Invalid academic period format. Expected format: 'YYYY.S' (e.g., '2026.1')");
                return ResponseEntity.badRequest().body(response);
            }

            // Execute import
            ImportResult result = importSubjectGroupsUseCase.importAndSaveSubjectGroups(
                    file.getInputStream(), academicYear, academicSemester);

            // Build response
            response.put("success", true);
            response.put("message", "Import completed");
            response.put("academicPeriod", academicPeriod);
            response.put("successCount", result.successCount());
            response.put("errorCount", result.errorCount());
            response.put("totalProcessed", result.getTotalProcessed());

            if (result.hasErrors()) {
                response.put("errors", result.errors());
            }

            // Return 207 Multi-Status if there were partial errors
            if (result.hasErrors() && result.successCount() > 0) {
                return ResponseEntity.status(HttpStatus.MULTI_STATUS).body(response);
            }

            // Return 200 OK if everything succeeded
            if (!result.hasErrors()) {
                return ResponseEntity.ok(response);
            }

            // Return 400 Bad Request if everything failed
            response.put("success", false);
            response.put("message", "Import failed - all rows had errors");
            return ResponseEntity.badRequest().body(response);

        } catch (IOException e) {
            response.put("success", false);
            response.put("message", "Error reading file: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Unexpected error during import: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}