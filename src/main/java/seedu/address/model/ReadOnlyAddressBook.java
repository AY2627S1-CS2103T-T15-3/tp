package seedu.address.model;

import java.util.List;

import javafx.collections.ObservableList;
import seedu.address.model.guardian.Guardian;
import seedu.address.model.lesson.Lesson;
import seedu.address.model.link.StudentGuardianLink;
import seedu.address.model.student.Student;

/**
 * Unmodifiable view of an address book
 */
public interface ReadOnlyAddressBook {

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

    /** Returns the student-guardian links in link creation order. */
    default List<StudentGuardianLink> getStudentGuardianLinkList() {
        return List.of();
    }

    /** Returns an unmodifiable view of the scheduled lessons. */
    default ObservableList<Lesson> getLessonList() {
        return javafx.collections.FXCollections.emptyObservableList();
    }

}
