package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.student.Remark;
import seedu.address.model.student.Student;

/** Clears the remark of a student identified by the displayed index. */
public class ClearStudentRemarkCommand extends Command {
    public static final String COMMAND_WORD = "clear-remark-s";
    public static final String MESSAGE_USAGE = "Usage: clear-remark-s INDEX\n"
            + "INDEX must be a positive integer.";
    public static final String MESSAGE_SUCCESS = "Cleared remark for %1$s (%2$s).";
    public static final String MESSAGE_NO_REMARK = "This student has no remark to clear: %1$s (%2$s).";

    private final Index index;

    /**
     * Creates a command to clear the remark of the student at the given displayed index.
     *
     * @param index the index in the currently displayed student list, resolved during execution
     * @throws NullPointerException if {@code index} is null
     */
    public ClearStudentRemarkCommand(Index index) {
        this.index = requireNonNull(index);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Student> students = model.getFilteredStudentList();
        if (students.isEmpty()) {
            throw new CommandException(Messages.MESSAGE_NO_STUDENTS_DISPLAYED);
        }
        if (index.getZeroBased() >= students.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_STUDENT_DISPLAYED_INDEX);
        }
        Student original = students.get(index.getZeroBased());
        if (original.getRemark().isEmpty()) {
            return new CommandResult(String.format(MESSAGE_NO_REMARK,
                    original.getName(), original.getPhone()), original);
        }
        Student updated = original.withRemark(Remark.EMPTY);
        model.setStudent(original, updated);
        return new CommandResult(String.format(MESSAGE_SUCCESS,
                updated.getName(), updated.getPhone()), updated);
    }

    @Override
    public boolean equals(Object other) {
        return other == this || other instanceof ClearStudentRemarkCommand otherCommand
                && index.equals(otherCommand.index);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("index", index).toString();
    }
}
