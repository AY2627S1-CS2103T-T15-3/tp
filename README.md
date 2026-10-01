[![CI Status](https://github.com/se-edu/addressbook-level3/workflows/Java%20CI/badge.svg)](https://github.com/se-edu/addressbook-level3/actions)


# TutorTrack

![Ui](docs/images/Ui.png)

* This is a **desktop application for tutors** to manage their students and guardians.

  * It allows tutors to store and manage contact details and academic information.
  * It supports relationships between students and their guardians.
  * It provides search and filtering features to help tutors quickly find relevant contacts.
* TutorTrack is **written in an object-oriented programming (OOP) style** and is designed as an ongoing software project.
* The application uses a **command-based interface** for managing contacts efficiently.
* For the detailed documentation of this project, see the documentation in the `docs/` directory.

## Features

### Student Management

TutorTrack allows tutors to manage student records containing:

* Name
* Phone number
* Email
* Address
* Academic level
* Subjects

Students can be added, edited, removed, searched, and filtered.

### Guardian Management

Tutors can store guardian contact details including:

* Name
* Phone number
* Email
* Address

Guardians can be added, edited, removed, and searched independently from students.

### Guardian-Student Relationships

TutorTrack supports relationships between students and guardians.

* A student can have multiple guardians.
* A guardian can be linked to multiple students, such as siblings.
* Guardians are added separately and can then be linked to students using `link-g`.

### Search and Filter

Tutors can find contacts using partial name searches.

Students can also be filtered by:

* Academic level
* Subject
* A combination of academic level and subjects

The displayed list is used for index-based commands such as editing, removing, and linking contacts.

## Commands

| Command         | Format                                                                           | Description                              |
| --------------- | -------------------------------------------------------------------------------- | ---------------------------------------- |
| Add student     | `add-s n/NAME p/PHONE e/EMAIL a/ADDRESS l/LEVEL s/SUBJECT [s/SUBJECT]...`        | Adds a new student                       |
| Add guardian    | `add-g n/NAME p/PHONE e/EMAIL a/ADDRESS`                                         | Adds a new guardian                      |
| Edit student    | `edit-s INDEX [n/NAME] [p/PHONE] [e/EMAIL] [a/ADDRESS] [l/LEVEL] [s/SUBJECT]...` | Edits an existing student                |
| Edit guardian   | `edit-g INDEX [n/NAME] [p/PHONE] [e/EMAIL] [a/ADDRESS]`                          | Edits an existing guardian               |
| Remove student  | `remove-s INDEX`                                                                 | Removes a student                        |
| Remove guardian | `remove-g INDEX`                                                                 | Removes a guardian                       |
| Link guardian   | `link-g STUDENT_INDEX g/GUARDIAN_INDEX`                                          | Links a guardian to a student            |
| Find student    | `find-s NAME`                                                                    | Finds students by name                   |
| Find guardian   | `find-g NAME`                                                                    | Finds guardians by name                  |
| Filter students | `filter [l/LEVEL] [s/SUBJECT]...`                                                | Filters students by level and/or subject |
| List students   | `list-s`                                                                         | Displays all students                    |
| List guardians  | `list-g`                                                                         | Displays all guardians                   |

## Input Validation

TutorTrack validates contact information to prevent invalid records.

* Names must be 1–100 characters and may contain alphanumeric characters, spaces, hyphens and apostrophes.
* Phone numbers must contain 3–15 digits and may optionally start with `+`.
* Emails must follow the required local-part@domain format.
* Addresses must be 1–200 characters and may contain common address punctuation.
* Academic levels must be 1–50 characters and may contain alphanumeric characters and spaces.
* Subjects must be 1–50 characters and may contain alphanumeric characters, spaces, hyphens and parentheses.

Names, academic levels and subjects are compared case-insensitively.

## Duplicate Contacts

Two students are considered duplicates if their normalised names and phone numbers are identical.

The same rule applies independently to guardians.

A student and a guardian may have the same phone number.

## Displayed List

TutorTrack maintains a **displayed list** for students and guardians.

Commands such as `find-s` and `filter` change the displayed student list. Index-based commands such as `edit-s`, `remove-s`, and `link-g` then use the indexes from that displayed list.

For example:

```text
find-s Henry
```

After searching, the indexes shown correspond to the matching students.

The `list-s` and `list-g` commands restore their respective full lists.

## Documentation

The project documentation is available in the `docs/` directory.

This includes documentation related to the product requirements, features, and development of TutorTrack.

## Acknowledgements

TutorTrack was developed as part of a software engineering project.

The project was based on the **AddressBook Level 3 (AB3)** project developed as part of the SE-EDU initiative.
