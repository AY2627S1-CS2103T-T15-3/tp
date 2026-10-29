package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.time.LocalDate;
import java.util.Objects;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.lesson.LessonMatchesDatePredicate;
import seedu.address.model.lesson.LessonMatchesStudentPredicate;
import seedu.address.model.student.Student;

/** Filters the displayed lessons by date, student, or shows all lessons. */
public class ViewLessonCommand extends Command {
    public static final String COMMAND_WORD = "view-l";
    public static final String MESSAGE_USAGE = "Usage: " + COMMAND_WORD
            + " [d/DATE(YYYY-MM-DD) | stu/INDEX]";
    public static final String MESSAGE_SHOW_ALL = "Showing all lessons.";
    public static final String MESSAGE_SHOW_DATE = "Showing lessons on %1$s.";
    public static final String MESSAGE_SHOW_STUDENT = "Showing lessons for %1$s.";

    private final LocalDate date;
    private final Index studentIndex;

    /** Creates a command that shows all lessons. */
    public ViewLessonCommand() {
        date = null;
        studentIndex = null;
    }

    /** Creates a command that shows lessons on {@code date}. */
    public ViewLessonCommand(LocalDate date) {
        this.date = requireNonNull(date);
        studentIndex = null;
    }

    /** Creates a command that shows lessons for the displayed student at {@code studentIndex}. */
    public ViewLessonCommand(Index studentIndex) {
        date = null;
        this.studentIndex = requireNonNull(studentIndex);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        if (date != null) {
            model.updateFilteredLessonList(new LessonMatchesDatePredicate(date));
            return new CommandResult(String.format(MESSAGE_SHOW_DATE, date));
        }
        if (studentIndex != null) {
            Student student = getTargetStudent(model);
            model.updateFilteredLessonList(new LessonMatchesStudentPredicate(student));
            return new CommandResult(String.format(MESSAGE_SHOW_STUDENT, student.getName()));
        }
        model.updateFilteredLessonList(Model.PREDICATE_SHOW_ALL_LESSONS);
        return new CommandResult(MESSAGE_SHOW_ALL);
    }

    private Student getTargetStudent(Model model) throws CommandException {
        if (studentIndex.getZeroBased() >= model.getFilteredStudentList().size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_STUDENT_DISPLAYED_INDEX);
        }
        return model.getFilteredStudentList().get(studentIndex.getZeroBased());
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof ViewLessonCommand otherCommand)) {
            return false;
        }
        return Objects.equals(date, otherCommand.date)
                && Objects.equals(studentIndex, otherCommand.studentIndex);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, studentIndex);
    }
}
