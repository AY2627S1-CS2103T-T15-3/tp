package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.model.Model;

/** Sorts the displayed student list by name. */
public class SortStudentCommand extends Command {

    public static final String COMMAND_WORD = "sort-s";
    public static final String MESSAGE_USAGE = "Usage: " + COMMAND_WORD;
    public static final String MESSAGE_SUCCESS = "Students sorted by name.";

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.sortStudentList();
        return new CommandResult(MESSAGE_SUCCESS);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        return other instanceof SortStudentCommand;
    }
}
