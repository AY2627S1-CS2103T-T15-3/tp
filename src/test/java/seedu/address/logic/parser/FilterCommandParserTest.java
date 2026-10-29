package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_LEVEL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PAID;
import static seedu.address.logic.parser.CliSyntax.PREFIX_UNPAID;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.Assert.assertThrows;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.FilterCommand;
import seedu.address.model.student.Level;
import seedu.address.model.student.PaymentMonth;
import seedu.address.model.student.StudentMatchesFilterPredicate;
import seedu.address.model.student.Subject;

public class FilterCommandParserTest {

    private static final String MESSAGE_INVALID_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE);

    private final FilterCommandParser parser = new FilterCommandParser();

    @Test
    public void parse_nullArgs_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> parser.parse(null));
    }

    @Test
    public void parse_levelOnly_success() {
        assertParseSuccess(parser, " l/JC 1", filterCommand("JC 1"));
    }

    @Test
    public void parse_oneSubjectOnly_success() {
        assertParseSuccess(parser, " s/Mathematics", filterCommand(null, "Mathematics"));
    }

    @Test
    public void parse_multipleSubjectsOnly_success() {
        assertParseSuccess(parser, " s/Mathematics s/Physics", filterCommand(null, "Mathematics", "Physics"));
    }

    @Test
    public void parse_levelAndSubjects_success() {
        FilterCommand expectedCommand = filterCommand("JC 1", "Mathematics", "Physics");

        assertParseSuccess(parser, " l/JC 1 s/Mathematics s/Physics", expectedCommand);

        // prefixes in a different order
        assertParseSuccess(parser, " s/Physics l/JC 1 s/Mathematics", expectedCommand);
    }

    @Test
    public void parse_extraWhitespace_success() {
        assertParseSuccess(parser, " \n l/  JC   1 \t s/  Mathematics  \n", filterCommand("JC 1", "Mathematics"));
    }

    @Test
    public void parse_differentCase_success() {
        assertParseSuccess(parser, " l/jc 1 s/MATHEMATICS", filterCommand("JC 1", "Mathematics"));
    }

    @Test
    public void parse_repeatedSubject_success() {
        assertParseSuccess(parser, " s/Mathematics s/mathematics", filterCommand(null, "Mathematics"));
    }

    @Test
    public void parse_paidOnly_success() {
        assertParseSuccess(parser, " paid/2026-10", paymentCommand("2026-10", null));
    }

    @Test
    public void parse_unpaidOnly_success() {
        assertParseSuccess(parser, " unpaid/2026-10", paymentCommand(null, "2026-10"));
    }

    @Test
    public void parse_allCriteria_success() {
        FilterCommand expectedCommand = new FilterCommand(new StudentMatchesFilterPredicate(
                Optional.of(new Level("JC 1")), Set.of(new Subject("Mathematics")),
                Optional.of(new PaymentMonth("2026-09")), Optional.of(new PaymentMonth("2026-10"))));

        assertParseSuccess(parser, " l/JC 1 s/Mathematics paid/2026-09 unpaid/2026-10", expectedCommand);

        // prefixes in a different order
        assertParseSuccess(parser, " unpaid/2026-10 s/Mathematics paid/2026-09 l/JC 1", expectedCommand);
    }

    @Test
    public void parse_repeatedPaymentMonth_failure() {
        assertParseFailure(parser, " paid/2026-10 paid/2026-11",
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PAID));
        assertParseFailure(parser, " unpaid/2026-10 unpaid/2026-11",
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_UNPAID));
    }

    @Test
    public void parse_invalidPaymentMonth_failure() {
        assertParseFailure(parser, " paid/2026-13", PaymentMonth.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, " unpaid/Oct 2026", PaymentMonth.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, " l/JC 1 unpaid/", PaymentMonth.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_noCriteria_failure() {
        assertParseFailure(parser, "", FilterCommand.MESSAGE_NO_FILTER);
        assertParseFailure(parser, "     ", FilterCommand.MESSAGE_NO_FILTER);
    }

    @Test
    public void parse_nonEmptyPreamble_failure() {
        // preamble without any criteria
        assertParseFailure(parser, " JC 1", MESSAGE_INVALID_FORMAT);

        // preamble before valid criteria
        assertParseFailure(parser, " 1 l/JC 1", MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, " some text s/Mathematics", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_repeatedLevel_failure() {
        String expectedMessage = Messages.getErrorMessageForDuplicatePrefixes(PREFIX_LEVEL);

        assertParseFailure(parser, " l/JC 1 l/JC 2", expectedMessage);
        assertParseFailure(parser, " l/JC 1 s/Mathematics l/JC 1", expectedMessage);
    }

    @Test
    public void parse_invalidLevel_failure() {
        // empty level
        assertParseFailure(parser, " l/", Level.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, " l/ s/Mathematics", Level.MESSAGE_CONSTRAINTS);

        // disallowed characters
        assertParseFailure(parser, " l/JC-1", Level.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_unknownParameter_failure() {
        assertParseFailure(parser, " l/JC 1 x/foo", "Unknown parameter: x/");
        assertParseFailure(parser, " paid/2026-10 m/2026-11", "Unknown parameter: m/");
        assertParseFailure(parser, " x/foo s/Mathematics", "Unknown parameter: x/");
        assertParseFailure(parser, " l/JC 1 n/Henry", "Unknown parameter: n/");
        assertParseFailure(parser, " x/foo", "Unknown parameter: x/");
    }

    @Test
    public void parse_invalidSubject_failure() {
        // empty subject
        assertParseFailure(parser, " s/", Subject.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, " l/JC 1 s/", Subject.MESSAGE_CONSTRAINTS);

        // disallowed characters
        assertParseFailure(parser, " s/Math*", Subject.MESSAGE_CONSTRAINTS);

        // one invalid subject among valid ones
        assertParseFailure(parser, " s/Mathematics s/Phy$ics", Subject.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_invalidLevelAndSubject_reportsLevelFirst() {
        assertParseFailure(parser, " l/JC-1 s/Math*", Level.MESSAGE_CONSTRAINTS);
    }

    private static FilterCommand paymentCommand(String paidMonth, String unpaidMonth) {
        return new FilterCommand(new StudentMatchesFilterPredicate(Optional.empty(), Set.of(),
                Optional.ofNullable(paidMonth).map(PaymentMonth::new),
                Optional.ofNullable(unpaidMonth).map(PaymentMonth::new)));
    }

    private static FilterCommand filterCommand(String level, String... subjects) {
        Set<Subject> subjectSet = new LinkedHashSet<>();
        for (String subject : subjects) {
            subjectSet.add(new Subject(subject));
        }
        return new FilterCommand(
                new StudentMatchesFilterPredicate(Optional.ofNullable(level).map(Level::new), subjectSet));
    }
}
