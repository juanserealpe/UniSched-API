package co.unicauca.edu.unisched.application;

import co.unicauca.edu.unisched.domain.model.Subject;
import co.unicauca.edu.unisched.domain.model.SubjectGroup;
import co.unicauca.edu.unisched.domain.ports.IGenerateSchedule;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Service
public class DefaultSchedchulesUserCase implements IGenerateSchedule {

    @Override
    public List<List<SubjectGroup>> generateAllValidSchedules(
            Map<Subject, List<SubjectGroup>> groupsBySubject) {

        List<List<SubjectGroup>> solutions = new ArrayList<>();
        List<Subject> subjects = new ArrayList<>(groupsBySubject.keySet());
        subjects.sort(Comparator.comparingInt(
                s -> groupsBySubject.get(s).size()
        ));

        backtrack(0, subjects, groupsBySubject,
                new ArrayList<>(), solutions);

        return solutions;
    }

    private void backtrack(
            int index,
            List<Subject> subjects,
            Map<Subject, List<SubjectGroup>> groupsBySubject,
            List<SubjectGroup> current,
            List<List<SubjectGroup>> solutions) {

        if (index == subjects.size()) {
            solutions.add(new ArrayList<>(current));
            return;
        }

        Subject subject = subjects.get(index);

        for (SubjectGroup group : groupsBySubject.get(subject)) {

            if (isCompatible(group, current)) {
                current.add(group);
                backtrack(index + 1, subjects, groupsBySubject, current, solutions);
                current.remove(current.size() - 1); // BACKTRACK
            }
        }
    }

    private boolean isCompatible(
            SubjectGroup candidate,
            List<SubjectGroup> selected) {

        for (SubjectGroup group : selected) {
            if (candidate.hasScheduleConflictWith(group)) {
                return false;
            }
        }
        return true;
    }
}