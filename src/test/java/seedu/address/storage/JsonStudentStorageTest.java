package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.model.student.Level;
import seedu.address.model.student.Student;
import seedu.address.model.student.Subject;

public class JsonStudentStorageTest {
    @TempDir
    public Path testFolder;

    @Test
    public void readAndSaveAddressBook_studentRoundTrip_success() throws Exception {
        AddressBook original = new AddressBook();
        original.addStudent(new Student(Name.fromStudentValue("Mary-Jane O'Connor"),
                Phone.fromStudentValue("+6591234567"), Email.fromStudentValue("mary@example.com"),
                Address.fromStudentValue("12 ABCD Ave 3, #04-15"), new Level("JC 1"),
                java.util.Set.of(new Subject("A-Level (H2)"), new Subject("Physics"))));

        Path filePath = testFolder.resolve("students.json");
        JsonAddressBookStorage storage = new JsonAddressBookStorage(filePath);
        storage.saveAddressBook(original);

        ReadOnlyAddressBook readBack = storage.readAddressBook().orElseThrow();
        assertEquals(original.getStudentList(), readBack.getStudentList());
    }
}
