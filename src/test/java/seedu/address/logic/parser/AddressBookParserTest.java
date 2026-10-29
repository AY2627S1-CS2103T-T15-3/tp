package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.AddGuardianCommand;
import seedu.address.logic.commands.AddLessonCommand;
import seedu.address.logic.commands.AddStudentCommand;
import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.commands.ClearStudentRemarkCommand;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.EditGuardianCommand;
import seedu.address.logic.commands.EditStudentCommand;
import seedu.address.logic.commands.ExitCommand;
import seedu.address.logic.commands.FilterCommand;
import seedu.address.logic.commands.FindGuardianCommand;
import seedu.address.logic.commands.FindStudentCommand;
import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.commands.LinkGuardianCommand;
import seedu.address.logic.commands.ListGuardianCommand;
import seedu.address.logic.commands.ListStudentCommand;
import seedu.address.logic.commands.MarkPaidCommand;
import seedu.address.logic.commands.RemoveGuardianCommand;
import seedu.address.logic.commands.RemoveLessonCommand;
import seedu.address.logic.commands.RemoveStudentCommand;
import seedu.address.logic.commands.SetStudentRemarkCommand;
import seedu.address.logic.commands.SortStudentCommand;
import seedu.address.logic.commands.UnlinkGuardianCommand;
import seedu.address.logic.commands.ViewLessonCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.guardian.GuardianNameContainsKeywordsPredicate;
import seedu.address.model.student.Level;
import seedu.address.model.student.PaymentMonth;
import seedu.address.model.student.Remark;
import seedu.address.model.student.StudentMatchesFilterPredicate;
import seedu.address.model.student.StudentNameContainsKeywordsPredicate;
import seedu.address.model.student.Subject;

public class AddressBookParserTest {

    private final AddressBookParser parser = new AddressBookParser();

    @Test
    public void parseCommand_lesson_returnsAddLessonCommand() throws Exception {
        Command command = new AddressBookParser().parseCommand(
                "add-l 1 d/2026-11-01 t/14:00 len/60 w/4");

        assertTrue(command instanceof AddLessonCommand);
    }

    @Test
    public void parseCommand_studentRemarks_routesBothCommands() throws Exception {
        assertEquals(new SetStudentRemarkCommand(INDEX_FIRST_PERSON, new Remark("Practice  fractions")),
                parser.parseCommand("  set-remark-s 1  Practice  fractions  "));
        assertEquals(new ClearStudentRemarkCommand(INDEX_FIRST_PERSON), parser.parseCommand("clear-remark-s 1"));
    }

    @Test
    public void parseCommand_studentRemarkTrailingWhitespace_isNormalized() throws Exception {
        SetStudentRemarkCommand expected = new SetStudentRemarkCommand(INDEX_FIRST_PERSON,
                new Remark("Practice  fractions"));
        for (String whitespace : List.of("  ", "\t", "\n", "\r\n", " \t\n")) {
            assertEquals(expected, parser.parseCommand("set-remark-s 1 Practice  fractions" + whitespace));
        }
    }

    @Test
    public void parseCommand_studentRemarks_rejectsInvalidArguments() {
        assertThrows(ParseException.class, SetStudentRemarkCommand.MESSAGE_REMARK_REQUIRED, () ->
                parser.parseCommand("set-remark-s 1 "));
        assertThrows(ParseException.class,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ClearStudentRemarkCommand.MESSAGE_USAGE), () ->
                        parser.parseCommand("clear-remark-s 1 unexpected"));
    }

    @Test
    public void parseCommand_studentRemarkIndexControl_reportsSetUsage() {
        assertThrows(ParseException.class,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, SetStudentRemarkCommand.MESSAGE_USAGE), () ->
                        parser.parseCommand("set-remark-s 1\u0000 Practice fractions"));
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
    public void parseCommand_viewLesson() throws Exception {
        assertTrue(parser.parseCommand("view-l d/2026-11-01") instanceof ViewLessonCommand);
        assertTrue(parser.parseCommand("view-l stu/1") instanceof ViewLessonCommand);
        assertTrue(parser.parseCommand("view-l") instanceof ViewLessonCommand);
    }

    @Test
    public void parseCommand_supportedCommandWordCaseVariants_areAccepted() {
        Map<String, String> commands = Map.ofEntries(
                Map.entry("add-s", " n/Henry Tan p/91234567 e/henry@example.com a/12 Road l/JC 1 s/Math"),
                Map.entry("add-g", " n/Ben Tan p/92345678 e/ben@example.com a/12 Road"),
                Map.entry("edit-s", " 1 n/Alice Tan"),
                Map.entry("edit-g", " 1 n/Alice Tan"),
                Map.entry("find-s", " Alice"),
                Map.entry("find-g", " Alice"),
                Map.entry("filter", " l/JC 1"),
                Map.entry("list-s", ""),
                Map.entry("list-g", ""),
                Map.entry("sort-s", ""),
                Map.entry("remove-s", " 1"),
                Map.entry("remove-g", " 1"),
                Map.entry("link-g", " 1 g/1"),
                Map.entry("unlink-g", " 1 g/1"),
                Map.entry("mark-paid", " 1 m/2026-10"),
                Map.entry("set-remark-s", " 1 A remark"),
                Map.entry("clear-remark-s", " 1"),
                Map.entry("clear", ""),
                Map.entry("exit", ""),
                Map.entry("help", ""));

        commands.forEach((commandWord, arguments) ->
                acceptedCommandWordVariants(commandWord).forEach(variant ->
                        assertDoesNotThrow(() -> parser.parseCommand(variant + arguments))));
    }

    @Test
    public void parseCommand_unsupportedMixedCaseCommandWord_isRejected() {
        assertThrows(ParseException.class, MESSAGE_UNKNOWN_COMMAND, () -> parser.parseCommand("lIst-S"));
    }

    private List<String> acceptedCommandWordVariants(String commandWord) {
        if (!commandWord.contains("-")) {
            return List.of(commandWord, Character.toUpperCase(commandWord.charAt(0)) + commandWord.substring(1),
                    commandWord.toUpperCase(Locale.ROOT));
        }
        int lastHyphen = commandWord.lastIndexOf('-');
        String lastToken = commandWord.substring(lastHyphen + 1);
        String capitalizedFirst = Character.toUpperCase(commandWord.charAt(0)) + commandWord.substring(1);
        String capitalizedLast = commandWord.substring(0, lastHyphen + 1)
                + Character.toUpperCase(lastToken.charAt(0)) + lastToken.substring(1);
        return List.of(commandWord, capitalizedFirst, capitalizedLast, commandWord.toUpperCase(Locale.ROOT));
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
    public void parseCommand_unlinkGuardian() throws Exception {
        Command command = parser.parseCommand("unlink-g 1 g/2");
        assertTrue(command instanceof UnlinkGuardianCommand);
    }

    @Test
    public void parseCommand_clear() throws Exception {
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD) instanceof ClearCommand);
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD + " 3") instanceof ClearCommand);
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
    public void parseCommand_removeLesson() throws Exception {
        RemoveLessonCommand command = (RemoveLessonCommand) parser.parseCommand("remove-l 1");
        assertEquals(new RemoveLessonCommand(INDEX_FIRST_PERSON), command);
    }

    @Test
    public void parseCommand_removeLesson_invalidArguments() {
        assertThrows(ParseException.class, seedu.address.logic.Messages.MESSAGE_INVALID_LESSON_DISPLAYED_INDEX, () ->
                parser.parseCommand("remove-l 0"));
        assertThrows(ParseException.class, "Invalid command format.\n"
                + RemoveLessonCommand.MESSAGE_USAGE, () -> parser.parseCommand("remove-l abc"));
    }

    @Test
    public void parseCommand_markPaid() throws Exception {
        MarkPaidCommand command = (MarkPaidCommand) parser.parseCommand(
                MarkPaidCommand.COMMAND_WORD + " " + INDEX_FIRST_PERSON.getOneBased() + " m/2026-10");
        assertEquals(new MarkPaidCommand(INDEX_FIRST_PERSON, new PaymentMonth("2026-10")), command);
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

    @Test
    public void parseCommand_legacyCommands_areUnavailable() {
        for (String command : List.of("add", "edit", "delete", "find", "list")) {
            assertThrows(ParseException.class, MESSAGE_UNKNOWN_COMMAND, () -> parser.parseCommand(command));
        }
    }
}
