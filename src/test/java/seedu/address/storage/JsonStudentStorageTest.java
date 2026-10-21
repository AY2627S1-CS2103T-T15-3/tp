package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import seedu.address.commons.exceptions.DataLoadingException;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.JsonUtil;
import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.guardian.Guardian;
import seedu.address.model.link.StudentGuardianLink;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.model.student.Level;
import seedu.address.model.student.Remark;
import seedu.address.model.student.Student;
import seedu.address.model.student.Subject;

public class JsonStudentStorageTest {
    @TempDir
    public Path testFolder;

    @Test
    public void readAndSaveAddressBook_studentRoundTrip_success() throws Exception {
        AddressBook original = new AddressBook();
        original.addStudent(student());

        Path filePath = testFolder.resolve("students.json");
        JsonAddressBookStorage storage = new JsonAddressBookStorage(filePath);
        storage.saveAddressBook(original);

        ReadOnlyAddressBook readBack = storage.readAddressBook().orElseThrow();
        assertEquals(original.getStudentList(), readBack.getStudentList());
        assertSame(Remark.EMPTY, readBack.getStudentList().get(0).getRemark());
        assertEquals("", savedRemark(filePath));
    }

    @Test
    public void readAndSaveAddressBook_nonemptyRemarks_preservesStudentAndJsonText() throws Exception {
        Path filePath = testFolder.resolve("remarks.json");
        JsonAddressBookStorage storage = new JsonAddressBookStorage(filePath);
        for (String text : List.of("Practice  fractions", "Review \"algebra\" \\ 学\ud83d\ude00",
                "a".repeat(299), "a".repeat(300), "\ud83d\ude00".repeat(299), "\ud83d\ude00".repeat(300))) {
            Student expected = student().withRemark(new Remark(text));
            AddressBook original = new AddressBook();
            original.addStudent(expected);
            storage.saveAddressBook(original);

            assertEquals(text, savedRemark(filePath));
            assertEquals(expected, storage.readAddressBook().orElseThrow().getStudentList().get(0));
        }
    }

    @Test
    public void readAddressBook_missingEmptyOrNullRemark_usesCanonicalEmpty() throws Exception {
        for (String property : List.of("", ", \"remark\": \"\"", ", \"remark\": null")) {
            Path filePath = writeStudentJson(property);
            Student loaded = new JsonAddressBookStorage(filePath).readAddressBook().orElseThrow()
                    .getStudentList().get(0);
            assertEquals(student(), loaded);
            assertSame(Remark.EMPTY, loaded.getRemark());
        }
    }

    @Test
    public void readAddressBook_invalidRemarks_throwsDataLoadingExceptionWithoutChangingFile() throws Exception {
        for (String text : List.of(" ", "\u00a0", "a".repeat(301), "\ud83d\ude00".repeat(301), "text\n",
                "\ttext", "text\u0000", "text\u2028", "text\u2029")) {
            Path filePath = writeStudentJson(", \"remark\": " + JsonUtil.toJsonString(text));
            String originalJson = Files.readString(filePath);
            DataLoadingException exception = assertThrows(
                    DataLoadingException.class, () -> new JsonAddressBookStorage(filePath).readAddressBook());
            assertInstanceOf(IllegalValueException.class, exception.getCause());
            assertEquals(Remark.MESSAGE_CONSTRAINTS, exception.getCause().getMessage());
            assertEquals(originalJson, Files.readString(filePath));
        }
    }

    @Test
    public void readAddressBook_structuredRemark_throwsDataLoadingException() throws Exception {
        Path filePath = writeStudentJson(", \"remark\": {\"text\": \"Practice fractions\"}");
        assertThrows(DataLoadingException.class, () -> new JsonAddressBookStorage(filePath).readAddressBook());
    }

    @Test
    public void readAndSaveAddressBook_linkedStudents_preservesRemarksAndRelationships() throws Exception {
        AddressBook original = new AddressBook();
        Student student = student().withRemark(new Remark("Practice fractions"));
        Guardian first = guardian("Amy Tan", "92345678");
        Guardian second = guardian("Ben Tan", "93456789");
        original.addStudent(student);
        original.addGuardian(first);
        original.addGuardian(second);
        original.addStudentGuardianLink(new StudentGuardianLink(student, first));
        original.addStudentGuardianLink(new StudentGuardianLink(student, second));
        Path filePath = testFolder.resolve("links.json");
        JsonAddressBookStorage storage = new JsonAddressBookStorage(filePath);
        storage.saveAddressBook(original);

        assertEquals(original, storage.readAddressBook().orElseThrow());

        // Simulate a legacy file while keeping the original link records unchanged.
        JsonNode json = JsonUtil.fromJsonString(Files.readString(filePath), JsonNode.class);
        ObjectNode savedStudent = (ObjectNode) json.get("students").get(0);
        savedStudent.remove("remark");
        Files.writeString(filePath, JsonUtil.toJsonString(json));
        ReadOnlyAddressBook legacy = storage.readAddressBook().orElseThrow();
        Student loaded = legacy.getStudentList().get(0);
        assertSame(Remark.EMPTY, loaded.getRemark());
        assertEquals(List.of(first, second), legacy.getGuardianList());
        assertEquals(List.of(new StudentGuardianLink(loaded, first), new StudentGuardianLink(loaded, second)),
                legacy.getStudentGuardianLinkList());
    }

    private Path writeStudentJson(String remarkProperty) throws Exception {
        Path filePath = testFolder.resolve("input.json");
        Files.writeString(filePath, """
                {"students": [{"name": "Mary-Jane O'Connor", "phone": "+6591234567",
                "email": "mary@example.com", "address": "12 ABCD Ave 3, #04-15", "level": "JC 1",
                "subjects": ["A-Level (H2)", "Physics"]%s}]}
                """.formatted(remarkProperty));
        return filePath;
    }

    private String savedRemark(Path filePath) throws Exception {
        return JsonUtil.fromJsonString(Files.readString(filePath), JsonNode.class)
                .get("students").get(0).get("remark").asText();
    }

    private static Student student() {
        return new Student(Name.fromStudentValue("Mary-Jane O'Connor"), Phone.fromStudentValue("+6591234567"),
                Email.fromStudentValue("mary@example.com"), Address.fromStudentValue("12 ABCD Ave 3, #04-15"),
                new Level("JC 1"), Set.of(new Subject("A-Level (H2)"), new Subject("Physics")));
    }

    private static Guardian guardian(String name, String phone) {
        return new Guardian(new Name(name), new Phone(phone), new Email("guardian@example.com"),
                new Address("25 Happy Road"));
    }
}
