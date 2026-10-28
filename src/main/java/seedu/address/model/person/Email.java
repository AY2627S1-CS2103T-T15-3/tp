package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.util.regex.Pattern;

/**
 * Represents a Person's email in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidEmail(String)}
 */
public class Email {

    public static final String MESSAGE_CONSTRAINTS =
            "Emails should be of the format local-part@domain, must not contain spaces, "
            + "and must have a domain such as example.com.";

    // Allows alphanumeric local-part segments separated by single periods and containing +, _, or -.
    private static final Pattern LOCAL_PART_PATTERN =
            Pattern.compile("[A-Za-z0-9+_-]+(?:\\.[A-Za-z0-9+_-]+)*");
    // Requires each domain label to start and end with an alphanumeric character.
    private static final Pattern DOMAIN_PART_PATTERN =
            Pattern.compile("[A-Za-z0-9](?:[A-Za-z0-9-]*[A-Za-z0-9])?");
    // Limits the complete email address to 1-254 characters.
    private static final Pattern LENGTH_PATTERN = Pattern.compile("(?=.{1,254}\\z).*");

    public final String value;

    /**
     * Constructs an {@code Email}.
     *
     * @param email A valid email address.
     */
    public Email(String email) {
        requireNonNull(email);
        checkArgument(isValidEmail(email), MESSAGE_CONSTRAINTS);
        value = email;
    }

    private Email(String email, boolean alreadyValidated) {
        requireNonNull(email);
        value = email;
    }

    /**
     * Creates an email that has already been validated by a student-specific validator.
     */
    public static Email fromStudentValue(String email) {
        requireNonNull(email);
        return new Email(email, true);
    }

    /**
     * Returns true if a given string is a valid email.
     */
    public static boolean isValidEmail(String test) {
        requireNonNull(test);
        if (!LENGTH_PATTERN.matcher(test).matches()) {
            return false;
        }
        int atIndex = test.indexOf('@');
        if (atIndex <= 0 || atIndex != test.lastIndexOf('@')) {
            return false;
        }
        if (!LOCAL_PART_PATTERN.matcher(test.substring(0, atIndex)).matches()) {
            return false;
        }
        String[] domainParts = test.substring(atIndex + 1).split("\\.", -1);
        if (domainParts.length < 2) {
            return false;
        }
        for (String domainPart : domainParts) {
            if (!DOMAIN_PART_PATTERN.matcher(domainPart).matches()) {
                return false;
            }
        }
        return true;
    }

    /** Normalizes an email address by removing surrounding whitespace. */
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
        if (!(other instanceof Email otherEmail)) {
            return false;
        }

        return value.equals(otherEmail.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
