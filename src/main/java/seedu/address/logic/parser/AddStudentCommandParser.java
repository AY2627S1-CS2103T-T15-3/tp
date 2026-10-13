package seedu.address.logic.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.address.logic.commands.AddStudentCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.model.student.Level;
import seedu.address.model.student.Student;
import seedu.address.model.student.Subject;

/** Parses the {@code add-s} command. */
public class AddStudentCommandParser implements Parser<AddStudentCommand> {
    private static final String MESSAGE_INVALID_FORMAT = "Invalid command format.\n";
    private static final Pattern PREFIX_PATTERN = Pattern.compile("(?<!\\S)([A-Za-z][A-Za-z0-9-]*)/");
    private static final Set<String> SUPPORTED_PREFIXES = Set.of("n/", "p/", "e/", "a/", "l/", "s/");
    private static final Set<String> SINGLE_VALUE_PREFIXES = Set.of("n/", "p/", "e/", "a/", "l/");

    @Override
    public AddStudentCommand parse(String args) throws ParseException {
        Map<String, List<String>> values = parseArguments(args);

        for (String prefix : SINGLE_VALUE_PREFIXES) {
            if (values.getOrDefault(prefix, List.of()).size() > 1) {
                throw new ParseException("The " + prefix + " parameter must not be specified more than once.");
            }
        }

        if (!values.keySet().containsAll(SUPPORTED_PREFIXES)
                || values.getOrDefault("s/", List.of()).isEmpty()) {
            throw invalidFormat();
        }

        Name name = ParserUtil.parseStudentName(values.get("n/").get(0));
        Phone phone = ParserUtil.parseStudentPhone(values.get("p/").get(0));
        Email email = ParserUtil.parseStudentEmail(values.get("e/").get(0));
        Address address = ParserUtil.parseStudentAddress(values.get("a/").get(0));
        Level level = ParserUtil.parseStudentLevel(values.get("l/").get(0));
        Set<Subject> subjects = new LinkedHashSet<>();
        for (String subject : values.get("s/")) {
            subjects.add(ParserUtil.parseStudentSubject(subject));
        }

        return new AddStudentCommand(new Student(name, phone, email, address, level, subjects));
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
            String prefix = current.prefix();
            if (!SUPPORTED_PREFIXES.contains(prefix)) {
                throw new ParseException("Unknown parameter: " + prefix);
            }
            values.computeIfAbsent(prefix, unused -> new ArrayList<>())
                    .add(args.substring(current.end(), valueEnd).trim());
        }
        return values;
    }

    private static ParseException invalidFormat() {
        return new ParseException(MESSAGE_INVALID_FORMAT + AddStudentCommand.MESSAGE_USAGE);
    }

    private record PrefixMatch(int start, int end, String prefix) {
    }
}
