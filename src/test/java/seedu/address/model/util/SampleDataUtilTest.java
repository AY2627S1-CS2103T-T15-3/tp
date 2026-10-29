package seedu.address.model.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import seedu.address.model.ReadOnlyAddressBook;

public class SampleDataUtilTest {

    @Test
    public void getSampleAddressBook_returnsPeople() {
        ReadOnlyAddressBook sampleAddressBook = SampleDataUtil.getSampleAddressBook();

        assertEquals(4, sampleAddressBook.getStudentList().size());
        assertEquals(3, sampleAddressBook.getGuardianList().size());
        assertFalse(sampleAddressBook.getStudentList().get(0).getRemark().isEmpty());
        assertFalse(sampleAddressBook.getStudentList().get(0).getPaidMonths().isEmpty());
        assertEquals(7, sampleAddressBook.getLessonList().size());
        assertEquals(4, sampleAddressBook.getStudentGuardianLinkList().size());
        assertEquals(2, sampleAddressBook.getStudentGuardianLinkList().stream()
                .filter(link -> link.getStudent().equals(sampleAddressBook.getStudentList().get(0))).count());
    }

    @Test
    public void getSampleAddressBook_containsUsableLinksAndLessons() {
        ReadOnlyAddressBook sampleAddressBook = SampleDataUtil.getSampleAddressBook();

        assertEquals("Alice Tan", sampleAddressBook.getStudentList().get(0).getName().fullName);
        assertEquals("Billie O'Connor", sampleAddressBook.getGuardianList().get(0).getName().fullName);
        assertTrue(sampleAddressBook.getStudentGuardianLinkList().stream()
                .allMatch(link -> sampleAddressBook.getStudentList().contains(link.getStudent())
                        && sampleAddressBook.getGuardianList().contains(link.getGuardian())));
        assertTrue(sampleAddressBook.getLessonList().stream()
                .allMatch(lesson -> sampleAddressBook.getStudentList().contains(lesson.getStudent())));

        assertTrue(sampleAddressBook.getLessonList().stream()
                .anyMatch(lesson -> lesson.getDate().equals(LocalDate.of(2026, 11, 10))
                        && lesson.getEndDateTime().toLocalDate().equals(LocalDate.of(2026, 11, 11))));
    }
}
