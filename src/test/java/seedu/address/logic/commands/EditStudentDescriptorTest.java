package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.LinkedHashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.model.student.Level;
import seedu.address.model.student.Student;
import seedu.address.model.student.Subject;

public class EditStudentDescriptorTest {
    private static final Student ORIGINAL = new Student(
            new Name("Lucy Lim"),
            new Phone("91234567"),
            new Email("lucy@example.com"),
            new Address("21 Happy Road"),
            new Level("JC 1"),
            subjects("Mathematics", "Physics"));

    @Test
    public void createEditedStudent_noFields_preservesOriginal() {
        EditStudentDescriptor descriptor = new EditStudentDescriptor();

        assertEquals(ORIGINAL, descriptor.createEditedStudent(ORIGINAL));
        assertTrue(!descriptor.isAnyFieldEdited());
    }

    @Test
    public void createEditedStudent_oneField_preservesOtherFields() {
        EditStudentDescriptor descriptor = new EditStudentDescriptor();
        descriptor.setPhone(new Phone("98765432"));

        Student edited = descriptor.createEditedStudent(ORIGINAL);

        assertEquals(new Phone("98765432"), edited.getPhone());
        assertEquals(ORIGINAL.getName(), edited.getName());
        assertEquals(ORIGINAL.getEmail(), edited.getEmail());
        assertEquals(ORIGINAL.getAddress(), edited.getAddress());
        assertEquals(ORIGINAL.getLevel(), edited.getLevel());
        assertEquals(ORIGINAL.getSubjects(), edited.getSubjects());
        assertTrue(descriptor.isAnyFieldEdited());
    }

    @Test
    public void createEditedStudent_multipleFields_updatesAllFields() {
        EditStudentDescriptor descriptor = new EditStudentDescriptor();
        descriptor.setName(new Name("Lucy Tan"));
        descriptor.setLevel(new Level("JC 2"));

        Student edited = descriptor.createEditedStudent(ORIGINAL);

        assertEquals(new Name("Lucy Tan"), edited.getName());
        assertEquals(new Level("JC 2"), edited.getLevel());
        assertEquals(ORIGINAL.getPhone(), edited.getPhone());
        assertEquals(ORIGINAL.getSubjects(), edited.getSubjects());
    }

    @Test
    public void createEditedStudent_subjectsReplaceExistingSubjects() {
        EditStudentDescriptor descriptor = new EditStudentDescriptor();
        descriptor.setSubjects(subjects("Chemistry", "Biology"));

        Student edited = descriptor.createEditedStudent(ORIGINAL);

        assertEquals(subjects("Chemistry", "Biology"), edited.getSubjects());
        assertEquals(ORIGINAL.getName(), edited.getName());
    }

    @Test
    public void createEditedStudent_emptySubjects_throwsIllegalArgumentException() {
        EditStudentDescriptor descriptor = new EditStudentDescriptor();
        descriptor.setSubjects(Set.of());

        assertThrows(IllegalArgumentException.class, () -> descriptor.createEditedStudent(ORIGINAL));
    }

    @Test
    public void getSubjects_returnsUnmodifiableSet() {
        EditStudentDescriptor descriptor = new EditStudentDescriptor();
        descriptor.setSubjects(subjects("Mathematics"));

        assertThrows(UnsupportedOperationException.class, () -> addSubject(descriptor));
    }

    private static Set<Subject> subjects(String... values) {
        Set<Subject> subjects = new LinkedHashSet<>();
        for (String value : values) {
            subjects.add(new Subject(value));
        }
        return subjects;
    }

    private static void addSubject(EditStudentDescriptor descriptor) {
        descriptor.getSubjects().orElseThrow().add(new Subject("Physics"));
    }
}
