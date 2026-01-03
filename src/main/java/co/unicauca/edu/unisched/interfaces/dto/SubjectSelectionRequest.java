package co.unicauca.edu.unisched.interfaces.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

/**
 * DTO for subject selection requests.
 * Contains subject IDs to validate against study plan and optional new groups to create.
 */
public record SubjectSelectionRequest(
        @NotNull(message = "Los IDs de las materias no pueden ser nulos")
        @NotEmpty(message = "Debe seleccionar al menos una materia")
        Set<Long> subjectIds
) {
}

