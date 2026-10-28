---
  layout: default.md
  title: "Developer Guide"
  pageNav: 3
---

# AB-3 Developer Guide

<!-- * Table of Contents -->
<page-nav-print />

--------------------------------------------------------------------------------------------------------------------

## **Acknowledgements**

* _{List the sources of reused or adapted ideas, code, documentation, and third-party libraries here, with links to the originals.}_

--------------------------------------------------------------------------------------------------------------------

## **Setting up, getting started**

Refer to the guide [_Setting up and getting started_](SettingUp.md).

--------------------------------------------------------------------------------------------------------------------

## **Design**

### Architecture

<puml src="diagrams/ArchitectureDiagram.puml" width="280" />

The ***Architecture Diagram*** given above explains the high-level design of the App.

The following provides a quick overview of the main components and their interactions.

**Main components of the architecture**

**`Main`** (consisting of classes [`Main`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/Main.java) and [`MainApp`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/MainApp.java)) is in charge of the app launch and shut down.
* At app launch, it initializes the other components in the correct sequence, and connects them up with each other.
* At shut down, it shuts down the other components and invokes cleanup methods where necessary.

The bulk of the app's work is done by the following four components:

* [**`UI`**](#ui-component): The UI of the App.
* [**`Logic`**](#logic-component): The command executor.
* [**`Model`**](#model-component): Holds the data of the App in memory.
* [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

**How the architecture components interact with each other**

The *Sequence Diagram* below shows how the components interact with each other for the scenario where the user issues the command `remove-s 1`.

<puml src="diagrams/ArchitectureSequenceDiagram.puml" width="574" />

Each of the four main components (also shown in the diagram above),

* defines its *API* in an `interface` with the same name as the Component.
* provides its functionality through a concrete `{Component Name}Manager` class that implements the corresponding API interface.

For example, the `Logic` component defines its API in `Logic.java` and implements it in `LogicManager.java`. Other components interact with a component through its interface rather than its concrete class, preventing them from coupling to that component's implementation, as illustrated in the following partial class diagram.

<puml src="diagrams/ComponentManagers.puml" width="300" />

The sections below give more details of each component.

### UI component

The **API** of this component is specified in [`Ui.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/Ui.java)

<puml src="diagrams/UiClassDiagram.puml" alt="Structure of the UI Component" />

The UI consists of a `MainWindow` and its parts, such as `CommandBox`, `ResultDisplay`, `PersonListPanel`, and `StatusBarFooter`. All of these, including `MainWindow`, inherit from the abstract `UiPart` class, which captures common behavior among classes that represent visible GUI parts.

The `UI` component uses the JavaFX UI framework. The layouts of these UI parts are defined in matching `.fxml` files in `src/main/resources/view`. For example, [`MainWindow.fxml`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/resources/view/MainWindow.fxml) specifies the layout of [`MainWindow`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/MainWindow.java).

The `UI` component,

* executes user commands using the `Logic` component.
* listens for changes to `Model` data so that the UI can be updated with the modified data.
* keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
* depends on some classes in the `Model` component because it displays `Person` objects from the model.

### Logic component

**API** : [`Logic.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/logic/Logic.java)

Here's a (partial) class diagram of the `Logic` component:

<puml src="diagrams/LogicClassDiagram.puml" width="550" />

The sequence diagram below illustrates the interactions within the `Logic` component, taking `execute("remove-s 1")` API call as an example.

<puml src="diagrams/DeleteSequenceDiagram.puml" alt="Interactions Inside the Logic Component for the `remove-s 1` Command" />

The following sequence diagram shows how `unlink-g` removes an existing student-guardian link.

<puml src="diagrams/UnlinkGuardianSequenceDiagram.puml"
      alt="Interactions for the unlink-g command"></puml>

<box type="info" seamless>

**Note:** The lifeline for `RemoveStudentCommandParser` should end at the destroy marker (X), but due to a limitation of PlantUML, the lifeline continues till the end of diagram.
</box>


How the `Logic` component works:

1. When `Logic` is called upon to execute a command, the command is passed to an `AddressBookParser` object, which in turn creates a parser that matches the command (e.g., `RemoveStudentCommandParser`) and uses it to parse the command.
1. This results in a `Command` object (more precisely, an object of one of its subclasses e.g., `RemoveStudentCommand`) which is executed by the `LogicManager`.
1. The command can communicate with the `Model` when it is executed (e.g. to remove a student).<br>
   Note that although this is shown as a single step in the diagram above for simplicity, the code can require several interactions between the command object and the `Model` to complete the operation.
1. The result of the command execution is encapsulated as a `CommandResult` object which is returned from `Logic`.

Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command:

<puml src="diagrams/ParserClasses.puml" width="600"></puml>

How the parsing works:
* When called upon to parse a user command, the `AddressBookParser` class creates an `XYZCommandParser` (`XYZ` is a placeholder for the specific command name, e.g., `AddCommandParser`). The parser uses the other classes shown above to parse the user command and create an `XYZCommand` object (e.g., `AddCommand`). The `AddressBookParser` returns that object as a `Command` object.
* All `XYZCommandParser` classes, such as `AddCommandParser` and `DeleteCommandParser`, implement the `Parser` interface so they can be treated similarly where appropriate, for example during testing.

The student and guardian MVP commands follow the same parser-command flow:

* `EditStudentCommandParser` parses `edit-s` indexes and optional student fields before creating an `EditStudentCommand`.
* `EditGuardianCommandParser` parses `edit-g` indexes and optional guardian fields before creating an `EditGuardianCommand`.
* `LinkGuardianCommandParser` parses a student index and a `g/` guardian index before creating a `LinkGuardianCommand`.
* `UnlinkGuardianCommandParser` parses a student index and a `g/` guardian index before creating an `UnlinkGuardianCommand`.
* `SetStudentRemarkCommandParser` parses `set-remark-s` and creates a `SetStudentRemarkCommand` that newly sets or replaces a student's remark.
* `ClearStudentRemarkCommandParser` parses `clear-remark-s` and creates a `ClearStudentRemarkCommand` that clears a student's remark.
* `ListStudentCommandParser` parses `list-s` and creates a `ListStudentCommand` that shows all students.
* `ListGuardianCommandParser` parses `list-g` and creates a `ListGuardianCommand` that shows all guardians.
* `FindStudentCommandParser` parses name keywords for `find-s` before creating a `FindStudentCommand`.
* `FindGuardianCommandParser` parses name keywords for `find-g` before creating a `FindGuardianCommand`.
* The edit commands update only supplied fields and preserve the remaining values, including student remarks and payment history.
* `LinkGuardianCommand` creates a `StudentGuardianLink` in the model and treats repeated pair as an existing link.
* `UnlinkGuardianCommand` checks that the selected link exists before asking the model to remove it.
* The list commands reset their respective filtered lists to show all students or guardians.
* The find commands filter their respective lists by name, matching any full-word keyword case-insensitively.

### Model component
**API** : [`Model.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/model/Model.java)

<puml src="diagrams/ModelClassDiagram.puml" width="450" />


The `Model` component is the in-memory domain model for TutorTrack. The diagram focuses on the classes needed to manage students, guardians, and their relationships; legacy generic-person classes and utility classes are omitted for readability.

* `ModelManager` implements the `Model` API and owns an `AddressBook` and `UserPrefs`. `AddressBook` and `UserPrefs` expose read-only interfaces to other components.
* `AddressBook` owns a unique student list, a unique guardian list, and the collection of `StudentGuardianLink` objects.
* `AddressBook` can remove an individual `StudentGuardianLink` without deleting either the student or the guardian.
* `Student` contains shared contact details together with an academic level and one or more subjects. `Guardian` contains the shared contact details. These domain classes are composed from value objects such as `Name`, `Phone`, `Email`, and `Address`.
* `StudentGuardianLink` represents a relationship between one student and one guardian. Duplicate links are rejected, and links are updated or removed when their linked student or guardian changes.
* `ModelManager` maintains filtered student and guardian views for the UI. The displayed student view can also be sorted by name without changing the underlying storage order.
* `UserPrefs` stores user preferences, currently the GUI settings, and is exposed through `ReadOnlyUserPrefs`.
* The Model component is independent of the UI, Logic, and Storage components; it represents domain data and exposes operations through its API.


### Storage component

**API** : [`Storage.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/storage/Storage.java)

<puml src="diagrams/StorageClassDiagram.puml" width="550" />

The `Storage` component,

* can save both address book data and user preference data in JSON format, and read them back into corresponding objects.
* is implemented by `StorageManager`, which delegates the actual JSON file access to `JsonAddressBookStorage` and `JsonUserPrefsStorage` (one class per data file).
* depends on some classes in the `Model` component (because the `Storage` component's job is to save/retrieve objects that belong to the `Model`)

Student–guardian links are persisted in the address-book JSON under a `links` array. Each entry stores the student and guardian name and phone, which identify the corresponding existing records without duplicating their full details. Links are resolved after students and guardians are loaded, preserving link creation order. A missing `links` field is treated as an empty list for compatibility with existing address-book files. A link referring to a student or guardian that is not present in the file is rejected as invalid data.

Student remarks are persisted as a `remark` string on each student. A missing `remark` field is treated as an empty remark for compatibility with existing address-book files.

Each student's paid months are persisted in a `paidMonths` array of `YYYY-MM` strings. A missing `paidMonths` field is treated as an empty list for compatibility with existing address-book files.


### Common classes

Classes used by multiple components are in the `seedu.address.commons` package.

--------------------------------------------------------------------------------------------------------------------

## **Implementation**

This section describes some noteworthy details on how certain features are implemented.

### Find feature

#### Implementation

The `find-s` command accepts one or more name keywords. `FindStudentCommandParser` creates a
`StudentNameContainsKeywordsPredicate`, which `FindStudentCommand` passes to the model to
update the filtered student list. Matching is case-insensitive, and a student is included
when their name contains any of the supplied keywords.

<puml src="diagrams/FindSequenceDiagram.puml" alt="Interactions Inside the Logic Component for the `find-s alice` Command" />

The same process is followed for `find-g`, using the corresponding guardian command parser,
command, predicate, and filtered guardian list.

### Student remarks feature

#### Implementation

Student remarks are represented by the immutable `Remark` value object, which validates and normalizes remark text. Each `Student` holds a non-null remark; `Remark.EMPTY` represents no remark.

The following partial sequence diagram shows `SetStudentRemarkCommand#execute(Model)` after parsing, assuming a valid index in the currently displayed student list. Parsing, invalid-input paths, persistence and UI handling are omitted.

<puml src="diagrams/SetStudentRemarkSequenceDiagram.puml" alt="Setting a student remark through immutable Student replacement" width="800" />

`Student#withRemark` creates a new `Student` object with the updated remark while preserving all other details. The command replaces the original Student only if the updated Student differs from it. Existing lessons and guardian links are preserved during replacement.

### Payment tracking feature

#### Implementation

Payment tracking lets a tutor record that a student has paid for a given month of a year, using the command `mark-paid INDEX m/YYYY-MM`. It is built from the following classes:

* `PaymentMonth` (in `model.student`) -- An immutable value class wrapping a `java.time.YearMonth`. It validates input in the strict `YYYY-MM` format (`PaymentMonth#isValidPaymentMonth(String)`), implements `Comparable<PaymentMonth>` for chronological ordering, and provides `PaymentMonth#toDisplayString()` (e.g. `Oct 2026`) for the UI and result messages.
* `Student` -- Holds an unmodifiable `SortedSet<PaymentMonth>` of the months the student has paid for, exposed through `Student#getPaidMonths()`. Two methods support the feature:
  * `Student#hasPaidFor(PaymentMonth)` -- Returns true if a payment is recorded for that month.
  * `Student#withPaymentFor(PaymentMonth)` -- Returns a **new** `Student` with the month added. `Student` remains immutable.
* `MarkPaidCommandParser` -- Tokenizes the arguments with the `m/` prefix (`CliSyntax#PREFIX_MONTH`), parses the index with `ParserUtil#parseIndex` and the month with `ParserUtil#parsePaymentMonth`, and rejects duplicate `m/` prefixes.
* `MarkPaidCommand` -- Looks up the student in the displayed student list and replaces it in the model with the updated copy.

<puml src="diagrams/PaymentClassDiagram.puml" width="450" />

Given below is an example usage scenario and how the payment tracking mechanism behaves at each step.

Step 1. The user executes `mark-paid 1 m/2026-10`. `AddressBookParser` creates a `MarkPaidCommandParser`, which parses the input into a `MarkPaidCommand` holding index `1` and the `PaymentMonth` `2026-10`.

Step 2. `MarkPaidCommand#execute(Model)` retrieves the first student, `s`, from `Model#getFilteredStudentList()`. If the index is out of range, a `CommandException` is thrown.

Step 3. The command calls `s.hasPaidFor(2026-10)`. If a payment is already recorded for that month, a `CommandException` is thrown and the model is left unchanged.

Step 4. Otherwise, the command calls `s.withPaymentFor(2026-10)` to create the updated student, then calls `Model#setStudent(s, paidStudent)`. `AddressBook#setStudent` also updates any `StudentGuardianLink` that refers to `s`, so the student's guardian links are preserved.

Step 5. The student list in the UI updates, and the `StudentCard` shows the new month in its `Paid:` line, e.g. `Paid: Sep 2026, Oct 2026`.

The following sequence diagram shows how the `mark-paid` command goes through the `Logic` and `Model` components:

<puml src="diagrams/MarkPaidSequenceDiagram.puml" alt="MarkPaidSequenceDiagram" />

<box type="info" seamless>

**Note:** The lifeline for `MarkPaidCommandParser` should end at the destroy marker (X), but due to a limitation of PlantUML, it continues to the end of the diagram.
</box>

**Interaction with other features:**

* **Remarks** -- `withPaymentFor` preserves the student's remark, and `withRemark` preserves all paid months. Setting, replacing or clearing a remark does not change payment history. Student identity remains based on normalized name and phone, while full equality and hashing include both remarks and payments.

* `edit-s` -- `EditStudentDescriptor#createEditedStudent` copies the original student's paid months into the edited student, so editing a student's details does not erase their payment history.
* `remove-s` -- Payment history is stored on the `Student`, so it is removed together with the student.
* `filter` -- `StudentMatchesFilterPredicate` takes an optional paid month and an optional unpaid month, parsed from the `paid/` and `unpaid/` prefixes. It uses `Student#hasPaidFor(PaymentMonth)` to keep only students who have paid for the paid month and have not paid for the unpaid month. These conditions are combined (AND) with the level and subject criteria.
* **Storage** -- `JsonAdaptedStudent` stores paid months as a `paidMonths` array of `YYYY-MM` strings. A missing `paidMonths` field is treated as an empty list, so data files created before this feature still load. An invalid entry (e.g. `2026-13`) is rejected as invalid data.

#### Design considerations:

**Aspect: How payments are stored:**

* **Alternative 1 (current choice):** Each `Student` holds a set of the months they have paid for.
  * Pros: Simple to implement. Payment history moves, edits and is deleted together with its student, with no extra bookkeeping in `AddressBook`.
  * Cons: Records only *whether* a month is paid, not the amount, payment date or method.

* **Alternative 2:** A separate `Payment` entity (student, month, amount, date) stored in its own list in `AddressBook`, similar to `StudentGuardianLink`.
  * Pros: Can hold richer payment details and supports payment-centric queries.
  * Cons: Every student edit and removal must keep the payment list consistent, as is already done for links, which adds complexity that the current requirements do not need.

**Aspect: How the month is specified:**

* **Alternative 1 (current choice):** The month is required and written as `YYYY-MM`.
  * Pros: Unambiguous, sorts naturally, and matches `YearMonth#parse`, so no custom date parsing is needed.
  * Cons: Slightly more typing than a month name.

* **Alternative 2:** Accept month names (e.g. `Oct 2026`) or default to the current month when `m/` is omitted.
  * Pros: Faster for the common case of recording this month's payment.
  * Cons: More input formats to validate. An implicit current month also makes it easy to record the wrong month by accident, and makes the command's behaviour depend on the system clock.

**Aspect: Marking a month that is already paid:**

* **Alternative 1 (current choice):** Show an error and leave the student unchanged.
  * Pros: Alerts the tutor to a possible mistake, such as entering the wrong student index.
  * Cons: Not idempotent, so repeating a command produces an error.

* **Alternative 2:** Silently succeed.
  * Pros: Idempotent.
  * Cons: The tutor gets no feedback that the command had no effect.

### \[Proposed\] Undo/redo feature

#### Proposed Implementation

The proposed undo/redo mechanism is facilitated by `VersionedAddressBook`. It extends `AddressBook` with an undo/redo history, stored internally as an `addressBookStateList` and `currentStatePointer`. Additionally, it implements the following operations:

* `VersionedAddressBook#commit()` -- Saves the current address book state in its history.
* `VersionedAddressBook#undo()` -- Restores the previous address book state from its history.
* `VersionedAddressBook#redo()` -- Restores a previously undone address book state from its history.

These operations are exposed in the `Model` interface as `Model#commitAddressBook()`, `Model#undoAddressBook()` and `Model#redoAddressBook()` respectively.

Given below is an example usage scenario and how the undo/redo mechanism behaves at each step.

Step 1. The user launches the application for the first time. The `VersionedAddressBook` will be initialized with the initial address book state, and the `currentStatePointer` pointing to that single address book state.

<puml src="diagrams/UndoRedoState0.puml" alt="UndoRedoState0" />

Step 2. The user executes `delete 5` command to delete the 5th person in the address book. The `delete` command calls `Model#commitAddressBook()`, causing the modified state of the address book after the `delete 5` command executes to be saved in the `addressBookStateList`, and the `currentStatePointer` is shifted to the newly inserted address book state.

<puml src="diagrams/UndoRedoState1.puml" alt="UndoRedoState1" />

Step 3. The user executes `add n/David …​` to add a new person. The `add` command also calls `Model#commitAddressBook()`, causing another modified address book state to be saved into the `addressBookStateList`.

<puml src="diagrams/UndoRedoState2.puml" alt="UndoRedoState2" />

<box type="info" seamless>

**Note:** If a command fails its execution, it will not call `Model#commitAddressBook()`, so the address book state will not be saved into the `addressBookStateList`.
</box>

Step 4. The user now decides that adding the person was a mistake, and decides to undo that action by executing the `undo` command. The `undo` command will call `Model#undoAddressBook()`, which will shift the `currentStatePointer` once to the left, pointing it to the previous address book state, and restores the address book to that state.

<puml src="diagrams/UndoRedoState3.puml" alt="UndoRedoState3" />


<box type="info" seamless>

**Note:** If the `currentStatePointer` is at index 0, pointing to the initial AddressBook state, then there are no previous AddressBook states to restore. The `undo` command uses `Model#canUndoAddressBook()` to check if this is the case. If so, it will return an error to the user rather
than attempting to perform the undo.
</box>

The following sequence diagram shows how an undo operation goes through the `Logic` component:

<puml src="diagrams/UndoSequenceDiagram-Logic.puml" alt="UndoSequenceDiagram-Logic" />

<box type="info" seamless>

**Note:** The lifeline for `UndoCommand` should end at the destroy marker (X), but due to a limitation of PlantUML, it continues to the end of the diagram.
</box>

Similarly, how an undo operation goes through the `Model` component is shown below:

<puml src="diagrams/UndoSequenceDiagram-Model.puml" alt="UndoSequenceDiagram-Model" />

The `redo` command does the opposite — it calls `Model#redoAddressBook()`, which shifts the `currentStatePointer` once to the right, pointing to the previously undone state, and restores the address book to that state.

<box type="info" seamless>

**Note:** If the `currentStatePointer` is at index `addressBookStateList.size() - 1`, pointing to the latest address book state, then there are no undone AddressBook states to restore. The `redo` command uses `Model#canRedoAddressBook()` to check if this is the case. If so, it will return an error to the user rather than attempting to perform the redo.
</box>

Step 5. The user then decides to execute the command `list`. Commands that do not modify the address book, such as `list`, will usually not call `Model#commitAddressBook()`, `Model#undoAddressBook()` or `Model#redoAddressBook()`. Thus, the `addressBookStateList` remains unchanged.

<puml src="diagrams/UndoRedoState4.puml" alt="UndoRedoState4" />

Step 6. The user executes `clear`, which calls `Model#commitAddressBook()`. Since the `currentStatePointer` is not pointing at the end of the `addressBookStateList`, all address book states after the `currentStatePointer` will be purged. Reason: It no longer makes sense to redo the `add n/David …` command. This is the behavior that most modern desktop applications follow.

<puml src="diagrams/UndoRedoState5.puml" alt="UndoRedoState5" />

The following activity diagram summarizes what happens when a user executes a new command:

<puml src="diagrams/CommitActivityDiagram.puml" width="250" />

#### Design considerations:

**Aspect: How undo & redo execute:**

* **Alternative 1 (current choice):** Saves the entire address book.
  * Pros: Easy to implement.
  * Cons: May have performance issues in terms of memory usage.

* **Alternative 2:** Individual command knows how to undo/redo by
  itself.
  * Pros: Will use less memory (e.g. for `delete`, just save the person being deleted).
  * Cons: We must ensure that the implementation of each individual command is correct.

_{more aspects and alternatives to be added}_

### \[Proposed\] Data archiving

_{Explain here how the data archiving feature will be implemented}_


--------------------------------------------------------------------------------------------------------------------

## **Documentation, logging, testing, dev-ops**

* [Documentation guide](Documentation.md)
* [Testing guide](Testing.md)
* [Logging guide](Logging.md)
* [DevOps guide](DevOps.md)

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Requirements**

### Product scope

**Target user profile**:

* has a need to manage a significant number of contacts
* prefers desktop apps over other types of applications
* can type fast
* prefers typing to mouse interactions
* is reasonably comfortable using CLI apps

**Value proposition**: Manage contacts faster than with a typical mouse-driven GUI application.


### User stories

Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`

| Priority | As a …                           | I want to …                                                       | So that I can…                                                               |
|----------|----------------------------------|-------------------------------------------------------------------|------------------------------------------------------------------------------|
| `* * *`  | new user                         | easily access guidance on available commands                      | learn how to use TutorTrack quickly                                          |
| `* * *`  | tutor                            | add a student and their relevant details                          | keep all the information I need about each student in one place              |
| `* * *`  | tutor                            | add guardian information                                          | keep track of the people I need to contact regarding my students             |
| `* * *`  | tutor                            | delete an incorrectly created student or guardian                 | remove records I do not need                                                 |
| `* * *`  | tutor                            | edit a student's or guardian's information                        | keep their details up to date                                                |
| `* * *`  | tutor                            | view the list of students                                         | know the details of all my students                                          |
| `* * *`  | tutor                            | view the guardian associated with a student                       | quickly know whom to contact regarding that student                          |
| `* * *`  | tutor                            | search for a student                                              | retrieve their information quickly without browsing my entire student list   |
| `* * *`  | tutor teaching multiple subjects | record the subjects I teach each student                          | remember what tuition I provide to each student                              |
| `* * `   | tutor with many students         | easily view my lessons for a day or a week                        | better plan my schedule and be on top of my work for each student            |
| `* *`    | tutor with many students         | sort students by name                                             | locate a specific student easily                                             |
| `* *`    | tutor with many students         | filter students by attribute                                      | quickly locate groups of students I am interested in                         |
| `* * `   | tutor rescheduling a lesson      | check whether a proposed timing clashes with my existing schedule | easily and confidently reschedule lessons                                    |
| `* *`    | first-time user                  | see sample student and guardian data                              | understand TutorTrack's capabilities before entering my own data             |
| `*`      | tutor ready to adopt TutorTrack  | remove all sample data                                            | start managing my actual students without leftover dummy data                |
| `*`      | tutor                            | set, replace and clear student remarks                            | keep my notes about each student up to date                                  |
| `*`      | tutor preparing for a lesson     | view my notes about a student                                     | remind myself of important information before teaching them                  |
| `*`      | tutor with past students         | archive student data                                              | keep my student list uncluttered while not completely deleting past students |
| `* *`    | tutor managing monthly payments  | mark a student as paid for a month                                | keep an accurate record of which months each student has paid for            |
| `*`      | tutor managing monthly payments  | view students payment status                                      | quickly identify whose payments need to be collected                         |

### Use cases

(For all use cases below, the **System** is `TutorTrack` and the **Actor** is the `user`, unless specified otherwise)

**Use case: U1 - Add a student**

**MSS**

1. User requests to add a student.
2. User provides the required student details.
3. TutorTrack validates and adds the student record.
4. TutorTrack confirms that the student was added.

   Use case ends.

**Extensions**

* 2a. The provided details are invalid or incomplete.

  * 2a1. TutorTrack informs the user of the error.
  * 2a2. User provides corrected details.

    Steps 2a1-2a2 are repeated until the details are valid and complete. Use
    case resumes from step 3.

* 2b. A student with the same identifying details already exists.

  * 2b1. TutorTrack informs the user that the student already exists.

    Use case ends.

**Use case: U2 - Add a guardian**

**MSS**

1. User requests to add a guardian.
2. User provides the required guardian details.
3. TutorTrack validates and adds the guardian record.
4. TutorTrack confirms that the guardian was added.

   Use case ends.

**Extensions**

* 2a. The provided details are invalid or incomplete.

  * 2a1. TutorTrack informs the user of the error.
  * 2a2. User provides corrected details.

    Steps 2a1-2a2 are repeated until the details are valid and complete. Use
    case resumes from step 3.

* 2b. A guardian with the same identifying details already exists.

  * 2b1. TutorTrack informs the user that the guardian already exists.

    Use case ends.

The guardian is not associated with a student by this use case; linking is a
separate use case.

**Use case: U3 - List students**

**MSS**

1. User requests to list students.
2. TutorTrack shows the full list of students.

   Use case ends.

**Extensions**

* 1a. The list is empty.

  Use case ends.

**Use case: U4 - List guardians**

**MSS**

1. User requests to list guardians.
2. TutorTrack shows the full list of guardians.

   Use case ends.

**Extensions**

* 1a. The list is empty.

  Use case ends.

**Use case: U5 - Edit student details**

**MSS**

1. User <u>requests to list students (U3)</u>.
2. User requests to edit a specific student in the list.
3. TutorTrack requests the details to be edited.
4. User enters the details to be edited.
5. TutorTrack updates the student's details.

   Use case ends.

**Extensions**

* 2a. The given index is invalid.

  * 2a1. TutorTrack shows an error message.

    Use case ends.

* 4a. There is an error in the entered data.

  * 4a1. TutorTrack requests the correct data.
  * 4a2. User enters new data.

    Steps 4a1-4a2 are repeated until the data entered is correct. Use case resumes from step 5.

**Use case: U6 - Edit guardian details**

**MSS**

1. User <u>requests to list guardians (U4)</u>.
2. User requests to edit a specific guardian in the list.
3. TutorTrack requests the details to be edited.
4. User enters the details to be edited.
5. TutorTrack updates the guardian's details.

   Use case ends.

**Extensions**

* 2a. The given index is invalid.

  * 2a1. TutorTrack shows an error message.

    Use case ends.

* 4a. There is an error in the entered data.

  * 4a1. TutorTrack requests the correct data.
  * 4a2. User enters new data.

    Steps 4a1-4a2 are repeated until the data entered is correct. Use case resumes from step 5.

**Use case: U7 - Delete student**

**MSS**

1. User <u>requests to list students (U3)</u>.
2. User requests to delete a specific student in the list.
3. TutorTrack deletes the student.

   Use case ends.

**Extensions**

* 2a. The given index is invalid.

  * 2a1. TutorTrack shows an error message.

    Use case ends.

**Use case: U8 - Delete guardian**

**MSS**

1. User <u>requests to list guardians (U4)</u>.
2. User requests to delete a specific guardian in the list.
3. TutorTrack deletes the guardian.

   Use case ends.

**Extensions**

* 2a. The given index is invalid.

  * 2a1. TutorTrack shows an error message.

    Use case ends.

**Use case: U9 - Link guardian to student**

**MSS**

1. User <u>requests to list students (U3)</u>.
2. User <u>requests to list guardians (U4)</u>.
3. User selects a student and a guardian to link.
4. TutorTrack links the student with the guardian.

   Use case ends.

**Extensions**

* 3a. One or both indices given are invalid.

  * 3a1. TutorTrack shows an error message.

    Use case ends.

* 3b. The student and guardian are already linked.

  * 3b1. TutorTrack informs the user that they are already linked.

    Use case ends.

**Use case: U10 - Filter contacts**

**MSS**

1. User requests to filter students by one or more criteria (academic level, subjects, a month paid for, a month not paid for).
2. TutorTrack shows the students that match all the criteria.

    Use case ends.

**Extensions**

* 1a. The command format or a criterion value is invalid.

    * 1a1. TutorTrack shows an error message.

      Use case resumes at step 1.

* 2a. No contact matches the criteria.

  Use case ends.

**Use case: U11 - Find contacts**

**MSS**

1. User requests to find contacts by name keywords.
2. TutorTrack shows the contacts whose names match any keyword.

    Use case ends.

**Extensions**

* 1a. No keyword is given.

    * 1a1. TutorTrack shows an error message.

      Use case resumes at step 1.

* 2a. No contact matches the keywords.

  Use case ends.

**Use case: U12 - Sort students**

**MSS**

1. User requests to sort the displayed students by name.
2. TutorTrack sorts the displayed student list in ascending order by name.
3. TutorTrack displays the sorted student list.

   Use case ends.

**Extensions**

* 1a. User provides arguments with the sorting request.

  * 1a1. TutorTrack shows an error message.

    Use case ends.

* 2a. The student list is empty.

  * 2a1. TutorTrack displays the empty student list.

    Use case ends.

**Use case: U13 - Schedule a lesson**

**MSS**

1. User <u>requests to list students (U3)</u>.
2. User requests to schedule a lesson for a specific student in the list, giving the day, start time and duration.
3. TutorTrack saves the lesson and shows the updated lesson slot.

    Use case ends.

**Extensions**

* 1a. The list is empty.

  Use case ends.

* 2a. The given index is invalid, or the person is not a student.

    * 2a1. TutorTrack shows an error message.

      Use case resumes at step 2.

* 2b. The day, start time or duration is invalid.

    * 2b1. TutorTrack shows an error message.

      Use case resumes at step 2.

* 2c. The lesson overlaps another student's lesson.

    * 2c1. TutorTrack shows a clash error message.

      Use case resumes at step 2.

* 2d. The lesson is less than 30 minutes from another lesson.

    * 2d1. TutorTrack shows a travel-time warning.

      Use case resumes at step 2.

**Use case: U14 - Mark a student's payment as paid**

**MSS**

1. User <u>requests to list students (U3)</u>.
2. User requests to mark a specific student in the list as paid for a given month of a year.
3. TutorTrack records the payment and shows the student's updated paid months.

   Use case ends.

**Extensions**

* 1a. The list is empty.

  Use case ends.

* 2a. The given index is invalid.

    * 2a1. TutorTrack shows an error message.

      Use case resumes at step 2.

* 2b. The month is missing or not in the format `YYYY-MM`.

    * 2b1. TutorTrack shows an error message describing the expected format.

      Use case resumes at step 2.

* 2c. The student is already marked as paid for the given month.

    * 2c1. TutorTrack shows an error message, and the student's record is unchanged.

      Use case ends.

**Use case: U15 - Set student remark**

**MSS**

1. User views the currently displayed student list.
2. User requests to set a remark for a specific student in the list, providing the remark text.
3. TutorTrack adds or updates the student's remark.

   Use case ends.

**Extensions**

* 2a. The given index is invalid.

  * 2a1. TutorTrack shows an error message.

    Use case ends.

* 2b. The remark text is invalid or missing.

  * 2b1. TutorTrack shows an error message.

    Use case ends.

**Use case: U16 - Clear student remark**

**MSS**

1. User views the currently displayed student list.
2. User requests to clear the remark of a specific student in the list.
3. TutorTrack clears the student's remark.

   Use case ends.

**Extensions**

* 2a. The given index is invalid.

  * 2a1. TutorTrack shows an error message.

    Use case ends.

* 2b. The student has no existing remark.

  * 2b1. TutorTrack informs the user that there is no remark to clear.

    Use case ends.

### Non-Functional Requirements

1.  Should work on any _mainstream OS_ as long as it has Java `25` or above installed.
2.  Should be able to hold up to 1000 contacts without noticeable sluggishness in performance for typical usage.
3.  A tutor with above average typing speed for regular English text (i.e. not code, not system admin commands) should be able to accomplish most of the tasks faster using commands than using the mouse.
4.  Should maintain consistent command formats and terminology across similar features.
5.  Should provide clear and informative error messages that help users identify and correct errors in their commands.
6.  Should preserve existing user data when a command fails to execute successfully.

### Glossary

* **Contact**: A student or guardian stored in TutorTrack.
* **Displayed list**: The student or guardian list currently displayed, either the full list or a filtered or found subset. Indices refer to the corresponding displayed list.
* **Student-guardian link**: An association between a student and a guardian. A student may be linked to multiple guardians, and a guardian may be linked to multiple students.
* **Lesson**: A scheduled tutoring session for a student, specified by its day, start time, and duration.
* **Archived student**: A past student whose data is retained in TutorTrack but is no longer included in the active student list.
* **Mainstream OS**: Windows, Linux, Unix, or macOS.
* **Payment month**: A month of a specific year, written as `YYYY-MM` (e.g. `2026-10`), for which a student's payment is recorded.

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Instructions for manual testing**

Given below are instructions to test the app manually.

<box type="info" seamless>

**Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more *exploratory* testing.
</box>

### Launch and shutdown

1. Initial launch

   1. Download the JAR file and copy it into an empty folder.

   1. Double-click the JAR file.<br>
      Expected: The GUI opens with a set of sample contacts. The window size may not be optimal.

1. Saving window preferences

   1. Resize the window to an optimal size. Move the window to a different location. Close the window.

   1. Relaunch the app by double-clicking the JAR file.<br>
       Expected: The most recent window size and location are retained.

1. _{ more test cases … }_

### Deleting a person

1. Deleting a person while all persons are being shown

   1. Prerequisites: List all persons using the `list` command, with multiple persons in the list.

   1. Test case: `delete 1`<br>
      Expected: The first contact is deleted from the list. The status message shows the deleted contact's details.

   1. Test case: `delete 0`<br>
      Expected: No person is deleted. The status message shows error details.

   1. Other incorrect delete commands to try: `delete`, `delete x`, `...` (where x is larger than the list size)<br>
      Expected: Similar to previous.

1. _{ more test cases … }_

### Marking a student's payment

1. Marking a student as paid while all students are being shown

   1. Prerequisites: At least two students in the student list, neither marked as paid for October 2026.

   1. Test case: `mark-paid 1 m/2026-10`<br>
      Expected: The first student's card shows `Oct 2026` in its `Paid:` line. The status message confirms that the student is marked as paid for Oct 2026.

   1. Test case: `mark-paid 1 m/2026-10` (repeated)<br>
      Expected: No change. The status message says the student is already marked as paid for Oct 2026.

   1. Test case: `mark-paid 1 m/2026-09`<br>
      Expected: The first student's `Paid:` line shows `Sep 2026, Oct 2026`, in chronological order.

   1. Test case: `mark-paid 0 m/2026-10`<br>
      Expected: No student is updated. The status message shows an invalid command format error.

   1. Test case: `mark-paid 1 m/2026-13`, `mark-paid 1 m/10-2026`, `mark-paid 1`<br>
      Expected: No student is updated. The status message shows an error describing the expected `YYYY-MM` format, or an invalid command format error.

   1. Test case: `mark-paid x m/2026-10` (where x is larger than the list size)<br>
      Expected: No student is updated. The status message says the student index is invalid.

1. Payment history is kept after editing and restarting

   1. Prerequisites: The first student is marked as paid for October 2026.

   1. Test case: `edit-s 1 p/98765432`<br>
      Expected: The phone number is updated, and `Oct 2026` remains in the `Paid:` line.

   1. Close and relaunch the app.<br>
      Expected: The student's paid months are unchanged.

1. Filtering students by payment status

   1. Prerequisites: The first student is marked as paid for October 2026, and the other students are not.

   1. Test case: `filter unpaid/2026-10`<br>
      Expected: Every student except the one marked as paid is listed. The status message shows the number of students listed.

   1. Test case: `filter paid/2026-10`<br>
      Expected: Only the student marked as paid is listed.

   1. Test case: `filter paid/2026-10 unpaid/2026-10`<br>
      Expected: No students are listed. The status message says that no students match the filter.

   1. Test case: `filter unpaid/2026-13`, `filter paid/2026-10 paid/2026-11`<br>
      Expected: The list is not changed. The status message shows the month format error, or a duplicate-field error.

### Saving data

1. Dealing with missing/corrupted data files

   1. _{Explain how to simulate missing or corrupted data files and state the expected behavior.}_

1. _{ more test cases … }_
