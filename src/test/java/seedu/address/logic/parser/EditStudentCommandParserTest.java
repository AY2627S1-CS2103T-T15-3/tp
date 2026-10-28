package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.EditStudentCommand;
import seedu.address.logic.commands.EditStudentDescriptor;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Phone;
import seedu.address.model.student.Level;
import seedu.address.model.student.Subject;

public class EditStudentCommandParserTest {
    private final EditStudentCommandParser parser = new EditStudentCommandParser();

    @Test
    public void parse_allSupportedFields_success() throws Exception {
        EditStudentCommand command = parser.parse("2 n/Ben Tan p/98765432 e/ben@example.com a/25 Happy Road "
                + "l/JC 2 s/Mathematics s/Physics");

        EditStudentDescriptor expected = new EditStudentDescriptor();
        expected.setName(ParserUtil.parseStudentName("Ben Tan"));
        expected.setPhone(new Phone("98765432"));
        expected.setEmail(ParserUtil.parseStudentEmail("ben@example.com"));
        expected.setAddress(ParserUtil.parseStudentAddress("25 Happy Road"));
        expected.setLevel(new Level("JC 2"));
        expected.setSubjects(java.util.Set.of(new Subject("Mathematics"), new Subject("Physics")));

        assertEquals(new EditStudentCommand(Index.fromOneBased(2), expected), command);
    }

    @Test
    public void parse_noFields_failure() {
        ParseException exception = assertThrows(ParseException.class, () -> parser.parse("2"));

        assertEquals(EditStudentCommand.MESSAGE_NOT_EDITED, exception.getMessage());
    }

    @Test
    public void parse_invalidIndex_failure() {
        assertInvalidFormat("p/91234567");
        assertInvalidFormat("0 p/91234567");
        assertInvalidFormat("-1 p/91234567");
        assertInvalidFormat("1.5 p/91234567");
        assertInvalidFormat("1 hello p/91234567");
    }

    @Test
    public void parse_repeatedSingleValuePrefix_failure() {
        assertDuplicate("1 p/91234567 p/98765432");
        assertDuplicate("1 n/Ben n/Lucy");
        assertDuplicate("1 e/a@example.com e/b@example.com");
        assertDuplicate("1 a/One Road a/Two Road");
        assertDuplicate("1 l/JC 1 l/JC 2");
    }

    @Test
    public void parse_repeatedSubjects_success() throws Exception {
        EditStudentCommand command = parser.parse("1 s/Mathematics s/Physics");

        EditStudentDescriptor expected = new EditStudentDescriptor();
        expected.setSubjects(java.util.Set.of(new Subject("Mathematics"), new Subject("Physics")));
        assertEquals(new EditStudentCommand(Index.fromOneBased(1), expected), command);
    }

    @Test
    public void parse_nameWithRelationshipMarker_success() throws Exception {
        EditStudentCommand command = parser.parse("1 n/John s/o Raj");

        EditStudentDescriptor expected = new EditStudentDescriptor();
        expected.setName(ParserUtil.parseStudentName("John s/o Raj"));
        assertEquals(new EditStudentCommand(Index.fromOneBased(1), expected), command);
    }

    @Test
    public void parse_addressContainingSlash_success() throws Exception {
        EditStudentCommand command = parser.parse("1 a/Block A/B");

        EditStudentDescriptor expected = new EditStudentDescriptor();
        expected.setAddress(ParserUtil.parseStudentAddress("Block A/B"));
        assertEquals(new EditStudentCommand(Index.fromOneBased(1), expected), command);
    }

    @Test
    public void parse_unknownPrefix_failure() {
        ParseException exception = assertThrows(ParseException.class, () -> parser.parse("1 x/Test"));

        assertEquals("Unknown parameter: x/", exception.getMessage());
    }

    @Test
    public void parse_invalidValues_failure() {
        assertThrows(ParseException.class, () -> parser.parse("1 p/invalid"));
        assertThrows(ParseException.class, () -> parser.parse("1 n/"));
        assertThrows(ParseException.class, () -> parser.parse("1 s/"));
        assertThrows(ParseException.class, () -> parser.parse("1 l/"));
    }

    private void assertInvalidFormat(String input) {
        ParseException exception = assertThrows(ParseException.class, () -> parser.parse(input));
        assertEquals("Invalid command format.\n" + EditStudentCommand.MESSAGE_USAGE, exception.getMessage());
    }

    private void assertDuplicate(String input) {
        ParseException exception = assertThrows(ParseException.class, () -> parser.parse(input));
        String prefix = input.substring(input.indexOf('/') - 1, input.indexOf('/') + 1);
        assertEquals("The " + prefix + " parameter must not be specified more than once.", exception.getMessage());
    }
}
