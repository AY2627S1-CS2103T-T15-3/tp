package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.student.Student;

/** Adds a student to TutorTrack. */
public class AddStudentCommand extends Command {
    public static final String COMMAND_WORD = "add-s";
    public static final String MESSAGE_USAGE = "Usage: add-s n/NAME p/PHONE e/EMAIL a/ADDRESS l/LEVEL "
            + "s/SUBJECT [s/SUBJECT]...";
    public static final String MESSAGE_SUCCESS = "New student added: %1$s";
    public static final String MESSAGE_DUPLICATE_STUDENT = "This student already exists in TutorTrack.";

    private final Student toAdd;

    public AddStudentCommand(Student student) {
        toAdd = requireNonNull(student);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        if (model.hasStudent(toAdd)) {
            throw new CommandException(MESSAGE_DUPLICATE_STUDENT);
        }
        model.addStudent(toAdd);
        return new CommandResult(String.format(MESSAGE_SUCCESS, Messages.format(toAdd)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof AddStudentCommand otherCommand)) {
            return false;
        }
        return toAdd.equals(otherCommand.toAdd);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("toAdd", toAdd).toString();
    }
}
