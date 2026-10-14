package seedu.address.logic.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.address.logic.commands.LinkGuardianCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/** Parses the {@code link-g} command. */
public class LinkGuardianCommandParser implements Parser<LinkGuardianCommand> {
    private static final Pattern FORMAT = Pattern.compile("\\s*(\\S+)\\s+g/(\\S+)\\s*");

    @Override
    public LinkGuardianCommand parse(String args) throws ParseException {
        Matcher matcher = FORMAT.matcher(args);
        if (!matcher.matches()) {
            throw new ParseException("Invalid command format.\n" + LinkGuardianCommand.MESSAGE_USAGE);
        }
        try {
            return new LinkGuardianCommand(ParserUtil.parseIndex(matcher.group(1)),
                    ParserUtil.parseIndex(matcher.group(2)));
        } catch (ParseException exception) {
            throw new ParseException("Invalid command format.\n" + LinkGuardianCommand.MESSAGE_USAGE);
        }
    }
}
