package co.unicauca.edu.unisched.domain.ports.schedules;

import java.util.Set;

import co.unicauca.edu.unisched.domain.model.Subject;

public interface IStudyPlanRepository {

    Set<Subject> loadByCareer(Long careerId);

}