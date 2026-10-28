package seedu.address.logic.parser;

import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.time.Duration;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.AddLessonCommand;

public class AddLessonCommandParserTest {
    private final AddLessonCommandParser parser = new AddLessonCommandParser();

    @Test
    public void parse_invalidWeeks_returnsDedicatedMessage() {
        // Boundary: zero is the greatest invalid value; negative values are the invalid equivalence class.
        assertParseFailure(parser, "1 d/2026-11-01 t/14:00 l/60 w/0", AddLessonCommand.MESSAGE_INVALID_WEEKS);
        assertParseFailure(parser, "1 d/2026-11-01 t/14:00 l/60 w/-2", AddLessonCommand.MESSAGE_INVALID_WEEKS);
        assertParseFailure(parser, "1 d/2026-11-01 t/14:00 l/60 w/two", AddLessonCommand.MESSAGE_INVALID_WEEKS);
        assertParseFailure(parser, "1 d/2026-11-01 t/14:00 l/60 w/53", AddLessonCommand.MESSAGE_INVALID_WEEKS);
    }

    @Test
    public void parse_missingArguments_failure() {
        String message = String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, AddLessonCommand.MESSAGE_USAGE);
        assertParseFailure(parser, "1 d/2026-11-01 t/14:00", message);
        assertParseFailure(parser, "1 t/14:00 l/60 w/2", message);
        assertParseFailure(parser, "1 d/2026-11-01 l/60 w/2", message);
        assertParseFailure(parser, "1 d/2026-11-01 t/14:00 w/2", message);
        assertParseFailure(parser, "1 d/2026-11-01 t/14:00 l/60", message);
        assertParseFailure(parser, "1 2026-11-01 t/14:00 l/60 w/2", message);
    }

    @Test
    public void parse_invalidIndex_failure() {
        String message = String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, AddLessonCommand.MESSAGE_USAGE);
        assertParseFailure(parser, "a d/2026-11-01 t/14:00 l/60 w/2", message);
    }

    @Test
    public void parse_invalidDate_failure() {
        assertParseFailure(parser, "1 d/not-a-date t/14:00 l/60 w/2",
                DateTimeParser.MESSAGE_INVALID_DATE);
    }

    @Test
    public void parse_invalidTime_failure() {
        assertParseFailure(parser, "1 d/2026-11-01 t/25:00 l/60 w/2",
                DateTimeParser.MESSAGE_INVALID_TIME);
    }

    @Test
    public void parse_duplicatePrefix_failure() {
        assertParseFailure(parser, "1 d/2026-11-01 d/2026-11-02 t/14:00 l/60 w/2",
                "The d/ parameter must not be specified more than once.");
    }

    @Test
    public void parse_duplicateTimeLengthAndWeeksPrefix_failure() {
        assertParseFailure(parser, "1 d/2026-11-01 t/14:00 t/15:00 l/60 w/2",
                "The t/ parameter must not be specified more than once.");
        assertParseFailure(parser, "1 d/2026-11-01 t/14:00 l/60 l/90 w/2",
                "The l/ parameter must not be specified more than once.");
        assertParseFailure(parser, "1 d/2026-11-01 t/14:00 l/60 w/2 w/3",
                "The w/ parameter must not be specified more than once.");
    }

    @Test
    public void parse_unknownPrefix_failure() {
        assertParseFailure(parser, "1 d/2026-11-01 t/14:00 l/60 w/2 x/value",
                "Unknown parameter: x/");
    }

    @Test
    public void parse_invalidLessonLength_failure() {
        assertParseFailure(parser, "1 d/2026-11-01 t/14:00 l/0 w/2",
                "Lesson length must be a positive integer of minutes.");
        assertParseFailure(parser, "1 d/2026-11-01 t/14:00 l/-1 w/2",
                "Lesson length must be a positive integer of minutes.");
        assertParseFailure(parser, "1 d/2026-11-01 t/14:00 l/sixty w/2",
                "Lesson length must be a positive integer of minutes.");
        assertParseFailure(parser, "1 d/2026-11-01 t/14:00 l/2147483648 w/2",
                "Lesson length must be a positive integer of minutes.");
    }

    @Test
    public void parse_validArgs_success() {
        assertParseSuccess(parser, "1 d/2026-11-01 t/14:00 l/60 w/2",
                new AddLessonCommand(seedu.address.commons.core.index.Index.fromOneBased(1),
                        java.time.LocalDate.of(2026, 11, 1), java.time.LocalTime.of(14, 0), Duration.ofMinutes(60), 2));
    }
}
