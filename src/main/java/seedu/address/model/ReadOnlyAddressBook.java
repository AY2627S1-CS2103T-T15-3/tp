package seedu.address.model;

import javafx.collections.ObservableList;
import seedu.address.model.guardian.Guardian;
import seedu.address.model.person.Person;
import seedu.address.model.student.Student;

/**
 * Unmodifiable view of an address book
 */
public interface ReadOnlyAddressBook {

    /**
     * Returns an unmodifiable view of the persons list.
     * This list will not contain any duplicate persons.
     */
    ObservableList<Person> getPersonList();

    /**
     * Returns an unmodifiable view of the students list.
     * A default empty list keeps legacy read-only address-book implementations source-compatible.
     */
    default ObservableList<Student> getStudentList() {
        return javafx.collections.FXCollections.emptyObservableList();
    }

    /**
     * Returns an unmodifiable view of the guardians list.
     * A default empty list keeps legacy read-only address-book implementations source-compatible.
     */
    default ObservableList<Guardian> getGuardianList() {
        return javafx.collections.FXCollections.emptyObservableList();
    }

}
