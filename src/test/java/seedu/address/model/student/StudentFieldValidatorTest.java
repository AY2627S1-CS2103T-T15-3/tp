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
        assertTrue(StudentFieldValidator.isValidName("John s/o Raj"));
        assertTrue(StudentFieldValidator.isValidName("Mrs. Lim"));
        assertTrue(StudentFieldValidator.isValidName("-A."));
        assertTrue(StudentFieldValidator.isValidName("1"));
        for (String name : new String[] {"...", "---", "'", "/", " . - / ' "}) {
            assertFalse(StudentFieldValidator.isValidName(name));
        }
        assertFalse(StudentFieldValidator.isValidName("Henry&Tan"));
        assertFalse(StudentFieldValidator.isValidName(""));
        assertFalse(StudentFieldValidator.isValidName("   "));
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
        assertFalse(StudentFieldValidator.isValidPhone("12-345"));
        assertTrue(StudentFieldValidator.isValidAddress("12 ABCD Ave 3, #04-15"));
        assertFalse(StudentFieldValidator.isValidAddress("12 ABCD Ave @"));
        assertTrue(StudentFieldValidator.isValidLevel("JC 1"));
        assertTrue(StudentFieldValidator.isValidLevel("Secondary 3 (Express)"));
        assertTrue(StudentFieldValidator.isValidSubject("A-Level (H2)"));
        assertEquals("+6591234567", StudentFieldValidator.normalizePhone(" +6591234567 "));
        assertEquals("mary@example.com", StudentFieldValidator.normalizeEmail(" mary@example.com "));
        assertEquals("12 Main Street", StudentFieldValidator.normalizeAddress(" 12 Main Street "));
    }
    @Test
    public void phoneWithSpaces_normalizesAndValidatesDigitCount() {
        assertEquals("+6591234567", StudentFieldValidator.normalizePhone(" +65 9123 4567 "));
        assertTrue(StudentFieldValidator.isValidPhone("+65 9123 4567"));
        assertTrue(StudentFieldValidator.isValidPhone("9  1 2"));
        assertTrue(StudentFieldValidator.isValidPhone("12345 67890 12345"));
        assertFalse(StudentFieldValidator.isValidPhone("1 2"));
        assertFalse(StudentFieldValidator.isValidPhone("12345 67890 123456"));
        assertFalse(StudentFieldValidator.isValidPhone("   "));
        assertFalse(StudentFieldValidator.isValidPhone("+ +65 9123 4567"));
        assertFalse(StudentFieldValidator.isValidPhone("9123\t4567"));
    }

}
