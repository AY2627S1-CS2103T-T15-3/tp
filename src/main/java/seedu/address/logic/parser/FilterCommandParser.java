package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_LEVEL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SUBJECT;

import java.util.Optional;
import java.util.Set;

import seedu.address.logic.commands.FilterCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.student.Level;
import seedu.address.model.student.StudentMatchesFilterPredicate;
import seedu.address.model.student.Subject;

/**
 * Parses input arguments and creates a new FilterCommand object
 */
public class FilterCommandParser implements Parser<FilterCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the FilterCommand
     * and returns a FilterCommand object for execution.
     * @throws ParseException if the user input does not conform to the expected format
     */
    public FilterCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_LEVEL, PREFIX_SUBJECT);

        boolean hasLevel = argMultimap.getValue(PREFIX_LEVEL).isPresent();
        boolean hasSubjects = !argMultimap.getAllValues(PREFIX_SUBJECT).isEmpty();
        if (!argMultimap.getPreamble().isEmpty() || !(hasLevel || hasSubjects)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_LEVEL);

        Optional<Level> level = Optional.empty();
        if (hasLevel) {
            level = Optional.of(ParserUtil.parseLevel(argMultimap.getValue(PREFIX_LEVEL).get()));
        }
        Set<Subject> subjects = ParserUtil.parseSubjects(argMultimap.getAllValues(PREFIX_SUBJECT));

        return new FilterCommand(new StudentMatchesFilterPredicate(level, subjects));
    }

}
