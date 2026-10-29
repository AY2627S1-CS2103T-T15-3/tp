package seedu.address.model.student;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

import seedu.address.commons.util.ToStringBuilder;

/**
 * Tests that a {@code Student} is at the given {@code Level}, if any, takes all of the given {@code Subject}s,
 * has paid for the given paid month, if any, and has not paid for the given unpaid month, if any.
 */
public class StudentMatchesFilterPredicate implements Predicate<Student> {
    private final Optional<Level> level;
    private final Set<Subject> subjects;
    private final Optional<PaymentMonth> paidMonth;
    private final Optional<PaymentMonth> unpaidMonth;

    /**
     * Creates a predicate that matches students at {@code level}, if present, who take all of {@code subjects}.
     */
    public StudentMatchesFilterPredicate(Optional<Level> level, Set<Subject> subjects) {
        this(level, subjects, Optional.empty(), Optional.empty());
    }

    /**
     * Creates a predicate that matches students at {@code level}, if present, who take all of {@code subjects},
     * have paid for {@code paidMonth}, if present, and have not paid for {@code unpaidMonth}, if present.
     */
    public StudentMatchesFilterPredicate(Optional<Level> level, Set<Subject> subjects,
            Optional<PaymentMonth> paidMonth, Optional<PaymentMonth> unpaidMonth) {
        requireAllNonNull(level, subjects, paidMonth, unpaidMonth);
        this.level = level;
        this.subjects = Set.copyOf(subjects);
        this.paidMonth = paidMonth;
        this.unpaidMonth = unpaidMonth;
    }

    @Override
    public boolean test(Student student) {
        boolean isLevelMatched = level.map(student.getLevel()::equals).orElse(true);
        boolean isPaidMatched = paidMonth.map(student::hasPaidFor).orElse(true);
        boolean isUnpaidMatched = unpaidMonth.map(month -> !student.hasPaidFor(month)).orElse(true);
        return isLevelMatched && student.getSubjects().containsAll(subjects) && isPaidMatched && isUnpaidMatched;
    }

    /** Returns a user-facing description of the active filter criteria. */
    public String getFilterDescription() {
        List<String> criteria = new ArrayList<>();
        level.ifPresent(value -> criteria.add(value.toString()));
        subjects.stream().map(Subject::toString).sorted().forEach(criteria::add);
        paidMonth.ifPresent(month -> criteria.add("Paid: " + month.toDisplayString()));
        unpaidMonth.ifPresent(month -> criteria.add("Unpaid: " + month.toDisplayString()));
        return String.join(", ", criteria);
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

        return level.equals(otherPredicate.level) && subjects.equals(otherPredicate.subjects)
                && paidMonth.equals(otherPredicate.paidMonth) && unpaidMonth.equals(otherPredicate.unpaidMonth);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("level", level)
                .add("subjects", subjects)
                .add("paidMonth", paidMonth)
                .add("unpaidMonth", unpaidMonth)
                .toString();
    }
}
