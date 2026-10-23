package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.ClearStudentRemarkCommand;

public class ClearStudentRemarkCommandParserTest {
    private final ClearStudentRemarkCommandParser parser = new ClearStudentRemarkCommandParser();

    @Test
    public void parse_validIndex_returnsCommand() {
        assertParseSuccess(parser, " 1 ", new ClearStudentRemarkCommand(Index.fromOneBased(1)));
    }

    @Test
    public void parse_missingInvalidOrExtraArguments_reportsUsage() {
        for (String args : List.of("", " ", "0", "-1", "+1", "1.5", "a", "2147483648", "1 text", "1 2")) {
            assertParseFailure(parser, args,
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, ClearStudentRemarkCommand.MESSAGE_USAGE));
        }
    }
}
