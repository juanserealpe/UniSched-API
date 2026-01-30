package co.unicauca.edu.unisched.infrastructure;

import co.unicauca.edu.unisched.domain.model.Subject;
import co.unicauca.edu.unisched.domain.ports.ISubjectRepository;
import co.unicauca.edu.unisched.infrastructure.persistence.entity.SubjectEntity;
import co.unicauca.edu.unisched.infrastructure.persistence.repository.SubjectJpaRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Service that initializes and provides the study plan.
 * Implements SubjectRepository to provide the knowledge base.
 *
 * This service maintains the complete study plan graph in memory,
 * including all prerequisite and mandatory relationships.
 * It also synchronizes basic subject data with the database.
 */
@Service
public class StudyPlanService implements ISubjectRepository {

    private final Set<Subject> allSubjects = new HashSet<>();

    private SubjectJpaRepository subjectJpaRepository;

    @Autowired
    public void setSubjectJpaRepository(@Lazy SubjectJpaRepository subjectJpaRepository) {
        this.subjectJpaRepository = subjectJpaRepository;
    }

    /**
     * Initializes the study plan with all subjects, their relationships,
     * prerequisites, and mandatory associations.
     * This method is automatically called after the bean is constructed.
     *
     * After building the in-memory graph, it synchronizes the subjects
     * with the database to ensure SubjectGroups can reference them.
     */

    @PostConstruct

    @Transactional
    public void initializeStudyPlan() {
        // =========================
        // S1
        // =========================
        Subject calculo1 = new Subject(11465L, "Cálculo I", (byte) 1);
        Subject lectura = new Subject(21505L, "Lectura y Escritura", (byte) 1);
        Subject intro_sistemas = new Subject(11479L, "Introducción a la Ing. de Sistemas", (byte) 1);
        Subject intro_info = new Subject(11477L, "Introducción a la Informática", (byte) 1);
        Subject lab_intro_info = new Subject(11478L, "Lab. Introducción a la Informática", (byte) 1);

        // =========================
        // S2
        // =========================
        Subject calculo2 = new Subject(8L, "Cálculo II", (byte) 2);
        Subject mecanica = new Subject(9L, "Mecánica", (byte) 2);
        Subject lab_mecanica = new Subject(10L, "Laboratorio Mecánica", (byte) 2);
        Subject algebra = new Subject(11410L, "Álgebra Lineal", (byte) 2);
        Subject poo = new Subject(12L, "Programación Orientada a Objetos", (byte) 2);
        Subject lab_poo = new Subject(13L, "Laboratorio POO", (byte) 2);

        // =========================
        // S3
        // =========================
        Subject calculo3 = new Subject(14L, "Cálculo III", (byte) 3);
        Subject electromagnetismo = new Subject(15L, "Electromagnetismo", (byte) 3);
        Subject lab_electro = new Subject(16L, "Lab. Electromagnetismo", (byte) 3);
        Subject ed1 = new Subject(18L, "Estructuras de Datos I", (byte) 3);
        Subject lab_ed1 = new Subject(19L, "Lab. Estructuras de Datos I", (byte) 3);

        // =========================
        // S4
        // =========================
        Subject ecuaciones = new Subject(20L, "Ecuaciones Diferenciales", (byte) 4);
        Subject vibraciones = new Subject(21L, "Vibraciones y Ondas", (byte) 4);
        Subject ed2 = new Subject(22L, "Estructuras de Datos II", (byte) 4);
        Subject lab_ed2 = new Subject(23L, "Lab. Estructuras de Datos II", (byte) 4);
        Subject bd1 = new Subject(24L, "Bases de Datos I", (byte) 4);
        Subject lab_bd1 = new Subject(25L, "Lab. Bases de Datos I", (byte) 4);

        // =========================
        // S5
        // =========================
        Subject analisis_num = new Subject(26L, "Análisis Numérico", (byte) 5);
        Subject teoria_comp = new Subject(27L, "Teoría de la Computación", (byte) 5);
        Subject lab_sw1 = new Subject(28L, "Lab. Ingeniería de Software I", (byte) 5);
        Subject sw1 = new Subject(29L, "Ingeniería de Software I", (byte) 5);
        Subject arq_comp = new Subject(30L, "Arquitectura Computacional", (byte) 5);
        Subject bd2 = new Subject(31L, "Bases de Datos II", (byte) 5);
        Subject lab_bd2 = new Subject(32L, "Lab. Bases de Datos II", (byte) 5);

        // =========================
        // S6
        // =========================
        Subject estadistica = new Subject(10177L, "Estadística y Probabilidad", (byte) 6);
        Subject lenguajes = new Subject(34L, "Estructuras de Lenguajes", (byte) 6);
        Subject lab_lenguajes = new Subject(35L, "Lab. Estructuras de Lenguajes", (byte) 6);
        Subject sw2 = new Subject(36L, "Ingeniería de Software II", (byte) 6);
        Subject lab_sw2 = new Subject(37L, "Lab. Ingeniería de Software II", (byte) 6);
        Subject so = new Subject(38L, "Sistemas Operativos", (byte) 6);
        Subject lab_so = new Subject(39L, "Lab. Sistemas Operativos", (byte) 6);

        // =========================
        // S7
        // =========================
        Subject teoria_dinamica = new Subject(10187L, "Teoria y Dinámica de Sistemas", (byte) 7);
        Subject metodologia = new Subject(10172L, "Metodología de la Investigación", (byte) 7);
        Subject ia = new Subject(10186L, "Inteligencia Artificial", (byte) 7);
        Subject distribuidos = new Subject(10188L, "Sistemas Distribuidos", (byte) 7);
        Subject lab_distribuidos = new Subject(10189L, "Lab. Sistemas Distribuidos", (byte) 7);
        Subject sw3 = new Subject(10190L, "Ingeniería de Software III", (byte) 7);
        Subject lab_sw3 = new Subject(10191L, "Lab. Ingeniería de Software III", (byte) 7);

        // =========================
        // S8
        // =========================
        Subject calidad_de_software = new Subject(47L, "Calidad de Software", (byte) 8);
        Subject investigacion_de_operacion = new Subject(48L, "Investigación de Operaciones", (byte) 8);
        Subject proyecto_1 = new Subject(49L, "Proyecto I", (byte) 8);
        Subject redes = new Subject(50L, "Redes", (byte) 8);

        // =========================
        // PREREQUISITES (GRAPH)
        // =========================

        // S1
        calculo1.unlock(calculo2);
        calculo1.unlock(mecanica);
        calculo1.unlock(algebra);
        intro_info.unlock(poo);
        intro_info.mandatoryWith(lab_intro_info);

        // S2
        calculo2.unlock(calculo3);
        calculo2.unlock(electromagnetismo);
        calculo2.unlock(lab_electro);
        mecanica.mandatoryWith(lab_mecanica);
        poo.unlock(ed1);
        poo.mandatoryWith(lab_poo);

        // S3
        calculo3.unlock(ecuaciones);
        calculo3.unlock(vibraciones);
        calculo3.unlock(teoria_comp);
        ed1.unlock(bd1);
        ed1.unlock(ed2);
        ed1.mandatoryWith(lab_ed1);

        // S4
        teoria_comp.unlock(lenguajes);
        ed2.mandatoryWith(lab_ed2);
        bd1.mandatoryWith(lab_bd1);
        bd1.unlock(bd2);
        ed2.unlock(sw1);
        ed2.unlock(teoria_comp);
        ed2.unlock(bd2);

        // S5
        ecuaciones.unlock(analisis_num);
        ecuaciones.unlock(estadistica);
        ecuaciones.unlock(teoria_dinamica);
        sw1.mandatoryWith(lab_sw1);
        sw1.unlock(sw2);
        arq_comp.unlock(so);
        bd2.mandatoryWith(lab_bd2);

        // S6
        lenguajes.unlock(ia);
        lenguajes.unlock(distribuidos);
        sw2.mandatoryWith(lab_sw2);
        so.mandatoryWith(lab_so);
        lenguajes.mandatoryWith(lab_lenguajes);

        // S7
        sw3.mandatoryWith(lab_sw3);

        // S8
        sw3.unlock(proyecto_1);
        sw3.unlock(calidad_de_software);
        estadistica.unlock(investigacion_de_operacion);

        // Add all subjects to in-memory collection
        allSubjects.addAll(Set.of(
                calculo1, lectura, intro_sistemas, intro_info, lab_intro_info,
                calculo2, mecanica, lab_mecanica, algebra, poo, lab_poo,
                calculo3, electromagnetismo, lab_electro, ed1, lab_ed1,
                ecuaciones, vibraciones, ed2, lab_ed2, bd1, lab_bd1,
                analisis_num, teoria_comp, lab_sw1, sw1, arq_comp, bd2, lab_bd2,
                estadistica, lenguajes, lab_lenguajes, sw2, lab_sw2, so, lab_so,
                teoria_dinamica, metodologia, ia, distribuidos, lab_distribuidos, sw3, lab_sw3,
                calidad_de_software, investigacion_de_operacion,
                proyecto_1, redes));

        // Synchronize with database
        synchronizeWithDatabase();
    }

    /**
     * Synchronizes the in-memory subject graph with the database.
     * Only persists basic subject data (id, name, semester).
     * Relationships are maintained in memory only.
     */
    private void synchronizeWithDatabase() {
        for (Subject subject : allSubjects) {
            if (!subjectJpaRepository.existsById(subject.getId())) {
                SubjectEntity entity = new SubjectEntity();
                entity.setId(subject.getId());
                entity.setName(subject.getName());
                entity.setNumSemester(subject.getNumSemester());
                subjectJpaRepository.save(entity);
            }
        }
    }

    /**
     * Returns all subjects in the study plan.
     *
     * @return a new set containing all subjects with full relationship graph
     */
    @Override
    public Set<Subject> findAll() {
        return new HashSet<>(allSubjects);
    }

    /**
     * Finds a subject by its ID from the in-memory graph.
     *
     * @param id the ID of the subject to find
     * @return an Optional containing the subject if found
     */
    @Override
    public Optional<Subject> findById(Long id) {
        return allSubjects.stream()
                .filter(subject -> subject.getId().equals(id))
                .findFirst();
    }

    /**
     * Finds multiple subjects by their IDs from the in-memory graph.
     *
     * @param ids a set of subject IDs to find
     * @return a set of found subjects with full relationship data
     */
    @Override
    public Set<Subject> findByIds(Set<Long> ids) {
        Set<Subject> result = new HashSet<>();
        for (Long id : ids) {
            findById(id).ifPresent(result::add);
        }
        return result;
    }

    @Override
    public Subject save(Subject subject) {
        // This implementation is read-only for the study plan
        // Subjects are defined programmatically, not created dynamically
        throw new UnsupportedOperationException(
                "Study plan subjects cannot be created dynamically. " +
                        "They must be defined in initializeStudyPlan()."
        );
    }
}