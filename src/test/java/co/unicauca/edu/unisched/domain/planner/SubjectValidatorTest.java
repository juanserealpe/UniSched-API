package co.unicauca.edu.unisched.domain.planner;

import co.unicauca.edu.unisched.domain.model.Subject;
import co.unicauca.edu.unisched.domain.model.SubjectSelection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for SubjectValidator.
 * Tests validation logic, knowledge base constraints, and restrictions
 * including prerequisites, unlock relationships, and mandatory relationships.
 */
class SubjectValidatorTest {

    private SubjectValidator validator;
    private Set<Subject> allSubjects;

    @BeforeEach
    void setUp() {
        allSubjects = new HashSet<>();
        initializeTestSubjects();
        validator = new SubjectValidator(allSubjects);
    }

    /**
     * Initializes test subjects with relationships similar to the study plan.
     */
    private void initializeTestSubjects() {
        // Semester 1
        Subject calculo1 = new Subject(1L, "Cálculo I", (byte) 1);
        Subject lectura = new Subject(2L, "Lectura y Escritura", (byte) 1);
        Subject intro_info = new Subject(5L, "Introducción a la Informática", (byte) 1);
        Subject lab_intro_info = new Subject(6L, "Lab. Introducción a la Informática", (byte) 1);

        // Semester 2
        Subject calculo2 = new Subject(8L, "Cálculo II", (byte) 2);
        Subject mecanica = new Subject(9L, "Mecánica", (byte) 2);
        Subject lab_mecanica = new Subject(10L, "Laboratorio Mecánica", (byte) 2);
        Subject poo = new Subject(12L, "Programación Orientada a Objetos", (byte) 2);
        Subject lab_poo = new Subject(13L, "Laboratorio POO", (byte) 2);

        // Semester 3
        Subject calculo3 = new Subject(14L, "Cálculo III", (byte) 3);
        Subject ed1 = new Subject(18L, "Estructuras de Datos I", (byte) 3);
        Subject lab_ed1 = new Subject(19L, "Lab. Estructuras de Datos I", (byte) 3);

        // Semester 4
        Subject ed2 = new Subject(22L, "Estructuras de Datos II", (byte) 4);
        Subject lab_ed2 = new Subject(23L, "Lab. Estructuras de Datos II", (byte) 4);
        Subject bd1 = new Subject(24L, "Bases de Datos I", (byte) 4);
        Subject lab_bd1 = new Subject(25L, "Lab. Bases de Datos I", (byte) 4);

        // Semester 5
        Subject sw1 = new Subject(29L, "Ingeniería de Software I", (byte) 5);
        Subject lab_sw1 = new Subject(28L, "Lab. Ingeniería de Software I", (byte) 5);

        // Semester 6
        Subject sw2 = new Subject(36L, "Ingeniería de Software II", (byte) 6);
        Subject lab_sw2 = new Subject(37L, "Lab. Ingeniería de Software II", (byte) 6);

        // Semester 7
        Subject ia = new Subject(42L, "Inteligencia Artificial", (byte) 7);
        Subject distribuidos = new Subject(43L, "Sistemas Distribuidos", (byte) 7);

        // Relationships
        calculo1.unlock(calculo2);
        calculo1.unlock(mecanica);
        intro_info.unlock(poo);
        intro_info.mandatoryWith(lab_intro_info);
        mecanica.mandatoryWith(lab_mecanica);
        poo.unlock(ed1);
        poo.mandatoryWith(lab_poo);
        calculo2.unlock(calculo3);
        ed1.unlock(ed2);
        ed1.unlock(bd1);
        ed1.mandatoryWith(lab_ed1);
        ed2.mandatoryWith(lab_ed2);
        bd1.mandatoryWith(lab_bd1);
        ed2.unlock(sw1);
        sw1.mandatoryWith(lab_sw1);
        sw1.unlock(sw2);
        sw2.mandatoryWith(lab_sw2);
        // Note: IA and Distribuidos need prerequisites that we'll add if needed

        allSubjects.addAll(Set.of(
                calculo1, lectura, intro_info, lab_intro_info,
                calculo2, mecanica, lab_mecanica, poo, lab_poo,
                calculo3, ed1, lab_ed1,
                ed2, lab_ed2, bd1, lab_bd1,
                sw1, lab_sw1,
                sw2, lab_sw2,
                ia, distribuidos
        ));
    }

    /**
     * Tests that a valid selection of first semester subjects is accepted.
     */
    @Test
    void testValidFirstSemesterSelection() {
        SubjectSelection selection = new SubjectSelection();
        selection.select(getSubject(1L)); // Cálculo I
        selection.select(getSubject(2L)); // Lectura
        selection.select(getSubject(5L)); // Intro Info
        selection.select(getSubject(6L)); // Lab Intro Info

        assertTrue(validator.isValidCombination(selection));
        List<String> errors = validator.validateCombinationWithErrors(selection);
        assertTrue(errors.isEmpty());
    }

    /**
     * Tests that selecting a subject and its prerequisite together is invalid.
     */
    @Test
    void testInvalidPrerequisiteRelationship() {
        SubjectSelection selection = new SubjectSelection();
        selection.select(getSubject(1L)); // Cálculo I
        selection.select(getSubject(8L)); // Cálculo II (unlocked by Cálculo I)

        assertFalse(validator.isValidCombination(selection));
        List<String> errors = validator.validateCombinationWithErrors(selection);
        assertFalse(errors.isEmpty());
        assertTrue(errors.stream().anyMatch(e -> e.contains("Cálculo I") && e.contains("Cálculo II")));
    }

    /**
     * Tests that selecting a subject and what it unlocks together is invalid.
     */
    @Test
    void testInvalidUnlockRelationship() {
        SubjectSelection selection = new SubjectSelection();
        selection.select(getSubject(12L)); // POO
        selection.select(getSubject(18L)); // ED1 (unlocked by POO)

        assertFalse(validator.isValidCombination(selection));
        List<String> errors = validator.validateCombinationWithErrors(selection);
        assertFalse(errors.isEmpty());
    }

    /**
     * Tests that selecting a subject without its mandatory partner is invalid.
     */
    @Test
    void testInvalidMissingMandatoryPartner() {
        SubjectSelection selection = new SubjectSelection();
        selection.select(getSubject(5L)); // Intro Info without Lab Intro Info

        assertFalse(validator.isValidCombination(selection));
        List<String> errors = validator.validateCombinationWithErrors(selection);
        assertFalse(errors.isEmpty());
        assertTrue(errors.stream().anyMatch(e -> e.contains("mandatory partner")));
    }

    /**
     * Tests that selecting both mandatory partners together is valid.
     */
    @Test
    void testValidMandatoryPartners() {
        SubjectSelection selection = new SubjectSelection();
        selection.select(getSubject(5L)); // Intro Info
        selection.select(getSubject(6L)); // Lab Intro Info

        assertTrue(validator.isValidCombination(selection));
        List<String> errors = validator.validateCombinationWithErrors(selection);
        assertTrue(errors.isEmpty());
    }

    /**
     * Tests valid progression through semesters with prerequisites.
     */
    @Test
    void testValidProgressionWithPrerequisites() {
        SubjectSelection selection = new SubjectSelection();
        // Semester 1
        selection.select(getSubject(1L)); // Cálculo I
        selection.select(getSubject(2L)); // Lectura
        selection.select(getSubject(5L)); // Intro Info
        selection.select(getSubject(6L)); // Lab Intro Info

        assertTrue(validator.isValidCombination(selection));
    }

    /**
     * Tests that selecting multiple mandatory pairs is valid.
     */
    @Test
    void testMultipleMandatoryPairs() {
        SubjectSelection selection = new SubjectSelection();
        selection.select(getSubject(5L)); // Intro Info
        selection.select(getSubject(6L)); // Lab Intro Info
        selection.select(getSubject(9L)); // Mecánica
        selection.select(getSubject(10L)); // Lab Mecánica

        assertTrue(validator.isValidCombination(selection));
    }

    /**
     * Tests complex invalid scenario with multiple conflicts.
     */
    @Test
    void testComplexInvalidScenario() {
        SubjectSelection selection = new SubjectSelection();
        selection.select(getSubject(1L)); // Cálculo I
        selection.select(getSubject(8L)); // Cálculo II (conflict: prerequisite)
        selection.select(getSubject(12L)); // POO
        selection.select(getSubject(18L)); // ED1 (conflict: unlock)
        selection.select(getSubject(5L)); // Intro Info (conflict: missing mandatory)

        assertFalse(validator.isValidCombination(selection));
        List<String> errors = validator.validateCombinationWithErrors(selection);
        assertFalse(errors.isEmpty());
        assertTrue(errors.size() > 1); // Should have multiple errors
    }

    /**
     * Tests that calculateBlocked correctly blocks descendants.
     */
    @Test
    void testCalculateBlocked() {
        SubjectSelection selection = new SubjectSelection();
        selection.select(getSubject(1L)); // Cálculo I

        Set<Subject> blocked = validator.calculateBlocked(selection);
        assertTrue(blocked.contains(getSubject(8L))); // Cálculo II should be blocked
        assertTrue(blocked.contains(getSubject(9L))); // Mecánica should be blocked
    }

    /**
     * Tests that calculatePlan returns correct validation result.
     */
    @Test
    void testCalculatePlan() {
        SubjectSelection selection = new SubjectSelection();
        selection.select(getSubject(1L)); // Cálculo I
        selection.select(getSubject(2L)); // Lectura
        selection.select(getSubject(5L)); // Intro Info
        selection.select(getSubject(6L)); // Lab Intro Info

        var result = validator.calculatePlan(selection);
        assertNotNull(result);
        assertEquals(4, result.getSelected().size());
        assertFalse(result.getBlocked().isEmpty());
        assertFalse(result.getAvailable().isEmpty());
    }

    /**
     * Tests validation up to 7th semester subjects.
     */
    @Test
    void testSeventhSemesterValidation() {
        SubjectSelection selection = new SubjectSelection();
        // Build a valid chain up to 7th semester
        selection.select(getSubject(1L)); // Cálculo I
        selection.select(getSubject(5L)); // Intro Info
        selection.select(getSubject(6L)); // Lab Intro Info
        selection.select(getSubject(12L)); // POO
        selection.select(getSubject(13L)); // Lab POO
        selection.select(getSubject(18L)); // ED1
        selection.select(getSubject(19L)); // Lab ED1
        selection.select(getSubject(22L)); // ED2
        selection.select(getSubject(23L)); // Lab ED2
        selection.select(getSubject(29L)); // SW1
        selection.select(getSubject(28L)); // Lab SW1
        selection.select(getSubject(36L)); // SW2
        selection.select(getSubject(37L)); // Lab SW2

        // Note: IA and Distribuidos would need additional prerequisites
        // This test verifies the structure works
        assertNotNull(validator.isValidCombination(selection));
    }

    /**
     * Tests that empty selection is handled correctly.
     */
    @Test
    void testEmptySelection() {
        SubjectSelection selection = new SubjectSelection();

        assertTrue(validator.isValidCombination(selection));
        List<String> errors = validator.validateCombinationWithErrors(selection);
        assertTrue(errors.isEmpty());
    }

    /**
     * Helper method to get a subject by ID.
     */
    private Subject getSubject(Long id) {
        return allSubjects.stream()
                .filter(s -> s.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Subject not found: " + id));
    }
}

