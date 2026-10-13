package seedu.address.storage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.guardian.Guardian;
import seedu.address.model.guardian.GuardianFieldValidator;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;

/** Jackson-friendly version of {@link Guardian}. */
class JsonAdaptedGuardian {
    private static final String MISSING_FIELD_MESSAGE_FORMAT = "Guardian's %s field is missing!";

    private final String name;
    private final String phone;
    private final String email;
    private final String address;

    @JsonCreator
    public JsonAdaptedGuardian(@JsonProperty("name") String name, @JsonProperty("phone") String phone,
            @JsonProperty("email") String email, @JsonProperty("address") String address) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
    }

    public JsonAdaptedGuardian(Guardian source) {
        name = source.getName().fullName;
        phone = source.getPhone().value;
        email = source.getEmail().value;
        address = source.getAddress().value;
    }

    public Guardian toModelType() throws IllegalValueException {
        if (name == null) {
            throw missing("Name");
        }
        if (!GuardianFieldValidator.isValidName(name)) {
            throw new IllegalValueException(GuardianFieldValidator.NAME_MESSAGE);
        }
        if (phone == null) {
            throw missing("Phone");
        }
        if (!GuardianFieldValidator.isValidPhone(phone)) {
            throw new IllegalValueException(GuardianFieldValidator.PHONE_MESSAGE);
        }
        if (email == null) {
            throw missing("Email");
        }
        if (!GuardianFieldValidator.isValidEmail(email)) {
            throw new IllegalValueException(GuardianFieldValidator.EMAIL_MESSAGE);
        }
        if (address == null) {
            throw missing("Address");
        }
        if (!GuardianFieldValidator.isValidAddress(address)) {
            throw new IllegalValueException(GuardianFieldValidator.ADDRESS_MESSAGE);
        }

        return new Guardian(Name.fromStudentValue(GuardianFieldValidator.normalizeName(name)),
                Phone.fromStudentValue(GuardianFieldValidator.normalizePhone(phone)),
                Email.fromStudentValue(GuardianFieldValidator.normalizeEmail(email)),
                Address.fromStudentValue(GuardianFieldValidator.normalizeAddress(address)));
    }

    private static IllegalValueException missing(String field) {
        return new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, field));
    }
}
