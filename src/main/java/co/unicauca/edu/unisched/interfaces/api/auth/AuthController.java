package co.unicauca.edu.unisched.interfaces.api.auth;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import co.unicauca.edu.unisched.application.dto.Result;
import co.unicauca.edu.unisched.application.usecases.auth.RegisterUseCase;
import co.unicauca.edu.unisched.interfaces.api.dto.auth.RegisterRequestDto;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final RegisterUseCase registerUseCase;

    public AuthController(RegisterUseCase registerUseCase) {
        this.registerUseCase = registerUseCase;
    }

    @PostMapping("/register")
    public ResponseEntity<Result<Void>> register(@Valid @RequestBody RegisterRequestDto request) {
        Result<Void> result = registerUseCase.register(request.username(),request.password(),request.email());
        if (!result.success()) return ResponseEntity.badRequest().body(result);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }
}