package co.unicauca.edu.unisched.application.usecases.schedules;

import co.unicauca.edu.unisched.application.dto.ImportResult;
import co.unicauca.edu.unisched.domain.model.RowData;
import co.unicauca.edu.unisched.domain.model.SubjectGroup;
import co.unicauca.edu.unisched.domain.ports.excel.IExcelReaderPort;
import co.unicauca.edu.unisched.domain.ports.excel.IExcelRowMapperPort;
import co.unicauca.edu.unisched.domain.ports.schedules.ISubjectGroupRepository;
import co.unicauca.edu.unisched.mapper.excel.RowDataToSubjectGroupMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Use case responsible for importing subject groups (academic schedules)
 * from an Excel file and persisting them.
 *
 * This application service coordinates the Excel import process by:
 * <ul>
 *   <li>Reading raw Excel data using {@link IExcelReaderPort}</li>
 *   <li>Mapping each {@link RowData} to a {@link SubjectGroup} domain entity</li>
 *   <li>Saving the mapped subject groups through {@link ISubjectGroupRepository}</li>
 * </ul>
 *
 * The use case follows Clean Architecture principles by depending only on
 * domain ports and remaining independent of infrastructure concerns.
 *
 * Import process:
 * <ol>
 *   <li>Read Excel file and extract rows</li>
 *   <li>For each row, validate and map to SubjectGroup</li>
 *   <li>Persist each valid SubjectGroup</li>
 *   <li>Collect and report any errors that occurred during import</li>
 * </ol>
 *
 * Note: This use case operates in a transactional context. If any critical
 * error occurs, all changes will be rolled back. Non-critical errors
 * (e.g., malformed rows) are collected and reported without stopping the import.
 */
@Service
public class ImportSubjectGroupsUseCase {

    private static final Logger logger =
            LoggerFactory.getLogger(ImportSubjectGroupsUseCase.class);

    private final IExcelReaderPort reader;
    private final IExcelRowMapperPort<SubjectGroup> mapper;
    private final ISubjectGroupRepository subjectGroupRepository;

    public ImportSubjectGroupsUseCase(
            @Qualifier("apachePoiExcelReaderUseCase") IExcelReaderPort reader,
            @Qualifier("rowDataToSubjectGroupMapper") IExcelRowMapperPort<SubjectGroup> mapper,
            ISubjectGroupRepository subjectGroupRepository) {
        this.reader = reader;
        this.mapper = mapper;
        this.subjectGroupRepository = subjectGroupRepository;
    }

    /**
     * Imports subject groups from an Excel file and saves them into the system
     * for a specific academic period.
     *
     * @param excelStream      Input stream of the Excel file containing subject group data
     * @param academicYear     Academic year (e.g., 2026)
     * @param academicSemester Academic semester (1 or 2)
     * @return ImportResult containing statistics and any errors encountered
     * @throws RuntimeException if a critical error occurs during reading or persistence
     */
    @Transactional
    public ImportResult importAndSaveSubjectGroups(InputStream excelStream,
                                                   Long academicYear,
                                                   byte academicSemester) {

        logger.info("Starting subject group import for academic period {}-{}.",
                academicYear, academicSemester);

        List<RowData> rows = reader.read(excelStream);

        logger.info("Successfully read {} rows from the Excel file.", rows.size());

        int successCount = 0;
        int errorCount = 0;
        List<String> errors = new ArrayList<>();

        for (RowData row : rows) {
            try {
                logger.debug("Processing row {}.", row.getRowNumber());

                SubjectGroup subjectGroup = ((RowDataToSubjectGroupMapper) mapper)
                        .map(row, academicYear, academicSemester);

                subjectGroupRepository.save(subjectGroup);

                successCount++;

                logger.debug("Row {} imported successfully.", row.getRowNumber());

            } catch (Exception e) {

                errorCount++;

                String errorMsg = String.format("Row %d: %s",
                        row.getRowNumber(), e.getMessage());

                errors.add(errorMsg);

                logger.warn("Failed to import row {}. Reason: {}",
                        row.getRowNumber(), e.getMessage());
            }
        }

        logger.info(
                "Subject group import completed. Successful imports: {}, Failed imports: {}, Total processed: {}.",
                successCount,
                errorCount,
                rows.size()
        );

        return new ImportResult(successCount, errorCount, errors);
    }
}