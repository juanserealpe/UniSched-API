package co.unicauca.edu.unisched.interfaces.api;

import co.unicauca.edu.unisched.application.usecases.schedules.GenerateAcademicSchedulesUseCase;
import co.unicauca.edu.unisched.application.usecases.schedules.ValidateSubjectSelectionUseCase;
import co.unicauca.edu.unisched.application.usecases.schedules.ValidateWithExclusionsUseCase;
import co.unicauca.edu.unisched.domain.model.SubjectCombinationOutcome;
import co.unicauca.edu.unisched.interfaces.api.dto.schedules.SubjectSelectionRequest;
import co.unicauca.edu.unisched.interfaces.api.dto.schedules.ValidationResponseDto;
import co.unicauca.edu.unisched.interfaces.api.schedules.SubjectSelectionController;
import co.unicauca.edu.unisched.mapper.api.SubjectRequestToSubjectMapper;
import co.unicauca.edu.unisched.mapper.api.SubjectSelectionRequestMapper;
import co.unicauca.edu.unisched.mapper.api.ValidationResultToResponseMapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SubjectSelectionControllerTest {

    @Mock
    private ValidateSubjectSelectionUseCase validateUseCase;
    @Mock
    private ValidateWithExclusionsUseCase validateWithExclusionsUseCase;
    @Mock
    private GenerateAcademicSchedulesUseCase generateSchedulesUseCase;
    @Mock
    private SubjectSelectionRequestMapper requestMapper;
    @Mock
    private ValidationResultToResponseMapper responseMapper;
    @Mock
    private SubjectRequestToSubjectMapper customSubjectMapper;

    @InjectMocks
    private SubjectSelectionController controller;

    @Test
    public void testValidateExclusions_Success() {
        // Arrange
        Set<Long> subjectIds = Set.of(1L);
        Set<Long> excludedGroupIds = Set.of(101L);
        SubjectSelectionRequest request = new SubjectSelectionRequest(subjectIds, null, List.of(101L));

        when(requestMapper.extractSubjectIds(request)).thenReturn(subjectIds);
        when(requestMapper.extractExcludedGroupIds(request)).thenReturn(excludedGroupIds);
        when(customSubjectMapper.mapToDomain(any(), anyLong())).thenReturn(Collections.emptyList());

        SubjectCombinationOutcome outcome = SubjectCombinationOutcome.valid(Collections.emptyMap());
        when(validateWithExclusionsUseCase.validate(subjectIds, Collections.emptyList(), excludedGroupIds))
                .thenReturn(outcome);

        ValidationResponseDto expectedResponse = ValidationResponseDto.valid(Collections.emptyMap());
        when(responseMapper.toDto(outcome)).thenReturn(expectedResponse);

        // Act
        ResponseEntity<ValidationResponseDto> response = controller.validateExclusions(request);

        // Assert
        Assertions.assertNotNull(response);
        Assertions.assertEquals(expectedResponse, response.getBody());
    }

    @Test
    public void testValidateExclusions_Failure() {
        // Arrange
        Set<Long> subjectIds = Set.of(1L);
        SubjectSelectionRequest request = new SubjectSelectionRequest(subjectIds, null, Collections.emptyList());

        when(requestMapper.extractSubjectIds(request)).thenReturn(subjectIds);
        when(customSubjectMapper.mapToDomain(any(), anyLong())).thenReturn(Collections.emptyList());

        SubjectCombinationOutcome outcome = SubjectCombinationOutcome.invalid(List.of("Error"));
        when(validateWithExclusionsUseCase.validate(eq(subjectIds), any(), any())).thenReturn(outcome);

        ValidationResponseDto expectedResponse = ValidationResponseDto.invalid(List.of("Error"));
        when(responseMapper.toDto(outcome)).thenReturn(expectedResponse);

        // Act
        ResponseEntity<ValidationResponseDto> response = controller.validateExclusions(request);

        // Assert
        Assertions.assertNotNull(response);
        Assertions.assertEquals(expectedResponse, response.getBody());
    }
}
