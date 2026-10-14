package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.ModelManager;
import seedu.address.model.guardian.Guardian;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;

public class EditGuardianCommandTest {
    private static final Guardian BEN = guardian("Ben Tan", "92345678", "ben@example.com", "25 Happy Road");

    @Test
    public void execute_editGuardian_success() throws Exception {
        ModelManager model = new ModelManager();
        model.addGuardian(BEN);
        EditGuardianDescriptor descriptor = new EditGuardianDescriptor();
        descriptor.setPhone(new Phone("98765432"));
        Guardian edited = guardian("Ben Tan", "98765432", "ben@example.com", "25 Happy Road");

        CommandResult result = new EditGuardianCommand(Index.fromOneBased(1), descriptor).execute(model);

        assertEquals(String.format(EditGuardianCommand.MESSAGE_SUCCESS, Messages.format(edited)),
                result.getFeedbackToUser());
        assertEquals(edited, result.getGuardianToHighlight());
        assertEquals(null, result.getStudentToHighlight());
        assertEquals(java.util.List.of(edited), model.getFilteredGuardianList());
    }

    @Test
    public void execute_invalidIndex_failure() throws Exception {
        ModelManager model = new ModelManager();
        model.addGuardian(BEN);
        EditGuardianDescriptor descriptor = new EditGuardianDescriptor();
        descriptor.setPhone(new Phone("98765432"));

        assertThrows(CommandException.class, EditGuardianCommand.MESSAGE_INVALID_INDEX, () ->
                new EditGuardianCommand(Index.fromOneBased(2), descriptor).execute(model));
    }

    @Test
    public void execute_duplicateGuardian_failure() throws Exception {
        ModelManager model = new ModelManager();
        model.addGuardian(BEN);
        Guardian other = guardian("Jane Lim", "91234567", "jane@example.com", "31 Clementi Avenue");
        model.addGuardian(other);
        EditGuardianDescriptor descriptor = new EditGuardianDescriptor();
        descriptor.setName(BEN.getName());
        descriptor.setPhone(BEN.getPhone());

        assertThrows(CommandException.class, EditGuardianCommand.MESSAGE_DUPLICATE_GUARDIAN, () ->
                new EditGuardianCommand(Index.fromOneBased(2), descriptor).execute(model));
        assertEquals(java.util.List.of(BEN, other), model.getFilteredGuardianList());
    }

    @Test
    public void equals_sameValues_returnsTrue() {
        EditGuardianDescriptor descriptor = new EditGuardianDescriptor();
        descriptor.setAddress(new Address("31 Clementi Avenue"));
        EditGuardianCommand command = new EditGuardianCommand(Index.fromOneBased(1), descriptor);

        assertTrue(command.equals(command));
        assertTrue(command.equals(new EditGuardianCommand(Index.fromOneBased(1), descriptor)));
        assertEquals(command.toString(), new EditGuardianCommand(Index.fromOneBased(1), descriptor).toString());
    }

    @Test
    public void equals_differentValues_returnsFalse() {
        EditGuardianDescriptor first = new EditGuardianDescriptor();
        first.setAddress(new Address("31 Clementi Avenue"));
        EditGuardianDescriptor second = new EditGuardianDescriptor();
        second.setAddress(new Address("32 Clementi Avenue"));
        EditGuardianCommand command = new EditGuardianCommand(Index.fromOneBased(1), first);

        assertFalse(command.equals(null));
        assertFalse(command.equals(5));
        assertFalse(command.equals(new EditGuardianCommand(Index.fromOneBased(1), second)));
    }

    private static Guardian guardian(String name, String phone, String email, String address) {
        return new Guardian(Name.fromStudentValue(name), Phone.fromStudentValue(phone),
                Email.fromStudentValue(email), Address.fromStudentValue(address));
    }
}
