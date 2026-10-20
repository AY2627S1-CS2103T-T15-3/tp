package seedu.address.model.student;

import java.util.Comparator;

/**
 * Compares students by their names in ascending, case-insensitive order.
 */
public class StudentNameComparator implements Comparator<Student> {

    @Override
    public int compare(Student firstStudent, Student secondStudent) {
        return Student.normalizeName(firstStudent.getName().fullName)
                .compareTo(Student.normalizeName(secondStudent.getName().fullName));
    }
}
