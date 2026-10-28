package seedu.address.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.GuiSettings;
import seedu.address.model.guardian.Guardian;
import seedu.address.model.lesson.Lesson;
import seedu.address.model.link.StudentGuardianLink;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.model.student.Level;
import seedu.address.model.student.Remark;
import seedu.address.model.student.Student;
import seedu.address.model.student.Subject;

public class ModelManagerTest {

    private ModelManager modelManager = new ModelManager();

    @Test
    public void constructor() {
        assertEquals(new UserPrefs(), modelManager.getUserPrefs());
        assertEquals(new GuiSettings(), modelManager.getGuiSettings());
        assertEquals(new AddressBook(), new AddressBook(modelManager.getAddressBook()));
    }

    @Test
    public void constructor_validUserPrefs_copiesUserPrefs() {
        UserPrefs userPrefs = new UserPrefs();
        userPrefs.setGuiSettings(new GuiSettings(1, 2, 3, 4));
        modelManager = new ModelManager(new AddressBook(), userPrefs);
        assertEquals(userPrefs, modelManager.getUserPrefs());

        // Modifying userPrefs should not modify modelManager's userPrefs
        UserPrefs oldUserPrefs = new UserPrefs(userPrefs);
        userPrefs.setGuiSettings(new GuiSettings(5, 6, 7, 8));
        assertEquals(oldUserPrefs, modelManager.getUserPrefs());
    }

    @Test
    public void setGuiSettings_nullGuiSettings_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setGuiSettings(null));
    }

    @Test
    public void setGuiSettings_validGuiSettings_setsGuiSettings() {
        GuiSettings guiSettings = new GuiSettings(1, 2, 3, 4);
        modelManager.setGuiSettings(guiSettings);
        assertEquals(guiSettings, modelManager.getGuiSettings());
    }

    @Test
    public void studentOperations_addFilterSetAndDelete_success() {
        Student student = student("Henry Tan", "91234567", "henry@example.com");
        Student editedStudent = student("Henry Tan", "91234567", "other@example.com");

        assertFalse(modelManager.hasStudent(student));
        modelManager.addStudent(student);
        assertTrue(modelManager.hasStudent(student));
        assertEquals(List.of(student), modelManager.getFilteredStudentList());

        modelManager.updateFilteredStudentList(candidate -> false);
        assertTrue(modelManager.getFilteredStudentList().isEmpty());
        modelManager.updateFilteredStudentList(Model.PREDICATE_SHOW_ALL_STUDENTS);

        modelManager.setStudent(student, editedStudent);
        assertEquals(List.of(editedStudent), modelManager.getFilteredStudentList());
        modelManager.deleteStudent(editedStudent);
        assertTrue(modelManager.getFilteredStudentList().isEmpty());
    }

    @Test
    public void studentOperations_nullInputs_throwNullPointerException() {
        Student student = student("Henry Tan", "91234567", "henry@example.com");
        assertThrows(NullPointerException.class, () -> modelManager.hasStudent(null));
        assertThrows(NullPointerException.class, () -> modelManager.addStudent(null));
        assertThrows(NullPointerException.class, () -> modelManager.deleteStudent(null));
        assertThrows(NullPointerException.class, () -> modelManager.setStudent(null, student));
        assertThrows(NullPointerException.class, () -> modelManager.setStudent(student, null));
        assertThrows(NullPointerException.class, () -> modelManager.updateFilteredStudentList(null));
    }

    @Test
    public void sortStudentList_sortsByNormalizedName_preservesStoredOrder() {
        Student zoe = student("zoe Tan", "91234567", "zoe@example.com");
        Student alice = student("Alice Tan", "92345678", "alice@example.com");
        Student bob = student("bob Tan", "93456789", "bob@example.com");
        Student duplicateAlice = student("alice tan", "94567890", "duplicate@example.com");

        modelManager.addStudent(zoe);
        modelManager.addStudent(alice);
        modelManager.addStudent(bob);
        modelManager.addStudent(duplicateAlice);

        modelManager.sortStudentList();

        assertEquals(List.of(alice, duplicateAlice, bob, zoe), modelManager.getFilteredStudentList());
        assertEquals(List.of(zoe, alice, bob, duplicateAlice), modelManager.getAddressBook().getStudentList());
    }

    @Test
    public void sortStudentList_emptyList_remainsEmpty() {
        modelManager.sortStudentList();

        assertTrue(modelManager.getFilteredStudentList().isEmpty());
    }

    @Test
    public void sortStudentList_withFilter_sortsDisplayedStudentsOnly() {
        Student zoe = student("Zoe Tan", "91234567", "zoe@example.com");
        Student alice = student("Alice Tan", "92345678", "alice@example.com");
        Student bob = student("Bob Tan", "93456789", "bob@example.com");

        modelManager.addStudent(zoe);
        modelManager.addStudent(alice);
        modelManager.addStudent(bob);
        modelManager.updateFilteredStudentList(candidate -> candidate != bob);

        modelManager.sortStudentList();

        assertEquals(List.of(alice, zoe), modelManager.getFilteredStudentList());
        assertEquals(List.of(zoe, alice, bob), modelManager.getAddressBook().getStudentList());

        modelManager.updateFilteredStudentList(candidate -> candidate != zoe);
        assertEquals(List.of(alice, bob), modelManager.getFilteredStudentList());
    }

    @Test
    public void sortStudentList_updatesAfterStudentChanges() {
        Student charlie = student("Charlie Tan", "91234567", "charlie@example.com");
        Student alice = student("Alice Tan", "92345678", "alice@example.com");
        Student bob = student("Bob Tan", "93456789", "bob@example.com");
        Student aaron = student("Aaron Tan", "91234567", "aaron@example.com");

        modelManager.addStudent(charlie);
        modelManager.addStudent(alice);
        modelManager.sortStudentList();
        assertEquals(List.of(alice, charlie), modelManager.getFilteredStudentList());

        modelManager.addStudent(bob);
        assertEquals(List.of(alice, bob, charlie), modelManager.getFilteredStudentList());

        modelManager.setStudent(charlie, aaron);
        assertEquals(List.of(aaron, alice, bob), modelManager.getFilteredStudentList());

        modelManager.deleteStudent(bob);
        assertEquals(List.of(aaron, alice), modelManager.getFilteredStudentList());
    }

    @Test
    public void lessonOperations_addAndDelete_success() {
        Student student = student("Henry Tan", "91234567", "henry@example.com");
        Lesson lesson = new Lesson(student, LocalDate.of(2026, 10, 25), LocalTime.of(14, 0),
                Duration.ofMinutes(60));

        modelManager.addStudent(student);
        modelManager.addLesson(lesson);

        assertEquals(List.of(lesson), modelManager.getLessonList());

        modelManager.deleteLesson(lesson);

        assertTrue(modelManager.getLessonList().isEmpty());
    }

    @Test
    public void lessonOperations_nullInputs_throwNullPointerException() {
        assertThrows(NullPointerException.class, () ->
                modelManager.addLesson(null));
        assertThrows(NullPointerException.class, () ->
                modelManager.deleteLesson(null));
    }

    @Test
    public void deleteStudent_removesAssociatedLessons() {
        Student student = student("Henry Tan", "91234567", "henry@example.com");
        Lesson lesson = new Lesson(student, LocalDate.of(2026, 10, 25), LocalTime.of(14, 0),
                Duration.ofMinutes(60));

        modelManager.addStudent(student);
        modelManager.addLesson(lesson);
        modelManager.deleteStudent(student);

        assertTrue(modelManager.getLessonList().isEmpty());
    }

    @Test
    public void guardianOperations_addFilterSetAndDelete_success() {
        Guardian guardian = guardian("Ben Tan", "92345678", "ben@example.com");
        Guardian editedGuardian = guardian("Ben Tan", "92345678", "other@example.com");

        assertFalse(modelManager.hasGuardian(guardian));
        modelManager.addGuardian(guardian);
        assertTrue(modelManager.hasGuardian(guardian));
        assertEquals(List.of(guardian), modelManager.getFilteredGuardianList());

        modelManager.updateFilteredGuardianList(candidate -> false);
        assertTrue(modelManager.getFilteredGuardianList().isEmpty());
        modelManager.updateFilteredGuardianList(Model.PREDICATE_SHOW_ALL_GUARDIANS);

        modelManager.setGuardian(guardian, editedGuardian);
        assertEquals(List.of(editedGuardian), modelManager.getFilteredGuardianList());
        modelManager.deleteGuardian(editedGuardian);
        assertTrue(modelManager.getFilteredGuardianList().isEmpty());
    }

    @Test
    public void guardianOperations_nullInputs_throwNullPointerException() {
        Guardian guardian = guardian("Ben Tan", "92345678", "ben@example.com");
        assertThrows(NullPointerException.class, () -> modelManager.hasGuardian(null));
        assertThrows(NullPointerException.class, () -> modelManager.addGuardian(null));
        assertThrows(NullPointerException.class, () -> modelManager.deleteGuardian(null));
        assertThrows(NullPointerException.class, () -> modelManager.setGuardian(null, guardian));
        assertThrows(NullPointerException.class, () -> modelManager.setGuardian(guardian, null));
        assertThrows(NullPointerException.class, () -> modelManager.updateFilteredGuardianList(null));
        assertThrows(NullPointerException.class, () -> modelManager.getGuardiansForStudent(null));
    }

    @Test
    public void getGuardiansForStudent_returnsLinkedGuardians() {
        Student student = student("Henry Tan", "91234567", "henry@example.com");
        Guardian guardian = guardian("Ben Tan", "92345678", "ben@example.com");
        modelManager.addStudent(student);
        modelManager.addGuardian(guardian);
        modelManager.addStudentGuardianLink(new seedu.address.model.link.StudentGuardianLink(student, guardian));

        assertEquals(List.of(guardian), modelManager.getGuardiansForStudent(student));
    }

    @Test
    public void setStudent_remarkChanges_preserveGuardianLinks() {
        Student original = student("Henry Tan", "91234567", "henry@example.com");
        Guardian first = guardian("Ben Tan", "92345678", "ben@example.com");
        Guardian second = guardian("Amy Tan", "93456789", "amy@example.com");
        modelManager.addStudent(original);
        modelManager.addGuardian(first);
        modelManager.addGuardian(second);
        modelManager.addStudentGuardianLink(new StudentGuardianLink(original, first));
        modelManager.addStudentGuardianLink(new StudentGuardianLink(original, second));

        Student current = original;
        for (Remark remark : List.of(new Remark("Practice fractions"), new Remark("Ready for exam"), Remark.EMPTY)) {
            Student updated = current.withRemark(remark);
            modelManager.setStudent(current, updated);
            assertEquals(List.of(updated), modelManager.getFilteredStudentList());
            assertEquals(List.of(first, second), modelManager.getGuardiansForStudent(updated));
            assertEquals(List.of(new StudentGuardianLink(updated, first), new StudentGuardianLink(updated, second)),
                    modelManager.getAddressBook().getStudentGuardianLinkList());
            modelManager.addStudentGuardianLink(new StudentGuardianLink(updated, first));
            assertEquals(2, modelManager.getAddressBook().getStudentGuardianLinkList().size());
            current = updated;
        }
        modelManager.deleteStudent(current);
        assertTrue(modelManager.getAddressBook().getStudentGuardianLinkList().isEmpty());
        assertEquals(List.of(first, second), modelManager.getFilteredGuardianList());
    }

    @Test
    public void equals() {
        Student alice = student("Alice Tan", "91234567", "alice@example.com");
        Student bob = student("Bob Tan", "92345678", "bob@example.com");
        AddressBook addressBook = new AddressBook();
        addressBook.addStudent(alice);
        addressBook.addStudent(bob);
        AddressBook differentAddressBook = new AddressBook();
        UserPrefs userPrefs = new UserPrefs();

        // same values -> returns true
        modelManager = new ModelManager(addressBook, userPrefs);
        ModelManager modelManagerCopy = new ModelManager(addressBook, userPrefs);
        assertTrue(modelManager.equals(modelManagerCopy));

        // same object -> returns true
        assertTrue(modelManager.equals(modelManager));

        // null -> returns false
        assertFalse(modelManager.equals(null));

        // different types -> returns false
        assertFalse(modelManager.equals(5));

        // different addressBook -> returns false
        assertFalse(modelManager.equals(new ModelManager(differentAddressBook, userPrefs)));

        // different filteredList -> returns false
        modelManager.updateFilteredStudentList(candidate -> candidate.equals(alice));
        assertFalse(modelManager.equals(new ModelManager(addressBook, userPrefs)));

        // resets modelManager to initial state for upcoming tests
        modelManager.updateFilteredStudentList(Model.PREDICATE_SHOW_ALL_STUDENTS);

        // different userPrefs -> returns false
        UserPrefs differentUserPrefs = new UserPrefs();
        differentUserPrefs.setGuiSettings(new GuiSettings(1, 2, 3, 4));
        assertFalse(modelManager.equals(new ModelManager(addressBook, differentUserPrefs)));
    }

    private static Student student(String name, String phone, String email) {
        return new Student(new Name(name), new Phone(phone), new Email(email), new Address("12 Main Street"),
                new Level("JC 1"), Set.of(new Subject("Mathematics")));
    }

    private static Guardian guardian(String name, String phone, String email) {
        return new Guardian(Name.fromStudentValue(name), Phone.fromStudentValue(phone),
                Email.fromStudentValue(email), Address.fromStudentValue("12 Main Street"));
    }
}
