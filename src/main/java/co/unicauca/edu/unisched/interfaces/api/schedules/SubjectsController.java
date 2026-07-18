package co.unicauca.edu.unisched.interfaces.api.schedules;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import co.unicauca.edu.unisched.application.usecases.schedules.ImportSubjectsUseCase;

import java.io.IOException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


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


    @PostMapping("/create/subject")
    public String createSubject(@RequestBody String entity) {
        return entity;
    }
    
}
