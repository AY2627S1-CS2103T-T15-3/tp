package seedu.address.model.guardian;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import seedu.address.commons.util.ToStringBuilder;

/**
 * Tests that a {@code Guardian}'s {@code Name} matches any of the keywords given.
 */
public class GuardianNameContainsKeywordsPredicate implements Predicate<Guardian> {
    private final List<String> keywords;

    public GuardianNameContainsKeywordsPredicate(List<String> keywords) {
        this.keywords = List.copyOf(keywords);
    }

    public List<String> getKeywords() {
        return keywords;
    }

    @Override
    public boolean test(Guardian guardian) {
        return keywords.stream()
                .anyMatch(keyword -> Arrays.stream(guardian.getName().fullName.split("\\s+"))
                        .anyMatch(word -> word.toLowerCase().startsWith(keyword.toLowerCase())));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        // instanceof handles nulls
        if (!(other instanceof GuardianNameContainsKeywordsPredicate otherPredicate)) {
            return false;
        }
        return keywords.equals(otherPredicate.keywords);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("keywords", keywords).toString();
    }
}
