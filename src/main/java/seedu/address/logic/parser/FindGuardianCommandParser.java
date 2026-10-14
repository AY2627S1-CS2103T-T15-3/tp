package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.List;

import seedu.address.logic.commands.FindGuardianCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.guardian.GuardianNameContainsKeywordsPredicate;

/**
 * Parses input arguments and creates a new FindGuardianCommand object.
 */
public class FindGuardianCommandParser implements Parser<FindGuardianCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the FindGuardianCommand
     * and returns a FindGuardianCommand object for execution.
     * @throws ParseException if the user input does not conform to the expected format
     */
    @Override
    public FindGuardianCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindGuardianCommand.MESSAGE_USAGE));
        }
        return new FindGuardianCommand(
                new GuardianNameContainsKeywordsPredicate(List.of(trimmedArgs.split("\\s+"))));
    }
}
