package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MONTH;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.MarkPaidCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.student.PaymentMonth;

/**
 * Parses input arguments and creates a new MarkPaidCommand object.
 */
public class MarkPaidCommandParser implements Parser<MarkPaidCommand> {
    private static final Pattern PREFIX_PATTERN = Pattern.compile("(?<!\\S)([A-Za-z][A-Za-z0-9-]*)/");

    /**
     * Parses the given {@code String} of arguments in the context of the MarkPaidCommand
     * and returns a MarkPaidCommand object for execution.
     * @throws ParseException if the user input does not conform to the expected format
     */
    @Override
    public MarkPaidCommand parse(String args) throws ParseException {
        Matcher matcher = PREFIX_PATTERN.matcher(args);
        while (matcher.find()) {
            String prefix = matcher.group(1) + "/";
            if (!PREFIX_MONTH.getPrefix().equals(prefix)) {
                throw new ParseException("Unknown parameter: " + prefix);
            }
        }

        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_MONTH);

        if (argMultimap.getValue(PREFIX_MONTH).isEmpty() || argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, MarkPaidCommand.MESSAGE_USAGE));
        }

        Index index;
        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, MarkPaidCommand.MESSAGE_USAGE), pe);
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_MONTH);
        PaymentMonth paymentMonth = ParserUtil.parsePaymentMonth(argMultimap.getValue(PREFIX_MONTH).get());
        return new MarkPaidCommand(index, paymentMonth);
    }
}
