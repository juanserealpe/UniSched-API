package co.unicauca.edu.unisched.application.dto;

import java.util.List;
import co.unicauca.edu.unisched.domain.validation.BusinessRuleViolation;

public record Result<T>(
        boolean success,
        T data,
        List<BusinessRuleViolation> errors
) {

    public static <T> Result<T> success(T data) {
        return new Result<>(true, data, List.of());
    }

    public static <T> Result<T> failure(List<BusinessRuleViolation> errors) {
        return new Result<>(false, null, List.copyOf(errors));
    }

    public static <T> Result<T> failure(String message) {
        return failure(List.of(BusinessRuleViolation.general(message)));
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }

}