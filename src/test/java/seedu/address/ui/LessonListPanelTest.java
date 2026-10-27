package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import seedu.address.model.lesson.Lesson;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.model.student.Level;
import seedu.address.model.student.Student;
import seedu.address.model.student.Subject;

public class LessonListPanelTest {
    private static final Student STUDENT = new Student(new Name("Lucy Lim"), new Phone("91234567"),
            new Email("lucy@example.com"), new Address("21 Happy Road"), new Level("JC 1"),
            Set.of(new Subject("Mathematics")));

    @BeforeAll
    public static void initializeJavaFx() {
        try {
            Platform.startup(() -> { });
        } catch (IllegalStateException exception) {
            // JavaFX was already started by another test class in this JVM.
        }
    }

    @Test
    public void constructor_filtersToCurrentDateAndSortsByTime() {
        LocalDate today = LocalDate.now();
        Lesson later = lesson(today, LocalTime.of(16, 0));
        Lesson earlier = lesson(today, LocalTime.of(10, 0));
        ObservableList<Lesson> lessons = FXCollections.observableArrayList(
                later, lesson(today.plusDays(1), LocalTime.NOON), earlier);

        LessonListPanel panel = new LessonListPanel(lessons);
        ListView<?> listView = (ListView<?>) panel.getRoot().lookup(".list-view");

        assertEquals(java.util.List.of(earlier, later), listView.getItems());
    }

    @Test
    public void setDisplayedDate_filtersAndUpdatesPlaceholder() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        ObservableList<Lesson> lessons = FXCollections.observableArrayList(
                lesson(tomorrow, LocalTime.NOON));
        LessonListPanel panel = new LessonListPanel(lessons);
        ListView<?> listView = (ListView<?>) panel.getRoot().lookup(".list-view");

        panel.setDisplayedDate(tomorrow);

        assertEquals(1, listView.getItems().size());
        assertEquals(tomorrow, panel.getDisplayedDate());
        assertTrue(listView.getPlaceholder() instanceof Label);
        assertEquals("No lessons scheduled for "
                + tomorrow + ".", ((Label) listView.getPlaceholder()).getText());
    }

    @Test
    public void emptyCurrentDate_showsPlaceholder() {
        LessonListPanel panel = new LessonListPanel(FXCollections.observableArrayList());
        ListView<?> listView = (ListView<?>) panel.getRoot().lookup(".list-view");

        assertEquals("No lessons scheduled for "
                + LocalDate.now() + ".", ((Label) listView.getPlaceholder()).getText());
    }

    private static Lesson lesson(LocalDate date, LocalTime time) {
        return new Lesson(STUDENT, date, time, Duration.ofMinutes(60));
    }
}
