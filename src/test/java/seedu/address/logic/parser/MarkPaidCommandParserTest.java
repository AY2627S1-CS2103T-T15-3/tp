package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MONTH;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.MarkPaidCommand;
import seedu.address.model.student.PaymentMonth;

/**
 * Contains tests for MarkPaidCommandParser.
 */
public class MarkPaidCommandParserTest {

    private static final String MESSAGE_INVALID_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, MarkPaidCommand.MESSAGE_USAGE);

    private final MarkPaidCommandParser parser = new MarkPaidCommandParser();

    @Test
    public void parse_validArgs_returnsMarkPaidCommand() {
        MarkPaidCommand expected = new MarkPaidCommand(INDEX_FIRST_PERSON, new PaymentMonth("2026-10"));
        assertParseSuccess(parser, " 1 m/2026-10", expected);
        assertParseSuccess(parser, "  1   m/  2026-10  ", expected);
    }

    @Test
    public void parse_missingParts_failure() {
        assertParseFailure(parser, "", MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, " 1", MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, " m/2026-10", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidIndex_failure() {
        assertParseFailure(parser, " a m/2026-10", MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, " 0 m/2026-10", MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, " -1 m/2026-10", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidMonth_failure() {
        assertParseFailure(parser, " 1 m/2026-13", PaymentMonth.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, " 1 m/10-2026", PaymentMonth.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, " 1 m/", PaymentMonth.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_duplicateMonth_failure() {
        assertParseFailure(parser, " 1 m/2026-10 m/2026-11",
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_MONTH));
    }
}
