package co.unicauca.edu.unisched.mapper.api;

import co.unicauca.edu.unisched.domain.model.Subject;
import co.unicauca.edu.unisched.domain.model.SubjectCombinationOutcome;
import co.unicauca.edu.unisched.domain.model.SubjectGroup;
import co.unicauca.edu.unisched.interfaces.api.dto.schedules.ValidationResponseDto;

import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
/**
 * Maps domain validation results into API response DTOs.
 *
 * Acts as an anti-corruption layer between the domain
 * and the HTTP layer, avoiding domain leakage.
 */
@Component
public class ValidationResultToResponseMapper {
    /**
     * Converts a SubjectCombinationOutcome into a ValidationResponseDto.
     *
     * If the outcome is invalid, returns an error-based response.
     * If valid, maps subject groups grouped by subject ID.
     *
     * @param outcome domain validation result
     * @return API validation response DTO
     */
    public ValidationResponseDto toDto(SubjectCombinationOutcome outcome) {
        if (!outcome.isValid()) {
            return ValidationResponseDto.invalid(outcome.getErrors());
        }

        Map<Long, List<ValidationResponseDto.SubjectGroupDto>> groupsDto = new HashMap<>();

        for (Map.Entry<Subject, List<SubjectGroup>> entry : outcome.getGroupsBySubject().entrySet()) {
            List<ValidationResponseDto.SubjectGroupDto> groupDtos = entry.getValue().stream()
                    .map(ValidationResponseDto.SubjectGroupDto::fromDomain)
                    .collect(Collectors.toList());
            groupsDto.put(entry.getKey().getId(), groupDtos);
        }

        return ValidationResponseDto.valid(groupsDto);
    }
}
