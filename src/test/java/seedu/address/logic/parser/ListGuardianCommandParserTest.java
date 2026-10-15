package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.ListGuardianCommand;

public class ListGuardianCommandParserTest {

    private static final String MESSAGE_INVALID_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, ListGuardianCommand.MESSAGE_USAGE);

    private ListGuardianCommandParser parser = new ListGuardianCommandParser();

    @Test
    public void parse_emptyArg_success() {
        // no trailing whitespace
        assertParseSuccess(parser, " ", new ListGuardianCommand());

        // multiple trailing whitespaces
        assertParseSuccess(parser, "     ", new ListGuardianCommand());
    }

    @Test
    public void parse_extraArgs_failure() {
        assertParseFailure(parser, "Bob", MESSAGE_INVALID_FORMAT);
    }
}
