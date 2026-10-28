package seedu.address.logic;

import java.time.format.DateTimeFormatter;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import seedu.address.logic.parser.Prefix;
import seedu.address.model.guardian.Guardian;
import seedu.address.model.lesson.Lesson;
import seedu.address.model.student.Student;

/**
 * Container for user visible messages.
 */
public class Messages {
    public static final DateTimeFormatter LESSON_DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static final String MESSAGE_UNKNOWN_COMMAND = "Unknown command.";
    public static final String MESSAGE_INVALID_COMMAND_FORMAT = "Invalid command format!\n%1$s";
    public static final String MESSAGE_INVALID_GUARDIAN_DISPLAYED_INDEX = "The guardian index provided is invalid.";
    public static final String MESSAGE_INVALID_STUDENT_DISPLAYED_INDEX = "The student index provided is invalid.";
    public static final String MESSAGE_INVALID_LESSON_DISPLAYED_INDEX = "The lesson index provided is invalid.";
    public static final String MESSAGE_STUDENT_INDEX_OUT_OF_RANGE = "Student index is out of range, "
            + "choose a valid index from the currently displayed student list";
    public static final String MESSAGE_NO_STUDENTS_DISPLAYED = "No students are currently displayed.";
    public static final String MESSAGE_STUDENTS_LISTED_OVERVIEW = "%1$d student(s) listed.";
    public static final String MESSAGE_GUARDIANS_LISTED_OVERVIEW = "%1$d guardian(s) listed.";
    public static final String MESSAGE_NO_GUARDIANS_MATCH_SEARCH = "No guardians match the given search.";
    public static final String MESSAGE_NO_STUDENTS_MATCH_SEARCH = "No students match the given search.";
    public static final String MESSAGE_NO_STUDENTS_MATCH_FILTER = "No students match the given filter.";
    public static final String MESSAGE_DUPLICATE_FIELDS =
                "Multiple values specified for the following single-valued field(s): ";

    /**
     * Returns an error message indicating the duplicate prefixes.
     */
    public static String getErrorMessageForDuplicatePrefixes(Prefix... duplicatePrefixes) {
        assert duplicatePrefixes.length > 0;

        Set<String> duplicateFields =
                Stream.of(duplicatePrefixes).map(Prefix::toString).collect(Collectors.toSet());

        return MESSAGE_DUPLICATE_FIELDS + String.join(" ", duplicateFields);
    }

    /** Formats a student for display to the user. */
    public static String format(Student student) {
        final StringBuilder builder = new StringBuilder();
        builder.append(student.getName())
                .append("; Phone: ").append(student.getPhone())
                .append("; Email: ").append(student.getEmail())
                .append("; Address: ").append(student.getAddress())
                .append("; Level: ").append(student.getLevel())
                .append("; Subjects: ");
        builder.append(student.getSubjects().stream()
                .map(subject -> subject.toString())
                .collect(Collectors.joining(", ")));
        return builder.toString();
    }

    /** Formats a guardian for display to the user. */
    public static String format(Guardian guardian) {
        return guardian.getName() + "; Phone: " + guardian.getPhone()
                + "; Email: " + guardian.getEmail() + "; Address: " + guardian.getAddress();
    }

    /** Formats a lesson for display to the user. */
    public static String format(Lesson lesson) {
        return lesson.getStudent().getName() + "; Start: "
                + lesson.getStartDateTime().format(LESSON_DATE_TIME_FORMATTER) + "; End: "
                + lesson.getEndDateTime().format(LESSON_DATE_TIME_FORMATTER);
    }

}
