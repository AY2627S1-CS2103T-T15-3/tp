package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.Constructor;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import seedu.address.logic.parser.exceptions.ParseException;

public class DateTimeParserTest {
    @Test
    public void parse_validDateTime_returnsDateTime() throws Exception {
        assertEquals(LocalDateTime.of(2026, 11, 1, 14, 30),
                DateTimeParser.parse("2026-11-01", "14:30"));
    }

    @Test
    public void parse_leapDay_returnsDateTime() throws Exception {
        assertEquals(LocalDateTime.of(2028, 2, 29, 0, 0),
                DateTimeParser.parse("2028-02-29", "00:00"));
    }

    @Test
    public void parseDateAndTime_validValues_returnParsedValues() throws Exception {
        assertEquals(java.time.LocalDate.of(2026, 12, 31), DateTimeParser.parseDate("2026-12-31"));
        assertEquals(java.time.LocalTime.of(23, 59), DateTimeParser.parseTime("23:59"));
    }

    @Test
    public void parse_invalidDate_throwsParseException() {
        ParseException exception = assertThrows(ParseException.class, () ->
                DateTimeParser.parse("1111-44-44", "14:30"));
        assertEquals(DateTimeParser.MESSAGE_INVALID_DATE, exception.getMessage());
    }

    @Test
    public void parseDate_invalidFormat_throwsParseException() {
        ParseException exception = assertThrows(ParseException.class, () ->
                DateTimeParser.parseDate("2026/11/01"));
        assertEquals(DateTimeParser.MESSAGE_INVALID_DATE, exception.getMessage());
    }

    @Test
    public void parseDate_impossibleDate_throwsParseException() {
        ParseException exception = assertThrows(ParseException.class, () ->
                DateTimeParser.parseDate("2026-02-29"));
        assertEquals(DateTimeParser.MESSAGE_INVALID_DATE, exception.getMessage());
    }

    @Test
    public void parse_invalidTime_throwsParseException() {
        ParseException exception = assertThrows(ParseException.class, () ->
                DateTimeParser.parse("2026-11-01", "03:67"));
        assertEquals(DateTimeParser.MESSAGE_INVALID_TIME, exception.getMessage());
    }

    @Test
    public void parseTime_invalidFormat_throwsParseException() {
        ParseException exception = assertThrows(ParseException.class, () ->
                DateTimeParser.parseTime("3:07"));
        assertEquals(DateTimeParser.MESSAGE_INVALID_TIME, exception.getMessage());
    }

    @Test
    public void parseTime_impossibleTime_throwsParseException() {
        ParseException exception = assertThrows(ParseException.class, () ->
                DateTimeParser.parseTime("24:00"));
        assertEquals(DateTimeParser.MESSAGE_INVALID_TIME, exception.getMessage());
    }

    @Test
    public void parse_emptyValues_throwsParseException() {
        assertThrows(ParseException.class, () -> DateTimeParser.parse("", ""));
    }

    @Test
    public void constructor_private_canBeInvokedReflectively() throws Exception {
        Constructor<DateTimeParser> constructor = DateTimeParser.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        assertDoesNotThrow(() -> constructor.newInstance());
    }

    @Test
    public void parse_malformedDateAndTime_throwsParseException() {
        ParseException dateException = assertThrows(ParseException.class, () ->
                DateTimeParser.parse("2026/11/01", "14:30"));
        assertEquals(DateTimeParser.MESSAGE_INVALID_DATE, dateException.getMessage());

        ParseException timeException = assertThrows(ParseException.class, () ->
                DateTimeParser.parse("2026-11-01", "2:30 PM"));
        assertEquals(DateTimeParser.MESSAGE_INVALID_TIME, timeException.getMessage());
    }
}
