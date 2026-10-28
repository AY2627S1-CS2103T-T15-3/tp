package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Person's phone number in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidPhone(String)}
 */
public class Phone {

    public static final String MESSAGE_CONSTRAINTS =
            "Phone numbers must contain 3-15 digits and may start with '+'.";

    // Allows an optional leading '+' followed by 3-15 digits.
    public static final String VALIDATION_REGEX = "\\+?\\d{3,15}";

    public final String value;

    /**
     * Constructs a {@code Phone}.
     *
     * @param phone A valid phone number.
     */
    public Phone(String phone) {
        requireNonNull(phone);
        checkArgument(isValidPhone(phone), MESSAGE_CONSTRAINTS);
        value = phone;
    }

    private Phone(String phone, boolean alreadyValidated) {
        requireNonNull(phone);
        value = phone;
    }

    /**
     * Creates a phone number that has already been validated by a student-specific validator.
     */
    public static Phone fromStudentValue(String phone) {
        requireNonNull(phone);
        return new Phone(phone, true);
    }

    /**
     * Returns true if a given string is a valid phone number.
     */
    public static boolean isValidPhone(String test) {
        return normalize(test).matches(VALIDATION_REGEX);
    }

    /** Normalizes a phone number by removing surrounding whitespace. */
    public static String normalize(String test) {
        requireNonNull(test);
        return test.trim();
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Phone otherPhone)) {
            return false;
        }

        return value.equals(otherPhone.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
