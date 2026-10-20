package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.AddGuardianCommand;
import seedu.address.logic.commands.AddStudentCommand;
import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.EditCommand.EditPersonDescriptor;
import seedu.address.logic.commands.EditGuardianCommand;
import seedu.address.logic.commands.EditStudentCommand;
import seedu.address.logic.commands.ExitCommand;
import seedu.address.logic.commands.FilterCommand;
import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.commands.FindGuardianCommand;
import seedu.address.logic.commands.FindStudentCommand;
import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.commands.LinkGuardianCommand;
import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.commands.ListGuardianCommand;
import seedu.address.logic.commands.ListStudentCommand;
import seedu.address.logic.commands.RemoveGuardianCommand;
import seedu.address.logic.commands.RemoveStudentCommand;
import seedu.address.logic.commands.SortStudentCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.guardian.GuardianNameContainsKeywordsPredicate;
import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.model.person.Person;
import seedu.address.model.student.Level;
import seedu.address.model.student.StudentMatchesFilterPredicate;
import seedu.address.model.student.StudentNameContainsKeywordsPredicate;
import seedu.address.model.student.Subject;
import seedu.address.testutil.EditPersonDescriptorBuilder;
import seedu.address.testutil.PersonBuilder;
import seedu.address.testutil.PersonUtil;

public class AddressBookParserTest {

    private final AddressBookParser parser = new AddressBookParser();

    @Test
    public void parseCommand_add() throws Exception {
        Person person = new PersonBuilder().build();
        AddCommand command = (AddCommand) parser.parseCommand(PersonUtil.getAddCommand(person));
        assertEquals(new AddCommand(person), command);
    }

    @Test
    public void parseCommand_addStudent() throws Exception {
        Command command = parser.parseCommand("add-s n/Henry Tan p/91234567 e/henry@example.com "
                + "a/12 ABCD Ave 3 l/JC 1 s/Mathematics");
        assertTrue(command instanceof AddStudentCommand);
    }

    @Test
    public void parseCommand_addGuardian() throws Exception {
        Command command = parser.parseCommand("add-g n/Ben Tan p/92345678 e/ben@example.com a/12 ABCD Ave 3");
        assertTrue(command instanceof AddGuardianCommand);
    }

    @Test
    public void parseCommand_editGuardian() throws Exception {
        Command command = parser.parseCommand("edit-g 1 n/Ben Tan");
        assertTrue(command instanceof EditGuardianCommand);
    }

    @Test
    public void parseCommand_linkGuardian() throws Exception {
        Command command = parser.parseCommand("link-g 1 g/2");
        assertTrue(command instanceof LinkGuardianCommand);
    }

    @Test
    public void parseCommand_clear() throws Exception {
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD) instanceof ClearCommand);
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD + " 3") instanceof ClearCommand);
    }

    @Test
    public void parseCommand_delete() throws Exception {
        DeleteCommand command = (DeleteCommand) parser.parseCommand(
                DeleteCommand.COMMAND_WORD + " " + INDEX_FIRST_PERSON.getOneBased());
        assertEquals(new DeleteCommand(INDEX_FIRST_PERSON), command);
    }

    @Test
    public void parseCommand_removeGuardian() throws Exception {
        RemoveGuardianCommand command = (RemoveGuardianCommand) parser.parseCommand("remove-g 1");
        assertEquals(new RemoveGuardianCommand(INDEX_FIRST_PERSON), command);
    }

    @Test
    public void parseCommand_removeStudent() throws Exception {
        RemoveStudentCommand command = (RemoveStudentCommand) parser.parseCommand(
                RemoveStudentCommand.COMMAND_WORD + " " + INDEX_FIRST_PERSON.getOneBased());
        assertEquals(new RemoveStudentCommand(INDEX_FIRST_PERSON), command);
    }

    @Test
    public void parseCommand_edit() throws Exception {
        Person person = new PersonBuilder().build();
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder(person).build();
        EditCommand command = (EditCommand) parser.parseCommand(EditCommand.COMMAND_WORD + " "
                + INDEX_FIRST_PERSON.getOneBased() + " " + PersonUtil.getEditPersonDescriptorDetails(descriptor));
        assertEquals(new EditCommand(INDEX_FIRST_PERSON, descriptor), command);
    }

    @Test
    public void parseCommand_editStudent() throws Exception {
        Command command = parser.parseCommand("edit-s 1 p/98765432");

        assertTrue(command instanceof EditStudentCommand);
    }

    @Test
    public void parseCommand_exit() throws Exception {
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD) instanceof ExitCommand);
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD + " 3") instanceof ExitCommand);
    }

    @Test
    public void parseCommand_filter() throws Exception {
        FilterCommand command = (FilterCommand) parser.parseCommand(
                FilterCommand.COMMAND_WORD + " l/JC 1 s/Mathematics");
        assertEquals(new FilterCommand(new StudentMatchesFilterPredicate(
                Optional.of(new Level("JC 1")), Set.of(new Subject("Mathematics")))), command);
    }

    @Test
    public void parseCommand_find() throws Exception {
        List<String> keywords = List.of("foo", "bar", "baz");
        FindCommand command = (FindCommand) parser.parseCommand(
                FindCommand.COMMAND_WORD + " " + keywords.stream().collect(Collectors.joining(" ")));
        assertEquals(new FindCommand(new NameContainsKeywordsPredicate(keywords)), command);
    }

    @Test
    public void parseCommand_findStudent() throws Exception {
        List<String> keywords = List.of("Alice", "Bob");
        FindStudentCommand command = (FindStudentCommand) parser.parseCommand(
                FindStudentCommand.COMMAND_WORD + " " + keywords.stream().collect(Collectors.joining(" ")));
        assertEquals(new FindStudentCommand(new StudentNameContainsKeywordsPredicate(keywords)), command);
    }

    @Test
    public void parseCommand_findGuardian() throws Exception {
        // Verifies that the top-level parser routes find-g to the guardian-search parser.
        List<String> keywords = List.of("Alice", "Bob");
        FindGuardianCommand command = (FindGuardianCommand) parser.parseCommand(
                FindGuardianCommand.COMMAND_WORD + " " + keywords.stream().collect(Collectors.joining(" ")));
        assertEquals(new FindGuardianCommand(new GuardianNameContainsKeywordsPredicate(keywords)), command);
    }

    @Test
    public void parseCommand_help() throws Exception {
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD) instanceof HelpCommand);
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD + " 3") instanceof HelpCommand);
    }

    @Test
    public void parseCommand_list() throws Exception {
        assertTrue(parser.parseCommand(ListCommand.COMMAND_WORD) instanceof ListCommand);
        assertTrue(parser.parseCommand(ListCommand.COMMAND_WORD + " 3") instanceof ListCommand);
    }

    @Test
    public void parseCommand_listGuardian() throws Exception {
        assertTrue(parser.parseCommand(ListGuardianCommand.COMMAND_WORD) instanceof ListGuardianCommand);
        assertThrows(ParseException.class,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ListGuardianCommand.MESSAGE_USAGE), ()
                -> parser.parseCommand(ListGuardianCommand.COMMAND_WORD + " 3"));
    }

    @Test
    public void parseCommand_listStudent() throws Exception {
        assertTrue(parser.parseCommand(ListStudentCommand.COMMAND_WORD) instanceof ListStudentCommand);
        assertThrows(ParseException.class,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ListStudentCommand.MESSAGE_USAGE), ()
                -> parser.parseCommand(ListStudentCommand.COMMAND_WORD + " 3"));
    }

    @Test
    public void parseCommand_sortStudent() throws Exception {
        assertEquals(new SortStudentCommand(), parser.parseCommand(SortStudentCommand.COMMAND_WORD));
        assertThrows(ParseException.class,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, SortStudentCommand.MESSAGE_USAGE), ()
                -> parser.parseCommand(SortStudentCommand.COMMAND_WORD + " reverse"));
    }

    @Test
    public void parseCommand_unrecognisedInput_throwsParseException() {
        assertThrows(ParseException.class, String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE), ()
            -> parser.parseCommand(""));
    }

    @Test
    public void parseCommand_unknownCommand_throwsParseException() {
        assertThrows(ParseException.class, MESSAGE_UNKNOWN_COMMAND, () -> parser.parseCommand("unknownCommand"));
    }
}
