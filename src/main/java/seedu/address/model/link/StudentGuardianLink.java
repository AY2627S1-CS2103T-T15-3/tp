package seedu.address.model.link;

import static java.util.Objects.requireNonNull;

import java.util.Objects;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.guardian.Guardian;
import seedu.address.model.student.Student;

/** Represents a relationship between one student and one guardian. */
public class StudentGuardianLink {
    private final Student student;
    private final Guardian guardian;

    /** Creates a link between the given student and guardian. */
    public StudentGuardianLink(Student student, Guardian guardian) {
        this.student = requireNonNull(student);
        this.guardian = requireNonNull(guardian);
    }

    /** Returns the student in this link. */
    public Student getStudent() {
        return student;
    }

    /** Returns the guardian in this link. */
    public Guardian getGuardian() {
        return guardian;
    }

    /** Returns true if this link connects the same student and guardian as the other link. */
    public boolean isSameLink(StudentGuardianLink other) {
        return other != null
                && student.equals(other.student)
                && guardian.equals(other.guardian);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof StudentGuardianLink otherLink)) {
            return false;
        }
        return student.equals(otherLink.student) && guardian.equals(otherLink.guardian);
    }

    @Override
    public int hashCode() {
        return Objects.hash(student, guardian);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("student", student)
                .add("guardian", guardian)
                .toString();
    }
}
