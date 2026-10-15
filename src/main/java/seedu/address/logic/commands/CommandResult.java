package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.Objects;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.guardian.Guardian;
import seedu.address.model.student.Student;

/**
 * Represents the result of a command execution.
 */
public class CommandResult {

    private final String feedbackToUser;

    /** Help information should be shown to the user. */
    private final boolean showHelp;

    /** The application should exit. */
    private final boolean exit;

    /** The student list should be refreshed. */
    private final boolean refreshStudentList;

    /** The student that the UI should highlight, if any. */
    private final Student studentToHighlight;

    /** The guardian that the UI should highlight, if any. */
    private final Guardian guardianToHighlight;

    /**
     * Constructs a {@code CommandResult} with the specified fields.
     */
    public CommandResult(String feedbackToUser, boolean showHelp, boolean exit) {
        this(feedbackToUser, showHelp, exit, null, null, false);
    }

    /** Constructs a result that also identifies a student for the UI to highlight. */
    public CommandResult(String feedbackToUser, Student studentToHighlight) {
        this(feedbackToUser, false, false, studentToHighlight, null, false);
    }

    /** Constructs a result that identifies a guardian for the UI to highlight. */
    public CommandResult(String feedbackToUser, Guardian guardianToHighlight) {
        this(feedbackToUser, false, false, null, guardianToHighlight, false);
    }

    /** Constructs a result that specifies whether the UI should refresh the student list. */
    public CommandResult(String feedbackToUser, boolean refreshStudentList) {
        this(feedbackToUser, false, false, null, null, refreshStudentList);
    }

    private CommandResult(String feedbackToUser, boolean showHelp, boolean exit, Student studentToHighlight,
                          Guardian guardianToHighlight, boolean refreshStudentList) {
        this.feedbackToUser = requireNonNull(feedbackToUser);
        this.showHelp = showHelp;
        this.exit = exit;
        this.refreshStudentList = refreshStudentList;
        this.studentToHighlight = studentToHighlight;
        this.guardianToHighlight = guardianToHighlight;
    }

    /**
     * Constructs a {@code CommandResult} with the specified {@code feedbackToUser},
     * and other fields set to their default value.
     */
    public CommandResult(String feedbackToUser) {
        this(feedbackToUser, false, false);
    }

    public String getFeedbackToUser() {
        return feedbackToUser;
    }

    public boolean isShowHelp() {
        return showHelp;
    }

    public boolean isExit() {
        return exit;
    }

    public boolean shouldRefreshStudentList() {
        return refreshStudentList;
    }

    public Student getStudentToHighlight() {
        return studentToHighlight;
    }

    public Guardian getGuardianToHighlight() {
        return guardianToHighlight;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof CommandResult otherCommandResult)) {
            return false;
        }

        return feedbackToUser.equals(otherCommandResult.feedbackToUser)
                && showHelp == otherCommandResult.showHelp
                && exit == otherCommandResult.exit
                && refreshStudentList == otherCommandResult.refreshStudentList;
    }

    @Override
    public int hashCode() {
        return Objects.hash(feedbackToUser, showHelp, exit, refreshStudentList);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("feedbackToUser", feedbackToUser)
                .add("showHelp", showHelp)
                .add("exit", exit)
                .add("refreshStudentList", refreshStudentList)
                .toString();
    }

}
