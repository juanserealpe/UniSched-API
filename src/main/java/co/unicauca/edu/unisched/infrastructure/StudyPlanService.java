package co.unicauca.edu.unisched.infrastructure;

import co.unicauca.edu.unisched.domain.model.Subject;
import co.unicauca.edu.unisched.domain.ports.ISubjectRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Service that initializes and provides the study plan.
 * Implements SubjectRepository to provide the knowledge base.
 */
@Service
public class StudyPlanService implements ISubjectRepository {

    private final Set<Subject> allSubjects = new HashSet<>();

    /**
     * Initializes the study plan with all subjects, their relationships,
     * prerequisites, and mandatory associations.
     * This method is automatically called after the bean is constructed.
     */
    @PostConstruct
    public void initializeStudyPlan() {
        // =========================
        // S1
        // =========================
        Subject calculo1 = new Subject("11465", "Cálculo I", (byte) 1);
        Subject lectura = new Subject("21505", "Lectura y Escritura", (byte) 1);
        Subject fish1 = new Subject("21740", "Electiva FISH I", (byte) 1);
        Subject intro_sistemas = new Subject("11479", "Introducción a la Ing. de Sistemas", (byte) 1);
        Subject intro_info = new Subject("11477", "Introducción a la Informática", (byte) 1);
        Subject lab_intro_info = new Subject("11478", "Lab. Introducción a la Informática", (byte) 1);

        // =========================
        // S2
        // =========================
        Subject fish2 = new Subject("7", "Electiva FISH II", (byte) 2);
        Subject calculo2 = new Subject("8", "Cálculo II", (byte) 2);
        Subject mecanica = new Subject("9", "Mecánica", (byte) 2);
        Subject lab_mecanica = new Subject("10", "Laboratorio Mecánica", (byte) 2);
        Subject algebra = new Subject("11", "Álgebra Lineal", (byte) 2);
        Subject poo = new Subject("12", "Programación Orientada a Objetos", (byte) 2);
        Subject lab_poo = new Subject("13", "Laboratorio POO", (byte) 2);

        // =========================
        // S3
        // =========================
        Subject calculo3 = new Subject("14", "Cálculo III", (byte) 3);
        Subject electromagnetismo = new Subject("15", "Electromagnetismo", (byte) 3);
        Subject lab_electro = new Subject("16", "Lab. Electromagnetismo", (byte) 3);
        Subject fish3 = new Subject("17", "Electiva FISH III", (byte) 3);
        Subject ed1 = new Subject("18", "Estructuras de Datos I", (byte) 3);
        Subject lab_ed1 = new Subject("19", "Lab. Estructuras de Datos I", (byte) 3);

        // =========================
        // S4
        // =========================
        Subject ecuaciones = new Subject("20", "Ecuaciones Diferenciales", (byte) 4);
        Subject vibraciones = new Subject("21", "Vibraciones y Ondas", (byte) 4);
        Subject ed2 = new Subject("22", "Estructuras de Datos II", (byte) 4);
        Subject lab_ed2 = new Subject("23", "Lab. Estructuras de Datos II", (byte) 4);
        Subject bd1 = new Subject("24", "Bases de Datos I", (byte) 4);
        Subject lab_bd1 = new Subject("25", "Lab. Bases de Datos I", (byte) 4);

        // =========================
        // S5
        // =========================
        Subject analisis_num = new Subject("26", "Análisis Numérico", (byte) 5);
        Subject teoria_comp = new Subject("27", "Teoría de la Computación", (byte) 5);
        Subject lab_sw1 = new Subject("28", "Lab. Ingeniería de Software I", (byte) 5);
        Subject sw1 = new Subject("29", "Ingeniería de Software I", (byte) 5);
        Subject arq_comp = new Subject("30", "Arquitectura Computacional", (byte) 5);
        Subject bd2 = new Subject("31", "Bases de Datos II", (byte) 5);
        Subject lab_bd2 = new Subject("32", "Lab. Bases de Datos II", (byte) 5);

        // =========================
        // S6
        // =========================
        Subject estadistica = new Subject("33", "Estadística y Probabilidad", (byte) 6);
        Subject lenguajes = new Subject("34", "Estructuras de Lenguajes", (byte) 6);
        Subject lab_lenguajes = new Subject("35", "Lab. Estructuras de Lenguajes", (byte) 6);
        Subject sw2 = new Subject("36", "Ingeniería de Software II", (byte) 6);
        Subject lab_sw2 = new Subject("37", "Lab. Ingeniería de Software II", (byte) 6);
        Subject so = new Subject("38", "Sistemas Operativos", (byte) 6);
        Subject lab_so = new Subject("39", "Lab. Sistemas Operativos", (byte) 6);

        // =========================
        // S7
        // =========================
        Subject teoria_dinamica = new Subject("40", "Teoria y Dinámica de Sistemas", (byte) 7);
        Subject metodologia = new Subject("41", "Metodología de la Investigación", (byte) 7);
        Subject ia = new Subject("42", "Inteligencia Artificial", (byte) 7);
        Subject distribuidos = new Subject("43", "Sistemas Distribuidos", (byte) 7);
        Subject lab_distribuidos = new Subject("44", "Lab. Sistemas Distribuidos", (byte) 7);
        Subject sw3 = new Subject("45", "Ingeniería de Software III", (byte) 7);
        Subject lab_sw3 = new Subject("46", "Lab. Ingeniería de Software III", (byte) 7);
        // =========================
        // S8
        // =========================
        Subject calidad_de_software = new Subject("47", "Calidad de Software", (byte) 8);
        Subject investigacion_de_operacion = new Subject("48", "Investigación de Operaciones", (byte) 8);
        Subject proyecto_1 = new Subject("49", "Proyecto I", (byte) 8);
        Subject redes = new Subject("50", "Redes", (byte) 8);

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

        // all subjects
        allSubjects.addAll(Set.of(
                calculo1, lectura, fish1, intro_sistemas, intro_info, lab_intro_info,
                fish2, calculo2, mecanica, lab_mecanica, algebra, poo, lab_poo,
                calculo3, electromagnetismo, lab_electro, fish3, ed1, lab_ed1,
                ecuaciones, vibraciones, ed2, lab_ed2, bd1, lab_bd1,
                analisis_num, teoria_comp, lab_sw1, sw1, arq_comp, bd2, lab_bd2,
                estadistica, lenguajes, lab_lenguajes, sw2, lab_sw2, so, lab_so,
                teoria_dinamica, metodologia, ia, distribuidos, lab_distribuidos, sw3, lab_sw3,
                calidad_de_software, investigacion_de_operacion,
                proyecto_1, redes));
    }

    /**
     * Returns all subjects in the study plan.
     *
     * @return a new set containing all subjects
     */
    @Override
    public Set<Subject> findAll() {
        return new HashSet<>(allSubjects);
    }

    /**
     * Finds a subject by its ID.
     *
     * @param id the ID of the subject to find
     * @return an Optional containing the subject if found
     */
    @Override
    public Optional<Subject> findById(String id) {
        return allSubjects.stream()
                .filter(subject -> subject.getId().equals(id))
                .findFirst();
    }

    /**
     * Finds multiple subjects by their IDs.
     *
     * @param ids a set of subject IDs to find
     * @return a set of found subjects
     */
    @Override
    public Set<Subject> findByIds(Set<String> ids) {
        Set<Subject> result = new HashSet<>();
        for (String id : ids) {
            findById(id).ifPresent(result::add);
        }
        return result;
    }
}
