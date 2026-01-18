package co.unicauca.edu.unisched.interfaces.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import java.util.Set;

/**
 * DTO for subject selection requests.
 * Contains subject IDs to validate against study plan and optional custom
 * subjects.
 */
public record SubjectSelectionRequest(
                @NotNull(message = "Los IDs de las materias no pueden ser nulos") Set<String> subjectIds,

                @Valid List<CustomSubjectDto> customSubjects,

                List<Long> excludedGroupIds) {
        /**
         * DTO for custom subjects created by the user.
         * A custom subject can have one or more groups.
         * Each group has its own schedules and professors.
         */
        public record CustomSubjectDto(
                        @NotNull(message = "El nombre de la materia personalizada es requerido") @NotEmpty(message = "El nombre no puede estar vacío") String name,

                        @NotNull(message = "Debe tener al menos un grupo") @NotEmpty(message = "La materia debe tener al menos un grupo") @Valid List<CustomGroupDto> groups) {
        }

        /**
         * DTO for a custom group within a custom subject.
         * Contains group code, professors, and schedules.
         */
        public record CustomGroupDto(
                        @NotNull(message = "El código del grupo es requerido") @NotEmpty(message = "El código del grupo no puede estar vacío") @Size(max = 5, message = "El código del grupo no debe superar los 5 caracteres") String groupCode,

                        String professors, // Optional

                        @NotNull(message = "Los horarios son requeridos") @NotEmpty(message = "El grupo debe tener al menos un horario") @Valid List<ScheduleRequestDto> schedules,

                        boolean excluded // New field to mark group as excluded
        ) {
        }
}