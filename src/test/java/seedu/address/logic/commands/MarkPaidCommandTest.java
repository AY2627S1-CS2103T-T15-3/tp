package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.ModelManager;
import seedu.address.model.guardian.Guardian;
import seedu.address.model.link.StudentGuardianLink;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.model.student.Level;
import seedu.address.model.student.PaymentMonth;
import seedu.address.model.student.Remark;
import seedu.address.model.student.Student;
import seedu.address.model.student.Subject;

/**
 * Contains integration tests (interaction with the Model) and unit tests for {@code MarkPaidCommand}.
 */
public class MarkPaidCommandTest {

    private static final PaymentMonth OCTOBER = new PaymentMonth("2026-10");
    private static final PaymentMonth NOVEMBER = new PaymentMonth("2026-11");

    private static final Student HENRY = student("Henry Tan", "91234567");
    private static final Student LUCY = student("Lucy Lim", "98765432");

    @Test
    public void execute_validIndexUnfilteredList_success() throws Exception {
        ModelManager model = new ModelManager();
        model.addStudent(HENRY);
        model.addStudent(LUCY);

        CommandResult result = new MarkPaidCommand(Index.fromOneBased(1), OCTOBER).execute(model);

        Student expectedHenry = HENRY.withPaymentFor(OCTOBER);
        assertEquals(String.format(MarkPaidCommand.MESSAGE_SUCCESS, HENRY.getName(), "Oct 2026"),
                result.getFeedbackToUser());
        assertEquals(List.of(expectedHenry, LUCY), model.getFilteredStudentList());
        assertTrue(model.getFilteredStudentList().get(0).hasPaidFor(OCTOBER));
        assertFalse(model.getFilteredStudentList().get(0).hasPaidFor(NOVEMBER));
    }

    @Test
    public void execute_validIndexFilteredList_marksDisplayedStudent() throws Exception {
        ModelManager model = new ModelManager();
        model.addStudent(HENRY);
        model.addStudent(LUCY);
        model.updateFilteredStudentList(student -> student.equals(LUCY));

        new MarkPaidCommand(Index.fromOneBased(1), OCTOBER).execute(model);

        assertTrue(model.getAddressBook().getStudentList().contains(LUCY.withPaymentFor(OCTOBER)));
        assertTrue(model.getAddressBook().getStudentList().contains(HENRY));
    }

    @Test
    public void execute_additionalMonth_keepsEarlierPayments() throws Exception {
        ModelManager model = new ModelManager();
        model.addStudent(HENRY.withPaymentFor(OCTOBER));

        new MarkPaidCommand(Index.fromOneBased(1), NOVEMBER).execute(model);

        Student marked = model.getFilteredStudentList().get(0);
        assertEquals(List.of(OCTOBER, NOVEMBER), List.copyOf(marked.getPaidMonths()));
    }

    @Test
    public void execute_alreadyPaid_throwsCommandException() {
        ModelManager model = new ModelManager();
        model.addStudent(HENRY.withPaymentFor(OCTOBER));

        assertThrows(CommandException.class,
                String.format(MarkPaidCommand.MESSAGE_ALREADY_PAID, HENRY.getName(), "Oct 2026"), ()
                        -> new MarkPaidCommand(Index.fromOneBased(1), OCTOBER).execute(model));
    }

    @Test
    public void execute_invalidIndex_throwsCommandException() {
        ModelManager model = new ModelManager();
        model.addStudent(HENRY);

        assertThrows(CommandException.class, Messages.MESSAGE_INVALID_STUDENT_DISPLAYED_INDEX, ()
                -> new MarkPaidCommand(Index.fromOneBased(2), OCTOBER).execute(model));
    }

    @Test
    public void execute_linkedStudent_keepsGuardianLink() throws Exception {
        ModelManager model = new ModelManager();
        Guardian guardian = new Guardian(new Name("Mary Tan"), new Phone("87654321"),
                new Email("mary@example.com"), new Address("12 ABCD Ave 3"));
        model.addStudent(HENRY);
        model.addGuardian(guardian);
        model.addStudentGuardianLink(new StudentGuardianLink(HENRY, guardian));

        new MarkPaidCommand(Index.fromOneBased(1), OCTOBER).execute(model);

        assertEquals(List.of(guardian), model.getGuardiansForStudent(HENRY.withPaymentFor(OCTOBER)));
    }

    @Test
    public void execute_sortedUnpaidList_preservesRemarksAndUpdatesDisplayedIndexes() throws Exception {
        ModelManager model = new ModelManager();
        Student lucy = LUCY.withRemark(new Remark("Review algebra"));
        Student henry = HENRY.withRemark(new Remark("Practice fractions"));
        model.addStudent(lucy);
        model.addStudent(henry);
        model.sortStudentList();
        model.updateFilteredStudentList(student -> !student.hasPaidFor(OCTOBER));
        assertEquals(List.of(henry, lucy), model.getFilteredStudentList());

        new MarkPaidCommand(Index.fromOneBased(1), OCTOBER).execute(model);
        assertEquals(List.of(lucy), model.getFilteredStudentList());
        assertEquals(List.of(lucy, henry.withPaymentFor(OCTOBER)), model.getAddressBook().getStudentList());

        new MarkPaidCommand(Index.fromOneBased(1), OCTOBER).execute(model);
        assertTrue(model.getFilteredStudentList().isEmpty());
        model.updateFilteredStudentList(student -> student.hasPaidFor(OCTOBER));
        assertEquals(List.of(henry.withPaymentFor(OCTOBER), lucy.withPaymentFor(OCTOBER)),
                model.getFilteredStudentList());
    }

    @Test
    public void equals() {
        MarkPaidCommand first = new MarkPaidCommand(Index.fromOneBased(1), OCTOBER);

        assertTrue(first.equals(first));
        assertTrue(first.equals(new MarkPaidCommand(Index.fromOneBased(1), new PaymentMonth("2026-10"))));
        assertFalse(first.equals(new MarkPaidCommand(Index.fromOneBased(2), OCTOBER)));
        assertFalse(first.equals(new MarkPaidCommand(Index.fromOneBased(1), NOVEMBER)));
        assertFalse(first.equals(null));
        assertFalse(first.equals(1));
    }

    @Test
    public void toStringMethod() {
        Index targetIndex = Index.fromOneBased(1);
        MarkPaidCommand command = new MarkPaidCommand(targetIndex, OCTOBER);
        String expected = MarkPaidCommand.class.getCanonicalName()
                + "{targetIndex=" + targetIndex + ", paymentMonth=" + OCTOBER + "}";
        assertEquals(expected, command.toString());
    }

    private static Student student(String name, String phone) {
        return new Student(Name.fromStudentValue(name), Phone.fromStudentValue(phone),
                Email.fromStudentValue("student@example.com"), Address.fromStudentValue("12 ABCD Ave 3"),
                new Level("Secondary 3"), Set.of(new Subject("Mathematics")));
    }
}
