package co.unicauca.edu.unisched.interfaces.api;

import co.unicauca.edu.unisched.application.ImportSubjectsUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@RestController
@RequestMapping("/api/subjects")
@CrossOrigin(origins = "*")
public class SubjectsController {
    private final ImportSubjectsUseCase importSubjectsUseCase;

    public SubjectsController(ImportSubjectsUseCase importSubjectsUseCase) {
        this.importSubjectsUseCase = importSubjectsUseCase;
    }

    @PostMapping("/import")
    public ResponseEntity<Void> importSubjects(@RequestParam("file") MultipartFile file) throws IOException {
        importSubjectsUseCase.importAndSaveSubjects(file.getInputStream());
        return ResponseEntity.ok().build();
    }
}
