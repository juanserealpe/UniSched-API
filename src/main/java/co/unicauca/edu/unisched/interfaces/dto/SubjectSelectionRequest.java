package co.unicauca.edu.unisched.interfaces.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

/**
 * DTO for subject selection requests.
 * Contains subject IDs to validate against study plan and optional custom subjects.
 */
public record SubjectSelectionRequest(
        @NotNull(message = "Los IDs de las materias no pueden ser nulos")
        Set<Long> subjectIds,

        @Valid
        List<CustomSubjectDto> customSubjects
) {
        /**
         * DTO for custom subjects created by the user.
         * These don't exist in the database but should be included in schedule generation.
         */
        public record CustomSubjectDto(
                @NotNull(message = "El nombre de la materia personalizada es requerido")
                @NotEmpty(message = "El nombre no puede estar vacío")
                String name,

                String groupCode, // Optional, can be null

                @NotNull(message = "Los horarios son requeridos")
                @NotEmpty(message = "Debe tener al menos un horario")
                @Valid
                List<ScheduleRequestDto> schedules
        ) {
        }
}