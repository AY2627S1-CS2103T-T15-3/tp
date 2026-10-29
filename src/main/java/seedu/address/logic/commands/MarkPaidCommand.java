package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MONTH;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.student.PaymentMonth;
import seedu.address.model.student.Student;

/**
 * Marks a student, identified using its displayed index, as having paid for a given month.
 */
public class MarkPaidCommand extends Command {

    public static final String COMMAND_WORD = "mark-paid";

    public static final String MESSAGE_USAGE = "Usage: " + COMMAND_WORD + " INDEX " + PREFIX_MONTH + "YYYY-MM";

    public static final String MESSAGE_SUCCESS = "Marked %1$s as paid for %2$s.";
    public static final String MESSAGE_ALREADY_PAID = "%1$s is already marked as paid for %2$s.";

    private final Index targetIndex;
    private final PaymentMonth paymentMonth;

    /**
     * Creates a command to mark the student at {@code targetIndex} as paid for {@code paymentMonth}.
     */
    public MarkPaidCommand(Index targetIndex, PaymentMonth paymentMonth) {
        this.targetIndex = requireNonNull(targetIndex);
        this.paymentMonth = requireNonNull(paymentMonth);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Student> lastShownList = model.getFilteredStudentList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_STUDENT_DISPLAYED_INDEX);
        }

        Student studentToMark = lastShownList.get(targetIndex.getZeroBased());
        if (studentToMark.hasPaidFor(paymentMonth)) {
            throw new CommandException(String.format(MESSAGE_ALREADY_PAID,
                    studentToMark.getName(), paymentMonth.toDisplayString()));
        }

        Student markedStudent = studentToMark.withPaymentFor(paymentMonth);
        model.setStudent(studentToMark, markedStudent);
        return new CommandResult(String.format(MESSAGE_SUCCESS,
                markedStudent.getName(), paymentMonth.toDisplayString()), markedStudent);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof MarkPaidCommand otherCommand)) {
            return false;
        }
        return targetIndex.equals(otherCommand.targetIndex)
                && paymentMonth.equals(otherCommand.paymentMonth);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndex", targetIndex)
                .add("paymentMonth", paymentMonth)
                .toString();
    }
}
