package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.testutil.Assert.assertThrows;

import java.util.LinkedHashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.ModelManager;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.model.student.Level;
import seedu.address.model.student.Student;
import seedu.address.model.student.Subject;

public class AddStudentCommandTest {
    private static final Student HENRY = student("Henry Tan", "91234567", "henry@example.com", "12 ABCD Ave 3",
            "Secondary 3", "Mathematics");

    @Test
    public void execute_newStudent_success() throws Exception {
        ModelManager model = new ModelManager();
        CommandResult result = new AddStudentCommand(HENRY).execute(model);

        assertEquals(String.format(AddStudentCommand.MESSAGE_SUCCESS, Messages.format(HENRY)),
                result.getFeedbackToUser());
        assertEquals(java.util.List.of(HENRY), model.getFilteredStudentList());
    }

    @Test
    public void execute_duplicateStudent_failure() throws Exception {
        ModelManager model = new ModelManager();
        model.addStudent(HENRY);
        Student duplicate = student("  henry   tan", "91234567", "other@example.com", "25 Happy Road", "JC 1",
                "Physics");

        assertThrows(CommandException.class, AddStudentCommand.MESSAGE_DUPLICATE_STUDENT, () ->
                new AddStudentCommand(duplicate).execute(model));
        assertEquals(java.util.List.of(HENRY), model.getFilteredStudentList());
    }

    @Test
    public void execute_sameNameDifferentPhone_success() throws Exception {
        ModelManager model = new ModelManager();
        model.addStudent(HENRY);
        Student sameName = student("Henry Tan", "98765432", "other@example.com", "25 Happy Road", "JC 1",
                "Physics");

        new AddStudentCommand(sameName).execute(model);
        assertEquals(java.util.List.of(HENRY, sameName), model.getFilteredStudentList());
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
