package seedu.address.model.student;

import java.util.Locale;
import java.util.regex.Pattern;

/**
 * Validation and normalisation rules for fields accepted by {@code add-s}.
 */
public final class StudentFieldValidator {
    public static final String NAME_MESSAGE =
            "Student names must be 1-100 characters long and may contain only alphanumeric characters, "
                    + "spaces, hyphens and apostrophes.";
    public static final String PHONE_MESSAGE =
            "Phone numbers must contain 3-15 digits and may start with '+'.";
    public static final String EMAIL_MESSAGE =
            "Emails should be of the format local-part@domain, must not contain spaces, "
                    + "and must have a domain such as example.com.";
    public static final String ADDRESS_MESSAGE =
            "Addresses must be 1-200 characters long, must not be blank, and may contain only letters, "
                    + "digits, spaces and the punctuation , . # - / ( ) '.";

    private static final Pattern NAME_PATTERN = Pattern.compile("[\\p{Alnum} '-]+");
    private static final Pattern PHONE_PATTERN = Pattern.compile("\\+?\\d{3,15}");
    private static final Pattern EMAIL_LOCAL_PATTERN = Pattern.compile("[A-Za-z0-9+_.-]+");
    private static final Pattern EMAIL_DOMAIN_PART_PATTERN =
            Pattern.compile("[A-Za-z0-9](?:[A-Za-z0-9-]*[A-Za-z0-9])?");
    private static final Pattern ADDRESS_PATTERN = Pattern.compile("[\\p{Alnum} ,.#/()'-]+");

    private StudentFieldValidator() {
    }

    public static String normalizeName(String value) {
        return value.trim().replaceAll("\\s+", " ");
    }

    public static boolean isValidName(String value) {
        String normalized = normalizeName(value);
        return normalized.length() >= 1 && normalized.length() <= 100
                && NAME_PATTERN.matcher(normalized).matches();
    }

    public static String normalizePhone(String value) {
        return value.trim();
    }

    public static boolean isValidPhone(String value) {
        return PHONE_PATTERN.matcher(normalizePhone(value)).matches();
    }

    public static String normalizeEmail(String value) {
        return value.trim();
    }

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

    public static String normalizeAddress(String value) {
        return value.trim();
    }

    public static boolean isValidAddress(String value) {
        String normalized = normalizeAddress(value);
        return normalized.length() >= 1 && normalized.length() <= 200
                && ADDRESS_PATTERN.matcher(normalized).matches();
    }

    public static boolean isValidLevel(String value) {
        return Level.isValidLevel(value);
    }

    public static boolean isValidSubject(String value) {
        return Subject.isValidSubject(value);
    }

    public static String canonicalName(String value) {
        return normalizeName(value).toLowerCase(Locale.ROOT);
    }
}
