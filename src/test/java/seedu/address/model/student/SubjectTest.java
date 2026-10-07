package seedu.address.model.student;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class SubjectTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Subject(null));
    }

    @Test
    public void constructor_invalidSubject_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new Subject(""));
        assertThrows(IllegalArgumentException.class, () -> new Subject("   "));
        assertThrows(IllegalArgumentException.class, () -> new Subject("Physics&Chemistry"));
        assertThrows(IllegalArgumentException.class, () -> new Subject("a".repeat(51)));
    }

    @Test
    public void constructor_normalizesWhitespace() {
        assertEquals("A-Level (H2)", new Subject("  A-Level   (H2) ").value);
    }

    @Test
    public void isValidSubject() {
        assertThrows(NullPointerException.class, () -> Subject.isValidSubject(null));
        assertTrue(Subject.isValidSubject("Mathematics"));
        assertTrue(Subject.isValidSubject("A-Level (H2)"));
        assertFalse(Subject.isValidSubject(""));
        assertFalse(Subject.isValidSubject("Physics&Chemistry"));
    }

    @Test
    public void normalize_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> Subject.normalize(null));
    }

    @Test
    public void equals() {
        Subject subject = new Subject("Mathematics");
        Subject equivalentSubject = new Subject(" mathematics ");
        Subject differentSubject = new Subject("Physics");

        assertTrue(subject.equals(subject));
        assertTrue(subject.equals(equivalentSubject));
        assertFalse(subject.equals(null));
        assertFalse(subject.equals(5));
        assertFalse(subject.equals(differentSubject));
        assertEquals(subject.hashCode(), equivalentSubject.hashCode());
    }

    @Test
    public void toStringMethod() {
        assertEquals("Mathematics", new Subject("Mathematics").toString());
    }
}
