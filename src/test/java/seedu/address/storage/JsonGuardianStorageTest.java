package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.guardian.Guardian;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;

public class JsonGuardianStorageTest {
    @TempDir
    public Path testFolder;

    @Test
    public void readAndSaveAddressBook_guardianRoundTrip_success() throws Exception {
        AddressBook original = new AddressBook();
        Guardian guardian = new Guardian(Name.fromStudentValue("Billie O'Connor"),
                Phone.fromStudentValue("+6598765432"), Email.fromStudentValue("billie@example.com"),
                Address.fromStudentValue("25 Happy Road"));
        original.addGuardian(guardian);

        Path filePath = testFolder.resolve("guardians.json");
        JsonAddressBookStorage storage = new JsonAddressBookStorage(filePath);
        storage.saveAddressBook(original);

        ReadOnlyAddressBook readBack = storage.readAddressBook().orElseThrow();
        assertEquals(original.getGuardianList(), readBack.getGuardianList());
        assertEquals(guardian, readBack.getGuardianList().get(0));
    }
}
