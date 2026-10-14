package seedu.address.logic.parser;

import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.LinkGuardianCommand;

public class LinkGuardianCommandParserTest {
    private final LinkGuardianCommandParser parser = new LinkGuardianCommandParser();

    @Test
    public void parse_validArguments_success() {
        assertParseSuccess(parser, "2 g/1",
                new LinkGuardianCommand(Index.fromOneBased(2), Index.fromOneBased(1)));
    }

    @Test
    public void parse_invalidArguments_failure() {
        String message = "Invalid command format.\n" + LinkGuardianCommand.MESSAGE_USAGE;
        assertParseFailure(parser, "", message);
        assertParseFailure(parser, "abc g/1", message);
        assertParseFailure(parser, "2", message);
        assertParseFailure(parser, "2 x/1", message);
        assertParseFailure(parser, "0 g/1", message);
    }
}
