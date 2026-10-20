package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.lesson.Lesson;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.model.student.Level;
import seedu.address.model.student.Student;
import seedu.address.model.student.Subject;

public class JsonAdaptedLessonTest {

    private static final Student STUDENT = new Student(
            new Name("Lucy Lim"), new Phone("91234567"), new Email("lucy@example.com"),
            new Address("21 Happy Road"), new Level("JC 1"), Set.of(new Subject("Mathematics")));
    private static final Lesson LESSON = new Lesson(STUDENT, LocalDate.of(2026, 10, 25),
            LocalTime.of(14, 0), Duration.ofMinutes(60));

    @Test
    public void constructorFromLesson_toModelType_preservesLesson() throws Exception {
        JsonAdaptedLesson adaptedLesson = new JsonAdaptedLesson(LESSON);

        assertEquals(LESSON, adaptedLesson.toModelType(STUDENT));
    }

    @Test
    public void matchesStudent_sameNameAndPhone_returnsTrue() {
        JsonAdaptedLesson adaptedLesson = new JsonAdaptedLesson(LESSON);
        Student equivalentStudent = new Student(
                new Name("Lucy Lim"), new Phone("91234567"), new Email("other@example.com"),
                new Address("25 Happy Road"), new Level("Secondary 3"), Set.of(new Subject("Physics")));

        assertTrue(adaptedLesson.matchesStudent(equivalentStudent));
        assertFalse(adaptedLesson.matchesStudent(new Student(
                new Name("Lucy Lim"), new Phone("98765432"), new Email("lucy@example.com"),
                new Address("21 Happy Road"), new Level("JC 1"), Set.of(new Subject("Mathematics")))));
    }

    @Test
    public void toModelType_missingField_throwsIllegalValueException() {
        JsonAdaptedLesson adaptedLesson = new JsonAdaptedLesson(
                null, "91234567", "2026-10-25", "14:00", 60);

        assertThrows(IllegalValueException.class, () ->
                adaptedLesson.toModelType(STUDENT));
    }

    @Test
    public void toModelType_invalidDateTime_throwsIllegalValueException() {
        JsonAdaptedLesson adaptedLesson = new JsonAdaptedLesson(
                "Lucy Lim", "91234567", "invalid-date", "invalid-time", 60);

        assertThrows(IllegalValueException.class, () ->
                adaptedLesson.toModelType(STUDENT));
    }

    @Test
    public void toModelType_nonPositiveDuration_throwsIllegalValueException() {
        JsonAdaptedLesson adaptedLesson = new JsonAdaptedLesson(
                "Lucy Lim", "91234567", "2026-10-25", "14:00", 0);

        assertThrows(IllegalValueException.class, () ->
                adaptedLesson.toModelType(STUDENT));
    }

    @Test
    public void toModelType_nullStudent_throwsNullPointerException() {
        JsonAdaptedLesson adaptedLesson = new JsonAdaptedLesson(LESSON);

        assertThrows(NullPointerException.class, () ->
                adaptedLesson.toModelType(null));
    }
}
