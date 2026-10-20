package seedu.address.model.lesson;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.model.lesson.exceptions.DuplicateLessonException;
import seedu.address.model.lesson.exceptions.LessonNotFoundException;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.model.student.Level;
import seedu.address.model.student.Student;
import seedu.address.model.student.Subject;

public class UniqueLessonListTest {

    private static final Student STUDENT = new Student(
            new Name("Henry Tan"), new Phone("91234567"), new Email("henry@example.com"),
            new Address("12 ABCD Ave 3"), new Level("Secondary 3"),
            Set.of(new Subject("Mathematics")));
    private static final LocalDate DATE = LocalDate.of(2026, 10, 25);
    private static final Lesson LESSON = lessonAt(14, 0);
    private static final Lesson OTHER_LESSON = lessonAt(16, 0);

    private final UniqueLessonList uniqueLessonList = new UniqueLessonList();

    @Test
    public void contains_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () ->
                uniqueLessonList.contains(null));
    }

    @Test
    public void add_duplicateLesson_throwsDuplicateLessonException() {
        uniqueLessonList.add(LESSON);

        assertThrows(DuplicateLessonException.class, () ->
                uniqueLessonList.add(LESSON));
    }

    @Test
    public void add_uniqueLessons_addsSuccessfully() {
        uniqueLessonList.add(LESSON);
        uniqueLessonList.add(OTHER_LESSON);

        assertTrue(uniqueLessonList.contains(LESSON));
        assertEquals(List.of(LESSON, OTHER_LESSON),
                uniqueLessonList.asUnmodifiableObservableList());
    }

    @Test
    public void remove_lesson_removesSuccessfully() {
        uniqueLessonList.add(LESSON);

        uniqueLessonList.remove(LESSON);

        assertEquals(List.of(), uniqueLessonList.asUnmodifiableObservableList());
    }

    @Test
    public void remove_missingLesson_throwsLessonNotFoundException() {
        assertThrows(LessonNotFoundException.class, () ->
                uniqueLessonList.remove(LESSON));
    }

    @Test
    public void setLesson_existingLesson_replacesSuccessfully() {
        uniqueLessonList.add(LESSON);

        uniqueLessonList.setLesson(LESSON, OTHER_LESSON);

        assertEquals(List.of(OTHER_LESSON), uniqueLessonList.asUnmodifiableObservableList());
    }

    @Test
    public void setLesson_missingLesson_throwsLessonNotFoundException() {
        assertThrows(LessonNotFoundException.class, () ->
                uniqueLessonList.setLesson(LESSON, OTHER_LESSON));
    }

    @Test
    public void setLessons_duplicateList_throwsDuplicateLessonException() {
        assertThrows(DuplicateLessonException.class, () ->
                uniqueLessonList.setLessons(List.of(LESSON, LESSON)));
    }

    @Test
    public void setLessons_list_replacesContents() {
        uniqueLessonList.add(LESSON);

        uniqueLessonList.setLessons(List.of(OTHER_LESSON));

        assertEquals(List.of(OTHER_LESSON), uniqueLessonList.asUnmodifiableObservableList());
    }

    @Test
    public void asUnmodifiableObservableList_cannotBeModified() {
        uniqueLessonList.add(LESSON);

        assertThrows(UnsupportedOperationException.class, () ->
                uniqueLessonList.asUnmodifiableObservableList().clear());
    }

    @Test
    public void nullArguments_throwNullPointerException() {
        assertThrows(NullPointerException.class, () ->
                uniqueLessonList.add(null));
        assertThrows(NullPointerException.class, () ->
                uniqueLessonList.remove(null));
        assertThrows(NullPointerException.class, () ->
                uniqueLessonList.setLesson(null, LESSON));
        assertThrows(NullPointerException.class, () ->
                uniqueLessonList.setLesson(LESSON, null));
        assertThrows(NullPointerException.class, () ->
                uniqueLessonList.setLessons((List<Lesson>) null));
        assertThrows(NullPointerException.class, () ->
                uniqueLessonList.setLessons((UniqueLessonList) null));
    }

    private static Lesson lessonAt(int hour, int minute) {
        return new Lesson(STUDENT, DATE, LocalTime.of(hour, minute), Duration.ofMinutes(60));
    }
}
