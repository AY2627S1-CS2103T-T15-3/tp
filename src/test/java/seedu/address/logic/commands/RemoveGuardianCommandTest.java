
package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.ModelManager;
import seedu.address.model.guardian.Guardian;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.model.student.Level;
import seedu.address.model.student.Student;
import seedu.address.model.student.Subject;

public class RemoveGuardianCommandTest {

    private static final Guardian BEN =
            guardian("Ben Tan", "92345678", "ben@example.com", "12 ABCD Ave 3");

    private static final Guardian BILLIE =
            guardian("Billie Tan", "98765432", "billie@example.com", "25 Happy Road");

    @Test
    public void execute_validIndexUnfilteredList_success() throws Exception {
        ModelManager model = new ModelManager();
        model.addGuardian(BEN);
        model.addGuardian(BILLIE);

        CommandResult result = new RemoveGuardianCommand(INDEX_FIRST_PERSON).execute(model);

        assertEquals(String.format(RemoveGuardianCommand.MESSAGE_REMOVE_GUARDIAN_SUCCESS,
                Messages.format(BEN)), result.getFeedbackToUser());
        assertEquals(List.of(BILLIE), model.getFilteredGuardianList());
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        ModelManager model = new ModelManager();
        model.addGuardian(BEN);

        assertThrows(CommandException.class, Messages.MESSAGE_INVALID_GUARDIAN_DISPLAYED_INDEX, ()
                -> new RemoveGuardianCommand(INDEX_SECOND_PERSON).execute(model));

        assertEquals(List.of(BEN), model.getFilteredGuardianList());
    }

    @Test
    public void execute_validIndexFilteredList_success() throws Exception {
        ModelManager model = new ModelManager();
        model.addGuardian(BEN);
        model.addGuardian(BILLIE);

        model.updateFilteredGuardianList(guardian -> guardian.equals(BILLIE));

        CommandResult result = new RemoveGuardianCommand(INDEX_FIRST_PERSON).execute(model);

        assertEquals(String.format(RemoveGuardianCommand.MESSAGE_REMOVE_GUARDIAN_SUCCESS,
                Messages.format(BILLIE)), result.getFeedbackToUser());
        assertFalse(model.hasGuardian(BILLIE));
        assertTrue(model.hasGuardian(BEN));
    }

    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() {
        ModelManager model = new ModelManager();
        model.addGuardian(BEN);
        model.addGuardian(BILLIE);

        model.updateFilteredGuardianList(guardian -> guardian.equals(BILLIE));

        assertThrows(CommandException.class, Messages.MESSAGE_INVALID_GUARDIAN_DISPLAYED_INDEX, ()
                -> new RemoveGuardianCommand(INDEX_SECOND_PERSON).execute(model));

        assertTrue(model.hasGuardian(BEN));
        assertTrue(model.hasGuardian(BILLIE));
    }

    @Test
    public void equals() {
        RemoveGuardianCommand firstCommand = new RemoveGuardianCommand(INDEX_FIRST_PERSON);
        RemoveGuardianCommand secondCommand = new RemoveGuardianCommand(INDEX_SECOND_PERSON);

        assertTrue(firstCommand.equals(firstCommand));
        assertTrue(firstCommand.equals(new RemoveGuardianCommand(INDEX_FIRST_PERSON)));
        assertFalse(firstCommand.equals(secondCommand));
        assertFalse(firstCommand.equals(null));
        assertFalse(firstCommand.equals(new Object()));
    }

    @Test
    public void toString_validIndex() {
        RemoveGuardianCommand command = new RemoveGuardianCommand(INDEX_FIRST_PERSON);

        assertTrue(command.toString().contains("targetIndex"));
        assertTrue(command.toString().contains(INDEX_FIRST_PERSON.toString()));
    }

    private static Guardian guardian(String name, String phone, String email, String address) {
        return new Guardian(Name.fromStudentValue(name), Phone.fromStudentValue(phone),
                Email.fromStudentValue(email), Address.fromStudentValue(address));
    }

    @Test
    public void execute_removeGuardian_studentsRemain() throws Exception {
        ModelManager model = new ModelManager();

        Student student = new Student(
                Name.fromStudentValue("Henry Tan"),
                Phone.fromStudentValue("91234567"),
                Email.fromStudentValue("henry@example.com"),
                Address.fromStudentValue("12 ABCD Ave 3"),
                new Level("Secondary 3"),
                Set.of(new Subject("Mathematics")));

        model.addStudent(student);
        model.addGuardian(BEN);

        new RemoveGuardianCommand(INDEX_FIRST_PERSON).execute(model);

        assertFalse(model.hasGuardian(BEN));
        assertTrue(model.hasStudent(student));
    }
}
