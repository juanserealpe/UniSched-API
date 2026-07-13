package co.unicauca.edu.unisched.domain.ports.auth.login;

import co.unicauca.edu.unisched.domain.model.User;

public interface ITokenProvider {

    String generateToken(User user);

    String getUsername(String token);

    boolean validate(String token);

}