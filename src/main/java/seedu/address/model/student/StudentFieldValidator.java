package seedu.address.model.student;

import java.util.Locale;
import java.util.regex.Pattern;

/**
 * Validation and normalisation rules for fields accepted by {@code add-s}.
 */
public final class StudentFieldValidator {
    public static final String NAME_MESSAGE =
            "Student names must be 1-100 characters long and may contain only alphanumeric characters, "
                    + "spaces, periods, hyphens, apostrophes and slashes.";
    public static final String PHONE_MESSAGE =
            "Phone numbers must contain 3-15 digits and may start with '+'.";
    public static final String EMAIL_MESSAGE =
            "Emails must use the format local-part@domain, must not contain spaces, "
                    + "must have a domain such as example.com, and must not start or end with a full stop (.) "
                    + "or contain consecutive full stops (.) in the local part.";
    public static final String ADDRESS_MESSAGE =
            "Addresses must be 1-200 characters long, must not be blank, and may contain only letters, "
                    + "digits, spaces and the punctuation , . # - / ( ) '.";

    private static final Pattern NAME_PATTERN = Pattern.compile("(?=.*[\\p{Alnum}])[\\p{Alnum} .'/-]+");
    private static final Pattern PHONE_PATTERN = Pattern.compile("\\+?\\d{3,15}");
    private static final Pattern EMAIL_LOCAL_PATTERN =
            Pattern.compile("[A-Za-z0-9+_-]+(?:\\.[A-Za-z0-9+_-]+)*");
    private static final Pattern EMAIL_DOMAIN_PART_PATTERN =
            Pattern.compile("[A-Za-z0-9](?:[A-Za-z0-9-]*[A-Za-z0-9])?");
    private static final Pattern ADDRESS_PATTERN = Pattern.compile("[\\p{Alnum} ,.#/()'-]+");

    private StudentFieldValidator() {
    }

    /**
     * Normalizes whitespace in a student name.
     *
     * @param value the name to normalize
     * @return the normalized name
     */
    public static String normalizeName(String value) {
        return value.trim().replaceAll("\\s+", " ");
    }

    /**
     * Returns whether a student name is valid.
     *
     * @param value the name to validate
     * @return true if the name is valid
     */
    public static boolean isValidName(String value) {
        String normalized = normalizeName(value);
        return normalized.length() >= 1 && normalized.length() <= 100
                && NAME_PATTERN.matcher(normalized).matches();
    }

    /**
     * Removes spaces from a student phone number and trims surrounding whitespace.
     *
     * @param value the phone number to normalize
     * @return the normalized phone number
     */
    public static String normalizePhone(String value) {
        return value.trim().replace(" ", "");
    }

    /**
     * Returns whether a student phone number is valid.
     *
     * @param value the phone number to validate
     * @return true if the phone number is valid
     */
    public static boolean isValidPhone(String value) {
        return PHONE_PATTERN.matcher(normalizePhone(value)).matches();
    }

    /**
     * Normalizes whitespace in a student email address.
     *
     * @param value the email address to normalize
     * @return the normalized email address
     */
    public static String normalizeEmail(String value) {
        return value.trim();
    }

    /**
     * Returns whether a student email address is valid.
     *
     * @param value the email address to validate
     * @return true if the email address is valid
     */
    public static boolean isValidEmail(String value) {
        String normalized = normalizeEmail(value);
        int atIndex = normalized.indexOf('@');
        if (atIndex <= 0 || atIndex != normalized.lastIndexOf('@')) {
            return false;
        }

        String localPart = normalized.substring(0, atIndex);
        String domain = normalized.substring(atIndex + 1);
        if (!EMAIL_LOCAL_PATTERN.matcher(localPart).matches()) {
            return false;
        }

        String[] domainParts = domain.split("\\.", -1);
        if (domainParts.length < 2) {
            return false;
        }
        for (String domainPart : domainParts) {
            if (!EMAIL_DOMAIN_PART_PATTERN.matcher(domainPart).matches()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Normalizes whitespace in a student address.
     *
     * @param value the address to normalize
     * @return the normalized address
     */
    public static String normalizeAddress(String value) {
        return value.trim();
    }

    /**
     * Returns whether a student address is valid.
     *
     * @param value the address to validate
     * @return true if the address is valid
     */
    public static boolean isValidAddress(String value) {
        String normalized = normalizeAddress(value);
        return normalized.length() >= 1 && normalized.length() <= 200
                && ADDRESS_PATTERN.matcher(normalized).matches();
    }

    /**
     * Returns whether a student level is valid.
     *
     * @param value the level to validate
     * @return true if the level is valid
     */
    public static boolean isValidLevel(String value) {
        return Level.isValidLevel(value);
    }

    /**
     * Returns whether a student subject is valid.
     *
     * @param value the subject to validate
     * @return true if the subject is valid
     */
    public static boolean isValidSubject(String value) {
        return Subject.isValidSubject(value);
    }

    /**
     * Returns the canonical form of a student name for comparisons.
     *
     * @param value the name to canonicalize
     * @return the canonical name
     */
    public static String canonicalName(String value) {
        return normalizeName(value).toLowerCase(Locale.ROOT);
    }
}
