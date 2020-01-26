# An in-process practice Todo app to help me prepare for Google's Associate Android Developer Certification exam

## A very happy ending/beginning

I'm thrilled to announce that I just (August 8, 2018) received my Google Associate Android Developer certificate: [https://www.credential.net/c2h121dl?key=cbb1e3a79763d330559fd1e92e98e3192a1d8fae4317e54cb5344835c9a42377](https://www.credential.net/c2h121dl?key=cbb1e3a79763d330559fd1e92e98e3192a1d8fae4317e54cb5344835c9a42377)

## About the app/project

**_Note: This app and the "Exam Content" tasks described below were all for the ORIGINAL 2016 VERSION of Google's 
Associate Android Developer certification exam, but in August 2018 Google released an updated version 
of the exam featuring Architecture Components. So you should no longer use this app and the list of tasks in this 
README as part of your practice for taking the exam -- instead, click each of the "Study Guide" competency area topics 
for the updated exam at [https://developers.google.com/training/certification/associate-android-developer/#exam-content](https://developers.google.com/training/certification/associate-android-developer/#exam-content) 
to see the detailed lists of tasks/"individual competencies" you need for the exam._**

Anyway, **_This app is ABSOLUTELY NOT a project or any other material from the actual exam itself_**, it's just a way 
for me to get more practice in preparation for taking Google's Associate Android Developer Certification Exam, by 
implementing all the tasks listed on the "Exam Content" tab of 
[https://developers.google.com/training/certification/associate-android-developer/](https://developers.google.com/training/certification/associate-android-developer/). 
(If you continue to scroll down through this README, you'll see a checklist of all those tasks, which I'm checking off 
one by one as I implement them in the app.)

It's also kind of a way of coming full circle for me -- I had done a [prework Todo app as part of my application for the CodePath Android for Engineers bootcamp](https://github.com/tachyonlabs/CodePath-Android-Bootcamp-Prework), so doing another Todo app as a way to use/practice all the skills that will be tested in the exam seems very appealing to me. 

BTW, my cat-themed productivity app Kitty Todo is a descendant of this project, you can check it out at [https://play.google.com/store/apps/details?id=com.tachyonlabs.kittytodo](https://play.google.com/store/apps/details?id=com.tachyonlabs.kittytodo) and [http://kittytodo.com/](http://kittytodo.com/).

## Screenshots

![TodoList Activity](https://github.com/tachyonlabs/Todo-App-to-practice-for-the-Google-Associate-Android-Developer-Certification-Exam/blob/master/TodoListActivity.png "TodoList Activity") &nbsp; &nbsp; ![Settings Activity](https://github.com/tachyonlabs/Todo-App-to-practice-for-the-Google-Associate-Android-Developer-Certification-Exam/blob/master/SettingsActivity.png "Settings Activity") &nbsp; &nbsp; ![Adding a new task](https://github.com/tachyonlabs/Todo-App-to-practice-for-the-Google-Associate-Android-Developer-Certification-Exam/blob/master/adding-a-new-task.png "Adding a new task")

![App Widget on home screen](https://github.com/tachyonlabs/Todo-App-to-practice-for-the-Google-Associate-Android-Developer-Certification-Exam/blob/master/app-widget.png "App Widget on home screen") &nbsp; &nbsp; ![Editing an existing task - landscape layout](https://github.com/tachyonlabs/Todo-App-to-practice-for-the-Google-Associate-Android-Developer-Certification-Exam/blob/master/editing-an-existing-task-landscape.png "Editing an existing task - landscape layout")

## "Exam Content" Checklist

On one hand, the list below seems to me like a huge number of tasks to accomplish within 24 hours, but on the other hand, in the exam itself you'll be working with an already partially-done project rather than creating everything from scratch, so, well, once I actually take the exam I'll see how it goes! The checklist is working out well for showing me things that I definitely could use more work on implementing completely by myself, and things I’m not familiar with at all, and then when I’m done I’ll have examples of all of them there in one app.

Again, the sections and individual tasks below are all from the "Exam Content" tab of [https://developers.google.com/training/certification/associate-android-developer/](https://developers.google.com/training/certification/associate-android-developer/). 

**_Any comments I've added to the list below are in bold italic like this._**

### Testing and debugging

Writing tests to verify that the application's logic and user interface are performing as expected, and executing those tests using the developer tools. Candidates should be able to analyze application crashes, and find common bugs such as layout errors and memory leaks. This includes working with the debuggers to step through application code and verify expected behavior.

* [x] Write and execute a local JVM unit test
* [ ] Write and execute a device UI test
* [x] Use the system log to output debug information

**_(As I understand it, the way the following four tasks work in the exam is that you will be given a list of bugs to fix rather than needing to hunt for bugs yourself. So I can't quite replicate that here, but I certainly found, replicated, and fixed bugs in these categories while doing this app. :-))_**

* [x] Given a problem description, replicate the failure
* [x] Debug and fix an application crash (uncaught exception)
* [x] Debug and fix an activity lifecycle issue
* [x] Debug and fix an issue binding data to views

### Application user interface (UI) and user experience (UX)

Implementation of the visual and navigational components of an application's design. This includes constructing layouts—using both XML and Java code—that consist of the standard framework UI elements as well as custom views. Candidates should have a working knowledge of using view styles and theme attributes to apply a consistent look and feel across an entire application. Understanding of how to include features that expand the application's audience through accessibility and localization may also be required.

* [ ] Mock up the main screens and navigation flow of the application
* [ ] Describe interactions between UI, background task, and data persistence
* [x] Construct a layout using XML or Java code
* [x] Create a custom view class and add it to a layout
* [x] Implement a custom application theme
* [x] Apply a custom style to a group of common widgets
* [x] Define a RecyclerView item list
* [x] Bind local data to a RecyclerView list
* [x] Implement menu-based or drawer navigation
* [x] Localize the application's UI text into one other language
* [x] Apply content descriptions to views for accessibility
* [x] Add accessibility hooks to a custom view

### Fundamental application components

Understanding of Android's top-level application components (Activity, Service, Broadcast Receiver, Content Provider) and the lifecycle associated with each one. Candidates should be able to describe the types of application logic that would be best suited for each component, and whether that component is executing in the foreground or in the background. This includes strategies for determining how and when to execute background work.

* [ ] Describe an application's key functional and nonfunctional requirements
* [x] Create an Activity that displays a layout resource
* [x] Fetch local data from disk using a Loader on a background thread
* [x] Propagate data changes through a Loader to the UI
* [ ] Schedule a time-sensitive task using alarms
* [ ] Schedule a background task using JobScheduler
* [ ] Execute a background task inside of a Service
* [x] Implement non-standard task stack navigation (deep links)

**_(Re the following, I've done this in other apps, but haven't come up with a reason to use one in this app yet.)_**

* [ ] Integrate code from an external support library

### Persistent data storage

Determining appropriate use cases for local persisted data, and designing solutions to implement data storage using files, preferences, and databases. This includes implementing strategies for bundling static data with applications, caching data from remote sources, and managing user-generated private data. Candidates should also be able to describe platform features that allow applications to store data securely and share that data with other applications in.

* [x] Define a database schema; include tables, fields, and indices
* [x] Create an application-private database file
* [ ] Construct database queries returning single results
* [x] Construct database queries returning multiple results
* [x] Insert new items into a database
* [x] Update or delete existing items in a database
* [x] Expose a database to other applications via Content Provider
* [ ] Read and parse raw resources or asset files
* [x] Create persistent preference data from user input
* [x] Toggle application logic based on preference values

### Enhanced system integration

Extending applications to integrate with interfaces outside the core application experience through notifications and app widgets. This includes displaying information to the user through these elements and keeping that information up to date. Candidates should also understand how to provide proper navigation from these external interfaces into the application's main task, including appropriate handling of deep links.

* [x] Create an app widget that displays on the device home screen
* [x] Implement a task to update the app widget periodically
* [x] Create and display a notification to the user
    
## Notes

I'm learning Spanish, but my Spanish is not that great, so I used Google Translate to do the Spanish UI localization strings.

It's more involved to do a widget with a ListView as opposed to one that just has, say TextViews and ImageViews in it. Many thanks to [https://www.sitepoint.com/killer-way-to-show-a-list-of-items-in-android-collection-widget/](https://www.sitepoint.com/killer-way-to-show-a-list-of-items-in-android-collection-widget/).
