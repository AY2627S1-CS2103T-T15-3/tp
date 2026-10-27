package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javafx.application.Platform;
import javafx.scene.control.Label;
import seedu.address.model.lesson.Lesson;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.model.student.Level;
import seedu.address.model.student.Student;
import seedu.address.model.student.Subject;

public class LessonCardTest {
    @BeforeAll
    public static void initializeJavaFx() {
        try {
            Platform.startup(() -> { });
        } catch (IllegalStateException exception) {
            // JavaFX was already started by another test class in this JVM.
        }
    }

    @Test
    public void constructor_displaysLessonDetails() {
        Student student = new Student(new Name("Lucy Lim"), new Phone("91234567"),
                new Email("lucy@example.com"), new Address("21 Happy Road"), new Level("JC 1"),
                Set.of(new Subject("Mathematics")));
        Lesson lesson = new Lesson(student, LocalDate.of(2026, 10, 27), LocalTime.of(14, 0),
                Duration.ofMinutes(90));
        LessonCard card = new LessonCard(lesson, 2);

        assertEquals("2. Lucy Lim", ((Label) card.getRoot().lookup("#student")).getText());
        assertEquals("Date: 2026-10-27", ((Label) card.getRoot().lookup("#date")).getText());
        assertEquals("Time: 14:00 - 15:30", ((Label) card.getRoot().lookup("#time")).getText());
        assertEquals("Length: 90 minutes", ((Label) card.getRoot().lookup("#duration")).getText());
    }
}
