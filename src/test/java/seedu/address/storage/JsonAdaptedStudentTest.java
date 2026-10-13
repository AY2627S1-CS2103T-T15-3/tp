package seedu.address.storage;

import static seedu.address.testutil.Assert.assertThrows;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.model.student.Level;
import seedu.address.model.student.Student;
import seedu.address.model.student.StudentFieldValidator;
import seedu.address.model.student.Subject;

public class JsonAdaptedStudentTest {

    @Test
    public void constructorFromStudent_andToModelType_success() throws Exception {
        Student source = new Student(Name.fromStudentValue("Henry Tan"), Phone.fromStudentValue("91234567"),
                Email.fromStudentValue("henry@example.com"), Address.fromStudentValue("12 ABCD Ave 3"),
                new Level("Secondary 3"), Set.of(new Subject("Mathematics"), new Subject("Physics")));

        Student result = new JsonAdaptedStudent(source).toModelType();

        org.junit.jupiter.api.Assertions.assertEquals(source, result);
    }

    @Test
    public void toModelType_missingFields_throwsIllegalValueException() {
        assertThrows(IllegalValueException.class, "Student's Name field is missing!", () ->
                adapted(null, "91234567", "henry@example.com", "12 ABCD Ave 3", "JC 1", List.of("Math"))
                        .toModelType());
        assertThrows(IllegalValueException.class, "Student's Phone field is missing!", () ->
                adapted("Henry Tan", null, "henry@example.com", "12 ABCD Ave 3", "JC 1", List.of("Math"))
                        .toModelType());
        assertThrows(IllegalValueException.class, "Student's Email field is missing!", () ->
                adapted("Henry Tan", "91234567", null, "12 ABCD Ave 3", "JC 1", List.of("Math"))
                        .toModelType());
        assertThrows(IllegalValueException.class, "Student's Address field is missing!", () ->
                adapted("Henry Tan", "91234567", "henry@example.com", null, "JC 1", List.of("Math"))
                        .toModelType());
        assertThrows(IllegalValueException.class, "Student's Level field is missing!", () ->
                adapted("Henry Tan", "91234567", "henry@example.com", "12 ABCD Ave 3", null,
                        List.of("Math")).toModelType());
    }

    @Test
    public void toModelType_invalidFields_throwsIllegalValueException() {
        assertThrows(IllegalValueException.class, StudentFieldValidator.NAME_MESSAGE, () ->
                adapted("Henry&Tan", "91234567", "henry@example.com", "12 ABCD Ave 3", "JC 1",
                        List.of("Math")).toModelType());
        assertThrows(IllegalValueException.class, StudentFieldValidator.PHONE_MESSAGE, () ->
                adapted("Henry Tan", "12 345", "henry@example.com", "12 ABCD Ave 3", "JC 1",
                        List.of("Math")).toModelType());
        assertThrows(IllegalValueException.class, StudentFieldValidator.EMAIL_MESSAGE, () ->
                adapted("Henry Tan", "91234567", "henry@example", "12 ABCD Ave 3", "JC 1",
                        List.of("Math")).toModelType());
        assertThrows(IllegalValueException.class, StudentFieldValidator.ADDRESS_MESSAGE, () ->
                adapted("Henry Tan", "91234567", "henry@example.com", "12 ABCD Ave @", "JC 1",
                        List.of("Math")).toModelType());
        assertThrows(IllegalValueException.class, Level.MESSAGE_CONSTRAINTS, () ->
                adapted("Henry Tan", "91234567", "henry@example.com", "12 ABCD Ave 3", "JC-1",
                        List.of("Math")).toModelType());
        assertThrows(IllegalValueException.class, Student.MESSAGE_NO_SUBJECTS, () ->
                adapted("Henry Tan", "91234567", "henry@example.com", "12 ABCD Ave 3", "JC 1", List.of())
                        .toModelType());
        assertThrows(IllegalValueException.class, Subject.MESSAGE_CONSTRAINTS, () ->
                adapted("Henry Tan", "91234567", "henry@example.com", "12 ABCD Ave 3", "JC 1",
                        List.of("Math & Physics")).toModelType());
    }

    private static JsonAdaptedStudent adapted(String name, String phone, String email, String address, String level,
            List<String> subjects) {
        return new JsonAdaptedStudent(name, phone, email, address, level, subjects);
    }
}
