package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_NO_STUDENTS_MATCH_SEARCH;
import static seedu.address.logic.Messages.MESSAGE_STUDENTS_LISTED_OVERVIEW;

import java.util.LinkedHashSet;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.model.student.Level;
import seedu.address.model.student.Student;
import seedu.address.model.student.StudentNameContainsKeywordsPredicate;
import seedu.address.model.student.Subject;

/** Contains integration tests for {@code FindStudentCommand}. */
public class FindStudentCommandTest {
    private final Model model = new ModelManager();

    @Test
    public void equals() {
        StudentNameContainsKeywordsPredicate firstPredicate =
                new StudentNameContainsKeywordsPredicate(List.of("first"));
        StudentNameContainsKeywordsPredicate secondPredicate =
                new StudentNameContainsKeywordsPredicate(List.of("second"));
        FindStudentCommand first = new FindStudentCommand(firstPredicate);
        FindStudentCommand second = new FindStudentCommand(secondPredicate);

        // same object -> returns true
        assertTrue(first.equals(first));

        // same values -> returns true
        FindStudentCommand firstCopy = new FindStudentCommand(firstPredicate);
        assertTrue(first.equals(firstCopy));

        // different types -> returns false
        assertFalse(first.equals(1));

        // null -> returns false
        assertFalse(first.equals(null));

        // different student -> returns false
        assertFalse(first.equals(second));
    }

    @Test
    public void execute_multipleKeywords_multipleStudentsFound() {
        Student alice = student("Alice Tan");
        Student bob = student("Bob Lim");
        Student carol = student("Carol Ng");
        model.addStudent(alice);
        model.addStudent(bob);
        model.addStudent(carol);

        StudentNameContainsKeywordsPredicate predicate =
                new StudentNameContainsKeywordsPredicate(List.of("tan", "ng"));
        CommandResult result = new FindStudentCommand(predicate).execute(model);

        assertEquals(String.format(MESSAGE_STUDENTS_LISTED_OVERVIEW, 2), result.getFeedbackToUser());
        assertEquals(List.of(alice, carol), model.getFilteredStudentList());
    }

    @Test
    public void execute_zeroKeywords_noStudentFound() {
        Student alice = student("Alice Tan");
        model.addStudent(alice);

        StudentNameContainsKeywordsPredicate predicate = new StudentNameContainsKeywordsPredicate(List.of());
        CommandResult result = new FindStudentCommand(predicate).execute(model);

        assertEquals(MESSAGE_NO_STUDENTS_MATCH_SEARCH, result.getFeedbackToUser());
        assertTrue(model.getFilteredStudentList().isEmpty());
    }

    @Test
    public void toStringMethod() {
        StudentNameContainsKeywordsPredicate predicate =
                new StudentNameContainsKeywordsPredicate(List.of("keyword"));
        FindStudentCommand command = new FindStudentCommand(predicate);
        String expected = FindStudentCommand.class.getCanonicalName() + "{predicate=" + predicate + "}";
        assertEquals(expected, command.toString());
    }

    /**
     * Creates a student with the given name and valid default details.
     */
    private static Student student(String name) {
        return new Student(new Name(name), new Phone("91234567"), new Email(name.replace(" ", "") + "@example.com"),
                new Address("12 Main Street"), new Level("JC 1"),
                new LinkedHashSet<>(List.of(new Subject("Mathematics"))));
    }
}
