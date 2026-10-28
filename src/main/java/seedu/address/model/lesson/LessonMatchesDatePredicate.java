package seedu.address.model.lesson;

import static java.util.Objects.requireNonNull;

import java.time.LocalDate;
import java.util.function.Predicate;

import seedu.address.commons.util.ToStringBuilder;

/** Tests that a {@code Lesson} takes place on the given date. */
public class LessonMatchesDatePredicate implements Predicate<Lesson> {
    private final LocalDate date;

    public LessonMatchesDatePredicate(LocalDate date) {
        this.date = requireNonNull(date);
    }

    @Override
    public boolean test(Lesson lesson) {
        return lesson.getDate().equals(date);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof LessonMatchesDatePredicate otherPredicate)) {
            return false;
        }
        return date.equals(otherPredicate.date);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("date", date).toString();
    }
}
