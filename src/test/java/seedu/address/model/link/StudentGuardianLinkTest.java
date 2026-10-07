package seedu.address.model.link;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.model.guardian.Guardian;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.model.student.Level;
import seedu.address.model.student.Student;
import seedu.address.model.student.Subject;

public class StudentGuardianLinkTest {
    private static final Student STUDENT = new Student(
            new Name("Lucy Lim"),
            new Phone("91234567"),
            new Email("lucy@example.com"),
            new Address("21 Happy Road"),
            new Level("JC 1"),
            Set.of(new Subject("Mathematics")));
    private static final Student OTHER_STUDENT = new Student(
            new Name("Amy Tan"),
            new Phone("98765432"),
            new Email("amy@example.com"),
            new Address("31 Clementi Avenue"),
            new Level("JC 1"),
            Set.of(new Subject("Physics")));
    private static final Guardian GUARDIAN = new Guardian(
            new Name("Ben Tan"),
            new Phone("92345678"),
            new Email("ben@example.com"),
            new Address("25 Happy Road"));
    private static final Guardian OTHER_GUARDIAN = new Guardian(
            new Name("May Lim"),
            new Phone("93456789"),
            new Email("may@example.com"),
            new Address("41 Sunset Way"));

    @Test
    public void constructorAndGetters_storeStudentAndGuardian() {
        StudentGuardianLink link = new StudentGuardianLink(STUDENT, GUARDIAN);

        assertEquals(STUDENT, link.getStudent());
        assertEquals(GUARDIAN, link.getGuardian());
    }

    @Test
    public void constructor_nullStudentOrGuardian_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new StudentGuardianLink(null, GUARDIAN));
        assertThrows(NullPointerException.class, () -> new StudentGuardianLink(STUDENT, null));
    }

    @Test
    public void isSameLink_samePair_returnsTrue() {
        StudentGuardianLink link = new StudentGuardianLink(STUDENT, GUARDIAN);
        StudentGuardianLink sameLink = new StudentGuardianLink(STUDENT, GUARDIAN);

        assertTrue(link.isSameLink(sameLink));
        assertTrue(link.isSameLink(link));
    }

    @Test
    public void isSameLink_differentPairOrNull_returnsFalse() {
        StudentGuardianLink link = new StudentGuardianLink(STUDENT, GUARDIAN);

        assertFalse(link.isSameLink(null));
        assertFalse(link.isSameLink(new StudentGuardianLink(OTHER_STUDENT, GUARDIAN)));
        assertFalse(link.isSameLink(new StudentGuardianLink(STUDENT, OTHER_GUARDIAN)));
    }

    @Test
    public void equalsAndHashCode_samePair_areEqual() {
        StudentGuardianLink link = new StudentGuardianLink(STUDENT, GUARDIAN);
        StudentGuardianLink sameLink = new StudentGuardianLink(STUDENT, GUARDIAN);

        assertEquals(link, sameLink);
        assertEquals(link.hashCode(), sameLink.hashCode());
    }

    @Test
    public void equals_differentPairOrType_areNotEqual() {
        StudentGuardianLink link = new StudentGuardianLink(STUDENT, GUARDIAN);

        assertNotEquals(link, new StudentGuardianLink(OTHER_STUDENT, GUARDIAN));
        assertNotEquals(link, new StudentGuardianLink(STUDENT, OTHER_GUARDIAN));
        assertNotEquals(link, null);
        assertNotEquals(link, "not a link");
    }

    @Test
    public void toString_containsLinkedContacts() {
        StudentGuardianLink link = new StudentGuardianLink(STUDENT, GUARDIAN);

        String result = link.toString();

        assertTrue(result.contains("student="));
        assertTrue(result.contains("guardian="));
    }
}
