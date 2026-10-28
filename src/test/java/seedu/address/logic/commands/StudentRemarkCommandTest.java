package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.LogicManager;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.AddressBook;
import seedu.address.model.ModelManager;
import seedu.address.model.guardian.Guardian;
import seedu.address.model.lesson.Lesson;
import seedu.address.model.link.StudentGuardianLink;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.model.student.Level;
import seedu.address.model.student.PaymentMonth;
import seedu.address.model.student.Remark;
import seedu.address.model.student.Student;
import seedu.address.model.student.Subject;
import seedu.address.storage.JsonAddressBookStorage;
import seedu.address.storage.JsonUserPrefsStorage;
import seedu.address.storage.StorageManager;

public class StudentRemarkCommandTest {
    private static final Index FIRST = Index.fromOneBased(1);
    private static final Index SECOND = Index.fromOneBased(2);
    private static final Student ORIGINAL = new Student(new Name("Lucy Lim"), new Phone("91234567"),
            new Email("lucy@example.com"), new Address("21 Happy Road"), new Level("JC 1"),
            Set.of(new Subject("Mathematics"), new Subject("Physics")));
    private ModelManager model;
    private Guardian guardian;

    @BeforeEach
    public void setUp() {
        model = new ModelManager();
        model.addStudent(ORIGINAL);
        guardian = new Guardian(new Name("Ben Tan"), new Phone("92345678"), new Email("ben@example.com"),
                new Address("25 Happy Road"));
        model.addGuardian(guardian);
        model.addStudentGuardianLink(new StudentGuardianLink(ORIGINAL, guardian));
    }

    @Test
    public void execute_setAndReplace_preservesAllAttributesAndLinks() throws Exception {
        for (Remark remark : List.of(new Remark("Practice fractions"), new Remark("Practice  algebra"),
                new Remark("\ud83d\ude00".repeat(300)))) {
            CommandResult result = new SetStudentRemarkCommand(FIRST, remark).execute(model);
            Student updated = model.getFilteredStudentList().get(0);
            assertEquals(ORIGINAL.withRemark(remark), updated);
            assertEquals(List.of(updated), model.getAddressBook().getStudentList());
            assertEquals(List.of(guardian), model.getGuardiansForStudent(updated));
            assertEquals(List.of(new StudentGuardianLink(updated, guardian)),
                    model.getAddressBook().getStudentGuardianLinkList());
            assertEquals("Set remark for Lucy Lim (91234567): " + remark, result.getFeedbackToUser());
            assertEquals(updated, result.getStudentToHighlight());
        }
    }

    @Test
    public void execute_clear_preservesAllAttributesAndLinks() throws Exception {
        Student populated = ORIGINAL.withRemark(new Remark("Practice fractions"));
        model.setStudent(ORIGINAL, populated);
        CommandResult result = new ClearStudentRemarkCommand(FIRST).execute(model);
        assertEquals(List.of(ORIGINAL), model.getAddressBook().getStudentList());
        assertEquals(List.of(guardian), model.getGuardiansForStudent(ORIGINAL));
        assertEquals(List.of(new StudentGuardianLink(ORIGINAL, guardian)),
                model.getAddressBook().getStudentGuardianLinkList());
        assertEquals("Cleared remark for Lucy Lim (91234567).", result.getFeedbackToUser());
        assertEquals(ORIGINAL, result.getStudentToHighlight());
    }

    @Test
    public void execute_emptyClearAndIdenticalSet_doNotReplaceStudent() throws Exception {
        CommandResult result = new ClearStudentRemarkCommand(FIRST).execute(model);
        assertEquals("This student has no remark to clear: Lucy Lim (91234567).", result.getFeedbackToUser());
        assertSame(ORIGINAL, model.getFilteredStudentList().get(0));
        Remark remark = new Remark("Practice fractions");
        Student populated = ORIGINAL.withRemark(remark);
        model.setStudent(ORIGINAL, populated);
        new SetStudentRemarkCommand(FIRST, remark).execute(model);
        assertSame(populated, model.getFilteredStudentList().get(0));
    }

    @Test
    public void execute_filteredList_resolvesDisplayedIndexAndRetainsFilter() throws Exception {
        Student hidden = new Student(new Name("Amy Tan"), new Phone("93456789"), ORIGINAL.getEmail(),
                ORIGINAL.getAddress(), ORIGINAL.getLevel(), ORIGINAL.getSubjects());
        model.addStudent(hidden);
        model.updateFilteredStudentList(student -> student.getName().equals(hidden.getName()));
        Remark remark = new Remark("Practice fractions");
        new SetStudentRemarkCommand(FIRST, remark).execute(model);
        assertEquals(List.of(hidden.withRemark(remark)), model.getFilteredStudentList());
        assertEquals(List.of(ORIGINAL, hidden.withRemark(remark)), model.getAddressBook().getStudentList());
        new ClearStudentRemarkCommand(FIRST).execute(model);
        assertEquals(List.of(hidden), model.getFilteredStudentList());
        assertEquals(List.of(ORIGINAL, hidden), model.getAddressBook().getStudentList());
        assertEquals(List.of(guardian), model.getGuardiansForStudent(ORIGINAL));
    }

    @Test
    public void execute_sortedList_resolvesDisplayedIndexAndRetainsSort() throws Exception {
        Student first = new Student(new Name("Amy Tan"), new Phone("93456789"), ORIGINAL.getEmail(),
                ORIGINAL.getAddress(), ORIGINAL.getLevel(), ORIGINAL.getSubjects());
        model.addStudent(first);
        model.sortStudentList();
        Remark remark = new Remark("Practice fractions");
        new SetStudentRemarkCommand(FIRST, remark).execute(model);
        assertEquals(List.of(first.withRemark(remark), ORIGINAL), model.getFilteredStudentList());
        new ClearStudentRemarkCommand(FIRST).execute(model);
        assertEquals(List.of(first, ORIGINAL), model.getFilteredStudentList());
    }

    @Test
    public void execute_invalidIndex_leavesEntireModelUnchanged() {
        for (boolean filtered : List.of(false, true)) {
            if (filtered) {
                model.updateFilteredStudentList(unused -> false);
            }
            AddressBook before = new AddressBook(model.getAddressBook());
            for (Command command : List.of(new SetStudentRemarkCommand(SECOND, new Remark("Practice fractions")),
                    new ClearStudentRemarkCommand(SECOND))) {
                CommandException exception = assertThrows(CommandException.class, () -> command.execute(model));
                assertEquals(filtered ? Messages.MESSAGE_NO_STUDENTS_DISPLAYED
                        : Messages.MESSAGE_STUDENT_INDEX_OUT_OF_RANGE, exception.getMessage());
                assertEquals(before, model.getAddressBook());
            }
        }
    }

    @Test
    public void execute_emptyAddressBook_reportsNoDisplayedStudents() {
        ModelManager emptyModel = new ModelManager();
        for (Command command : List.of(new SetStudentRemarkCommand(FIRST, new Remark("Practice fractions")),
                new ClearStudentRemarkCommand(FIRST))) {
            CommandException exception = assertThrows(CommandException.class, () -> command.execute(emptyModel));
            assertEquals("No students are currently displayed.", exception.getMessage());
            assertTrue(emptyModel.getAddressBook().getStudentList().isEmpty());
        }
    }

    @Test
    public void logic_rejectedRemarkCommands_preserveDataAndSavedJson(@TempDir Path temporaryFolder) throws Exception {
        model.setStudent(ORIGINAL, ORIGINAL.withRemark(new Remark("Existing remark")));
        Student other = new Student(new Name("Amy Tan"), new Phone("93456789"), ORIGINAL.getEmail(),
                ORIGINAL.getAddress(), ORIGINAL.getLevel(), ORIGINAL.getSubjects());
        model.addStudent(other);
        Path dataPath = temporaryFolder.resolve("remarks.json");
        JsonAddressBookStorage jsonStorage = new JsonAddressBookStorage(dataPath);
        jsonStorage.saveAddressBook(model.getAddressBook());
        LogicManager logic = new LogicManager(model, new StorageManager(jsonStorage,
                new JsonUserPrefsStorage(temporaryFolder.resolve("userPrefs.json"))));
        AddressBook before = new AddressBook(model.getAddressBook());
        String savedJson = Files.readString(dataPath);

        Map<String, String> parseErrors = new LinkedHashMap<>();
        for (String token : List.of("abc", "-1", "0", "2147483648", "1\u00002")) {
            parseErrors.put("set-remark-s " + token + " Hello",
                    "Invalid command format.\nUsage: set-remark-s INDEX REMARK\nINDEX must be a positive integer.");
            parseErrors.put("clear-remark-s " + token,
                    "Invalid command format.\nUsage: clear-remark-s INDEX\nINDEX must be a positive integer.");
        }
        parseErrors.put("set-remark-s",
                "Invalid command format.\nUsage: set-remark-s INDEX REMARK\nINDEX must be a positive integer.");
        parseErrors.put("clear-remark-s",
                "Invalid command format.\nUsage: clear-remark-s INDEX\nINDEX must be a positive integer.");
        parseErrors.put("set-remark-s 1", SetStudentRemarkCommand.MESSAGE_REMARK_REQUIRED);
        parseErrors.put("set-remark-s 1 " + "a".repeat(301), Remark.MESSAGE_CONSTRAINTS);
        for (int displayedCount : List.of(2, 1, 0)) {
            model.updateFilteredStudentList(student -> displayedCount == 2
                    || displayedCount == 1 && student.getName().equals(ORIGINAL.getName()));
            assertEquals(displayedCount, model.getFilteredStudentList().size());
            for (Map.Entry<String, String> entry : parseErrors.entrySet()) {
                ParseException exception = assertThrows(ParseException.class, () -> logic.execute(entry.getKey()));
                assertEquals(entry.getValue(), exception.getMessage());
                assertEquals(before, model.getAddressBook());
                assertEquals(savedJson, Files.readString(dataPath));
            }
            String message = displayedCount == 0 ? "No students are currently displayed."
                    : "Student index is out of range, choose a valid index from the currently displayed student list";
            for (String input : List.of("set-remark-s 999 Hello", "clear-remark-s 999")) {
                CommandException exception = assertThrows(CommandException.class, () -> logic.execute(input));
                assertEquals(message, exception.getMessage());
                assertEquals(before, model.getAddressBook());
                assertEquals(savedJson, Files.readString(dataPath));
            }
            if (displayedCount == 1) {
                // Index 2 exists in storage, but is outside the currently displayed list.
                for (String input : List.of("set-remark-s 2 Hello", "clear-remark-s 2")) {
                    CommandException exception = assertThrows(CommandException.class, () -> logic.execute(input));
                    assertEquals(message, exception.getMessage());
                    assertEquals(before, model.getAddressBook());
                    assertEquals(savedJson, Files.readString(dataPath));
                }
            }
        }
    }

    @Test
    public void logic_studentUpdates_preservePaymentsRemarksAndReferences(@TempDir Path folder) throws Exception {
        PaymentMonth october = new PaymentMonth("2026-10");
        model.addLesson(new Lesson(ORIGINAL, LocalDate.of(2026, 10, 25), LocalTime.of(14, 0),
                Duration.ofHours(1)));
        JsonAddressBookStorage storage = new JsonAddressBookStorage(folder.resolve("integrated.json"));
        LogicManager logic = new LogicManager(model, new StorageManager(storage,
                new JsonUserPrefsStorage(folder.resolve("preferences.json"))));

        for (boolean paymentFirst : List.of(false, true)) {
            logic.execute("clear-remark-s 1");
            String month = paymentFirst ? "2026-11" : "2026-10";
            if (paymentFirst) {
                logic.execute("mark-paid 1 m/" + month);
                logic.execute("set-remark-s 1 Practice fractions");
            } else {
                logic.execute("set-remark-s 1 Practice fractions");
                logic.execute("mark-paid 1 m/" + month);
            }
            Student expected = model.getFilteredStudentList().get(0);
            assertEquals(new Remark("Practice fractions"), expected.getRemark());
            assertTrue(expected.hasPaidFor(october));
            for (String edit : List.of("n/Lucy Tan", "p/98765432", "e/new@example.com",
                    "a/12 Main Street", "l/JC 2", "s/Chemistry")) {
                logic.execute("edit-s 1 " + edit);
                Student edited = model.getFilteredStudentList().get(0);
                assertEquals(expected.getRemark(), edited.getRemark());
                assertEquals(expected.getPaidMonths(), edited.getPaidMonths());
                assertEquals(List.of(guardian), model.getGuardiansForStudent(edited));
                assertSame(edited, model.getLessonList().get(0).getStudent());
                assertSame(edited, model.getAddressBook().getStudentGuardianLinkList().get(0).getStudent());
                assertEquals(model.getAddressBook(), storage.readAddressBook().orElseThrow());
                expected = edited;
            }
            logic.execute("set-remark-s 1 Review algebra");
            assertEquals(expected.withRemark(new Remark("Review algebra")), model.getFilteredStudentList().get(0));
            logic.execute("clear-remark-s 1");
            Student cleared = model.getFilteredStudentList().get(0);
            assertEquals(expected.withRemark(Remark.EMPTY), cleared);
            assertSame(cleared, model.getLessonList().get(0).getStudent());
            assertEquals(List.of(guardian), model.getGuardiansForStudent(cleared));
            assertEquals(model.getAddressBook(), storage.readAddressBook().orElseThrow());
        }
    }

    @Test
    public void constructorAndExecute_invalidArguments_rejected() {
        Remark remark = new Remark("Practice fractions");
        assertThrows(NullPointerException.class, () -> new SetStudentRemarkCommand(null, remark));
        assertThrows(NullPointerException.class, () -> new SetStudentRemarkCommand(FIRST, null));
        assertThrows(IllegalArgumentException.class, () -> new SetStudentRemarkCommand(FIRST, Remark.EMPTY));
        assertThrows(NullPointerException.class, () -> new ClearStudentRemarkCommand(null));
        assertThrows(NullPointerException.class, () -> new SetStudentRemarkCommand(FIRST, remark).execute(null));
        assertThrows(NullPointerException.class, () -> new ClearStudentRemarkCommand(FIRST).execute(null));
    }

    @Test
    public void equalsAndToString_compareCommandValues() {
        Remark remark = new Remark("Practice fractions");
        SetStudentRemarkCommand set = new SetStudentRemarkCommand(FIRST, remark);
        assertEquals(set, set);
        assertEquals(set, new SetStudentRemarkCommand(FIRST, new Remark(" Practice fractions ")));
        assertNotEquals(set, new SetStudentRemarkCommand(SECOND, remark));
        assertNotEquals(set, new SetStudentRemarkCommand(FIRST, new Remark("Practice algebra")));
        assertNotEquals(set, null);
        assertNotEquals(set, "set");
        assertTrue(set.toString().contains("remark=Practice fractions"));
        ClearStudentRemarkCommand clear = new ClearStudentRemarkCommand(FIRST);
        assertEquals(clear, clear);
        assertEquals(clear, new ClearStudentRemarkCommand(FIRST));
        assertNotEquals(clear, new ClearStudentRemarkCommand(SECOND));
        assertNotEquals(clear, null);
        assertFalse(clear.equals(set));
        assertTrue(clear.toString().contains("index="));
    }
}
