package co.unicauca.edu.unisched.domain.ports;

import co.unicauca.edu.unisched.domain.model.ExcelRow;

import java.io.InputStream;
import java.util.List;

/**
 * Port for the subject selection validation service.
 * Defines the interface for the validation use case.
 */
public interface ExcelRowMapperPort<T> {
    T map (ExcelRow row);
}


