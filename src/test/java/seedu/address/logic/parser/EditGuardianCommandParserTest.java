package seedu.address.logic.parser;

import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.EditGuardianCommand;
import seedu.address.logic.commands.EditGuardianDescriptor;
import seedu.address.model.guardian.GuardianFieldValidator;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;

public class EditGuardianCommandParserTest {
    private final EditGuardianCommandParser parser = new EditGuardianCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        EditGuardianDescriptor descriptor = new EditGuardianDescriptor();
        descriptor.setName(new Name("Ben Lim"));
        descriptor.setPhone(new Phone("98765432"));
        descriptor.setEmail(new Email("ben.lim@example.com"));
        descriptor.setAddress(new Address("25 Happy Road"));

        assertParseSuccess(parser, "2 n/Ben Lim p/98765432 e/ben.lim@example.com a/25 Happy Road",
                new EditGuardianCommand(Index.fromOneBased(2), descriptor));
    }

    @Test
    public void parse_singleField_success() {
        EditGuardianDescriptor descriptor = new EditGuardianDescriptor();
        descriptor.setAddress(new Address("31 Clementi Avenue"));

        assertParseSuccess(parser, "1 a/31 Clementi Avenue",
                new EditGuardianCommand(Index.fromOneBased(1), descriptor));
    }

    @Test
    public void parse_missingField_failure() {
        assertParseFailure(parser, "2", EditGuardianCommand.MESSAGE_NOT_EDITED);
    }

    @Test
    public void parse_invalidIndex_failure() {
        assertParseFailure(parser, "0 n/Ben", "Invalid command format.\n" + EditGuardianCommand.MESSAGE_USAGE);
        assertParseFailure(parser, "abc n/Ben", "Invalid command format.\n" + EditGuardianCommand.MESSAGE_USAGE);
    }

    @Test
    public void parse_repeatedSingleValuePrefix_failure() {
        assertParseFailure(parser, "1 n/Ben Tan n/Ben Lim",
                "The n/ parameter must not be specified more than once.");
    }

    @Test
    public void parse_unknownPrefix_failure() {
        assertParseFailure(parser, "1 x/Test", "Unknown parameter: x/");
    }

    @Test
    public void parse_invalidValue_failure() {
        assertParseFailure(parser, "1 n/Ben&Tan", GuardianFieldValidator.NAME_MESSAGE);
    }
}
