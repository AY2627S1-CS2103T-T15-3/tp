package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.scene.control.Label;

/** Tests the student list panel's active-filter indicator. */
public class StudentListPanelTest {

    @BeforeAll
    public static void initializeJavaFx() {
        try {
            Platform.startup(() -> { });
        } catch (IllegalStateException ignored) {
            // JavaFX was already initialized by another UI test class.
        }
    }

    @Test
    public void filterDescription_isShownAndCleared() {
        StudentListPanel panel = new StudentListPanel(
                FXCollections.observableArrayList(), ignored -> java.util.List.of());
        Label filterLabel = (Label) panel.getRoot().lookup("#filterLabel");

        assertFalse(filterLabel.isVisible());
        assertFalse(filterLabel.isManaged());

        panel.setFilterDescription("Filtered: Secondary 3");
        assertEquals("Filtered: Secondary 3", filterLabel.getText());
        assertTrue(filterLabel.isVisible());
        assertTrue(filterLabel.isManaged());

        panel.clearFilterDescription();
        assertEquals("", filterLabel.getText());
        assertFalse(filterLabel.isVisible());
        assertFalse(filterLabel.isManaged());
    }
}
