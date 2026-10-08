package seedu.address.model.guardian;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Iterator;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.model.person.exceptions.PersonNotFoundException;

/**
 * A list of guardians that enforces uniqueness between its elements and does not allow nulls.
 * A guardian is considered unique by comparing using {@code Guardian#isSameGuardian(Guardian)}. Adding and updating
 * guardians uses this identity comparison to prevent duplicates. Locating a target for replacement or removal uses
 * {@code Guardian#equals(Object)} to match the guardian with exactly the same fields.
 *
 * Supports a minimal set of list operations.
 *
 * @see Guardian#isSameGuardian(Guardian)
 */
public class UniqueGuardianList implements Iterable<Guardian> {

    private final ObservableList<Guardian> internalList = FXCollections.observableArrayList();
    private final ObservableList<Guardian> internalUnmodifiableList =
            FXCollections.unmodifiableObservableList(internalList);

    /**
     * Returns true if the list contains an equivalent guardian as the given argument.
     */
    public boolean contains(Guardian toCheck) {
        requireNonNull(toCheck);
        return internalList.stream().anyMatch(toCheck::isSameGuardian);
    }

    /**
     * Adds a guardian to the list.
     * The guardian must not already exist in the list.
     */
    public void add(Guardian toAdd) {
        requireNonNull(toAdd);
        if (contains(toAdd)) {
            throw new DuplicatePersonException();
        }
        internalList.add(toAdd);
    }

    /**
     * Replaces the guardian {@code target} in the list with {@code editedGuardian}.
     * {@code target} must exist in the list.
     * The guardian identity of {@code editedGuardian} must not be the same as another existing guardian in the list.
     */
    public void setGuardian(Guardian target, Guardian editedGuardian) {
        requireAllNonNull(target, editedGuardian);

        int index = internalList.indexOf(target);
        if (index == -1) {
            throw new PersonNotFoundException();
        }

        if (!target.isSameGuardian(editedGuardian) && contains(editedGuardian)) {
            throw new DuplicatePersonException();
        }

        internalList.set(index, editedGuardian);
    }

    /**
     * Removes the equivalent guardian from the list.
     * The guardian must exist in the list.
     */
    public void remove(Guardian toRemove) {
        requireNonNull(toRemove);
        if (!internalList.remove(toRemove)) {
            throw new PersonNotFoundException();
        }
    }

    public void setGuardians(UniqueGuardianList replacement) {
        requireNonNull(replacement);
        internalList.setAll(replacement.internalList);
    }

    /**
     * Replaces the contents of this list with {@code guardians}.
     * {@code guardians} must not contain duplicate guardians.
     */
    public void setGuardians(List<Guardian> guardians) {
        requireAllNonNull(guardians);
        if (!guardiansAreUnique(guardians)) {
            throw new DuplicatePersonException();
        }

        internalList.setAll(guardians);
    }

    /**
     * Returns the backing list as an unmodifiable {@code ObservableList}.
     */
    public ObservableList<Guardian> asUnmodifiableObservableList() {
        return internalUnmodifiableList;
    }

    @Override
    public Iterator<Guardian> iterator() {
        return internalList.iterator();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof UniqueGuardianList otherUniqueGuardianList)) {
            return false;
        }

        return internalList.equals(otherUniqueGuardianList.internalList);
    }

    @Override
    public int hashCode() {
        return internalList.hashCode();
    }

    @Override
    public String toString() {
        return internalList.toString();
    }

    /**
     * Returns true if {@code guardians} contains only unique guardians.
     */
    private boolean guardiansAreUnique(List<Guardian> guardians) {
        for (int i = 0; i < guardians.size() - 1; i++) {
            for (int j = i + 1; j < guardians.size(); j++) {
                if (guardians.get(i).isSameGuardian(guardians.get(j))) {
                    return false;
                }
            }
        }
        return true;
    }
}
