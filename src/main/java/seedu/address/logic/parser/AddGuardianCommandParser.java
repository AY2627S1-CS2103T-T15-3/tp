package seedu.address.logic.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.address.logic.commands.AddGuardianCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.guardian.Guardian;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;

/** Parses the {@code add-g} command. */
public class AddGuardianCommandParser implements Parser<AddGuardianCommand> {
    private static final String MESSAGE_INVALID_FORMAT = "Invalid command format.\n";
    private static final Pattern PREFIX_PATTERN = Pattern.compile("(?<!\\S)([A-Za-z][A-Za-z0-9-]*)/");
    private static final Set<String> SUPPORTED_PREFIXES = Set.of("n/", "p/", "e/", "a/");

    @Override
    public AddGuardianCommand parse(String args) throws ParseException {
        Map<String, List<String>> values = parseArguments(args);
        for (String prefix : SUPPORTED_PREFIXES) {
            if (values.getOrDefault(prefix, List.of()).size() > 1) {
                throw new ParseException("The " + prefix + " parameter must not be specified more than once.");
            }
        }
        if (!values.keySet().containsAll(SUPPORTED_PREFIXES)) {
            throw invalidFormat();
        }

        Name name = ParserUtil.parseGuardianName(values.get("n/").get(0));
        Phone phone = ParserUtil.parseGuardianPhone(values.get("p/").get(0));
        Email email = ParserUtil.parseGuardianEmail(values.get("e/").get(0));
        Address address = ParserUtil.parseGuardianAddress(values.get("a/").get(0));
        return new AddGuardianCommand(new Guardian(name, phone, email, address));
    }

    private static Map<String, List<String>> parseArguments(String args) throws ParseException {
        Matcher matcher = PREFIX_PATTERN.matcher(args);
        List<PrefixMatch> matches = new ArrayList<>();
        while (matcher.find()) {
            matches.add(new PrefixMatch(matcher.start(), matcher.end(), matcher.group(1) + "/"));
        }

        if (matches.isEmpty() || !args.substring(0, matches.get(0).start()).trim().isEmpty()) {
            throw invalidFormat();
        }

        Map<String, List<String>> values = new HashMap<>();
        for (int i = 0; i < matches.size(); i++) {
            PrefixMatch current = matches.get(i);
            int valueEnd = i + 1 < matches.size() ? matches.get(i + 1).start() : args.length();
            if (!SUPPORTED_PREFIXES.contains(current.prefix())) {
                throw new ParseException("Unknown parameter: " + current.prefix());
            }
            values.computeIfAbsent(current.prefix(), unused -> new ArrayList<>())
                    .add(args.substring(current.end(), valueEnd).trim());
        }
        return values;
    }

    private static ParseException invalidFormat() {
        return new ParseException(MESSAGE_INVALID_FORMAT + AddGuardianCommand.MESSAGE_USAGE);
    }

    private record PrefixMatch(int start, int end, String prefix) {
    }
}
