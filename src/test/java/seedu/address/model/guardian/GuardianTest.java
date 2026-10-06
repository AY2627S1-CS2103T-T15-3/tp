package seedu.address.model.guardian;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;

public class GuardianTest {

    private static final Name NAME = new Name("Ben Tan");
    private static final Phone PHONE = new Phone("92345678");
    private static final Email EMAIL = new Email("ben.tan@example.com");
    private static final Address ADDRESS = new Address("12 ABCD Ave 3");

    @Test
    public void constructor_nullField_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Guardian(null, PHONE, EMAIL, ADDRESS));
        assertThrows(NullPointerException.class, () -> new Guardian(NAME, null, EMAIL, ADDRESS));
        assertThrows(NullPointerException.class, () -> new Guardian(NAME, PHONE, null, ADDRESS));
        assertThrows(NullPointerException.class, () -> new Guardian(NAME, PHONE, EMAIL, null));
    }

    @Test
    public void constructor_invalidField_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> invalidNameGuardian());
        assertThrows(IllegalArgumentException.class, () -> invalidPhoneGuardian());
        assertThrows(IllegalArgumentException.class, () -> invalidEmailGuardian());
        assertThrows(IllegalArgumentException.class, () -> invalidAddressGuardian());
    }

    @Test
    public void getters_returnConstructorValues() {
        Guardian guardian = new Guardian(NAME, PHONE, EMAIL, ADDRESS);

        assertEquals(NAME, guardian.getName());
        assertEquals(PHONE, guardian.getPhone());
        assertEquals(EMAIL, guardian.getEmail());
        assertEquals(ADDRESS, guardian.getAddress());
    }

    @Test
    public void normalizeName_normalizesWhitespaceAndCase() {
        assertEquals("ben tan", Guardian.normalizeName("  Ben   Tan "));
    }

    @Test
    public void isSameGuardian_sameNormalizedNameAndPhone_returnsTrue() {
        Guardian guardian = new Guardian(NAME, PHONE, EMAIL, ADDRESS);
        Guardian equivalentGuardian = new Guardian(new Name("ben  tan"), PHONE,
                new Email("other@example.com"), new Address("25 Happy Road"));

        assertTrue(guardian.isSameGuardian(guardian));
        assertTrue(guardian.isSameGuardian(equivalentGuardian));
        assertFalse(guardian.isSameGuardian(null));
    }

    @Test
    public void isSameGuardian_differentPhone_returnsFalse() {
        Guardian guardian = new Guardian(NAME, PHONE, EMAIL, ADDRESS);
        Guardian differentGuardian = new Guardian(NAME, new Phone("98765432"), EMAIL, ADDRESS);

        assertFalse(guardian.isSameGuardian(differentGuardian));
    }

    @Test
    public void isSameGuardian_differentName_returnsFalse() {
        Guardian guardian = new Guardian(NAME, PHONE, EMAIL, ADDRESS);
        Guardian differentGuardian = new Guardian(new Name("Billie Lim"), PHONE, EMAIL, ADDRESS);

        assertFalse(guardian.isSameGuardian(differentGuardian));
    }

    @Test
    public void equals() {
        Guardian guardian = new Guardian(NAME, PHONE, EMAIL, ADDRESS);
        Guardian copy = new Guardian(NAME, PHONE, EMAIL, ADDRESS);
        Guardian differentName = new Guardian(new Name("Billie Lim"), PHONE, EMAIL, ADDRESS);
        Guardian differentPhone = new Guardian(NAME, new Phone("98765432"), EMAIL, ADDRESS);
        Guardian differentEmail = new Guardian(NAME, PHONE, new Email("other@example.com"), ADDRESS);
        Guardian differentAddress = new Guardian(NAME, PHONE, EMAIL, new Address("25 Happy Road"));

        assertTrue(guardian.equals(guardian));
        assertTrue(guardian.equals(copy));
        assertFalse(guardian.equals(null));
        assertFalse(guardian.equals(5));
        assertFalse(guardian.equals(differentName));
        assertFalse(guardian.equals(differentPhone));
        assertFalse(guardian.equals(differentEmail));
        assertFalse(guardian.equals(differentAddress));
        assertEquals(guardian.hashCode(), copy.hashCode());
    }

    @Test
    public void toStringMethod() {
        Guardian guardian = new Guardian(NAME, PHONE, EMAIL, ADDRESS);

        String expected = Guardian.class.getCanonicalName() + "{name=" + NAME + ", phone=" + PHONE
                + ", email=" + EMAIL + ", address=" + ADDRESS + "}";
        assertEquals(expected, guardian.toString());
    }

    private static Guardian invalidNameGuardian() {
        return new Guardian(new Name(""), PHONE, EMAIL, ADDRESS);
    }

    private static Guardian invalidPhoneGuardian() {
        return new Guardian(NAME, new Phone("12"), EMAIL, ADDRESS);
    }

    private static Guardian invalidEmailGuardian() {
        return new Guardian(NAME, PHONE, new Email("invalid"), ADDRESS);
    }

    private static Guardian invalidAddressGuardian() {
        return new Guardian(NAME, PHONE, EMAIL, new Address(" "));
    }
}
