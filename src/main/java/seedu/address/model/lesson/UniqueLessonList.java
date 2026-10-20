package seedu.address.model.lesson;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.lesson.exceptions.DuplicateLessonException;
import seedu.address.model.lesson.exceptions.LessonNotFoundException;

/**
 * A list of lessons that does not allow nulls, duplicate or overlapping lessons.
 */
public class UniqueLessonList implements Iterable<Lesson> {

    private static final Logger logger = LogsCenter.getLogger(UniqueLessonList.class);
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
        assert internalList.stream().noneMatch(existingLesson -> existingLesson.overlaps(toAdd))
                : "Lesson overlaps an existing lesson";
        internalList.add(toAdd);
        logger.info("Added lesson: " + toAdd);
    }

    /** Removes an existing lesson from this list. */
    public void remove(Lesson toRemove) {
        requireNonNull(toRemove);
        if (!internalList.remove(toRemove)) {
            throw new LessonNotFoundException();
        }
        logger.info("Removed lesson: " + toRemove);
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
        assert internalList.stream()
                .filter(existingLesson -> !existingLesson.equals(target))
                .noneMatch(existingLesson -> existingLesson.overlaps(editedLesson))
                : "Lesson overlaps an existing lesson";
        internalList.set(index, editedLesson);
        logger.info("Replaced lesson: " + target + " with " + editedLesson);
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
