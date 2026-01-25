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

    public void importAndSaveSubjects(InputStream excelStream) {
        List<ExcelRow> rows = reader.read(excelStream);

        for (ExcelRow row : rows) {
            Subject subject = mapper.map(row);
            subjectRepository.save(subject);
        }
    }
}