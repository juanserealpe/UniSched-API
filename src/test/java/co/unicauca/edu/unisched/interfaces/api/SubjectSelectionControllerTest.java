package co.unicauca.edu.unisched.interfaces.api;

import co.unicauca.edu.unisched.domain.model.Subject;
import co.unicauca.edu.unisched.domain.model.SubjectGroup;
import co.unicauca.edu.unisched.domain.ports.IScheduleGenerationService;
import co.unicauca.edu.unisched.domain.ports.ISubjectGroupRepository;
import co.unicauca.edu.unisched.domain.ports.ISubjectRepository;
import co.unicauca.edu.unisched.domain.ports.ISubjectValidationService;
import co.unicauca.edu.unisched.interfaces.dto.SubjectSelectionRequest;
import co.unicauca.edu.unisched.interfaces.dto.ValidationResponseDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SubjectSelectionControllerTest {

    @Mock
    private ISubjectValidationService validationService;
    @Mock
    private ISubjectRepository subjectRepository;
    @Mock
    private ISubjectGroupRepository groupRepository;
    @Mock
    private IScheduleGenerationService generateScheduleService;

    @InjectMocks
    private SubjectSelectionController controller;

    @Test
    public void testValidateExclusions_Success() {
        // Arrange
        String subjectId = "1";
        Subject subject = new Subject(subjectId, "Math", (byte) 1);
        SubjectGroup group1 = new SubjectGroup(101L, subject, "A", "Prof 1", new ArrayList<>());
        SubjectGroup group2 = new SubjectGroup(102L, subject, "B", "Prof 2", new ArrayList<>());

        when(subjectRepository.findByIds(Set.of(subjectId))).thenReturn(Set.of(subject));
        when(validationService.validateCombinationWithErrors(any())).thenReturn(Collections.emptyList());
        when(groupRepository.findBySubjectIds(Set.of(subjectId))).thenReturn(List.of(group1, group2));

        SubjectSelectionRequest request = new SubjectSelectionRequest(
                Set.of(subjectId),
                null,
                List.of(101L) // Exclude group 1
        );

        // Act
        ResponseEntity<ValidationResponseDto> response = controller.validateExclusions(request);

        // Assert
        Assertions.assertNotNull(response);
        Assertions.assertTrue(response.getBody().isValid());
        Assertions.assertTrue(response.getBody().groupsBySubject().containsKey(subjectId));
        Assertions.assertEquals(1, response.getBody().groupsBySubject().get(subjectId).size());
        Assertions.assertEquals("B", response.getBody().groupsBySubject().get(subjectId).get(0).groupCode());
    }

    @Test
    public void testValidateExclusions_AllGroupsExcluded() {
        // Arrange
        String subjectId = "1";
        Subject subject = new Subject(subjectId, "Math", (byte) 1);
        SubjectGroup group1 = new SubjectGroup(101L, subject, "A", "Prof 1", new ArrayList<>());

        when(subjectRepository.findByIds(Set.of(subjectId))).thenReturn(Set.of(subject));
        when(validationService.validateCombinationWithErrors(any())).thenReturn(Collections.emptyList());
        when(groupRepository.findBySubjectIds(Set.of(subjectId))).thenReturn(List.of(group1));

        SubjectSelectionRequest request = new SubjectSelectionRequest(
                Set.of(subjectId),
                null,
                List.of(101L) // Exclude the only group
        );

        // Act
        ResponseEntity<ValidationResponseDto> response = controller.validateExclusions(request);

        // Assert
        Assertions.assertNotNull(response);
        Assertions.assertFalse(response.getBody().isValid());
        Assertions.assertFalse(response.getBody().errors().isEmpty());
        Assertions.assertTrue(response.getBody().errors().get(0).contains("no tiene grupos disponibles"));
    }
}
