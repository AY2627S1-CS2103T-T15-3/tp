package seedu.address.ui;

import java.util.List;

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
    private List<String> highlightKeywords = List.of();

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

    public void setHighlightKeywords(List<String> keywords) {
        highlightKeywords = List.copyOf(keywords);
        guardianListView.refresh();
    }

    /** Clears the current guardian selection. */
    public void clearSelection() {
        guardianListView.getSelectionModel().clearSelection();
    }

    /** Refreshes guardian cards after a guardian is edited. */
    public void refresh() {
        guardianListView.refresh();
    }

    /** Highlights the specified guardian if it is in the displayed list. */
    public void highlightGuardian(Guardian guardian) {
        guardianListView.getSelectionModel().select(guardian);
    }

    class GuardianListViewCell extends ListCell<Guardian> {
        @Override
        protected void updateItem(Guardian guardian, boolean empty) {
            super.updateItem(guardian, empty);
            if (empty || guardian == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(new GuardianCard(guardian, getIndex() + 1, highlightKeywords).getRoot());
            }
        }
    }
}
