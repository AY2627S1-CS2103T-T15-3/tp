---
  layout: default.md
  title: "Developer Guide"
  pageNav: 3
---

# TutorTrack Developer Guide

<!-- * Table of Contents -->
<page-nav-print />

--------------------------------------------------------------------------------------------------------------------

## **Acknowledgements**

* TutorTrack is adapted from [AddressBook-Level3](https://se-education.org/addressbook-level3/) (AB3) by the
  [SE-EDU initiative](https://se-education.org). Its overall architecture, much of its code, and parts of this guide
  are reused from AB3.
* Libraries used: [JavaFX](https://openjfx.io/), [Jackson](https://github.com/FasterXML/jackson),
  [JUnit5](https://github.com/junit-team/junit5).

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

**`Main`** (consisting of classes [`Main`](https://github.com/AY2627S1-CS2103T-T15-3/tp/tree/master/src/main/java/seedu/address/Main.java) and [`MainApp`](https://github.com/AY2627S1-CS2103T-T15-3/tp/tree/master/src/main/java/seedu/address/MainApp.java)) is in charge of the app launch and shut down.
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

The **API** of this component is specified in [`Ui.java`](https://github.com/AY2627S1-CS2103T-T15-3/tp/tree/master/src/main/java/seedu/address/ui/Ui.java)

<puml src="diagrams/UiClassDiagram.puml" alt="Structure of the UI Component" />

The UI consists of a `MainWindow` and its parts, such as `CommandBox`, `ResultDisplay`, `StudentListPanel`, `GuardianListPanel`, `LessonListPanel`, and `StatusBarFooter`. Each list panel displays its items using the corresponding `StudentCard`, `GuardianCard`, or `LessonCard`. All of these, including `MainWindow`, inherit from the abstract `UiPart` class, which captures common behavior among classes that represent visible GUI parts.

The figure below shows the main window with the sample data. From top to bottom and left to right, it shows the `CommandBox`, the `ResultDisplay`, the `StudentListPanel`, `GuardianListPanel` and `LessonListPanel`, and the `StatusBarFooter`.

![Main window of TutorTrack](images/Ui.png)

The `UI` component uses the JavaFX UI framework. The layouts of these UI parts are defined in matching `.fxml` files in `src/main/resources/view`. For example, [`MainWindow.fxml`](https://github.com/AY2627S1-CS2103T-T15-3/tp/tree/master/src/main/resources/view/MainWindow.fxml) specifies the layout of [`MainWindow`](https://github.com/AY2627S1-CS2103T-T15-3/tp/tree/master/src/main/java/seedu/address/ui/MainWindow.java).

The `UI` component,

* executes user commands using the `Logic` component.
* listens for changes to `Model` data so that the UI can be updated with the modified data.
* keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
* depends on some classes in the `Model` component because it displays `Student`, `Guardian`, and `Lesson` objects from the model.
* uses the `CommandResult` returned by `Logic` to decide how to update the display, e.g. which student or guardian to highlight, which name keywords to highlight after a search, and whether to show, replace, or clear the active filter or search label above the student and guardian lists.

### Logic component

**API** : [`Logic.java`](https://github.com/AY2627S1-CS2103T-T15-3/tp/tree/master/src/main/java/seedu/address/logic/Logic.java)

Here's a (partial) class diagram of the `Logic` component:

<puml src="diagrams/LogicClassDiagram.puml" width="550" />

The sequence diagram below illustrates the interactions within the `Logic` component, taking `execute("remove-s 1")` API call as an example.

<puml src="diagrams/DeleteSequenceDiagram.puml" alt="Interactions Inside the Logic Component for the `remove-s 1` Command" />

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
* When called upon to parse a user command, the `AddressBookParser` class creates an `XYZCommandParser` (`XYZ` is a placeholder for the specific command name, e.g., `AddStudentCommandParser`). The parser uses the other classes shown above to parse the user command and create an `XYZCommand` object (e.g., `AddStudentCommand`). The `AddressBookParser` returns that object as a `Command` object.
* All `XYZCommandParser` classes, such as `AddStudentCommandParser` and `RemoveStudentCommandParser`, implement the `Parser` interface so they can be treated similarly where appropriate, for example during testing.
* Commands that take no arguments that need parsing (`clear`, `exit` and `help`) are created directly by `AddressBookParser` without a dedicated parser.

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
* `ViewLessonCommandParser` parses an optional date or displayed student index for `view-l` before creating a
  `ViewLessonCommand`. A bare `view-l` creates a command that clears the lesson filter.
* `FilterCommandParser`, `ViewLessonCommandParser` and `MarkPaidCommandParser` scan the arguments for any
  `word/` token that is not one of their supported prefixes and reject it with an `Unknown parameter: PREFIX` error.
  Without this check, an unknown prefix would be silently treated as part of the preceding value, producing a
  misleading format error.
* The edit commands update only supplied fields and preserve the remaining values, including student remarks and payment history.
* `AddLessonCommandParser` parses a student index and the `d/`, `t/`, `len/` (or `l/`) and `w/` prefixes before
  creating an `AddLessonCommand`, while `RemoveLessonCommandParser` parses a displayed lesson index.
* `MarkPaidCommandParser`, `FilterCommandParser` and `SortStudentCommandParser` parse the payment, filter and sort
  commands described in the [Implementation](#implementation) section.
* `LinkGuardianCommand` creates a `StudentGuardianLink` in the model and rejects a pair that is already linked.
* `UnlinkGuardianCommand` checks that the selected link exists before asking the model to remove it.
* The add and edit commands preserve the active predicate for their respective student or guardian lists.
  Newly added or edited records are displayed only when they match that predicate.
* The list commands reset their respective filtered lists to show all students or guardians.
* The find commands filter their respective lists by name, keeping a record if any word in its name starts with any
  keyword, ignoring case.
* Command words are case-insensitive: `AddressBookParser` lower-cases the command word before matching it.
  Parameter prefixes remain case-sensitive.

### Model component
**API** : [`Model.java`](https://github.com/AY2627S1-CS2103T-T15-3/tp/tree/master/src/main/java/seedu/address/model/Model.java)

<puml src="diagrams/ModelClassDiagram.puml" width="450" />


The `Model` component is the in-memory domain model for TutorTrack. The diagram focuses on the classes needed to manage students, guardians, and their relationships; legacy generic-person classes and utility classes are omitted for readability.

* `ModelManager` implements the `Model` API and owns an `AddressBook` and `UserPrefs`. `AddressBook` and `UserPrefs` expose read-only interfaces to other components.
* `AddressBook` owns a unique student list, a unique guardian list, a unique lesson list, and the collection of `StudentGuardianLink` objects.
* `AddressBook` can remove an individual `StudentGuardianLink` without deleting either the student or the guardian.
* `Student` contains shared contact details together with an academic level, one or more subjects, a `Remark` (which may be `Remark.EMPTY`), and the set of `PaymentMonth`s the student has paid for. `Guardian` contains the shared contact details. These domain classes are composed from value objects such as `Name`, `Phone`, `Email`, and `Address`.
* Two students (or two guardians) are considered the same record when their names match after trimming, collapsing whitespace and ignoring case, and their phone numbers match. This identity is used to reject duplicates.
* `Lesson` represents one scheduled session of a student, with a date, start time, and duration. `UniqueLessonList` rejects lessons that overlap any existing lesson.
* `StudentGuardianLink` represents a relationship between one student and one guardian. Duplicate links are rejected, and links are updated or removed when their linked student or guardian changes.
* When a student is edited, `AddressBook#setStudent` also replaces the student in their lessons and links. When a student is removed, their lessons and links are removed too. Removing a guardian removes only that guardian's links.
* `ModelManager` maintains filtered student, guardian, and lesson views for the UI. Lessons are sorted by date and
  start time, while the displayed student view can also be sorted by name without changing the underlying storage order.
  Lesson filters can match either a date or a student.
* `UserPrefs` stores user preferences, currently the GUI settings, and is exposed through `ReadOnlyUserPrefs`.
* The Model component is independent of the UI, Logic, and Storage components; it represents domain data and exposes operations through its API.


### Storage component

**API** : [`Storage.java`](https://github.com/AY2627S1-CS2103T-T15-3/tp/tree/master/src/main/java/seedu/address/storage/Storage.java)

<puml src="diagrams/StorageClassDiagram.puml" width="550" />

The `Storage` component,

* can save both address book data and user preference data in JSON format, and read them back into corresponding objects.
* is implemented by `StorageManager`, which delegates the actual JSON file access to `JsonAddressBookStorage` and `JsonUserPrefsStorage` (one class per data file).
* depends on some classes in the `Model` component (because the `Storage` component's job is to save/retrieve objects that belong to the `Model`)

Student–guardian links are persisted in the address-book JSON under a `links` array. Each entry stores the student and guardian name and phone, which identify the corresponding existing records without duplicating their full details. Links are resolved after students and guardians are loaded, preserving link creation order. A missing `links` field is treated as an empty list for compatibility with existing address-book files. A link referring to a student or guardian that is not present in the file is rejected as invalid data.

Student remarks are persisted as a `remark` string on each student. A missing `remark` field is treated as an empty remark for compatibility with existing address-book files.

Each student's paid months are persisted in a `paidMonths` array of `YYYY-MM` strings. A missing `paidMonths` field is treated as an empty list for compatibility with existing address-book files.

Lessons are persisted in a `lessons` array. Each entry stores the student's name and phone, which identify the
student, together with the `date`, `startTime` and `durationMinutes` of the lesson. A lesson that refers to a missing
student, or that overlaps another stored lesson, is rejected as invalid data.

When the app starts, `MainApp` asks `Storage` to read the address book:

* If the data file does not exist, the app starts with the sample data from `SampleDataUtil`.
* If the data file cannot be read or contains invalid data, the invalid file is deleted and the app starts with an
  empty address book.


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
when any word in their name starts with any of the supplied keywords. For example, `find-s ali` matches `Alice Tan`.

`FindStudentCommand` returns a `CommandResult` carrying the keywords, so that `StudentCard` can highlight the
matching parts of each name, and a description of the search (e.g. `Search: alice lucy`) to be shown above the list.

<puml src="diagrams/FindSequenceDiagram.puml" alt="Interactions Inside the Logic Component for the `find-s alice` Command" />

The same process is followed for `find-g`, using the corresponding guardian command parser,
command, predicate, and filtered guardian list.

### Student and guardian list status indicators

`FilterCommand` and `FindStudentCommand` include a user-facing description of the active student filter or search in
their `CommandResult`. `MainWindow` applies this update to the `StudentListPanel`, which displays the description above
the student cards. Commands that do not change the student predicate leave the description unchanged, so it remains
visible after commands such as `sort-s`. `ListStudentCommand` explicitly clears the description. `FindGuardianCommand`
and `ListGuardianCommand` provide the equivalent status updates for the guardian search displayed above the guardian
cards.

The screenshot below shows the label displayed after `filter unpaid/2026-10`.

![Active filter label above the student list](images/filterResult.png)

### Add lesson feature

#### Implementation

The `add-l INDEX d/YYYY-MM-DD t/HH:MM len/LENGTH(MINUTES) w/WEEKS` command schedules a weekly series of lessons for a
student. `AddLessonCommandParser` validates the index, date, time, length and number of weeks, and accepts `l/` as a
short form of `len/`. Dates and times are parsed strictly by `DateTimeParser`.

When executed, `AddLessonCommand`:

1. looks up the student in the displayed student list, throwing a `CommandException` if the index is out of range;
1. creates one `Lesson` per week, each a week after the previous one;
1. checks every new lesson against all existing lessons (`Model#getLessonList()`) and against the other lessons in the
   same series, using `Lesson#overlaps(Lesson)`. If any overlap is found, no lesson is added and the error names the
   conflicting lesson;
1. otherwise adds all the lessons to the model. If the lesson length exceeds `Lesson.MAX_RECOMMENDED_DURATION`
   (4 hours), the success message also contains a warning.

Validating the whole series before adding any lesson keeps the command all-or-nothing, so a clash in a later week
never leaves a partially added series.

The lesson filter is not changed by `add-l`, so the new lessons appear in the lesson panel only if they match it.
`remove-l INDEX` removes one lesson from the displayed lesson list without affecting the rest of its series.

### View lesson feature

#### Implementation

The `view-l` command supports three modes. `ViewLessonCommandParser` accepts no arguments, a date with the `d/`
prefix, or a displayed student index with the `stu/` prefix. It rejects extra arguments, unknown prefixes, duplicate
prefixes, invalid dates, and simultaneous date and student filters.

`ViewLessonCommand` updates the model's filtered lesson list with one of the following predicates:

* `Model.PREDICATE_SHOW_ALL_LESSONS` for a bare `view-l` command;
* `LessonMatchesDatePredicate` for a date filter; or
* `LessonMatchesStudentPredicate` for a student filter.

`LessonMatchesDatePredicate` treats a lesson as occurring on a date when its time interval overlaps that calendar
day. This includes lessons that begin on one date and end after midnight on the next date. The model exposes the
complete lesson list separately from the filtered, date-and-time-sorted lesson list so commands can filter without
altering stored data.

The UI's `LessonListPanel` displays the filtered list using `LessonCard` components. It maintains the selected date
for the schedule view, refreshes after commands execute, and shows a placeholder when no lessons match. The main
window connects date changes from the panel to the model's `LessonMatchesDatePredicate`, while `view-l stu/INDEX`
changes the model filter through the normal command flow.

`ModelManager` remembers the active lesson predicate. When `Model#setStudent` replaces a student while the lessons are
filtered by that student (checked with `LessonMatchesStudentPredicate#matchesStudent`), it replaces the predicate with
one for the edited student. Without this, editing the student would leave the lesson panel empty, because the old
predicate would refer to a `Student` object that no longer exists.

The command flow is illustrated below. The three branches represent date
filtering, student filtering, and clearing the filter with a bare `view-l`.

<puml src="diagrams/ViewLessonSequenceDiagram.puml" alt="Interactions for the `view-l` command" />

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

Step 5. The student list in the UI updates, and the `StudentCard` shows the new month in its `Paid:` line, e.g. `Paid: Oct 2026, Sep 2026`. The line shows the 3 most recent months, most recent first, and a `+N more` button beside it expands the line to show all months (and `Show less` collapses it again).

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

### Link guardian feature

#### Implementation

The `link-g STUDENT_INDEX g/GUARDIAN_INDEX` command links an existing guardian to an existing student. The link is stored as a `StudentGuardianLink` in `AddressBook`, so one guardian can be linked to many students and one student can have many guardians.

The sequence diagram below shows the interactions within the `Logic` and `Model` components for the command `link-g 1 g/2`.

<puml src="diagrams/LinkGuardianSequenceDiagram.puml" alt="Interactions Inside the Logic Component for the `link-g 1 g/2` Command" />

1. `LinkGuardianCommandParser` parses the student index and the `g/` guardian index, and creates a `LinkGuardianCommand`.
1. When executed, `LinkGuardianCommand` looks up the student and guardian in the currently displayed (filtered) student and guardian lists. If either index is out of range, a `CommandException` is thrown.
1. A `StudentGuardianLink` is created from the two. If the model already contains this link, a `CommandException` is thrown and the model is left unchanged. Otherwise, the link is added to the model.
1. The `CommandResult` identifies the student, so the UI highlights the student, whose card now lists the guardian.

The sequence diagram below shows how `Model#addStudentGuardianLink(l)` stores the link inside the `Model` component.

<puml src="diagrams/LinkGuardianSequenceDiagram-Model.puml" alt="Interactions Inside the Model Component for adding a StudentGuardianLink" />

`ModelManager` passes the link to `AddressBook`, which checks again for a duplicate before adding it to its `studentGuardianLinks` list.

#### Unlinking a guardian from a student

The `unlink-g STUDENT_INDEX g/GUARDIAN_INDEX` command removes an existing
`StudentGuardianLink`. It validates both displayed indexes, checks that the
relationship exists, and removes the link through `Model` and `AddressBook`.
If the relationship does not exist, the command reports an error and leaves
the address book unchanged.

The following diagram shows the parser, command, model, and address-book
interactions for `unlink-g 1 g/2`.

<puml src="diagrams/UnlinkGuardianSequenceDiagram.puml" alt="Interactions Inside the Logic Component for the `unlink-g 1 g/2` Command" />

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

* is a private tutor who teaches a significant number of students, possibly across different levels and subjects
* needs to keep track of students' guardians, weekly lessons, monthly payments, and notes about each student
* prefers desktop apps over other types of applications
* can type fast
* prefers typing to mouse interactions
* is reasonably comfortable using CLI apps

**Value proposition**: Manage students, guardians, lesson schedules, and payments in one place, faster than with a
typical mouse-driven GUI application or a collection of spreadsheets and chat messages.


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

**Command:** `add-l INDEX d/YYYY-MM-DD t/HH:MM len/LENGTH(MINUTES) w/WEEKS`

The lesson-length prefix `len/` may be shortened to `l/`.

**MSS**

1. User <u>requests to list students (U3)</u>.
2. User requests a weekly series of lessons for a specific student in the list, giving the first day, start time, duration and number of weeks.
3. TutorTrack saves all lessons in the series and shows the updated lesson slots.

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

* 2c. A lesson in the series overlaps an existing lesson or another lesson in the series.

    * 2c1. TutorTrack shows a clash error message and adds none of the lessons in the series.

      Use case resumes at step 2.

* 2d. The number of weeks is not between 1 and 52.

    * 2d1. TutorTrack shows an error message.

      Use case resumes at step 2.

**Use case: U14 - Remove a lesson**

**Command:** `remove-l INDEX`

**MSS**

1. User views the currently displayed lesson list.
2. User requests to remove a lesson by its displayed index.
3. TutorTrack removes the selected lesson and displays the updated lesson list.

    Use case ends.

**Extensions**

* 1a. The lesson list is empty.

  Use case ends.

* 2a. The given index is invalid or outside the displayed lesson list.

    * 2a1. TutorTrack shows an error message and leaves the lesson list unchanged.

      Use case resumes at step 2.

**Use case: U15 - View lessons**

**Command:** `view-l [d/YYYY-MM-DD | stu/INDEX]`

The user may filter lessons by a calendar date or by a student in the currently displayed student list. A bare
`view-l` clears the active lesson filter and displays all lessons.

**MSS**

1. User requests to view lessons, optionally supplying a date or displayed student index.
2. TutorTrack filters the lesson list using the supplied criterion, or removes the filter if no criterion is given.
3. TutorTrack displays the matching lessons sorted by date and start time.

    Use case ends.

**Extensions**

* 1a. The command format is invalid, the date is not in `YYYY-MM-DD` format, or both filters are supplied.

    * 1a1. TutorTrack shows an error message and leaves the current lesson filter unchanged.

      Use case resumes at step 1.

* 1b. The student index is invalid or outside the currently displayed student list.

    * 1b1. TutorTrack shows an error message and leaves the current lesson filter unchanged.

      Use case resumes at step 1.

* 2a. A lesson overlaps the requested date, including a lesson that crosses midnight.

    * 2a1. TutorTrack includes the lesson in the displayed results.

      Use case resumes at step 3.

* 3a. No lessons match the criterion.

    * 3a1. TutorTrack displays an empty lesson list with an appropriate placeholder.

      Use case ends.

**Use case: U16 - Mark a student's payment as paid**

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

**Use case: U17 - Set student remark**

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

**Use case: U18 - Clear student remark**

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

**Use case: U19 - Unlink guardian from student**

**MSS**

1. User <u>requests to list students (U3)</u>.
2. User <u>requests to list guardians (U4)</u>.
3. User selects a student and a guardian to unlink.
4. TutorTrack removes the link between the student and the guardian, keeping both records.

   Use case ends.

**Extensions**

* 3a. One or both indices given are invalid.

  * 3a1. TutorTrack shows an error message.

    Use case ends.

* 3b. The student and guardian are not linked.

  * 3b1. TutorTrack informs the user that the student is not linked to the guardian.

    Use case ends.

**Use case: U20 - Clear all data**

**MSS**

1. User requests to clear all data, e.g. to remove the sample data shown on first launch.
2. TutorTrack removes all students, guardians, lessons, and links.

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

   1. Double-click the JAR file, or run `java -jar addressbook.jar` from that folder.<br>
      Expected: The GUI opens with sample data: four students (Alice Tan, Henry Tan, Lucy Lim, Sam Lee), three
      guardians, and their links, remarks, payments and lessons. The lessons panel shows today's lessons. The window
      size may not be optimal.

1. Saving window preferences

   1. Resize the window to an optimal size. Move the window to a different location. Close the window.

   1. Relaunch the app by double-clicking the JAR file.<br>
       Expected: The most recent window size and location are retained.

1. Shutting down

   1. Test case: `exit`<br>
      Expected: The app closes. Relaunching shows the same data as before.

The test cases below assume the app has just been launched with the sample data, unless stated otherwise.

### Removing a student

1. Removing a student while all students are being shown

   1. Prerequisites: List all students using the `list-s` command.

   1. Test case: `remove-s 1`<br>
      Expected: Alice Tan is removed from the student list. Her lessons are removed from the lessons panel, and her
      guardians remain in the guardian list. The result display shows the removed student's details.

   1. Test case: `remove-s 0`<br>
      Expected: No student is removed. The result display shows an invalid command format error.

   1. Other incorrect remove commands to try: `remove-s`, `remove-s x` (where x is larger than the list size)<br>
      Expected: Similar to previous, or an error that the student index is invalid.

1. Removing a student from a filtered list

   1. Prerequisites: Run `find-s lucy`, so that only Lucy Lim is shown.

   1. Test case: `remove-s 1`<br>
      Expected: Lucy Lim is removed, even though she is not the first student in the full list.

### Scheduling, viewing and removing lessons

1. Scheduling lessons

   1. Test case: `add-l 4 d/2026-11-14 t/18:00 len/120 w/3`<br>
      Expected: `Added 3 lesson(s) for Sam Lee.` Running `view-l stu/4` shows the three new lessons on 14, 21 and 28
      November 2026, together with Sam's existing lesson.

   1. Test case: `add-l 1 d/2026-11-01 t/14:30 len/60 w/1`<br>
      Expected: No lesson is added. The error states that the lesson overlaps Alice Tan's lesson on 2026-11-01.

   1. Test case: `add-l 1 d/2026-12-01 t/09:00 len/300 w/1`<br>
      Expected: The lesson is added, and the result display shows a warning that the lesson exceeds 4 hours.

   1. Other incorrect commands to try: `add-l 1 d/2026-02-30 t/10:00 len/60 w/1`, `add-l 1 d/2026-11-20 t/25:00 len/60 w/1`,
      `add-l 1 d/2026-11-20 t/10:00 len/0 w/1`, `add-l 1 d/2026-11-20 t/10:00 len/60 w/53`<br>
      Expected: No lesson is added. The result display shows an error describing the invalid value.

1. Viewing lessons

   1. Test case: `view-l d/2026-11-10`<br>
      Expected: The lessons panel title changes to `Lessons for 2026-11-10` and shows Lucy Lim's lesson starting at
      23:30.

   1. Test case: `view-l d/2026-11-11`<br>
      Expected: The same lesson is shown, because it ends after midnight on 11 November.

   1. Test case: `view-l stu/3`<br>
      Expected: The title changes to `Lessons for Lucy Lim` and only Lucy Lim's lessons are shown.

   1. Test case: `view-l`<br>
      Expected: The title changes to `Lessons` and all lessons are shown.

   1. Test case: `view-l d/2026-11-01 stu/1`, `view-l d/2026-13-01`<br>
      Expected: The lesson filter is unchanged, and an error message is shown.

1. Removing a lesson

   1. Prerequisites: Run `view-l stu/1` to show Alice Tan's two lessons.

   1. Test case: `remove-l 1`<br>
      Expected: The lesson on 2026-11-01 is removed. The lesson on 2026-11-08 remains.

   1. Test case: `remove-l 5`<br>
      Expected: No lesson is removed. The result display says the lesson index is invalid.

### Linking and unlinking guardians

1. Test case: `link-g 2 g/1`<br>
   Expected: Henry Tan is highlighted, and his card lists Billie O'Connor as a guardian.

1. Test case: `link-g 2 g/1` (repeated)<br>
   Expected: No change. The result display says that Henry Tan is already linked to Billie O'Connor.

1. Test case: `unlink-g 2 g/1`<br>
   Expected: Billie O'Connor is removed from Henry Tan's `Guardians:` line. Both records remain.

1. Test case: `unlink-g 4 g/1`<br>
   Expected: No change. The result display says that Sam Lee is not linked to Billie O'Connor.

### Setting and clearing remarks

1. Test case: `set-remark-s 2 Practise fractions before the next lesson`<br>
   Expected: Henry Tan's card shows `Remark: Practise fractions before the next lesson`.

1. Test case: `clear-remark-s 2`<br>
   Expected: The remark line disappears from Henry Tan's card.

1. Test case: `clear-remark-s 2` (repeated)<br>
   Expected: The result display says that Henry Tan has no remark to clear.

1. Test case: `set-remark-s 2`, `set-remark-s 0 Hello`<br>
   Expected: No remark is changed, and an error message is shown.

### Finding and filtering

1. Test case: `find-s ali`<br>
   Expected: Only Alice Tan is shown. `Ali` is highlighted in her name, and `Search: ali` is shown above the list.

1. Test case: `sort-s` after the previous test<br>
   Expected: The search label remains visible.

1. Test case: `filter l/jc 1`<br>
   Expected: Only Sam Lee is shown, and the label above the list is replaced by the filter description.

1. Test case: `list-s`<br>
   Expected: All students are shown, and the label is hidden.

1. Test case: `find-g tan`<br>
   Expected: Only John Tan is shown in the guardian list, with `Tan` highlighted and `Search: tan` shown above it.

### Marking a student's payment

1. Marking a student as paid while all students are being shown

   1. Prerequisites: At least two students in the student list, neither marked as paid for October 2026.

   1. Test case: `mark-paid 1 m/2026-10`<br>
      Expected: The first student's card shows `Oct 2026` in its `Paid:` line. The status message confirms that the student is marked as paid for Oct 2026.

   1. Test case: `mark-paid 1 m/2026-10` (repeated)<br>
      Expected: No change. The status message says the student is already marked as paid for Oct 2026.

   1. Test case: `mark-paid 1 m/2026-09`<br>
      Expected: The first student's `Paid:` line shows `Oct 2026, Sep 2026`, with the most recent month first.

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

   1. Test case: `filter n/Alice`<br>
      Expected: The list is not changed. The status message says `Unknown parameter: n/`.

### Saving data

1. Dealing with a missing data file

   1. Close the app, then delete `data/addressbook.json` from the folder containing the JAR file.

   1. Relaunch the app.<br>
      Expected: The app starts with the sample data.

1. Dealing with a corrupted data file

   1. Close the app, then open `data/addressbook.json` in a text editor and make it invalid, e.g. change a student's
      `phone` to `abc`, or delete a closing `}`.

   1. Relaunch the app.<br>
      Expected: The app starts with no students, guardians or lessons, and the invalid data file is deleted.

1. Data is saved after every command

   1. Run `add-g n/Ben Tan p/92345678 e/ben.tan@gmail.com a/12 ABCD Ave 3`, then close and relaunch the app.<br>
      Expected: Ben Tan is still in the guardian list.
