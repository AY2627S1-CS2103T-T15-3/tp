package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.logic.commands.RemoveLessonCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/** Parses input arguments and creates a {@link RemoveLessonCommand}. */
public class RemoveLessonCommandParser implements Parser<RemoveLessonCommand> {
    @Override
    public RemoveLessonCommand parse(String args) throws ParseException {
        try {
            return new RemoveLessonCommand(ParserUtil.parseIndex(args));
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, RemoveLessonCommand.MESSAGE_USAGE), pe);
        }
    }
}
