package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.model.student.Level;
import seedu.address.model.student.Student;
import seedu.address.model.student.Subject;

/** Stores the optional fields used to update a student. */
public class EditStudentDescriptor {
    private Name name;
    private Phone phone;
    private Email email;
    private Address address;
    private Level level;
    private Set<Subject> subjects;

    public EditStudentDescriptor() {
    }

    /** Copy constructor. */
    public EditStudentDescriptor(EditStudentDescriptor toCopy) {
        requireNonNull(toCopy);
        name = toCopy.name;
        phone = toCopy.phone;
        email = toCopy.email;
        address = toCopy.address;
        level = toCopy.level;
        setSubjects(toCopy.subjects);
    }

    /** Returns an updated student, preserving fields absent from this descriptor. */
    public Student createEditedStudent(Student student) {
        requireNonNull(student);
        return new Student(
                getName().orElse(student.getName()),
                getPhone().orElse(student.getPhone()),
                getEmail().orElse(student.getEmail()),
                getAddress().orElse(student.getAddress()),
                getLevel().orElse(student.getLevel()),
                getSubjects().orElse(student.getSubjects()));
    }

    public void setName(Name name) {
        this.name = name;
    }

    public Optional<Name> getName() {
        return Optional.ofNullable(name);
    }

    public void setPhone(Phone phone) {
        this.phone = phone;
    }

    public Optional<Phone> getPhone() {
        return Optional.ofNullable(phone);
    }

    public void setEmail(Email email) {
        this.email = email;
    }

    public Optional<Email> getEmail() {
        return Optional.ofNullable(email);
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Optional<Address> getAddress() {
        return Optional.ofNullable(address);
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public Optional<Level> getLevel() {
        return Optional.ofNullable(level);
    }

    public void setSubjects(Set<Subject> subjects) {
        this.subjects = subjects == null ? null : new LinkedHashSet<>(subjects);
    }

    public Optional<Set<Subject>> getSubjects() {
        return subjects == null
                ? Optional.empty()
                : Optional.of(Collections.unmodifiableSet(subjects));
    }

    /** Returns true if at least one student field has been supplied. */
    public boolean isAnyFieldEdited() {
        return name != null || phone != null || email != null || address != null
                || level != null || subjects != null;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("name", name)
                .add("phone", phone)
                .add("email", email)
                .add("address", address)
                .add("level", level)
                .add("subjects", subjects)
                .toString();
    }
}
