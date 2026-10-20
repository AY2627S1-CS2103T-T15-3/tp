package seedu.address.model.student;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Iterator;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.model.student.exceptions.DuplicateLessonException;
import seedu.address.model.student.exceptions.LessonNotFoundException;

/**
 * A list of lessons that does not allow nulls or duplicate lessons.
 */
public class UniqueLessonList implements Iterable<Lesson> {

    private final ObservableList<Lesson> internalList = FXCollections.observableArrayList();
    private final ObservableList<Lesson> internalUnmodifiableList =
            FXCollections.unmodifiableObservableList(internalList);

    /** Returns true if this list contains an equivalent lesson. */
    public boolean contains(Lesson toCheck) {
        requireNonNull(toCheck);
        return internalList.contains(toCheck);
    }

    /** Adds a lesson that is not already in this list. */
    public void add(Lesson toAdd) {
        requireNonNull(toAdd);
        if (contains(toAdd)) {
            throw new DuplicateLessonException();
        }
        internalList.add(toAdd);
    }

    /** Removes an existing lesson from this list. */
    public void remove(Lesson toRemove) {
        requireNonNull(toRemove);
        if (!internalList.remove(toRemove)) {
            throw new LessonNotFoundException();
        }
    }

    /** Replaces an existing lesson with an edited lesson. */
    public void setLesson(Lesson target, Lesson editedLesson) {
        requireAllNonNull(target, editedLesson);
        int index = internalList.indexOf(target);
        if (index == -1) {
            throw new LessonNotFoundException();
        }
        if (!target.equals(editedLesson) && contains(editedLesson)) {
            throw new DuplicateLessonException();
        }
        internalList.set(index, editedLesson);
    }

    public void setLessons(UniqueLessonList replacement) {
        requireNonNull(replacement);
        internalList.setAll(replacement.internalList);
    }

    /** Replaces this list after verifying that the supplied lessons are unique. */
    public void setLessons(List<Lesson> lessons) {
        requireAllNonNull(lessons);
        if (!lessonsAreUnique(lessons)) {
            throw new DuplicateLessonException();
        }
        internalList.setAll(lessons);
    }

    /** Returns the backing list as an unmodifiable observable list. */
    public ObservableList<Lesson> asUnmodifiableObservableList() {
        return internalUnmodifiableList;
    }

    @Override
    public Iterator<Lesson> iterator() {
        return internalList.iterator();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof UniqueLessonList otherUniqueLessonList)) {
            return false;
        }
        return internalList.equals(otherUniqueLessonList.internalList);
    }

    @Override
    public int hashCode() {
        return internalList.hashCode();
    }

    @Override
    public String toString() {
        return internalList.toString();
    }

    private boolean lessonsAreUnique(List<Lesson> lessons) {
        for (int i = 0; i < lessons.size() - 1; i++) {
            for (int j = i + 1; j < lessons.size(); j++) {
                if (lessons.get(i).equals(lessons.get(j))) {
                    return false;
                }
            }
        }
        return true;
    }
}
