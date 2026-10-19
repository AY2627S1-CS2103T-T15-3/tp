package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import seedu.address.model.guardian.Guardian;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;

/** Tests text highlighting in JavaFX UI components. */
public class GuardianHighlightingUiTest {

    @BeforeAll
    public static void initializeJavaFx() {
        try {
            Platform.startup(() -> { });
        } catch (IllegalStateException ignored) {
            // JavaFX was already initialized by another UI test class.
        }
    }

    @Test
    public void highlightText_highlightsCaseInsensitivePrefixes() {
        TextFlow flow = HighlightText.create("Alice Bob", List.of("ali", "BO"));

        assertEquals(4, flow.getChildren().size());
        assertEquals("search-match", flow.getChildren().get(0).getStyleClass().get(0));
        assertEquals("search-match", flow.getChildren().get(2).getStyleClass().get(0));
    }

    @Test
    public void guardianCard_highlightsMatchingNamePrefix() {
        GuardianCard card = new GuardianCard(guardian("Ben Tan"), 1, List.of("be"));
        TextFlow name = (TextFlow) card.getRoot().lookup("#name");

        assertEquals(List.of("Be", "n Tan"), name.getChildren().stream()
                .map(node -> node instanceof Label ? ((Label) node).getText() : ((Text) node).getText())
                .toList());
        assertEquals("search-match", name.getChildren().get(0).getStyleClass().get(0));
    }

    private static Guardian guardian(String name) {
        return new Guardian(new Name(name), new Phone("92345678"), new Email("ben@example.com"),
                new Address("25 Happy Road"));
    }
}
