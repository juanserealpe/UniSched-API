package co.unicauca.edu.unisched.domain.ports.excel;

import co.unicauca.edu.unisched.domain.model.RowData;
import java.io.InputStream;
import java.util.List;

/**
 * Port for the subject selection validation service.
 * Defines the interface for the validation use case.
 */
public interface IExcelReaderPort {
    List<RowData> read(InputStream inputStream);
}


