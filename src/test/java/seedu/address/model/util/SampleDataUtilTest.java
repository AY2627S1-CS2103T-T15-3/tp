package seedu.address.model.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

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
}
