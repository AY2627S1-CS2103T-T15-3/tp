package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_LEVEL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SUBJECT;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.student.StudentMatchesFilterPredicate;

/**
 * Filters the student list to the students who match the given level and subjects.
 * The guardian list is not changed.
 */
public class FilterCommand extends Command {

    public static final String COMMAND_WORD = "filter";

    public static final String MESSAGE_USAGE = "Usage: " + COMMAND_WORD + " "
            + "[" + PREFIX_LEVEL + "LEVEL] "
            + "[" + PREFIX_SUBJECT + "SUBJECT]...";

    private final StudentMatchesFilterPredicate predicate;

    /**
     * Creates a FilterCommand that shows only the students who match {@code predicate}.
     */
    public FilterCommand(StudentMatchesFilterPredicate predicate) {
        requireNonNull(predicate);
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredStudentList(predicate);

        int matchCount = model.getFilteredStudentList().size();
        if (matchCount == 0) {
            return new CommandResult(Messages.MESSAGE_NO_STUDENTS_MATCH_FILTER);
        }
        return new CommandResult(String.format(Messages.MESSAGE_STUDENTS_LISTED_OVERVIEW, matchCount));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof FilterCommand otherFilterCommand)) {
            return false;
        }

        return predicate.equals(otherFilterCommand.predicate);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("predicate", predicate)
                .toString();
    }
}
