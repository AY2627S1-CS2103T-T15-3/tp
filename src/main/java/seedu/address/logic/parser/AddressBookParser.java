package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.Messages.MESSAGE_UNKNOWN_COMMAND;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.commands.AddGuardianCommand;
import seedu.address.logic.commands.AddLessonCommand;
import seedu.address.logic.commands.AddStudentCommand;
import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.commands.ClearStudentRemarkCommand;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.EditGuardianCommand;
import seedu.address.logic.commands.EditStudentCommand;
import seedu.address.logic.commands.ExitCommand;
import seedu.address.logic.commands.FilterCommand;
import seedu.address.logic.commands.FindGuardianCommand;
import seedu.address.logic.commands.FindStudentCommand;
import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.commands.LinkGuardianCommand;
import seedu.address.logic.commands.ListGuardianCommand;
import seedu.address.logic.commands.ListStudentCommand;
import seedu.address.logic.commands.MarkPaidCommand;
import seedu.address.logic.commands.RemoveGuardianCommand;
import seedu.address.logic.commands.RemoveLessonCommand;
import seedu.address.logic.commands.RemoveStudentCommand;
import seedu.address.logic.commands.SetStudentRemarkCommand;
import seedu.address.logic.commands.SortStudentCommand;
import seedu.address.logic.commands.UnlinkGuardianCommand;
import seedu.address.logic.commands.ViewLessonCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses user input.
 */
public class AddressBookParser {

    /**
     * Used for initial separation of command word and args.
     */
    private static final Pattern BASIC_COMMAND_FORMAT = Pattern.compile("(?<commandWord>\\S+)(?<arguments>.*)");
    private static final Logger logger = LogsCenter.getLogger(AddressBookParser.class);
    private static final Map<String, String> COMMAND_WORD_ALIASES = createCommandWordAliases();

    private static Map<String, String> createCommandWordAliases() {
        Map<String, String> aliases = new HashMap<>();
        List.of(
                AddGuardianCommand.COMMAND_WORD,
                AddLessonCommand.COMMAND_WORD,
                AddStudentCommand.COMMAND_WORD,
                EditGuardianCommand.COMMAND_WORD,
                LinkGuardianCommand.COMMAND_WORD,
                UnlinkGuardianCommand.COMMAND_WORD,
                EditStudentCommand.COMMAND_WORD,
                RemoveGuardianCommand.COMMAND_WORD,
                RemoveStudentCommand.COMMAND_WORD,
                SetStudentRemarkCommand.COMMAND_WORD,
                ClearStudentRemarkCommand.COMMAND_WORD,
                MarkPaidCommand.COMMAND_WORD,
                ClearCommand.COMMAND_WORD,
                FilterCommand.COMMAND_WORD,
                FindGuardianCommand.COMMAND_WORD,
                FindStudentCommand.COMMAND_WORD,
                ListGuardianCommand.COMMAND_WORD,
                ListStudentCommand.COMMAND_WORD,
                SortStudentCommand.COMMAND_WORD,
                RemoveLessonCommand.COMMAND_WORD,
                ViewLessonCommand.COMMAND_WORD,
                ExitCommand.COMMAND_WORD,
                HelpCommand.COMMAND_WORD)
                .forEach(commandWord -> addCommandWordAliases(aliases, commandWord));
        return Map.copyOf(aliases);
    }

    private static void addCommandWordAliases(Map<String, String> aliases, String commandWord) {
        aliases.put(commandWord, commandWord);
        aliases.put(Character.toUpperCase(commandWord.charAt(0)) + commandWord.substring(1), commandWord);
        aliases.put(commandWord.toUpperCase(Locale.ROOT), commandWord);
        if (commandWord.contains("-")) {
            int lastHyphen = commandWord.lastIndexOf('-');
            String lastToken = commandWord.substring(lastHyphen + 1);
            String capitalizedLast = commandWord.substring(0, lastHyphen + 1)
                    + Character.toUpperCase(lastToken.charAt(0)) + lastToken.substring(1);
            aliases.put(capitalizedLast, commandWord);
        }
    }

    /**
     * Parses user input into command for execution.
     *
     * @param userInput full user input string
     * @return the command based on the user input
     * @throws ParseException if the user input does not conform to the expected format
     */
    public Command parseCommand(String userInput) throws ParseException {
        final Matcher matcher = BASIC_COMMAND_FORMAT.matcher(userInput.trim());
        if (!matcher.matches()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
        }

        final String rawCommandWord = matcher.group("commandWord");
        final String commandWord = COMMAND_WORD_ALIASES.get(rawCommandWord);
        final String arguments = matcher.group("arguments");

        if (commandWord == null) {
            logger.finer("This user input caused a ParseException: " + userInput);
            throw new ParseException(MESSAGE_UNKNOWN_COMMAND);
        }

        // Note to developers: Change LOG_LEVEL in LogsCenter to enable lower level (i.e., FINE, FINER and lower)
        // log messages such as the one below.
        // Lower level log messages are used sparingly to minimize noise in the code.
        logger.fine("Command word: " + rawCommandWord + "; Arguments: " + arguments);

        return switch (commandWord) {
            case AddGuardianCommand.COMMAND_WORD -> new AddGuardianCommandParser().parse(arguments);
            case AddLessonCommand.COMMAND_WORD -> new AddLessonCommandParser().parse(arguments);
            case AddStudentCommand.COMMAND_WORD -> new AddStudentCommandParser().parse(arguments);
            case EditGuardianCommand.COMMAND_WORD -> new EditGuardianCommandParser().parse(arguments);
            case LinkGuardianCommand.COMMAND_WORD -> new LinkGuardianCommandParser().parse(arguments);
            case UnlinkGuardianCommand.COMMAND_WORD -> new UnlinkGuardianCommandParser().parse(arguments);
            case EditStudentCommand.COMMAND_WORD -> new EditStudentCommandParser().parse(arguments);
            case RemoveGuardianCommand.COMMAND_WORD -> new RemoveGuardianCommandParser().parse(arguments);
            case RemoveLessonCommand.COMMAND_WORD -> new RemoveLessonCommandParser().parse(arguments);
            case ViewLessonCommand.COMMAND_WORD -> new ViewLessonCommandParser().parse(arguments);
            case RemoveStudentCommand.COMMAND_WORD -> new RemoveStudentCommandParser().parse(arguments);
            case SetStudentRemarkCommand.COMMAND_WORD -> new SetStudentRemarkCommandParser().parse(arguments);
            case ClearStudentRemarkCommand.COMMAND_WORD -> new ClearStudentRemarkCommandParser().parse(arguments);
            case MarkPaidCommand.COMMAND_WORD -> new MarkPaidCommandParser().parse(arguments);
            case ClearCommand.COMMAND_WORD -> new ClearCommand();
            case FilterCommand.COMMAND_WORD -> new FilterCommandParser().parse(arguments);
            case FindGuardianCommand.COMMAND_WORD -> new FindGuardianCommandParser().parse(arguments);
            case FindStudentCommand.COMMAND_WORD -> new FindStudentCommandParser().parse(arguments);
            case ListGuardianCommand.COMMAND_WORD -> new ListGuardianCommandParser().parse(arguments);
            case ListStudentCommand.COMMAND_WORD -> new ListStudentCommandParser().parse(arguments);
            case SortStudentCommand.COMMAND_WORD -> new SortStudentCommandParser().parse(arguments);
            case ExitCommand.COMMAND_WORD -> new ExitCommand();
            case HelpCommand.COMMAND_WORD -> new HelpCommand();
            default -> {
                logger.finer("This user input caused a ParseException: " + userInput);
                throw new ParseException(MESSAGE_UNKNOWN_COMMAND);
            }
        };
    }

}
