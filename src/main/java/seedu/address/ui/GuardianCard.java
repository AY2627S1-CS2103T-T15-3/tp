package seedu.address.ui;

import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.text.TextFlow;
import seedu.address.model.guardian.Guardian;

/** UI component that displays a guardian. */
public class GuardianCard extends UiPart<Region> {
    private static final String FXML = "GuardianListCard.fxml";

    public final Guardian guardian;

    @FXML
    private TextFlow name;
    @FXML
    private Label id;
    @FXML
    private Label phone;
    @FXML
    private Label address;
    @FXML
    private Label email;

    /**
     * Creates a card displaying the details of a guardian.
     *
     * @param guardian the guardian to display
     * @param displayedIndex the guardian's displayed index
     */
    public GuardianCard(Guardian guardian, int displayedIndex) {
        this(guardian, displayedIndex, List.of());
    }

    /**
     * Creates a card displaying the details of a guardian with highlighted text.
     *
     * @param guardian the guardian to display
     * @param displayedIndex the guardian's displayed index
     * @param highlightKeywords the text to highlight after a find-g command
     */
    public GuardianCard(Guardian guardian, int displayedIndex, List<String> highlightKeywords) {
        super(FXML);
        this.guardian = guardian;
        id.setText(displayedIndex + ". ");
        name.getChildren().setAll(HighlightText.create(guardian.getName().fullName, highlightKeywords).getChildren());
        phone.setText("Phone: " + guardian.getPhone().value);
        email.setText("Email: " + guardian.getEmail().value);
        address.setText("Address: " + guardian.getAddress().value);
    }
}
