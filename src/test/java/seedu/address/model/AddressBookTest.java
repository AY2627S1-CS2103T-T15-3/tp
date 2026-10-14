package seedu.address.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.model.guardian.Guardian;
import seedu.address.model.link.StudentGuardianLink;
import seedu.address.model.person.Person;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.model.student.Level;
import seedu.address.model.student.Student;
import seedu.address.model.student.Subject;
import seedu.address.testutil.PersonBuilder;

public class AddressBookTest {

    private final AddressBook addressBook = new AddressBook();

    @Test
    public void constructor() {
        assertEquals(List.of(), addressBook.getPersonList());
        assertEquals(List.of(), new AddressBookStub(List.of()).getStudentList());
        assertEquals(List.of(), new AddressBookStub(List.of()).getGuardianList());
    }

    @Test
    public void resetData_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> addressBook.resetData(null));
    }

    @Test
    public void resetData_withValidReadOnlyAddressBook_replacesData() {
        AddressBook newData = getTypicalAddressBook();
        addressBook.resetData(newData);
        assertEquals(newData, addressBook);
    }

    @Test
    public void resetData_withDuplicatePersons_throwsDuplicatePersonException() {
        // Two persons with the same identity fields
        Person editedAlice = new PersonBuilder(ALICE).withAddress(VALID_ADDRESS_BOB).withTags(VALID_TAG_HUSBAND)
                .build();
        List<Person> newPersons = List.of(ALICE, editedAlice);
        AddressBookStub newData = new AddressBookStub(newPersons);

        assertThrows(DuplicatePersonException.class, () -> addressBook.resetData(newData));
    }

    @Test
    public void hasPerson_nullPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> addressBook.hasPerson(null));
    }

    @Test
    public void hasPerson_personNotInAddressBook_returnsFalse() {
        assertFalse(addressBook.hasPerson(ALICE));
    }

    @Test
    public void hasPerson_personInAddressBook_returnsTrue() {
        addressBook.addPerson(ALICE);
        assertTrue(addressBook.hasPerson(ALICE));
    }

    @Test
    public void hasPerson_personWithSameIdentityFieldsInAddressBook_returnsTrue() {
        addressBook.addPerson(ALICE);
        Person editedAlice = new PersonBuilder(ALICE).withAddress(VALID_ADDRESS_BOB).withTags(VALID_TAG_HUSBAND)
                .build();
        assertTrue(addressBook.hasPerson(editedAlice));
    }

    @Test
    public void getPersonList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> addressBook.getPersonList().remove(0));
    }

    @Test
    public void studentOperations_addSetRemoveAndReset_success() {
        Student student = student("Henry Tan", "91234567", "henry@example.com");
        Student editedStudent = student("Henry Tan", "91234567", "other@example.com");

        assertFalse(addressBook.hasStudent(student));
        addressBook.addStudent(student);
        assertTrue(addressBook.hasStudent(student));
        assertEquals(List.of(student), addressBook.getStudentList());

        addressBook.setStudent(student, editedStudent);
        assertEquals(List.of(editedStudent), addressBook.getStudentList());
        addressBook.removeStudent(editedStudent);
        assertTrue(addressBook.getStudentList().isEmpty());

        addressBook.setStudents(List.of(student));
        assertEquals(List.of(student), addressBook.getStudentList());
    }

    @Test
    public void studentOperations_invalidInputs_throwExceptions() {
        Student student = student("Henry Tan", "91234567", "henry@example.com");
        assertThrows(NullPointerException.class, () -> addressBook.hasStudent(null));
        assertThrows(NullPointerException.class, () -> addressBook.setStudent(student, null));
        assertThrows(UnsupportedOperationException.class, () -> addressBook.getStudentList().remove(0));
    }

    @Test
    public void equals_studentDataAndDifferentTypes_returnsExpectedResult() {
        Student student = student("Henry Tan", "91234567", "henry@example.com");
        addressBook.addStudent(student);
        AddressBook copy = new AddressBook();
        copy.addStudent(student);

        assertTrue(addressBook.equals(addressBook));
        assertTrue(addressBook.equals(copy));
        assertFalse(addressBook.equals(5));
        assertEquals(addressBook.hashCode(), copy.hashCode());
    }

    @Test
    public void guardianOperations_addSetRemoveAndReset_success() {
        Guardian guardian = guardian("Ben Tan", "92345678", "ben@example.com");
        Guardian editedGuardian = guardian("Ben Tan", "92345678", "other@example.com");

        assertFalse(addressBook.hasGuardian(guardian));
        addressBook.addGuardian(guardian);
        assertTrue(addressBook.hasGuardian(guardian));
        assertEquals(List.of(guardian), addressBook.getGuardianList());

        addressBook.setGuardian(guardian, editedGuardian);
        assertEquals(List.of(editedGuardian), addressBook.getGuardianList());
        addressBook.removeGuardian(editedGuardian);
        assertTrue(addressBook.getGuardianList().isEmpty());

        addressBook.setGuardians(List.of(guardian));
        assertEquals(List.of(guardian), addressBook.getGuardianList());
    }

    @Test
    public void guardianOperations_invalidInputs_throwExceptions() {
        Guardian guardian = guardian("Ben Tan", "92345678", "ben@example.com");
        assertThrows(NullPointerException.class, () -> addressBook.hasGuardian(null));
        assertThrows(NullPointerException.class, () -> addressBook.setGuardian(guardian, null));
        assertThrows(UnsupportedOperationException.class, () -> addressBook.getGuardianList().remove(0));
    }

    @Test
    public void equals_guardianData_returnsExpectedResult() {
        Guardian guardian = guardian("Ben Tan", "92345678", "ben@example.com");
        addressBook.addGuardian(guardian);
        AddressBook copy = new AddressBook();
        copy.addGuardian(guardian);

        assertTrue(addressBook.equals(copy));
        assertEquals(addressBook.hashCode(), copy.hashCode());
    }

    @Test
    public void studentGuardianLinkOperations_addAndDetectLink_success() {
        Student student = student("Henry Tan", "91234567", "henry@example.com");
        Guardian guardian = guardian("Ben Tan", "92345678", "ben@example.com");
        StudentGuardianLink link = new StudentGuardianLink(student, guardian);

        assertFalse(addressBook.hasStudentGuardianLink(link));
        addressBook.addStudentGuardianLink(link);
        assertTrue(addressBook.hasStudentGuardianLink(link));
        assertEquals(List.of(guardian), addressBook.getGuardiansForStudent(student));
        addressBook.addStudentGuardianLink(link);
        assertEquals(List.of(guardian), addressBook.getGuardiansForStudent(student));
    }

    @Test
    public void studentGuardianLinkOperations_editStudent_preservesUpdatedLink() {
        Student student = student("Henry Tan", "91234567", "henry@example.com");
        Student editedStudent = student("Henry Tan", "99887766", "henry@example.com");
        Guardian guardian = guardian("Ben Tan", "92345678", "ben@example.com");
        addressBook.addStudent(student);
        addressBook.addGuardian(guardian);
        addressBook.addStudentGuardianLink(new StudentGuardianLink(student, guardian));

        addressBook.setStudent(student, editedStudent);

        assertEquals(List.of(guardian), addressBook.getGuardiansForStudent(editedStudent));
        assertFalse(addressBook.hasStudentGuardianLink(new StudentGuardianLink(student, guardian)));
        assertTrue(addressBook.hasStudentGuardianLink(new StudentGuardianLink(editedStudent, guardian)));
    }

    @Test
    public void studentGuardianLinkOperations_editGuardian_preservesUpdatedLink() {
        Student student = student("Henry Tan", "91234567", "henry@example.com");
        Guardian guardian = guardian("Ben Tan", "92345678", "ben@example.com");
        Guardian editedGuardian = guardian("Ben Tan", "99887766", "ben@example.com");
        addressBook.addStudent(student);
        addressBook.addGuardian(guardian);
        addressBook.addStudentGuardianLink(new StudentGuardianLink(student, guardian));

        addressBook.setGuardian(guardian, editedGuardian);

        assertEquals(List.of(editedGuardian), addressBook.getGuardiansForStudent(student));
        assertFalse(addressBook.hasStudentGuardianLink(new StudentGuardianLink(student, guardian)));
        assertTrue(addressBook.hasStudentGuardianLink(new StudentGuardianLink(student, editedGuardian)));
    }

    @Test
    public void studentGuardianLinkOperations_nullLink_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> addressBook.hasStudentGuardianLink(null));
        assertThrows(NullPointerException.class, () -> addressBook.addStudentGuardianLink(null));
        assertThrows(NullPointerException.class, () -> addressBook.getGuardiansForStudent(null));
    }

    @Test
    public void toStringMethod() {
        String expected = AddressBook.class.getCanonicalName() + "{persons=" + addressBook.getPersonList() + "}";
        assertEquals(expected, addressBook.toString());
    }

    /**
     * A stub ReadOnlyAddressBook whose persons list can violate interface constraints.
     */
    private static class AddressBookStub implements ReadOnlyAddressBook {
        private final ObservableList<Person> persons = FXCollections.observableArrayList();

        AddressBookStub(Collection<Person> persons) {
            this.persons.setAll(persons);
        }

        @Override
        public ObservableList<Person> getPersonList() {
            return persons;
        }
    }

    private static Student student(String name, String phone, String email) {
        return new Student(new seedu.address.model.person.Name(name), new seedu.address.model.person.Phone(phone),
                new seedu.address.model.person.Email(email), new seedu.address.model.person.Address("12 Main Street"),
                new Level("JC 1"), java.util.Set.of(new Subject("Mathematics")));
    }

    private static Guardian guardian(String name, String phone, String email) {
        return new Guardian(seedu.address.model.person.Name.fromStudentValue(name),
                seedu.address.model.person.Phone.fromStudentValue(phone),
                seedu.address.model.person.Email.fromStudentValue(email),
                seedu.address.model.person.Address.fromStudentValue("12 Main Street"));
    }

}
