package seedu.address.model;

import java.util.List;
import java.util.function.Predicate;

import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.model.guardian.Guardian;
import seedu.address.model.lesson.Lesson;
import seedu.address.model.link.StudentGuardianLink;
import seedu.address.model.student.Student;

/**
 * The API of the Model component.
 */
public interface Model {
    /** Predicate that always evaluates to true for students. */
    Predicate<Student> PREDICATE_SHOW_ALL_STUDENTS = unused -> true;

    /** Predicate that always evaluates to true for guardians. */
    Predicate<Guardian> PREDICATE_SHOW_ALL_GUARDIANS = unused -> true;

    /**
     * Returns the user prefs.
     */
    ReadOnlyUserPrefs getUserPrefs();

    /**
     * Returns the user prefs' GUI settings.
     */
    GuiSettings getGuiSettings();

    /**
     * Sets the user prefs' GUI settings.
     */
    void setGuiSettings(GuiSettings guiSettings);

    /**
     * Replaces address book data with the data in {@code addressBook}.
     */
    void setAddressBook(ReadOnlyAddressBook addressBook);

    /** Returns the AddressBook */
    ReadOnlyAddressBook getAddressBook();

    /**
     * Returns true if a student with the same identity as {@code student} exists in the address book.
     */
    boolean hasStudent(Student student);

    /**
     * Deletes the given student.
     * The student must exist in the address book.
     */
    void deleteStudent(Student target);

    /**
     * Adds the given student.
     * {@code student} must not already exist in the address book.
     */
    void addStudent(Student student);

    /**
     * Replaces the given student {@code target} with {@code editedStudent}.
     * {@code target} must exist in the address book.
     * The student identity of {@code editedStudent} must not be the same as
     * another existing student in the address book.
     */
    void setStudent(Student target, Student editedStudent);

    /** Returns an unmodifiable view of the filtered student list */
    ObservableList<Student> getFilteredStudentList();

    /**
     * Updates the filter of the filtered student list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredStudentList(Predicate<Student> predicate);

    /** Adds the given lesson. */
    void addLesson(Lesson lesson);

    /** Deletes the given lesson. */
    void deleteLesson(Lesson lesson);

    /** Returns the scheduled lessons. */
    ObservableList<Lesson> getLessonList();

    /**
     * Sorts the displayed student list by student name in ascending order.
     * The underlying student list and its storage order are not changed.
     */
    void sortStudentList();

    /**
     * Returns true if a guardian with the same identity as {@code guardian} exists in the address book.
     */
    boolean hasGuardian(Guardian guardian);

    /**
     * Deletes the given guardian.
     * The guardian must exist in the address book.
     */
    void deleteGuardian(Guardian target);

    /**
     * Adds the given guardian.
     * {@code guardian} must not already exist in the address book.
     */
    void addGuardian(Guardian guardian);

    /**
     * Replaces the given guardian {@code target} with {@code editedGuardian}.
     * {@code target} must exist in the address book.
     * The guardian identity of {@code editedGuardian} must not be the same as
     * another existing guardian in the address book.
     */
    void setGuardian(Guardian target, Guardian editedGuardian);

    /** Returns an unmodifiable view of the filtered guardian list */
    ObservableList<Guardian> getFilteredGuardianList();

    /**
     * Updates the filter of the filtered guardian list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredGuardianList(Predicate<Guardian> predicate);

    /** Returns true if the student-guardian link already exists. */
    boolean hasStudentGuardianLink(StudentGuardianLink link);

    /**
     * Returns an unmodifiable list of guardians linked to {@code student}.
     *
     * @param student the student whose linked guardians are requested
     * @return the guardians linked to the student, in link creation order
     */
    List<Guardian> getGuardiansForStudent(Student student);

    /**
     * Adds a student-guardian link. Re-adding an existing link has no effect.
     *
     * @param link the link to add
     */
    void addStudentGuardianLink(StudentGuardianLink link);

    /**
     * Removes a student-guardian link. If the link does not exist, no action is taken.
     *
     * @param link the link to remove
     */
    void removeStudentGuardianLink(StudentGuardianLink link);
}
