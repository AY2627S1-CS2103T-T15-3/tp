package seedu.address.model.guardian;

import seedu.address.model.student.StudentFieldValidator;

/** Validation and normalisation rules for fields accepted by {@code add-g}. */
public final class GuardianFieldValidator {
    public static final String NAME_MESSAGE =
            "Guardian names must be 1-100 characters long and may contain only alphanumeric characters, "
                    + "spaces, hyphens, apostrophes and slashes.";
    public static final String PHONE_MESSAGE = StudentFieldValidator.PHONE_MESSAGE;
    public static final String EMAIL_MESSAGE = StudentFieldValidator.EMAIL_MESSAGE;
    public static final String ADDRESS_MESSAGE = StudentFieldValidator.ADDRESS_MESSAGE;

    private GuardianFieldValidator() {
    }

    /** Normalizes whitespace in a guardian name. */
    public static String normalizeName(String value) {
        return StudentFieldValidator.normalizeName(value);
    }

    /** Returns whether a guardian name is valid. */
    public static boolean isValidName(String value) {
        return StudentFieldValidator.isValidName(value);
    }

    /** Normalizes whitespace in a guardian phone number. */
    public static String normalizePhone(String value) {
        return StudentFieldValidator.normalizePhone(value);
    }

    /** Returns whether a guardian phone number is valid. */
    public static boolean isValidPhone(String value) {
        return StudentFieldValidator.isValidPhone(value);
    }

    /** Normalizes whitespace in a guardian email address. */
    public static String normalizeEmail(String value) {
        return StudentFieldValidator.normalizeEmail(value);
    }

    /** Returns whether a guardian email address is valid. */
    public static boolean isValidEmail(String value) {
        return StudentFieldValidator.isValidEmail(value);
    }

    /** Normalizes whitespace in a guardian address. */
    public static String normalizeAddress(String value) {
        return StudentFieldValidator.normalizeAddress(value);
    }

    /** Returns whether a guardian address is valid. */
    public static boolean isValidAddress(String value) {
        return StudentFieldValidator.isValidAddress(value);
    }
}
