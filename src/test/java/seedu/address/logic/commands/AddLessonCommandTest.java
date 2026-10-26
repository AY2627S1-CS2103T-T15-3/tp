package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
}
