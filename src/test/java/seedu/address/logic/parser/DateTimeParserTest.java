package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
    public void parse_invalidDate_throwsParseException() {
        ParseException exception = assertThrows(ParseException.class, () ->
                DateTimeParser.parse("1111-44-44", "14:30"));
        assertEquals(DateTimeParser.MESSAGE_INVALID_DATE, exception.getMessage());
    }

    @Test
    public void parse_invalidTime_throwsParseException() {
        ParseException exception = assertThrows(ParseException.class, () ->
                DateTimeParser.parse("2026-11-01", "03:67"));
        assertEquals(DateTimeParser.MESSAGE_INVALID_TIME, exception.getMessage());
    }

    @Test
    public void parse_emptyValues_throwsParseException() {
        assertThrows(ParseException.class, () -> DateTimeParser.parse("", ""));
    }
}
