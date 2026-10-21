package seedu.address.model.student;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

/**
 * Represents a month of a year for which a student's payment has been recorded.
 * Guarantees: immutable and valid as declared in {@link #isValidPaymentMonth(String)}.
 */
public class PaymentMonth implements Comparable<PaymentMonth> {

    public static final String MESSAGE_CONSTRAINTS =
            "Payment month must be in the format YYYY-MM with a month from 01 to 12, e.g. 2026-10.";
    public static final String VALIDATION_REGEX = "\\d{4}-\\d{2}";
    private static final DateTimeFormatter DISPLAY_FORMATTER = DateTimeFormatter.ofPattern("MMM yyyy", Locale.ENGLISH);

    public final YearMonth value;

    /**
     * Creates a {@code PaymentMonth}.
     *
     * @param paymentMonth a valid payment month in the format YYYY-MM
     */
    public PaymentMonth(String paymentMonth) {
        requireNonNull(paymentMonth);
        String trimmedPaymentMonth = paymentMonth.trim();
        checkArgument(isValidPaymentMonth(trimmedPaymentMonth), MESSAGE_CONSTRAINTS);
        value = YearMonth.parse(trimmedPaymentMonth);
    }

    /**
     * Returns true if the given text is a valid payment month.
     *
     * @param test the text to validate
     * @return true if the text is a valid payment month
     */
    public static boolean isValidPaymentMonth(String test) {
        requireNonNull(test);
        String trimmedTest = test.trim();
        if (!trimmedTest.matches(VALIDATION_REGEX)) {
            return false;
        }
        try {
            YearMonth.parse(trimmedTest);
            return true;
        } catch (DateTimeParseException exception) {
            return false;
        }
    }

    /**
     * Returns the payment month in a human-readable form, e.g. "Oct 2026".
     */
    public String toDisplayString() {
        return value.format(DISPLAY_FORMATTER);
    }

    @Override
    public int compareTo(PaymentMonth other) {
        return value.compareTo(other.value);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof PaymentMonth otherPaymentMonth)) {
            return false;
        }
        return value.equals(otherPaymentMonth.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    /**
     * Returns the payment month in the format YYYY-MM.
     */
    @Override
    public String toString() {
        return value.toString();
    }
}
