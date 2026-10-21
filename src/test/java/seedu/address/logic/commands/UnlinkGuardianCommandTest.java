package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static seedu.address.testutil.Assert.assertThrows;

import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.ModelManager;
import seedu.address.model.guardian.Guardian;
import seedu.address.model.link.StudentGuardianLink;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.model.student.Level;
import seedu.address.model.student.Student;
import seedu.address.model.student.Subject;

public class UnlinkGuardianCommandTest {
    private static final Student STUDENT = new Student(new Name("Lucy Lim"), new Phone("91234567"),
            new Email("lucy@example.com"), new Address("21 Happy Road"), new Level("JC 1"),
            Set.of(new Subject("Mathematics")));
    private static final Guardian GUARDIAN = new Guardian(new Name("Ben Tan"), new Phone("92345678"),
            new Email("ben@example.com"), new Address("25 Happy Road"));

    @Test
    public void execute_validIndexes_unlinksContacts() throws Exception {
        ModelManager model = modelWithLink();

        CommandResult result = new UnlinkGuardianCommand(Index.fromOneBased(1), Index.fromOneBased(1)).execute(model);

        assertEquals(String.format(UnlinkGuardianCommand.MESSAGE_SUCCESS, STUDENT.getName(), GUARDIAN.getName()),
                result.getFeedbackToUser());
        assertEquals(STUDENT, result.getStudentToHighlight());
        assertFalse(model.hasStudentGuardianLink(new StudentGuardianLink(STUDENT, GUARDIAN)));
    }

    @Test
    public void execute_missingLink_throwsCommandException() throws Exception {
        ModelManager model = new ModelManager();
        model.addStudent(STUDENT);
        model.addGuardian(GUARDIAN);

        assertThrows(CommandException.class, UnlinkGuardianCommand.MESSAGE_LINK_NOT_FOUND, () ->
                new UnlinkGuardianCommand(Index.fromOneBased(1), Index.fromOneBased(1)).execute(model));
    }

    @Test
    public void execute_invalidIndexes_throwsCommandException() throws Exception {
        ModelManager model = modelWithLink();

        assertThrows(CommandException.class, UnlinkGuardianCommand.MESSAGE_INVALID_STUDENT_INDEX, () ->
                new UnlinkGuardianCommand(Index.fromOneBased(2), Index.fromOneBased(1)).execute(model));
        assertThrows(CommandException.class, UnlinkGuardianCommand.MESSAGE_INVALID_GUARDIAN_INDEX, () ->
                new UnlinkGuardianCommand(Index.fromOneBased(1), Index.fromOneBased(2)).execute(model));
    }

    @Test
    public void equals_sameValuesAndDifferentTypes_returnsExpectedResult() {
        UnlinkGuardianCommand command = new UnlinkGuardianCommand(Index.fromOneBased(1), Index.fromOneBased(1));

        assertEquals(command, command);
        assertEquals(command, new UnlinkGuardianCommand(Index.fromOneBased(1), Index.fromOneBased(1)));
        assertFalse(command.equals(null));
        assertFalse(command.equals(0.5f));
        assertFalse(command.equals(new UnlinkGuardianCommand(Index.fromOneBased(2), Index.fromOneBased(1))));
        assertFalse(command.equals(new UnlinkGuardianCommand(Index.fromOneBased(1), Index.fromOneBased(2))));
    }

    private static ModelManager modelWithLink() {
        ModelManager model = new ModelManager();
        model.addStudent(STUDENT);
        model.addGuardian(GUARDIAN);
        model.addStudentGuardianLink(new StudentGuardianLink(STUDENT, GUARDIAN));
        return model;
    }
}
