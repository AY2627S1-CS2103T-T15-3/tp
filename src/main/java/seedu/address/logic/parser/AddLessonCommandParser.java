package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_LENGTH;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TIME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_WEEKS;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.AddLessonCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/** Parses the lesson command. */
public class AddLessonCommandParser implements Parser<AddLessonCommand> {
    private static final Pattern PREFIX_PATTERN = Pattern.compile("(?<!\\S)([A-Za-z][A-Za-z0-9-]*)/");
    private static final Set<String> SUPPORTED_PREFIXES = Set.of("d/", "t/", "l/", "w/");

    @Override
    public AddLessonCommand parse(String args) throws ParseException {
        rejectUnknownPrefixes(args);
        ArgumentMultimap arguments = ArgumentTokenizer.tokenize(
                args, PREFIX_DATE, PREFIX_TIME, PREFIX_LENGTH, PREFIX_WEEKS);
        rejectDuplicatePrefix(arguments, PREFIX_DATE);
        rejectDuplicatePrefix(arguments, PREFIX_TIME);
        rejectDuplicatePrefix(arguments, PREFIX_WEEKS);
        rejectDuplicatePrefix(arguments, PREFIX_LENGTH);
        if (arguments.getPreamble().isBlank()
                || arguments.getValue(PREFIX_DATE).isEmpty()
                || arguments.getValue(PREFIX_TIME).isEmpty()
                || arguments.getValue(PREFIX_LENGTH).isEmpty()
                || arguments.getValue(PREFIX_WEEKS).isEmpty()) {
            throw invalidFormat();
        }
        Index index = parseIndex(arguments.getPreamble());
        LocalDateTime dateTime = parseDateTime(arguments.getValue(PREFIX_DATE).get(),
                arguments.getValue(PREFIX_TIME).get());
        int weeks = parseWeeks(arguments.getValue(PREFIX_WEEKS).get());
        Duration duration = parseLength(arguments.getValue(PREFIX_LENGTH).get());
        return new AddLessonCommand(index, dateTime.toLocalDate(), dateTime.toLocalTime(), duration, weeks);
    }

    private void rejectUnknownPrefixes(String args) throws ParseException {
        Matcher matcher = PREFIX_PATTERN.matcher(args);
        while (matcher.find()) {
            String prefix = matcher.group(1) + "/";
            if (!SUPPORTED_PREFIXES.contains(prefix)) {
                throw new ParseException("Unknown parameter: " + prefix);
            }
        }
    }

    private Index parseIndex(String value) throws ParseException {
        try {
            return ParserUtil.parseIndex(value);
        } catch (ParseException exception) {
            throw invalidFormat(exception);
        }
    }

    private void rejectDuplicatePrefix(ArgumentMultimap arguments, Prefix prefix) throws ParseException {
        if (arguments.getAllValues(prefix).size() > 1) {
            throw new ParseException("The " + prefix.getPrefix() + " parameter must not be specified more than once.");
        }
    }

    private LocalDateTime parseDateTime(String date, String time) throws ParseException {
        return DateTimeParser.parse(date, time);
    }

    private int parseWeeks(String value) throws ParseException {
        try {
            int weeks = Integer.parseInt(value);
            if (weeks <= 0 || weeks > AddLessonCommand.MAX_WEEKS) {
                throw new ParseException(AddLessonCommand.MESSAGE_INVALID_WEEKS);
            }
            return weeks;
        } catch (NumberFormatException exception) {
            throw new ParseException(AddLessonCommand.MESSAGE_INVALID_WEEKS, exception);
        }
    }

    private Duration parseLength(String value) throws ParseException {
        try {
            int minutes = Integer.parseInt(value);
            if (minutes <= 0) {
                throw new ParseException("Lesson length must be a positive integer of minutes.");
            }
            return Duration.ofMinutes(minutes);
        } catch (NumberFormatException exception) {
            throw new ParseException("Lesson length must be a positive integer of minutes.", exception);
        }
    }

    private ParseException invalidFormat() {
        return new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddLessonCommand.MESSAGE_USAGE));
    }

    private ParseException invalidFormat(Exception cause) {
        return new ParseException(
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddLessonCommand.MESSAGE_USAGE), cause);
    }
}
