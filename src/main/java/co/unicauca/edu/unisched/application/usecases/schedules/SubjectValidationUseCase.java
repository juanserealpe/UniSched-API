package co.unicauca.edu.unisched.application.usecases.schedules;

import co.unicauca.edu.unisched.domain.model.SubjectSelection;
import co.unicauca.edu.unisched.domain.planner.SubjectValidator;
import co.unicauca.edu.unisched.domain.ports.ISubjectRepository;
import co.unicauca.edu.unisched.domain.ports.ISubjectValidationService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Use case for validating subject selections.
 * Implements the business logic for subject validation.
 */
@Service
public class SubjectValidationUseCase implements ISubjectValidationService {

    private final ISubjectRepository subjectRepository;
    private SubjectValidator validator;

    public SubjectValidationUseCase(@Qualifier("studyPlanService") ISubjectRepository subjectRepository) {
        this.subjectRepository = subjectRepository;
        initializeValidator();
    }

    /**
     * Initializes the validator with all subjects from the repository.
     */
    private void initializeValidator() {
        this.validator = new SubjectValidator(subjectRepository.findAll());
    }

    /**
     * Validates if a combination of subjects is valid.
     * Ensures the validator is initialized before performing validation.
     *
     * @param selection the subject selection to validate
     * @return true if the combination is valid, false otherwise
     */
    @Override
    public boolean isValidCombination(SubjectSelection selection) {
        if (validator == null) {
            initializeValidator();
        }
        return validator.isValidCombination(selection);
    }

    /**
     * Validates a combination and returns detailed error messages if invalid.
     * Ensures the validator is initialized before performing validation.
     *
     * @param selection the subject selection to validate
     * @return a list of error messages, empty if valid
     */
    @Override
    public List<String> validateCombinationWithErrors(SubjectSelection selection) {
        if (validator == null) initializeValidator();
        return validator.validateCombinationWithErrors(selection);
    }
}

