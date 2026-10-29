package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Objects;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.guardian.Guardian;
import seedu.address.model.student.Student;

/**
 * Represents the result of a command execution.
 */
public class CommandResult {

    /** Indicates the list to highlight for a find command. */
    public enum HighlightTarget { NONE, STUDENTS, GUARDIANS }

    /** Describes how the student filter label should be updated by the UI. */
    public enum StudentFilterUpdate { UNCHANGED, SET, CLEAR }

    /** Describes how the guardian search label should be updated by the UI. */
    public enum GuardianSearchUpdate { UNCHANGED, SET, CLEAR }

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
    private final List<String> highlightKeywords;
    private final HighlightTarget highlightTarget;
    private final StudentFilterUpdate studentFilterUpdate;
    private final String studentFilterDescription;
    private final GuardianSearchUpdate guardianSearchUpdate;
    private final String guardianSearchDescription;

    /**
     * Constructs a {@code CommandResult} with the specified fields.
     */
    public CommandResult(String feedbackToUser, boolean showHelp, boolean exit) {
        this(feedbackToUser, showHelp, exit, null, null, false, List.of(), HighlightTarget.NONE,
                StudentFilterUpdate.UNCHANGED, "");
    }

    /** Constructs a result that also identifies a student for the UI to highlight. */
    public CommandResult(String feedbackToUser, Student studentToHighlight) {
        this(feedbackToUser, false, false, studentToHighlight, null, false, List.of(), HighlightTarget.NONE,
                StudentFilterUpdate.UNCHANGED, "");
    }

    /** Constructs a result that identifies a guardian for the UI to highlight. */
    public CommandResult(String feedbackToUser, Guardian guardianToHighlight) {
        this(feedbackToUser, false, false, null, guardianToHighlight, false, List.of(), HighlightTarget.NONE,
                StudentFilterUpdate.UNCHANGED, "");
    }

    /** Constructs a result that specifies whether the UI should refresh the student list. */
    public CommandResult(String feedbackToUser, boolean refreshStudentList) {
        this(feedbackToUser, false, false, null, null, refreshStudentList, List.of(), HighlightTarget.NONE,
                StudentFilterUpdate.UNCHANGED, "");
    }

    /** Constructs a result that identifies search keywords and their highlight target. */
    public CommandResult(String feedbackToUser, List<String> highlightKeywords, HighlightTarget highlightTarget) {
        this(feedbackToUser, false, false, null, null, false, highlightKeywords, highlightTarget,
                StudentFilterUpdate.UNCHANGED, "");
    }

    private CommandResult(String feedbackToUser, boolean showHelp, boolean exit, Student studentToHighlight,
                          Guardian guardianToHighlight, boolean refreshStudentList,
                          List<String> highlightKeywords, HighlightTarget highlightTarget,
                          StudentFilterUpdate studentFilterUpdate, String studentFilterDescription) {
        this(feedbackToUser, showHelp, exit, studentToHighlight, guardianToHighlight, refreshStudentList,
                highlightKeywords, highlightTarget, studentFilterUpdate, studentFilterDescription,
                GuardianSearchUpdate.UNCHANGED, "");
    }

    private CommandResult(String feedbackToUser, boolean showHelp, boolean exit, Student studentToHighlight,
                          Guardian guardianToHighlight, boolean refreshStudentList,
                          List<String> highlightKeywords, HighlightTarget highlightTarget,
                          StudentFilterUpdate studentFilterUpdate, String studentFilterDescription,
                          GuardianSearchUpdate guardianSearchUpdate, String guardianSearchDescription) {
        this.feedbackToUser = requireNonNull(feedbackToUser);
        this.showHelp = showHelp;
        this.exit = exit;
        this.refreshStudentList = refreshStudentList;
        this.studentToHighlight = studentToHighlight;
        this.guardianToHighlight = guardianToHighlight;
        this.highlightKeywords = List.copyOf(highlightKeywords);
        this.highlightTarget = highlightTarget;
        this.studentFilterUpdate = requireNonNull(studentFilterUpdate);
        this.studentFilterDescription = requireNonNull(studentFilterDescription);
        this.guardianSearchUpdate = requireNonNull(guardianSearchUpdate);
        this.guardianSearchDescription = requireNonNull(guardianSearchDescription);
    }

    /**
     * Constructs a {@code CommandResult} with the specified {@code feedbackToUser},
     * and other fields set to their default value.
     */
    public CommandResult(String feedbackToUser) {
        this(feedbackToUser, false, false);
    }

    /** Creates a result that sets the active student filter label. */
    public static CommandResult withStudentFilterDescription(String feedbackToUser, String description) {
        return withStudentFilterDescription(feedbackToUser, description, List.of(), HighlightTarget.NONE);
    }

    /** Creates a result that sets the active student filter label and search highlighting. */
    public static CommandResult withStudentFilterDescription(String feedbackToUser, String description,
                                                             List<String> highlightKeywords,
                                                             HighlightTarget highlightTarget) {
        return new CommandResult(feedbackToUser, false, false, null, null, false, highlightKeywords,
                highlightTarget, StudentFilterUpdate.SET, description);
    }

    /** Creates a result that clears the active student filter label. */
    public static CommandResult clearStudentFilterDescription(String feedbackToUser) {
        return new CommandResult(feedbackToUser, false, false, null, null, false, List.of(), HighlightTarget.NONE,
                StudentFilterUpdate.CLEAR, "");
    }

    /** Creates a result that sets the active guardian search label and search highlighting. */
    public static CommandResult withGuardianSearchDescription(String feedbackToUser, String description,
                                                              List<String> highlightKeywords,
                                                              HighlightTarget highlightTarget) {
        return new CommandResult(feedbackToUser, false, false, null, null, false, highlightKeywords,
                highlightTarget, StudentFilterUpdate.UNCHANGED, "", GuardianSearchUpdate.SET, description);
    }

    /** Creates a result that clears the active guardian search label. */
    public static CommandResult clearGuardianSearchDescription(String feedbackToUser) {
        return new CommandResult(feedbackToUser, false, false, null, null, false, List.of(), HighlightTarget.NONE,
                StudentFilterUpdate.UNCHANGED, "", GuardianSearchUpdate.CLEAR, "");
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

    public List<String> getHighlightKeywords() {
        return highlightKeywords;
    }

    public HighlightTarget getHighlightTarget() {
        return highlightTarget;
    }

    public StudentFilterUpdate getStudentFilterUpdate() {
        return studentFilterUpdate;
    }

    public String getStudentFilterDescription() {
        return studentFilterDescription;
    }

    public GuardianSearchUpdate getGuardianSearchUpdate() {
        return guardianSearchUpdate;
    }

    public String getGuardianSearchDescription() {
        return guardianSearchDescription;
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
                && refreshStudentList == otherCommandResult.refreshStudentList
                && Objects.equals(studentToHighlight, otherCommandResult.studentToHighlight)
                && Objects.equals(guardianToHighlight, otherCommandResult.guardianToHighlight)
                && highlightKeywords.equals(otherCommandResult.highlightKeywords)
                && highlightTarget == otherCommandResult.highlightTarget
                && studentFilterUpdate == otherCommandResult.studentFilterUpdate
                && studentFilterDescription.equals(otherCommandResult.studentFilterDescription)
                && guardianSearchUpdate == otherCommandResult.guardianSearchUpdate
                && guardianSearchDescription.equals(otherCommandResult.guardianSearchDescription);
    }

    @Override
    public int hashCode() {
        return Objects.hash(feedbackToUser, showHelp, exit, refreshStudentList,
                studentToHighlight, guardianToHighlight, highlightKeywords, highlightTarget,
                studentFilterUpdate, studentFilterDescription, guardianSearchUpdate, guardianSearchDescription);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("feedbackToUser", feedbackToUser)
                .add("showHelp", showHelp)
                .add("exit", exit)
                .add("refreshStudentList", refreshStudentList)
                .add("studentFilterUpdate", studentFilterUpdate)
                .add("studentFilterDescription", studentFilterDescription)
                .add("guardianSearchUpdate", guardianSearchUpdate)
                .add("guardianSearchDescription", guardianSearchDescription)
                .toString();
    }

}
