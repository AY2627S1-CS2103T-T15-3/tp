package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.RemoveGuardianCommand;
import seedu.address.logic.parser.exceptions.ParseException;

public class RemoveGuardianCommandParserTest {

    private final RemoveGuardianCommandParser parser = new RemoveGuardianCommandParser();

    @Test
    public void parse_validArgs_returnsRemoveGuardianCommand() {
        CommandParserTestUtil.assertParseSuccess(parser,
                "1", new RemoveGuardianCommand(INDEX_FIRST_PERSON));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        assertThrows(ParseException.class,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, RemoveGuardianCommand.MESSAGE_USAGE), ()
                        -> parser.parse("a"));
    }
}
