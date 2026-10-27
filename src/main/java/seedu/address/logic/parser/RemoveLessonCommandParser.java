package seedu.address.logic.parser;

import seedu.address.logic.commands.RemoveLessonCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/** Parses input arguments and creates a {@link RemoveLessonCommand}. */
public class RemoveLessonCommandParser implements Parser<RemoveLessonCommand> {
    @Override
    public RemoveLessonCommand parse(String args) throws ParseException {
        try {
            return new RemoveLessonCommand(ParserUtil.parseIndex(args));
        } catch (ParseException exception) {
            try {
                if (Integer.parseInt(args.trim()) <= 0) {
                    throw new ParseException(RemoveLessonCommand.MESSAGE_INVALID_INDEX, exception);
                }
            } catch (NumberFormatException ignored) {
                // Non-numeric arguments use the command-format error below.
            }
            throw new ParseException("Invalid command format.\n" + RemoveLessonCommand.MESSAGE_USAGE, exception);
        }
    }
}
