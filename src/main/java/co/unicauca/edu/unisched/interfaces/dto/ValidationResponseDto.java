package co.unicauca.edu.unisched.interfaces.dto;

import co.unicauca.edu.unisched.domain.model.SubjectGroup;
import java.util.List;
import java.util.Map;

/**
 * DTO for validation response indicating whether a subject combination is
 * valid.
 * Includes available groups for each subject.
 */
public record ValidationResponseDto(
        boolean isValid,
        List<String> errors,
        Map<Long, List<SubjectGroupDto>> groupsBySubject) {
    /**
     * Creates a valid validation response with groups.
     *
     * @param groupsBySubject map of subject IDs to their available groups
     * @return a ValidationResponseDto indicating the combination is valid
     */
    public static ValidationResponseDto valid(Map<Long, List<SubjectGroupDto>> groupsBySubject) {
        return new ValidationResponseDto(true, List.of(), groupsBySubject);
    }

    /**
     * Creates an invalid validation response with error messages.
     *
     * @param errors list of error messages explaining why the combination is
     *               invalid
     * @return a ValidationResponseDto indicating the combination is invalid
     */
    public static ValidationResponseDto invalid(List<String> errors) {
        return new ValidationResponseDto(false, errors, Map.of());
    }

    /**
     * DTO for SubjectGroup in the response.
     */
    public record SubjectGroupDto(
            Long id,
            Long subjectId,
            String subjectName,
            String groupCode,
            String professors,
            List<ScheduleDto> schedules) {
        public static SubjectGroupDto fromDomain(SubjectGroup group) {
            return new SubjectGroupDto(
                    group.getId(),
                    group.getSubject().getId(),
                    group.getSubject().getName(),
                    group.getGroupCode(),
                    group.getProfessors(),
                    group.getSchedules().stream()
                            .map(schedule -> new ScheduleDto(
                                    schedule.getDayOfWeek(),
                                    schedule.getStartTime(),
                                    schedule.getEndTime()))
                            .toList());
        }
    }

    /**
     * DTO for Schedule in the response.
     */
    public record ScheduleDto(
            java.time.DayOfWeek dayOfWeek,
            java.time.LocalTime startTime,
            java.time.LocalTime endTime) {
    }
}
