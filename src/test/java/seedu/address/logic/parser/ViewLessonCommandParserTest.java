package seedu.address.logic.parser;

import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.ViewLessonCommand;

public class ViewLessonCommandParserTest {
    private final ViewLessonCommandParser parser = new ViewLessonCommandParser();

    @Test
    public void parse_emptyArgs_success() {
        assertParseSuccess(parser, "", new ViewLessonCommand());
    }

    @Test
    public void parse_date_success() {
        assertParseSuccess(parser, " d/2026-11-01", new ViewLessonCommand(LocalDate.of(2026, 11, 1)));
    }

    @Test
    public void parse_studentIndex_success() {
        assertParseSuccess(parser, " s/2", new ViewLessonCommand(Index.fromOneBased(2)));
    }

    @Test
    public void parse_invalidArguments_returnsUsageMessage() {
        String usage = String.format(seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT,
                ViewLessonCommand.MESSAGE_USAGE);
        assertParseFailure(parser, " d/2026-02-30", DateTimeParser.MESSAGE_INVALID_DATE);
        assertParseFailure(parser, " s/0", usage);
        assertParseFailure(parser, " d/2026-11-01 s/1", usage);
        assertParseFailure(parser, "2026-11-01", usage);
        assertParseFailure(parser, " x/anything", usage);
    }

    @Test
    public void parse_duplicatePrefix_returnsDuplicateMessage() {
        assertParseFailure(parser, " d/2026-11-01 d/2026-11-08",
                seedu.address.logic.Messages.MESSAGE_DUPLICATE_FIELDS + "d/");
    }
}
