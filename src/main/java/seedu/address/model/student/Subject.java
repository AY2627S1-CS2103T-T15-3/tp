package seedu.address.model.student;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.util.Locale;

/**
 * Represents a subject taught to a student.
 * Guarantees: immutable and valid as declared in {@link #isValidSubject(String)}.
 */
public class Subject {

    public static final String MESSAGE_CONSTRAINTS =
            "Subjects must be 1-50 characters long and may contain only alphanumeric characters, "
                    + "spaces, hyphens and parentheses.";
    public static final String VALIDATION_REGEX = "[\\p{Alnum} ()-]+";
    private static final int MAX_LENGTH = 50;

    public final String value;

    /**
     * Creates a {@code Subject}.
     *
     * @param subject a valid subject
     */
    public Subject(String subject) {
        requireNonNull(subject);
        String normalizedSubject = normalize(subject);
        checkArgument(isValidSubject(normalizedSubject), MESSAGE_CONSTRAINTS);
        value = normalizedSubject;
    }

    /**
     * Returns true if the given text is a valid subject.
     *
     * @param test the text to validate
     * @return true if the text is a valid subject
     */
    public static boolean isValidSubject(String test) {
        requireNonNull(test);
        String normalizedSubject = normalize(test);
        return normalizedSubject.length() <= MAX_LENGTH && normalizedSubject.matches(VALIDATION_REGEX);
    }

    /**
     * Normalizes whitespace in a subject while preserving capitalization.
     *
     * @param subject the subject to normalize
     * @return the normalized subject
     */
    public static String normalize(String subject) {
        requireNonNull(subject);
        return subject.trim().replaceAll("\\s+", " ");
    }

    /**
     * Returns a case-insensitive canonical form for comparisons.
     *
     * @return the case-insensitive normalized subject
     */
    private String canonicalValue() {
        return value.toLowerCase(Locale.ROOT);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof Subject otherSubject)) {
            return false;
        }
        return canonicalValue().equals(otherSubject.canonicalValue());
    }

    @Override
    public int hashCode() {
        return canonicalValue().hashCode();
    }

    /**
     * Returns the normalized subject.
     */
    @Override
    public String toString() {
        return value;
    }
}
