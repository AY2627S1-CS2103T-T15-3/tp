package seedu.address.logic.parser;

import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.RemoveLessonCommand;

public class RemoveLessonCommandParserTest {
    private final RemoveLessonCommandParser parser = new RemoveLessonCommandParser();

    @Test
    public void parse_validIndex_success() {
        assertParseSuccess(parser, "1", new RemoveLessonCommand(
                seedu.address.commons.core.index.Index.fromOneBased(1)));
    }

    @Test
    public void parse_nonPositiveIndex_returnsUsageMessage() {
        assertParseFailure(parser, "0", "Invalid command format.\n" + RemoveLessonCommand.MESSAGE_USAGE);
        assertParseFailure(parser, "-1", "Invalid command format.\n" + RemoveLessonCommand.MESSAGE_USAGE);
    }

    @Test
    public void parse_malformedInput_returnsUsageMessage() {
        assertParseFailure(parser, "abc", "Invalid command format.\n" + RemoveLessonCommand.MESSAGE_USAGE);
        assertParseFailure(parser, "", "Invalid command format.\n" + RemoveLessonCommand.MESSAGE_USAGE);
        assertParseFailure(parser, "1 extra", "Invalid command format.\n" + RemoveLessonCommand.MESSAGE_USAGE);
        assertParseFailure(parser, "2147483648", "Invalid command format.\n" + RemoveLessonCommand.MESSAGE_USAGE);
        assertParseFailure(parser, "+1", "Invalid command format.\n" + RemoveLessonCommand.MESSAGE_USAGE);
    }
}
