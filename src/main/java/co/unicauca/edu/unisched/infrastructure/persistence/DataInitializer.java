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
                if (!groupRepository.findBySubjectId("1").isEmpty()) {
                        return; // Data already initialized
                }

                // Get subjects from study plan
                // =========================
                // S1
                // =========================
                Subject calculo1 = subjectRepository.findById("11465").orElse(null);
                Subject lectura = subjectRepository.findById("21505").orElse(null);
                Subject intro_sistemas = subjectRepository.findById("11479").orElse(null);
                Subject intro_info = subjectRepository.findById("11477").orElse(null);
                Subject lab_intro_info = subjectRepository.findById("11478").orElse(null);


                // =========================
                // S2
                // =========================
                Subject calculo2 = subjectRepository.findById("8").orElse(null);
                Subject mecanica = subjectRepository.findById("9").orElse(null);
                Subject lab_mecanica = subjectRepository.findById("10").orElse(null);
                Subject algebra = subjectRepository.findById("11").orElse(null);
                Subject poo = subjectRepository.findById("12").orElse(null);
                Subject lab_poo = subjectRepository.findById("13").orElse(null);

                // =========================
                // S3
                // =========================
                Subject calculo3 = subjectRepository.findById("14").orElse(null);
                Subject electromagnetismo = subjectRepository.findById("15").orElse(null);
                Subject lab_electro = subjectRepository.findById("16").orElse(null);
                Subject ed1 = subjectRepository.findById("18").orElse(null);
                Subject lab_ed1 = subjectRepository.findById("19").orElse(null);

                // =========================
                // S4
                // =========================
                Subject ecuaciones = subjectRepository.findById("20").orElse(null);
                Subject vibraciones = subjectRepository.findById("21").orElse(null);
                Subject ed2 = subjectRepository.findById("22").orElse(null);
                Subject lab_ed2 = subjectRepository.findById("23").orElse(null);
                Subject bd1 = subjectRepository.findById("24").orElse(null);
                Subject lab_bd1 = subjectRepository.findById("25").orElse(null);

                // =========================
                // S5
                // =========================
                Subject analisis_num = subjectRepository.findById("26").orElse(null);
                Subject teoria_comp = subjectRepository.findById("27").orElse(null);
                Subject lab_sw1 = subjectRepository.findById("28").orElse(null);
                Subject sw1 = subjectRepository.findById("29").orElse(null);
                Subject arq_comp = subjectRepository.findById("30").orElse(null);
                Subject bd2 = subjectRepository.findById("31").orElse(null);
                Subject lab_bd2 = subjectRepository.findById("32").orElse(null);

                // =========================
                // S6
                // =========================
                Subject estadistica = subjectRepository.findById("33").orElse(null);
                Subject lenguajes = subjectRepository.findById("34").orElse(null);
                Subject lab_lenguajes = subjectRepository.findById("35").orElse(null);
                Subject sw2 = subjectRepository.findById("36").orElse(null);
                Subject lab_sw2 = subjectRepository.findById("37").orElse(null);
                Subject so = subjectRepository.findById("38").orElse(null);
                Subject lab_so = subjectRepository.findById("39").orElse(null);

                // =========================
                // S7
                // =========================
                Subject teoria_dinamica = subjectRepository.findById("40").orElse(null);
                Subject metodologia = subjectRepository.findById("41").orElse(null);
                Subject ia = subjectRepository.findById("42").orElse(null);
                Subject distribuidos = subjectRepository.findById("43").orElse(null);
                Subject lab_distribuidos = subjectRepository.findById("44").orElse(null);
                Subject sw3 = subjectRepository.findById("45").orElse(null);
                Subject lab_sw3 = subjectRepository.findById("46").orElse(null);

                // =========================
                // S8
                // =========================
                Subject calidad_de_software = subjectRepository.findById("47").orElse(null);
                Subject investigacion_de_operacion = subjectRepository.findById("48").orElse(null);
                Subject proyecto_1 = subjectRepository.findById("49").orElse(null);
                Subject redes = subjectRepository.findById("50").orElse(null);

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


                // Lectura y Escritura
                if (lectura != null) {
                        createGroup(lectura, "L", "HECTOR WILMER MARTÍNEZ ORTEGA",
                                        List.of(
                                                        new Schedule(DayOfWeek.MONDAY, LocalTime.of(18, 0),
                                                                        LocalTime.of(20, 0)),
                                                        new Schedule(DayOfWeek.WEDNESDAY, LocalTime.of(18, 0),
                                                                        LocalTime.of(20, 0))));

                        createGroup(lectura, "M", "VICTOR ANDRES RIVERA FERNANDEZ",
                                        List.of(
                                                        new Schedule(DayOfWeek.MONDAY, LocalTime.of(16, 0),
                                                                        LocalTime.of(18, 0)),
                                                        new Schedule(DayOfWeek.THURSDAY, LocalTime.of(14, 0),
                                                                        LocalTime.of(16, 0))));
                }

                // Introducción a la Ingeniería de Sistemas
                if (intro_sistemas != null) {
                        createGroup(intro_sistemas, "A", "VANESSA AGREDO DELGADO",
                                        List.of(
                                                        new Schedule(DayOfWeek.TUESDAY, LocalTime.of(14, 0),
                                                                        LocalTime.of(16, 0))));

                        createGroup(intro_sistemas, "B", "VANESSA AGREDO DELGADO",
                                        List.of(
                                                        new Schedule(DayOfWeek.THURSDAY, LocalTime.of(14, 0),
                                                                        LocalTime.of(16, 0))));
                }

                // Introducción a la Informática
                if (intro_info != null) {
                        createGroup(intro_info, "A", "JORGE JAIR MORENO CHAUSTRE, ALEXIS SANTIAGO MARTINEZ SILVA",
                                        List.of(
                                                        new Schedule(DayOfWeek.MONDAY, LocalTime.of(14, 0),
                                                                        LocalTime.of(16, 0)),
                                                        new Schedule(DayOfWeek.TUESDAY, LocalTime.of(14, 0),
                                                                        LocalTime.of(16, 0))));

                        createGroup(intro_info, "B", "JULIAN ANDRES GIL PRADO",
                                        List.of(
                                                        new Schedule(DayOfWeek.TUESDAY, LocalTime.of(7, 0),
                                                                        LocalTime.of(9, 0)),
                                                        new Schedule(DayOfWeek.WEDNESDAY, LocalTime.of(9, 0),
                                                                        LocalTime.of(11, 0))));

                        createGroup(intro_info, "C", "KATERINE MARCELES VILLALBA",
                                        List.of(
                                                        new Schedule(DayOfWeek.TUESDAY, LocalTime.of(7, 0),
                                                                        LocalTime.of(9, 0)),
                                                        new Schedule(DayOfWeek.WEDNESDAY, LocalTime.of(9, 0),
                                                                        LocalTime.of(11, 0))));
                }

                // Laboratorio de Introducción a la Informática
                if (lab_intro_info != null) {
                        createGroup(lab_intro_info, "A", "CARLOS ALBERTO COBOS LOZADA, ALEXIS SANTIAGO MARTINEZ SILVA",
                                        List.of(
                                                        new Schedule(DayOfWeek.THURSDAY, LocalTime.of(7, 0),
                                                                        LocalTime.of(9, 0))));

                        createGroup(lab_intro_info, "B", "RUBEN DARIO VARGAS YANDY",
                                        List.of(
                                                        new Schedule(DayOfWeek.THURSDAY, LocalTime.of(9, 0),
                                                                        LocalTime.of(11, 0))));

                        createGroup(lab_intro_info, "C", "LAURA MARÍA OROZCO GARCÍA, ISABELLA OMEN RENGIFO",
                                        List.of(
                                                        new Schedule(DayOfWeek.WEDNESDAY, LocalTime.of(11, 0),
                                                                        LocalTime.of(13, 0))));

                        createGroup(lab_intro_info, "D", "ANDRES FELIPE CRUZ ERAZO",
                                        List.of(
                                                        new Schedule(DayOfWeek.THURSDAY, LocalTime.of(16, 0),
                                                                        LocalTime.of(18, 0))));
                }

                // Álgebra Lineal
                if (algebra != null) {
                        createGroup(algebra, "A", "MARIA DEL PILAR ASTUDILLO FERNANDEZ, EDUARD MAURICIO MACIAS CAICEDO",
                                        List.of(
                                                        new Schedule(DayOfWeek.TUESDAY, LocalTime.of(11, 0),
                                                                        LocalTime.of(13, 0)),
                                                        new Schedule(DayOfWeek.THURSDAY, LocalTime.of(11, 0),
                                                                        LocalTime.of(13, 0))));

                        createGroup(algebra, "B", "JOSE LUIS HERRERA BRAVO",
                                        List.of(
                                                        new Schedule(DayOfWeek.MONDAY, LocalTime.of(14, 0),
                                                                        LocalTime.of(16, 0)),
                                                        new Schedule(DayOfWeek.FRIDAY, LocalTime.of(14, 0),
                                                                        LocalTime.of(16, 0))));

                        createGroup(algebra, "C", "TULIO EMIRO LOPEZ ERAZO",
                                        List.of(
                                                        new Schedule(DayOfWeek.TUESDAY, LocalTime.of(9, 0),
                                                                        LocalTime.of(11, 0)),
                                                        new Schedule(DayOfWeek.THURSDAY, LocalTime.of(9, 0),
                                                                        LocalTime.of(11, 0))));
                }

                // Cálculo II
                if (calculo2 != null) {
                        createGroup(calculo2, "A", "TULIO EMIRO LOPEZ ERAZO",
                                        List.of(
                                                        new Schedule(DayOfWeek.WEDNESDAY, LocalTime.of(11, 0),
                                                                        LocalTime.of(13, 0)),
                                                        new Schedule(DayOfWeek.FRIDAY, LocalTime.of(11, 0),
                                                                        LocalTime.of(13, 0))));

                        createGroup(calculo2, "B", "ALVARO FELIPE GALINDEZ HURTADO",
                                        List.of(
                                                        new Schedule(DayOfWeek.WEDNESDAY, LocalTime.of(14, 0),
                                                                        LocalTime.of(16, 0)),
                                                        new Schedule(DayOfWeek.FRIDAY, LocalTime.of(9, 0),
                                                                        LocalTime.of(11, 0))));

                        createGroup(calculo2, "C", "ROSANA PEREZ MERA",
                                        List.of(
                                                        new Schedule(DayOfWeek.TUESDAY, LocalTime.of(11, 0),
                                                                        LocalTime.of(13, 0)),
                                                        new Schedule(DayOfWeek.THURSDAY, LocalTime.of(11, 0),
                                                                        LocalTime.of(13, 0))));
                }

                // Mecánica
                if (mecanica != null) {
                        createGroup(mecanica, "F1", "HERMES FABIAN VARGAS ROSERO",
                                        List.of(
                                                        new Schedule(DayOfWeek.WEDNESDAY, LocalTime.of(11, 0),
                                                                        LocalTime.of(13, 0)),
                                                        new Schedule(DayOfWeek.FRIDAY, LocalTime.of(9, 0),
                                                                        LocalTime.of(11, 0))));

                        createGroup(mecanica, "F", "OBER HERNAN LIZARDO",
                                        List.of(
                                                        new Schedule(DayOfWeek.MONDAY, LocalTime.of(9, 0),
                                                                        LocalTime.of(11, 0)),
                                                        new Schedule(DayOfWeek.WEDNESDAY, LocalTime.of(7, 0),
                                                                        LocalTime.of(9, 0))));
                }

                // Laboratorio de Mecánica
                if (lab_mecanica != null) {
                        createGroup(lab_mecanica, "A", "JORGE WASINTONG CORONEL",
                                        List.of(
                                                        new Schedule(DayOfWeek.FRIDAY, LocalTime.of(11, 0),
                                                                        LocalTime.of(13, 0))));

                        createGroup(lab_mecanica, "B", "JAZMIN CALVACHE MUÑOZ",
                                        List.of(
                                                        new Schedule(DayOfWeek.FRIDAY, LocalTime.of(9, 0),
                                                                        LocalTime.of(11, 0))));

                        createGroup(lab_mecanica, "C", "JAZMIN CALVACHE MUÑOZ",
                                        List.of(
                                                        new Schedule(DayOfWeek.WEDNESDAY, LocalTime.of(14, 0),
                                                                        LocalTime.of(16, 0))));
                }

                // Programación Orientada a Objetos
                if (poo != null) {
                        createGroup(poo, "A", "VANESSA AGREDO DELGADO",
                                        List.of(
                                                        new Schedule(DayOfWeek.TUESDAY, LocalTime.of(16, 0),
                                                                        LocalTime.of(18, 0)),
                                                        new Schedule(DayOfWeek.THURSDAY, LocalTime.of(16, 0),
                                                                        LocalTime.of(18, 0))));

                        createGroup(poo, "B", "JUAN CARLOS NARVAEZ NARVAEZ",
                                        List.of(
                                                        new Schedule(DayOfWeek.WEDNESDAY, LocalTime.of(9, 0),
                                                                        LocalTime.of(11, 0)),
                                                        new Schedule(DayOfWeek.THURSDAY, LocalTime.of(14, 0),
                                                                        LocalTime.of(16, 0))));
                }

                // Laboratorio de Programación Orientada a Objetos
                if (lab_poo != null) {
                        createGroup(lab_poo, "A", "IVAN SANTIAGO HERRERA BRAVO",
                                        List.of(
                                                        new Schedule(DayOfWeek.WEDNESDAY, LocalTime.of(7, 0),
                                                                        LocalTime.of(9, 0))));

                        createGroup(lab_poo, "B", "MARITZA MERA GAONA",
                                        List.of(
                                                        new Schedule(DayOfWeek.FRIDAY, LocalTime.of(11, 0),
                                                                        LocalTime.of(13, 0))));

                        createGroup(lab_poo, "C", "WILLINTON FIDEL ORTIZ FAJARDO",
                                        List.of(
                                                        new Schedule(DayOfWeek.FRIDAY, LocalTime.of(7, 0),
                                                                        LocalTime.of(9, 0))));

                        createGroup(lab_poo, "D", "IVAN SANTIAGO HERRERA BRAVO",
                                        List.of(
                                                        new Schedule(DayOfWeek.FRIDAY, LocalTime.of(7, 0),
                                                                        LocalTime.of(9, 0))));
                }

                // Cálculo III
                if (calculo3 != null) {
                        createGroup(calculo3, "1A", "JUAN DAVID SAMBONI CHICANGANA",
                                        List.of(
                                                        new Schedule(DayOfWeek.WEDNESDAY, LocalTime.of(14, 0),
                                                                        LocalTime.of(16, 0)),
                                                        new Schedule(DayOfWeek.FRIDAY, LocalTime.of(7, 0),
                                                                        LocalTime.of(9, 0))));

                        createGroup(calculo3, "1B", "WILMER SANCHEZ GRUESO",
                                        List.of(
                                                        new Schedule(DayOfWeek.WEDNESDAY, LocalTime.of(14, 0),
                                                                        LocalTime.of(16, 0)),
                                                        new Schedule(DayOfWeek.FRIDAY, LocalTime.of(14, 0),
                                                                        LocalTime.of(16, 0))));

                        createGroup(calculo3, "1C", "JUAN DAVID SAMBONI CHICANGANA",
                                        List.of(
                                                        new Schedule(DayOfWeek.TUESDAY, LocalTime.of(16, 0),
                                                                        LocalTime.of(18, 0)),
                                                        new Schedule(DayOfWeek.THURSDAY, LocalTime.of(16, 0),
                                                                        LocalTime.of(18, 0))));
                }

                // Electromagnetismo
                if (electromagnetismo != null) {
                        createGroup(electromagnetismo, "C", "DELIO EDUARDO ENRIQUEZ CABRERA",
                                        List.of(
                                                        new Schedule(DayOfWeek.MONDAY, LocalTime.of(16, 0),
                                                                        LocalTime.of(18, 0)),
                                                        new Schedule(DayOfWeek.WEDNESDAY, LocalTime.of(9, 0),
                                                                        LocalTime.of(11, 0))));

                        createGroup(electromagnetismo, "A", "JHON ALEJANDRO ANDRADE",
                                        List.of(
                                                        new Schedule(DayOfWeek.MONDAY, LocalTime.of(16, 0),
                                                                        LocalTime.of(18, 0)),
                                                        new Schedule(DayOfWeek.FRIDAY, LocalTime.of(9, 0),
                                                                        LocalTime.of(11, 0))));

                        createGroup(electromagnetismo, "B", "CAMILO SANCHEZ FERREIRA",
                                        List.of(
                                                        new Schedule(DayOfWeek.MONDAY, LocalTime.of(7, 0),
                                                                        LocalTime.of(9, 0)),
                                                        new Schedule(DayOfWeek.FRIDAY, LocalTime.of(16, 0),
                                                                        LocalTime.of(18, 0))));
                }

                // Laboratorio de Electromagnetismo
                if (lab_electro != null) {
                        createGroup(lab_electro, "A", "CARLOS FELIPE ORDONEZ URBANO",
                                        List.of(
                                                        new Schedule(DayOfWeek.MONDAY, LocalTime.of(7, 0),
                                                                        LocalTime.of(9, 0))));

                        createGroup(lab_electro, "B", "OBER HERNAN LIZARDO",
                                        List.of(
                                                        new Schedule(DayOfWeek.TUESDAY, LocalTime.of(7, 0),
                                                                        LocalTime.of(9, 0))));

                        createGroup(lab_electro, "C", "GILBERTO BOLAÑOS PANTOJA",
                                        List.of(
                                                        new Schedule(DayOfWeek.THURSDAY, LocalTime.of(7, 0),
                                                                        LocalTime.of(9, 0))));
                }

                // Estructuras de Datos I
                if (ed1 != null) {
                        createGroup(ed1, "A", "JULIAN ANDRES GIL PRADO",
                                        List.of(
                                                        new Schedule(DayOfWeek.MONDAY, LocalTime.of(9, 0),
                                                                        LocalTime.of(11, 0)),
                                                        new Schedule(DayOfWeek.FRIDAY, LocalTime.of(9, 0),
                                                                        LocalTime.of(11, 0))));

                        createGroup(ed1, "B", "JORGE JAIR MORENO CHAUSTRE",
                                        List.of(
                                                        new Schedule(DayOfWeek.MONDAY, LocalTime.of(11, 0),
                                                                        LocalTime.of(13, 0)),
                                                        new Schedule(DayOfWeek.WEDNESDAY, LocalTime.of(7, 0),
                                                                        LocalTime.of(9, 0))));
                }

                // Laboratorio de Estructuras de Datos I
                if (lab_ed1 != null) {
                        createGroup(lab_ed1, "A", "JORGE JAIR MORENO CHAUSTRE",
                                        List.of(
                                                        new Schedule(DayOfWeek.TUESDAY, LocalTime.of(11, 0),
                                                                        LocalTime.of(13, 0))));

                        createGroup(lab_ed1, "B", "RENE FABIAN ZUÑIGA MUÑOZ",
                                        List.of(
                                                        new Schedule(DayOfWeek.WEDNESDAY, LocalTime.of(7, 0),
                                                                        LocalTime.of(9, 0))));

                        createGroup(lab_ed1, "C", "JORGE JAIR MORENO CHAUSTRE",
                                        List.of(
                                                        new Schedule(DayOfWeek.TUESDAY, LocalTime.of(9, 0),
                                                                        LocalTime.of(11, 0))));
                }

                // Bases de Datos I
                if (bd1 != null) {
                        createGroup(bd1, "A", "JIMENA ADRIANA TIMANA PEÑA",
                                        List.of(
                                                        new Schedule(DayOfWeek.TUESDAY, LocalTime.of(7, 0),
                                                                        LocalTime.of(9, 0)),
                                                        new Schedule(DayOfWeek.THURSDAY, LocalTime.of(7, 0),
                                                                        LocalTime.of(9, 0))));

                        createGroup(bd1, "B", "MARTHA MENDOZA",
                                        List.of(
                                                        new Schedule(DayOfWeek.TUESDAY, LocalTime.of(9, 0),
                                                                        LocalTime.of(11, 0)),
                                                        new Schedule(DayOfWeek.THURSDAY, LocalTime.of(9, 0),
                                                                        LocalTime.of(11, 0))));
                }

                // Ecuaciones Diferenciales Ordinarias
                if (ecuaciones != null) {
                        createGroup(ecuaciones, "A", "LUIS FELIPE NARVAEZ PLAZA",
                                        List.of(
                                                        new Schedule(DayOfWeek.TUESDAY, LocalTime.of(11, 0),
                                                                        LocalTime.of(13, 0)),
                                                        new Schedule(DayOfWeek.THURSDAY, LocalTime.of(11, 0),
                                                                        LocalTime.of(13, 0))));

                        createGroup(ecuaciones, "B", "ERIC FERNANDO BRAVO MONTENEGRO",
                                        List.of(
                                                        new Schedule(DayOfWeek.WEDNESDAY, LocalTime.of(7, 0),
                                                                        LocalTime.of(9, 0)),
                                                        new Schedule(DayOfWeek.FRIDAY, LocalTime.of(11, 0),
                                                                        LocalTime.of(13, 0))));

                        createGroup(ecuaciones, "C", "JAIME TOBAR MUÑOZ",
                                        List.of(
                                                        new Schedule(DayOfWeek.WEDNESDAY, LocalTime.of(7, 0),
                                                                        LocalTime.of(9, 0)),
                                                        new Schedule(DayOfWeek.FRIDAY, LocalTime.of(9, 0),
                                                                        LocalTime.of(11, 0))));
                }

                // Vibraciones y Ondas
                if (vibraciones != null) {
                        createGroup(vibraciones, "A", "CAMILO SANCHEZ FERREIRA",
                                        List.of(
                                                        new Schedule(DayOfWeek.TUESDAY, LocalTime.of(9, 0),
                                                                        LocalTime.of(11, 0)),
                                                        new Schedule(DayOfWeek.THURSDAY, LocalTime.of(14, 0),
                                                                        LocalTime.of(16, 0))));

                        createGroup(vibraciones, "B", "CAMILO SANCHEZ FERREIRA",
                                        List.of(
                                                        new Schedule(DayOfWeek.WEDNESDAY, LocalTime.of(7, 0),
                                                                        LocalTime.of(9, 0)),
                                                        new Schedule(DayOfWeek.THURSDAY, LocalTime.of(16, 0),
                                                                        LocalTime.of(18, 0))));
                }

                // Estructura de Datos II
                if (ed2 != null) {
                        createGroup(ed2, "A", "CARLOS ALBERTO ARDILA ALBARRACIN",
                                        List.of(
                                                        new Schedule(DayOfWeek.MONDAY, LocalTime.of(16, 0),
                                                                        LocalTime.of(18, 0)),
                                                        new Schedule(DayOfWeek.FRIDAY, LocalTime.of(9, 0),
                                                                        LocalTime.of(11, 0))));

                        createGroup(ed2, "B", "PABLO HERNANDO RUIZ MELENJE",
                                        List.of(
                                                        new Schedule(DayOfWeek.MONDAY, LocalTime.of(11, 0),
                                                                        LocalTime.of(13, 0)),
                                                        new Schedule(DayOfWeek.TUESDAY, LocalTime.of(7, 0),
                                                                        LocalTime.of(9, 0))));
                }

                // Laboratorio de Estructuras de Datos II
                if (lab_ed2 != null) {
                        createGroup(lab_ed2, "A", "PABLO HERNANDO RUIZ MELENJE",
                                        List.of(
                                                        new Schedule(DayOfWeek.WEDNESDAY, LocalTime.of(11, 0),
                                                                        LocalTime.of(13, 0))));

                        createGroup(lab_ed2, "B", "RENE FABIAN ZUÑIGA MUÑOZ",
                                        List.of(
                                                        new Schedule(DayOfWeek.THURSDAY, LocalTime.of(7, 0),
                                                                        LocalTime.of(9, 0))));

                        createGroup(lab_ed2, "C", "PABLO HERNANDO RUIZ MELENJE",
                                        List.of(
                                                        new Schedule(DayOfWeek.WEDNESDAY, LocalTime.of(9, 0),
                                                                        LocalTime.of(11, 0))));
                }

                // Laboratorio de Bases de Datos I
                if (lab_bd1 != null) {
                        createGroup(lab_bd1, "A", "MARITZA FERNANDA MERA GAONA",
                                        List.of(
                                                        new Schedule(DayOfWeek.FRIDAY, LocalTime.of(14, 0),
                                                                        LocalTime.of(16, 0))));

                        createGroup(lab_bd1, "B", "JIMENA ADRIANA TIMANA PEÑA",
                                        List.of(
                                                        new Schedule(DayOfWeek.TUESDAY, LocalTime.of(14, 0),
                                                                        LocalTime.of(16, 0))));

                        createGroup(lab_bd1, "C", "ALEJANDRA VARGAS MUÑOZ",
                                        List.of(
                                                        new Schedule(DayOfWeek.FRIDAY, LocalTime.of(7, 0),
                                                                        LocalTime.of(9, 0))));
                }

                // Análisis Numérico
                if (analisis_num != null) {
                        createGroup(analisis_num, "A", "CARLOS ALBERTO ARDILA ALBARRACIN",
                                        List.of(
                                                        new Schedule(DayOfWeek.MONDAY, LocalTime.of(14, 0),
                                                                        LocalTime.of(16, 0)),
                                                        new Schedule(DayOfWeek.THURSDAY, LocalTime.of(16, 0),
                                                                        LocalTime.of(18, 0))));

                        createGroup(analisis_num, "B", "CARLOS ALBERTO ARDILA ALBARRACIN",
                                        List.of(
                                                        new Schedule(DayOfWeek.TUESDAY, LocalTime.of(14, 0),
                                                                        LocalTime.of(16, 0)),
                                                        new Schedule(DayOfWeek.FRIDAY, LocalTime.of(11, 0),
                                                                        LocalTime.of(13, 0))));
                }

                // Arquitectura Computacional
                if (arq_comp != null) {
                        createGroup(arq_comp, "A",
                                        "IVAN EDUARDO HERNANDEZ DELGADO, ADOLFO LEON PLAZAS TENORIO, FULVIO YESID VIVAS CANTERO",
                                        List.of(
                                                        new Schedule(DayOfWeek.TUESDAY, LocalTime.of(11, 0),
                                                                        LocalTime.of(13, 0)),
                                                        new Schedule(DayOfWeek.WEDNESDAY, LocalTime.of(11, 0),
                                                                        LocalTime.of(13, 0)),
                                                        new Schedule(DayOfWeek.THURSDAY, LocalTime.of(11, 0),
                                                                        LocalTime.of(13, 0))));

                        createGroup(arq_comp, "B",
                                        "IVAN EDUARDO HERNANDEZ DELGADO, ADOLFO LEON PLAZAS TENORIO, FULVIO YESID VIVAS CANTERO",
                                        List.of(
                                                        new Schedule(DayOfWeek.MONDAY, LocalTime.of(11, 0),
                                                                        LocalTime.of(13, 0)),
                                                        new Schedule(DayOfWeek.TUESDAY, LocalTime.of(7, 0),
                                                                        LocalTime.of(9, 0)),
                                                        new Schedule(DayOfWeek.FRIDAY, LocalTime.of(16, 0),
                                                                        LocalTime.of(18, 0))));
                }

                // Bases de Datos II
                if (bd2 != null) {
                        createGroup(bd2, "A", "SARA DONNELLY GARCES AGREDO",
                                        List.of(
                                                        new Schedule(DayOfWeek.MONDAY, LocalTime.of(7, 0),
                                                                        LocalTime.of(9, 0)),
                                                        new Schedule(DayOfWeek.WEDNESDAY, LocalTime.of(7, 0),
                                                                        LocalTime.of(9, 0))));

                        createGroup(bd2, "B", "FRANCISCO JAVIER OBANDO VIDAL",
                                        List.of(
                                                        new Schedule(DayOfWeek.TUESDAY, LocalTime.of(7, 0),
                                                                        LocalTime.of(9, 0)),
                                                        new Schedule(DayOfWeek.FRIDAY, LocalTime.of(11, 0),
                                                                        LocalTime.of(13, 0))));
                }

                // Ingeniería de Software I
                if (sw1 != null) {
                        createGroup(sw1, "A", "SANDRA LORENA BUITRON RUIZ",
                                        List.of(
                                                        new Schedule(DayOfWeek.THURSDAY, LocalTime.of(14, 0),
                                                                        LocalTime.of(16, 0)),
                                                        new Schedule(DayOfWeek.FRIDAY, LocalTime.of(7, 0),
                                                                        LocalTime.of(9, 0))));

                        createGroup(sw1, "B", "JORGE JAIR MORENO CHAUSTRE",
                                        List.of(
                                                        new Schedule(DayOfWeek.MONDAY, LocalTime.of(9, 0),
                                                                        LocalTime.of(11, 0)),
                                                        new Schedule(DayOfWeek.FRIDAY, LocalTime.of(9, 0),
                                                                        LocalTime.of(11, 0))));
                }

                // Laboratorio de Ingeniería de Software I
                if (lab_sw1 != null) {
                        createGroup(lab_sw1, "A", "RENE FABIAN ZUÑIGA MUÑOZ",
                                        List.of(
                                                        new Schedule(DayOfWeek.TUESDAY, LocalTime.of(9, 0),
                                                                        LocalTime.of(11, 0))));

                        createGroup(lab_sw1, "B", "JORGE JAIR MORENO CHAUSTRE",
                                        List.of(
                                                        new Schedule(DayOfWeek.TUESDAY, LocalTime.of(16, 0),
                                                                        LocalTime.of(18, 0))));

                        createGroup(lab_sw1, "C", "RENE FABIAN ZUÑIGA MUÑOZ",
                                        List.of(
                                                        new Schedule(DayOfWeek.THURSDAY, LocalTime.of(11, 0),
                                                                        LocalTime.of(13, 0))));
                }

                // Laboratorio de Bases de Datos II
                if (lab_bd2 != null) {
                        createGroup(lab_bd2, "A", "SARA DONNELLY GARCES AGREDO",
                                        List.of(
                                                        new Schedule(DayOfWeek.WEDNESDAY, LocalTime.of(9, 0),
                                                                        LocalTime.of(11, 0))));

                        createGroup(lab_bd2, "B", "MARITZA FERNANDA MERA GAONA",
                                        List.of(
                                                        new Schedule(DayOfWeek.FRIDAY, LocalTime.of(7, 0),
                                                                        LocalTime.of(9, 0))));

                        createGroup(lab_bd2, "C", "SARA DONNELLY GARCES AGREDO",
                                        List.of(
                                                        new Schedule(DayOfWeek.THURSDAY, LocalTime.of(7, 0),
                                                                        LocalTime.of(9, 0))));
                }

                // Teoría de la Computación
                if (teoria_comp != null) {
                        createGroup(teoria_comp, "A", "RICARDO ANTONIO ZAMBRANO SEGURA",
                                        List.of(
                                                        new Schedule(DayOfWeek.TUESDAY, LocalTime.of(14, 0),
                                                                        LocalTime.of(16, 0)),
                                                        new Schedule(DayOfWeek.THURSDAY, LocalTime.of(14, 0),
                                                                        LocalTime.of(16, 0))));
                }

                // Estadística y Probabilidad
                if (estadistica != null) {
                        createGroup(estadistica, "A", "EDWIN CHAMORRO IBARRA",
                                        List.of(
                                                        new Schedule(DayOfWeek.TUESDAY, LocalTime.of(9, 0),
                                                                        LocalTime.of(11, 0)),
                                                        new Schedule(DayOfWeek.FRIDAY, LocalTime.of(11, 0),
                                                                        LocalTime.of(13, 0))));

                        createGroup(estadistica, "B", "MARIO ELCIAS MUÑOZ OCHOA",
                                        List.of(
                                                        new Schedule(DayOfWeek.TUESDAY, LocalTime.of(11, 0),
                                                                        LocalTime.of(13, 0)),
                                                        new Schedule(DayOfWeek.FRIDAY, LocalTime.of(11, 0),
                                                                        LocalTime.of(13, 0))));
                }

                // Estructura de Lenguajes
                if (lenguajes != null) {
                        createGroup(lenguajes, "A", "JIMENA ADRIANA TIMANA PEÑA",
                                        List.of(
                                                        new Schedule(DayOfWeek.MONDAY, LocalTime.of(7, 0),
                                                                        LocalTime.of(9, 0)),
                                                        new Schedule(DayOfWeek.THURSDAY, LocalTime.of(14, 0),
                                                                        LocalTime.of(16, 0))));

                        createGroup(lenguajes, "B", "PABLO HERNANDO RUIZ MELENJE",
                                        List.of(
                                                        new Schedule(DayOfWeek.TUESDAY, LocalTime.of(16, 0),
                                                                        LocalTime.of(18, 0)),
                                                        new Schedule(DayOfWeek.FRIDAY, LocalTime.of(11, 0),
                                                                        LocalTime.of(13, 0))));
                }

                // Ingeniería de Software II
                if (sw2 != null) {
                        createGroup(sw2, "A", "WILSON LIBARDO PANTOJA YEPEZ",
                                        List.of(
                                                        new Schedule(DayOfWeek.MONDAY, LocalTime.of(16, 0),
                                                                        LocalTime.of(18, 0)),
                                                        new Schedule(DayOfWeek.WEDNESDAY, LocalTime.of(7, 0),
                                                                        LocalTime.of(9, 0))));

                        createGroup(sw2, "B", "BRAYAN DANIEL PERDOMO URBANO",
                                        List.of(
                                                        new Schedule(DayOfWeek.MONDAY, LocalTime.of(14, 0),
                                                                        LocalTime.of(16, 0)),
                                                        new Schedule(DayOfWeek.WEDNESDAY, LocalTime.of(9, 0),
                                                                        LocalTime.of(11, 0))));
                }

                // Laboratorio de Estructura de Lenguajes
                if (lab_lenguajes != null) {
                        createGroup(lab_lenguajes, "A", "JIMENA ADRIANA TIMANA PEÑA",
                                        List.of(
                                                        new Schedule(DayOfWeek.WEDNESDAY, LocalTime.of(14, 0),
                                                                        LocalTime.of(16, 0))));

                        createGroup(lab_lenguajes, "B", "PABLO HERNANDO RUIZ MELENJE",
                                        List.of(
                                                        new Schedule(DayOfWeek.THURSDAY, LocalTime.of(14, 0),
                                                                        LocalTime.of(16, 0))));
                }

                // Laboratorio de Ingeniería de Software II
                if (lab_sw2 != null) {
                        createGroup(lab_sw2, "A", "WILSON LIBARDO PANTOJA YEPEZ",
                                        List.of(
                                                        new Schedule(DayOfWeek.WEDNESDAY, LocalTime.of(7, 0),
                                                                        LocalTime.of(9, 0))));

                        createGroup(lab_sw2, "B", "BRAYAN DANIEL PERDOMO URBANO",
                                        List.of(
                                                        new Schedule(DayOfWeek.FRIDAY, LocalTime.of(16, 0),
                                                                        LocalTime.of(18, 0))));

                        createGroup(lab_sw2, "C", "BRAYAN DANIEL PERDOMO URBANO",
                                        List.of(
                                                        new Schedule(DayOfWeek.THURSDAY, LocalTime.of(14, 0),
                                                                        LocalTime.of(16, 0))));
                }

                // Sistemas Operativos
                if (so != null) {
                        createGroup(so, "A", "PABLO AUGUSTO MAGE IMBACHI",
                                        List.of(
                                                        new Schedule(DayOfWeek.MONDAY, LocalTime.of(9, 0),
                                                                        LocalTime.of(11, 0)),
                                                        new Schedule(DayOfWeek.THURSDAY, LocalTime.of(16, 0),
                                                                        LocalTime.of(18, 0))));
                }

                // Laboratorio de Sistemas Operativos
                if (lab_so != null) {
                        createGroup(lab_so, "A", "PABLO AUGUSTO MAGE IMBACHI",
                                        List.of(
                                                        new Schedule(DayOfWeek.WEDNESDAY, LocalTime.of(9, 0),
                                                                        LocalTime.of(11, 0))));

                        createGroup(lab_so, "B", "PABLO AUGUSTO MAGE IMBACHI",
                                        List.of(
                                                        new Schedule(DayOfWeek.WEDNESDAY, LocalTime.of(9, 0),
                                                                        LocalTime.of(11, 0))));
                }

                // Ingeniería de Software III (sw3)
                if (sw3 != null) {
                        createGroup(sw3, "A", "FRANCISCO JAVIER OBANDO VIDAL",
                                        List.of(
                                                        new Schedule(DayOfWeek.MONDAY, LocalTime.of(14, 0),
                                                                        LocalTime.of(16, 0)),
                                                        new Schedule(DayOfWeek.WEDNESDAY, LocalTime.of(7, 0),
                                                                        LocalTime.of(9, 0))));

                        createGroup(sw3, "B", "WILSON LIBARDO PANTOJA YEPEZ",
                                        List.of(
                                                        new Schedule(DayOfWeek.TUESDAY, LocalTime.of(9, 0),
                                                                        LocalTime.of(11, 0)),
                                                        new Schedule(DayOfWeek.THURSDAY, LocalTime.of(9, 0),
                                                                        LocalTime.of(11, 0))));
                }

                // Laboratorio de Sistemas Distribuidos (lab_distribuidos)
                if (lab_distribuidos != null) {
                        createGroup(lab_distribuidos, "A", "DANIEL EDUARDO PAZ PERAFAN",
                                        List.of(
                                                        new Schedule(DayOfWeek.TUESDAY, LocalTime.of(16, 0),
                                                                        LocalTime.of(18, 0))));

                        createGroup(lab_distribuidos, "B", "DANIEL EDUARDO PAZ PERAFAN",
                                        List.of(
                                                        new Schedule(DayOfWeek.TUESDAY, LocalTime.of(11, 0),
                                                                        LocalTime.of(13, 0))));

                        createGroup(lab_distribuidos, "C", "PABLO AUGUSTO MAGE IMBACHI",
                                        List.of(
                                                        new Schedule(DayOfWeek.WEDNESDAY, LocalTime.of(11, 0),
                                                                        LocalTime.of(13, 0))));
                }

                // Laboratorio de Ingeniería de Software III (lab_sw3)
                if (lab_sw3 != null) {
                        createGroup(lab_sw3, "A", "DANIEL EDUARDO PAZ PERAFAN",
                                        List.of(
                                                        new Schedule(DayOfWeek.THURSDAY, LocalTime.of(14, 0),
                                                                        LocalTime.of(16, 0))));

                        createGroup(lab_sw3, "B", "DANIEL EDUARDO PAZ PERAFAN",
                                        List.of(
                                                        new Schedule(DayOfWeek.THURSDAY, LocalTime.of(11, 0),
                                                                        LocalTime.of(13, 0))));
                }

                // Inteligencia Artificial
                if (ia != null) {
                        createGroup(ia, "A", "MARIA ISABEL VIDAL CAICEDO",
                                        List.of(
                                                        new Schedule(DayOfWeek.MONDAY, LocalTime.of(16, 0),
                                                                        LocalTime.of(18, 0)),
                                                        new Schedule(DayOfWeek.WEDNESDAY, LocalTime.of(14, 0),
                                                                        LocalTime.of(16, 0)),
                                                        new Schedule(DayOfWeek.FRIDAY, LocalTime.of(11, 0),
                                                                        LocalTime.of(13, 0))));

                        createGroup(ia, "B", "MARIA ISABEL VIDAL CAICEDO",
                                        List.of(
                                                        new Schedule(DayOfWeek.MONDAY, LocalTime.of(9, 0),
                                                                        LocalTime.of(11, 0)),
                                                        new Schedule(DayOfWeek.THURSDAY, LocalTime.of(9, 0),
                                                                        LocalTime.of(11, 0)),
                                                        new Schedule(DayOfWeek.FRIDAY, LocalTime.of(9, 0),
                                                                        LocalTime.of(11, 0))));
                }

                // Metodología de la Investigación
                if (metodologia != null) {
                        createGroup(metodologia, "A", "SANDRA MILENA ROA MARTINEZ",
                                        List.of(
                                                        new Schedule(DayOfWeek.MONDAY, LocalTime.of(11, 0),
                                                                        LocalTime.of(13, 0)),
                                                        new Schedule(DayOfWeek.WEDNESDAY, LocalTime.of(11, 0),
                                                                        LocalTime.of(13, 0))));

                        /*
                         * createGroup(metodologia, "B", "SANDRA MILENA ROA MARTINEZ",
                         * List.of(
                         * new Schedule(DayOfWeek.MONDAY, LocalTime.of(11, 0), LocalTime.of(13, 0)),
                         * new Schedule(DayOfWeek.WEDNESDAY, LocalTime.of(11, 0), LocalTime.of(13, 0))
                         * ));
                         */
                }

                // Sistemas Distribuidos
                if (distribuidos != null) {
                        createGroup(distribuidos, "A", "DANIEL EDUARDO PAZ PERAFAN",
                                        List.of(
                                                        new Schedule(DayOfWeek.THURSDAY, LocalTime.of(7, 0),
                                                                        LocalTime.of(9, 0)),
                                                        new Schedule(DayOfWeek.FRIDAY, LocalTime.of(14, 0),
                                                                        LocalTime.of(16, 0))));

                        createGroup(distribuidos, "B", "PABLO AUGUSTO MAGE IMBACHI",
                                        List.of(
                                                        new Schedule(DayOfWeek.TUESDAY, LocalTime.of(14, 0),
                                                                        LocalTime.of(16, 0)),
                                                        new Schedule(DayOfWeek.THURSDAY, LocalTime.of(14, 0),
                                                                        LocalTime.of(16, 0))));
                }

                // Teoría y Dinámica de Sistemas
                if (teoria_dinamica != null) {
                        createGroup(teoria_dinamica, "A", "NESTOR DIAZ MARIÑO,PABLO HERNANDO RUIZ MELENJE",
                                        List.of(
                                                        new Schedule(DayOfWeek.TUESDAY, LocalTime.of(14, 0),
                                                                        LocalTime.of(16, 0)),
                                                        new Schedule(DayOfWeek.THURSDAY, LocalTime.of(16, 0),
                                                                        LocalTime.of(18, 0))));

                        createGroup(teoria_dinamica, "B", "PABLO HERNANDO RUIZ MELENJE",
                                        List.of(
                                                        new Schedule(DayOfWeek.MONDAY, LocalTime.of(14, 0),
                                                                        LocalTime.of(16, 0)),
                                                        new Schedule(DayOfWeek.THURSDAY, LocalTime.of(14, 0),
                                                                        LocalTime.of(16, 0))));
                }

                // =========================
                // S8
                // =========================

                // Calidad de Software
                if (calidad_de_software != null) {
                        createGroup(calidad_de_software, "A", "CARLOS ALBERTO ARDILA ALBARRACIN",
                                        List.of(
                                                        new Schedule(DayOfWeek.WEDNESDAY, LocalTime.of(9, 0),
                                                                        LocalTime.of(11, 0)),
                                                        new Schedule(DayOfWeek.THURSDAY, LocalTime.of(9, 0),
                                                                        LocalTime.of(11, 0))));
                }

                // Investigación de Operaciones
                if (investigacion_de_operacion != null) {
                        createGroup(investigacion_de_operacion, "A", "ARIEL EDMUNDO PABON BURBANO",
                                        List.of(
                                                        new Schedule(DayOfWeek.MONDAY, LocalTime.of(7, 0),
                                                                        LocalTime.of(9, 0)),
                                                        new Schedule(DayOfWeek.THURSDAY, LocalTime.of(7, 0),
                                                                        LocalTime.of(9, 0))));

                        createGroup(investigacion_de_operacion, "B", "ARIEL EDMUNDO PABON BURBANO",
                                        List.of(
                                                        new Schedule(DayOfWeek.WEDNESDAY, LocalTime.of(11, 0),
                                                                        LocalTime.of(13, 0)),
                                                        new Schedule(DayOfWeek.FRIDAY, LocalTime.of(11, 0),
                                                                        LocalTime.of(13, 0))));
                }

                // Proyecto I
                if (proyecto_1 != null) {
                        createGroup(proyecto_1, "A", "MIGUEL ANGEL NIÑO",
                                        List.of(
                                                        new Schedule(DayOfWeek.WEDNESDAY, LocalTime.of(7, 0),
                                                                        LocalTime.of(9, 0)),
                                                        new Schedule(DayOfWeek.FRIDAY, LocalTime.of(9, 0),
                                                                        LocalTime.of(11, 0))));
                }

                // Redes
                if (redes != null) {
                        createGroup(redes, "A",
                                        "EDWIN FERNEY CASTILLO QUINTERO,JOHANNA ANDREA HURTADO SANCHEZ,EMIGDIO ANDRES LARA SILVA",
                                        List.of(
                                                        new Schedule(DayOfWeek.MONDAY, LocalTime.of(11, 0),
                                                                        LocalTime.of(13, 0)),
                                                        new Schedule(DayOfWeek.TUESDAY, LocalTime.of(9, 0),
                                                                        LocalTime.of(11, 0)),
                                                        new Schedule(DayOfWeek.FRIDAY, LocalTime.of(11, 0),
                                                                        LocalTime.of(13, 0))));

                        createGroup(redes, "B",
                                        "EDWIN FERNEY CASTILLO QUINTERO,JOHANNA ANDREA HURTADO SANCHEZ,EMIGDIO ANDRES LARA SILVA",
                                        List.of(
                                                        new Schedule(DayOfWeek.MONDAY, LocalTime.of(16, 0),
                                                                        LocalTime.of(18, 0)),
                                                        new Schedule(DayOfWeek.TUESDAY, LocalTime.of(16, 0),
                                                                        LocalTime.of(18, 0)),
                                                        new Schedule(DayOfWeek.THURSDAY, LocalTime.of(16, 0),
                                                                        LocalTime.of(18, 0))));
                }
        }

        private void createGroup(Subject subject, String groupCode, String professors, List<Schedule> schedules) {
                SubjectGroup group = new SubjectGroup(null, subject, groupCode, professors, schedules);
                groupRepository.save(group);
        }
}
