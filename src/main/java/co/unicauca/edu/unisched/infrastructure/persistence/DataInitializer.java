package co.unicauca.edu.unisched.infrastructure.persistence;

import co.unicauca.edu.unisched.domain.model.Schedule;
import co.unicauca.edu.unisched.domain.model.Subject;
import co.unicauca.edu.unisched.domain.model.SubjectGroup;
import co.unicauca.edu.unisched.domain.ports.ISubjectGroupRepository;
import co.unicauca.edu.unisched.domain.ports.ISubjectRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

/**
 * Initializes H2 database with test data.
 * Creates sample subject groups for testing purposes.
 * 
 * Depends on StudyPlanService to ensure subjects are initialized first.
 */
@Component
@DependsOn("studyPlanService")
public class DataInitializer {

    private final ISubjectRepository subjectRepository;
    private final ISubjectGroupRepository groupRepository;

    public DataInitializer(
            ISubjectRepository subjectRepository,
            ISubjectGroupRepository groupRepository) {
        this.subjectRepository = subjectRepository;
        this.groupRepository = groupRepository;
    }

    @PostConstruct
    public void initializeData() {
        // Check if data already exists
        if (!groupRepository.findBySubjectId(1L).isEmpty()) {
            return; // Data already initialized
        }

        // Get subjects from study plan
        Subject calculo1 = subjectRepository.findById(1L).orElse(null);
        Subject calculo2 = subjectRepository.findById(8L).orElse(null);
        Subject poo = subjectRepository.findById(12L).orElse(null);
        Subject ed1 = subjectRepository.findById(18L).orElse(null);
        Subject bd1 = subjectRepository.findById(24L).orElse(null);

        // Create groups for Cálculo I (3 groups)
        if (calculo1 != null) {
            createGroup(calculo1, "A1", "Prof. García", 
                    List.of(
                            new Schedule(DayOfWeek.MONDAY, LocalTime.of(8, 0), LocalTime.of(10, 0)),
                            new Schedule(DayOfWeek.WEDNESDAY, LocalTime.of(8, 0), LocalTime.of(10, 0))
                    ));
            
            createGroup(calculo1, "A2", "Prof. Martínez", 
                    List.of(
                            new Schedule(DayOfWeek.TUESDAY, LocalTime.of(8, 0), LocalTime.of(10, 0)),
                            new Schedule(DayOfWeek.THURSDAY, LocalTime.of(8, 0), LocalTime.of(10, 0))
                    ));
            
            createGroup(calculo1, "B1", "Prof. López", 
                    List.of(
                            new Schedule(DayOfWeek.MONDAY, LocalTime.of(14, 0), LocalTime.of(16, 0)),
                            new Schedule(DayOfWeek.WEDNESDAY, LocalTime.of(14, 0), LocalTime.of(16, 0))
                    ));
        }

        // Create groups for Cálculo II (2 groups)
        if (calculo2 != null) {
            createGroup(calculo2, "A1", "Prof. Rodríguez", 
                    List.of(
                            new Schedule(DayOfWeek.MONDAY, LocalTime.of(10, 0), LocalTime.of(12, 0)),
                            new Schedule(DayOfWeek.WEDNESDAY, LocalTime.of(10, 0), LocalTime.of(12, 0))
                    ));
            
            createGroup(calculo2, "A2", "Prof. Sánchez", 
                    List.of(
                            new Schedule(DayOfWeek.TUESDAY, LocalTime.of(10, 0), LocalTime.of(12, 0)),
                            new Schedule(DayOfWeek.THURSDAY, LocalTime.of(10, 0), LocalTime.of(12, 0))
                    ));
        }

        // Create groups for POO (2 groups)
        if (poo != null) {
            createGroup(poo, "A1", "Prof. González", 
                    List.of(
                            new Schedule(DayOfWeek.MONDAY, LocalTime.of(14, 0), LocalTime.of(16, 0)),
                            new Schedule(DayOfWeek.WEDNESDAY, LocalTime.of(14, 0), LocalTime.of(16, 0))
                    ));
            
            createGroup(poo, "A2", "Prof. Fernández", 
                    List.of(
                            new Schedule(DayOfWeek.TUESDAY, LocalTime.of(14, 0), LocalTime.of(16, 0)),
                            new Schedule(DayOfWeek.THURSDAY, LocalTime.of(14, 0), LocalTime.of(16, 0))
                    ));
        }

        // Create groups for Estructuras de Datos I (2 groups)
        if (ed1 != null) {
            createGroup(ed1, "A1", "Prof. Pérez", 
                    List.of(
                            new Schedule(DayOfWeek.MONDAY, LocalTime.of(16, 0), LocalTime.of(18, 0)),
                            new Schedule(DayOfWeek.WEDNESDAY, LocalTime.of(16, 0), LocalTime.of(18, 0))
                    ));
            
            createGroup(ed1, "A2", "Prof. Torres", 
                    List.of(
                            new Schedule(DayOfWeek.TUESDAY, LocalTime.of(16, 0), LocalTime.of(18, 0)),
                            new Schedule(DayOfWeek.THURSDAY, LocalTime.of(16, 0), LocalTime.of(18, 0))
                    ));
        }

        // Create groups for Bases de Datos I (2 groups)
        if (bd1 != null) {
            createGroup(bd1, "A1", "Prof. Ramírez", 
                    List.of(
                            new Schedule(DayOfWeek.FRIDAY, LocalTime.of(8, 0), LocalTime.of(10, 0)),
                            new Schedule(DayOfWeek.FRIDAY, LocalTime.of(10, 0), LocalTime.of(12, 0))
                    ));
            
            createGroup(bd1, "A2", "Prof. Morales", 
                    List.of(
                            new Schedule(DayOfWeek.FRIDAY, LocalTime.of(14, 0), LocalTime.of(16, 0)),
                            new Schedule(DayOfWeek.FRIDAY, LocalTime.of(16, 0), LocalTime.of(18, 0))
                    ));
        }
    }

    private void createGroup(Subject subject, String groupCode, String professors, List<Schedule> schedules) {
        SubjectGroup group = new SubjectGroup(null, subject, groupCode, professors, schedules);
        groupRepository.save(group);
    }
}

