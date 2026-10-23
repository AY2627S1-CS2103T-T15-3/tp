package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.CommandResult.HighlightTarget;
import seedu.address.model.Model;
import seedu.address.model.guardian.GuardianNameContainsKeywordsPredicate;

/**
 * Finds and lists all guardians in the address book whose name contains any of the argument keywords.
 * Keyword matching is case-insensitive.
 */
public class FindGuardianCommand extends Command {
    public static final String COMMAND_WORD = "find-g";

    public static final String MESSAGE_USAGE = "Usage: " + COMMAND_WORD + " KEYWORD [MORE_KEYWORDS]...";

    private final GuardianNameContainsKeywordsPredicate predicate;

    public FindGuardianCommand(GuardianNameContainsKeywordsPredicate predicate) {
        this.predicate = requireNonNull(predicate);
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredGuardianList(predicate);

        int resultCount = model.getFilteredGuardianList().size();
        if (resultCount > 0) {
            return new CommandResult(
                    String.format(Messages.MESSAGE_GUARDIANS_LISTED_OVERVIEW, resultCount),
                    predicate.getKeywords(), HighlightTarget.GUARDIANS);
        } else {
            return new CommandResult(Messages.MESSAGE_NO_GUARDIANS_MATCH_SEARCH,
                    predicate.getKeywords(), HighlightTarget.GUARDIANS);
        }
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof FindGuardianCommand otherCommand)) {
            return false;
        }
        return predicate.equals(otherCommand.predicate);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("predicate", predicate).toString();
    }
}
