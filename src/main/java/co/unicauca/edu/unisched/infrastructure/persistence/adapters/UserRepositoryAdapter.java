package co.unicauca.edu.unisched.infrastructure.persistence.adapters;

import java.util.Optional;
import org.springframework.stereotype.Repository;
import co.unicauca.edu.unisched.domain.model.User;
import co.unicauca.edu.unisched.domain.ports.auth.register.IUserRepository;
import co.unicauca.edu.unisched.infrastructure.persistence.entity.UserEntity;
import co.unicauca.edu.unisched.infrastructure.persistence.repository.UserJpaRepository;

@Repository
public class UserRepositoryAdapter implements IUserRepository {

    private final UserJpaRepository userJpaRepository;

    public UserRepositoryAdapter(UserJpaRepository userJpaRepository) {
        this.userJpaRepository = userJpaRepository;
    }

    @Override
    public User save(User user) {
        return toDomain(
                userJpaRepository.save(
                        toEntity(user)
                )
        );
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userJpaRepository.findById(username)
                .map(this::toDomain);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userJpaRepository.existsById(username);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userJpaRepository.findByEmail(email)
                .map(this::toDomain);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userJpaRepository.existsByEmail(email);
    }

    private UserEntity toEntity(User user) {
        return new UserEntity(
                user.getUsername(),
                user.getPassword(),
                user.getEmail(),
                user.getRoles()
        );
    }

    private User toDomain(UserEntity entity) {
        return new User(
                entity.getUsername(),
                entity.getPassword(),
                entity.getEmail(),
                entity.getRoles()
        );
    }

}