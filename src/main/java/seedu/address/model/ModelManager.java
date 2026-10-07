package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.person.Person;
import seedu.address.model.student.Student;
import seedu.address.model.guardian.Guardian;

/**
 * Represents the in-memory model of the address book data.
 */
public class ModelManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final AddressBook addressBook;
    private final UserPrefs userPrefs;
    private final FilteredList<Person> filteredPersons;

    /**
     * Initializes a ModelManager with the given addressBook and userPrefs.
     */
    public ModelManager(ReadOnlyAddressBook addressBook, ReadOnlyUserPrefs userPrefs) {
        requireAllNonNull(addressBook, userPrefs);

        logger.fine("Initializing with address book: " + addressBook + " and user prefs " + userPrefs);

        this.addressBook = new AddressBook(addressBook);
        this.userPrefs = new UserPrefs(userPrefs);
        filteredPersons = new FilteredList<>(this.addressBook.getPersonList());
    }

    public ModelManager() {
        this(new AddressBook(), new UserPrefs());
    }

    //=========== UserPrefs ==================================================================================

    @Override
    public ReadOnlyUserPrefs getUserPrefs() {
        return userPrefs;
    }

    @Override
    public GuiSettings getGuiSettings() {
        return userPrefs.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        requireNonNull(guiSettings);
        userPrefs.setGuiSettings(guiSettings);
    }

    //=========== AddressBook ================================================================================

    @Override
    public void setAddressBook(ReadOnlyAddressBook addressBook) {
        this.addressBook.resetData(addressBook);
    }

    @Override
    public ReadOnlyAddressBook getAddressBook() {
        return addressBook;
    }

    @Override
    public boolean hasPerson(Person person) {
        requireNonNull(person);
        return addressBook.hasPerson(person);
    }

    @Override
    public void deletePerson(Person target) {
        addressBook.removePerson(target);
    }

    @Override
    public void addPerson(Person person) {
        addressBook.addPerson(person);
        updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
    }

    @Override
    public void setPerson(Person target, Person editedPerson) {
        requireAllNonNull(target, editedPerson);

        addressBook.setPerson(target, editedPerson);
    }

    //=========== Filtered Person List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Person} backed by the internal list of
     * {@code addressBook}
     */
    @Override
    public ObservableList<Person> getFilteredPersonList() {
        return filteredPersons;
    }

    @Override
    public void updateFilteredPersonList(Predicate<Person> predicate) {
        requireNonNull(predicate);
        filteredPersons.setPredicate(predicate);
    }

    //=========== Student Operations ========================================================================

    @Override
    public boolean hasStudent(Student student) {
        throw new UnsupportedOperationException("Student operations are not implemented yet");
    }

    @Override
    public void deleteStudent(Student target) {
        throw new UnsupportedOperationException("Student operations are not implemented yet");
    }

    @Override
    public void addStudent(Student student) {
        throw new UnsupportedOperationException("Student operations are not implemented yet");
    }

    @Override
    public void setStudent(Student target, Student editedStudent) {
        throw new UnsupportedOperationException("Student operations are not implemented yet");
    }

    @Override
    public ObservableList<Student> getFilteredStudentList() {
        throw new UnsupportedOperationException("Student operations are not implemented yet");
    }

    @Override
    public void updateFilteredStudentList(Predicate<Student> predicate) {
        throw new UnsupportedOperationException("Student operations are not implemented yet");
    }

    //=========== Guardian Operations ========================================================================

    @Override
    public boolean hasGuardian(Guardian guardian) {
        throw new UnsupportedOperationException("Guardian operations are not implemented yet");
    }

    @Override
    public void deleteGuardian(Guardian target) {
        throw new UnsupportedOperationException("Guardian operations are not implemented yet");
    }

    @Override
    public void addGuardian(Guardian guardian) {
        throw new UnsupportedOperationException("Guardian operations are not implemented yet");
    }

    @Override
    public void setGuardian(Guardian target, Guardian editedGuardian) {
        throw new UnsupportedOperationException("Guardian operations are not implemented yet");
    }

    @Override
    public ObservableList<Guardian> getFilteredGuardianList() {
        throw new UnsupportedOperationException("Guardian operations are not implemented yet");
    }

    @Override
    public void updateFilteredGuardianList(Predicate<Guardian> predicate) {
        throw new UnsupportedOperationException("Guardian operations are not implemented yet");
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ModelManager otherModelManager)) {
            return false;
        }

        return addressBook.equals(otherModelManager.addressBook)
                && userPrefs.equals(otherModelManager.userPrefs)
                && filteredPersons.equals(otherModelManager.filteredPersons);
    }

}
