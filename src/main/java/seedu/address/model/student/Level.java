package seedu.address.model.student;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.util.Locale;

/**
 * Represents a student's academic level.
 * Guarantees: immutable and valid as declared in {@link #isValidLevel(String)}.
 */
public class Level {

    public static final String MESSAGE_CONSTRAINTS =
            "Academic level must be 1-50 characters long and may contain only alphanumeric characters and spaces.";
    public static final String VALIDATION_REGEX = "[\\p{Alnum} ]+";
    private static final int MAX_LENGTH = 50;

    public final String value;

    /**
     * Creates a {@code Level}.
     *
     * @param level a valid academic level
     */
    public Level(String level) {
        requireNonNull(level);
        String normalizedLevel = normalize(level);
        checkArgument(isValidLevel(normalizedLevel), MESSAGE_CONSTRAINTS);
        value = normalizedLevel;
    }

    /**
     * Returns true if the given text is a valid academic level.
     *
     * @param test the text to validate
     * @return true if the text is a valid academic level
     */
    public static boolean isValidLevel(String test) {
        requireNonNull(test);
        String normalizedLevel = normalize(test);
        return normalizedLevel.length() <= MAX_LENGTH && normalizedLevel.matches(VALIDATION_REGEX);
    }

    /**
     * Normalizes whitespace in an academic level while preserving capitalization.
     *
     * @param level the level to normalize
     * @return the normalized level
     */
    public static String normalize(String level) {
        requireNonNull(level);
        return level.trim().replaceAll("\\s+", " ");
    }

    /**
     * Returns a case-insensitive canonical form for comparisons.
     *
     * @return the case-insensitive normalized level
     */
    private String canonicalValue() {
        return value.toLowerCase(Locale.ROOT);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof Level otherLevel)) {
            return false;
        }
        return canonicalValue().equals(otherLevel.canonicalValue());
    }

    @Override
    public int hashCode() {
        return canonicalValue().hashCode();
    }

    /**
     * Returns the normalized academic level.
     */
    @Override
    public String toString() {
        return value;
    }
}
