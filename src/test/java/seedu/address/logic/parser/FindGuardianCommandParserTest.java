package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.FindGuardianCommand;
import seedu.address.model.guardian.GuardianNameContainsKeywordsPredicate;

/** Contains unit tests for {@code FindGuardianCommandParser}. */
public class FindGuardianCommandParserTest {
    private final FindGuardianCommandParser parser = new FindGuardianCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindGuardianCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validArgs_returnsFindGuardianCommand() {
        FindGuardianCommand expected = new FindGuardianCommand(
                new GuardianNameContainsKeywordsPredicate(List.of("Alice", "Bob")));

        // no leading and trailing whitespaces
        assertParseSuccess(parser, "Alice Bob", expected);

        // multiple whitespaces between keywords
        assertParseSuccess(parser, " \n Alice \n \t Bob  \t", expected);
    }
}
