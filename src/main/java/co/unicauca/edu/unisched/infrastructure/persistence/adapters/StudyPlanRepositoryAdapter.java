package co.unicauca.edu.unisched.infrastructure.persistence.adapters;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import co.unicauca.edu.unisched.domain.model.Career;
import co.unicauca.edu.unisched.domain.model.Subject;
import co.unicauca.edu.unisched.domain.ports.schedules.IStudyPlanRepository;
import co.unicauca.edu.unisched.infrastructure.persistence.entity.MandatoryRelationEntity;
import co.unicauca.edu.unisched.infrastructure.persistence.entity.PrerequisiteEntity;
import co.unicauca.edu.unisched.infrastructure.persistence.entity.SubjectEntity;
import co.unicauca.edu.unisched.infrastructure.persistence.repository.MandatoryRelationJpaRepository;
import co.unicauca.edu.unisched.infrastructure.persistence.repository.PrerequisiteJpaRepository;
import co.unicauca.edu.unisched.infrastructure.persistence.repository.SubjectJpaRepository;
import co.unicauca.edu.unisched.mapper.subjects.CareerMapper;

@Repository
public class StudyPlanRepositoryAdapter implements IStudyPlanRepository {

    private final SubjectJpaRepository subjectRepository;
    private final PrerequisiteJpaRepository prerequisiteRepository;
    private final MandatoryRelationJpaRepository mandatoryRepository;
    private final CareerMapper careerMapper;

    public StudyPlanRepositoryAdapter(
            SubjectJpaRepository subjectRepository,
            PrerequisiteJpaRepository prerequisiteRepository,
            MandatoryRelationJpaRepository mandatoryRepository,
            CareerMapper careerMapper) {

        this.subjectRepository = subjectRepository;
        this.prerequisiteRepository = prerequisiteRepository;
        this.mandatoryRepository = mandatoryRepository;
        this.careerMapper = careerMapper;
    }

    @Override
    @Cacheable("studyPlans")
    public Set<Subject> loadByCareer(Long careerId) {

        List<SubjectEntity> subjectEntities =
                subjectRepository.findByCareerId(careerId);

        Map<Long, Subject> subjects = new HashMap<>();

        for (SubjectEntity entity : subjectEntities) {
                subjects.put(entity.getId(),
                new Subject(
                        entity.getId(),
                        entity.getName(),
                        entity.getNumSemester(),
                        careerMapper.toDomain(entity.getCareer())
                ));
        }

        List<PrerequisiteEntity> prerequisites =
                prerequisiteRepository.findBySubjectCareerId(careerId);

        for (PrerequisiteEntity relation : prerequisites) {

            Subject prerequisite =
                    subjects.get(relation.getPrerequisite().getId());

            Subject subject =
                    subjects.get(relation.getSubject().getId());

            prerequisite.unlock(subject);
        }

        List<MandatoryRelationEntity> mandatoryRelations =
                mandatoryRepository.findBySubjectCareerId(careerId);

        for (MandatoryRelationEntity relation : mandatoryRelations) {

            Subject subject =
                    subjects.get(relation.getSubject().getId());

            Subject mandatory =
                    subjects.get(relation.getMandatorySubject().getId());

            subject.mandatoryWith(mandatory);
        }

        return new HashSet<>(subjects.values());
    }
}