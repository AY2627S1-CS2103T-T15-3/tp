package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.guardian.Guardian;
import seedu.address.model.link.StudentGuardianLink;
import seedu.address.model.student.Student;

/** Unlinks an existing guardian from an existing student. */
public class UnlinkGuardianCommand extends Command {
    public static final String COMMAND_WORD = "unlink-g";
    public static final String MESSAGE_USAGE = "Usage: unlink-g STUDENT_INDEX g/GUARDIAN_INDEX";
    public static final String MESSAGE_SUCCESS = "Unlinked %1$s (student) from %2$s (guardian).";
    public static final String MESSAGE_LINK_NOT_FOUND = "The student is not linked to the guardian.";
    public static final String MESSAGE_INVALID_STUDENT_INDEX = "The student index provided is invalid.";
    public static final String MESSAGE_INVALID_GUARDIAN_INDEX = "The guardian index provided is invalid.";

    private final Index studentIndex;
    private final Index guardianIndex;

    /** Creates a command unlinking the supplied student and guardian indexes. */
    public UnlinkGuardianCommand(Index studentIndex, Index guardianIndex) {
        this.studentIndex = requireNonNull(studentIndex);
        this.guardianIndex = requireNonNull(guardianIndex);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Student> students = model.getFilteredStudentList();
        if (studentIndex.getZeroBased() >= students.size()) {
            throw new CommandException(MESSAGE_INVALID_STUDENT_INDEX);
        }
        List<Guardian> guardians = model.getFilteredGuardianList();
        if (guardianIndex.getZeroBased() >= guardians.size()) {
            throw new CommandException(MESSAGE_INVALID_GUARDIAN_INDEX);
        }
        Student student = students.get(studentIndex.getZeroBased());
        Guardian guardian = guardians.get(guardianIndex.getZeroBased());
        StudentGuardianLink link = new StudentGuardianLink(student, guardian);
        if (!model.hasStudentGuardianLink(link)) {
            throw new CommandException(MESSAGE_LINK_NOT_FOUND);
        }
        model.removeStudentGuardianLink(link);
        return new CommandResult(String.format(MESSAGE_SUCCESS, student.getName(), guardian.getName()), student);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof UnlinkGuardianCommand otherCommand)) {
            return false;
        }
        return studentIndex.equals(otherCommand.studentIndex) && guardianIndex.equals(otherCommand.guardianIndex);
    }
}
