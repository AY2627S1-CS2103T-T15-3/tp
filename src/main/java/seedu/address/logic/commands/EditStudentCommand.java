package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_STUDENTS;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.student.Student;

/** Edits the details of an existing student. */
public class EditStudentCommand extends Command {
    public static final String COMMAND_WORD = "edit-s";
    public static final String MESSAGE_USAGE = "Usage: edit-s INDEX [n/NAME] [p/PHONE] [e/EMAIL] [a/ADDRESS] "
            + "[l/LEVEL] [s/SUBJECT]...";
    public static final String MESSAGE_SUCCESS = "Edited student: %1$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_INVALID_INDEX = "The student index provided is invalid.";
    public static final String MESSAGE_DUPLICATE_STUDENT = "This student already exists in TutorTrack.";

    private final Index index;
    private final EditStudentDescriptor editStudentDescriptor;

    /** Creates a command to edit the student at {@code index}. */
    public EditStudentCommand(Index index, EditStudentDescriptor editStudentDescriptor) {
        this.index = requireNonNull(index);
        this.editStudentDescriptor = new EditStudentDescriptor(requireNonNull(editStudentDescriptor));
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Student> lastShownList = model.getFilteredStudentList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(MESSAGE_INVALID_INDEX);
        }

        Student studentToEdit = lastShownList.get(index.getZeroBased());
        Student editedStudent = editStudentDescriptor.createEditedStudent(studentToEdit);

        if (!studentToEdit.isSameStudent(editedStudent) && model.hasStudent(editedStudent)) {
            throw new CommandException(MESSAGE_DUPLICATE_STUDENT);
        }

        model.setStudent(studentToEdit, editedStudent);
        model.updateFilteredStudentList(PREDICATE_SHOW_ALL_STUDENTS);
        return new CommandResult(String.format(MESSAGE_SUCCESS, Messages.format(editedStudent)), editedStudent);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof EditStudentCommand otherCommand)) {
            return false;
        }
        return index.equals(otherCommand.index)
                && editStudentDescriptor.equals(otherCommand.editStudentDescriptor);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("index", index)
                .add("editStudentDescriptor", editStudentDescriptor)
                .toString();
    }
}
