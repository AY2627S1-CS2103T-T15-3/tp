package seedu.address.logic.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.EditStudentCommand;
import seedu.address.logic.commands.EditStudentDescriptor;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.model.student.Level;
import seedu.address.model.student.Subject;

/** Parses the {@code edit-s} command. */
public class EditStudentCommandParser implements Parser<EditStudentCommand> {
    /* Treat the relationship marker "s/o" as name content rather than the subject prefix "s/". */
    private static final Pattern PREFIX_PATTERN =
            Pattern.compile("(?<!\\S)(?!s/o(?:\\s|$))([a-z][a-z0-9-]*)/");
    private static final Set<String> SUPPORTED_PREFIXES = Set.of("n/", "p/", "e/", "a/", "l/", "s/");
    private static final Set<String> SINGLE_VALUE_PREFIXES = Set.of("n/", "p/", "e/", "a/", "l/");

    @Override
    public EditStudentCommand parse(String args) throws ParseException {
        Map<String, List<String>> values = parseArguments(args);
        Index index;
        try {
            index = ParserUtil.parseIndex(values.remove("").get(0));
        } catch (ParseException exception) {
            throw invalidFormat();
        }

        for (String prefix : SINGLE_VALUE_PREFIXES) {
            if (values.getOrDefault(prefix, List.of()).size() > 1) {
                throw new ParseException("The " + prefix + " parameter must not be specified more than once.");
            }
        }
        if (values.isEmpty()) {
            throw new ParseException(EditStudentCommand.MESSAGE_NOT_EDITED);
        }

        EditStudentDescriptor descriptor = new EditStudentDescriptor();
        if (values.containsKey("n/")) {
            descriptor.setName(parseName(values, "n/"));
        }
        if (values.containsKey("p/")) {
            descriptor.setPhone(parsePhone(values, "p/"));
        }
        if (values.containsKey("e/")) {
            descriptor.setEmail(parseEmail(values, "e/"));
        }
        if (values.containsKey("a/")) {
            descriptor.setAddress(parseAddress(values, "a/"));
        }
        if (values.containsKey("l/")) {
            descriptor.setLevel(parseLevel(values, "l/"));
        }
        if (values.containsKey("s/")) {
            Set<Subject> subjects = new LinkedHashSet<>();
            for (String subject : values.get("s/")) {
                subjects.add(ParserUtil.parseStudentSubject(subject));
            }
            descriptor.setSubjects(subjects);
        }
        return new EditStudentCommand(index, descriptor);
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

    private static Name parseName(Map<String, List<String>> values, String prefix) throws ParseException {
        return ParserUtil.parseStudentName(values.get(prefix).get(0));
    }

    private static Phone parsePhone(Map<String, List<String>> values, String prefix) throws ParseException {
        return ParserUtil.parseStudentPhone(values.get(prefix).get(0));
    }

    private static Email parseEmail(Map<String, List<String>> values, String prefix) throws ParseException {
        return ParserUtil.parseStudentEmail(values.get(prefix).get(0));
    }

    private static Address parseAddress(Map<String, List<String>> values, String prefix) throws ParseException {
        return ParserUtil.parseStudentAddress(values.get(prefix).get(0));
    }

    private static Level parseLevel(Map<String, List<String>> values, String prefix) throws ParseException {
        return ParserUtil.parseStudentLevel(values.get(prefix).get(0));
    }

    private static ParseException invalidFormat() {
        return new ParseException("Invalid command format.\n" + EditStudentCommand.MESSAGE_USAGE);
    }

    private record PrefixMatch(int start, int end, String prefix) {
    }
}
