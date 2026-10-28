package seedu.address.logic.parser;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.RemoveLessonCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/** Parses input arguments and creates a {@link RemoveLessonCommand}. */
public class RemoveLessonCommandParser implements Parser<RemoveLessonCommand> {
    @Override
    public RemoveLessonCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        try {
            return new RemoveLessonCommand(ParserUtil.parseIndex(args));
        } catch (ParseException exception) {
            try {
                if (Integer.parseInt(trimmedArgs) <= 0) {
                    throw new ParseException(Messages.MESSAGE_INVALID_LESSON_DISPLAYED_INDEX, exception);
                }
            } catch (NumberFormatException ignored) {
                // Non-numeric arguments use the command-format error below.
            } catch (ParseException invalidLessonIndex) {
                throw invalidLessonIndex;
            }
            throw new ParseException("Invalid command format.\n" + RemoveLessonCommand.MESSAGE_USAGE, exception);
        }
    }
}
