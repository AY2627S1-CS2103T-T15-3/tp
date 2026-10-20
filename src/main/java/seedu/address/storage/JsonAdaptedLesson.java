package seedu.address.storage;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.lesson.Lesson;
import seedu.address.model.student.Student;

/** Jackson-friendly version of {@link Lesson}. */
class JsonAdaptedLesson {
    private final String studentName;
    private final String studentPhone;
    private final String date;
    private final String startTime;
    private final long durationMinutes;

    @JsonCreator
    public JsonAdaptedLesson(@JsonProperty("studentName") String studentName,
            @JsonProperty("studentPhone") String studentPhone,
            @JsonProperty("date") String date,
            @JsonProperty("startTime") String startTime,
            @JsonProperty("durationMinutes") long durationMinutes) {
        this.studentName = studentName;
        this.studentPhone = studentPhone;
        this.date = date;
        this.startTime = startTime;
        this.durationMinutes = durationMinutes;
    }

    public JsonAdaptedLesson(Lesson source) {
        studentName = source.getStudent().getName().fullName;
        studentPhone = source.getStudent().getPhone().value;
        date = source.getDate().toString();
        startTime = source.getStartTime().toString();
        durationMinutes = source.getDuration().toMinutes();
    }

    public boolean matchesStudent(Student student) {
        return student.getName().fullName.equals(studentName)
                && student.getPhone().value.equals(studentPhone);
    }

    public Lesson toModelType(Student student) throws IllegalValueException {
        if (studentName == null || studentPhone == null || date == null || startTime == null) {
            throw new IllegalValueException("Lesson contains a missing field.");
        }
        try {
            return new Lesson(student, LocalDate.parse(date), LocalTime.parse(startTime),
                    Duration.ofMinutes(durationMinutes));
        } catch (RuntimeException exception) {
            throw new IllegalValueException("Lesson contains invalid date, time, or duration.");
        }
    }
}
