package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.guardian.Guardian;

/** Edits the details of an existing guardian. */
public class EditGuardianCommand extends Command {
    public static final String COMMAND_WORD = "edit-g";
    public static final String MESSAGE_USAGE = "Usage: edit-g INDEX [n/NAME] [p/PHONE] [e/EMAIL] [a/ADDRESS]";
    public static final String MESSAGE_SUCCESS = "Edited guardian: %1$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_INVALID_INDEX = "The guardian index provided is invalid.";
    public static final String MESSAGE_DUPLICATE_GUARDIAN = "This guardian already exists in TutorTrack.";

    private final Index index;
    private final EditGuardianDescriptor editGuardianDescriptor;

    /** Creates a command to edit the guardian at {@code index}. */
    public EditGuardianCommand(Index index, EditGuardianDescriptor editGuardianDescriptor) {
        this.index = requireNonNull(index);
        this.editGuardianDescriptor = new EditGuardianDescriptor(requireNonNull(editGuardianDescriptor));
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Guardian> lastShownList = model.getFilteredGuardianList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(MESSAGE_INVALID_INDEX);
        }

        Guardian guardianToEdit = lastShownList.get(index.getZeroBased());
        Guardian editedGuardian = editGuardianDescriptor.createEditedGuardian(guardianToEdit);

        if (!guardianToEdit.isSameGuardian(editedGuardian) && model.hasGuardian(editedGuardian)) {
            throw new CommandException(MESSAGE_DUPLICATE_GUARDIAN);
        }

        model.setGuardian(guardianToEdit, editedGuardian);
        return new CommandResult(String.format(MESSAGE_SUCCESS, Messages.format(editedGuardian)), editedGuardian);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof EditGuardianCommand otherCommand)) {
            return false;
        }
        return index.equals(otherCommand.index)
                && editGuardianDescriptor.equals(otherCommand.editGuardianDescriptor);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("index", index)
                .add("editGuardianDescriptor", editGuardianDescriptor)
                .toString();
    }
}
