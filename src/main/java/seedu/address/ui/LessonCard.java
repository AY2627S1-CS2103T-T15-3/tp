package seedu.address.ui;

import java.time.format.DateTimeFormatter;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.text.TextFlow;
import seedu.address.model.lesson.Lesson;

/** UI component that displays a lesson. */
public class LessonCard extends UiPart<Region> {
    private static final String FXML = "LessonListCard.fxml";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @FXML
    private TextFlow name;
    @FXML
    private Label id;
    @FXML
    private Label date;
    @FXML
    private Label time;

    /** Creates a card displaying the given lesson and its displayed index. */
    public LessonCard(Lesson lesson, int displayedIndex) {
        super(FXML);
        id.setText(displayedIndex + ". ");
        name.getChildren().setAll(HighlightText.create(lesson.getStudent().getName().fullName, List.of()).getChildren());
        date.setText("Date: " + lesson.getDate());
        time.setText("Time: " + lesson.getStartDateTime().format(DATE_TIME_FORMATTER)
                + " - " + lesson.getEndDateTime().format(DATE_TIME_FORMATTER));
    }
}
