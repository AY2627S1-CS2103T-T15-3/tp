package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STUDENT;

import java.time.LocalDate;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.ViewLessonCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/** Parses input arguments and creates a {@link ViewLessonCommand}. */
public class ViewLessonCommandParser implements Parser<ViewLessonCommand> {
    @Override
    public ViewLessonCommand parse(String args) throws ParseException {
        ArgumentMultimap arguments = ArgumentTokenizer.tokenize(args, PREFIX_DATE, PREFIX_STUDENT);
        arguments.verifyNoDuplicatePrefixesFor(PREFIX_DATE, PREFIX_STUDENT);

        if (arguments.getPreamble().isBlank()
                && arguments.getValue(PREFIX_DATE).isEmpty()
                && arguments.getValue(PREFIX_STUDENT).isEmpty()) {
            return new ViewLessonCommand();
        }
        if (!arguments.getPreamble().isBlank()
                || arguments.getValue(PREFIX_DATE).isPresent()
                        && arguments.getValue(PREFIX_STUDENT).isPresent()) {
            throw invalidFormat();
        }

        if (arguments.getValue(PREFIX_DATE).isPresent()) {
            LocalDate date = DateTimeParser.parseDate(arguments.getValue(PREFIX_DATE).get());
            return new ViewLessonCommand(date);
        }

        try {
            Index index = ParserUtil.parseIndex(arguments.getValue(PREFIX_STUDENT).get());
            return new ViewLessonCommand(index);
        } catch (ParseException exception) {
            throw invalidFormat(exception);
        }
    }

    private ParseException invalidFormat() {
        return new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ViewLessonCommand.MESSAGE_USAGE));
    }

    private ParseException invalidFormat(Exception cause) {
        return new ParseException(
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ViewLessonCommand.MESSAGE_USAGE), cause);
    }
}
