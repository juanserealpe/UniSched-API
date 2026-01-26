package co.unicauca.edu.unisched.application;

import co.unicauca.edu.unisched.domain.model.ExcelRow;
import co.unicauca.edu.unisched.domain.model.Subject;
import co.unicauca.edu.unisched.domain.ports.ExcelReaderPort;
import co.unicauca.edu.unisched.domain.ports.ExcelRowMapperPort;
import co.unicauca.edu.unisched.domain.ports.ISubjectRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import java.io.InputStream;
import java.util.List;
/**
 * Use case responsible for importing subjects from an Excel file and persisting them.
 *
 * This application service coordinates the Excel import process by:
 * <ul>
 *   <li>Reading raw Excel data using {@link ExcelReaderPort}</li>
 *   <li>Mapping each {@link ExcelRow} to a {@link Subject} domain entity</li>
 *   <li>Saving the mapped subjects through {@link ISubjectRepository}</li>
 * </ul>
 *
 * The use case follows Clean Architecture principles by depending only on
 * domain ports and remaining independent of infrastructure concerns.
 *
 * Any validation or parsing errors are propagated from the underlying
 * reader or mapper components.
 */
@Service
public class ImportSubjectsUseCase {

    private final ExcelReaderPort reader;
    private final ExcelRowMapperPort<Subject> mapper;
    private final ISubjectRepository subjectRepository;

    public ImportSubjectsUseCase(@Qualifier("apachePoiExcelReaderUseCase") ExcelReaderPort reader,
            ExcelRowMapperPort<Subject> mapper,
            @Qualifier("subjectRepositoryAdapter") ISubjectRepository subjectRepository) {
        this.reader = reader;
        this.mapper = mapper;
        this.subjectRepository = subjectRepository;
    }
    /**
     * Imports subjects from an Excel file and saves them into the system.
     *
     * The Excel file is read from the provided input stream, transformed into
     * {@link ExcelRow} objects, mapped to {@link Subject} entities, and finally
     * persisted using the subject repository.
     *
     * @param excelStream Input stream of the Excel file containing subject data
     * @throws RuntimeException if an error occurs during reading, mapping, or persistence
     */
    public void importAndSaveSubjects(InputStream excelStream) {
        List<ExcelRow> rows = reader.read(excelStream);

        for (ExcelRow row : rows) {
            Subject subject = mapper.map(row);
            subjectRepository.save(subject);
        }
    }
}