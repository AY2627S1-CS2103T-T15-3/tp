package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.student.Remark;
import seedu.address.model.student.Student;

/** Sets or replaces the remark of a student identified by the displayed index. */
public class SetStudentRemarkCommand extends Command {
    public static final String COMMAND_WORD = "set-remark-s";
    public static final String MESSAGE_USAGE = "Usage: set-remark-s INDEX REMARK\n"
            + "INDEX must be a positive integer.";
    public static final String MESSAGE_REMARK_REQUIRED = "A nonblank remark must be provided.";
    public static final String MESSAGE_SUCCESS = "Set remark for %1$s (%2$s): %3$s";

    private final Index index;
    private final Remark remark;

    /** Creates a command with a nonempty validated remark. */
    public SetStudentRemarkCommand(Index index, Remark remark) {
        this.index = requireNonNull(index);
        this.remark = requireNonNull(remark);
        checkArgument(!remark.isEmpty(), MESSAGE_REMARK_REQUIRED);
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
        Student updated = original.withRemark(remark);
        if (!original.equals(updated)) {
            model.setStudent(original, updated);
        }
        return new CommandResult(String.format(MESSAGE_SUCCESS,
                updated.getName(), updated.getPhone(), remark), updated);
    }

    @Override
    public boolean equals(Object other) {
        return other == this || other instanceof SetStudentRemarkCommand otherCommand
                && index.equals(otherCommand.index) && remark.equals(otherCommand.remark);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("index", index).add("remark", remark).toString();
    }
}
