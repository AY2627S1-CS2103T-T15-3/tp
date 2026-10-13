package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.guardian.Guardian;

/** Adds a guardian to TutorTrack. */
public class AddGuardianCommand extends Command {
    public static final String COMMAND_WORD = "add-g";
    public static final String MESSAGE_USAGE = "Usage: add-g n/NAME p/PHONE e/EMAIL a/ADDRESS";
    public static final String MESSAGE_SUCCESS = "New guardian added: %1$s";
    public static final String MESSAGE_DUPLICATE_GUARDIAN = "This guardian already exists in TutorTrack.";

    private final Guardian toAdd;

    public AddGuardianCommand(Guardian guardian) {
        toAdd = requireNonNull(guardian);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        if (model.hasGuardian(toAdd)) {
            throw new CommandException(MESSAGE_DUPLICATE_GUARDIAN);
        }
        model.addGuardian(toAdd);
        return new CommandResult(String.format(MESSAGE_SUCCESS, Messages.format(toAdd)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof AddGuardianCommand otherCommand)) {
            return false;
        }
        return toAdd.equals(otherCommand.toAdd);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("toAdd", toAdd).toString();
    }
}
