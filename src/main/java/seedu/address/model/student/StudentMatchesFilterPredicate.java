package seedu.address.model.student;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

import seedu.address.commons.util.ToStringBuilder;

/**
 * Tests that a {@code Student} is at the given {@code Level}, if any, and takes all of the given {@code Subject}s.
 */
public class StudentMatchesFilterPredicate implements Predicate<Student> {
    private final Optional<Level> level;
    private final Set<Subject> subjects;

    /**
     * Creates a predicate that matches students at {@code level}, if present, who take all of {@code subjects}.
     */
    public StudentMatchesFilterPredicate(Optional<Level> level, Set<Subject> subjects) {
        requireAllNonNull(level, subjects);
        this.level = level;
        this.subjects = Set.copyOf(subjects);
    }

    @Override
    public boolean test(Student student) {
        boolean isLevelMatched = level.map(student.getLevel()::equals).orElse(true);
        return isLevelMatched && student.getSubjects().containsAll(subjects);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof StudentMatchesFilterPredicate otherPredicate)) {
            return false;
        }

        return level.equals(otherPredicate.level) && subjects.equals(otherPredicate.subjects);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("level", level)
                .add("subjects", subjects)
                .toString();
    }
}
