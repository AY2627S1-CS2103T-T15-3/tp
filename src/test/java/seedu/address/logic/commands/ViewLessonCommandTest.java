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
import seedu.address.model.lesson.Lesson;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.model.student.Level;
import seedu.address.model.student.Student;
import seedu.address.model.student.Subject;

public class ViewLessonCommandTest {
    private static final Student HENRY = student("Henry Tan", "91234567", "henry@example.com");
    private static final Student ALICE = student("Alice Tan", "92345678", "alice@example.com");
    private static final Lesson HENRY_LESSON = lesson(HENRY, LocalDate.of(2026, 11, 1), LocalTime.of(14, 0));
    private static final Lesson ALICE_LESSON = lesson(ALICE, LocalDate.of(2026, 11, 1), LocalTime.of(16, 0));
    private static final Lesson HENRY_OTHER_LESSON = lesson(HENRY, LocalDate.of(2026, 11, 8), LocalTime.of(14, 0));

    @Test
    public void execute_filterByDate_showsLessonsOnDate() throws Exception {
        ModelManager model = populatedModel();

        CommandResult result = new ViewLessonCommand(LocalDate.of(2026, 11, 1)).execute(model);

        assertEquals("Showing lessons on 2026-11-01.", result.getFeedbackToUser());
        assertEquals(java.util.List.of(HENRY_LESSON, ALICE_LESSON), model.getFilteredLessonList());
    }

    @Test
    public void execute_filterByStudent_showsAllStudentLessons() throws Exception {
        ModelManager model = populatedModel();

        CommandResult result = new ViewLessonCommand(Index.fromOneBased(1)).execute(model);

        assertEquals("Showing lessons for Henry Tan.", result.getFeedbackToUser());
        assertEquals(java.util.List.of(HENRY_LESSON, HENRY_OTHER_LESSON), model.getFilteredLessonList());
    }

    @Test
    public void execute_withoutFilter_showsAllLessons() throws Exception {
        ModelManager model = populatedModel();
        new ViewLessonCommand(LocalDate.of(2026, 11, 1)).execute(model);

        CommandResult result = new ViewLessonCommand().execute(model);

        assertEquals(ViewLessonCommand.MESSAGE_SHOW_ALL, result.getFeedbackToUser());
        assertEquals(java.util.List.of(HENRY_LESSON, ALICE_LESSON, HENRY_OTHER_LESSON),
                model.getFilteredLessonList());
    }

    @Test
    public void execute_filterByDate_includesOvernightLessonOnBothDates() throws Exception {
        ModelManager model = new ModelManager();
        model.addStudent(HENRY);
        Lesson overnightLesson = new Lesson(HENRY, LocalDate.of(2026, 11, 1), LocalTime.of(23, 0),
                Duration.ofHours(2));
        model.addLesson(overnightLesson);

        new ViewLessonCommand(LocalDate.of(2026, 11, 1)).execute(model);
        assertEquals(java.util.List.of(overnightLesson), model.getFilteredLessonList());

        new ViewLessonCommand(LocalDate.of(2026, 11, 2)).execute(model);
        assertEquals(java.util.List.of(overnightLesson), model.getFilteredLessonList());
    }

    @Test
    public void execute_invalidStudentIndex_throwsCommandException() {
        ModelManager model = populatedModel();

        CommandException exception = assertThrows(
                CommandException.class, () -> new ViewLessonCommand(Index.fromOneBased(3)).execute(model));

        assertEquals(Messages.MESSAGE_INVALID_STUDENT_DISPLAYED_INDEX, exception.getMessage());
    }

    @Test
    public void execute_filterThenRemove_usesDisplayedLessonIndex() throws Exception {
        ModelManager model = populatedModel();

        new ViewLessonCommand(Index.fromOneBased(1)).execute(model);
        new RemoveLessonCommand(Index.fromOneBased(1)).execute(model);

        assertEquals(java.util.List.of(ALICE_LESSON, HENRY_OTHER_LESSON), model.getLessonList());
        assertEquals(java.util.List.of(HENRY_OTHER_LESSON), model.getFilteredLessonList());
    }

    private static ModelManager populatedModel() {
        ModelManager model = new ModelManager();
        model.addStudent(HENRY);
        model.addStudent(ALICE);
        model.addLesson(HENRY_LESSON);
        model.addLesson(ALICE_LESSON);
        model.addLesson(HENRY_OTHER_LESSON);
        return model;
    }

    private static Lesson lesson(Student student, LocalDate date, LocalTime time) {
        return new Lesson(student, date, time, Duration.ofMinutes(60));
    }

    private static Student student(String name, String phone, String email) {
        return new Student(new Name(name), new Phone(phone), new Email(email), new Address("12 Main Street"),
                new Level("JC 1"), Set.of(new Subject("Mathematics")));
    }
}
