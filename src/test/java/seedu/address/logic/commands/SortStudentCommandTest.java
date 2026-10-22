package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.model.ModelManager;
import seedu.address.model.guardian.Guardian;
import seedu.address.model.link.StudentGuardianLink;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.model.student.Level;
import seedu.address.model.student.Student;
import seedu.address.model.student.StudentMatchesFilterPredicate;
import seedu.address.model.student.Subject;

/** Contains integration tests for {@code SortStudentCommand}. */
public class SortStudentCommandTest {

    @Test
    public void execute_unorderedStudents_sortsDisplayedList() {
        ModelManager model = modelWith(
                student("Zoe Tan", "91234567"),
                student("alice Lim", "92345678"),
                student("Bob Ng", "93456789"));

        CommandResult result = new SortStudentCommand().execute(model);

        assertEquals(SortStudentCommand.MESSAGE_SUCCESS, result.getFeedbackToUser());
        assertEquals(List.of(student("alice Lim", "92345678"), student("Bob Ng", "93456789"),
                student("Zoe Tan", "91234567")), model.getFilteredStudentList());
    }

    @Test
    public void execute_filterThenSort_sortsOnlyDisplayedStudents() {
        Student zoe = student("Zoe Tan", "91234567", "Physics");
        Student alice = student("Alice Lim", "92345678");
        Student bob = student("Bob Ng", "93456789");
        ModelManager model = modelWith(zoe, alice, bob);
        FilterCommand filterCommand = new FilterCommand(new StudentMatchesFilterPredicate(Optional.empty(),
                Set.of(new Subject("Mathematics"))));
        filterCommand.execute(model);

        new SortStudentCommand().execute(model);

        assertEquals(List.of(alice, bob), model.getFilteredStudentList());
        assertEquals(List.of(zoe, alice, bob), model.getAddressBook().getStudentList());
    }

    @Test
    public void execute_sortThenFilter_preservesSortedOrder() {
        Student charlie = student("Charlie Ng", "91234567", "Physics");
        Student alice = student("Alice Lim", "92345678");
        Student bob = student("Bob Tan", "93456789");
        ModelManager model = modelWith(charlie, alice, bob);

        new SortStudentCommand().execute(model);
        new FilterCommand(new StudentMatchesFilterPredicate(Optional.empty(),
                Set.of(new Subject("Mathematics")))).execute(model);

        assertEquals(List.of(alice, bob), model.getFilteredStudentList());
    }

    @Test
    public void execute_sortedList_editUsesSortedIndex() throws Exception {
        Student zoe = student("Zoe Tan", "91234567");
        Student alice = student("Alice Lim", "92345678");
        ModelManager model = modelWith(zoe, alice);

        new SortStudentCommand().execute(model);

        EditStudentDescriptor descriptor = new EditStudentDescriptor();
        descriptor.setEmail(new Email("edited@example.com"));
        new EditStudentCommand(Index.fromOneBased(1), descriptor).execute(model);

        Student editedAlice = model.getAddressBook().getStudentList().stream()
                .filter(student -> student.getPhone().equals(alice.getPhone()))
                .findFirst()
                .orElseThrow();
        assertEquals(new Email("edited@example.com"), editedAlice.getEmail());
        assertEquals(List.of(zoe, editedAlice), model.getAddressBook().getStudentList());
    }

    @Test
    public void execute_sortedList_deleteUsesSortedIndex() throws Exception {
        Student zoe = student("Zoe Tan", "91234567");
        Student alice = student("Alice Lim", "92345678");
        ModelManager model = modelWith(zoe, alice);

        new SortStudentCommand().execute(model);

        new RemoveStudentCommand(Index.fromOneBased(2)).execute(model);

        assertEquals(List.of(alice), model.getAddressBook().getStudentList());
    }

    @Test
    public void execute_sortedList_linkUsesSortedStudentIndex() throws Exception {
        Student zoe = student("Zoe Tan", "91234567");
        Student alice = student("Alice Lim", "92345678");
        Guardian guardian = guardian("Ben Lim", "94567890");
        ModelManager model = modelWith(zoe, alice);
        model.addGuardian(guardian);

        new SortStudentCommand().execute(model);
        CommandResult result = new LinkGuardianCommand(Index.fromOneBased(1), Index.fromOneBased(1)).execute(model);

        assertEquals(alice, result.getStudentToHighlight());
        assertEquals(List.of(guardian), model.getGuardiansForStudent(alice));
        assertTrue(model.hasStudentGuardianLink(new StudentGuardianLink(alice, guardian)));
        assertTrue(model.getGuardiansForStudent(zoe).isEmpty());
    }

    @Test
    public void execute_repeatedly_isIdempotent() {
        ModelManager model = modelWith(student("Zoe Tan", "91234567"), student("Alice Lim", "92345678"));
        SortStudentCommand command = new SortStudentCommand();

        command.execute(model);
        command.execute(model);

        assertEquals(List.of(student("Alice Lim", "92345678"), student("Zoe Tan", "91234567")),
                model.getFilteredStudentList());
    }

    @Test
    public void execute_emptyList_remainsEmpty() {
        ModelManager model = modelWith();

        CommandResult result = new SortStudentCommand().execute(model);

        assertEquals(SortStudentCommand.MESSAGE_SUCCESS, result.getFeedbackToUser());
        assertTrue(model.getFilteredStudentList().isEmpty());
        assertTrue(model.getAddressBook().getStudentList().isEmpty());
    }

    @Test
    public void execute_nullModel_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new SortStudentCommand().execute(null));
    }

    @Test
    public void equals() {
        SortStudentCommand command = new SortStudentCommand();

        assertTrue(command.equals(command));
        assertTrue(command.equals(new SortStudentCommand()));
        assertFalse(command.equals(null));
        assertFalse(command.equals(1));
    }

    private static ModelManager modelWith(Student... students) {
        ModelManager model = new ModelManager();
        for (Student student : students) {
            model.addStudent(student);
        }
        return model;
    }

    private static Student student(String name, String phone) {
        return student(name, phone, "Mathematics");
    }

    private static Student student(String name, String phone, String subject) {
        return new Student(Name.fromStudentValue(name), Phone.fromStudentValue(phone),
                Email.fromStudentValue(name.replace(" ", "").toLowerCase() + "@example.com"),
                Address.fromStudentValue("12 Main Street"), new Level("JC 1"),
                new LinkedHashSet<>(List.of(new Subject(subject))));
    }

    private static Guardian guardian(String name, String phone) {
        return new Guardian(Name.fromStudentValue(name), Phone.fromStudentValue(phone),
                Email.fromStudentValue("guardian@example.com"), Address.fromStudentValue("12 Main Street"));
    }
}
