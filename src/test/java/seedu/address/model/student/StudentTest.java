package seedu.address.model.student;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import java.util.LinkedHashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;

public class StudentTest {

    private static final Name NAME = new Name("Henry Tan");
    private static final Phone PHONE = new Phone("91234567");
    private static final Email EMAIL = new Email("henry@example.com");
    private static final Address ADDRESS = new Address("12 ABCD Ave 3");
    private static final Level LEVEL = new Level("Secondary 3");

    @Test
    public void constructor_nullField_throwsNullPointerException() {
        Set<Subject> subjects = subjects("Mathematics");

        assertThrows(NullPointerException.class, () -> studentWithNullName(subjects));
        assertThrows(NullPointerException.class, () -> studentWithNullPhone(subjects));
        assertThrows(NullPointerException.class, () -> studentWithNullLevel(subjects));
        assertThrows(NullPointerException.class, () -> studentWithNullSubjects());
        assertThrows(NullPointerException.class, () -> studentWithNullSubject());
    }

    @Test
    public void constructor_noSubjects_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> studentWithNoSubjects());
    }

    @Test
    public void getters_returnConstructorValuesAndDeduplicateSubjects() {
        Set<Subject> subjects = new LinkedHashSet<>();
        subjects.add(new Subject("Mathematics"));
        subjects.add(new Subject(" mathematics "));
        subjects.add(new Subject("Physics"));

        Student student = new Student(NAME, PHONE, EMAIL, ADDRESS, LEVEL, subjects);

        assertEquals(NAME, student.getName());
        assertEquals(PHONE, student.getPhone());
        assertEquals(EMAIL, student.getEmail());
        assertEquals(ADDRESS, student.getAddress());
        assertEquals(LEVEL, student.getLevel());
        assertEquals(Set.of(new Subject("Mathematics"), new Subject("Physics")), student.getSubjects());
        assertThrows(UnsupportedOperationException.class, () -> addSubject(student));
    }

    @Test
    public void normalizeName_normalizesWhitespaceAndCase() {
        assertEquals("henry tan", Student.normalizeName("  Henry   Tan "));
    }

    @Test
    public void isSameStudent_sameNormalizedNameAndPhone_returnsTrue() {
        Student student = studentWith(NAME, PHONE, EMAIL, ADDRESS, LEVEL, subjects("Mathematics"));
        Student equivalentStudent = studentWith(new Name("henry  tan"), PHONE,
                new Email("other@example.com"), new Address("25 Happy Road"), new Level("JC 1"),
                subjects("Physics"));

        assertTrue(student.isSameStudent(student));
        assertTrue(student.isSameStudent(equivalentStudent));
        assertFalse(student.isSameStudent(null));
    }

    @Test
    public void isSameStudent_differentNameOrPhone_returnsFalse() {
        Student student = studentWith(NAME, PHONE, EMAIL, ADDRESS, LEVEL, subjects("Mathematics"));
        Student differentName = studentWith(new Name("Lucy Lim"), PHONE, EMAIL, ADDRESS, LEVEL,
                subjects("Mathematics"));
        Student differentPhone = studentWith(NAME, new Phone("98765432"), EMAIL, ADDRESS, LEVEL,
                subjects("Mathematics"));

        assertFalse(student.isSameStudent(differentName));
        assertFalse(student.isSameStudent(differentPhone));
    }

    @Test
    public void normalizeName_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> Student.normalizeName(null));
    }

    @Test
    public void equals() {
        Student student = studentWith(NAME, PHONE, EMAIL, ADDRESS, LEVEL, subjects("Mathematics"));
        Student copy = studentWith(NAME, PHONE, EMAIL, ADDRESS, LEVEL, subjects("Mathematics"));
        Student differentLevel = studentWith(NAME, PHONE, EMAIL, ADDRESS, new Level("JC 1"),
                subjects("Mathematics"));
        Student differentSubjects = studentWith(NAME, PHONE, EMAIL, ADDRESS, LEVEL, subjects("Physics"));

        assertTrue(student.equals(student));
        assertTrue(student.equals(copy));
        assertFalse(student.equals(null));
        assertFalse(student.equals(5));
        assertFalse(student.equals(differentLevel));
        assertFalse(student.equals(differentSubjects));
        assertEquals(student.hashCode(), copy.hashCode());
    }

    @Test
    public void toStringMethod() {
        Student student = studentWith(NAME, PHONE, EMAIL, ADDRESS, LEVEL, subjects("Mathematics"));

        String expected = Student.class.getCanonicalName() + "{name=" + NAME + ", phone=" + PHONE
                + ", email=" + EMAIL + ", address=" + ADDRESS + ", level=" + LEVEL
                + ", subjects=" + student.getSubjects() + "}";
        assertEquals(expected, student.toString());
    }

    private static Student studentWith(Name name, Phone phone, Email email, Address address, Level level,
            Set<Subject> subjects) {
        return new Student(name, phone, email, address, level, subjects);
    }

    private static Student studentWithNullName(Set<Subject> subjects) {
        return studentWith(null, PHONE, EMAIL, ADDRESS, LEVEL, subjects);
    }

    private static Student studentWithNullPhone(Set<Subject> subjects) {
        return studentWith(NAME, null, EMAIL, ADDRESS, LEVEL, subjects);
    }

    private static Student studentWithNullLevel(Set<Subject> subjects) {
        return studentWith(NAME, PHONE, EMAIL, ADDRESS, null, subjects);
    }

    private static Student studentWithNullSubjects() {
        return studentWith(NAME, PHONE, EMAIL, ADDRESS, LEVEL, null);
    }

    private static Student studentWithNullSubject() {
        return studentWith(NAME, PHONE, EMAIL, ADDRESS, LEVEL, subjectsWithNull());
    }

    private static Student studentWithNoSubjects() {
        return studentWith(NAME, PHONE, EMAIL, ADDRESS, LEVEL, new LinkedHashSet<>());
    }

    private static void addSubject(Student student) {
        student.getSubjects().add(new Subject("Chemistry"));
    }

    private static Set<Subject> subjects(String... subjects) {
        Set<Subject> result = new LinkedHashSet<>();
        for (String subject : subjects) {
            result.add(new Subject(subject));
        }
        return result;
    }

    private static Set<Subject> subjectsWithNull() {
        Set<Subject> result = new LinkedHashSet<>();
        result.add(null);
        return result;
    }
}
