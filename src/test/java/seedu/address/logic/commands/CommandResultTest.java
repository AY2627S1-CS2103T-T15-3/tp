package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.model.guardian.Guardian;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;

public class CommandResultTest {
    private static final Guardian GUARDIAN = new Guardian(new Name("Ben Tan"), new Phone("92345678"),
            new Email("ben@example.com"), new Address("25 Happy Road"));

    @Test
    public void equals() {
        CommandResult commandResult = new CommandResult("feedback");

        // same values -> returns true
        assertTrue(commandResult.equals(new CommandResult("feedback")));
        assertTrue(commandResult.equals(new CommandResult("feedback", false, false)));

        // same object -> returns true
        assertTrue(commandResult.equals(commandResult));

        // null -> returns false
        assertFalse(commandResult.equals(null));

        // different types -> returns false
        assertFalse(commandResult.equals(0.5f));

        // different feedbackToUser value -> returns false
        assertFalse(commandResult.equals(new CommandResult("different")));

        // different showHelp value -> returns false
        assertFalse(commandResult.equals(new CommandResult("feedback", true, false)));

        // different exit value -> returns false
        assertFalse(commandResult.equals(new CommandResult("feedback", false, true)));
    }

    @Test
    public void equals_statusDescriptionsAreCompared() {
        CommandResult studentFilterResult = CommandResult.withStudentFilterDescription(
                "feedback", "Filtered: Secondary 3");
        assertTrue(studentFilterResult.equals(CommandResult.withStudentFilterDescription(
                "feedback", "Filtered: Secondary 3")));
        assertFalse(studentFilterResult.equals(CommandResult.withStudentFilterDescription(
                "feedback", "Filtered: Secondary 4")));
        assertFalse(new CommandResult("feedback").equals(studentFilterResult));

        CommandResult guardianSearchResult = CommandResult.withGuardianSearchDescription(
                "feedback", "Search: Ben", List.of("Ben"), CommandResult.HighlightTarget.GUARDIANS);
        assertTrue(guardianSearchResult.equals(CommandResult.withGuardianSearchDescription(
                "feedback", "Search: Ben", List.of("Ben"), CommandResult.HighlightTarget.GUARDIANS)));
        assertFalse(guardianSearchResult.equals(CommandResult.withGuardianSearchDescription(
                "feedback", "Search: Alice", List.of("Alice"), CommandResult.HighlightTarget.GUARDIANS)));
        assertFalse(new CommandResult("feedback").equals(guardianSearchResult));
    }

    @Test
    public void hashcode() {
        CommandResult commandResult = new CommandResult("feedback");

        // same values -> returns same hashcode
        assertEquals(commandResult.hashCode(), new CommandResult("feedback").hashCode());

        // different feedbackToUser value -> returns different hashcode
        assertNotEquals(commandResult.hashCode(), new CommandResult("different").hashCode());

        // different showHelp value -> returns different hashcode
        assertNotEquals(commandResult.hashCode(), new CommandResult("feedback", true, false).hashCode());

        // different exit value -> returns different hashcode
        assertNotEquals(commandResult.hashCode(), new CommandResult("feedback", false, true).hashCode());
    }

    @Test
    public void toStringMethod() {
        CommandResult commandResult = new CommandResult("feedback");
        String expected = CommandResult.class.getCanonicalName() + "{feedbackToUser="
                + commandResult.getFeedbackToUser() + ", showHelp=" + commandResult.isShowHelp()
                + ", exit=" + commandResult.isExit()
                + ", refreshStudentList=" + commandResult.shouldRefreshStudentList()
                + ", studentFilterUpdate=" + commandResult.getStudentFilterUpdate()
                + ", studentFilterDescription=" + commandResult.getStudentFilterDescription()
                + ", guardianSearchUpdate=" + commandResult.getGuardianSearchUpdate()
                + ", guardianSearchDescription=" + commandResult.getGuardianSearchDescription() + "}";
        assertEquals(expected, commandResult.toString());
    }

    @Test
    public void refreshStudentList() {
        assertFalse(new CommandResult("feedback").shouldRefreshStudentList());
        assertFalse(new CommandResult("feedback", false, false).shouldRefreshStudentList());
        assertFalse(new CommandResult("feedback", GUARDIAN).shouldRefreshStudentList());
        assertTrue(new CommandResult("feedback", true).shouldRefreshStudentList());
        assertEquals(new CommandResult("feedback"), new CommandResult("feedback", false));
        assertNotEquals(new CommandResult("feedback"), new CommandResult("feedback", true));
    }

    @Test
    public void guardianHighlight_constructorStoresGuardian() {
        CommandResult result = new CommandResult("feedback", GUARDIAN);

        assertEquals(GUARDIAN, result.getGuardianToHighlight());
        assertEquals(null, result.getStudentToHighlight());
    }

    @Test
    public void searchHighlight_constructorStoresTargetAndKeywords() {
        List<String> keywords = List.of("ali", "bo");
        CommandResult result = new CommandResult("feedback", keywords,
                CommandResult.HighlightTarget.STUDENTS);

        assertEquals(keywords, result.getHighlightKeywords());
        assertEquals(CommandResult.HighlightTarget.STUDENTS, result.getHighlightTarget());
    }

    @Test
    public void defaultHighlight_isEmptyAndHasNoTarget() {
        CommandResult result = new CommandResult("feedback");

        assertEquals(List.of(), result.getHighlightKeywords());
        assertEquals(CommandResult.HighlightTarget.NONE, result.getHighlightTarget());
    }

    @Test
    public void studentFilterDescription_updateIsStored() {
        CommandResult setResult = CommandResult.withStudentFilterDescription(
                "feedback", "Filtered: Secondary 3");
        assertEquals(CommandResult.StudentFilterUpdate.SET, setResult.getStudentFilterUpdate());
        assertEquals("Filtered: Secondary 3", setResult.getStudentFilterDescription());

        CommandResult clearResult = CommandResult.clearStudentFilterDescription("feedback");
        assertEquals(CommandResult.StudentFilterUpdate.CLEAR, clearResult.getStudentFilterUpdate());
        assertEquals("", clearResult.getStudentFilterDescription());

        CommandResult unchangedResult = new CommandResult("feedback");
        assertEquals(CommandResult.StudentFilterUpdate.UNCHANGED, unchangedResult.getStudentFilterUpdate());
    }

    @Test
    public void guardianSearchDescription_updateIsStored() {
        CommandResult setResult = CommandResult.withGuardianSearchDescription(
                "feedback", "Search: Alice", List.of("Alice"), CommandResult.HighlightTarget.GUARDIANS);
        assertEquals(CommandResult.GuardianSearchUpdate.SET, setResult.getGuardianSearchUpdate());
        assertEquals("Search: Alice", setResult.getGuardianSearchDescription());

        CommandResult clearResult = CommandResult.clearGuardianSearchDescription("feedback");
        assertEquals(CommandResult.GuardianSearchUpdate.CLEAR, clearResult.getGuardianSearchUpdate());
        assertEquals("", clearResult.getGuardianSearchDescription());
    }
}
