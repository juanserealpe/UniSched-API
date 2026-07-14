package co.unicauca.edu.unisched.interfaces.api.dto.schedules;

import java.util.Set;

public record SubjectCreationRequestDto(
    String name,
    byte semester,
    Set<Integer> prerequisites,
    Set<Integer> unlock
) {
}
