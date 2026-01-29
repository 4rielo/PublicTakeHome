# Public Take-Home Exercise
Please create a local Git repository and commit your work as you go. Send it back to us once you are done. 

## Let's get started
For this exercise we'd like you to create a simple **to-do list** app.

It should be possible to:
- Have an overview of all tasks
- Add a new task (title and description with a 50 and 200 character limit respectively)
- Mark a task as completed
- Delete a task

When the user attempts to add a task, the app should simulate making a network call. Please add a random delay between 0.5 and 2.5 seconds before continuing. After the delay, the app should simulate that a network call can fail. It should have a success rate of 75%. Please add what you'd consider graceful error handling.

It's up to you to decide how you want to implement the user interface, navigation framework, state management, data storage, etc.

You're welcome to expand upon the scope of this exercise if you want to show off your skills / creativity.

Good luck & please feel free to reach out with any questions!


## IMPLEMENTATION DETAILS:

# Overview of Task app details: 

* Requested basic functionality completed
---
* Light/Dark theming
* Adaptive design: Orientation responsiveness with grid columns count depending on device and layout (phone, tablet, desktop, portrait or landscape)
* Multi Language configuration (english / spanish at the moment), depending on device's language config.
* DeviceFirst repository implementation, and sync with remote
* Search bar to filter by either Title or Description
* Pinned/Unpinned tasks lists
* "wiggle" animations for tasks while selecting them for pinning/deletion
* Tasks can be edited/updated
* Pull to refresh tasks list
* Completed tasks still shown as grayed-out, can be marked as not completed again.
* SnackBar messages for error reporting to the user
* Staggered list of tasks
* Scrolling task list with button to go back up

# Stack employed:

* GitHub with GitFlow for version control 
* Room database
* Ktor/okhttp for REST API calls
* Koin for dependency injection
* Error handling with Result.Success / Result.Error
* Compose for UI
* Animations for a better user experience / components transition
* Compose Navigation
* Side effects
* Coroutines
* Flow / StateFlow / Channels for reactive development.
* Material design
* IME action on TextFields
* Unit test for syncing algorithm

## Design considerations:

# Clean Architecture / Onion layering and SOLID principles

* MVI design pattern

* application: Main Application and dependency injection framework
* domain: Business basics, with interfaces for dependency inversion (Objects depend on abstractions)
* data: Data layer, with datasources, database, repositories, mappers, networking. 
* ui: Presentation layer, with main App file, and packages for each section, like navigation, theming, utils, or each screen, like main and create_task

ui/presentation -> domain <- data

