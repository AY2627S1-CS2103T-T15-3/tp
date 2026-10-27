package seedu.address.ui;

import java.time.Duration;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import seedu.address.model.lesson.Lesson;

/** UI component that displays a lesson. */
public class LessonCard extends UiPart<Region> {
    private static final String FXML = "LessonListCard.fxml";

    @FXML
    private Label student;
    @FXML
    private Label date;
    @FXML
    private Label time;
    @FXML
    private Label duration;

    /** Creates a card displaying the given lesson and its displayed index. */
    public LessonCard(Lesson lesson, int displayedIndex) {
        super(FXML);
        student.setText(displayedIndex + ". " + lesson.getStudent().getName());
        date.setText("Date: " + lesson.getDate());
        time.setText("Time: " + lesson.getStartTime() + " - " + lesson.getEndDateTime().toLocalTime());
        Duration lessonDuration = lesson.getDuration();
        duration.setText("Length: " + lessonDuration.toMinutes() + " minutes");
    }
}
