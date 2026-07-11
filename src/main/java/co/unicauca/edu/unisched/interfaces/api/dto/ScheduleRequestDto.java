package co.unicauca.edu.unisched.interfaces.api.dto;

import jakarta.validation.constraints.NotNull;
import java.time.DayOfWeek;
import java.time.LocalTime;

/**
 * DTO for schedule requests from frontend.
 * Used in both regular groups and custom subjects.
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