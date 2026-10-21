package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_LEVEL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PAID;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SUBJECT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_UNPAID;

import java.util.Optional;
import java.util.Set;

import seedu.address.logic.commands.FilterCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.student.Level;
import seedu.address.model.student.PaymentMonth;
import seedu.address.model.student.StudentMatchesFilterPredicate;
import seedu.address.model.student.Subject;

/**
 * Parses input arguments and creates a new FilterCommand object
 */
public class FilterCommandParser implements Parser<FilterCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the FilterCommand
     * and returns a FilterCommand object for execution.
     * @throws ParseException if the user input does not conform to the expected format
     */
    public FilterCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_LEVEL, PREFIX_SUBJECT,
                PREFIX_PAID, PREFIX_UNPAID);

        boolean hasLevel = argMultimap.getValue(PREFIX_LEVEL).isPresent();
        boolean hasSubjects = !argMultimap.getAllValues(PREFIX_SUBJECT).isEmpty();
        boolean hasPaid = argMultimap.getValue(PREFIX_PAID).isPresent();
        boolean hasUnpaid = argMultimap.getValue(PREFIX_UNPAID).isPresent();
        if (!argMultimap.getPreamble().isEmpty() || !(hasLevel || hasSubjects || hasPaid || hasUnpaid)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_LEVEL, PREFIX_PAID, PREFIX_UNPAID);

        Optional<Level> level = Optional.empty();
        if (hasLevel) {
            level = Optional.of(ParserUtil.parseLevel(argMultimap.getValue(PREFIX_LEVEL).get()));
        }
        Set<Subject> subjects = ParserUtil.parseSubjects(argMultimap.getAllValues(PREFIX_SUBJECT));
        Optional<PaymentMonth> paidMonth = parseOptionalPaymentMonth(argMultimap, PREFIX_PAID);
        Optional<PaymentMonth> unpaidMonth = parseOptionalPaymentMonth(argMultimap, PREFIX_UNPAID);

        return new FilterCommand(new StudentMatchesFilterPredicate(level, subjects, paidMonth, unpaidMonth));
    }

    private static Optional<PaymentMonth> parseOptionalPaymentMonth(ArgumentMultimap argMultimap, Prefix prefix)
            throws ParseException {
        Optional<String> value = argMultimap.getValue(prefix);
        if (value.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(ParserUtil.parsePaymentMonth(value.get()));
    }

}
