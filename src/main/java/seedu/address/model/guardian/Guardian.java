package seedu.address.model.guardian;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Locale;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;

/**
 * Represents a guardian in TutorTrack.
 * Guarantees: details are present and not null, field values are validated,
 * immutable.
 */
public class Guardian {

    private final Name name;
    private final Phone phone;
    private final Email email;
    private final Address address;

    /**
     * Creates a {@code Guardian} with the given contact details.
     */
    public Guardian(Name name, Phone phone, Email email, Address address) {
        requireAllNonNull(name, phone, email, address);
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
    }

    /**
     * Returns this guardian's name.
     */
    public Name getName() {
        return name;
    }

    /**
     * Returns this guardian's phone number.
     */
    public Phone getPhone() {
        return phone;
    }

    /**
     * Returns this guardian's email address.
     */
    public Email getEmail() {
        return email;
    }

    /**
     * Returns this guardian's postal address.
     */
    public Address getAddress() {
        return address;
    }

    /**
     * Returns the canonical form used when comparing guardian names.
     * Leading and trailing whitespace is ignored, repeated whitespace is collapsed,
     * and comparison is case-insensitive.
     *
     * @param name the name to normalize
     * @return the normalized name
     */
    public static String normalizeName(String name) {
        requireNonNull(name);
        return name.trim().replaceAll("\\s+", " ").toLowerCase(Locale.ROOT);
    }

    /**
     * Returns true if both guardians have the same normalized name and phone
     * number.
     * This defines guardian identity for duplicate detection.
     *
     * @param otherGuardian the guardian to compare with
     * @return true if both guardians have the same identity
     */
    public boolean isSameGuardian(Guardian otherGuardian) {
        if (otherGuardian == this) {
            return true;
        }

        return otherGuardian != null
                && normalizeName(otherGuardian.getName().fullName).equals(normalizeName(name.fullName))
                && otherGuardian.getPhone().equals(phone);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Guardian otherGuardian)) {
            return false;
        }

        return name.equals(otherGuardian.name)
                && phone.equals(otherGuardian.phone)
                && email.equals(otherGuardian.email)
                && address.equals(otherGuardian.address);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + phone.hashCode();
        result = 31 * result + email.hashCode();
        result = 31 * result + address.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("name", name)
                .add("phone", phone)
                .add("email", email)
                .add("address", address)
                .toString();
    }
}
