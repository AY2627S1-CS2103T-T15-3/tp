package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.Optional;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.guardian.Guardian;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;

/** Stores the optional fields used to update a guardian. */
public class EditGuardianDescriptor {
    private Name name;
    private Phone phone;
    private Email email;
    private Address address;

    public EditGuardianDescriptor() {
    }

    /** Copy constructor. */
    public EditGuardianDescriptor(EditGuardianDescriptor toCopy) {
        requireNonNull(toCopy);
        name = toCopy.name;
        phone = toCopy.phone;
        email = toCopy.email;
        address = toCopy.address;
    }

    /** Returns an updated guardian, preserving fields absent from this descriptor. */
    public Guardian createEditedGuardian(Guardian guardian) {
        requireNonNull(guardian);
        return new Guardian(
                getName().orElse(guardian.getName()),
                getPhone().orElse(guardian.getPhone()),
                getEmail().orElse(guardian.getEmail()),
                getAddress().orElse(guardian.getAddress()));
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

    /** Returns true if at least one guardian field has been supplied. */
    public boolean isAnyFieldEdited() {
        return name != null || phone != null || email != null || address != null;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("name", name)
                .add("phone", phone)
                .add("email", email)
                .add("address", address)
                .toString();
    }
}
