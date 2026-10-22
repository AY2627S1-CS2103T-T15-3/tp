package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.logic.commands.SortStudentCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/** Parses input arguments and creates a {@link SortStudentCommand}. */
public class SortStudentCommandParser implements Parser<SortStudentCommand> {

    /**
     * Parses the given arguments and returns a sort-student command.
     *
     * @throws ParseException if any arguments are provided
     */
    @Override
    public SortStudentCommand parse(String args) throws ParseException {
        requireNonNull(args);
        if (!args.trim().isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, SortStudentCommand.MESSAGE_USAGE));
        }

        return new SortStudentCommand();
    }
}
