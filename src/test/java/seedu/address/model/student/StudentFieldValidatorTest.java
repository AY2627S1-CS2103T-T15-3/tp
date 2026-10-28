package seedu.address.model.student;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class StudentFieldValidatorTest {

    @Test
    public void normalizeName_collapsesWhitespace() {
        assertEquals("Henry Tan", StudentFieldValidator.normalizeName("  Henry   Tan "));
        assertEquals("henry tan", StudentFieldValidator.canonicalName("  Henry   Tan "));
    }

    @Test
    public void isValidName_validAndInvalidValues() {
        assertTrue(StudentFieldValidator.isValidName("Mary-Jane O'Connor"));
        assertFalse(StudentFieldValidator.isValidName("Henry&Tan"));
        assertFalse(StudentFieldValidator.isValidName(""));
    }

    @Test
    public void isValidEmail_rejectsMalformedLocalAndDomainParts() {
        assertTrue(StudentFieldValidator.isValidEmail("mary.jane+school@example.com"));
        assertFalse(StudentFieldValidator.isValidEmail("@example.com"));
        assertFalse(StudentFieldValidator.isValidEmail("mary@@example.com"));
        assertFalse(StudentFieldValidator.isValidEmail(".alice@example.com"));
        assertFalse(StudentFieldValidator.isValidEmail("alice.@example.com"));
        assertFalse(StudentFieldValidator.isValidEmail("alice..test@example.com"));
        assertFalse(StudentFieldValidator.isValidEmail("mary?@example.com"));
        assertFalse(StudentFieldValidator.isValidEmail("mary@example..com"));
        assertFalse(StudentFieldValidator.isValidEmail("mary@-example.com"));
    }

    @Test
    public void isValidAddressPhoneLevelAndSubject_delegatesToRules() {
        assertTrue(StudentFieldValidator.isValidPhone("+6591234567"));
        assertFalse(StudentFieldValidator.isValidPhone("12 345"));
        assertTrue(StudentFieldValidator.isValidAddress("12 ABCD Ave 3, #04-15"));
        assertFalse(StudentFieldValidator.isValidAddress("12 ABCD Ave @"));
        assertTrue(StudentFieldValidator.isValidLevel("JC 1"));
        assertTrue(StudentFieldValidator.isValidSubject("A-Level (H2)"));
        assertEquals("+6591234567", StudentFieldValidator.normalizePhone(" +6591234567 "));
        assertEquals("mary@example.com", StudentFieldValidator.normalizeEmail(" mary@example.com "));
        assertEquals("12 Main Street", StudentFieldValidator.normalizeAddress(" 12 Main Street "));
    }
}
