package seedu.address.model.student;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;

/**
 * Represents a student in TutorTrack.
 * Guarantees: details are present and not null, field values are validated, immutable,
 * and at least one subject is present.
 */
public class Student {

    public static final String MESSAGE_NO_SUBJECTS = "A student must have at least one subject.";

    private final Name name;
    private final Phone phone;
    private final Email email;
    private final Address address;
    private final Level level;
    private final Set<Subject> subjects;
    private final SortedSet<PaymentMonth> paidMonths;

    /**
     * Creates a {@code Student} with the given contact and academic details and no recorded payments.
     *
     * @param name the student's name
     * @param phone the student's phone number
     * @param email the student's email address
     * @param address the student's postal address
     * @param level the student's academic level
     * @param subjects the student's subjects
     */
    public Student(Name name, Phone phone, Email email, Address address, Level level, Set<Subject> subjects) {
        this(name, phone, email, address, level, subjects, Set.of());
    }

    /**
     * Creates a {@code Student} with the given contact and academic details.
     * Equivalent subjects are stored only once while preserving insertion order.
     *
     * @param name the student's name
     * @param phone the student's phone number
     * @param email the student's email address
     * @param address the student's postal address
     * @param level the student's academic level
     * @param subjects the student's subjects
     * @param paidMonths the months for which the student has paid
     */
    public Student(Name name, Phone phone, Email email, Address address, Level level, Set<Subject> subjects,
            Set<PaymentMonth> paidMonths) {
        requireAllNonNull(name, phone, email, address, level, subjects, paidMonths);
        requireAllNonNull(subjects);
        requireAllNonNull(paidMonths);
        checkArgument(StudentFieldValidator.isValidName(name.fullName), StudentFieldValidator.NAME_MESSAGE);
        checkArgument(StudentFieldValidator.isValidPhone(phone.value), StudentFieldValidator.PHONE_MESSAGE);
        checkArgument(StudentFieldValidator.isValidEmail(email.value), StudentFieldValidator.EMAIL_MESSAGE);
        checkArgument(StudentFieldValidator.isValidAddress(address.value), StudentFieldValidator.ADDRESS_MESSAGE);
        checkArgument(!subjects.isEmpty(), MESSAGE_NO_SUBJECTS);
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.level = level;
        this.subjects = Collections.unmodifiableSet(new LinkedHashSet<>(subjects));
        this.paidMonths = Collections.unmodifiableSortedSet(new TreeSet<>(paidMonths));
    }

    /**
     * Returns the student's name.
     */
    public Name getName() {
        return name;
    }

    /**
     * Returns the student's phone number.
     */
    public Phone getPhone() {
        return phone;
    }

    /**
     * Returns the student's email address.
     */
    public Email getEmail() {
        return email;
    }

    /**
     * Returns the student's postal address.
     */
    public Address getAddress() {
        return address;
    }

    /**
     * Returns the student's academic level.
     */
    public Level getLevel() {
        return level;
    }

    /**
     * Returns an immutable set of the student's subjects.
     */
    public Set<Subject> getSubjects() {
        return subjects;
    }

    /**
     * Returns an immutable set of the months the student has paid for, in chronological order.
     */
    public SortedSet<PaymentMonth> getPaidMonths() {
        return paidMonths;
    }

    /**
     * Returns true if the student has paid for the given month.
     *
     * @param paymentMonth the month to check
     * @return true if a payment is recorded for the month
     */
    public boolean hasPaidFor(PaymentMonth paymentMonth) {
        requireNonNull(paymentMonth);
        return paidMonths.contains(paymentMonth);
    }

    /**
     * Returns a copy of this student with a payment recorded for the given month.
     *
     * @param paymentMonth the month the student has paid for
     * @return the updated student
     */
    public Student withPaymentFor(PaymentMonth paymentMonth) {
        requireNonNull(paymentMonth);
        Set<PaymentMonth> updatedPaidMonths = new TreeSet<>(paidMonths);
        updatedPaidMonths.add(paymentMonth);
        return new Student(name, phone, email, address, level, subjects, updatedPaidMonths);
    }

    /**
     * Normalizes a name for student identity comparison.
     *
     * @param name the name to normalize
     * @return the case-insensitive normalized name
     */
    public static String normalizeName(String name) {
        requireNonNull(name);
        return name.trim().replaceAll("\\s+", " ").toLowerCase(Locale.ROOT);
    }

    /**
     * Returns true if both students have the same normalized name and phone number.
     * This defines student identity for duplicate detection.
     *
     * @param otherStudent the student to compare with
     * @return true if both students have the same identity
     */
    public boolean isSameStudent(Student otherStudent) {
        if (otherStudent == this) {
            return true;
        }
        return otherStudent != null
                && normalizeName(otherStudent.getName().fullName).equals(normalizeName(name.fullName))
                && otherStudent.getPhone().equals(phone);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof Student otherStudent)) {
            return false;
        }
        return name.equals(otherStudent.name)
                && phone.equals(otherStudent.phone)
                && email.equals(otherStudent.email)
                && address.equals(otherStudent.address)
                && level.equals(otherStudent.level)
                && subjects.equals(otherStudent.subjects)
                && paidMonths.equals(otherStudent.paidMonths);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, phone, email, address, level, subjects, paidMonths);
    }

    /**
     * Formats the student's state for viewing.
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("name", name)
                .add("phone", phone)
                .add("email", email)
                .add("address", address)
                .add("level", level)
                .add("subjects", subjects)
                .add("paidMonths", paidMonths)
                .toString();
    }
}
