package seedu.address.logic.parser;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;

import seedu.address.logic.parser.exceptions.ParseException;

/** Parses date and time values supplied to commands. */
public final class DateTimeParser {
    public static final String MESSAGE_INVALID_DATE = "Date must be valid and in the format YYYY-MM-DD.";
    public static final String MESSAGE_INVALID_TIME = "Time must be valid and in the format HH:MM.";
    private static final Pattern DATE_PATTERN = Pattern.compile("\\d{4}-\\d{2}-\\d{2}");
    private static final Pattern TIME_PATTERN = Pattern.compile("\\d{2}:\\d{2}");

    private DateTimeParser() {}

    /** Parses an ISO date and time from separate {@code YYYY-MM-DD} and {@code HH:MM} values. */
    public static LocalDateTime parse(String date, String time) throws ParseException {
        return LocalDateTime.of(parseDate(date), parseTime(time));
    }

    /** Parses an ISO date in {@code YYYY-MM-DD} format. */
    public static LocalDate parseDate(String date) throws ParseException {
        try {
            if (!DATE_PATTERN.matcher(date).matches()) {
                throw new DateTimeParseException("Invalid date", date, 0);
            }
            return LocalDate.parse(date);
        } catch (DateTimeParseException exception) {
            throw new ParseException(MESSAGE_INVALID_DATE, exception);
        }
    }

    /** Parses a time in {@code HH:MM} format. */
    public static LocalTime parseTime(String time) throws ParseException {
        try {
            if (!TIME_PATTERN.matcher(time).matches()) {
                throw new DateTimeParseException("Invalid time", time, 0);
            }
            return LocalTime.parse(time);
        } catch (DateTimeParseException exception) {
            throw new ParseException(MESSAGE_INVALID_TIME, exception);
        }
    }
}
