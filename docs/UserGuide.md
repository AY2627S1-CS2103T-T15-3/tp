---
  layout: default.md
  title: "User Guide"
  pageNav: 3
---

# AB-3 User Guide

AddressBook Level 3 (AB3) is a **desktop application for managing contacts, optimized for use through a Command Line Interface (CLI)** while retaining the benefits of a Graphical User Interface (GUI). If you type quickly, AB3 can help you manage contacts faster than traditional GUI applications.

<!-- * Table of Contents -->
<page-nav-print />

--------------------------------------------------------------------------------------------------------------------

## Quick start

1. Ensure that Java `25` or later is installed on your computer.<br>
   **Mac users:** Ensure you have the precise JDK version prescribed [here](https://se-education.org/guides/tutorials/javaInstallationMac.html).

1. Download the latest `.jar` file from [here](https://github.com/se-edu/addressbook-level3/releases).

1. Copy the file to the folder you want to use as the _home folder_ for your AddressBook.

1. Open a terminal, `cd` to the folder containing the JAR file, and run `java -jar addressbook.jar`.<br>
   A GUI similar to the one below should appear in a few seconds. Note how the app contains some sample data.<br>
   ![Ui](images/Ui.png)

1. Type a command in the command box and press Enter to execute it. For example, type **`help`** and press Enter to open the help window.<br>
   Some example commands you can try:

   * `list` : Lists all contacts.

   * `add n/John Doe p/98765432 e/johnd@example.com a/John street, block 123, #01-01` : Adds a contact named `John Doe` to the Address Book.

   * `edit-s 1 p/98765432` : Updates the phone number of the first student.

   * `edit-g 1 a/25 Happy Road` : Updates the address of the first guardian.

   * `link-g 1 g/1` : Links the first student to the first guardian.

   * `delete 3` : Deletes the 3rd contact shown in the current list.

   * `clear` : Deletes all contacts.

   * `exit` : Exits the app.

1. Refer to the [Features](#features) section below for details of each command.

--------------------------------------------------------------------------------------------------------------------

## Features

<box type="info" seamless>

**Notes about the command format:**<br>

* Words in `UPPER_CASE` are the parameters to be supplied by the user.<br>
  For example, in `add n/NAME`, replace `NAME` with a value such as `John Doe`.

* Items in square brackets are optional.<br>
  For example, `n/NAME [t/TAG]` can be used as `n/John Doe t/friend` or as `n/John Doe`.

* Items followed by `...` can appear zero or more times.<br>
  For example, `[t/TAG]... ` may be omitted, or written as `t/friend` or `t/friend t/family`.

* Parameters can be in any order.<br>
  For example, if the command specifies `n/NAME p/PHONE_NUMBER`, `p/PHONE_NUMBER n/NAME` is also acceptable.

* Extraneous parameters for commands that take no parameters, such as `help`, `list`, `exit`, and `clear`, are ignored.<br>
  For example, `help 123` is interpreted as `help`.

* If you are using a PDF version of this document, be careful when copying and pasting commands that span multiple lines as space characters surrounding line-breaks may be omitted when copied over to the application.
</box>

### Viewing help: `help`

Shows a message explaining how to access the help page.

![help message](images/helpMessage.png)

Format: `help`


### Adding a person: `add`

Adds a person to the address book.

Format: `add n/NAME p/PHONE_NUMBER e/EMAIL a/ADDRESS [t/TAG]... `

<box type="tip" seamless>

**Tip:** A person can have any number of tags, including zero.
</box>

Examples:
* `add n/John Doe p/98765432 e/johnd@example.com a/John street, block 123, #01-01`
* `add n/Betsy Crowe t/friend e/betsycrowe@example.com a/Newgate Prison p/1234567 t/criminal`

### Adding a student: `add-s`

Adds a student to TutorTrack.

Format: `add-s n/NAME p/PHONE e/EMAIL a/ADDRESS l/LEVEL s/SUBJECT [s/SUBJECT]...`

All of `n/NAME`, `p/PHONE`, `e/EMAIL`, `a/ADDRESS`, `l/LEVEL`, and at least one
`s/SUBJECT` are required. The additional `s/SUBJECT` parameters are optional
and may be repeated. The supported prefixes are `n/`, `p/`, `e/`, `a/`, `l/`,
and `s/`. The parameters can be entered in any order.

Validation rules:
* `NAME` must be 1-100 characters long and contain only letters, digits,
  spaces, hyphens, or apostrophes.
* `PHONE` must contain 3-15 digits and may start with `+`.
* `EMAIL` must have the format `local-part@domain`, contain no spaces, and use
  a domain with at least one dot, such as `example.com`.
* `ADDRESS` must be 1-200 characters long and may contain letters, digits,
  spaces, and the punctuation `, . # / ( ) ' -`.
* `LEVEL` must be 1-50 characters long and contain only letters, digits, and
  spaces.
* `SUBJECT` must be 1-50 characters long and may contain letters, digits,
  spaces, hyphens, or parentheses.

Examples:
* `add-s n/Henry Tan p/91234567 e/henry@example.com a/12 ABCD Ave 3 l/Secondary 3 s/Mathematics s/Physics`
* `add-s n/Mary-Jane O'Connor p/+6591234567 e/mary@example.com a/12 ABCD Ave 3, #04-15 l/JC 1 s/A-Level (H2)`

On success, the student is added to the student list and TutorTrack displays a
message such as `New student added: Henry Tan; Phone: 91234567; Email: henry@example.com; Address: 12 ABCD Ave 3; Level: Secondary 3; Subjects: Mathematics, Physics`.

If a student with the same normalized name and phone number already exists,
the command is rejected with:

`This student already exists in TutorTrack.`

Invalid command formats are rejected with:

`Invalid command format.`<br>
`Usage: add-s n/NAME p/PHONE e/EMAIL a/ADDRESS l/LEVEL s/SUBJECT [s/SUBJECT]...`

### Adding a guardian: `add-g`

Adds a guardian's contact details to TutorTrack. This command does not link the
guardian to a student; linking is handled separately.

Format: `add-g n/NAME p/PHONE e/EMAIL a/ADDRESS`

All of `n/NAME`, `p/PHONE`, `e/EMAIL`, and `a/ADDRESS` are required and must be
specified exactly once. There are no optional fields. The supported prefixes
are `n/`, `p/`, `e/`, and `a/`. The parameters can be entered in any order.

The validation rules for `NAME`, `PHONE`, `EMAIL`, and `ADDRESS` are the same
as those listed for [`add-s`](#adding-a-student-add-s).

Examples:
* `add-g n/Ben Tan p/92345678 e/ben.tan@gmail.com a/12 ABCD Ave 3`
* `add-g n/Billie O'Connor p/+6598765432 e/billie@example.com a/25 Happy Road`

On success, the guardian is added to the guardian list and TutorTrack displays
a message such as `New guardian added: Ben Tan; Phone: 92345678; Email: ben.tan@gmail.com; Address: 12 ABCD Ave 3`.

If a guardian with the same normalized name and phone number already exists,
the command is rejected with:

`This guardian already exists in TutorTrack.`

Invalid command formats are rejected with:

`Invalid command format.`<br>
`Usage: add-g n/NAME p/PHONE e/EMAIL a/ADDRESS`

### Listing all persons: `list`

Shows a list of all persons in the address book.

Format: `list`

### Listing all students: `list-s`

Shows a list of all students in TutorTrack.

Format: `list-s`

### Listing all guardians: `list-g`

Shows a list of all guardians in TutorTrack.

Format: `list-g`

### Editing a person: `edit`

Edits an existing person in the address book.

Format: `edit INDEX [n/NAME] [p/PHONE] [e/EMAIL] [a/ADDRESS] [t/TAG]... `

* Edits the person at the specified `INDEX`. The index refers to the index number shown in the displayed person list. The index **must be a positive integer** 1, 2, 3, ...
* At least one of the optional fields must be provided.
* Existing values will be updated to the input values.
* When editing tags, all of the person's existing tags are removed; adding tags is not cumulative.
* To remove all of a person's tags, enter `t/` without a tag after it.

Examples:
*  `edit 1 p/91234567 e/johndoe@example.com` Edits the phone number and email address of the 1st person to be `91234567` and `johndoe@example.com` respectively.
*  `edit 2 n/Betsy Crower t/` Edits the name of the 2nd person to be `Betsy Crower` and clears all existing tags.

### Editing a student: `edit-s`

Edits selected details of an existing student. The index refers to the displayed student list, including a filtered list.

Format: `edit-s INDEX [n/NAME] [p/PHONE] [e/EMAIL] [a/ADDRESS] [l/LEVEL] [s/SUBJECT]...`

* `INDEX` must be a positive integer within the displayed student list.
* At least one field must be provided. Fields that are not provided keep their current values.
* If `s/` is provided, it replaces the student's complete subject list. Retype existing subjects to keep them.
* A student must always have at least one subject.
* The edited student must not duplicate another student by name and phone number.

Examples:
* `edit-s 2 p/98765432 l/JC 2`
* `edit-s 1 s/Mathematics s/Physics`

### Editing a guardian: `edit-g`

Edits selected details of an existing guardian. The index refers to the displayed guardian list, including a filtered list.

Format: `edit-g INDEX [n/NAME] [p/PHONE] [e/EMAIL] [a/ADDRESS]`

* `INDEX` must be a positive integer within the displayed guardian list.
* At least one field must be provided. Fields that are not provided keep their current values.
* The edited guardian must not duplicate another guardian by name and phone number.

Example:
* `edit-g 1 a/25 Happy Road e/ben.tan@example.com`

### Linking a guardian to a student: `link-g`

Links an existing guardian to an existing student.

Format: `link-g STUDENT_INDEX g/GUARDIAN_INDEX`

* Both indexes are required and must be positive integers within their respective displayed lists.
* The student and guardian must already exist.
* Repeating an existing link does not create another link.
* After a successful link, the student is highlighted and the guardian selection is cleared.
* A student can be linked to multiple guardians, and a guardian can be linked to multiple students.
* Links are saved with the address book and restored when the application is restarted.
* If a linked student or guardian is edited, the updated details are shown everywhere that contact is linked.
* Unlinking an individual relationship is not supported yet.

Examples:
* `link-g 2 g/1`
* `link-g 3 g/1`

### Locating persons by name: `find`

Finds persons whose names contain any of the given keywords.

Format: `find KEYWORD [MORE_KEYWORDS]`

* The search is case-insensitive; for example, `hans` matches `Hans`.
* Keyword order does not matter; for example, `Hans Bo` matches `Bo Hans`.
* The search considers only names.
* Only full words match; for example, `Han` does not match `Hans`.
* Persons matching at least one keyword are returned (an `OR` search); for example, `Hans Bo` returns `Hans Gruber` and `Bo Yang`.

Examples:
* `find John` returns `john` and `John Doe`
* `find alex david` returns `Alex Yeoh`, `David Li`<br>
  ![result for 'find alex david'](images/findAlexDavidResult.png)

### Locating students by name: `find-s`

Finds students whose names contain any of the given keywords.

Format: `find-s KEYWORD [MORE_KEYWORDS]`

* The search is case-insensitive.
* Keyword order does not matter.
* The search considers only student names.
* Matches by prefix of each part of the name.
* Students matching at least one keyword are returned (an `OR` search).

Example:
* `find-s alex david` returns matching students named Alex or David.

### Locating guardians by name: `find-g`

Finds guardians whose names contain any of the given keywords.

Format: `find-g KEYWORD [MORE_KEYWORDS]`

* The search is case-insensitive.
* Keyword order does not matter.
* The search considers only guardian names.
* Matches by prefix of each part of the name.
* Guardians matching at least one keyword are returned (an `OR` search).

Example:
* `find-g alex david` returns matching guardians named Alex or David.

### Deleting a person: `delete`

Deletes the specified person from the address book.

Format: `delete INDEX`

* Deletes the person at the specified `INDEX`.
* The index refers to the index number shown in the displayed person list.
* The index **must be a positive integer** 1, 2, 3, ...

Examples:
* `list` followed by `delete 2` deletes the 2nd person in the address book.
* `find Betsy` followed by `delete 1` deletes the 1st person in the results of the `find` command.


### Removing a student: `remove-s`

Removes a specified student from TutorTrack.

Format: `remove-s INDEX`

* Removes the student at the specified `INDEX`. The index refers to the
index number shown in the currently displayed student list.
* The index **must be a positive integer** 1, 2, 3, ..., and must not exceed the number
of students in the displayed list.
* Removing a student also removes all associated student-guardian links, but the associated
guardian's contact record is not deleted.

Examples:
* `remove-s 2` removes the 2nd student in the currently displayed student list.
* `remove-s 1` removes the 1st student in the currently displayed student list, even if it is filtered.

### Removing a guardian: `remove-g`

Removes a specified guardian from TutorTrack.

Format: `remove-g INDEX`

* Removes the guardian at the specified `INDEX`. The index refers to the
index number shown in the currently displayed guardian list.
* The index **must be a positive integer** 1, 2, 3, ..., and must not exceed the number
  of guardians in the displayed list.
* Removing a guardian also removes all associated student-guardian links, but the associated
  student's contact record is not deleted.

Examples:
* `remove-g 2` removes the 2nd guardian in the currently displayed guardian list.
* `remove-g 1` removes the 1st guardian in the currently displayed guardian list, even if it is filtered









### Clearing all entries: `clear`

Clears all entries from the address book.

Format: `clear`

### Exiting the program: `exit`

Exits the program.

Format: `exit`

### Saving the data

AddressBook automatically saves data after every command. You do not need to save manually.

### Editing the data file

AddressBook data is saved automatically as a JSON file `[JAR file location]/data/addressbook.json`. Advanced users are welcome to update data directly by editing that data file.

<box type="warning" seamless>

**Caution:**
If your changes make the data file invalid, AddressBook starts with an empty address book at the next run. The invalid file remains on disk until you run a command (AddressBook saves after every command). Still, we recommend backing up the file before editing it.<br>
Furthermore, certain edits can cause the AddressBook to behave in unexpected ways (e.g., if a value entered is outside of the acceptable range). Therefore, edit the data file only if you are confident that you can update it correctly.
</box>

### Archiving data files `[coming in v2.0]`

_Details coming soon ..._

--------------------------------------------------------------------------------------------------------------------

## FAQ

**Q**: How do I transfer my data to another computer?<br>
**A**: Install the app on the other computer and overwrite the data file it creates with the data file from your previous AddressBook home folder.

--------------------------------------------------------------------------------------------------------------------

## Known issues

1. **When using multiple screens**, if you move the application to a secondary screen, and later switch to using only the primary screen, the GUI will open off-screen. The remedy is to delete the `preferences.json` file created by the application before running the application again.
2. **If you minimize the Help Window** and then run the `help` command (or use the `Help` menu, or the keyboard shortcut `F1`) again, the original Help Window will remain minimized, and no new Help Window will appear. The remedy is to manually restore the minimized Help Window.

--------------------------------------------------------------------------------------------------------------------

## Command summary

Action     | Format, Examples
-----------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------
**Add**    | `add n/NAME p/PHONE_NUMBER e/EMAIL a/ADDRESS [t/TAG]... ` <br> e.g., `add n/James Ho p/22224444 e/jamesho@example.com a/123, Clementi Rd, 1234665 t/friend t/colleague`
**Add student** | `add-s n/NAME p/PHONE e/EMAIL a/ADDRESS l/LEVEL s/SUBJECT [s/SUBJECT]...` <br> e.g., `add-s n/Henry Tan p/91234567 e/henry@example.com a/12 ABCD Ave 3 l/Secondary 3 s/Mathematics s/Physics`
**Add guardian** | `add-g n/NAME p/PHONE e/EMAIL a/ADDRESS` <br> e.g., `add-g n/Ben Tan p/92345678 e/ben.tan@gmail.com a/12 ABCD Ave 3`
**Clear**  | `clear`
**Delete** | `delete INDEX`<br> e.g., `delete 3`
**Edit**   | `edit INDEX [n/NAME] [p/PHONE_NUMBER] [e/EMAIL] [a/ADDRESS] [t/TAG]... `<br> e.g.,`edit 2 n/James Lee e/jameslee@example.com`
**Edit student** | `edit-s INDEX [n/NAME] [p/PHONE] [e/EMAIL] [a/ADDRESS] [l/LEVEL] [s/SUBJECT]...`<br> e.g., `edit-s 2 p/98765432 l/JC 2`
**Edit guardian** | `edit-g INDEX [n/NAME] [p/PHONE] [e/EMAIL] [a/ADDRESS]`<br> e.g., `edit-g 1 a/25 Happy Road`
**Find**   | `find KEYWORD [MORE_KEYWORDS]`<br> e.g., `find James Jake`
**Find student** | `find-s KEYWORD [MORE_KEYWORDS]`<br> e.g., `find-s James Jake`
**Find guardian** | `find-g KEYWORD [MORE_KEYWORDS]`<br> e.g., `find-g James Jake`
**Link guardian** | `link-g STUDENT_INDEX g/GUARDIAN_INDEX`<br> e.g., `link-g 2 g/1`
**List**   | `list`
**List students** | `list-s`
**List guardians** | `list-g`
**Help**   | `help`
