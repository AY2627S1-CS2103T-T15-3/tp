package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import java.util.concurrent.FutureTask;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import seedu.address.commons.core.GuiSettings;
import seedu.address.logic.Logic;
import seedu.address.logic.LogicManager;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.guardian.Guardian;
import seedu.address.model.link.StudentGuardianLink;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.student.Level;
import seedu.address.model.student.Student;
import seedu.address.model.student.Subject;
import seedu.address.storage.JsonAddressBookStorage;
import seedu.address.storage.JsonUserPrefsStorage;
import seedu.address.storage.StorageManager;

/** Tests the JavaFX components changed for the link-g feature. */
public class LinkGuardianUiTest {

    @BeforeAll
    public static void initializeJavaFx() {
        try {
            Platform.startup(() -> { });
        } catch (IllegalStateException ignored) {
            // JavaFX was already initialized by another UI test class.
        }
    }

    @Test
    public void studentCard_displaysLinkedGuardian() {
        Student student = student("Lucy Lim");
        Guardian guardian = guardian("Ben Tan");

        StudentCard card = new StudentCard(student, 1, ignored -> List.of(guardian));
        FlowPane guardians = (FlowPane) card.getRoot().lookup("#guardians");

        assertEquals("Ben Tan (92345678)", ((Label) guardians.getChildren().get(1)).getText());
    }

    @Test
    public void studentListPanel_highlightAndClearSelection() {
        Student student = student("Lucy Lim");
        StudentListPanel panel = new StudentListPanel(FXCollections.observableArrayList(student),
                ignored -> List.of());

        panel.highlightStudent(student);
        panel.clearSelection();
    }

    @Test
    public void guardianListPanel_highlightAndClearSelection() {
        Guardian guardian = guardian("Ben Tan");
        GuardianListPanel panel = new GuardianListPanel(FXCollections.observableArrayList(guardian));

        panel.highlightGuardian(guardian);
        panel.clearSelection();
        panel.refresh();
    }

    @Test
    public void mainWindow_handlesHighlightResults() throws Exception {
        Student student = student("Lucy Lim");
        Guardian guardian = guardian("Ben Tan");
        Logic logic = new StubLogic(student, guardian);

        runOnFxThread(() -> {
            MainWindow window = new MainWindow(new javafx.stage.Stage(), logic, Path.of("data.json"));
            window.fillInnerParts();
            Method executeCommand = MainWindow.class.getDeclaredMethod("executeCommand", String.class);
            executeCommand.setAccessible(true);
            executeCommand.invoke(window, "student");
            executeCommand.invoke(window, "guardian");
            window.show();
            return window;
        });
    }

    @Test
    public void mainWindow_removeGuardian_refreshesAllLinkedStudentCards(@TempDir Path temporaryFolder)
            throws Exception {
        runOnFxThread(() -> {
            Model model = new ModelManager();
            List<Student> students = List.of(student("Lucy Lim"), student("Mary Tan"));
            Guardian removedGuardian = guardian("Ben Tan");
            Guardian remainingGuardian = guardian("Amy Lim");
            model.addGuardian(removedGuardian);
            model.addGuardian(remainingGuardian);
            for (Student student : students) {
                model.addStudent(student);
                model.addStudentGuardianLink(new StudentGuardianLink(student, removedGuardian));
                model.addStudentGuardianLink(new StudentGuardianLink(student, remainingGuardian));
            }
            Path dataPath = temporaryFolder.resolve("addressBook.json");
            Logic logic = new LogicManager(model, new StorageManager(new JsonAddressBookStorage(dataPath),
                    new JsonUserPrefsStorage(temporaryFolder.resolve("userPrefs.json"))));
            MainWindow window = new MainWindow(new javafx.stage.Stage(), logic, dataPath);
            try {
                window.fillInnerParts();
                window.getPrimaryStage().setHeight(1000);
                window.show();
                assertStudentGuardianLabels(window, students,
                        List.of("Guardians:", "Ben Tan (92345678)", "Amy Lim (92345678)"));

                TextField commandField = (TextField) window.getRoot().getScene().lookup("#commandTextField");
                commandField.setText("remove-g 1");
                commandField.fireEvent(new ActionEvent());
                assertEquals(List.of(remainingGuardian), List.copyOf(model.getFilteredGuardianList()));
                assertStudentGuardianLabels(window, students, List.of("Guardians:", "Amy Lim (92345678)"));
            } finally {
                window.getPrimaryStage().hide();
            }
            return null;
        });
    }

    private static void assertStudentGuardianLabels(MainWindow window, List<Student> students,
            List<String> expectedLabels) {
        // Process the refresh in the existing skin without manually refreshing or recreating any cards.
        window.getPrimaryStage().getScene().getRoot().applyCss();
        window.getPrimaryStage().getScene().getRoot().layout();
        List<ListCell<?>> cells = window.getStudentListPanel().getRoot().lookupAll(".list-cell").stream()
                .filter(node -> node instanceof ListCell<?>)
                .<ListCell<?>>map(node -> (ListCell<?>) node)
                .filter(cell -> !cell.isEmpty())
                .sorted(java.util.Comparator.comparingInt(ListCell::getIndex))
                .toList();
        assertEquals(students, cells.stream().map(ListCell::getItem).toList());
        for (ListCell<?> cell : cells) {
            FlowPane guardians = (FlowPane) cell.getGraphic().lookup("#guardians");
            assertEquals(expectedLabels, guardians.getChildren().stream()
                    .map(node -> ((Label) node).getText()).toList());
        }
    }

    private static <T> T runOnFxThread(java.util.concurrent.Callable<T> action) throws Exception {
        FutureTask<T> task = new FutureTask<>(action);
        Platform.runLater(task);
        return task.get();
    }

    private static Student student(String name) {
        return new Student(new Name(name), new Phone("91234567"), new Email("lucy@example.com"),
                new Address("21 Happy Road"), new Level("JC 1"), Set.of(new Subject("Mathematics")));
    }

    private static Guardian guardian(String name) {
        return new Guardian(new Name(name), new Phone("92345678"), new Email("ben@example.com"),
                new Address("25 Happy Road"));
    }

    private static final class StubLogic implements Logic {
        private final Student student;
        private final Guardian guardian;

        StubLogic(Student student, Guardian guardian) {
            this.student = student;
            this.guardian = guardian;
        }

        @Override
        public CommandResult execute(String commandText) throws CommandException, ParseException {
            return commandText.equals("student")
                    ? new CommandResult("student", student) : new CommandResult("guardian", guardian);
        }

        @Override
        public javafx.collections.ObservableList<Person> getFilteredPersonList() {
            return FXCollections.observableArrayList();
        }

        @Override
        public javafx.collections.ObservableList<Student> getFilteredStudentList() {
            return FXCollections.observableArrayList(student);
        }

        @Override
        public javafx.collections.ObservableList<Guardian> getFilteredGuardianList() {
            return FXCollections.observableArrayList(guardian);
        }

        @Override
        public List<Guardian> getGuardiansForStudent(Student ignored) {
            return List.of(guardian);
        }

        @Override
        public GuiSettings getGuiSettings() {
            return new GuiSettings(800, 600, 0, 0);
        }

        @Override
        public void setGuiSettings(GuiSettings guiSettings) { }
    }
}
