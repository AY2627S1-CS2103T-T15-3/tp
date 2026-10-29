package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.ModelManager;
import seedu.address.model.guardian.Guardian;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;

public class AddGuardianCommandTest {
    private static final Guardian BEN = guardian("Ben Tan", "92345678", "ben.tan@gmail.com", "12 ABCD Ave 3");

    @Test
    public void execute_newGuardian_success() throws Exception {
        ModelManager model = new ModelManager();
        CommandResult result = new AddGuardianCommand(BEN).execute(model);

        assertEquals(String.format(AddGuardianCommand.MESSAGE_SUCCESS, Messages.format(BEN)),
                result.getFeedbackToUser());
        assertEquals(java.util.List.of(BEN), model.getFilteredGuardianList());
    }

    @Test
    public void execute_duplicateGuardian_failure() throws Exception {
        ModelManager model = new ModelManager();
        model.addGuardian(BEN);
        Guardian duplicate = guardian("  ben   tan", "92345678", "other@example.com", "25 Happy Road");

        assertThrows(CommandException.class, AddGuardianCommand.MESSAGE_DUPLICATE_GUARDIAN, () ->
                new AddGuardianCommand(duplicate).execute(model));
        assertEquals(java.util.List.of(BEN), model.getFilteredGuardianList());
    }

    @Test
    public void execute_sameNameDifferentPhone_success() throws Exception {
        ModelManager model = new ModelManager();
        model.addGuardian(BEN);
        Guardian sameName = guardian("Ben Tan", "+6598765432", "other@example.com", "25 Happy Road");

        new AddGuardianCommand(sameName).execute(model);
        assertEquals(java.util.List.of(BEN, sameName), model.getFilteredGuardianList());
    }

    @Test
    public void execute_preservesActiveSearch() throws Exception {
        Guardian other = guardian("Jane Lim", "91234567", "jane@example.com", "31 Clementi Avenue");
        ModelManager model = new ModelManager();
        model.addGuardian(BEN);
        model.addGuardian(other);
        model.updateFilteredGuardianList(guardian -> guardian.getName().fullName.equals("Ben Tan"));

        Guardian unrelated = guardian("Alice Tan", "98765432", "alice@example.com", "25 Happy Road");
        new AddGuardianCommand(unrelated).execute(model);

        assertEquals(java.util.List.of(BEN), model.getFilteredGuardianList());
    }

    @Test
    public void equals_sameGuardian_returnsTrue() {
        AddGuardianCommand command = new AddGuardianCommand(BEN);
        assertTrue(command.equals(command));
        assertTrue(command.equals(new AddGuardianCommand(BEN)));
        assertEquals(command.toString(), new AddGuardianCommand(BEN).toString());
    }

    @Test
    public void equals_differentObject_returnsFalse() {
        AddGuardianCommand command = new AddGuardianCommand(BEN);
        Guardian differentGuardian = guardian("Billie O'Connor", "98765432", "billie@example.com", "25 Happy Road");

        assertFalse(command.equals(null));
        assertFalse(command.equals(5));
        assertFalse(command.equals(new AddGuardianCommand(differentGuardian)));
    }

    private static Guardian guardian(String name, String phone, String email, String address) {
        return new Guardian(Name.fromStudentValue(name), Phone.fromStudentValue(phone),
                Email.fromStudentValue(email), Address.fromStudentValue(address));
    }
}
