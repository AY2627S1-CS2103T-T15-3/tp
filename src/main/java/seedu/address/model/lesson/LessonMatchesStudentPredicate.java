package seedu.address.model.lesson;

import static java.util.Objects.requireNonNull;

import java.util.function.Predicate;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.student.Student;

/** Tests that a {@code Lesson} belongs to the given student. */
public class LessonMatchesStudentPredicate implements Predicate<Lesson> {
    private final Student student;

    public LessonMatchesStudentPredicate(Student student) {
        this.student = requireNonNull(student);
    }

    @Override
    public boolean test(Lesson lesson) {
        return lesson.getStudent().equals(student);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof LessonMatchesStudentPredicate otherPredicate)) {
            return false;
        }
        return student.equals(otherPredicate.student);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("student", student).toString();
    }
}
