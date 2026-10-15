package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.logic.commands.ListGuardianCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new FindCommand object
 */
public class ListGuardianCommandParser implements Parser<ListGuardianCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the ListGuardianCommand
     * and returns a ListGuardianCommand object for execution.
     *
     * @throws ParseException if the user input does not conform to the expected format
     */
    public ListGuardianCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        if (!trimmedArgs.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, ListGuardianCommand.MESSAGE_USAGE));
        }

        return new ListGuardianCommand();
    }

}
