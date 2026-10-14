package seedu.address.logic.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.address.logic.commands.LinkGuardianCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/** Parses the {@code link-g} command. */
public class LinkGuardianCommandParser implements Parser<LinkGuardianCommand> {
    private static final Pattern PREFIX_PATTERN = Pattern.compile("(?<!\\S)([A-Za-z][A-Za-z0-9-]*)/");
    private static final String GUARDIAN_PREFIX = "g/";

    @Override
    public LinkGuardianCommand parse(String args) throws ParseException {
        Map<String, List<String>> values = parseArguments(args);
        String studentIndexValue = values.remove("").getFirst();

        if (values.isEmpty()) {
            throw invalidFormat();
        }

        List<String> guardianIndexValues = values.get(GUARDIAN_PREFIX);
        if (guardianIndexValues.size() > 1) {
            throw new ParseException("The g/ parameter must not be specified more than once.");
        }

        try {
            return new LinkGuardianCommand(ParserUtil.parseIndex(studentIndexValue),
                    ParserUtil.parseIndex(guardianIndexValues.getFirst()));
        } catch (ParseException exception) {
            throw invalidFormat();
        }
    }

    private static Map<String, List<String>> parseArguments(String args) throws ParseException {
        Matcher matcher = PREFIX_PATTERN.matcher(args);
        List<PrefixMatch> matches = new ArrayList<>();
        while (matcher.find()) {
            matches.add(new PrefixMatch(matcher.start(), matcher.end(), matcher.group(1) + "/"));
        }

        if (matches.isEmpty() || args.substring(0, matches.getFirst().start()).trim().isEmpty()) {
            throw invalidFormat();
        }

        Map<String, List<String>> values = new HashMap<>();
        values.put("", List.of(args.substring(0, matches.getFirst().start()).trim()));
        for (int i = 0; i < matches.size(); i++) {
            PrefixMatch current = matches.get(i);
            int valueEnd = i + 1 < matches.size() ? matches.get(i + 1).start() : args.length();
            if (!GUARDIAN_PREFIX.equals(current.prefix())) {
                throw new ParseException("Unknown parameter: " + current.prefix());
            }
            values.computeIfAbsent(current.prefix(), unused -> new ArrayList<>())
                    .add(args.substring(current.end(), valueEnd).trim());
        }
        return values;
    }

    private static ParseException invalidFormat() {
        return new ParseException("Invalid command format.\n" + LinkGuardianCommand.MESSAGE_USAGE);
    }

    private record PrefixMatch(int start, int end, String prefix) {
    }
}
