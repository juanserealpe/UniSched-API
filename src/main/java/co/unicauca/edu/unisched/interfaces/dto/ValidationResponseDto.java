package co.unicauca.edu.unisched.interfaces.dto;

import java.util.List;

/**
 * DTO for validation response indicating whether a subject combination is valid.
 */
public record ValidationResponseDto(
        boolean isValid,
        List<String> errors
) {
    /**
     * Creates a valid validation response.
     *
     * @return a ValidationResponseDto indicating the combination is valid
     */
    public static ValidationResponseDto valid() {
        return new ValidationResponseDto(true, List.of());
    }

    /**
     * Creates an invalid validation response with error messages.
     *
     * @param errors list of error messages explaining why the combination is invalid
     * @return a ValidationResponseDto indicating the combination is invalid
     */
    public static ValidationResponseDto invalid(List<String> errors) {
        return new ValidationResponseDto(false, errors);
    }
}

