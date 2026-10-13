package seedu.address.logic.parser;

import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.LinkedHashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.AddStudentCommand;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.model.student.Level;
import seedu.address.model.student.Student;
import seedu.address.model.student.StudentFieldValidator;
import seedu.address.model.student.Subject;

public class AddStudentCommandParserTest {
    private final AddStudentCommandParser parser = new AddStudentCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        Student expected = student("Henry Tan", "91234567", "henry@example.com", "12 ABCD Ave 3", "Secondary 3",
                "Mathematics", "Physics");
        assertParseSuccess(parser,
                "n/Henry   Tan p/91234567 e/henry@example.com a/12 ABCD Ave 3 l/Secondary   3 "
                        + "s/Mathematics s/Physics",
                new AddStudentCommand(expected));
    }

    @Test
    public void parse_nameNormalisationAndSpecialCharacters_success() {
        Student expected = student("Mary-Jane O'Connor", "+6591234567", "mary@example.com",
                "12 ABCD Ave 3, #04-15", "JC 1", "A-Level (H2)");
        assertParseSuccess(parser,
                "n/  Mary-Jane  O'Connor  p/+6591234567 e/mary@example.com a/12 ABCD Ave 3, #04-15 "
                        + "l/JC 1 s/A-Level (H2)", new AddStudentCommand(expected));
    }

    @Test
    public void parse_duplicateSubjects_deduplicates() {
        Student expected = student("Henry Tan", "91234567", "henry@example.com", "12 ABCD Ave 3", "Secondary 3",
                "Mathematics");
        assertParseSuccess(parser,
                "n/Henry Tan p/91234567 e/henry@example.com a/12 ABCD Ave 3 l/Secondary 3 "
                        + "s/Mathematics s/mathematics", new AddStudentCommand(expected));
    }

    @Test
    public void parse_missingMandatoryPrefix_failure() {
        String args = "n/Henry Tan p/91234567 e/henry@example.com a/12 ABCD Ave 3 l/Secondary 3";
        assertParseFailure(parser, args, "Invalid command format.\n" + AddStudentCommand.MESSAGE_USAGE);
    }

    @Test
    public void parse_repeatedSingleValuePrefix_failure() {
        String args = "n/Henry Tan n/Lucy Lim p/91234567 e/henry@example.com a/12 ABCD Ave 3 "
                + "l/Secondary 3 s/Mathematics";
        assertParseFailure(parser, args, "The n/ parameter must not be specified more than once.");
    }

    @Test
    public void parse_unknownPrefix_failure() {
        String args = "n/Henry Tan p/91234567 e/henry@example.com a/12 ABCD Ave 3 l/Secondary 3 "
                + "s/Mathematics x/value";
        assertParseFailure(parser, args, "Unknown parameter: x/");
    }

    @Test
    public void parse_invalidValues_failure() {
        assertParseFailure(parser,
                "n/Henry&Tan p/91234567 e/henry@example.com a/12 ABCD Ave 3 l/Secondary 3 s/Mathematics",
                StudentFieldValidator.NAME_MESSAGE);
        assertParseFailure(parser,
                "n/Henry Tan p/12 345 e/henry@example.com a/12 ABCD Ave 3 l/Secondary 3 s/Mathematics",
                StudentFieldValidator.PHONE_MESSAGE);
        assertParseFailure(parser,
                "n/Henry Tan p/91234567 e/henry@example a/12 ABCD Ave 3 l/Secondary 3 s/Mathematics",
                StudentFieldValidator.EMAIL_MESSAGE);
    }

    private static Student student(String name, String phone, String email, String address, String level,
            String... subjects) {
        Set<Subject> subjectSet = new LinkedHashSet<>();
        for (String subject : subjects) {
            subjectSet.add(new Subject(subject));
        }
        return new Student(Name.fromStudentValue(name), Phone.fromStudentValue(phone), Email.fromStudentValue(email),
                Address.fromStudentValue(address), new Level(level), subjectSet);
    }

}
