package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_GUARDIANS_LISTED_OVERVIEW;
import static seedu.address.logic.Messages.MESSAGE_NO_GUARDIANS_MATCH_SEARCH;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.guardian.Guardian;
import seedu.address.model.guardian.GuardianNameContainsKeywordsPredicate;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;

/** Contains integration tests for {@code FindGuardianCommand}. */
public class FindGuardianCommandTest {
    private final Model model = new ModelManager();

    @Test
    public void equals() {
        GuardianNameContainsKeywordsPredicate firstPredicate =
                new GuardianNameContainsKeywordsPredicate(List.of("first"));
        GuardianNameContainsKeywordsPredicate secondPredicate =
                new GuardianNameContainsKeywordsPredicate(List.of("second"));
        FindGuardianCommand first = new FindGuardianCommand(firstPredicate);
        FindGuardianCommand second = new FindGuardianCommand(secondPredicate);

        // same object -> returns true
        assertTrue(first.equals(first));

        // same values -> returns true
        assertTrue(first.equals(new FindGuardianCommand(firstPredicate)));

        // different types -> returns false
        assertFalse(first.equals(1));

        // null -> returns false
        assertFalse(first.equals(null));

        // different guardian -> returns false
        assertFalse(first.equals(second));
    }

    @Test
    public void execute_multipleKeywords_multipleGuardiansFound() {
        Guardian alice = guardian("Alice Tan");
        Guardian bob = guardian("Bob Lim");
        Guardian carol = guardian("Carol Ng");
        model.addGuardian(alice);
        model.addGuardian(bob);
        model.addGuardian(carol);

        CommandResult result = new FindGuardianCommand(
                new GuardianNameContainsKeywordsPredicate(List.of("tan", "ng"))).execute(model);

        assertEquals(String.format(MESSAGE_GUARDIANS_LISTED_OVERVIEW, 2), result.getFeedbackToUser());
        assertEquals(List.of(alice, carol), model.getFilteredGuardianList());
    }

    @Test
    public void execute_zeroKeywords_noGuardianFound() {
        model.addGuardian(guardian("Alice Tan"));
        CommandResult result = new FindGuardianCommand(
                new GuardianNameContainsKeywordsPredicate(List.of())).execute(model);
        assertEquals(MESSAGE_NO_GUARDIANS_MATCH_SEARCH, result.getFeedbackToUser());
        assertTrue(model.getFilteredGuardianList().isEmpty());
    }

    @Test
    public void toStringMethod() {
        GuardianNameContainsKeywordsPredicate predicate =
                new GuardianNameContainsKeywordsPredicate(List.of("keyword"));
        FindGuardianCommand command = new FindGuardianCommand(predicate);
        String expected = FindGuardianCommand.class.getCanonicalName() + "{predicate=" + predicate + "}";
        assertEquals(expected, command.toString());
    }

    private static Guardian guardian(String name) {
        return new Guardian(new Name(name), new Phone("91234567"),
                new Email(name.replace(" ", "") + "@example.com"), new Address("12 Main Street"));
    }
}
