package seedu.address.ui;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import seedu.address.model.lesson.Lesson;

/** Panel containing the displayed lessons. */
public class LessonListPanel extends UiPart<Region> {
    private static final String FXML = "LessonListPanel.fxml";

    @FXML
    private ListView<Lesson> lessonListView;

    /** Creates a panel displaying the given list of lessons. */
    public LessonListPanel(ObservableList<Lesson> lessonList) {
        super(FXML);
        lessonListView.setItems(lessonList);
        lessonListView.setCellFactory(listView -> new LessonListViewCell());
    }

    /** Returns the lessons currently displayed by this panel. */
    ObservableList<Lesson> getDisplayedLessons() {
        return lessonListView.getItems();
    }

    class LessonListViewCell extends ListCell<Lesson> {
        @Override
        protected void updateItem(Lesson lesson, boolean empty) {
            super.updateItem(lesson, empty);
            if (empty || lesson == null) {
                setText(null);
            } else {
                setText(String.format("%s | %s | %s-%s", lesson.getStudent().getName(),
                        lesson.getDate(), lesson.getStartTime(), lesson.getEndDateTime().toLocalTime()));
            }
        }
    }
}
