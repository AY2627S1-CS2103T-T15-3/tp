package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.student.StudentNameContainsKeywordsPredicate;

/**
 * Finds and lists all persons in the address book whose name contains any of the argument keywords.
 * Keyword matching is case-insensitive.
 */
public class FindStudentCommand extends Command {

    public static final String COMMAND_WORD = "find-s";

    public static final String MESSAGE_USAGE = "Usage: " + COMMAND_WORD + " KEYWORD [MORE_KEYWORDS]...";

    private final StudentNameContainsKeywordsPredicate predicate;

    public FindStudentCommand(StudentNameContainsKeywordsPredicate predicate) {
        this.predicate = requireNonNull(predicate);
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredStudentList(predicate);

        int resultCount = model.getFilteredStudentList().size();
        if (resultCount > 0) {
            return new CommandResult(
                    String.format(Messages.MESSAGE_STUDENTS_LISTED_OVERVIEW, resultCount));
        } else {
            return new CommandResult(Messages.MESSAGE_NO_STUDENTS_MATCH_SEARCH);
        }
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof FindStudentCommand otherCommand)) {
            return false;
        }

        return predicate.equals(otherCommand.predicate);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("predicate", predicate).toString();
    }
}
