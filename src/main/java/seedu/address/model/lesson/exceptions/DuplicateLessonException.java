package seedu.address.model.lesson.exceptions;

/** Signals that an operation would result in duplicate lessons. */
public class DuplicateLessonException extends RuntimeException {
    public DuplicateLessonException() {
        super("Operation would result in duplicate lessons");
    }

    public DuplicateLessonException(String message) {
        super(message);
    }
}
