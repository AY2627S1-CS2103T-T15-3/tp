package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.testutil.Assert.assertThrows;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.address.logic.commands.AddGuardianCommand;
import seedu.address.logic.commands.AddStudentCommand;
import seedu.address.logic.commands.EditGuardianCommand;
import seedu.address.logic.commands.EditStudentCommand;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.AddressBookParser;
import seedu.address.model.ModelManager;

/** Exercises contact formatting through parsing, command execution and persistence. */
public class ContactFormattingIntegrationTest {
    @TempDir
    public Path temporaryFolder;

    @Test
    public void addAndEdit_formattedContacts_persistCanonicalPhonesAndNames() throws Exception {
        ModelManager model = new ModelManager();
        AddressBookParser parser = new AddressBookParser();
        parser.parseCommand("add-s n/Mrs. Lim p/+65 9123 4567 e/lim@example.com a/12 Road l/JC 1 s/Math")
                .execute(model);
        parser.parseCommand("add-g n/Mr. Lim p/+65 9876 5432 e/lim@example.com a/12 Road").execute(model);
        assertEquals("Mrs. Lim", model.getFilteredStudentList().get(0).getName().fullName);
        assertEquals("Mr. Lim", model.getFilteredGuardianList().get(0).getName().fullName);
        assertEquals("+6591234567", model.getFilteredStudentList().get(0).getPhone().value);
        assertEquals("+6598765432", model.getFilteredGuardianList().get(0).getPhone().value);
        assertRoundTrip(model, "added.json");

        parser.parseCommand("edit-s 1 n/Dr. Lim p/+65 8111 2222").execute(model);
        parser.parseCommand("edit-g 1 n/Prof. Lim p/+65 8333 4444").execute(model);
        assertEquals("Dr. Lim", model.getFilteredStudentList().get(0).getName().fullName);
        assertEquals("Prof. Lim", model.getFilteredGuardianList().get(0).getName().fullName);
        assertEquals("+6581112222", model.getFilteredStudentList().get(0).getPhone().value);
        assertEquals("+6583334444", model.getFilteredGuardianList().get(0).getPhone().value);
        assertRoundTrip(model, "edited.json");
    }

    @Test
    public void addAndEdit_spacedDuplicatePhones_rejectedWithoutChangingRecords() throws Exception {
        ModelManager model = new ModelManager();
        AddressBookParser parser = new AddressBookParser();
        String student = "add-s n/Mrs. Lim p/+6591234567 e/lim@example.com a/12 Road l/JC 1 s/Math";
        String guardian = "add-g n/Mr. Lim p/+6598765432 e/lim@example.com a/12 Road";
        parser.parseCommand(student).execute(model);
        parser.parseCommand(guardian).execute(model);
        assertThrows(CommandException.class, AddStudentCommand.MESSAGE_DUPLICATE_STUDENT, () ->
                parser.parseCommand(student.replace("+6591234567", "+65 9123 4567")).execute(model));
        assertThrows(CommandException.class, AddGuardianCommand.MESSAGE_DUPLICATE_GUARDIAN, () ->
                parser.parseCommand(guardian.replace("+6598765432", "+65 9876 5432")).execute(model));
        parser.parseCommand(student.replace("Mrs. Lim", "Other Student")).execute(model);
        parser.parseCommand(guardian.replace("Mr. Lim", "Other Guardian")).execute(model);
        var studentsBefore = List.copyOf(model.getFilteredStudentList());
        var guardiansBefore = List.copyOf(model.getFilteredGuardianList());
        assertThrows(CommandException.class, EditStudentCommand.MESSAGE_DUPLICATE_STUDENT, () ->
                parser.parseCommand("edit-s 2 n/Mrs. Lim p/+65 9123 4567").execute(model));
        assertThrows(CommandException.class, EditGuardianCommand.MESSAGE_DUPLICATE_GUARDIAN, () ->
                parser.parseCommand("edit-g 2 n/Mr. Lim p/+65 9876 5432").execute(model));
        assertEquals(studentsBefore, model.getFilteredStudentList());
        assertEquals(guardiansBefore, model.getFilteredGuardianList());
        // Reformatting the same record's phone must remain a valid edit.
        parser.parseCommand("edit-s 1 p/+65 9123 4567").execute(model);
        parser.parseCommand("edit-g 1 p/+65 9876 5432").execute(model);
        assertEquals(studentsBefore, model.getFilteredStudentList());
        assertEquals(guardiansBefore, model.getFilteredGuardianList());
    }

    @Test
    public void load_existingCompactPhones_preservesExactValues() throws Exception {
        for (String phone : List.of("00123456", "91234567", "+6591234567")) {
            var student = new JsonAdaptedStudent("Henry", phone, "henry@example.com", "12 Road",
                    "JC 1", List.of("Math")).toModelType();
            var guardian = new JsonAdaptedGuardian("Ben", phone, "ben@example.com", "12 Road")
                    .toModelType();
            assertEquals(phone, student.getPhone().value);
            assertEquals(phone, guardian.getPhone().value);
            ModelManager model = new ModelManager();
            model.addStudent(student);
            model.addGuardian(guardian);
            assertRoundTrip(model, "existing.json");
        }
    }

    private void assertRoundTrip(ModelManager model, String filename) throws Exception {
        Path file = temporaryFolder.resolve(filename);
        JsonAddressBookStorage storage = new JsonAddressBookStorage(file);
        storage.saveAddressBook(model.getAddressBook());
        String savedJson = Files.readString(file);
        assertEquals(model.getAddressBook(), storage.readAddressBook().orElseThrow());
        assertEquals(savedJson, Files.readString(file));
    }
}
