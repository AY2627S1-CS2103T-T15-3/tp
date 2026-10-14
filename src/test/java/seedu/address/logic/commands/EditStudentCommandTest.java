package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.LinkedHashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.model.student.Level;
import seedu.address.model.student.Student;
import seedu.address.model.student.Subject;

public class EditStudentCommandTest {
    private static final Student ORIGINAL = student("Lucy Lim", "91234567", "lucy@example.com",
            "21 Happy Road", "JC 1", "Mathematics", "Physics");

    @Test
    public void execute_validEdit_updatesStudentAndReturnsSuccess() throws Exception {
        ModelManager model = modelWith(ORIGINAL);
        EditStudentDescriptor descriptor = new EditStudentDescriptor();
        descriptor.setPhone(new Phone("98765432"));
        EditStudentCommand command = new EditStudentCommand(Index.fromOneBased(1), descriptor);

        CommandResult result = command.execute(model);

        Student edited = model.getFilteredStudentList().get(0);
        assertEquals(new Phone("98765432"), edited.getPhone());
        assertEquals(String.format(EditStudentCommand.MESSAGE_SUCCESS, Messages.format(edited)),
                result.getFeedbackToUser());
    }

    @Test
    public void execute_outOfRangeIndex_throwsCommandException() {
        EditStudentCommand command = new EditStudentCommand(Index.fromOneBased(2), new EditStudentDescriptor());

        CommandException exception = assertThrows(
                CommandException.class, () -> command.execute(modelWith(ORIGINAL)));

        assertEquals(EditStudentCommand.MESSAGE_INVALID_INDEX, exception.getMessage());
    }

    @Test
    public void execute_duplicateIdentity_throwsCommandException() {
        Student other = student("Ben Tan", "98765432", "ben@example.com", "25 Happy Road", "JC 1", "Physics");
        ModelManager model = modelWith(ORIGINAL, other);
        EditStudentDescriptor descriptor = new EditStudentDescriptor();
        descriptor.setName(new Name("Ben Tan"));
        descriptor.setPhone(new Phone("98765432"));

        CommandException exception = assertThrows(
                CommandException.class, () -> new EditStudentCommand(Index.fromOneBased(1), descriptor).execute(model));

        assertEquals(EditStudentCommand.MESSAGE_DUPLICATE_STUDENT, exception.getMessage());
    }

    @Test
    public void execute_sameIdentityEdit_isAllowed() throws Exception {
        ModelManager model = modelWith(ORIGINAL);
        EditStudentDescriptor descriptor = new EditStudentDescriptor();
        descriptor.setEmail(new Email("new@example.com"));

        new EditStudentCommand(Index.fromOneBased(1), descriptor).execute(model);

        assertEquals(new Email("new@example.com"), model.getFilteredStudentList().get(0).getEmail());
    }

    @Test
    public void execute_filteredIndex_editsDisplayedStudent() throws Exception {
        Student other = student("Ben Tan", "98765432", "ben@example.com", "25 Happy Road", "JC 1", "Physics");
        ModelManager model = modelWith(ORIGINAL, other);
        model.updateFilteredStudentList(student -> student.getName().fullName.equals("Ben Tan"));
        EditStudentDescriptor descriptor = new EditStudentDescriptor();
        descriptor.setLevel(new Level("JC 2"));

        new EditStudentCommand(Index.fromOneBased(1), descriptor).execute(model);

        Student edited = model.getFilteredStudentList().stream()
                .filter(student -> student.getName().fullName.equals("Ben Tan"))
                .findFirst()
                .orElseThrow();
        assertEquals(new Level("JC 2"), edited.getLevel());
    }

    @Test
    public void equals() {
        EditStudentDescriptor descriptor = new EditStudentDescriptor();
        descriptor.setPhone(new Phone("98765432"));
        EditStudentCommand command = new EditStudentCommand(Index.fromOneBased(1), descriptor);

        assertTrue(command.equals(command));
        assertTrue(command.equals(new EditStudentCommand(Index.fromOneBased(1), descriptor)));
        assertFalse(command.equals(null));
        assertFalse(command.equals(1));
        assertFalse(command.equals(new EditStudentCommand(Index.fromOneBased(2), descriptor)));
    }

    @Test
    public void constructor_nullArguments_throwsNullPointerException() {
        assertThrows(
                NullPointerException.class, () -> new EditStudentCommand(null, new EditStudentDescriptor()));
        assertThrows(
                NullPointerException.class, () -> new EditStudentCommand(Index.fromOneBased(1), null));
    }

    @Test
    public void toStringMethod_containsFields() {
        EditStudentCommand command = new EditStudentCommand(Index.fromOneBased(1), new EditStudentDescriptor());

        assertTrue(command.toString().contains("index="));
        assertTrue(command.toString().contains("editStudentDescriptor="));
    }

    private static ModelManager modelWith(Student... students) {
        AddressBook addressBook = new AddressBook();
        for (Student student : students) {
            addressBook.addStudent(student);
        }
        return new ModelManager(addressBook, new UserPrefs());
    }

    private static Student student(String name, String phone, String email, String address, String level,
            String... subjects) {
        Set<Subject> subjectSet = new LinkedHashSet<>();
        for (String subject : subjects) {
            subjectSet.add(new Subject(subject));
        }
        return new Student(new Name(name), new Phone(phone), new Email(email), new Address(address),
                new Level(level), subjectSet);
    }
}
