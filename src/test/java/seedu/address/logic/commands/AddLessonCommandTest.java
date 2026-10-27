package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.ModelManager;
import seedu.address.model.lesson.Lesson;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.model.student.Level;
import seedu.address.model.student.Student;
import seedu.address.model.student.Subject;

public class AddLessonCommandTest {
    private static final Student STUDENT = new Student(Name.fromStudentValue("Amy Tan"),
            Phone.fromStudentValue("91234567"), Email.fromStudentValue("amy@example.com"),
            Address.fromStudentValue("12 Main Street"), new Level("JC 1"), Set.of(new Subject("Mathematics")));

    @Test
    public void execute_addsWeeklyLessons() throws Exception {
        ModelManager model = new ModelManager();
        model.addStudent(STUDENT);

        CommandResult result = new AddLessonCommand(Index.fromOneBased(1), LocalDate.of(2026, 11, 1),
                LocalTime.of(14, 0), Duration.ofMinutes(60), 3).execute(model);

        assertEquals(3, model.getLessonList().size());
        assertEquals(LocalDate.of(2026, 11, 1), model.getLessonList().get(0).getDate());
        assertEquals(LocalDate.of(2026, 11, 15), model.getLessonList().get(2).getDate());
        assertEquals(String.format(AddLessonCommand.MESSAGE_SUCCESS, 3, STUDENT.getName()),
                result.getFeedbackToUser());
    }

    @Test
    public void execute_oneWeekBoundary_addsOneLesson() throws Exception {
        ModelManager model = new ModelManager();
        model.addStudent(STUDENT);

        new AddLessonCommand(Index.fromOneBased(1), LocalDate.of(2026, 11, 1),
                LocalTime.NOON, Duration.ofMinutes(60), 1)
                .execute(model);

        assertEquals(1, model.getLessonList().size());
    }

    @Test
    public void execute_invalidStudentIndex_failure() {
        ModelManager model = new ModelManager();
        CommandException exception = assertThrows(CommandException.class, () ->
                new AddLessonCommand(Index.fromOneBased(1), LocalDate.now(),
                        LocalTime.NOON, Duration.ofMinutes(60), 1)
                        .execute(model));
        assertEquals(Messages.MESSAGE_INVALID_STUDENT_DISPLAYED_INDEX, exception.getMessage());
    }

    @Test
    public void constructor_nonPositiveWeeks_failure() {
        seedu.address.testutil.Assert.assertThrows(IllegalArgumentException.class,
                AddLessonCommand.MESSAGE_INVALID_WEEKS, () ->
                new AddLessonCommand(Index.fromOneBased(1), LocalDate.now(),
                        LocalTime.NOON, Duration.ofMinutes(60), 0));
    }

    @Test
    public void constructor_nonPositiveDuration_failure() {
        assertThrows(IllegalArgumentException.class, () ->
                new AddLessonCommand(Index.fromOneBased(1), LocalDate.now(), LocalTime.NOON,
                        Duration.ZERO, 1));
    }

    @Test
    public void execute_overlap_rollsBackWeeklySeries() {
        ModelManager model = new ModelManager();
        model.addStudent(STUDENT);
        model.addLesson(new Lesson(STUDENT, LocalDate.of(2026, 11, 8), LocalTime.NOON,
                Duration.ofMinutes(60)));

        assertThrows(CommandException.class, () ->
                new AddLessonCommand(Index.fromOneBased(1), LocalDate.of(2026, 11, 1), LocalTime.NOON,
                        Duration.ofMinutes(60), 3).execute(model));
        assertEquals(1, model.getLessonList().size());
    }

    @Test
    public void execute_seriesLessonsDoNotOverlap() {
        ModelManager model = new ModelManager();
        model.addStudent(STUDENT);

        CommandResult result = assertDoesNotThrow(() -> new AddLessonCommand(Index.fromOneBased(1),
                LocalDate.of(2026, 11, 1), LocalTime.NOON, Duration.ofMinutes(60), 2).execute(model));

        assertEquals(String.format(AddLessonCommand.MESSAGE_SUCCESS, 2, STUDENT.getName()),
                result.getFeedbackToUser());
    }

    @Test
    public void execute_seriesOverlap_throwsCommandException() {
        ModelManager model = new ModelManager();
        model.addStudent(STUDENT);

        CommandException exception = assertThrows(CommandException.class, () ->
                new AddLessonCommand(Index.fromOneBased(1), LocalDate.of(2026, 11, 1), LocalTime.NOON,
                        Duration.ofDays(8), 2).execute(model));

        org.junit.jupiter.api.Assertions.assertTrue(exception.getMessage()
                .contains("overlaps with another lesson in the series"));
        assertEquals(0, model.getLessonList().size());
    }

    @Test
    public void execute_nullModel_throwsNullPointerException() {
        AddLessonCommand command = new AddLessonCommand(Index.fromOneBased(1), LocalDate.of(2026, 11, 1),
                LocalTime.NOON, Duration.ofMinutes(60), 1);

        assertThrows(NullPointerException.class, () -> command.execute(null));
    }

    @Test
    public void constructor_nullArguments_throwNullPointerException() {
        assertThrows(NullPointerException.class, () ->
                new AddLessonCommand(null, LocalDate.now(), LocalTime.NOON, Duration.ofMinutes(60), 1));
        assertThrows(NullPointerException.class, () ->
                new AddLessonCommand(Index.fromOneBased(1), null, LocalTime.NOON, Duration.ofMinutes(60), 1));
        assertThrows(NullPointerException.class, () ->
                new AddLessonCommand(Index.fromOneBased(1), LocalDate.now(), null, Duration.ofMinutes(60), 1));
        assertThrows(NullPointerException.class, () ->
                new AddLessonCommand(Index.fromOneBased(1), LocalDate.now(), LocalTime.NOON, null, 1));
    }

    @Test
    public void equalsAndHashCode_sameValues_equal() {
        AddLessonCommand first = new AddLessonCommand(Index.fromOneBased(1), LocalDate.of(2026, 11, 1),
                LocalTime.NOON, Duration.ofMinutes(60), 2);
        AddLessonCommand same = new AddLessonCommand(Index.fromOneBased(1), LocalDate.of(2026, 11, 1),
                LocalTime.NOON, Duration.ofMinutes(60), 2);
        AddLessonCommand different = new AddLessonCommand(Index.fromOneBased(2), LocalDate.of(2026, 11, 1),
                LocalTime.NOON, Duration.ofMinutes(60), 2);

        assertEquals(first, first);
        assertEquals(first, same);
        assertEquals(first.hashCode(), same.hashCode());
        assertNotEquals(first, different);
        assertNotEquals(first, null);
        assertNotEquals(first, "command");
    }
}
