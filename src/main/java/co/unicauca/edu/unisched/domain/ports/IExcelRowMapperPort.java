package co.unicauca.edu.unisched.domain.ports;

import co.unicauca.edu.unisched.domain.model.RowData;

import java.io.InputStream;
import java.util.List;

/**
 * Port for the subject selection validation service.
 * Defines the interface for the validation use case.
 */
public interface IExcelRowMapperPort<T> {
    T map (RowData row);
}


