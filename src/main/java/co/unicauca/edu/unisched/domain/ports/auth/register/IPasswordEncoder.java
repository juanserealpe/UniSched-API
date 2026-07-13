package co.unicauca.edu.unisched.domain.ports.auth.register;

public interface IPasswordEncoder {

    String encode(String rawPassword);

    boolean matches(String rawPassword, String encodedPassword);

}