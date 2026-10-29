package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.model.student.Level;
import seedu.address.model.student.PaymentMonth;
import seedu.address.model.student.Remark;
import seedu.address.model.student.Student;
import seedu.address.model.student.Subject;

/** Tests the payment display of {@code StudentCard}. */
public class StudentCardTest {

    @BeforeAll
    public static void initializeJavaFx() {
        try {
            Platform.startup(() -> { });
        } catch (IllegalStateException exception) {
            // JavaFX was already started by another test class in this JVM.
        }
    }

    @Test
    public void constructor_noPayments_displaysNone() {
        StudentCard card = new StudentCard(student(), 1, ignored -> List.of());

        assertEquals("Paid: None", paymentsText(card));
    }

    @Test
    public void constructor_withPayments_displaysMostRecentMonthsFirst() {
        Student student = student()
                .withPaymentFor(new PaymentMonth("2026-09"))
                .withPaymentFor(new PaymentMonth("2025-12"))
                .withPaymentFor(new PaymentMonth("2026-10"));

        StudentCard card = new StudentCard(student, 1, ignored -> List.of());

        assertEquals("Paid: Oct 2026, Sep 2026, Dec 2025", paymentsText(card));
        assertFalse(paymentsToggle(card).isVisible());
        assertFalse(paymentsToggle(card).isManaged());
    }

    @Test
    public void paymentsToggle_manyPayments_showsAndHidesOlderMonths() {
        Student student = student()
                .withPaymentFor(new PaymentMonth("2026-06"))
                .withPaymentFor(new PaymentMonth("2026-07"))
                .withPaymentFor(new PaymentMonth("2026-08"))
                .withPaymentFor(new PaymentMonth("2026-09"))
                .withPaymentFor(new PaymentMonth("2026-10"));

        StudentCard card = new StudentCard(student, 1, ignored -> List.of());
        Button toggle = paymentsToggle(card);

        assertEquals("Paid: Oct 2026, Sep 2026, Aug 2026", paymentsText(card));
        assertTrue(toggle.isVisible());
        assertEquals("+2 more", toggle.getText());

        toggle.fire();
        assertEquals("Paid: Oct 2026, Sep 2026, Aug 2026, Jul 2026, Jun 2026", paymentsText(card));
        assertEquals("Show less", toggle.getText());

        toggle.fire();
        assertEquals("Paid: Oct 2026, Sep 2026, Aug 2026", paymentsText(card));
        assertEquals("+2 more", toggle.getText());
    }

    @Test
    public void constructor_multipleSubjects_displaysSubjectsAlphabetically() {
        StudentCard card = new StudentCard(student(), 1, ignored -> List.of());
        FlowPane subjects = (FlowPane) card.getRoot().lookup("#subjects");

        assertEquals(List.of("Subjects:", "Mathematics", "Physics"), subjects.getChildren().stream()
                .map(node -> ((Label) node).getText()).toList());
    }

    @Test
    public void constructor_remarkPaymentsAndHighlighting_displayTogether() {
        Student student = student().withRemark(new Remark("Practice fractions"))
                .withPaymentFor(new PaymentMonth("2026-10"));
        StudentCard card = new StudentCard(student, 1, ignored -> List.of(), List.of("Lucy"));
        Label remark = (Label) card.getRoot().lookup("#remark");
        assertEquals("Remark: Practice fractions", remark.getText());
        assertTrue(remark.isVisible());
        assertTrue(remark.isManaged());
        assertEquals("Paid: Oct 2026", paymentsText(card));
        assertFalse(card.getRoot().lookupAll(".search-match").isEmpty());

        StudentCard cleared = new StudentCard(student.withRemark(Remark.EMPTY), 1, ignored -> List.of());
        Label emptyRemark = (Label) cleared.getRoot().lookup("#remark");
        assertFalse(emptyRemark.isVisible());
        assertFalse(emptyRemark.isManaged());
        assertEquals("Paid: Oct 2026", paymentsText(cleared));
    }

    private static String paymentsText(StudentCard card) {
        return ((Label) card.getRoot().lookup("#payments")).getText();
    }

    private static Button paymentsToggle(StudentCard card) {
        return (Button) card.getRoot().lookup("#paymentsToggle");
    }

    private static Student student() {
        return new Student(new Name("Lucy Lim"), new Phone("91234567"), new Email("lucy@example.com"),
                new Address("21 Happy Road"), new Level("JC 1"),
                Set.of(new Subject("Physics"), new Subject("Mathematics")));
    }
}
