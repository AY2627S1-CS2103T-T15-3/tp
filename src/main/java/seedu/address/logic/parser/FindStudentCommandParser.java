package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.List;

import seedu.address.logic.commands.FindStudentCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Name;
import seedu.address.model.student.StudentNameContainsKeywordsPredicate;

/**
 * Parses input arguments and creates a new FindStudentCommand object.
 */
public class FindStudentCommandParser implements Parser<FindStudentCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the FindStudentCommand
     * and returns a FindStudentCommand object for execution.
     * @throws ParseException if the user input does not conform to the expected format
     */
    public FindStudentCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindStudentCommand.MESSAGE_USAGE));
        }

        String normalizedArgs = trimmedArgs.replaceAll("\\s+", " ");
        if (!Name.isValidName(normalizedArgs)) {
            throw new ParseException(Name.MESSAGE_CONSTRAINTS);
        }

        return new FindStudentCommand(
                new StudentNameContainsKeywordsPredicate(List.of(normalizedArgs.split(" "))));
    }
}
