package co.unicauca.edu.unisched.domain.validation;

public record BusinessRuleViolation(
        String field,
        String message
) {

    public static BusinessRuleViolation of(String field, String message) {
        return new BusinessRuleViolation(field, message);
    }

    public static BusinessRuleViolation general(String message) {
        return new BusinessRuleViolation("general", message);
    }

}