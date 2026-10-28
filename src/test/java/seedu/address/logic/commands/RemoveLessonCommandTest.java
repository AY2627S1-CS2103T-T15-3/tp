package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.model.ModelManager;
import seedu.address.model.lesson.Lesson;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.model.student.Level;
import seedu.address.model.student.Student;
import seedu.address.model.student.Subject;

public class RemoveLessonCommandTest {
    private static final Student STUDENT = new Student(new Name("Lucy Lim"), new Phone("91234567"),
            new Email("lucy@example.com"), new Address("21 Happy Road"), new Level("JC 1"),
            Set.of(new Subject("Mathematics")));

    @Test
    public void execute_removesLessonInDateAndTimeOrder() throws Exception {
        ModelManager model = new ModelManager();
        Lesson later = lesson(LocalTime.of(16, 0));
        Lesson earlier = lesson(LocalTime.of(10, 0));
        model.addLesson(later);
        model.addLesson(earlier);
        model.addLesson(new Lesson(STUDENT, LocalDate.now().plusDays(1), LocalTime.NOON,
                Duration.ofMinutes(60)));

        CommandResult result = new RemoveLessonCommand(Index.fromOneBased(1)).execute(model);

        assertEquals(String.format(RemoveLessonCommand.MESSAGE_REMOVE_LESSON_SUCCESS, earlier),
                result.getFeedbackToUser());
        assertEquals(2, model.getLessonList().size());
        assertEquals(later, model.getLessonList().get(0));
    }

    @Test
    public void execute_removesFutureLesson() throws Exception {
        ModelManager model = new ModelManager();
        Lesson futureLesson = new Lesson(STUDENT, LocalDate.now().plusDays(1), LocalTime.NOON,
                Duration.ofMinutes(60));
        model.addLesson(futureLesson);

        new RemoveLessonCommand(Index.fromOneBased(1)).execute(model);

        assertTrue(model.getLessonList().isEmpty());
    }

    @Test
    public void execute_removesLessonAcrossMidnight() throws Exception {
        ModelManager model = new ModelManager();
        Lesson overnightLesson = new Lesson(STUDENT, LocalDate.now(), LocalTime.of(23, 30),
                Duration.ofMinutes(120));
        model.addLesson(overnightLesson);

        new RemoveLessonCommand(Index.fromOneBased(1)).execute(model);

        assertFalse(model.getLessonList().contains(overnightLesson));
        assertTrue(model.getLessonList().isEmpty());
    }

    @Test
    public void execute_nullModel_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () ->
                new RemoveLessonCommand(Index.fromOneBased(1)).execute(null));
    }

    @Test
    public void equalsAndToString_behavesAsExpected() {
        RemoveLessonCommand first = new RemoveLessonCommand(Index.fromOneBased(1));
        RemoveLessonCommand same = new RemoveLessonCommand(Index.fromOneBased(1));
        RemoveLessonCommand different = new RemoveLessonCommand(Index.fromOneBased(2));

        assertEquals(first, first);
        assertEquals(first, same);
        assertNotEquals(first, different);
        assertNotEquals(first, null);
        assertNotEquals(first, "command");
        assertTrue(first.toString().contains("targetIndex="));
    }

    private static Lesson lesson(LocalTime time) {
        return new Lesson(STUDENT, LocalDate.now(), time, Duration.ofMinutes(60));
    }
}
