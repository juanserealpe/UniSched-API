package co.unicauca.edu.unisched.interfaces.dto;

import jakarta.validation.constraints.NotNull;
import java.time.DayOfWeek;
import java.time.LocalTime;

/**
 * DTO for schedule requests from frontend.
 */
public record ScheduleRequestDto(
        @NotNull(message = "El día de la semana es requerido")
        DayOfWeek dayOfWeek,
        @NotNull(message = "La hora de inicio es requerida")
        LocalTime startTime,
        @NotNull(message = "La hora de fin es requerida")
        LocalTime endTime
) {
}

