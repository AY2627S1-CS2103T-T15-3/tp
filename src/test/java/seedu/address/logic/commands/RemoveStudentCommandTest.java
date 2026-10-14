package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.ModelManager;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.model.student.Level;
import seedu.address.model.student.Student;
import seedu.address.model.student.Subject;
/**
 * Contains integration tests (interaction with the Model) and unit tests for
 * {@code RemoveStudentCommand}.
 */
public class RemoveStudentCommandTest {

    private static final Student HENRY = student("Henry Tan", "91234567",
            "henry@example.com", "12 ABCD Ave 3", "Secondary 3", "Mathematics");

    private static final Student LUCY = student("Lucy Lim", "98765432",
            "lucy@example.com", "25 Happy Road", "JC 1", "Physics");

    @Test
    public void execute_validIndexUnfilteredList_success() throws Exception {
        ModelManager model = new ModelManager();
        model.addStudent(HENRY);
        model.addStudent(LUCY);

        RemoveStudentCommand command = new RemoveStudentCommand(Index.fromOneBased(1));
        CommandResult result = command.execute(model);

        assertEquals(String.format(RemoveStudentCommand.MESSAGE_REMOVE_STUDENT_SUCCESS,
                Messages.format(HENRY)), result.getFeedbackToUser());
        assertEquals(List.of(LUCY), model.getFilteredStudentList());
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        ModelManager model = new ModelManager();
        model.addStudent(HENRY);
        RemoveStudentCommand command = new RemoveStudentCommand(Index.fromOneBased(2));
        assertThrows(CommandException.class, Messages.MESSAGE_INVALID_STUDENT_DISPLAYED_INDEX, ()
                -> command.execute(model));
        assertEquals(List.of(HENRY), model.getFilteredStudentList());
    }

    @Test
    public void execute_validIndexFilteredList_success() throws Exception {
        ModelManager model = new ModelManager();
        model.addStudent(HENRY);
        model.addStudent(LUCY);

        // Only Lucy is displayed, so she becomes displayed index 1.
        model.updateFilteredStudentList(s -> s.equals(LUCY));

        RemoveStudentCommand command = new RemoveStudentCommand(Index.fromOneBased(1));
        CommandResult result = command.execute(model);

        assertEquals(String.format(RemoveStudentCommand.MESSAGE_REMOVE_STUDENT_SUCCESS,
                Messages.format(LUCY)), result.getFeedbackToUser());
        assertFalse(model.getAddressBook().getStudentList().contains(LUCY));
        assertTrue(model.getAddressBook().getStudentList().contains(HENRY));
    }

    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() {
        ModelManager model = new ModelManager();
        model.addStudent(HENRY);
        model.addStudent(LUCY);

        // Only Henry is displayed.
        model.updateFilteredStudentList(s -> s.equals(HENRY));

        RemoveStudentCommand command = new RemoveStudentCommand(Index.fromOneBased(2));

        assertThrows(CommandException.class, Messages.MESSAGE_INVALID_STUDENT_DISPLAYED_INDEX, ()
                -> command.execute(model));
    }

    @Test
    public void equals() {
        RemoveStudentCommand first = new RemoveStudentCommand(Index.fromOneBased(1));
        RemoveStudentCommand firstCopy = new RemoveStudentCommand(Index.fromOneBased(1));
        RemoveStudentCommand second = new RemoveStudentCommand(Index.fromOneBased(2));

        // same object -> returns true
        assertTrue(first.equals(first));

        // same values -> returns true
        assertTrue(first.equals(firstCopy));

        // different indices -> returns false
        assertFalse(first.equals(second));

        // null -> returns false
        assertFalse(first.equals(null));

        // different types -> returns false
        assertFalse(first.equals(1));
    }

    @Test
    public void toStringMethod() {
        Index targetIndex = Index.fromOneBased(1);
        RemoveStudentCommand command = new RemoveStudentCommand(targetIndex);
        String expected = RemoveStudentCommand.class.getCanonicalName() + "{targetIndex=" + targetIndex + "}";
        assertEquals(expected, command.toString());
    }

    /**
     * Creates a student for testing.
     */
    private static Student student(String name, String phone, String email, String address, String level,
                                   String... subjects) {
        Set<Subject> subjectSet = new LinkedHashSet<>();
        for (String subj : subjects) {
            subjectSet.add(new Subject(subj));
        }

        return new Student(Name.fromStudentValue(name), Phone.fromStudentValue(phone),
                Email.fromStudentValue(email), Address.fromStudentValue(address),
                new Level(level), subjectSet);
    }

}
