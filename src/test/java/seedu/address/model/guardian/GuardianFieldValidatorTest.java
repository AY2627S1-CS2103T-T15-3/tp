package seedu.address.model.guardian;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class GuardianFieldValidatorTest {

    @Test
    public void normalizeName_delegatesStudentRules() {
        assertEquals("Ben Tan", GuardianFieldValidator.normalizeName("  Ben   Tan "));
        assertTrue(GuardianFieldValidator.isValidName("Billie O'Connor"));
        assertTrue(GuardianFieldValidator.isValidName("John s/o Raj"));
        assertFalse(GuardianFieldValidator.isValidName("Ben&Tan"));
    }

    @Test
    public void normalizeAndValidateContactFields_delegatesSharedRules() {
        assertEquals("+6598765432", GuardianFieldValidator.normalizePhone(" +6598765432 "));
        assertEquals("ben@example.com", GuardianFieldValidator.normalizeEmail(" ben@example.com "));
        assertEquals("25 Happy Road", GuardianFieldValidator.normalizeAddress(" 25 Happy Road "));
        assertTrue(GuardianFieldValidator.isValidPhone("92345678"));
        assertTrue(GuardianFieldValidator.isValidEmail("ben@example.com"));
        assertTrue(GuardianFieldValidator.isValidAddress("25 Happy Road"));
        assertFalse(GuardianFieldValidator.isValidPhone("12 345"));
        assertFalse(GuardianFieldValidator.isValidEmail("ben@example"));
        assertFalse(GuardianFieldValidator.isValidAddress("25 Happy Road @"));
    }
}
