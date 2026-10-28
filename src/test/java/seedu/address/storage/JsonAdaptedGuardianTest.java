package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.guardian.Guardian;
import seedu.address.model.guardian.GuardianFieldValidator;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;

public class JsonAdaptedGuardianTest {

    @Test
    public void constructorFromGuardian_andToModelType_success() throws Exception {
        Guardian source = guardian("Billie O'Connor", "+6598765432", "billie@example.com", "25 Happy Road");

        Guardian result = new JsonAdaptedGuardian(source).toModelType();

        assertEquals(source, result);
    }

    @Test
    public void toModelType_missingFields_throwsIllegalValueException() {
        assertThrows(IllegalValueException.class, "Guardian's Name field is missing!", () ->
                adapted(null, "92345678", "ben@example.com", "12 ABCD Ave 3").toModelType());
        assertThrows(IllegalValueException.class, "Guardian's Phone field is missing!", () ->
                adapted("Ben Tan", null, "ben@example.com", "12 ABCD Ave 3").toModelType());
        assertThrows(IllegalValueException.class, "Guardian's Email field is missing!", () ->
                adapted("Ben Tan", "92345678", null, "12 ABCD Ave 3").toModelType());
        assertThrows(IllegalValueException.class, "Guardian's Address field is missing!", () ->
                adapted("Ben Tan", "92345678", "ben@example.com", null).toModelType());
    }

    @Test
    public void toModelType_invalidFields_throwsIllegalValueException() {
        assertThrows(IllegalValueException.class, GuardianFieldValidator.NAME_MESSAGE, () ->
                adapted("Ben&Tan", "92345678", "ben@example.com", "12 ABCD Ave 3").toModelType());
        assertThrows(IllegalValueException.class, GuardianFieldValidator.PHONE_MESSAGE, () ->
                adapted("Ben Tan", "12-345", "ben@example.com", "12 ABCD Ave 3").toModelType());
        assertThrows(IllegalValueException.class, GuardianFieldValidator.EMAIL_MESSAGE, () ->
                adapted("Ben Tan", "92345678", "ben@example", "12 ABCD Ave 3").toModelType());
        assertThrows(IllegalValueException.class, GuardianFieldValidator.ADDRESS_MESSAGE, () ->
                adapted("Ben Tan", "92345678", "ben@example.com", "12 ABCD Ave @").toModelType());
    }

    private static JsonAdaptedGuardian adapted(String name, String phone, String email, String address) {
        return new JsonAdaptedGuardian(name, phone, email, address);
    }

    private static Guardian guardian(String name, String phone, String email, String address) {
        return new Guardian(Name.fromStudentValue(name), Phone.fromStudentValue(phone),
                Email.fromStudentValue(email), Address.fromStudentValue(address));
    }
}
