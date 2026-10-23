package seedu.address.model.student;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * An immutable, single-line student remark. The empty string represents no remark.
 * Length is measured in Unicode code points after stripping surrounding whitespace.
 */

public final class Remark {
    public static final int MAX_LENGTH = 300;
    public static final String MESSAGE_CONSTRAINTS = "Remarks must be empty or contain non-whitespace text, "
            + "at most " + MAX_LENGTH + " Unicode code points, without line breaks or control characters.";
    public static final Remark EMPTY = new Remark("");

    public final String value;

    /** Creates a validated remark, preserving internal whitespace and capitalization. */
    public Remark(String remark) {
        requireNonNull(remark);
        checkArgument(isValidRemark(remark), MESSAGE_CONSTRAINTS);
        value = remark.strip();
    }

    /** Returns whether the raw input is empty or a valid nonblank, single-line remark. */
    public static boolean isValidRemark(String test) {
        requireNonNull(test);
        if (test.isEmpty()) {
            return true;
        }
        // Validate before stripping so surrounding tabs or newlines cannot disappear silently.
        // Reject any ISO control characters
        if (test.codePoints().anyMatch(codePoint -> Character.isISOControl(codePoint)
                || codePoint == 0x2028 || codePoint == 0x2029)) {
            return false;
        }
        String normalized = test.strip();
        // Check that the remark contains meaningful text and fits length requirement
        return normalized.codePoints().anyMatch(codePoint -> !Character.isWhitespace(codePoint)
                && !Character.isSpaceChar(codePoint))
                && normalized.codePointCount(0, normalized.length()) <= MAX_LENGTH;
    }

    /** Returns whether this remark represents no remark. */
    public boolean isEmpty() {
        return value.isEmpty();
    }

    @Override
    public boolean equals(Object other) {
        return other == this || other instanceof Remark otherRemark && value.equals(otherRemark.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return value;
    }
}
