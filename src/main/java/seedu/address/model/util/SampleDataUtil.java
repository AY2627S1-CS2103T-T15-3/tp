package seedu.address.model.util;

import java.util.Set;
import java.util.stream.Collectors;

import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.guardian.Guardian;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.model.student.Level;
import seedu.address.model.student.PaymentMonth;
import seedu.address.model.student.Remark;
import seedu.address.model.student.Student;
import seedu.address.model.student.Subject;

/**
 * Contains utility methods for populating {@code AddressBook} with sample data.
 */
public class SampleDataUtil {
    public static ReadOnlyAddressBook getSampleAddressBook() {
        AddressBook addressBook = new AddressBook();

        Student alice = createStudent("Alice Tan", "91234568", "alice@example.com", "20 Hill Road",
                "Secondary 3", Set.of("Mathematics", "Physics"),
                "Bring the worksheet next lesson", Set.of("2026-10"));
        Student henry = createStudent("Henry Tan", "98765432", "henry@example.com", "12 ABCD Ave 3",
                "Secondary 3", Set.of("Mathematics"), "", Set.of());
        Student lucy = createStudent("Lucy Lim", "98889999", "lucy@example.com", "21 Happy Road",
                "JC 2", Set.of("Chemistry", "Mathematics", "Physics"), "", Set.of("2026-09", "2026-10"));
        Student sam = createStudent("Sam Lee", "91234030", "sam@example.com", "6 River Road",
                "JC 1", Set.of("Economics"), "Prefers weekend lessons", Set.of());

        addressBook.addStudent(alice);
        addressBook.addStudent(henry);
        addressBook.addStudent(lucy);
        addressBook.addStudent(sam);

        addressBook.addGuardian(createGuardian("Billie O'Connor", "6598765432", "billie@example.com",
                "30 River Valley Road"));
        addressBook.addGuardian(createGuardian("John Tan", "91234567", "john@example.com", "10 Main Street"));
        addressBook.addGuardian(createGuardian("Mary Jane", "6598765432", "mary@example.com", "25 Happy Road"));

        return addressBook;
    }

    private static Student createStudent(String name, String phone, String email, String address, String level,
            Set<String> subjects, String remark, Set<String> paidMonths) {
        Set<Subject> studentSubjects = subjects.stream().map(Subject::new).collect(Collectors.toSet());
        Set<PaymentMonth> studentPaidMonths = paidMonths.stream()
                .map(PaymentMonth::new).collect(Collectors.toSet());
        return new Student(Name.fromStudentValue(name), Phone.fromStudentValue(phone), Email.fromStudentValue(email),
                Address.fromStudentValue(address), new Level(level), studentSubjects, new Remark(remark),
                studentPaidMonths);
    }

    private static Guardian createGuardian(String name, String phone, String email, String address) {
        return new Guardian(Name.fromStudentValue(name), Phone.fromStudentValue(phone), Email.fromStudentValue(email),
                Address.fromStudentValue(address));
    }

}
