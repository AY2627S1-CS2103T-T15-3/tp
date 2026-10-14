package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.RemoveStudentCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new RemoveStudentCommand object
 */
public class RemoveStudentCommandParser implements Parser<RemoveStudentCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the RemoveStudentCommand
     * and returns a RemoveStudentCommand object for execution.
     * @throws ParseException if the user input does not conform to the expected format
     */
    public RemoveStudentCommand parse(String args) throws ParseException {
        try {
            Index index = ParserUtil.parseIndex(args);
            return new RemoveStudentCommand(index);
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, RemoveStudentCommand.MESSAGE_USAGE), pe);
        }
    }

}
