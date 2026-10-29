package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
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

public class LinkGuardianCommandTest {
    private static final Student STUDENT = new Student(new Name("Lucy Lim"), new Phone("91234567"),
            new Email("lucy@example.com"), new Address("21 Happy Road"), new Level("JC 1"),
            Set.of(new Subject("Mathematics")));
    private static final Guardian GUARDIAN = new Guardian(new Name("Ben Tan"), new Phone("92345678"),
            new Email("ben@example.com"), new Address("25 Happy Road"));

    @Test
    public void execute_validIndexes_linksContacts() throws Exception {
        ModelManager model = new ModelManager();
        model.addStudent(STUDENT);
        model.addGuardian(GUARDIAN);

        CommandResult result = new LinkGuardianCommand(Index.fromOneBased(1), Index.fromOneBased(1)).execute(model);

        assertEquals(String.format(LinkGuardianCommand.MESSAGE_SUCCESS, STUDENT.getName(), GUARDIAN.getName()),
                result.getFeedbackToUser());
        assertEquals(STUDENT, result.getStudentToHighlight());
        assertEquals(null, result.getGuardianToHighlight());
        assertEquals(true, model.hasStudentGuardianLink(new StudentGuardianLink(STUDENT, GUARDIAN)));
    }

    @Test
    public void execute_duplicateLink_failure() throws Exception {
        ModelManager model = new ModelManager();
        model.addStudent(STUDENT);
        model.addGuardian(GUARDIAN);
        LinkGuardianCommand command = new LinkGuardianCommand(Index.fromOneBased(1), Index.fromOneBased(1));
        command.execute(model);

        CommandException exception = assertThrows(CommandException.class, () -> command.execute(model));

        assertEquals("Student Lucy Lim is already linked to guardian Ben Tan.", exception.getMessage());
        assertEquals(1, model.getGuardiansForStudent(STUDENT).size());
        assertEquals(List.of(GUARDIAN), model.getGuardiansForStudent(STUDENT));
    }

    @Test
    public void execute_invalidIndexes_throwsCommandException() throws Exception {
        ModelManager model = new ModelManager();
        model.addStudent(STUDENT);
        model.addGuardian(GUARDIAN);

        CommandException invalidStudentIndex = assertThrows(CommandException.class, () ->
                new LinkGuardianCommand(Index.fromOneBased(2), Index.fromOneBased(1)).execute(model));
        assertEquals(LinkGuardianCommand.MESSAGE_INVALID_STUDENT_INDEX, invalidStudentIndex.getMessage());
        CommandException invalidGuardianIndex = assertThrows(CommandException.class, () ->
                new LinkGuardianCommand(Index.fromOneBased(1), Index.fromOneBased(2)).execute(model));
        assertEquals(LinkGuardianCommand.MESSAGE_INVALID_GUARDIAN_INDEX, invalidGuardianIndex.getMessage());
    }

    @Test
    public void equals_sameValuesAndDifferentTypes_returnsExpectedResult() {
        LinkGuardianCommand command = new LinkGuardianCommand(Index.fromOneBased(1), Index.fromOneBased(1));

        assertEquals(command, command);
        assertEquals(command, new LinkGuardianCommand(Index.fromOneBased(1), Index.fromOneBased(1)));
        assertFalse(command.equals(null));
        assertFalse(command.equals(0.5f));
        assertFalse(command.equals(new LinkGuardianCommand(Index.fromOneBased(2), Index.fromOneBased(1))));
        assertFalse(command.equals(new LinkGuardianCommand(Index.fromOneBased(1), Index.fromOneBased(2))));
    }
}
