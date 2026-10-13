package seedu.address.ui;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import seedu.address.model.student.Student;

/** Panel containing the displayed students. */
public class StudentListPanel extends UiPart<Region> {
    private static final String FXML = "StudentListPanel.fxml";

    @FXML
    private ListView<Student> studentListView;

    /**
     * Creates a panel displaying the given list of students.
     *
     * @param studentList the list of students to display
     */
    public StudentListPanel(ObservableList<Student> studentList) {
        super(FXML);
        studentListView.setItems(studentList);
        studentListView.setCellFactory(listView -> new StudentListViewCell());
        studentList.addListener((ListChangeListener<Student>) change -> {
            while (change.next()) {
                if (change.wasAdded() && change.getAddedSize() > 0) {
                    studentListView.getSelectionModel().select(
                            change.getFrom() + change.getAddedSize() - 1);
                }
            }
        });
    }

    class StudentListViewCell extends ListCell<Student> {
        @Override
        protected void updateItem(Student student, boolean empty) {
            super.updateItem(student, empty);
            if (empty || student == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(new StudentCard(student, getIndex() + 1).getRoot());
            }
        }
    }
}
