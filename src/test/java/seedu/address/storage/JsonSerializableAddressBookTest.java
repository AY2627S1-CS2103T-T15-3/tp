package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.AddressBook;
import seedu.address.model.guardian.Guardian;
import seedu.address.model.lesson.Lesson;
import seedu.address.model.link.StudentGuardianLink;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.model.student.Level;
import seedu.address.model.student.Student;
import seedu.address.model.student.Subject;

public class JsonSerializableAddressBookTest {

    @Test
    public void toModelType_duplicateStudents_throwsIllegalValueException() {
        JsonAdaptedStudent first = new JsonAdaptedStudent("Henry Tan", "91234567", "henry@example.com",
                "12 ABCD Ave 3", "JC 1", java.util.List.of("Mathematics"));
        JsonAdaptedStudent duplicate = new JsonAdaptedStudent("henry  tan", "91234567", "other@example.com",
                "25 Happy Road", "JC 1", java.util.List.of("Physics"));
        JsonSerializableAddressBook data = new JsonSerializableAddressBook(java.util.List.of(first, duplicate));

        assertThrows(IllegalValueException.class, "Students list contains duplicate student(s).",
                data::toModelType);
    }

    @Test
    public void toModelType_duplicateGuardians_throwsIllegalValueException() {
        JsonAdaptedGuardian first = new JsonAdaptedGuardian("Ben Tan", "92345678", "ben@example.com",
                "12 ABCD Ave 3");
        JsonAdaptedGuardian duplicate = new JsonAdaptedGuardian("ben  tan", "92345678", "other@example.com",
                "25 Happy Road");
        JsonSerializableAddressBook data = new JsonSerializableAddressBook(java.util.List.of(),
                java.util.List.of(first, duplicate));

        assertThrows(IllegalValueException.class, JsonSerializableAddressBook.MESSAGE_DUPLICATE_GUARDIAN,
                data::toModelType);
    }

    @Test
    public void constructorAndToModelType_links_preserveOrder() throws Exception {
        AddressBook source = new AddressBook();
        Student student = student("Lucy Lim", "91234567");
        Guardian firstGuardian = guardian("Ben Tan", "92345678");
        Guardian secondGuardian = guardian("Billie Ng", "94567890");
        source.addStudent(student);
        source.addGuardian(firstGuardian);
        source.addGuardian(secondGuardian);
        source.addStudentGuardianLink(new StudentGuardianLink(student, firstGuardian));
        source.addStudentGuardianLink(new StudentGuardianLink(student, secondGuardian));

        AddressBook restored = new JsonSerializableAddressBook(source).toModelType();

        assertEquals(source, restored);
        assertEquals(List.of(firstGuardian, secondGuardian), restored.getGuardiansForStudent(student));
    }

    @Test
    public void constructorAndToModelType_lessons_preserveData() throws Exception {
        AddressBook source = new AddressBook();
        Student student = student("Lucy Lim", "91234567");
        Lesson lesson = new Lesson(student, LocalDate.of(2026, 10, 25), LocalTime.of(14, 0),
                Duration.ofMinutes(60));
        source.addStudent(student);
        source.addLesson(lesson);

        AddressBook restored = new JsonSerializableAddressBook(source).toModelType();

        assertEquals(source, restored);
        assertEquals(List.of(lesson), restored.getLessonList());
    }

    @Test
    public void toModelType_missingLinksField_loadsWithNoLinks() throws Exception {
        JsonSerializableAddressBook data = new JsonSerializableAddressBook(java.util.List.of());
        assertTrue(data.toModelType().getStudentGuardianLinkList().isEmpty());
    }

    @Test
    public void toModelType_linkWithMissingContact_throwsIllegalValueException() {
        JsonAdaptedStudent student = new JsonAdaptedStudent("Lucy Lim", "91234567", "lucy@example.com",
                "21 Happy Road", "JC 1", List.of("Mathematics"));
        JsonAdaptedGuardian guardian = new JsonAdaptedGuardian("Ben Tan", "92345678", "ben@example.com",
                "25 Happy Road");
        JsonAdaptedStudentGuardianLink missingLink = new JsonAdaptedStudentGuardianLink(
                "Unknown Student", "99999999", "Ben Tan", "92345678");
        JsonSerializableAddressBook data = new JsonSerializableAddressBook(List.of(student),
                List.of(guardian), List.of(missingLink));

        assertThrows(IllegalValueException.class, JsonSerializableAddressBook.MESSAGE_INVALID_LINK,
                data::toModelType);
    }

    @Test
    public void toModelType_lessonWithMissingStudent_throwsIllegalValueException() {
        JsonAdaptedLesson lesson = new JsonAdaptedLesson(
                "Unknown Student", "99999999", "2026-10-25", "14:00", 60);
        JsonSerializableAddressBook data = new JsonSerializableAddressBook(
                List.of(), List.of(), List.of(), List.of(lesson));

        assertThrows(IllegalValueException.class, JsonSerializableAddressBook.MESSAGE_INVALID_LESSON,
                data::toModelType);
    }

    @Test
    public void toModelType_duplicateLessons_throwsIllegalValueException() {
        JsonAdaptedStudent student = new JsonAdaptedStudent("Lucy Lim", "91234567", "lucy@example.com",
                "21 Happy Road", "JC 1", List.of("Mathematics"));
        JsonAdaptedLesson first = new JsonAdaptedLesson(
                "Lucy Lim", "91234567", "2026-10-25", "14:00", 60);
        JsonAdaptedLesson duplicate = new JsonAdaptedLesson(
                "Lucy Lim", "91234567", "2026-10-25", "14:00", 60);
        JsonSerializableAddressBook data = new JsonSerializableAddressBook(
                List.of(student), List.of(), List.of(), List.of(first, duplicate));

        assertThrows(IllegalValueException.class, JsonSerializableAddressBook.MESSAGE_DUPLICATE_LESSON,
                data::toModelType);
    }

    @Test
    public void toModelType_overlappingLessons_throwsIllegalValueException() {
        JsonAdaptedStudent student = new JsonAdaptedStudent("Lucy Lim", "91234567", "lucy@example.com",
                "21 Happy Road", "JC 1", List.of("Mathematics"));
        JsonAdaptedLesson first = new JsonAdaptedLesson(
                "Lucy Lim", "91234567", "2026-10-25", "14:00", 60);
        JsonAdaptedLesson overlapping = new JsonAdaptedLesson(
                "Lucy Lim", "91234567", "2026-10-25", "14:30", 60);
        JsonSerializableAddressBook data = new JsonSerializableAddressBook(
                List.of(student), List.of(), List.of(), List.of(first, overlapping));

        assertThrows(IllegalValueException.class, JsonSerializableAddressBook.MESSAGE_OVERLAPPING_LESSON,
                data::toModelType);
    }

    private static Student student(String name, String phone) {
        return new Student(new Name(name), new Phone(phone), new Email("lucy@example.com"),
                new Address("21 Happy Road"), new Level("JC 1"), Set.of(new Subject("Mathematics")));
    }

    private static Guardian guardian(String name, String phone) {
        return new Guardian(new Name(name), new Phone(phone), new Email("guardian@example.com"),
                new Address("25 Happy Road"));
    }

}
