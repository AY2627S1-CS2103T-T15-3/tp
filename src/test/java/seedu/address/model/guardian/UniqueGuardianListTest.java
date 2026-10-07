package seedu.address.model.guardian;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.junit.jupiter.api.Test;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.model.person.exceptions.PersonNotFoundException;

public class UniqueGuardianListTest {

    private static final Guardian ALICE = guardian("Alice Pauline", "91234567");
    private static final Guardian BOB = guardian("Bob Choo", "98765432");
    private static final Guardian EDITED_ALICE = new Guardian(new Name("alice  pauline"), ALICE.getPhone(),
            new Email("other@example.com"), new Address("25 Happy Road"));

    private final UniqueGuardianList uniqueGuardianList = new UniqueGuardianList();

    @Test
    public void contains_nullGuardian_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueGuardianList.contains(null));
    }

    @Test
    public void contains_guardianNotInList_returnsFalse() {
        assertFalse(uniqueGuardianList.contains(ALICE));
    }

    @Test
    public void contains_guardianInList_returnsTrue() {
        uniqueGuardianList.add(ALICE);
        assertTrue(uniqueGuardianList.contains(ALICE));
    }

    @Test
    public void contains_guardianWithSameIdentityFieldsInList_returnsTrue() {
        uniqueGuardianList.add(ALICE);
        Guardian editedAlice = EDITED_ALICE;
        assertTrue(uniqueGuardianList.contains(editedAlice));
    }

    @Test
    public void add_nullGuardian_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueGuardianList.add(null));
    }

    @Test
    public void add_duplicateGuardian_throwsDuplicatePersonException() {
        uniqueGuardianList.add(ALICE);
        assertThrows(DuplicatePersonException.class, () -> uniqueGuardianList.add(ALICE));
    }

    @Test
    public void setGuardian_nullTargetGuardian_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueGuardianList.setGuardian(null, ALICE));
    }

    @Test
    public void setGuardian_nullEditedGuardian_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueGuardianList.setGuardian(ALICE, null));
    }

    @Test
    public void setGuardian_targetGuardianNotInList_throwsPersonNotFoundException() {
        assertThrows(PersonNotFoundException.class, () -> uniqueGuardianList.setGuardian(ALICE, ALICE));
    }

    @Test
    public void setGuardian_editedGuardianIsSameGuardian_success() {
        uniqueGuardianList.add(ALICE);
        uniqueGuardianList.setGuardian(ALICE, ALICE);
        UniqueGuardianList expectedUniqueGuardianList = new UniqueGuardianList();
        expectedUniqueGuardianList.add(ALICE);
        assertEquals(expectedUniqueGuardianList, uniqueGuardianList);
    }

    @Test
    public void setGuardian_editedGuardianHasSameIdentity_success() {
        uniqueGuardianList.add(ALICE);
        Guardian editedAlice = EDITED_ALICE;
        uniqueGuardianList.setGuardian(ALICE, editedAlice);
        UniqueGuardianList expectedUniqueGuardianList = new UniqueGuardianList();
        expectedUniqueGuardianList.add(editedAlice);
        assertEquals(expectedUniqueGuardianList, uniqueGuardianList);
    }

    @Test
    public void setGuardian_editedGuardianHasDifferentIdentity_success() {
        uniqueGuardianList.add(ALICE);
        uniqueGuardianList.setGuardian(ALICE, BOB);
        UniqueGuardianList expectedUniqueGuardianList = new UniqueGuardianList();
        expectedUniqueGuardianList.add(BOB);
        assertEquals(expectedUniqueGuardianList, uniqueGuardianList);
    }

    @Test
    public void setGuardian_editedGuardianHasNonUniqueIdentity_throwsDuplicatePersonException() {
        uniqueGuardianList.add(ALICE);
        uniqueGuardianList.add(BOB);
        assertThrows(DuplicatePersonException.class, () -> uniqueGuardianList.setGuardian(ALICE, BOB));
    }

    @Test
    public void remove_nullGuardian_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueGuardianList.remove(null));
    }

    @Test
    public void remove_guardianDoesNotExist_throwsPersonNotFoundException() {
        assertThrows(PersonNotFoundException.class, () -> uniqueGuardianList.remove(ALICE));
    }

    @Test
    public void remove_existingGuardian_removesGuardian() {
        uniqueGuardianList.add(ALICE);
        uniqueGuardianList.remove(ALICE);
        UniqueGuardianList expectedUniqueGuardianList = new UniqueGuardianList();
        assertEquals(expectedUniqueGuardianList, uniqueGuardianList);
    }

    @Test
    public void setGuardians_nullUniqueGuardianList_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueGuardianList.setGuardians((UniqueGuardianList) null));
    }

    @Test
    public void setGuardians_uniqueGuardianList_replacesOwnListWithProvidedUniqueGuardianList() {
        uniqueGuardianList.add(ALICE);
        UniqueGuardianList expectedUniqueGuardianList = new UniqueGuardianList();
        expectedUniqueGuardianList.add(BOB);
        uniqueGuardianList.setGuardians(expectedUniqueGuardianList);
        assertEquals(expectedUniqueGuardianList, uniqueGuardianList);
    }

    @Test
    public void setGuardians_nullList_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueGuardianList.setGuardians((List<Guardian>) null));
    }

    @Test
    public void setGuardians_list_replacesOwnListWithProvidedList() {
        uniqueGuardianList.add(ALICE);
        List<Guardian> guardianList = List.of(BOB);
        uniqueGuardianList.setGuardians(guardianList);
        UniqueGuardianList expectedUniqueGuardianList = new UniqueGuardianList();
        expectedUniqueGuardianList.add(BOB);
        assertEquals(expectedUniqueGuardianList, uniqueGuardianList);
    }

    @Test
    public void setGuardians_listWithDuplicateGuardians_throwsDuplicatePersonException() {
        List<Guardian> listWithDuplicateGuardians = List.of(ALICE, ALICE);
        assertThrows(DuplicatePersonException.class, () -> uniqueGuardianList.setGuardians(listWithDuplicateGuardians));
    }

    @Test
    public void asUnmodifiableObservableList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, ()
            -> uniqueGuardianList.asUnmodifiableObservableList().remove(0));
    }

    @Test
    public void toStringMethod() {
        assertEquals(uniqueGuardianList.asUnmodifiableObservableList().toString(), uniqueGuardianList.toString());
    }

    @Test
    public void add_sameIdentityDifferentDetails_throwsDuplicatePersonException() {
        uniqueGuardianList.add(ALICE);
        assertThrows(DuplicatePersonException.class, () -> uniqueGuardianList.add(EDITED_ALICE));
        assertEquals(List.of(ALICE), uniqueGuardianList.asUnmodifiableObservableList());
    }

    @Test
    public void add_differentNameOrPhone_success() {
        Guardian differentName = guardian("Alice Tan", "91234567");
        Guardian differentPhone = guardian("Alice Pauline", "92345678");
        uniqueGuardianList.add(ALICE);
        uniqueGuardianList.add(differentName);
        uniqueGuardianList.add(differentPhone);
        assertEquals(List.of(ALICE, differentName, differentPhone), uniqueGuardianList.asUnmodifiableObservableList());
    }

    @Test
    public void setGuardian_targetHasSameIdentityButDifferentDetails_throwsPersonNotFoundException() {
        uniqueGuardianList.add(ALICE);
        assertThrows(PersonNotFoundException.class, () -> uniqueGuardianList.setGuardian(EDITED_ALICE, BOB));
        assertEquals(List.of(ALICE), uniqueGuardianList.asUnmodifiableObservableList());
    }

    @Test
    public void setGuardian_duplicateIdentity_leavesListUnchanged() {
        uniqueGuardianList.setGuardians(List.of(ALICE, BOB));
        assertThrows(DuplicatePersonException.class, () -> uniqueGuardianList.setGuardian(BOB, EDITED_ALICE));
        assertEquals(List.of(ALICE, BOB), uniqueGuardianList.asUnmodifiableObservableList());
    }

    @Test
    public void setGuardian_equalTarget_preservesPosition() {
        uniqueGuardianList.setGuardians(List.of(ALICE, BOB));
        uniqueGuardianList.setGuardian(guardian("Alice Pauline", "91234567"), EDITED_ALICE);
        assertEquals(List.of(EDITED_ALICE, BOB), uniqueGuardianList.asUnmodifiableObservableList());
    }

    @Test
    public void remove_sameIdentityDifferentDetails_throwsPersonNotFoundException() {
        uniqueGuardianList.add(ALICE);
        assertThrows(PersonNotFoundException.class, () -> uniqueGuardianList.remove(EDITED_ALICE));
        assertEquals(List.of(ALICE), uniqueGuardianList.asUnmodifiableObservableList());
    }

    @Test
    public void remove_equalGuardian_removesOnlyMatchingGuardian() {
        uniqueGuardianList.setGuardians(List.of(ALICE, BOB));
        uniqueGuardianList.remove(guardian("Alice Pauline", "91234567"));
        assertEquals(List.of(BOB), uniqueGuardianList.asUnmodifiableObservableList());
    }

    @Test
    public void setGuardians_nullElement_leavesListUnchanged() {
        uniqueGuardianList.add(BOB);
        assertThrows(NullPointerException.class, () -> uniqueGuardianList.setGuardians(Arrays.asList(ALICE, null)));
        assertEquals(List.of(BOB), uniqueGuardianList.asUnmodifiableObservableList());
    }

    @Test
    public void setGuardians_duplicateIdentity_leavesListUnchanged() {
        uniqueGuardianList.add(BOB);
        assertThrows(DuplicatePersonException.class, ()
                -> uniqueGuardianList.setGuardians(List.of(ALICE, EDITED_ALICE)));
        assertEquals(List.of(BOB), uniqueGuardianList.asUnmodifiableObservableList());
    }

    @Test
    public void setGuardians_emptyList_clearsList() {
        uniqueGuardianList.add(ALICE);
        uniqueGuardianList.setGuardians(List.of());
        assertTrue(uniqueGuardianList.asUnmodifiableObservableList().isEmpty());
    }

    @Test
    public void setGuardians_sourceChanges_doNotChangeOwnList() {
        List<Guardian> source = new ArrayList<>(List.of(ALICE));
        uniqueGuardianList.setGuardians(source);
        source.clear();
        assertEquals(List.of(ALICE), uniqueGuardianList.asUnmodifiableObservableList());

        UniqueGuardianList replacement = new UniqueGuardianList();
        replacement.add(BOB);
        uniqueGuardianList.setGuardians(replacement);
        replacement.remove(BOB);
        assertEquals(List.of(BOB), uniqueGuardianList.asUnmodifiableObservableList());
    }

    @Test
    public void asUnmodifiableObservableList_updatesRemainVisible() {
        ObservableList<Guardian> view = uniqueGuardianList.asUnmodifiableObservableList();
        List<List<Guardian>> snapshots = new ArrayList<>();
        view.addListener((ListChangeListener<Guardian>) change -> snapshots.add(List.copyOf(view)));
        uniqueGuardianList.add(ALICE);
        uniqueGuardianList.setGuardian(ALICE, EDITED_ALICE);
        uniqueGuardianList.remove(EDITED_ALICE);
        uniqueGuardianList.setGuardians(List.of(BOB));
        assertEquals(List.of(List.of(ALICE), List.of(EDITED_ALICE), List.of(), List.of(BOB)), snapshots);
        assertThrows(UnsupportedOperationException.class, () -> view.add(ALICE));
        assertThrows(UnsupportedOperationException.class, () -> view.set(0, ALICE));
    }

    @Test
    public void iterator_returnsGuardiansInOrder() {
        uniqueGuardianList.setGuardians(List.of(ALICE, BOB));
        Iterator<Guardian> iterator = uniqueGuardianList.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(ALICE, iterator.next());
        assertEquals(BOB, iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void equalsAndHashCode() {
        uniqueGuardianList.setGuardians(List.of(ALICE, BOB));
        UniqueGuardianList copy = new UniqueGuardianList();
        copy.setGuardians(List.of(ALICE, BOB));
        assertTrue(uniqueGuardianList.equals(uniqueGuardianList));
        assertTrue(uniqueGuardianList.equals(copy));
        assertEquals(uniqueGuardianList.hashCode(), copy.hashCode());
        assertFalse(uniqueGuardianList.equals(null));
        assertFalse(uniqueGuardianList.equals(5));
        copy.setGuardians(List.of(BOB, ALICE));
        assertFalse(uniqueGuardianList.equals(copy));
        copy.setGuardians(List.of(EDITED_ALICE, BOB));
        assertFalse(uniqueGuardianList.equals(copy));
    }

    private static Guardian guardian(String name, String phone) {
        return new Guardian(new Name(name), new Phone(phone), new Email("guardian@example.com"),
                new Address("12 ABCD Ave 3"));
    }
}
