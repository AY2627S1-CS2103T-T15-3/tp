package seedu.address.model.student;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;

public class StudentNameComparatorTest {

    private final StudentNameComparator comparator = new StudentNameComparator();

    @Test
    public void compare_names_returnsAscendingCaseInsensitiveOrder() {
        Student alice = student("alice Tan", "91234567");
        Student bob = student("Bob Tan", "92345678");

        assertTrue(comparator.compare(alice, bob) < 0);
        assertTrue(comparator.compare(bob, alice) > 0);
    }

    @Test
    public void compare_namesWithDifferentWhitespace_returnsEqual() {
        Student first = student("Alice Tan", "91234567");
        Student second = student(" alice  tan ", "92345678");

        assertEquals(0, comparator.compare(first, second));
    }

    private static Student student(String name, String phone) {
        return new Student(Name.fromStudentValue(name), Phone.fromStudentValue(phone),
                Email.fromStudentValue("student" + phone + "@example.com"),
                Address.fromStudentValue("12 Main Street"), new Level("JC 1"), Set.of(new Subject("Mathematics")));
    }
}
