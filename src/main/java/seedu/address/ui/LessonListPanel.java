package seedu.address.ui;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.function.Consumer;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import seedu.address.model.lesson.Lesson;
import seedu.address.model.lesson.LessonMatchesDatePredicate;

/** Panel containing the scheduled lessons. */
public class LessonListPanel extends UiPart<Region> {
    private static final String FXML = "LessonListPanel.fxml";

    @FXML
    private ListView<Lesson> lessonListView;
    private final FilteredList<Lesson> filteredLessons;
    private final Consumer<LocalDate> displayedDateUpdater;
    private LocalDate displayedDate;

    /** Creates a panel displaying lessons filtered to the current day. */
    public LessonListPanel(ObservableList<Lesson> lessonList) {
        this(lessonList, null);
    }

    /** Creates a panel that also updates the model's displayed lesson predicate. */
    public LessonListPanel(ObservableList<Lesson> lessonList, Consumer<LocalDate> displayedDateUpdater) {
        super(FXML);
        this.displayedDateUpdater = displayedDateUpdater;
        displayedDate = LocalDate.now();
        if (displayedDateUpdater != null) {
            displayedDateUpdater.accept(displayedDate);
        }
        filteredLessons = new FilteredList<>(lessonList, new LessonMatchesDatePredicate(displayedDate));
        SortedList<Lesson> sortedLessons = new SortedList<>(filteredLessons,
                Comparator.comparing(Lesson::getDate).thenComparing(Lesson::getStartTime));
        lessonListView.setItems(sortedLessons);
        updatePlaceholder();
        lessonListView.setCellFactory(listView -> new LessonListViewCell());
        lessonList.addListener((ListChangeListener<Lesson>) change -> {
            while (change.next()) {
                if (change.wasAdded() && change.getAddedSize() > 0) {
                    lessonListView.getSelectionModel().select(
                            change.getFrom() + change.getAddedSize() - 1);
                }
            }
        });
    }

    /** Refreshes the displayed lesson cards. */
    public void refresh() {
        lessonListView.refresh();
    }

    /** Changes the date whose lessons are displayed. */
    public void setDisplayedDate(LocalDate date) {
        displayedDate = date;
        if (displayedDateUpdater != null) {
            displayedDateUpdater.accept(date);
        }
        filteredLessons.setPredicate(new LessonMatchesDatePredicate(displayedDate));
        updatePlaceholder();
    }

    /** Returns the date currently displayed by this panel. */
    public LocalDate getDisplayedDate() {
        return displayedDate;
    }

    private void updatePlaceholder() {
        Label placeholder = new Label("No lessons scheduled for " + displayedDate + ".");
        placeholder.getStyleClass().add("label-bright");
        placeholder.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        placeholder.setWrapText(true);
        lessonListView.setPlaceholder(placeholder);
    }

    class LessonListViewCell extends ListCell<Lesson> {
        @Override
        public void updateItem(Lesson lesson, boolean empty) {
            super.updateItem(lesson, empty);
            if (empty || lesson == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(new LessonCard(lesson, getIndex() + 1).getRoot());
            }
        }
    }
}
