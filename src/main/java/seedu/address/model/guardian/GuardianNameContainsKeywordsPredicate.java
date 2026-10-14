package seedu.address.model.guardian;

import java.util.List;
import java.util.function.Predicate;

import seedu.address.commons.util.StringUtil;
import seedu.address.commons.util.ToStringBuilder;

/**
 * Tests that a {@code Guardian}'s {@code Name} matches any of the keywords given.
 */
public class GuardianNameContainsKeywordsPredicate implements Predicate<Guardian> {
    private final List<String> keywords;

    public GuardianNameContainsKeywordsPredicate(List<String> keywords) {
        this.keywords = keywords;
    }

    @Override
    public boolean test(Guardian guardian) {
        return keywords.stream()
                .anyMatch(keyword -> StringUtil.containsWordIgnoreCase(guardian.getName().fullName, keyword));
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
