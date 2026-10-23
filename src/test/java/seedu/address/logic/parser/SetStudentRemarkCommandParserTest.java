package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.SetStudentRemarkCommand;
import seedu.address.model.student.Remark;

public class SetStudentRemarkCommandParserTest {
    private final SetStudentRemarkCommandParser parser = new SetStudentRemarkCommandParser();

    @Test
    public void parse_validText_preservesLiteralContentAndNormalizesEdges() {
        for (String text : List.of("Practice fractions", "Practice  fractions", "Review s/Physics n/questions", "2",
                "a".repeat(299), "a".repeat(300), "\ud83d\ude00".repeat(299), "\ud83d\ude00".repeat(300),
                "学\ud83d\ude00".repeat(150))) {
            assertParseSuccess(parser, " 1  " + text + "  ",
                    new SetStudentRemarkCommand(Index.fromOneBased(1), new Remark(text)));
        }
        assertParseSuccess(parser, "1 \u2003Practice fractions\u2003",
                new SetStudentRemarkCommand(Index.fromOneBased(1), new Remark("Practice fractions")));
    }

    @Test
    public void parse_missingRemark_reportsRequiredRemark() {
        for (String args : List.of("1", "1 ")) {
            assertParseFailure(parser, args, SetStudentRemarkCommand.MESSAGE_REMARK_REQUIRED);
        }
    }

    @Test
    public void parse_invalidRemark_reportsValueObjectConstraints() {
        for (String text : List.of(" ", "\u2003", "\u00a0", "a".repeat(301), "\ud83d\ude00".repeat(301),
                "\ttext", "text\t", "text\n", "text\rtext", "text\u0000", "text\u0085",
                "text\u2028", "text\u2029")) {
            assertParseFailure(parser, "1 " + text, Remark.MESSAGE_CONSTRAINTS);
        }
    }

    @Test
    public void parse_indexContainsControls_reportsUsageBeforeTrimming() {
        for (String token : List.of("1\u0000", "\u00001", "1\u001f", "1\u007f", "1\u0085", "1\u00002")) {
            assertParseFailure(parser, token + " Practice fractions",
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, SetStudentRemarkCommand.MESSAGE_USAGE));
        }
    }

    @Test
    public void parse_singleTabSeparator_acceptsButAdditionalTabBelongsToRemark() {
        assertParseSuccess(parser, "1\tPractice fractions",
                new SetStudentRemarkCommand(Index.fromOneBased(1), new Remark("Practice fractions")));
        assertParseFailure(parser, "1\t\tPractice fractions", Remark.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_invalidIndex_reportsUsage() {
        for (String args : List.of("", " ", "text", "0 text", "-1 text", "+1 text", "1.5 text", "a text",
                "2147483648 text", "999999999999999999999 text")) {
            assertParseFailure(parser, args,
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, SetStudentRemarkCommand.MESSAGE_USAGE));
        }
    }
}
