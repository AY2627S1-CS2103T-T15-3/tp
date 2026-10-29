package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.logic.Messages.MESSAGE_GUARDIANS_LISTED_OVERVIEW;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.guardian.Guardian;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;

/** Contains integration tests for {@code ListGuardianCommand}. */
public class ListGuardianCommandTest {
    @Test
    public void execute_listGuardians_showsAllGuardians() {
        Model model = new ModelManager();
        Guardian alice = guardian("Alice Tan");
        Guardian bob = guardian("Bob Lim");
        model.addGuardian(alice);
        model.addGuardian(bob);
        model.updateFilteredGuardianList(guardian -> guardian.getName().fullName.equals("Alice Tan"));

        CommandResult result = new ListGuardianCommand().execute(model);

        assertEquals(String.format(MESSAGE_GUARDIANS_LISTED_OVERVIEW, model.getFilteredGuardianList().size()),
                result.getFeedbackToUser());
        assertEquals(CommandResult.GuardianSearchUpdate.CLEAR, result.getGuardianSearchUpdate());
        assertEquals(List.of(alice, bob), model.getFilteredGuardianList());
    }

    private static Guardian guardian(String name) {
        return new Guardian(new Name(name), new Phone("91234567"),
                new Email(name.replace(" ", "") + "@example.com"), new Address("12 Main Street"));
    }
}
