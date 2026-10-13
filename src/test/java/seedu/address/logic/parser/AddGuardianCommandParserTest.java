package seedu.address.logic.parser;

import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.AddGuardianCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.guardian.Guardian;
import seedu.address.model.guardian.GuardianFieldValidator;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;

public class AddGuardianCommandParserTest {
    private final AddGuardianCommandParser parser = new AddGuardianCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        Guardian expected = guardian("Ben Tan", "92345678", "ben.tan@gmail.com", "12 ABCD Ave 3");
        assertParseSuccess(parser,
                "n/Ben   Tan p/92345678 e/ben.tan@gmail.com a/12 ABCD Ave 3",
                new AddGuardianCommand(expected));
    }

    @Test
    public void parse_specialCharacters_success() {
        Guardian expected = guardian("Billie O'Connor", "+6598765432", "billie@example.com", "25 Happy Road");
        assertParseSuccess(parser,
                "n/ Billie O'Connor p/+6598765432 e/billie@example.com a/25 Happy Road",
                new AddGuardianCommand(expected));
    }

    @Test
    public void parse_missingMandatoryPrefix_failure() {
        assertParseFailure(parser, "n/Ben Tan p/92345678 e/ben@example.com",
                "Invalid command format.\n" + AddGuardianCommand.MESSAGE_USAGE);
    }

    @Test
    public void parse_repeatedSingleValuePrefix_failure() {
        assertParseFailure(parser,
                "n/Ben Tan n/Bill Tan p/92345678 e/ben@example.com a/12 ABCD Ave 3",
                "The n/ parameter must not be specified more than once.");
    }

    @Test
    public void parse_unknownPrefix_failure() {
        assertParseFailure(parser,
                "n/Ben Tan p/92345678 e/ben@example.com a/12 ABCD Ave 3 r/Father",
                "Unknown parameter: r/");
    }

    @Test
    public void parse_textBeforeFirstPrefix_failure() {
        assertParseFailure(parser,
                "unexpected n/Ben Tan p/92345678 e/ben@example.com a/12 ABCD Ave 3",
                "Invalid command format.\n" + AddGuardianCommand.MESSAGE_USAGE);
    }

    @Test
    public void parse_invalidValues_failure() throws ParseException {
        assertParseFailure(parser,
                "n/Ben&Tan p/92345678 e/ben@example.com a/12 ABCD Ave 3",
                GuardianFieldValidator.NAME_MESSAGE);
        assertParseFailure(parser,
                "n/Ben Tan p/12 345 e/ben@example.com a/12 ABCD Ave 3",
                GuardianFieldValidator.PHONE_MESSAGE);
        assertParseFailure(parser,
                "n/Ben Tan p/92345678 e/ben@example a/12 ABCD Ave 3",
                GuardianFieldValidator.EMAIL_MESSAGE);
        assertParseFailure(parser,
                "n/Ben Tan p/92345678 e/ben@example.com a/12 ABCD Ave @",
                GuardianFieldValidator.ADDRESS_MESSAGE);
    }

    private static Guardian guardian(String name, String phone, String email, String address) {
        return new Guardian(Name.fromStudentValue(name), Phone.fromStudentValue(phone),
                Email.fromStudentValue(email), Address.fromStudentValue(address));
    }
}
