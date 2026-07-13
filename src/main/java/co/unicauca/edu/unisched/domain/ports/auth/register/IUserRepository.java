package co.unicauca.edu.unisched.domain.ports.auth.register;

import java.util.Optional;

import co.unicauca.edu.unisched.domain.model.User;

public interface IUserRepository {

    User save(User user);

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

}