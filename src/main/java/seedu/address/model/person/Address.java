package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Person's address in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidAddress(String)}
 */
public class Address {

    public static final String MESSAGE_CONSTRAINTS =
            "Addresses must be 1-200 characters long, must not be blank, and may contain only letters, "
            + "digits, spaces and the punctuation , . # - / ( ) '.";

    /*
     * The first character of the address must not be a whitespace,
     * otherwise " " (a blank string) becomes a valid input.
     *
     * Requires 1-200 characters, allowing alphanumeric characters, spaces and common address punctuation.
     */
    public static final String VALIDATION_REGEX = "(?=.{1,200}\\z)[\\p{Alnum} ,.#/()'-]+";

    public final String value;

    /**
     * Constructs an {@code Address}.
     *
     * @param address A valid address.
     */
    public Address(String address) {
        requireNonNull(address);
        checkArgument(isValidAddress(address), MESSAGE_CONSTRAINTS);
        value = address;
    }

    private Address(String address, boolean alreadyValidated) {
        requireNonNull(address);
        value = address;
    }

    /**
     * Creates an address that has already been validated by a student-specific validator.
     */
    public static Address fromStudentValue(String address) {
        requireNonNull(address);
        return new Address(address, true);
    }

    /**
     * Returns true if a given string is a valid address.
     */
    public static boolean isValidAddress(String test) {
        return normalize(test).matches(VALIDATION_REGEX);
    }

    /** Normalizes an address by removing surrounding whitespace. */
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
        if (!(other instanceof Address otherAddress)) {
            return false;
        }

        return value.equals(otherAddress.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
