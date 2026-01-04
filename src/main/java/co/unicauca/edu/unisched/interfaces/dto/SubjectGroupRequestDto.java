package co.unicauca.edu.unisched.interfaces.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

/**
 * DTO for subject group requests from frontend.
 * Used when creating new groups for subjects.
 */
public record SubjectGroupRequestDto(
        @NotNull(message = "El ID de la materia es requerido")
        Long subjectId,
        @NotNull(message = "El código del grupo es requerido")
        @NotEmpty(message = "El código del grupo no puede estar vacío")
        String groupCode,
        @NotNull(message = "Los profesores son requeridos")
        @NotEmpty(message = "Los profesores no pueden estar vacíos")
        String professors,
        @NotNull(message = "Los horarios son requeridos")
        @NotEmpty(message = "Debe tener al menos un horario")
        @Valid
        List<ScheduleRequestDto> schedules
) {
}



