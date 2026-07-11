package co.unicauca.edu.unisched.mapper.api;

import org.springframework.stereotype.Component;

import co.unicauca.edu.unisched.interfaces.api.dto.SubjectSelectionRequest;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
/**
 * Mapper responsible for extracting domain-relevant data from
 * SubjectSelectionRequest DTOs.
 *
 * This class isolates request parsing logic from controllers and use cases,
 * ensuring null-safe extraction of subject and group identifiers.
 */
@Component
public class SubjectSelectionRequestMapper {
    /**
     * Extracts selected subject IDs from the request.
     *
     * Returns an empty set when the request does not contain subject IDs,
     * avoiding null handling in upper layers.
     *
     * @param request incoming subject selection request
     * @return set of selected subject IDs
     */
    public Set<Long> extractSubjectIds(SubjectSelectionRequest request) {
        return request.subjectIds() != null ? request.subjectIds() : Collections.emptySet();
    }
    /**
     * Extracts excluded subject group IDs from the request.
     *
     * Returns a defensive copy of the IDs to prevent external modification,
     * or an empty set if no exclusions are provided.
     *
     * @param request incoming subject selection request
     * @return set of excluded subject group IDs
     */
    public Set<Long> extractExcludedGroupIds(SubjectSelectionRequest request) {
        return request.excludedGroupIds() != null ? new HashSet<>(request.excludedGroupIds()) : Collections.emptySet();
    }
}
