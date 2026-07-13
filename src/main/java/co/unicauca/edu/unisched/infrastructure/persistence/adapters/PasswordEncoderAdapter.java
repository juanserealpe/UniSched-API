package co.unicauca.edu.unisched.infrastructure.persistence.adapters;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import co.unicauca.edu.unisched.domain.ports.auth.register.IPasswordEncoder;

@Component
public class PasswordEncoderAdapter implements IPasswordEncoder {

    private final BCryptPasswordEncoder encoder;

    public PasswordEncoderAdapter(BCryptPasswordEncoder encoder) {
        this.encoder = encoder;
    }

    @Override
    public String encode(String rawPassword) {
        return encoder.encode(rawPassword);
    }

    @Override
    public boolean matches(String rawPassword, String encodedPassword) {
        return encoder.matches(rawPassword, encodedPassword);
    }
}