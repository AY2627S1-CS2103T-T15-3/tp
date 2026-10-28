package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Person's name in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidName(String)}
 */
public class Name {

    public static final String MESSAGE_CONSTRAINTS =
            "Student names must be 1-100 characters long and may contain only alphanumeric characters, "
                    + "spaces, hyphens, apostrophes and slashes.";

    /*
     * The first character of the name must not be a whitespace,
     * otherwise " " (a blank string) becomes a valid input.
     *
     * Requires 1-100 characters, allowing alphanumeric characters, spaces, apostrophes, slashes and hyphens.
     */
    public static final String VALIDATION_REGEX = "(?=.{1,100}\\z)[\\p{Alnum} '/-]+";

    public final String fullName;

    /**
     * Constructs a {@code Name}.
     *
     * @param name A valid name.
     */
    public Name(String name) {
        requireNonNull(name);
        checkArgument(isValidName(name), MESSAGE_CONSTRAINTS);
        fullName = name;
    }

    private Name(String name, boolean alreadyValidated) {
        requireNonNull(name);
        fullName = name;
    }

    /**
     * Creates a name that has already been validated by a student-specific validator.
     */
    public static Name fromStudentValue(String name) {
        requireNonNull(name);
        return new Name(name, true);
    }

    /**
     * Normalizes whitespace in a student name.
     *
     * @param value the name to normalize
     * @return the normalized name
     */
    public static String normalize(String value) {
        return value.trim().replaceAll("\\s+", " ");
    }

    /**
     * Returns true if a given string is a valid name.
     */
    public static boolean isValidName(String test) {
        return normalize(test).matches(VALIDATION_REGEX);
    }


    @Override
    public String toString() {
        return fullName;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Name otherName)) {
            return false;
        }

        return fullName.equals(otherName.fullName);
    }

    @Override
    public int hashCode() {
        return fullName.hashCode();
    }

}
