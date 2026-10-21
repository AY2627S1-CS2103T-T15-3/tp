package seedu.address.ui;

import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Region;
import seedu.address.model.guardian.Guardian;
import seedu.address.model.student.PaymentMonth;
import seedu.address.model.student.Student;

/** UI component that displays a student. */
public class StudentCard extends UiPart<Region> {
    private static final String FXML = "StudentListCard.fxml";

    public final Student student;

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
    @FXML
    private Label level;
    @FXML
    private Label payments;
    @FXML
    private FlowPane subjects;
    @FXML
    private FlowPane guardians;

    /**
     * Creates a card displaying the details of a student.
     *
     * @param student the student to display
     * @param displayedIndex the student's displayed index
     */
    public StudentCard(Student student, int displayedIndex, Function<Student, List<Guardian>> guardianProvider) {
        super(FXML);
        this.student = student;
        id.setText(displayedIndex + ". ");
        name.setText(student.getName().fullName);
        phone.setText("Phone: " + student.getPhone().value);
        email.setText("Email: " + student.getEmail().value);
        address.setText("Address: " + student.getAddress().value);
        level.setText("Level: " + student.getLevel().value);
        payments.setText("Paid: " + (student.getPaidMonths().isEmpty() ? "None"
                : student.getPaidMonths().stream()
                        .map(PaymentMonth::toDisplayString)
                        .collect(Collectors.joining(", "))));
        student.getSubjects().stream()
                .sorted(Comparator.comparing(subject -> subject.value))
                .forEach(subject -> subjects.getChildren().add(new Label(subject.value)));
        guardianProvider.apply(student).forEach(guardian -> guardians.getChildren().add(
                new Label(guardian.getName().fullName + " (" + guardian.getPhone().value + ")")));
    }
}
