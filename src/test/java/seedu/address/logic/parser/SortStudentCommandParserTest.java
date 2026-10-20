package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.SortStudentCommand;

/** Contains unit tests for {@code SortStudentCommandParser}. */
public class SortStudentCommandParserTest {

    private static final String MESSAGE_INVALID_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, SortStudentCommand.MESSAGE_USAGE);

    private final SortStudentCommandParser parser = new SortStudentCommandParser();

    @Test
    public void parse_noArguments_success() {
        assertParseSuccess(parser, "", new SortStudentCommand());
    }

    @Test
    public void parse_whitespace_success() {
        assertParseSuccess(parser, " \n\t ", new SortStudentCommand());
    }

    @Test
    public void parse_arguments_failure() {
        assertParseFailure(parser, "reverse", MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, "name", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_nullArguments_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> parser.parse(null));
    }
}
