package seedu.address.storage;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.model.student.Level;
import seedu.address.model.student.PaymentMonth;
import seedu.address.model.student.Student;
import seedu.address.model.student.StudentFieldValidator;
import seedu.address.model.student.Subject;

/** Jackson-friendly version of {@link Student}. */
class JsonAdaptedStudent {
    private static final String MISSING_FIELD_MESSAGE_FORMAT = "Student's %s field is missing!";

    private final String name;
    private final String phone;
    private final String email;
    private final String address;
    private final String level;
    private final List<String> subjects = new ArrayList<>();
    private final List<String> paidMonths = new ArrayList<>();

    public JsonAdaptedStudent(String name, String phone, String email, String address, String level,
            List<String> subjects) {
        this(name, phone, email, address, level, subjects, null);
    }

    /** Data files written before payment tracking have no {@code paidMonths}, which is treated as none paid. */
    @JsonCreator
    public JsonAdaptedStudent(@JsonProperty("name") String name, @JsonProperty("phone") String phone,
            @JsonProperty("email") String email, @JsonProperty("address") String address,
            @JsonProperty("level") String level, @JsonProperty("subjects") List<String> subjects,
            @JsonProperty("paidMonths") List<String> paidMonths) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.level = level;
        if (subjects != null) {
            this.subjects.addAll(subjects);
        }
        if (paidMonths != null) {
            this.paidMonths.addAll(paidMonths);
        }
    }

    public JsonAdaptedStudent(Student source) {
        name = source.getName().fullName;
        phone = source.getPhone().value;
        email = source.getEmail().value;
        address = source.getAddress().value;
        level = source.getLevel().value;
        subjects.addAll(source.getSubjects().stream()
                .map(subject -> subject.value)
                .collect(Collectors.toList()));
        paidMonths.addAll(source.getPaidMonths().stream()
                .map(PaymentMonth::toString)
                .collect(Collectors.toList()));
    }

    public Student toModelType() throws IllegalValueException {
        if (name == null) {
            throw missing("Name");
        }
        if (!StudentFieldValidator.isValidName(name)) {
            throw new IllegalValueException(StudentFieldValidator.NAME_MESSAGE);
        }
        if (phone == null) {
            throw missing("Phone");
        }
        if (!StudentFieldValidator.isValidPhone(phone)) {
            throw new IllegalValueException(StudentFieldValidator.PHONE_MESSAGE);
        }
        if (email == null) {
            throw missing("Email");
        }
        if (!StudentFieldValidator.isValidEmail(email)) {
            throw new IllegalValueException(StudentFieldValidator.EMAIL_MESSAGE);
        }
        if (address == null) {
            throw missing("Address");
        }
        if (!StudentFieldValidator.isValidAddress(address)) {
            throw new IllegalValueException(StudentFieldValidator.ADDRESS_MESSAGE);
        }
        if (level == null) {
            throw missing("Level");
        }
        if (!StudentFieldValidator.isValidLevel(level)) {
            throw new IllegalValueException(Level.MESSAGE_CONSTRAINTS);
        }
        if (subjects.isEmpty()) {
            throw new IllegalValueException(Student.MESSAGE_NO_SUBJECTS);
        }

        Set<Subject> modelSubjects = new LinkedHashSet<>();
        for (String subject : subjects) {
            if (!StudentFieldValidator.isValidSubject(subject)) {
                throw new IllegalValueException(Subject.MESSAGE_CONSTRAINTS);
            }
            modelSubjects.add(new Subject(subject));
        }

        Set<PaymentMonth> modelPaidMonths = new LinkedHashSet<>();
        for (String paidMonth : paidMonths) {
            if (paidMonth == null || !PaymentMonth.isValidPaymentMonth(paidMonth)) {
                throw new IllegalValueException(PaymentMonth.MESSAGE_CONSTRAINTS);
            }
            modelPaidMonths.add(new PaymentMonth(paidMonth));
        }

        return new Student(Name.fromStudentValue(StudentFieldValidator.normalizeName(name)),
                Phone.fromStudentValue(StudentFieldValidator.normalizePhone(phone)),
                Email.fromStudentValue(StudentFieldValidator.normalizeEmail(email)),
                Address.fromStudentValue(StudentFieldValidator.normalizeAddress(address)),
                new Level(level), modelSubjects, modelPaidMonths);
    }

    private static IllegalValueException missing(String field) {
        return new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, field));
    }
}
