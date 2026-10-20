package seedu.address.model.student;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

import seedu.address.commons.util.ToStringBuilder;

/**
 * Represents a lesson scheduled for a student.
 * Guarantees: all fields are present, immutable, and valid.
 */
public class Lesson {

    public static final String MESSAGE_DURATION_CONSTRAINTS =
            "Lesson duration must be positive.";

    private final Student student;
    private final LocalDate date;
    private final LocalTime startTime;
    private final Duration duration;

    /**
     * Creates a {@code Lesson}.
     *
     * @param student the student attending the lesson
     * @param date the date of the lesson
     * @param startTime the starting time of the lesson
     * @param duration the duration of the lesson
     */
    public Lesson(Student student, LocalDate date, LocalTime startTime, Duration duration) {
        requireNonNull(student);
        requireNonNull(date);
        requireNonNull(startTime);
        requireNonNull(duration);
        checkArgument(!duration.isNegative() && !duration.isZero(), MESSAGE_DURATION_CONSTRAINTS);
        this.student = student;
        this.date = date;
        this.startTime = startTime;
        this.duration = duration;
    }

    public Student getStudent() {
        return student;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public Duration getDuration() {
        return duration;
    }

    public LocalDateTime getStartDateTime() {
        return LocalDateTime.of(date, startTime);
    }

    public LocalDateTime getEndDateTime() {
        return getStartDateTime().plus(duration);
    }

    /**
     * Returns true if this lesson overlaps the given lesson.
     * Lessons that end exactly when another starts do not overlap.
     */
    public boolean overlaps(Lesson otherLesson) {
        requireNonNull(otherLesson);
        return getStartDateTime().isBefore(otherLesson.getEndDateTime())
                && otherLesson.getStartDateTime().isBefore(getEndDateTime());
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof Lesson otherLesson)) {
            return false;
        }
        return student.equals(otherLesson.student)
                && date.equals(otherLesson.date)
                && startTime.equals(otherLesson.startTime)
                && duration.equals(otherLesson.duration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(student, date, startTime, duration);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("student", student.getName())
                .add("date", date)
                .add("startTime", startTime)
                .add("duration", duration)
                .toString();
    }
}
