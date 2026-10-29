package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.guardian.Guardian;

/**
 * Removes a guardian identified using its displayed index from TutorTrack.
 */
public class RemoveGuardianCommand extends Command {

    public static final String COMMAND_WORD = "remove-g";

    public static final String MESSAGE_USAGE = "Usage: " + COMMAND_WORD + " INDEX";

    public static final String MESSAGE_REMOVE_GUARDIAN_SUCCESS = "Removed guardian: %1$s";

    private final Index targetIndex;

    public RemoveGuardianCommand(Index targetIndex) {
        this.targetIndex = requireNonNull(targetIndex);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Guardian> lastShownList = model.getFilteredGuardianList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_GUARDIAN_DISPLAYED_INDEX);
        }

        Guardian guardianToRemove = lastShownList.get(targetIndex.getZeroBased());
        model.deleteGuardian(guardianToRemove);
        return new CommandResult(
                String.format(MESSAGE_REMOVE_GUARDIAN_SUCCESS, Messages.format(guardianToRemove)), true);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof RemoveGuardianCommand otherRemoveGuardianCommand)) {
            return false;
        }

        return targetIndex.equals(otherRemoveGuardianCommand.targetIndex);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndex", targetIndex)
                .toString();
    }
}
