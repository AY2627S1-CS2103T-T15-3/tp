package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.model.student.Level;
import seedu.address.model.student.Student;
import seedu.address.model.student.Subject;

/** Tests student-name highlighting in JavaFX UI components. */
public class StudentHighlightingUiTest {

    @BeforeAll
    public static void initializeJavaFx() {
        try {
            Platform.startup(() -> { });
        } catch (IllegalStateException ignored) {
            // JavaFX was already initialized by another UI test class.
        }
    }

    @Test
    public void studentCard_highlightsMatchingNamePrefix() {
        StudentCard card = new StudentCard(student("Alice Tan"), 1, ignored -> List.of(), List.of("ali"));
        TextFlow name = (TextFlow) card.getRoot().lookup("#name");

        assertEquals(List.of("Ali", "ce Tan"), name.getChildren().stream()
                .map(node -> node instanceof Label ? ((Label) node).getText() : ((Text) node).getText())
                .toList());
        assertEquals("search-match", name.getChildren().get(0).getStyleClass().get(0));
    }

    private static Student student(String name) {
        return new Student(new Name(name), new Phone("91234567"), new Email("student@example.com"),
                new Address("21 Happy Road"), new Level("JC 1"), Set.of(new Subject("Mathematics")));
    }
}
