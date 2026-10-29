package seedu.address.ui;

import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Region;
import javafx.scene.text.TextFlow;
import seedu.address.model.guardian.Guardian;
import seedu.address.model.student.PaymentMonth;
import seedu.address.model.student.Student;

/** UI component that displays a student. */
public class StudentCard extends UiPart<Region> {
    private static final String FXML = "StudentListCard.fxml";
    /** Number of most recent paid months shown before the rest are hidden behind the toggle button. */
    private static final int PAID_MONTHS_SHOWN = 3;

    public final Student student;

    private final List<String> paidMonths;
    private boolean isShowingAllPaidMonths;

    @FXML
    private TextFlow name;
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
    private Button paymentsToggle;
    @FXML
    private FlowPane subjects;
    @FXML
    private FlowPane guardians;
    @FXML
    private Label remark;

    /**
     * Creates a card displaying the details of a student.
     *
     * @param student the student to display
     * @param displayedIndex the student's displayed index
     */
    public StudentCard(Student student, int displayedIndex, Function<Student, List<Guardian>> guardianProvider) {
        this(student, displayedIndex, guardianProvider, List.of());
    }

    /**
     * Creates a card displaying the details of a student with highlighted text.
     *
     * @param student the student to display
     * @param displayedIndex the student's displayed index
     * @param highlightKeywords the text to highlight after a find-s command
     */
    public StudentCard(Student student, int displayedIndex, Function<Student, List<Guardian>> guardianProvider,
                       List<String> highlightKeywords) {
        super(FXML);
        this.student = student;
        id.setText(displayedIndex + ". ");
        name.getChildren().setAll(HighlightText.create(student.getName().fullName, highlightKeywords).getChildren());
        phone.setText("Phone: " + student.getPhone().value);
        email.setText("Email: " + student.getEmail().value);
        address.setText("Address: " + student.getAddress().value);
        level.setText("Level: " + student.getLevel().value);
        remark.setText("Remark: " + student.getRemark().value);
        remark.setVisible(!student.getRemark().isEmpty());
        remark.setManaged(remark.isVisible());
        paidMonths = student.getPaidMonths().reversed().stream()
                .map(PaymentMonth::toDisplayString)
                .toList();
        showPaidMonths(false);
        paymentsToggle.setOnAction(event -> showPaidMonths(!isShowingAllPaidMonths));
        paymentsToggle.setVisible(paidMonths.size() > PAID_MONTHS_SHOWN);
        paymentsToggle.setManaged(paymentsToggle.isVisible());
        student.getSubjects().stream()
                .sorted(Comparator.comparing(subject -> subject.value))
                .forEach(subject -> subjects.getChildren().add(new Label(subject.value)));
        guardianProvider.apply(student).forEach(guardian -> guardians.getChildren().add(
                new Label(guardian.getName().fullName + " (" + guardian.getPhone().value + ")")));
    }

    /**
     * Shows either all paid months or only the most recent few, and updates the toggle button to match.
     *
     * @param showAll whether all paid months should be shown
     */
    private void showPaidMonths(boolean showAll) {
        isShowingAllPaidMonths = showAll;
        int shownCount = showAll ? paidMonths.size() : Math.min(paidMonths.size(), PAID_MONTHS_SHOWN);
        payments.setText("Paid: " + (paidMonths.isEmpty() ? "None"
                : String.join(", ", paidMonths.subList(0, shownCount))));
        paymentsToggle.setText(showAll ? "Show less" : "+" + (paidMonths.size() - shownCount) + " more");
    }
}
