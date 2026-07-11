package co.unicauca.edu.unisched.mapper.api;

import co.unicauca.edu.unisched.domain.model.Schedule;
import co.unicauca.edu.unisched.domain.model.Subject;
import co.unicauca.edu.unisched.domain.model.SubjectGroup;
import co.unicauca.edu.unisched.interfaces.api.dto.SubjectSelectionRequest;

import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class SubjectRequestToSubjectMapper {

    /**
     * Maps a list of CustomSubjectDto to a Domain representation.
     * Note: This returns a list of maps/objects or handles the ID logic.
     * For simplicity and to match current logic, we'll return a helper object or
     * just process them in the specific mapper.
     * But per plan, this mapper handles conversion.
     */
    public List<SubjectGroup> mapToDomain(List<SubjectSelectionRequest.CustomSubjectDto> customSubjects,
            long startIdCounter) {
        List<SubjectGroup> allCustomGroups = new ArrayList<>();
        long currentId = startIdCounter;

        if (customSubjects == null)
            return allCustomGroups;

        for (SubjectSelectionRequest.CustomSubjectDto customDto : customSubjects) {
            Subject customSubject = new Subject(currentId--, customDto.name(), (byte) 0);

            for (SubjectSelectionRequest.CustomGroupDto groupDto : customDto.groups()) {
                if (groupDto.excluded())
                    continue;

                List<Schedule> schedules = groupDto.schedules().stream()
                        .map(s -> new Schedule(s.dayOfWeek(), s.startTime(), s.endTime()))
                        .collect(Collectors.toList());

                SubjectGroup customGroup = new SubjectGroup(
                        currentId--,
                        customSubject,
                        groupDto.groupCode(),
                        groupDto.professors() != null ? groupDto.professors() : "Sin especificar",
                        schedules);
                allCustomGroups.add(customGroup);
            }
        }
        return allCustomGroups;
    }
}
