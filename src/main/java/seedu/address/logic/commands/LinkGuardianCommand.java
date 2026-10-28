package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.guardian.Guardian;
import seedu.address.model.link.StudentGuardianLink;
import seedu.address.model.student.Student;

/** Links an existing guardian to an existing student. */
public class LinkGuardianCommand extends Command {
    public static final String COMMAND_WORD = "link-g";
    public static final String MESSAGE_USAGE = "Usage: link-g STUDENT_INDEX g/GUARDIAN_INDEX";
    public static final String MESSAGE_SUCCESS = "Linked %1$s (student) to %2$s (guardian).";
    public static final String MESSAGE_ALREADY_LINKED =
            "Student %1$s is already linked to guardian %2$s.";
    public static final String MESSAGE_INVALID_STUDENT_INDEX = "The student index provided is invalid.";
    public static final String MESSAGE_INVALID_GUARDIAN_INDEX = "The guardian index provided is invalid.";

    private final Index studentIndex;
    private final Index guardianIndex;

    /** Creates a command linking the supplied student and guardian indexes. */
    public LinkGuardianCommand(Index studentIndex, Index guardianIndex) {
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
        if (model.hasStudentGuardianLink(link)) {
            throw new CommandException(String.format(MESSAGE_ALREADY_LINKED,
                    student.getName(), guardian.getName()));
        }
        model.addStudentGuardianLink(link);
        return new CommandResult(String.format(MESSAGE_SUCCESS, student.getName(), guardian.getName()), student);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof LinkGuardianCommand otherCommand)) {
            return false;
        }
        return studentIndex.equals(otherCommand.studentIndex) && guardianIndex.equals(otherCommand.guardianIndex);
    }
}
