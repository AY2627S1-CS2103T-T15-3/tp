package seedu.address.model.student;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class PaymentMonthTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new PaymentMonth(null));
    }

    @Test
    public void constructor_invalidPaymentMonth_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, PaymentMonth.MESSAGE_CONSTRAINTS, ()
                -> new PaymentMonth("2026-13"));
    }

    @Test
    public void isValidPaymentMonth() {
        // invalid
        assertFalse(PaymentMonth.isValidPaymentMonth("")); // empty
        assertFalse(PaymentMonth.isValidPaymentMonth("2026-00")); // month too small
        assertFalse(PaymentMonth.isValidPaymentMonth("2026-13")); // month too large
        assertFalse(PaymentMonth.isValidPaymentMonth("2026-1")); // single-digit month
        assertFalse(PaymentMonth.isValidPaymentMonth("10-2026")); // wrong order
        assertFalse(PaymentMonth.isValidPaymentMonth("2026/10")); // wrong separator
        assertFalse(PaymentMonth.isValidPaymentMonth("Oct 2026")); // month name

        // valid
        assertTrue(PaymentMonth.isValidPaymentMonth("2026-01"));
        assertTrue(PaymentMonth.isValidPaymentMonth("2026-12"));
        assertTrue(PaymentMonth.isValidPaymentMonth(" 2026-10 ")); // surrounding whitespace
    }

    @Test
    public void toStringAndDisplayString() {
        PaymentMonth paymentMonth = new PaymentMonth("2026-10");
        assertEquals("2026-10", paymentMonth.toString());
        assertEquals("Oct 2026", paymentMonth.toDisplayString());
    }

    @Test
    public void compareTo() {
        assertTrue(new PaymentMonth("2025-12").compareTo(new PaymentMonth("2026-01")) < 0);
        assertEquals(0, new PaymentMonth("2026-01").compareTo(new PaymentMonth("2026-01")));
    }

    @Test
    public void equals() {
        PaymentMonth paymentMonth = new PaymentMonth("2026-10");

        assertTrue(paymentMonth.equals(new PaymentMonth("2026-10")));
        assertTrue(paymentMonth.equals(paymentMonth));
        assertFalse(paymentMonth.equals(null));
        assertFalse(paymentMonth.equals(5.0f));
        assertFalse(paymentMonth.equals(new PaymentMonth("2026-11")));
        assertFalse(paymentMonth.equals(new PaymentMonth("2025-10")));
    }
}
