package co.unicauca.edu.unisched.domain.ports;

import co.unicauca.edu.unisched.domain.model.Subject;
import co.unicauca.edu.unisched.domain.model.SubjectGroup;

import java.util.List;
import java.util.Map;

public interface IGenerateSchedule {
    public List<List<SubjectGroup>> generateAllValidSchedules(Map<Subject, List<SubjectGroup>> groupsBySubject);
}
