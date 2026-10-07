package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import seedu.address.model.guardian.Guardian;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;

public class EditGuardianDescriptorTest {
    private static final Guardian ORIGINAL = new Guardian(
            new Name("Ben Tan"),
            new Phone("92345678"),
            new Email("ben@example.com"),
            new Address("25 Happy Road"));

    @Test
    public void createEditedGuardian_noFields_preservesOriginal() {
        EditGuardianDescriptor descriptor = new EditGuardianDescriptor();

        assertEquals(ORIGINAL, descriptor.createEditedGuardian(ORIGINAL));
        assertFalse(descriptor.isAnyFieldEdited());
    }

    @Test
    public void createEditedGuardian_oneField_preservesOtherFields() {
        EditGuardianDescriptor descriptor = new EditGuardianDescriptor();
        descriptor.setPhone(new Phone("98765432"));

        Guardian edited = descriptor.createEditedGuardian(ORIGINAL);

        assertEquals(new Phone("98765432"), edited.getPhone());
        assertEquals(ORIGINAL.getName(), edited.getName());
        assertEquals(ORIGINAL.getEmail(), edited.getEmail());
        assertEquals(ORIGINAL.getAddress(), edited.getAddress());
        assertTrue(descriptor.isAnyFieldEdited());
    }

    @Test
    public void createEditedGuardian_multipleFields_updatesAllFields() {
        EditGuardianDescriptor descriptor = new EditGuardianDescriptor();
        descriptor.setName(new Name("Ben Lim"));
        descriptor.setAddress(new Address("31 Clementi Avenue"));

        Guardian edited = descriptor.createEditedGuardian(ORIGINAL);

        assertEquals(new Name("Ben Lim"), edited.getName());
        assertEquals(new Address("31 Clementi Avenue"), edited.getAddress());
        assertEquals(ORIGINAL.getPhone(), edited.getPhone());
        assertEquals(ORIGINAL.getEmail(), edited.getEmail());
    }

    @Test
    public void settersAndGetters_allFields_returnSuppliedValues() {
        EditGuardianDescriptor descriptor = new EditGuardianDescriptor();
        Name name = new Name("Lucy Lim");
        Phone phone = new Phone("91234567");
        Email email = new Email("lucy@example.com");
        Address address = new Address("21 Happy Road");

        descriptor.setName(name);
        descriptor.setPhone(phone);
        descriptor.setEmail(email);
        descriptor.setAddress(address);

        assertEquals(name, descriptor.getName().orElseThrow());
        assertEquals(phone, descriptor.getPhone().orElseThrow());
        assertEquals(email, descriptor.getEmail().orElseThrow());
        assertEquals(address, descriptor.getAddress().orElseThrow());
    }

    @Test
    public void copyConstructor_copiesAllFields() {
        EditGuardianDescriptor original = new EditGuardianDescriptor();
        original.setName(new Name("Lucy Lim"));
        original.setPhone(new Phone("91234567"));
        original.setEmail(new Email("lucy@example.com"));
        original.setAddress(new Address("21 Happy Road"));

        EditGuardianDescriptor copy = new EditGuardianDescriptor(original);

        assertEquals(original.getName(), copy.getName());
        assertEquals(original.getPhone(), copy.getPhone());
        assertEquals(original.getEmail(), copy.getEmail());
        assertEquals(original.getAddress(), copy.getAddress());
    }

    @Test
    public void copyConstructor_nullDescriptor_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new EditGuardianDescriptor(null));
    }

    @Test
    public void createEditedGuardian_nullGuardian_throwsNullPointerException() {
        EditGuardianDescriptor descriptor = new EditGuardianDescriptor();

        assertThrows(NullPointerException.class, () -> descriptor.createEditedGuardian(null));
    }

    @Test
    public void toString_containsEditedFields() {
        EditGuardianDescriptor descriptor = new EditGuardianDescriptor();
        descriptor.setName(new Name("Lucy Lim"));

        String result = descriptor.toString();

        assertTrue(result.contains("name=Lucy Lim"));
    }
}
