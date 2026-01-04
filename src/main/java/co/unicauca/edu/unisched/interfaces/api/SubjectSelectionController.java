package co.unicauca.edu.unisched.interfaces.api;

import co.unicauca.edu.unisched.domain.model.Subject;
import co.unicauca.edu.unisched.domain.model.SubjectGroup;
import co.unicauca.edu.unisched.domain.model.SubjectSelection;
import co.unicauca.edu.unisched.domain.ports.ISubjectGroupRepository;
import co.unicauca.edu.unisched.domain.ports.ISubjectRepository;
import co.unicauca.edu.unisched.domain.ports.ISubjectValidationService;
import co.unicauca.edu.unisched.interfaces.dto.SubjectSelectionRequest;
import co.unicauca.edu.unisched.interfaces.dto.ValidationResponseDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * REST controller for subject selection validation.
 * Provides a single endpoint to validate if a combination of subjects is valid
 * and manage subject groups.
 */
@RestController
@RequestMapping("/api/subjects")
@CrossOrigin(origins = "*")
public class SubjectSelectionController {

    private final ISubjectValidationService validationService;
    private final ISubjectRepository subjectRepository;
    private final ISubjectGroupRepository groupRepository;

    public SubjectSelectionController(
            ISubjectValidationService validationService,
            ISubjectRepository subjectRepository,
            ISubjectGroupRepository groupRepository
    ) {
        this.validationService = validationService;
        this.subjectRepository = subjectRepository;
        this.groupRepository = groupRepository;
    }

    /**
     * Validates if a combination of subjects is valid and manages subject groups.
     * 
     * A combination is invalid if:
     * - A subject and its prerequisite are selected together (e.g., Cálculo I and Cálculo II)
     * - A subject is selected without its prerequisites
     * - A subject is selected with another that has a mandatory relationship with a blocked subject
     * 
     * The request can include:
     * - subjectIds: IDs to validate against the study plan
     * - newGroups: New groups to create (not validated against study plan)
     * 
     * @param request subject selection request containing subject IDs and optional new groups
     * @return ResponseEntity containing validation result with isValid flag, error messages, and available groups
     */
    @PostMapping("/validate")
    public ResponseEntity<ValidationResponseDto> validate(
            @Valid @RequestBody SubjectSelectionRequest request
    ) {
        // Step 1: Validate subject IDs against study plan
        Set<Long> subjectIds = request.subjectIds();
        Set<Subject> selectedSubjects = subjectRepository.findByIds(subjectIds);
        
        if (selectedSubjects.size() != subjectIds.size())
            return ResponseEntity.badRequest()
                    .body(ValidationResponseDto.invalid(List.of("Una o más IDs son inválidas")));

        // Step 2: Validate subject combination
        SubjectSelection selection = new SubjectSelection();
        selectedSubjects.forEach(selection::select);

        List<String> errors = validationService.validateCombinationWithErrors(selection);
        
        // Step 3: Get all available groups for the selected subjects
        List<SubjectGroup> allGroups = groupRepository.findBySubjectIds(subjectIds);
        
        // Step 4: Organize groups by subject ID
        Map<Long, List<ValidationResponseDto.SubjectGroupDto>> groupsBySubject = allGroups.stream()
                .collect(Collectors.groupingBy(
                        group -> group.getSubject().getId(),
                        Collectors.mapping(
                                ValidationResponseDto.SubjectGroupDto::fromDomain,
                                Collectors.toList()
                        )
                ));
        
        if (errors.isEmpty()) {
            return ResponseEntity.ok(ValidationResponseDto.valid(groupsBySubject));
        } else {
            return ResponseEntity.ok(ValidationResponseDto.invalid(errors));
        }
    }
}

