package seedu.address.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.guardian.Guardian;
import seedu.address.model.lesson.Lesson;
import seedu.address.model.student.Student;

/**
 * An Immutable AddressBook that is serializable to JSON format.
 */
@JsonRootName(value = "addressbook")
class JsonSerializableAddressBook {

    public static final String MESSAGE_DUPLICATE_LESSON = "Lessons list contains duplicate lesson(s).";
    public static final String MESSAGE_DUPLICATE_GUARDIAN = "Guardians list contains duplicate guardian(s).";
    public static final String MESSAGE_INVALID_LINK = "Student-guardian link references a missing contact.";
    public static final String MESSAGE_INVALID_LESSON = "Lesson references a missing student.";

    private final List<JsonAdaptedStudent> students = new ArrayList<>();
    private final List<JsonAdaptedGuardian> guardians = new ArrayList<>();
    private final List<JsonAdaptedStudentGuardianLink> links = new ArrayList<>();
    private final List<JsonAdaptedLesson> lessons = new ArrayList<>();

    /** Constructs a {@code JsonSerializableAddressBook} with the given TutorTrack data. */
    @JsonCreator
    public JsonSerializableAddressBook(@JsonProperty("students") List<JsonAdaptedStudent> students,
            @JsonProperty("guardians") List<JsonAdaptedGuardian> guardians,
            @JsonProperty("links") List<JsonAdaptedStudentGuardianLink> links,
            @JsonProperty("lessons") List<JsonAdaptedLesson> lessons) {
        if (students != null) {
            this.students.addAll(students);
        }
        if (guardians != null) {
            this.guardians.addAll(guardians);
        }
        if (links != null) {
            this.links.addAll(links);
        }
        if (lessons != null) {
            this.lessons.addAll(lessons);
        }
    }

    /** Constructs serializable address-book data without guardians for compatibility with older callers. */
    public JsonSerializableAddressBook(List<JsonAdaptedStudent> students) {
        this(students, null, null, null);
    }

    /** Constructs serializable data without links for compatibility with older callers. */
    public JsonSerializableAddressBook(List<JsonAdaptedStudent> students,
            List<JsonAdaptedGuardian> guardians) {
        this(students, guardians, null, null);
    }

    /** Constructs serializable data without lessons for compatibility with older callers. */
    public JsonSerializableAddressBook(List<JsonAdaptedStudent> students,
            List<JsonAdaptedGuardian> guardians, List<JsonAdaptedStudentGuardianLink> links) {
        this(students, guardians, links, null);
    }

    /**
     * Converts a given {@code ReadOnlyAddressBook} into this class for Jackson use.
     *
     * @param source future changes to this will not affect the created {@code JsonSerializableAddressBook}.
     */
    public JsonSerializableAddressBook(ReadOnlyAddressBook source) {
        students.addAll(source.getStudentList().stream().map(JsonAdaptedStudent::new).collect(Collectors.toList()));
        guardians.addAll(source.getGuardianList().stream().map(JsonAdaptedGuardian::new).collect(Collectors.toList()));
        links.addAll(source.getStudentGuardianLinkList().stream()
                .map(JsonAdaptedStudentGuardianLink::new).collect(Collectors.toList()));
        lessons.addAll(source.getLessonList().stream()
                .map(JsonAdaptedLesson::new).collect(Collectors.toList()));
    }

    /**
     * Converts this address book into the model's {@code AddressBook} object.
     *
     * @throws IllegalValueException if there were any data constraints violated.
     */
    public AddressBook toModelType() throws IllegalValueException {
        AddressBook addressBook = new AddressBook();
        for (JsonAdaptedStudent jsonAdaptedStudent : students) {
            Student student = jsonAdaptedStudent.toModelType();
            if (addressBook.hasStudent(student)) {
                throw new IllegalValueException("Students list contains duplicate student(s).");
            }
            addressBook.addStudent(student);
        }
        for (JsonAdaptedGuardian jsonAdaptedGuardian : guardians) {
            Guardian guardian = jsonAdaptedGuardian.toModelType();
            if (addressBook.hasGuardian(guardian)) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_GUARDIAN);
            }
            addressBook.addGuardian(guardian);
        }
        for (JsonAdaptedStudentGuardianLink jsonLink : links) {
            Student matchingStudent = addressBook.getStudentList().stream()
                    .filter(jsonLink::matchesStudent)
                    .findFirst().orElse(null);
            Guardian matchingGuardian = addressBook.getGuardianList().stream()
                    .filter(jsonLink::matchesGuardian)
                    .findFirst().orElse(null);
            if (matchingStudent == null || matchingGuardian == null) {
                throw new IllegalValueException(MESSAGE_INVALID_LINK);
            }
            addressBook.addStudentGuardianLink(jsonLink.toModelType(matchingStudent, matchingGuardian));
        }
        for (JsonAdaptedLesson jsonLesson : lessons) {
            Student matchingStudent = addressBook.getStudentList().stream()
                    .filter(jsonLesson::matchesStudent)
                    .findFirst().orElse(null);
            if (matchingStudent == null) {
                throw new IllegalValueException(MESSAGE_INVALID_LESSON);
            }
            Lesson lesson = jsonLesson.toModelType(matchingStudent);
            if (addressBook.getLessonList().contains(lesson)) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_LESSON);
            }
            addressBook.addLesson(lesson);
        }
        return addressBook;
    }

}
