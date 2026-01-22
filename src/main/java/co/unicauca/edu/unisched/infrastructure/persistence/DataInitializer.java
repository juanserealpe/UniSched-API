package co.unicauca.edu.unisched.infrastructure.persistence;

import co.unicauca.edu.unisched.domain.model.AcademicPeriod;
import co.unicauca.edu.unisched.domain.model.Schedule;
import co.unicauca.edu.unisched.domain.model.Subject;
import co.unicauca.edu.unisched.domain.model.SubjectGroup;
import co.unicauca.edu.unisched.domain.ports.IAcademicPeriodRepository;
import co.unicauca.edu.unisched.domain.ports.ISubjectGroupRepository;
import co.unicauca.edu.unisched.domain.ports.ISubjectRepository;
import co.unicauca.edu.unisched.infrastructure.persistence.entity.AcademicPeriodEntity;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Qualifier;
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
        private final IAcademicPeriodRepository academicPeriodRepository;

        public DataInitializer(
                @Qualifier("studyPlanService") ISubjectRepository subjectRepository,
                ISubjectGroupRepository groupRepository,
                IAcademicPeriodRepository academicPeriodRepository
        ) {
                this.subjectRepository = subjectRepository;
                this.groupRepository = groupRepository;
                this.academicPeriodRepository = academicPeriodRepository;
        }

        @PostConstruct
        public void initializeData() {
                Subject calculo1 = subjectRepository.findById(<11465L>).orElse(null);

                if (calculo1 != null) {
                        createGroup(calculo1, "A", "ORLANDO RODRIGUEZ BUITRAGO, ANYI DANIELA CORREDOR",
                                        List.of(
                                                        new Schedule(DayOfWeek.WEDNESDAY, LocalTime.of(7, 0),
                                                                        LocalTime.of(9, 0)),
                                                        new Schedule(DayOfWeek.FRIDAY, LocalTime.of(7, 0),
                                                                        LocalTime.of(9, 0))));

                        createGroup(calculo1, "B", "LUIS ERNESTO PORTILLA PALADINES",
                                        List.of(
                                                        new Schedule(DayOfWeek.TUESDAY, LocalTime.of(9, 0),
                                                                        LocalTime.of(11, 0)),
                                                        new Schedule(DayOfWeek.THURSDAY, LocalTime.of(14, 0),
                                                                        LocalTime.of(16, 0))));

                        createGroup(calculo1, "C", "ASTRID YINNET ALVAREZ CASTRO",
                                        List.of(
                                                        new Schedule(DayOfWeek.MONDAY, LocalTime.of(11, 0),
                                                                        LocalTime.of(13, 0)),
                                                        new Schedule(DayOfWeek.WEDNESDAY, LocalTime.of(11, 0),
                                                                        LocalTime.of(13, 0))));
                }
        }

        private void createGroup(
                Subject subject,
                String groupCode,
                String professors,
                List<Schedule> schedules
        ) {
                if (subject == null) {
                        throw new IllegalArgumentException("Subject cannot be null");
                }

                if (schedules == null) {
                        schedules = List.of();
                }

                AcademicPeriod period = academicPeriodRepository
                        .findByYearAndSemester(2026L, (byte) 1)
                        .orElseGet(() ->
                                academicPeriodRepository.save(
                                        new AcademicPeriod(null, 2026L, (byte) 1)
                                )
                        );

                SubjectGroup group = new SubjectGroup(
                        null,
                        subject,
                        groupCode,
                        professors,
                        schedules,
                        period
                );

                groupRepository.save(group);
        }


}
