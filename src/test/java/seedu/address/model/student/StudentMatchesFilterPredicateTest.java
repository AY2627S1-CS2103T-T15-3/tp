package seedu.address.model.student;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;

public class StudentMatchesFilterPredicateTest {

    private static final Student STUDENT = new Student(new Name("Henry Tan"), new Phone("91234567"),
            new Email("henry@example.com"), new Address("12 ABCD Ave 3"), new Level("JC 1"),
            subjects("Mathematics", "Physics"));
    private static final Student PAID_STUDENT = STUDENT.withPaymentFor(new PaymentMonth("2026-10"));

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new StudentMatchesFilterPredicate(null, Set.of()));
        assertThrows(NullPointerException.class, () -> new StudentMatchesFilterPredicate(Optional.empty(), null));
        assertThrows(NullPointerException.class, () -> new StudentMatchesFilterPredicate(Optional.empty(), Set.of(),
                null, Optional.empty()));
        assertThrows(NullPointerException.class, () -> new StudentMatchesFilterPredicate(Optional.empty(), Set.of(),
                Optional.empty(), null));
    }

    @Test
    public void equals() {
        StudentMatchesFilterPredicate firstPredicate = predicate("JC 1", "Mathematics");
        StudentMatchesFilterPredicate secondPredicate = predicate("JC 2", "Mathematics");

        // same object -> returns true
        assertTrue(firstPredicate.equals(firstPredicate));

        // same values -> returns true
        assertTrue(firstPredicate.equals(predicate("JC 1", "Mathematics")));

        // same values ignoring case and repeated spaces -> returns true
        assertTrue(firstPredicate.equals(predicate("jc   1", "MATHEMATICS")));

        // different types -> returns false
        assertFalse(firstPredicate.equals(1));

        // null -> returns false
        assertFalse(firstPredicate.equals(null));

        // different level -> returns false
        assertFalse(firstPredicate.equals(secondPredicate));

        // no level -> returns false
        assertFalse(firstPredicate.equals(predicate(null, "Mathematics")));

        // different subjects -> returns false
        assertFalse(firstPredicate.equals(predicate("JC 1", "Physics")));
        assertFalse(firstPredicate.equals(predicate("JC 1", "Mathematics", "Physics")));

        // different payment months -> returns false
        assertFalse(paymentPredicate("2026-10", null).equals(paymentPredicate("2026-11", null)));
        assertFalse(paymentPredicate(null, "2026-10").equals(paymentPredicate(null, "2026-11")));
        assertFalse(paymentPredicate("2026-10", null).equals(paymentPredicate(null, "2026-10")));

        // same payment months -> returns true
        assertTrue(paymentPredicate("2026-10", "2026-11").equals(paymentPredicate("2026-10", "2026-11")));
    }

    @Test
    public void test_paidMonth() {
        // paid for the month -> returns true
        assertTrue(paymentPredicate("2026-10", null).test(PAID_STUDENT));

        // not paid for the month -> returns false
        assertFalse(paymentPredicate("2026-11", null).test(PAID_STUDENT));
        assertFalse(paymentPredicate("2026-10", null).test(STUDENT));
    }

    @Test
    public void test_unpaidMonth() {
        // not paid for the month -> returns true
        assertTrue(paymentPredicate(null, "2026-10").test(STUDENT));
        assertTrue(paymentPredicate(null, "2026-11").test(PAID_STUDENT));

        // paid for the month -> returns false
        assertFalse(paymentPredicate(null, "2026-10").test(PAID_STUDENT));
    }

    @Test
    public void test_paidAndUnpaidMonths() {
        // paid for the first month but not the second -> returns true
        assertTrue(paymentPredicate("2026-10", "2026-11").test(PAID_STUDENT));

        // same month for both -> returns false
        assertFalse(paymentPredicate("2026-10", "2026-10").test(PAID_STUDENT));
    }

    @Test
    public void test_levelSubjectsAndPayment() {
        StudentMatchesFilterPredicate predicate = new StudentMatchesFilterPredicate(Optional.of(new Level("JC 1")),
                subjects("Mathematics"), Optional.empty(), Optional.of(new PaymentMonth("2026-10")));

        assertTrue(predicate.test(STUDENT));
        assertFalse(predicate.test(PAID_STUDENT));
    }

    @Test
    public void test_levelOnly_returnsTrue() {
        // identical level
        assertTrue(predicate("JC 1").test(STUDENT));

        // different capitalisation
        assertTrue(predicate("jc 1").test(STUDENT));

        // repeated spaces
        assertTrue(predicate("JC    1").test(STUDENT));
    }

    @Test
    public void test_levelOnly_returnsFalse() {
        // different level
        assertFalse(predicate("Secondary 3").test(STUDENT));

        // level that only starts with the student's level
        assertFalse(predicate("JC 10").test(STUDENT));

        // level that the student's level only starts with
        assertFalse(predicate("JC").test(STUDENT));
    }

    @Test
    public void test_subjectsOnly_returnsTrue() {
        // one subject
        assertTrue(predicate(null, "Mathematics").test(STUDENT));

        // all subjects, in a different order
        assertTrue(predicate(null, "Physics", "Mathematics").test(STUDENT));

        // different capitalisation and repeated spaces
        assertTrue(predicate(null, "  mathematics ").test(STUDENT));
    }

    @Test
    public void test_subjectsOnly_returnsFalse() {
        // subject the student does not take
        assertFalse(predicate(null, "Chemistry").test(STUDENT));

        // student takes only some of the subjects
        assertFalse(predicate(null, "Mathematics", "Chemistry").test(STUDENT));

        // subject that only starts with one of the student's subjects
        assertFalse(predicate(null, "Mathematics (H2)").test(STUDENT));
    }

    @Test
    public void test_levelAndSubjects() {
        // both match -> returns true
        assertTrue(predicate("JC 1", "Mathematics", "Physics").test(STUDENT));

        // level matches, subject does not -> returns false
        assertFalse(predicate("JC 1", "Chemistry").test(STUDENT));

        // subject matches, level does not -> returns false
        assertFalse(predicate("JC 2", "Mathematics").test(STUDENT));
    }

    @Test
    public void getFilterDescription_returnsReadableCriteria() {
        StudentMatchesFilterPredicate predicate = new StudentMatchesFilterPredicate(Optional.of(new Level("JC 1")),
                subjects("Physics", "Mathematics"), Optional.of(new PaymentMonth("2026-10")),
                Optional.of(new PaymentMonth("2026-11")));

        assertEquals("JC 1, Mathematics, Physics, Paid: Oct 2026, Unpaid: Nov 2026",
                predicate.getFilterDescription());
    }

    @Test
    public void test_noCriteria_returnsTrue() {
        assertTrue(predicate(null).test(STUDENT));
    }

    @Test
    public void toStringMethod() {
        Optional<Level> level = Optional.of(new Level("JC 1"));
        Set<Subject> subjects = Set.of(new Subject("Mathematics"));
        StudentMatchesFilterPredicate predicate = new StudentMatchesFilterPredicate(level, subjects);

        String expected = StudentMatchesFilterPredicate.class.getCanonicalName()
                + "{level=" + level + ", subjects=" + subjects
                + ", paidMonth=" + Optional.empty() + ", unpaidMonth=" + Optional.empty() + "}";
        assertEquals(expected, predicate.toString());
    }

    /**
     * Returns a predicate for the given {@code level}, or no level if it is null, and the given {@code subjects}.
     */
    private static StudentMatchesFilterPredicate predicate(String level, String... subjects) {
        return new StudentMatchesFilterPredicate(Optional.ofNullable(level).map(Level::new), subjects(subjects));
    }

    /**
     * Returns a predicate with only the given {@code paidMonth} and {@code unpaidMonth}, each absent if null.
     */
    private static StudentMatchesFilterPredicate paymentPredicate(String paidMonth, String unpaidMonth) {
        return new StudentMatchesFilterPredicate(Optional.empty(), Set.of(),
                Optional.ofNullable(paidMonth).map(PaymentMonth::new),
                Optional.ofNullable(unpaidMonth).map(PaymentMonth::new));
    }

    private static Set<Subject> subjects(String... subjects) {
        Set<Subject> subjectSet = new LinkedHashSet<>();
        for (String subject : subjects) {
            subjectSet.add(new Subject(subject));
        }
        return subjectSet;
    }
}
