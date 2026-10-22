package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.SetStudentRemarkCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.student.Remark;

/** Parses an index followed by literal free-text remark content. */
public class SetStudentRemarkCommandParser implements Parser<SetStudentRemarkCommand> {
    // Consume one separator after the index; preserve all remaining text for Remark validation.
    private static final Pattern ARGUMENTS = Pattern.compile("[ \\t]*(\\S+)(?:[ \\t](.*))?", Pattern.DOTALL);

    /**
     * Parses a positive student index followed by a required, nonblank remark.
     * Spaces and tabs may precede the index. One space or tab separates the index from the remark;
     * the remaining text is passed unchanged to {@link Remark} validation and normalization.
     * The displayed-list bounds are checked when the command executes.
     *
     * @param args raw arguments after the command word, preserving trailing characters for validation
     * @return a command to set or replace the selected student's remark
     * @throws ParseException if the index is missing or malformed, or the remark is missing or invalid
     */
    @Override
    public SetStudentRemarkCommand parse(String args) throws ParseException {
        Matcher matcher = ARGUMENTS.matcher(args);
        if (!matcher.matches()) {
            throw invalidFormat();
        }
        String indexToken = matcher.group(1);
        // parseIndex trims its input; reject controls before they can be discarded.
        if (indexToken.codePoints().anyMatch(Character::isISOControl)) {
            throw invalidFormat();
        }
        Index index;
        try {
            index = ParserUtil.parseIndex(indexToken);
        } catch (ParseException exception) {
            throw invalidFormat();
        }
        String text = matcher.group(2);
        if (text == null || text.isEmpty()) {
            throw new ParseException(SetStudentRemarkCommand.MESSAGE_REMARK_REQUIRED);
        }
        if (!Remark.isValidRemark(text)) {
            throw new ParseException(Remark.MESSAGE_CONSTRAINTS);
        }
        return new SetStudentRemarkCommand(index, new Remark(text));
    }

    private static ParseException invalidFormat() {
        return new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, SetStudentRemarkCommand.MESSAGE_USAGE));
    }
}
