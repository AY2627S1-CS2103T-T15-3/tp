package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.LinkedHashSet;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.model.student.Level;
import seedu.address.model.student.Student;
import seedu.address.model.student.Subject;

/** Contains integration tests for {@code ListStudentCommand}. */
public class ListStudentCommandTest {

    @Test
    public void execute_listStudents_showsAllStudents() {
        Model model = new ModelManager();
        Student alice = student("Alice Tan");
        Student bob = student("Bob Lim");
        model.addStudent(alice);
        model.addStudent(bob);
        model.updateFilteredStudentList(student -> student.getName().fullName.equals("Alice Tan"));

        CommandResult result = new ListStudentCommand().execute(model);

        assertEquals(ListStudentCommand.MESSAGE_SUCCESS, result.getFeedbackToUser());
        assertEquals(List.of(alice, bob), model.getFilteredStudentList());
    }

    private static Student student(String name) {
        return new Student(new Name(name), new Phone("91234567"),
                new Email(name.replace(" ", "") + "@example.com"), new Address("12 Main Street"),
                new Level("JC 1"), new LinkedHashSet<>(List.of(new Subject("Mathematics"))));
    }
}
