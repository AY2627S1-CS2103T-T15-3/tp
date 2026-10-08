package seedu.address.model.student;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.model.person.exceptions.PersonNotFoundException;

public class UniqueStudentListTest {

    private static final Student ALICE = student("Alice Pauline", "91234567");
    private static final Student BOB = student("Bob Choo", "98765432");
    private static final Student EDITED_ALICE = new Student(new Name("alice  pauline"), ALICE.getPhone(),
            new Email("other@example.com"), new Address("25 Happy Road"), new Level("JC 1"),
            Set.of(new Subject("Physics")));

    private final UniqueStudentList uniqueStudentList = new UniqueStudentList();

    @Test
    public void contains_nullStudent_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueStudentList.contains(null));
    }

    @Test
    public void contains_studentNotInList_returnsFalse() {
        assertFalse(uniqueStudentList.contains(ALICE));
    }

    @Test
    public void contains_studentInList_returnsTrue() {
        uniqueStudentList.add(ALICE);
        assertTrue(uniqueStudentList.contains(ALICE));
    }

    @Test
    public void contains_studentWithSameIdentityFieldsInList_returnsTrue() {
        uniqueStudentList.add(ALICE);
        Student editedAlice = EDITED_ALICE;
        assertTrue(uniqueStudentList.contains(editedAlice));
    }

    @Test
    public void add_nullStudent_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueStudentList.add(null));
    }

    @Test
    public void add_duplicateStudent_throwsDuplicatePersonException() {
        uniqueStudentList.add(ALICE);
        assertThrows(DuplicatePersonException.class, () -> uniqueStudentList.add(ALICE));
    }

    @Test
    public void setStudent_nullTargetStudent_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueStudentList.setStudent(null, ALICE));
    }

    @Test
    public void setStudent_nullEditedStudent_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueStudentList.setStudent(ALICE, null));
    }

    @Test
    public void setStudent_targetStudentNotInList_throwsPersonNotFoundException() {
        assertThrows(PersonNotFoundException.class, () -> uniqueStudentList.setStudent(ALICE, ALICE));
    }

    @Test
    public void setStudent_editedStudentIsSameStudent_success() {
        uniqueStudentList.add(ALICE);
        uniqueStudentList.setStudent(ALICE, ALICE);
        UniqueStudentList expectedUniqueStudentList = new UniqueStudentList();
        expectedUniqueStudentList.add(ALICE);
        assertEquals(expectedUniqueStudentList, uniqueStudentList);
    }

    @Test
    public void setStudent_editedStudentHasSameIdentity_success() {
        uniqueStudentList.add(ALICE);
        Student editedAlice = EDITED_ALICE;
        uniqueStudentList.setStudent(ALICE, editedAlice);
        UniqueStudentList expectedUniqueStudentList = new UniqueStudentList();
        expectedUniqueStudentList.add(editedAlice);
        assertEquals(expectedUniqueStudentList, uniqueStudentList);
    }

    @Test
    public void setStudent_editedStudentHasDifferentIdentity_success() {
        uniqueStudentList.add(ALICE);
        uniqueStudentList.setStudent(ALICE, BOB);
        UniqueStudentList expectedUniqueStudentList = new UniqueStudentList();
        expectedUniqueStudentList.add(BOB);
        assertEquals(expectedUniqueStudentList, uniqueStudentList);
    }

    @Test
    public void setStudent_editedStudentHasNonUniqueIdentity_throwsDuplicatePersonException() {
        uniqueStudentList.add(ALICE);
        uniqueStudentList.add(BOB);
        assertThrows(DuplicatePersonException.class, () -> uniqueStudentList.setStudent(ALICE, BOB));
    }

    @Test
    public void remove_nullStudent_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueStudentList.remove(null));
    }

    @Test
    public void remove_studentDoesNotExist_throwsPersonNotFoundException() {
        assertThrows(PersonNotFoundException.class, () -> uniqueStudentList.remove(ALICE));
    }

    @Test
    public void remove_existingStudent_removesStudent() {
        uniqueStudentList.add(ALICE);
        uniqueStudentList.remove(ALICE);
        UniqueStudentList expectedUniqueStudentList = new UniqueStudentList();
        assertEquals(expectedUniqueStudentList, uniqueStudentList);
    }

    @Test
    public void setStudents_nullUniqueStudentList_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueStudentList.setStudents((UniqueStudentList) null));
    }

    @Test
    public void setStudents_uniqueStudentList_replacesOwnListWithProvidedUniqueStudentList() {
        uniqueStudentList.add(ALICE);
        UniqueStudentList expectedUniqueStudentList = new UniqueStudentList();
        expectedUniqueStudentList.add(BOB);
        uniqueStudentList.setStudents(expectedUniqueStudentList);
        assertEquals(expectedUniqueStudentList, uniqueStudentList);
    }

    @Test
    public void setStudents_nullList_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueStudentList.setStudents((List<Student>) null));
    }

    @Test
    public void setStudents_list_replacesOwnListWithProvidedList() {
        uniqueStudentList.add(ALICE);
        List<Student> studentList = List.of(BOB);
        uniqueStudentList.setStudents(studentList);
        UniqueStudentList expectedUniqueStudentList = new UniqueStudentList();
        expectedUniqueStudentList.add(BOB);
        assertEquals(expectedUniqueStudentList, uniqueStudentList);
    }

    @Test
    public void setStudents_listWithDuplicateStudents_throwsDuplicatePersonException() {
        List<Student> listWithDuplicateStudents = List.of(ALICE, ALICE);
        assertThrows(DuplicatePersonException.class, () -> uniqueStudentList.setStudents(listWithDuplicateStudents));
    }

    @Test
    public void asUnmodifiableObservableList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, ()
            -> uniqueStudentList.asUnmodifiableObservableList().remove(0));
    }

    @Test
    public void toStringMethod() {
        assertEquals(uniqueStudentList.asUnmodifiableObservableList().toString(), uniqueStudentList.toString());
    }

    @Test
    public void add_sameIdentityDifferentDetails_throwsDuplicatePersonException() {
        uniqueStudentList.add(ALICE);
        assertThrows(DuplicatePersonException.class, () -> uniqueStudentList.add(EDITED_ALICE));
        assertEquals(List.of(ALICE), uniqueStudentList.asUnmodifiableObservableList());
    }

    @Test
    public void add_differentNameOrPhone_success() {
        Student differentName = student("Alice Tan", "91234567");
        Student differentPhone = student("Alice Pauline", "92345678");
        uniqueStudentList.add(ALICE);
        uniqueStudentList.add(differentName);
        uniqueStudentList.add(differentPhone);
        assertEquals(List.of(ALICE, differentName, differentPhone), uniqueStudentList.asUnmodifiableObservableList());
    }

    @Test
    public void setStudent_targetHasSameIdentityButDifferentDetails_throwsPersonNotFoundException() {
        uniqueStudentList.add(ALICE);
        assertThrows(PersonNotFoundException.class, () -> uniqueStudentList.setStudent(EDITED_ALICE, BOB));
        assertEquals(List.of(ALICE), uniqueStudentList.asUnmodifiableObservableList());
    }

    @Test
    public void setStudent_duplicateIdentity_leavesListUnchanged() {
        uniqueStudentList.setStudents(List.of(ALICE, BOB));
        assertThrows(DuplicatePersonException.class, () -> uniqueStudentList.setStudent(BOB, EDITED_ALICE));
        assertEquals(List.of(ALICE, BOB), uniqueStudentList.asUnmodifiableObservableList());
    }

    @Test
    public void setStudent_equalTarget_preservesPosition() {
        uniqueStudentList.setStudents(List.of(ALICE, BOB));
        uniqueStudentList.setStudent(student("Alice Pauline", "91234567"), EDITED_ALICE);
        assertEquals(List.of(EDITED_ALICE, BOB), uniqueStudentList.asUnmodifiableObservableList());
    }

    @Test
    public void remove_sameIdentityDifferentDetails_throwsPersonNotFoundException() {
        uniqueStudentList.add(ALICE);
        assertThrows(PersonNotFoundException.class, () -> uniqueStudentList.remove(EDITED_ALICE));
        assertEquals(List.of(ALICE), uniqueStudentList.asUnmodifiableObservableList());
    }

    @Test
    public void remove_equalStudent_removesOnlyMatchingStudent() {
        uniqueStudentList.setStudents(List.of(ALICE, BOB));
        uniqueStudentList.remove(student("Alice Pauline", "91234567"));
        assertEquals(List.of(BOB), uniqueStudentList.asUnmodifiableObservableList());
    }

    @Test
    public void setStudents_nullElement_leavesListUnchanged() {
        uniqueStudentList.add(BOB);
        assertThrows(NullPointerException.class, () -> uniqueStudentList.setStudents(Arrays.asList(ALICE, null)));
        assertEquals(List.of(BOB), uniqueStudentList.asUnmodifiableObservableList());
    }

    @Test
    public void setStudents_duplicateIdentity_leavesListUnchanged() {
        uniqueStudentList.add(BOB);
        assertThrows(DuplicatePersonException.class, ()
                -> uniqueStudentList.setStudents(List.of(ALICE, EDITED_ALICE)));
        assertEquals(List.of(BOB), uniqueStudentList.asUnmodifiableObservableList());
    }

    @Test
    public void setStudents_emptyList_clearsList() {
        uniqueStudentList.add(ALICE);
        uniqueStudentList.setStudents(List.of());
        assertTrue(uniqueStudentList.asUnmodifiableObservableList().isEmpty());
    }

    @Test
    public void setStudents_sourceChanges_doNotChangeOwnList() {
        List<Student> source = new ArrayList<>(List.of(ALICE));
        uniqueStudentList.setStudents(source);
        source.clear();
        assertEquals(List.of(ALICE), uniqueStudentList.asUnmodifiableObservableList());

        UniqueStudentList replacement = new UniqueStudentList();
        replacement.add(BOB);
        uniqueStudentList.setStudents(replacement);
        replacement.remove(BOB);
        assertEquals(List.of(BOB), uniqueStudentList.asUnmodifiableObservableList());
    }

    @Test
    public void asUnmodifiableObservableList_updatesRemainVisible() {
        ObservableList<Student> view = uniqueStudentList.asUnmodifiableObservableList();
        List<List<Student>> snapshots = new ArrayList<>();
        view.addListener((ListChangeListener<Student>) change -> snapshots.add(List.copyOf(view)));
        uniqueStudentList.add(ALICE);
        uniqueStudentList.setStudent(ALICE, EDITED_ALICE);
        uniqueStudentList.remove(EDITED_ALICE);
        uniqueStudentList.setStudents(List.of(BOB));
        assertEquals(List.of(List.of(ALICE), List.of(EDITED_ALICE), List.of(), List.of(BOB)), snapshots);
        assertThrows(UnsupportedOperationException.class, () -> view.add(ALICE));
        assertThrows(UnsupportedOperationException.class, () -> view.set(0, ALICE));
    }

    @Test
    public void iterator_returnsStudentsInOrder() {
        uniqueStudentList.setStudents(List.of(ALICE, BOB));
        Iterator<Student> iterator = uniqueStudentList.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(ALICE, iterator.next());
        assertEquals(BOB, iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void equalsAndHashCode() {
        uniqueStudentList.setStudents(List.of(ALICE, BOB));
        UniqueStudentList copy = new UniqueStudentList();
        copy.setStudents(List.of(ALICE, BOB));
        assertTrue(uniqueStudentList.equals(uniqueStudentList));
        assertTrue(uniqueStudentList.equals(copy));
        assertEquals(uniqueStudentList.hashCode(), copy.hashCode());
        assertFalse(uniqueStudentList.equals(null));
        assertFalse(uniqueStudentList.equals(5));
        copy.setStudents(List.of(BOB, ALICE));
        assertFalse(uniqueStudentList.equals(copy));
        copy.setStudents(List.of(EDITED_ALICE, BOB));
        assertFalse(uniqueStudentList.equals(copy));
    }

    private static Student student(String name, String phone) {
        return new Student(new Name(name), new Phone(phone), new Email("student@example.com"),
                new Address("12 ABCD Ave 3"), new Level("Secondary 3"), Set.of(new Subject("Mathematics")));
    }
}
