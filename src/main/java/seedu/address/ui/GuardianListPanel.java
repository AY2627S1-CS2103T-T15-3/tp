package seedu.address.ui;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import seedu.address.model.guardian.Guardian;

/** Panel containing the displayed guardians. */
public class GuardianListPanel extends UiPart<Region> {
    private static final String FXML = "GuardianListPanel.fxml";

    @FXML
    private ListView<Guardian> guardianListView;

    /**
     * Creates a panel displaying the given list of guardians.
     *
     * @param guardianList the list of guardians to display
     */
    public GuardianListPanel(ObservableList<Guardian> guardianList) {
        super(FXML);
        guardianListView.setItems(guardianList);
        guardianListView.setCellFactory(listView -> new GuardianListViewCell());
        guardianList.addListener((ListChangeListener<Guardian>) change -> {
            while (change.next()) {
                if (change.wasAdded() && change.getAddedSize() > 0) {
                    guardianListView.getSelectionModel().select(
                            change.getFrom() + change.getAddedSize() - 1);
                }
            }
        });
    }

    class GuardianListViewCell extends ListCell<Guardian> {
        @Override
        protected void updateItem(Guardian guardian, boolean empty) {
            super.updateItem(guardian, empty);
            if (empty || guardian == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(new GuardianCard(guardian, getIndex() + 1).getRoot());
            }
        }
    }
}
