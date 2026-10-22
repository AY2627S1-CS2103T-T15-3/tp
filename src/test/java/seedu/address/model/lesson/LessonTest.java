package seedu.address.model.lesson;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.model.student.Level;
import seedu.address.model.student.Student;
import seedu.address.model.student.Subject;

public class LessonTest {

    private static final Student STUDENT = new Student(
            new Name("Henry Tan"),
            new Phone("91234567"),
            new Email("henry@example.com"),
            new Address("12 ABCD Ave 3"),
            new Level("Secondary 3"),
            Set.of(new Subject("Mathematics")));
    private static final LocalDate DATE = LocalDate.of(2026, 10, 25);
    private static final LocalTime START_TIME = LocalTime.of(14, 0);
    private static final Duration DURATION = Duration.ofMinutes(60);

    @Test
    public void constructor_nullField_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Lesson(null, DATE, START_TIME, DURATION));
        assertThrows(NullPointerException.class, () -> new Lesson(STUDENT, null, START_TIME, DURATION));
        assertThrows(NullPointerException.class, () -> new Lesson(STUDENT, DATE, null, DURATION));
        assertThrows(NullPointerException.class, () -> new Lesson(STUDENT, DATE, START_TIME, null));
    }

    @Test
    public void constructor_nonPositiveDuration_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () ->
                new Lesson(STUDENT, DATE, START_TIME, Duration.ZERO));
        assertThrows(IllegalArgumentException.class, () ->
                new Lesson(STUDENT, DATE, START_TIME, Duration.ofMinutes(-1)));
    }

    @Test
    public void constructor_positiveDurationAtMinimumBoundary_succeeds() {
        Lesson lesson = new Lesson(STUDENT, DATE, START_TIME, Duration.ofNanos(1));

        assertEquals(Duration.ofNanos(1), lesson.getDuration());
    }

    @Test
    public void hasLongDurationWarning_durationAtOrBelowFourHours_returnsFalse() {
        assertFalse(new Lesson(STUDENT, DATE, START_TIME, Duration.ofHours(4))
                .hasLongDurationWarning());
        assertFalse(new Lesson(STUDENT, DATE, START_TIME, Duration.ofHours(3))
                .hasLongDurationWarning());
    }

    @Test
    public void hasLongDurationWarning_durationAboveFourHours_returnsTrue() {
        assertTrue(new Lesson(STUDENT, DATE, START_TIME, Duration.ofMinutes(241))
                .hasLongDurationWarning());
    }

    @Test
    public void getters_returnConstructorValues() {
        Lesson lesson = new Lesson(STUDENT, DATE, START_TIME, DURATION);

        assertEquals(STUDENT, lesson.getStudent());
        assertEquals(DATE, lesson.getDate());
        assertEquals(START_TIME, lesson.getStartTime());
        assertEquals(DURATION, lesson.getDuration());
        assertEquals(LocalDateTime.of(2026, 10, 25, 14, 0), lesson.getStartDateTime());
        assertEquals(LocalDateTime.of(2026, 10, 25, 15, 0), lesson.getEndDateTime());
    }

    @Test
    public void overlaps_overlappingAndAdjacentLessons_returnsExpectedResult() {
        Lesson lesson = new Lesson(STUDENT, DATE, START_TIME, DURATION);
        Lesson overlappingLesson = new Lesson(STUDENT, DATE, LocalTime.of(14, 30), DURATION);
        Lesson adjacentLesson = new Lesson(STUDENT, DATE, LocalTime.of(15, 0), DURATION);

        assertTrue(lesson.overlaps(overlappingLesson));
        assertTrue(overlappingLesson.overlaps(lesson));
        assertFalse(lesson.overlaps(adjacentLesson));
    }

    @Test
    public void overlaps_nullLesson_throwsNullPointerException() {
        Lesson lesson = new Lesson(STUDENT, DATE, START_TIME, DURATION);

        assertThrows(NullPointerException.class, () ->
                lesson.overlaps(null));
    }

    @Test
    public void equals_sameValues_returnsTrue() {
        Lesson lesson = new Lesson(STUDENT, DATE, START_TIME, DURATION);
        Lesson copy = new Lesson(STUDENT, DATE, START_TIME, DURATION);
        Lesson differentDate = new Lesson(STUDENT, DATE.plusDays(1), START_TIME, DURATION);
        Lesson differentDuration = new Lesson(STUDENT, DATE, START_TIME, Duration.ofMinutes(90));
        Lesson differentTime = new Lesson(STUDENT, DATE, LocalTime.of(15, 0), DURATION);

        assertTrue(lesson.equals(lesson));
        assertTrue(lesson.equals(copy));
        assertFalse(lesson.equals(null));
        assertFalse(lesson.equals(5));
        assertFalse(lesson.equals(differentDate));
        assertFalse(lesson.equals(differentDuration));
        assertFalse(lesson.equals(differentTime));
        assertEquals(lesson.hashCode(), copy.hashCode());
    }

    @Test
    public void toStringMethod_containsLessonFields() {
        Lesson lesson = new Lesson(STUDENT, DATE, START_TIME, DURATION);

        assertEquals(Lesson.class.getCanonicalName() + "{student=" + STUDENT.getName()
                + ", date=" + DATE + ", startTime=" + START_TIME + ", duration=" + DURATION + "}",
                lesson.toString());
    }
}
