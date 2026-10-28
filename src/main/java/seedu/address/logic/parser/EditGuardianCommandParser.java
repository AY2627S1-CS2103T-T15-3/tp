package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.EditGuardianCommand;
import seedu.address.logic.commands.EditGuardianDescriptor;
import seedu.address.logic.parser.exceptions.ParseException;
/** Parses the {@code edit-g} command. */
public class EditGuardianCommandParser implements Parser<EditGuardianCommand> {
    /* Treat the relationship marker "s/o" as name content, not as an unknown prefix. */
    private static final Pattern PREFIX_PATTERN =
            Pattern.compile("(?<!\\S)(?!s/o(?:\\s|$))([A-Za-z][A-Za-z0-9-]*)/");
    private static final Set<String> SUPPORTED_PREFIXES = Set.of("n/", "p/", "e/", "a/");

    @Override
    public EditGuardianCommand parse(String args) throws ParseException {
        Map<String, List<String>> values = parseArguments(args);
        Index index;
        try {
            index = ParserUtil.parseIndex(values.remove("").get(0));
        } catch (ParseException exception) {
            throw invalidFormat();
        }

        for (String prefix : SUPPORTED_PREFIXES) {
            if (values.getOrDefault(prefix, List.of()).size() > 1) {
                throw new ParseException("The " + prefix + " parameter must not be specified more than once.");
            }
        }
        if (values.isEmpty()) {
            throw new ParseException(EditGuardianCommand.MESSAGE_NOT_EDITED);
        }

        EditGuardianDescriptor descriptor = new EditGuardianDescriptor();
        if (values.containsKey("n/")) {
            descriptor.setName(ParserUtil.parseGuardianName(values.get("n/").get(0)));
        }
        if (values.containsKey("p/")) {
            descriptor.setPhone(ParserUtil.parseGuardianPhone(values.get("p/").get(0)));
        }
        if (values.containsKey("e/")) {
            descriptor.setEmail(ParserUtil.parseGuardianEmail(values.get("e/").get(0)));
        }
        if (values.containsKey("a/")) {
            descriptor.setAddress(ParserUtil.parseGuardianAddress(values.get("a/").get(0)));
        }
        return new EditGuardianCommand(index, descriptor);
    }

    private static Map<String, List<String>> parseArguments(String args) throws ParseException {
        Matcher matcher = PREFIX_PATTERN.matcher(args);
        List<PrefixMatch> matches = new ArrayList<>();
        while (matcher.find()) {
            matches.add(new PrefixMatch(matcher.start(), matcher.end(), matcher.group(1) + "/"));
        }
        if (matches.isEmpty()) {
            if (args.trim().isEmpty()) {
                throw invalidFormat();
            }
            Map<String, List<String>> values = new HashMap<>();
            values.put("", List.of(args.trim()));
            return values;
        }
        if (args.substring(0, matches.get(0).start()).trim().isEmpty()) {
            throw invalidFormat();
        }

        Map<String, List<String>> values = new HashMap<>();
        values.put("", List.of(args.substring(0, matches.get(0).start()).trim()));
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
        return new ParseException(
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditGuardianCommand.MESSAGE_USAGE));
    }

    private record PrefixMatch(int start, int end, String prefix) {
    }
}
