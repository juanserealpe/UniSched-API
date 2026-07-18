package co.unicauca.edu.unisched.domain.validation.auth;

import java.util.ArrayList;
import java.util.List;

import co.unicauca.edu.unisched.domain.validation.BusinessRuleViolation;

public class PasswordValidator {

    public List<BusinessRuleViolation> validate(String password) {

        List<BusinessRuleViolation> errors = new ArrayList<>();

        if (password == null || password.isBlank()) {
            errors.add(
                BusinessRuleViolation.of(
                    "password",
                    "La contraseña es obligatoria."
                )
            );
            return errors;
        }

        if (password.length() < 8) {
            errors.add(
                BusinessRuleViolation.of(
                    "password",
                    "Debe tener al menos 8 caracteres."
                )
            );
        }

        if (!password.matches(".*[A-Z].*")) {
            errors.add(
                BusinessRuleViolation.of(
                    "password",
                    "Debe contener al menos una letra mayúscula."
                )
            );
        }

        if (!password.matches(".*[a-z].*")) {
            errors.add(
                BusinessRuleViolation.of(
                    "password",
                    "Debe contener al menos una letra minúscula."
                )
            );
        }

        if (!password.matches(".*\\d.*")) {
            errors.add(
                BusinessRuleViolation.of(
                    "password",
                    "Debe contener al menos un número."
                )
            );
        }

        if (!password.matches(".*[^a-zA-Z0-9].*")) {
            errors.add(
                BusinessRuleViolation.of(
                    "password",
                    "Debe contener al menos un carácter especial."
                )
            );
        }
        return errors;
    }

}