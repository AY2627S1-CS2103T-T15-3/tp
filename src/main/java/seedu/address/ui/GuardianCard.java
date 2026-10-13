package seedu.address.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import seedu.address.model.guardian.Guardian;

/** UI component that displays a guardian. */
public class GuardianCard extends UiPart<Region> {
    private static final String FXML = "GuardianListCard.fxml";

    public final Guardian guardian;

    @FXML
    private Label name;
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
        super(FXML);
        this.guardian = guardian;
        id.setText(displayedIndex + ". ");
        name.setText(guardian.getName().fullName);
        phone.setText("Phone: " + guardian.getPhone().value);
        email.setText("Email: " + guardian.getEmail().value);
        address.setText("Address: " + guardian.getAddress().value);
    }
}
