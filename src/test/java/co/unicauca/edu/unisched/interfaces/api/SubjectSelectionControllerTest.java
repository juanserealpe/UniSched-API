package co.unicauca.edu.unisched.interfaces.api;

import co.unicauca.edu.unisched.interfaces.dto.SubjectSelectionRequest;
import co.unicauca.edu.unisched.interfaces.dto.ValidationResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import java.util.List;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the SubjectSelectionController validate endpoint.
 * Tests HTTP layer, request/response format, and basic endpoint functionality.
 * For detailed validation logic tests, see SubjectValidatorTest.
 */
@SpringBootTest
@AutoConfigureMockMvc
class SubjectSelectionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Tests that the endpoint accepts valid requests and returns proper response format.
     */
    @Test
    void testValidRequestFormat() throws Exception {
        SubjectSelectionRequest request = new SubjectSelectionRequest(Set.of(1L, 2L, 3L));

        MvcResult result = mockMvc.perform(post("/api/subjects/validate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(List.of(request))))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        ValidationResponseDto response = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                ValidationResponseDto.class
        );

        assertNotNull(response);
        assertNotNull(response.errors());
    }

    /**
     * Tests that the endpoint returns invalid response when validation fails.
     */
    @Test
    void testInvalidRequestReturnsErrors() throws Exception {
        SubjectSelectionRequest request = new SubjectSelectionRequest(Set.of(1L, 8L)); // Invalid combination

        MvcResult result = mockMvc.perform(post("/api/subjects/validate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(List.of(request))))
                .andExpect(status().isOk())
                .andReturn();

        ValidationResponseDto response = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                ValidationResponseDto.class
        );

        assertFalse(response.isValid());
        assertFalse(response.errors().isEmpty());
    }

    /**
     * Tests that invalid subject IDs return bad request.
     */
    @Test
    void testInvalidSubjectIds() throws Exception {
        SubjectSelectionRequest request = new SubjectSelectionRequest(Set.of(999L, 1000L));

        mockMvc.perform(post("/api/subjects/validate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(List.of(request))))
                .andExpect(status().isBadRequest());
    }

    /**
     * Tests that empty request is rejected.
     */
    @Test
    void testEmptyRequest() throws Exception {
        SubjectSelectionRequest request = new SubjectSelectionRequest(Set.of());

        mockMvc.perform(post("/api/subjects/validate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(List.of(request))))
                .andExpect(status().isBadRequest());
    }

    /**
     * Tests that the endpoint handles multiple requests in the array.
     */
    @Test
    void testMultipleRequestsInArray() throws Exception {
        SubjectSelectionRequest request1 = new SubjectSelectionRequest(Set.of(1L));
        SubjectSelectionRequest request2 = new SubjectSelectionRequest(Set.of(2L));

        MvcResult result = mockMvc.perform(post("/api/subjects/validate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(List.of(request1, request2))))
                .andExpect(status().isOk())
                .andReturn();

        ValidationResponseDto response = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                ValidationResponseDto.class
        );

        assertNotNull(response);
    }
}

