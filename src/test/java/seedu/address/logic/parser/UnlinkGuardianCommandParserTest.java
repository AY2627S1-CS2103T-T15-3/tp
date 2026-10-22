package seedu.address.logic.parser;

import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.UnlinkGuardianCommand;

public class UnlinkGuardianCommandParserTest {
    private final UnlinkGuardianCommandParser parser = new UnlinkGuardianCommandParser();

    @Test
    public void parse_validArguments_success() {
        assertParseSuccess(parser, "2 g/1",
                new UnlinkGuardianCommand(Index.fromOneBased(2), Index.fromOneBased(1)));
    }

    @Test
    public void parse_invalidArguments_failure() {
        String message = "Invalid command format.\n" + UnlinkGuardianCommand.MESSAGE_USAGE;
        assertParseFailure(parser, "", message);
        assertParseFailure(parser, "abc g/1", message);
        assertParseFailure(parser, "2", message);
        assertParseFailure(parser, "0 g/1", message);
        assertParseFailure(parser, "2 g/0", message);
        assertParseFailure(parser, "2 g/1.5", message);
        assertParseFailure(parser, "2 g/", message);
        assertParseFailure(parser, "2 g/1 g/2",
                "The g/ parameter must not be specified more than once.");
        assertParseFailure(parser, "2 x/1", "Unknown parameter: x/");
    }
}
