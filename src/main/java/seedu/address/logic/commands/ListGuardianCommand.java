package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_GUARDIANS;

import seedu.address.model.Model;

/** Lists all guardians in the address book to the user. */
public class ListGuardianCommand extends Command {
    public static final String COMMAND_WORD = "list-g";
    public static final String MESSAGE_SUCCESS = "Listed all guardians.";

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredGuardianList(PREDICATE_SHOW_ALL_GUARDIANS);
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
