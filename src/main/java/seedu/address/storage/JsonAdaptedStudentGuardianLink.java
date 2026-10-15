package seedu.address.storage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.model.guardian.Guardian;
import seedu.address.model.link.StudentGuardianLink;
import seedu.address.model.student.Student;

/** Jackson-friendly version of a student-guardian link. */
class JsonAdaptedStudentGuardianLink {
    private final String studentName;
    private final String studentPhone;
    private final String guardianName;
    private final String guardianPhone;

    @JsonCreator
    public JsonAdaptedStudentGuardianLink(@JsonProperty("studentName") String studentName,
            @JsonProperty("studentPhone") String studentPhone, @JsonProperty("guardianName") String guardianName,
            @JsonProperty("guardianPhone") String guardianPhone) {
        this.studentName = studentName;
        this.studentPhone = studentPhone;
        this.guardianName = guardianName;
        this.guardianPhone = guardianPhone;
    }

    JsonAdaptedStudentGuardianLink(StudentGuardianLink source) {
        studentName = source.getStudent().getName().fullName;
        studentPhone = source.getStudent().getPhone().value;
        guardianName = source.getGuardian().getName().fullName;
        guardianPhone = source.getGuardian().getPhone().value;
    }

    StudentGuardianLink toModelType(Student student, Guardian guardian) {
        return new StudentGuardianLink(student, guardian);
    }

    boolean matchesStudent(Student student) {
        return student.getName().fullName.equals(studentName)
                && student.getPhone().value.equals(studentPhone);
    }

    boolean matchesGuardian(Guardian guardian) {
        return guardian.getName().fullName.equals(guardianName)
                && guardian.getPhone().value.equals(guardianPhone);
    }

    boolean matches(Student student, Guardian guardian) {
        return matchesStudent(student)
                && matchesGuardian(guardian);
    }
}
