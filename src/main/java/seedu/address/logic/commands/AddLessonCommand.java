package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.lesson.Lesson;
import seedu.address.model.lesson.exceptions.DuplicateLessonException;
import seedu.address.model.student.Student;

/** Adds a weekly series of lessons for a displayed student. */
public class AddLessonCommand extends Command {
    public static final String COMMAND_WORD = "lesson";
    public static final String MESSAGE_USAGE = "Usage: " + COMMAND_WORD
            + " INDEX d/YYYY-MM-DD t/HH:MM l/LENGTH(MINUTES) w/WEEKS";
    public static final String MESSAGE_INVALID_WEEKS = "Number of weeks must be a positive integer.";
    public static final String MESSAGE_SUCCESS = "Added %1$d lesson(s) for %2$s.";

    private final Index targetIndex;
    private final LocalDate startDate;
    private final LocalTime startTime;
    private final Duration duration;
    private final int numberOfWeeks;

    /**
     * Creates a command that adds weekly lessons for the selected student.
     *
     * @param targetIndex displayed index of the student
     * @param startDate date of the first lesson
     * @param startTime start time shared by all lessons
     * @param duration duration of each lesson
     * @param numberOfWeeks number of weekly lessons to add
     */
    public AddLessonCommand(Index targetIndex, LocalDate startDate, LocalTime startTime, Duration duration,
            int numberOfWeeks) {
        this.targetIndex = requireNonNull(targetIndex);
        this.startDate = requireNonNull(startDate);
        this.startTime = requireNonNull(startTime);
        this.duration = requireNonNull(duration);
        if (!duration.isPositive()) {
            throw new IllegalArgumentException(Lesson.MESSAGE_DURATION_CONSTRAINTS);
        }
        if (numberOfWeeks <= 0) {
            throw new IllegalArgumentException(MESSAGE_INVALID_WEEKS);
        }
        this.numberOfWeeks = numberOfWeeks;
    }

    /** Adds the requested weekly lessons to the model. */
    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Student> students = model.getFilteredStudentList();
        if (targetIndex.getZeroBased() >= students.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_STUDENT_DISPLAYED_INDEX);
        }
        Student student = students.get(targetIndex.getZeroBased());
        List<Lesson> lessonsToAdd = new ArrayList<>();
        for (int week = 0; week < numberOfWeeks; week++) {
            lessonsToAdd.add(new Lesson(student, startDate.plusWeeks(week), startTime, duration));
        }
        try {
            for (Lesson lessonToAdd : lessonsToAdd) {
                Lesson existingOverlap = model.getLessonList().stream()
                        .filter(lessonToAdd::overlaps)
                        .findFirst()
                        .orElse(null);
                if (existingOverlap != null) {
                    throw new DuplicateLessonException(
                            "Cannot add lesson because it overlaps with " + formatLesson(existingOverlap) + ".");
                }
                Lesson seriesOverlap = lessonsToAdd.stream()
                        .filter(other -> other != lessonToAdd && lessonToAdd.overlaps(other))
                        .findFirst()
                        .orElse(null);
                if (seriesOverlap != null) {
                    throw new DuplicateLessonException(
                            "Cannot add lesson because it overlaps with another lesson in the series: "
                                    + formatLesson(seriesOverlap) + ".");
                }
            }
            for (Lesson lessonToAdd : lessonsToAdd) {
                model.addLesson(lessonToAdd);
            }
        } catch (DuplicateLessonException exception) {
            throw new CommandException(exception.getMessage(), exception);
        }
        String feedback = String.format(MESSAGE_SUCCESS, numberOfWeeks, student.getName());
        if (duration.compareTo(Lesson.MAX_RECOMMENDED_DURATION) > 0) {
            feedback += " " + Lesson.MESSAGE_LONG_DURATION_WARNING;
        }
        return new CommandResult(feedback, student);
    }

    private String formatLesson(Lesson lesson) {
        return lesson.getStudent().getName() + "'s lesson on " + lesson.getDate()
                + " from " + lesson.getStartDateTime() + " to " + lesson.getEndDateTime();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof AddLessonCommand otherCommand)) {
            return false;
        }
        return targetIndex.equals(otherCommand.targetIndex)
                && startDate.equals(otherCommand.startDate)
                && startTime.equals(otherCommand.startTime)
                && duration.equals(otherCommand.duration)
                && numberOfWeeks == otherCommand.numberOfWeeks;
    }

    @Override
    public int hashCode() {
        return Objects.hash(targetIndex.getZeroBased(), startDate, startTime, duration, numberOfWeeks);
    }
}
