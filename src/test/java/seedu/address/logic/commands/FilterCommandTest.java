package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_NO_STUDENTS_MATCH_FILTER;
import static seedu.address.logic.Messages.MESSAGE_STUDENTS_LISTED_OVERVIEW;
import static seedu.address.testutil.Assert.assertThrows;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.guardian.Guardian;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.student.Level;
import seedu.address.model.student.Student;
import seedu.address.model.student.StudentMatchesFilterPredicate;
import seedu.address.model.student.Subject;

/**
 * Contains unit tests for {@code FilterCommand}.
 */
public class FilterCommandTest {

    private static final Student HENRY = student("Henry Tan", "91234567", "JC 1", "Mathematics", "Physics");
    private static final Student IVY = student("Ivy Lim", "92345678", "JC 1", "Mathematics");
    private static final Student JOEL = student("Joel Ng", "93456789", "Secondary 3", "Mathematics", "English");

    @Test
    public void constructor_nullPredicate_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new FilterCommand(null));
    }

    @Test
    public void execute_levelOnly_multipleStudentsFound() {
        ModelStubWithStudents model = new ModelStubWithStudents(HENRY, IVY, JOEL);

        CommandResult result = new FilterCommand(predicate("JC 1")).execute(model);

        assertEquals(String.format(MESSAGE_STUDENTS_LISTED_OVERVIEW, 2), result.getFeedbackToUser());
        assertEquals(List.of(HENRY, IVY), model.getFilteredStudentList());
    }

    @Test
    public void execute_subjectsOnly_multipleStudentsFound() {
        ModelStubWithStudents model = new ModelStubWithStudents(HENRY, IVY, JOEL);

        CommandResult result = new FilterCommand(predicate(null, "Mathematics")).execute(model);

        assertEquals(String.format(MESSAGE_STUDENTS_LISTED_OVERVIEW, 3), result.getFeedbackToUser());
        assertEquals(List.of(HENRY, IVY, JOEL), model.getFilteredStudentList());
    }

    @Test
    public void execute_levelAndSubjects_oneStudentFound() {
        ModelStubWithStudents model = new ModelStubWithStudents(HENRY, IVY, JOEL);

        CommandResult result = new FilterCommand(predicate("JC 1", "Mathematics", "Physics")).execute(model);

        assertEquals(String.format(MESSAGE_STUDENTS_LISTED_OVERVIEW, 1), result.getFeedbackToUser());
        assertEquals(List.of(HENRY), model.getFilteredStudentList());
    }

    @Test
    public void execute_noStudentMatches_noStudentFound() {
        ModelStubWithStudents model = new ModelStubWithStudents(HENRY, IVY, JOEL);

        CommandResult result = new FilterCommand(predicate("JC 2")).execute(model);

        assertEquals(MESSAGE_NO_STUDENTS_MATCH_FILTER, result.getFeedbackToUser());
        assertTrue(model.getFilteredStudentList().isEmpty());
    }

    @Test
    public void execute_emptyStudentList_noStudentFound() {
        ModelStubWithStudents model = new ModelStubWithStudents();

        CommandResult result = new FilterCommand(predicate("JC 1")).execute(model);

        assertEquals(MESSAGE_NO_STUDENTS_MATCH_FILTER, result.getFeedbackToUser());
        assertTrue(model.getFilteredStudentList().isEmpty());
    }

    @Test
    public void execute_listAlreadyFiltered_filtersFullStudentList() {
        ModelStubWithStudents model = new ModelStubWithStudents(HENRY, IVY, JOEL);
        new FilterCommand(predicate("JC 1")).execute(model);

        // JOEL is not in the currently displayed list, but is still found
        CommandResult result = new FilterCommand(predicate("Secondary 3")).execute(model);

        assertEquals(String.format(MESSAGE_STUDENTS_LISTED_OVERVIEW, 1), result.getFeedbackToUser());
        assertEquals(List.of(JOEL), model.getFilteredStudentList());
    }

    @Test
    public void equals() {
        FilterCommand filterFirstCommand = new FilterCommand(predicate("JC 1"));
        FilterCommand filterSecondCommand = new FilterCommand(predicate("JC 2"));

        // same object -> returns true
        assertTrue(filterFirstCommand.equals(filterFirstCommand));

        // same values -> returns true
        assertTrue(filterFirstCommand.equals(new FilterCommand(predicate("JC 1"))));

        // different types -> returns false
        assertFalse(filterFirstCommand.equals(1));

        // null -> returns false
        assertFalse(filterFirstCommand.equals(null));

        // different predicate -> returns false
        assertFalse(filterFirstCommand.equals(filterSecondCommand));
    }

    @Test
    public void toStringMethod() {
        StudentMatchesFilterPredicate predicate = predicate("JC 1", "Mathematics");
        FilterCommand filterCommand = new FilterCommand(predicate);
        String expected = FilterCommand.class.getCanonicalName() + "{predicate=" + predicate + "}";
        assertEquals(expected, filterCommand.toString());
    }

    /**
     * Returns a predicate for the given {@code level}, or no level if it is null, and the given {@code subjects}.
     */
    private static StudentMatchesFilterPredicate predicate(String level, String... subjects) {
        return new StudentMatchesFilterPredicate(Optional.ofNullable(level).map(Level::new), subjects(subjects));
    }

    private static Student student(String name, String phone, String level, String... subjects) {
        return new Student(new Name(name), new Phone(phone), new Email("student@example.com"),
                new Address("12 ABCD Ave 3"), new Level(level), subjects(subjects));
    }

    private static Set<Subject> subjects(String... subjects) {
        Set<Subject> subjectSet = new LinkedHashSet<>();
        for (String subject : subjects) {
            subjectSet.add(new Subject(subject));
        }
        return subjectSet;
    }

    /**
     * A default model stub that has all of the methods failing.
     */
    private class ModelStub implements Model {
        @Override
        public ReadOnlyUserPrefs getUserPrefs() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public GuiSettings getGuiSettings() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setGuiSettings(GuiSettings guiSettings) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addPerson(Person person) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setAddressBook(ReadOnlyAddressBook newData) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasPerson(Person person) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deletePerson(Person target) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setPerson(Person target, Person editedPerson) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Person> getFilteredPersonList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateFilteredPersonList(Predicate<Person> predicate) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasStudent(Student student) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deleteStudent(Student target) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addStudent(Student student) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setStudent(Student target, Student editedStudent) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Student> getFilteredStudentList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateFilteredStudentList(Predicate<Student> predicate) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasGuardian(Guardian guardian) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deleteGuardian(Guardian target) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addGuardian(Guardian guardian) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setGuardian(Guardian target, Guardian editedGuardian) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Guardian> getFilteredGuardianList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateFilteredGuardianList(Predicate<Guardian> predicate) {
            throw new AssertionError("This method should not be called.");
        }
    }

    /**
     * A Model stub that holds a filterable list of students. Guardian methods still fail, so a test fails if the
     * guardian list is touched.
     */
    private class ModelStubWithStudents extends ModelStub {
        private final FilteredList<Student> filteredStudents;

        ModelStubWithStudents(Student... students) {
            filteredStudents = new FilteredList<>(FXCollections.observableArrayList(students));
        }

        @Override
        public ObservableList<Student> getFilteredStudentList() {
            return filteredStudents;
        }

        @Override
        public void updateFilteredStudentList(Predicate<Student> predicate) {
            filteredStudents.setPredicate(predicate);
        }
    }
}
