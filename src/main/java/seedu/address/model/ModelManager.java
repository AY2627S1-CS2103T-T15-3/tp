package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.guardian.Guardian;
import seedu.address.model.lesson.Lesson;
import seedu.address.model.link.StudentGuardianLink;
import seedu.address.model.student.Student;
import seedu.address.model.student.StudentNameComparator;

/**
 * Represents the in-memory model of the address book data.
 */
public class ModelManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final AddressBook addressBook;
    private final UserPrefs userPrefs;
    private final FilteredList<Student> filteredStudents;
    private final SortedList<Student> sortedStudents;
    private final FilteredList<Guardian> filteredGuardians;
    private final FilteredList<Lesson> filteredLessons;
    private final SortedList<Lesson> sortedLessons;

    /**
     * Initializes a ModelManager with the given addressBook and userPrefs.
     */
    public ModelManager(ReadOnlyAddressBook addressBook, ReadOnlyUserPrefs userPrefs) {
        requireAllNonNull(addressBook, userPrefs);

        logger.fine("Initializing with address book: " + addressBook + " and user prefs " + userPrefs);

        this.addressBook = new AddressBook(addressBook);
        this.userPrefs = new UserPrefs(userPrefs);
        filteredStudents = new FilteredList<>(this.addressBook.getStudentList());
        sortedStudents = new SortedList<>(filteredStudents);
        filteredGuardians = new FilteredList<>(this.addressBook.getGuardianList());
        filteredLessons = new FilteredList<>(this.addressBook.getLessonList());
        sortedLessons = new SortedList<>(filteredLessons,
                Comparator.comparing(Lesson::getDate).thenComparing(Lesson::getStartTime));
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

    //=========== Student Operations ========================================================================

    @Override
    public boolean hasStudent(Student student) {
        requireNonNull(student);
        return addressBook.hasStudent(student);
    }

    @Override
    public void deleteStudent(Student target) {
        requireNonNull(target);
        addressBook.removeStudent(target);
    }

    @Override
    public void addStudent(Student student) {
        requireNonNull(student);
        addressBook.addStudent(student);
        updateFilteredStudentList(PREDICATE_SHOW_ALL_STUDENTS);
    }

    @Override
    public void setStudent(Student target, Student editedStudent) {
        requireAllNonNull(target, editedStudent);
        addressBook.setStudent(target, editedStudent);
    }

    @Override
    public ObservableList<Student> getFilteredStudentList() {
        return sortedStudents;
    }

    @Override
    public void updateFilteredStudentList(Predicate<Student> predicate) {
        requireNonNull(predicate);
        filteredStudents.setPredicate(predicate);
    }

    @Override
    public void sortStudentList() {
        sortedStudents.setComparator(new StudentNameComparator());
    }

    //=========== Lesson Operations ========================================================================

    @Override
    public void addLesson(Lesson lesson) {
        requireNonNull(lesson);
        addressBook.addLesson(lesson);
    }

    @Override
    public void deleteLesson(Lesson lesson) {
        requireNonNull(lesson);
        addressBook.removeLesson(lesson);
    }

    @Override
    public ObservableList<Lesson> getLessonList() {
        return addressBook.getLessonList();
    }

    @Override
    public ObservableList<Lesson> getFilteredLessonList() {
        return sortedLessons;
    }

    @Override
    public void updateFilteredLessonList(Predicate<Lesson> predicate) {
        requireNonNull(predicate);
        filteredLessons.setPredicate(predicate);
    }

    //=========== Guardian Operations ========================================================================

    @Override
    public boolean hasGuardian(Guardian guardian) {
        requireNonNull(guardian);
        return addressBook.hasGuardian(guardian);
    }

    @Override
    public void deleteGuardian(Guardian target) {
        requireNonNull(target);
        addressBook.removeGuardian(target);
    }

    @Override
    public void addGuardian(Guardian guardian) {
        requireNonNull(guardian);
        addressBook.addGuardian(guardian);
        updateFilteredGuardianList(PREDICATE_SHOW_ALL_GUARDIANS);
    }

    @Override
    public void setGuardian(Guardian target, Guardian editedGuardian) {
        requireAllNonNull(target, editedGuardian);
        addressBook.setGuardian(target, editedGuardian);
    }

    @Override
    public ObservableList<Guardian> getFilteredGuardianList() {
        return filteredGuardians;
    }

    @Override
    public void updateFilteredGuardianList(Predicate<Guardian> predicate) {
        requireNonNull(predicate);
        filteredGuardians.setPredicate(predicate);
    }

    @Override
    public boolean hasStudentGuardianLink(StudentGuardianLink link) {
        return addressBook.hasStudentGuardianLink(link);
    }

    @Override
    public List<Guardian> getGuardiansForStudent(Student student) {
        requireNonNull(student);
        return addressBook.getGuardiansForStudent(student);
    }

    @Override
    public void addStudentGuardianLink(StudentGuardianLink link) {
        requireNonNull(link);
        addressBook.addStudentGuardianLink(link);
    }

    @Override
    public void removeStudentGuardianLink(StudentGuardianLink link) {
        requireNonNull(link);
        addressBook.removeStudentGuardianLink(link);
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
                && filteredStudents.equals(otherModelManager.filteredStudents)
                && filteredGuardians.equals(otherModelManager.filteredGuardians);
    }

}
