package seedu.address.model.student;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.LinkedHashSet;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;

public class StudentNameContainsKeywordsPredicateTest {

    @Test
    public void equals() {
        List<String> firstKeywords = List.of("first");
        List<String> secondKeywords = List.of("first", "second");
        StudentNameContainsKeywordsPredicate first = new StudentNameContainsKeywordsPredicate(firstKeywords);
        StudentNameContainsKeywordsPredicate second = new StudentNameContainsKeywordsPredicate(secondKeywords);

        // same object -> returns true
        assertTrue(first.equals(first));

        // same values -> returns true
        StudentNameContainsKeywordsPredicate firstCopy =
                new StudentNameContainsKeywordsPredicate(firstKeywords);
        assertTrue(first.equals(firstCopy));

        // different types -> returns false
        assertFalse(first.equals(1));

        // null -> returns false
        assertFalse(first.equals(null));

        // different student -> returns false
        assertFalse(first.equals(second));
    }

    @Test
    public void test_nameContainsKeywords_returnsTrue() {
        // One keyword
        StudentNameContainsKeywordsPredicate predicate =
                new StudentNameContainsKeywordsPredicate(List.of("Alice"));
        assertTrue(predicate.test(student("Alice Bob")));

        // Multiple keywords
        predicate = new StudentNameContainsKeywordsPredicate(List.of("Alice", "Bob"));
        assertTrue(predicate.test(student("Alice Bob")));

        // Only one matching keyword
        predicate = new StudentNameContainsKeywordsPredicate(List.of("Bob", "Carol"));
        assertTrue(predicate.test(student("Alice Carol")));

        // Mixed-case keywords
        predicate = new StudentNameContainsKeywordsPredicate(List.of("aLIce", "bOB"));
        assertTrue(predicate.test(student("Alice Bob")));
    }

    @Test
    public void test_nameContainsPrefixKeywords_returnsTrue() {
        // One keyword
        StudentNameContainsKeywordsPredicate predicate =
                new StudentNameContainsKeywordsPredicate(List.of("Ali"));
        assertTrue(predicate.test(student("Alice Bob")));

        // Multiple keywords
        predicate = new StudentNameContainsKeywordsPredicate(List.of("Ali", "Bo"));
        assertTrue(predicate.test(student("Alice Bob")));

        // Only one matching keyword
        predicate = new StudentNameContainsKeywordsPredicate(List.of("Bo", "Car"));
        assertTrue(predicate.test(student("Alice Carol")));

        // Mixed-case keywords
        predicate = new StudentNameContainsKeywordsPredicate(List.of("aLIc", "bO"));
        assertTrue(predicate.test(student("Alice Bob")));
    }

    @Test
    public void test_nameDoesNotContainKeywords_returnsFalse() {
        // Zero keywords
        StudentNameContainsKeywordsPredicate predicate =
                new StudentNameContainsKeywordsPredicate(List.of());
        assertFalse(predicate.test(student("Alice")));

        // Non-matching keyword
        predicate = new StudentNameContainsKeywordsPredicate(List.of("Carol"));
        assertFalse(predicate.test(student("Alice Bob")));

        // Keywords match phone and email, but do not match name
        predicate = new StudentNameContainsKeywordsPredicate(List.of("91234567", "student@example.com"));
        assertFalse(predicate.test(student("Alice")));
    }

    @Test
    public void toStringMethod() {
        List<String> keywords = List.of("keyword1", "keyword2");
        StudentNameContainsKeywordsPredicate predicate = new StudentNameContainsKeywordsPredicate(keywords);
        String expected = StudentNameContainsKeywordsPredicate.class.getCanonicalName()
                + "{keywords=" + keywords + "}";
        assertEquals(expected, predicate.toString());
    }

    private static Student student(String name) {
        return new Student(new Name(name), new Phone("91234567"), new Email("student@example.com"),
                new Address("12 Main Street"), new Level("JC 1"),
                new LinkedHashSet<>(List.of(new Subject("Mathematics"))));
    }
}
