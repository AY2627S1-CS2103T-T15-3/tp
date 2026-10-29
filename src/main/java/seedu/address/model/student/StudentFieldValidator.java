package seedu.address.model.student;

import java.util.Locale;

import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;

/**
 * Validation and normalisation rules for fields accepted by {@code add-s}.
 */
public final class StudentFieldValidator {
    public static final String NAME_MESSAGE = Name.MESSAGE_CONSTRAINTS;
    public static final String PHONE_MESSAGE = Phone.MESSAGE_CONSTRAINTS;
    public static final String EMAIL_MESSAGE = Email.MESSAGE_CONSTRAINTS;
    public static final String ADDRESS_MESSAGE = Address.MESSAGE_CONSTRAINTS;

    private StudentFieldValidator() {
    }

    /**
     * Normalizes whitespace in a student name.
     *
     * @param value the name to normalize
     * @return the normalized name
     */
    public static String normalizeName(String value) {
        return Name.normalize(value);
    }

    /**
     * Returns whether a student name is valid.
     *
     * @param value the name to validate
     * @return true if the name is valid
     */
    public static boolean isValidName(String value) {
        String normalized = normalizeName(value);
        return Name.isValidName(normalized);
    }

    /**
     * Normalizes whitespace in a student phone number.
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
        String normalized = normalizePhone(value);
        return Phone.isValidPhone(normalized);
    }

    /**
     * Normalizes whitespace in a student email address.
     *
     * @param value the email address to normalize
     * @return the normalized email address
     */
    public static String normalizeEmail(String value) {
        return Email.normalize(value);
    }

    /**
     * Returns whether a student email address is valid.
     *
     * @param value the email address to validate
     * @return true if the email address is valid
     */
    public static boolean isValidEmail(String value) {
        String normalized = normalizeEmail(value);
        return Email.isValidEmail(normalized);
    }

    /**
     * Normalizes whitespace in a student address.
     *
     * @param value the address to normalize
     * @return the normalized address
     */
    public static String normalizeAddress(String value) {
        return Address.normalize(value);
    }

    /**
     * Returns whether a student address is valid.
     *
     * @param value the address to validate
     * @return true if the address is valid
     */
    public static boolean isValidAddress(String value) {
        String normalized = normalizeAddress(value);
        return Address.isValidAddress(normalized);
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
