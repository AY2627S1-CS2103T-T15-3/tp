package seedu.address.model.util;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import seedu.address.model.ReadOnlyAddressBook;

public class SampleDataUtilTest {

    @Test
    public void getSampleAddressBook_returnsEmptyAddressBook() {
        ReadOnlyAddressBook sampleAddressBook = SampleDataUtil.getSampleAddressBook();

        assertTrue(sampleAddressBook.getStudentList().isEmpty());
        assertTrue(sampleAddressBook.getGuardianList().isEmpty());
        assertTrue(sampleAddressBook.getLessonList().isEmpty());
        assertTrue(sampleAddressBook.getStudentGuardianLinkList().isEmpty());
    }
}
