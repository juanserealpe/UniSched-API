package co.unicauca.edu.unisched.application.usecases.auth;

import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Service;
import co.unicauca.edu.unisched.application.dto.Result;
import co.unicauca.edu.unisched.domain.model.RolesEnum;
import co.unicauca.edu.unisched.domain.model.User;
import co.unicauca.edu.unisched.domain.ports.auth.register.IPasswordEncoder;
import co.unicauca.edu.unisched.domain.ports.auth.register.IUserRepository;
import co.unicauca.edu.unisched.domain.validation.BusinessRuleViolation;
import co.unicauca.edu.unisched.domain.validation.auth.PasswordValidator;

@Service
public class RegisterUseCase {

    private final IUserRepository repository;
    private final IPasswordEncoder encoder;

    public RegisterUseCase(IUserRepository repository, IPasswordEncoder encoder) {
        this.repository = repository;
        this.encoder = encoder;
    }

    public Result<Void> register(String username,String password,String email) {

        if (repository.existsByUsername(username)) return Result.failure("El nombre de usuario ya existe.");
        if (repository.existsByEmail(email)) return Result.failure("El correo electrónico ya está registrado.");
        
        PasswordValidator passwordValidator = new PasswordValidator();
        List<BusinessRuleViolation> errors = passwordValidator.validate(password);

        if (!errors.isEmpty()) return Result.failure(errors);
        String encodedPassword = encoder.encode(password);

        User user = new User(username, encodedPassword, email);
        user.addRole(RolesEnum.STUDENT);
        repository.save(user);

        return Result.success(null);
    }
}