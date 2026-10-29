package seedu.address.model.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.Objects;

import org.junit.jupiter.api.Test;

import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.guardian.Guardian;
import seedu.address.model.student.Student;

public class SampleDataUtilTest {

    @Test
    public void getSampleAddressBook_returnsExpectedData() {
        ReadOnlyAddressBook sampleAddressBook = SampleDataUtil.getSampleAddressBook();
        Student alice = findStudent(sampleAddressBook, "Alice Tan");

        assertEquals(4, sampleAddressBook.getStudentList().size());
        assertEquals(3, sampleAddressBook.getGuardianList().size());
        assertFalse(alice.getRemark().isEmpty());
        assertEquals(4, alice.getPaidMonths().size());
        assertEquals(7, sampleAddressBook.getLessonList().size());
        assertEquals(4, sampleAddressBook.getStudentGuardianLinkList().size());
        assertEquals(2, sampleAddressBook.getStudentGuardianLinkList().stream()
                .filter(link -> link.getStudent().equals(alice)).count());
    }

    @Test
    public void getSampleAddressBook_containsUsableLinksAndLessons() {
        ReadOnlyAddressBook sampleAddressBook = SampleDataUtil.getSampleAddressBook();

        Student alice = findStudent(sampleAddressBook, "Alice Tan");
        Guardian billie = findGuardian(sampleAddressBook, "Billie O'Connor");
        assertEquals("Alice Tan", alice.getName().fullName);
        assertEquals("Billie O'Connor", billie.getName().fullName);
        assertTrue(sampleAddressBook.getStudentGuardianLinkList().stream()
                .allMatch(link -> sampleAddressBook.getStudentList().contains(link.getStudent())
                        && sampleAddressBook.getGuardianList().contains(link.getGuardian())));
        assertTrue(sampleAddressBook.getLessonList().stream()
                .allMatch(lesson -> sampleAddressBook.getStudentList().contains(lesson.getStudent())));

        assertTrue(sampleAddressBook.getLessonList().stream()
                .anyMatch(lesson -> lesson.getDate().equals(LocalDate.of(2026, 11, 10))
                        && lesson.getEndDateTime().toLocalDate().equals(LocalDate.of(2026, 11, 11))));
    }

    private static Student findStudent(ReadOnlyAddressBook addressBook, String name) {
        return addressBook.getStudentList().stream()
                .filter(student -> Objects.equals(student.getName().fullName, name))
                .findFirst()
                .orElseThrow();
    }

    private static Guardian findGuardian(ReadOnlyAddressBook addressBook, String name) {
        return addressBook.getGuardianList().stream()
                .filter(guardian -> Objects.equals(guardian.getName().fullName, name))
                .findFirst()
                .orElseThrow();
    }
}
