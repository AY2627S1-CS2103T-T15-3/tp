package seedu.address.model.lesson;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.model.student.Level;
import seedu.address.model.student.Student;
import seedu.address.model.student.Subject;

public class LessonPredicateTest {
    private static final Student STUDENT = new Student(
            new Name("Henry Tan"), new Phone("91234567"), new Email("henry@example.com"),
            new Address("12 Main Street"), new Level("JC 1"), Set.of(new Subject("Mathematics")));
    private static final Student OTHER_STUDENT = new Student(
            new Name("Alice Tan"), new Phone("92345678"), new Email("alice@example.com"),
            new Address("12 Main Street"), new Level("JC 1"), Set.of(new Subject("Mathematics")));
    private static final LocalDate DATE = LocalDate.of(2026, 10, 25);
    private static final Lesson LESSON = new Lesson(STUDENT, DATE, LocalTime.of(14, 0), Duration.ofMinutes(60));

    @Test
    public void datePredicate_matchingDate_returnsTrue() {
        assertTrue(new LessonMatchesDatePredicate(DATE).test(LESSON));
        assertFalse(new LessonMatchesDatePredicate(DATE.plusDays(1)).test(LESSON));
    }

    @Test
    public void studentPredicate_matchingStudent_returnsTrue() {
        assertTrue(new LessonMatchesStudentPredicate(STUDENT).test(LESSON));
        assertFalse(new LessonMatchesStudentPredicate(OTHER_STUDENT).test(LESSON));
    }

    @Test
    public void predicates_nullArguments_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new LessonMatchesDatePredicate(null));
        assertThrows(NullPointerException.class, () -> new LessonMatchesStudentPredicate(null));
    }
}
