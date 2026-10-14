package seedu.address.ui;

import java.util.List;
import java.util.function.Function;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import seedu.address.model.guardian.Guardian;
import seedu.address.model.student.Student;

/** Panel containing the displayed students. */
public class StudentListPanel extends UiPart<Region> {
    private static final String FXML = "StudentListPanel.fxml";

    @FXML
    private ListView<Student> studentListView;

    private final Function<Student, List<Guardian>> guardianProvider;

    /**
     * Creates a panel displaying the given list of students.
     *
     * @param studentList the list of students to display
     * @param guardianProvider retrieves guardians linked to a student
     */
    public StudentListPanel(ObservableList<Student> studentList,
                            Function<Student, List<Guardian>> guardianProvider) {
        super(FXML);
        this.guardianProvider = guardianProvider;
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

    /** Refreshes cards after a relationship changes. */
    public void refresh() {
        studentListView.refresh();
    }

    /** Highlights the specified student if it is in the displayed list. */
    public void highlightStudent(Student student) {
        studentListView.getSelectionModel().select(student);
    }

    class StudentListViewCell extends ListCell<Student> {
        @Override
        protected void updateItem(Student student, boolean empty) {
            super.updateItem(student, empty);
            if (empty || student == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(new StudentCard(student, getIndex() + 1, guardianProvider).getRoot());
            }
        }
    }
}
