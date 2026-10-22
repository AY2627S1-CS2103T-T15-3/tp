package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.logic.commands.ClearStudentRemarkCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/** Parses a single displayed student index. */
public class ClearStudentRemarkCommandParser implements Parser<ClearStudentRemarkCommand> {
    /**
     * Parses a single positive student index using the shared {@link ParserUtil#parseIndex(String)} rules.
     * The displayed-list bounds are checked when the command executes.
     *
     * @param args the index text after the command word
     * @return a command to clear the selected student's remark
     * @throws ParseException if the index is missing or malformed, or additional arguments are supplied
     */
    @Override
    public ClearStudentRemarkCommand parse(String args) throws ParseException {
        try {
            return new ClearStudentRemarkCommand(ParserUtil.parseIndex(args));
        } catch (ParseException exception) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, ClearStudentRemarkCommand.MESSAGE_USAGE), exception);
        }
    }
}
