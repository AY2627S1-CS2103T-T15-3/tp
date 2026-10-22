package seedu.address.model.guardian;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;

/** Contains unit tests for {@code GuardianNameContainsKeywordsPredicate}. */
public class GuardianNameContainsKeywordsPredicateTest {

    @Test
    public void equals() {
        List<String> firstKeywords = List.of("first");
        List<String> secondKeywords = List.of("first", "second");
        GuardianNameContainsKeywordsPredicate first =
                new GuardianNameContainsKeywordsPredicate(firstKeywords);
        GuardianNameContainsKeywordsPredicate second =
                new GuardianNameContainsKeywordsPredicate(secondKeywords);

        // same object -> returns true
        assertTrue(first.equals(first));

        // same values -> returns true
        assertTrue(first.equals(new GuardianNameContainsKeywordsPredicate(firstKeywords)));

        // different types -> returns false
        assertFalse(first.equals(1));

        // null -> returns false
        assertFalse(first.equals(null));

        // different guardian -> returns false
        assertFalse(first.equals(second));
    }

    @Test
    public void test_nameContainsFullKeywords_returnsTrue() {
        // One keyword
        GuardianNameContainsKeywordsPredicate predicate =
                new GuardianNameContainsKeywordsPredicate(List.of("Alice"));
        assertTrue(predicate.test(guardian("Alice Bob")));

        // Multiple keywords
        assertTrue(new GuardianNameContainsKeywordsPredicate(List.of("Alice", "Bob"))
                .test(guardian("Alice Bob")));

        // Only one matching keyword
        assertTrue(new GuardianNameContainsKeywordsPredicate(List.of("Bob", "Carol"))
                .test(guardian("Alice Carol")));

        // Mixed-case keywords
        assertTrue(new GuardianNameContainsKeywordsPredicate(List.of("aLIce", "bOB"))
                .test(guardian("Alice Bob")));
    }

    @Test
    public void test_nameContainsPrefixKeywords_returnsTrue() {
        // One keyword
        GuardianNameContainsKeywordsPredicate predicate =
                new GuardianNameContainsKeywordsPredicate(List.of("Ali"));
        assertTrue(predicate.test(guardian("Alice Bob")));

        // Multiple keywords
        assertTrue(new GuardianNameContainsKeywordsPredicate(List.of("Ali", "Bo"))
                .test(guardian("Alice Bob")));

        // Only one matching keyword
        assertTrue(new GuardianNameContainsKeywordsPredicate(List.of("Bo", "Car"))
                .test(guardian("Alice Carol")));

        // Mixed-case keywords
        assertTrue(new GuardianNameContainsKeywordsPredicate(List.of("aLIc", "bO"))
                .test(guardian("Alice Bob")));
    }

    @Test
    public void test_nameDoesNotContainKeywords_returnsFalse() {
        // Zero keywords
        assertFalse(new GuardianNameContainsKeywordsPredicate(List.of()).test(guardian("Alice")));

        // Non-matching keyword
        assertFalse(new GuardianNameContainsKeywordsPredicate(List.of("Carol")).test(guardian("Alice Bob")));

        // Keywords match phone and email, but do not match name
        assertFalse(new GuardianNameContainsKeywordsPredicate(List.of("91234567", "guardian@example.com"))
                .test(guardian("Alice")));
    }

    @Test
    public void toStringMethod() {
        List<String> keywords = List.of("keyword1", "keyword2");
        GuardianNameContainsKeywordsPredicate predicate =
                new GuardianNameContainsKeywordsPredicate(keywords);
        String expected = GuardianNameContainsKeywordsPredicate.class.getCanonicalName()
                + "{keywords=" + keywords + "}";
        assertEquals(expected, predicate.toString());
    }

    @Test
    public void getKeywords_returnsOriginalKeywords() {
        List<String> keywords = List.of("Ali", "Bo");
        GuardianNameContainsKeywordsPredicate predicate =
                new GuardianNameContainsKeywordsPredicate(keywords);

        assertEquals(keywords, predicate.getKeywords());
    }

    private static Guardian guardian(String name) {
        return new Guardian(new Name(name), new Phone("91234567"), new Email("guardian@example.com"),
                new Address("12 Main Street"));
    }
}
