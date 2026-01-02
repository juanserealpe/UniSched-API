package co.unicauca.edu.unisched.interfaces.api;

import co.unicauca.edu.unisched.domain.model.Subject;
import co.unicauca.edu.unisched.domain.model.SubjectSelection;
import co.unicauca.edu.unisched.domain.ports.ISubjectRepository;
import co.unicauca.edu.unisched.domain.ports.ISubjectValidationService;
import co.unicauca.edu.unisched.interfaces.dto.SubjectSelectionRequest;
import co.unicauca.edu.unisched.interfaces.dto.ValidationResponseDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Set;

/**
 * REST controller for subject selection validation.
 * Provides a single endpoint to validate if a combination of subjects is valid.
 */
@RestController
@RequestMapping("/api/subjects")
@CrossOrigin(origins = "*")
public class SubjectSelectionController {

    private final ISubjectValidationService validationService;
    private final ISubjectRepository subjectRepository;

    public SubjectSelectionController(
            ISubjectValidationService validationService,
            ISubjectRepository subjectRepository
    ) {
        this.validationService = validationService;
        this.subjectRepository = subjectRepository;
    }

    /**
     * Validates if a combination of subjects is valid.
     * 
     * A combination is invalid if:
     * - A subject and its prerequisite are selected together (e.g., Cálculo I and Cálculo II)
     * - A subject is selected without its prerequisites
     * - A subject is selected with another that has a mandatory relationship with a blocked subject
     * 
     * @param requests array of subject selection requests containing subject IDs
     * @return ResponseEntity containing validation result with isValid flag and error messages
     */
    @PostMapping("/validate")
    public ResponseEntity<ValidationResponseDto> validate(
            @Valid @RequestBody List<SubjectSelectionRequest> requests
    ) {
        Set<Long> allSubjectIds = requests.stream()
                .flatMap(req -> req.subjectIds().stream())
                .collect(java.util.stream.Collectors.toSet());
        
        Set<Subject> selectedSubjects = subjectRepository.findByIds(allSubjectIds);
        
        if (selectedSubjects.size() != allSubjectIds.size()) {
            return ResponseEntity.badRequest()
                    .body(ValidationResponseDto.invalid(List.of("One or more subject IDs are invalid")));
        }

        SubjectSelection selection = new SubjectSelection();
        selectedSubjects.forEach(selection::select);

        List<String> errors = validationService.validateCombinationWithErrors(selection);
        
        if (errors.isEmpty()) {
            return ResponseEntity.ok(ValidationResponseDto.valid());
        } else {
            return ResponseEntity.ok(ValidationResponseDto.invalid(errors));
        }
    }
}

