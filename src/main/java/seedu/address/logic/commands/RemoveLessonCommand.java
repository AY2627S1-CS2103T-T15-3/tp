package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.lesson.Lesson;

/** Removes a lesson identified using its displayed index from TutorTrack. */
public class RemoveLessonCommand extends Command {
    public static final String COMMAND_WORD = "remove-l";
    public static final String MESSAGE_USAGE = "Usage: " + COMMAND_WORD + " INDEX";
    public static final String MESSAGE_INVALID_INDEX = "The lesson index provided is invalid.";
    public static final String MESSAGE_REMOVE_LESSON_SUCCESS = "Removed lesson: %1$s";

    private final Index targetIndex;

    public RemoveLessonCommand(Index targetIndex) {
        this.targetIndex = requireNonNull(targetIndex);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Lesson> lessons = model.getFilteredLessonList();
        if (targetIndex.getZeroBased() >= lessons.size()) {
            throw new CommandException(MESSAGE_INVALID_INDEX);
        }
        Lesson lessonToRemove = lessons.get(targetIndex.getZeroBased());
        model.deleteLesson(lessonToRemove);
        return new CommandResult(String.format(MESSAGE_REMOVE_LESSON_SUCCESS, lessonToRemove));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof RemoveLessonCommand otherCommand)) {
            return false;
        }
        return targetIndex.equals(otherCommand.targetIndex);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("targetIndex", targetIndex).toString();
    }
}
