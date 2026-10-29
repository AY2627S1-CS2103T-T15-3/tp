package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.scene.control.Label;

/** Tests the guardian list panel's active-search indicator. */
public class GuardianListPanelTest {

    @BeforeAll
    public static void initializeJavaFx() {
        try {
            Platform.startup(() -> { });
        } catch (IllegalStateException ignored) {
            // JavaFX was already initialized by another UI test class.
        }
    }

    @Test
    public void searchDescription_isShownAndCleared() {
        GuardianListPanel panel = new GuardianListPanel(FXCollections.observableArrayList());
        Label searchLabel = (Label) panel.getRoot().lookup("#searchLabel");

        assertFalse(searchLabel.isVisible());
        assertFalse(searchLabel.isManaged());

        panel.setSearchDescription("Search: Ben");
        assertEquals("Search: Ben", searchLabel.getText());
        assertTrue(searchLabel.isVisible());
        assertTrue(searchLabel.isManaged());

        panel.clearSearchDescription();
        assertEquals("", searchLabel.getText());
        assertFalse(searchLabel.isVisible());
        assertFalse(searchLabel.isManaged());
    }
}
