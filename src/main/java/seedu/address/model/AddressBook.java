package seedu.address.model;

import static java.util.Objects.requireNonNull;

import java.util.List;

import javafx.collections.ObservableList;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.guardian.Guardian;
import seedu.address.model.guardian.UniqueGuardianList;
import seedu.address.model.link.StudentGuardianLink;
import seedu.address.model.person.Person;
import seedu.address.model.person.UniquePersonList;
import seedu.address.model.student.Student;
import seedu.address.model.student.UniqueStudentList;

/**
 * Wraps all data at the address-book level.
 * Duplicates are not allowed (by .isSamePerson comparison).
 */
public class AddressBook implements ReadOnlyAddressBook {

    private final UniquePersonList persons = new UniquePersonList();
    private final UniqueStudentList students = new UniqueStudentList();
    private final UniqueGuardianList guardians = new UniqueGuardianList();
    private final List<StudentGuardianLink> studentGuardianLinks = new java.util.ArrayList<>();

    public AddressBook() {}

    /**
     * Creates an AddressBook using the Persons in the {@code toBeCopied}
     */
    public AddressBook(ReadOnlyAddressBook toBeCopied) {
        this();
        resetData(toBeCopied);
    }

    //// list overwrite operations

    /**
     * Replaces the contents of the person list with {@code persons}.
     * {@code persons} must not contain duplicate persons.
     */
    public void setPersons(List<Person> persons) {
        this.persons.setPersons(persons);
    }

    /**
     * Resets the existing data of this {@code AddressBook} with {@code newData}.
     */
    public void resetData(ReadOnlyAddressBook newData) {
        requireNonNull(newData);

        setPersons(newData.getPersonList());
        setStudents(newData.getStudentList());
        setGuardians(newData.getGuardianList());
    }

    //// person-level operations

    /**
     * Returns true if a person with the same identity as {@code person} exists in the address book.
     */
    public boolean hasPerson(Person person) {
        requireNonNull(person);
        return persons.contains(person);
    }

    /**
     * Adds a person to the address book.
     * The person must not already exist in the address book.
     */
    public void addPerson(Person p) {
        persons.add(p);
    }

    /**
     * Replaces the given person {@code target} in the list with {@code editedPerson}.
     * {@code target} must exist in the address book.
     * The person identity of {@code editedPerson} must not be the same as another existing person in the address book.
     */
    public void setPerson(Person target, Person editedPerson) {
        requireNonNull(editedPerson);

        persons.setPerson(target, editedPerson);
    }

    /**
     * Removes {@code key} from this {@code AddressBook}.
     * {@code key} must exist in the address book.
     */
    public void removePerson(Person key) {
        persons.remove(key);
    }

    //// student-level operations

    /**
     * Returns true if a student with the same identity as {@code student} exists in the address book.
     */
    public boolean hasStudent(Student student) {
        requireNonNull(student);
        return students.contains(student);
    }

    /**
     * Adds a student to the address book.
     * The student must not already exist in the address book.
     */
    public void addStudent(Student student) {
        students.add(student);
    }

    /**
     * Replaces the given student {@code target} with {@code editedStudent}.
     */
    public void setStudent(Student target, Student editedStudent) {
        requireNonNull(editedStudent);
        students.setStudent(target, editedStudent);
    }

    /**
     * Removes {@code student} from the address book.
     */
    public void removeStudent(Student student) {
        students.remove(student);
    }

    /**
     * Replaces the contents of the student list with {@code students}.
     */
    public void setStudents(List<Student> students) {
        this.students.setStudents(students);
    }

    //// guardian-level operations

    /** Returns true if a guardian with the same identity exists in the address book. */
    public boolean hasGuardian(Guardian guardian) {
        requireNonNull(guardian);
        return guardians.contains(guardian);
    }

    /** Adds a guardian to the address book. */
    public void addGuardian(Guardian guardian) {
        guardians.add(guardian);
    }

    /** Replaces the given guardian with {@code editedGuardian}. */
    public void setGuardian(Guardian target, Guardian editedGuardian) {
        requireNonNull(editedGuardian);
        guardians.setGuardian(target, editedGuardian);
    }

    /** Removes {@code guardian} from the address book. */
    public void removeGuardian(Guardian guardian) {
        guardians.remove(guardian);
    }

    /** Replaces the contents of the guardian list with {@code guardians}. */
    public void setGuardians(List<Guardian> guardians) {
        this.guardians.setGuardians(guardians);
    }

    /** Returns true if the given student-guardian link exists. */
    public boolean hasStudentGuardianLink(StudentGuardianLink link) {
        return studentGuardianLinks.contains(requireNonNull(link));
    }

    /** Adds a student-guardian link. */
    public void addStudentGuardianLink(StudentGuardianLink link) {
        studentGuardianLinks.add(requireNonNull(link));
    }

    //// util methods

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("persons", persons)
                .toString();
    }

    @Override
    public ObservableList<Person> getPersonList() {
        return persons.asUnmodifiableObservableList();
    }

    @Override
    public ObservableList<Student> getStudentList() {
        return students.asUnmodifiableObservableList();
    }

    @Override
    public ObservableList<Guardian> getGuardianList() {
        return guardians.asUnmodifiableObservableList();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AddressBook otherAddressBook)) {
            return false;
        }

        return persons.equals(otherAddressBook.persons)
                && students.equals(otherAddressBook.students)
                && guardians.equals(otherAddressBook.guardians)
                && studentGuardianLinks.equals(otherAddressBook.studentGuardianLinks);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(persons, students, guardians, studentGuardianLinks);
    }
}
