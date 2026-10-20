package seedu.address.model.student.exceptions;

/** Signals that an operation would result in duplicate lessons. */
public class DuplicateLessonException extends RuntimeException {
    public DuplicateLessonException() {
        super("Operation would result in duplicate lessons");
    }
}
