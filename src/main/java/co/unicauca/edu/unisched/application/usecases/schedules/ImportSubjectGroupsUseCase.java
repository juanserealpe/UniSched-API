package co.unicauca.edu.unisched.application.usecases.schedules;

import co.unicauca.edu.unisched.application.dto.ImportResult;
import co.unicauca.edu.unisched.domain.model.RowData;
import co.unicauca.edu.unisched.domain.model.SubjectGroup;
import co.unicauca.edu.unisched.domain.ports.IExcelReaderPort;
import co.unicauca.edu.unisched.domain.ports.IExcelRowMapperPort;
import co.unicauca.edu.unisched.domain.ports.ISubjectGroupRepository;
import co.unicauca.edu.unisched.mapper.excel.RowDataToSubjectGroupMapper;

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

    private final IExcelReaderPort reader;
    private final IExcelRowMapperPort<SubjectGroup> mapper;
    private final ISubjectGroupRepository subjectGroupRepository;

    public ImportSubjectGroupsUseCase(
            @Qualifier("apachePoiExcelReaderUseCase") IExcelReaderPort reader,
            @Qualifier("rowDataToSubjectGroupMapper")IExcelRowMapperPort<SubjectGroup> mapper,
            ISubjectGroupRepository subjectGroupRepository) {
        this.reader = reader;
        this.mapper = mapper;
        this.subjectGroupRepository = subjectGroupRepository;
    }

    /**
     * Imports subject groups from an Excel file and saves them into the system
     * for a specific academic period.
     * <p>
     * The Excel file is read from the provided input stream, transformed into
     * {@link RowData} objects, mapped to {@link SubjectGroup} entities, and finally
     * persisted using the subject group repository.
     * <p>
     * This method collects errors that occur during the mapping process
     * and returns a summary of the import operation.
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
        List<RowData> rows = reader.read(excelStream);

        int successCount = 0;
        int errorCount = 0;
        List<String> errors = new ArrayList<>();

        for (RowData row : rows) {
            try {
                SubjectGroup subjectGroup = ((RowDataToSubjectGroupMapper) mapper)
                        .map(row, academicYear, academicSemester);
                subjectGroupRepository.save(subjectGroup);
                successCount++;
            } catch (Exception e) {
                errorCount++;
                String errorMsg = String.format("Row %d: %s",
                        row.getRowNumber(), e.getMessage());
                errors.add(errorMsg);
            }
        }

        return new ImportResult(successCount, errorCount, errors);
    }
}