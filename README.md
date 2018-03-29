# Training Diary

JDBC project for __TDT4145__ Data Modelling, Databases and Database Management Systems

By Ask Sommervoll, Claus Martinsen, Henrik Fauskanger and Nora Bodin

## About

For this small project we implemented a [Active Domain Object][ado] pattern for database 
access, providing a simple programmatic interface for retrieving and operating on 
the data.

> WARNINIG: Because of the nature of this project, norwegian and english is used interchangebly
> in the source code (the GUI is in norwegian). It's bad practice but oh well...

## How to run

1) Run the __updated__ [SQL script][sql_script] for creating the required database structure on your 
preffered MySQL server.

2) Run [`release_1.0.jar`][jar] or the [main method][main] in the source code to launch the application.

3) Click the `Instillinger` button to access database settings. Change the URL, username 
and password to match your setup.

## Classes

### Persistence

Our Active Domain Object classes represent the entities we care about in the database 
and provides the necessary methods for interacting with them.

The most important methods of all _ADOs_ is:
* `.save()`, which saves the objects state to the database.
* `.getAll()` (_static_), which fetches all objects of a certain type (rows from a certain table) from the database.
* `.getById(int id)` (_static_), which fetches a object with a certain ID from the database.

For in-depth information about how the classes work, see the respective `JavaDoc`.

![Persistence diagram][persistence diagram]

### UI

We use JavaFX for our GUI and have one Controller for each screen in the application. The controllers 
use our Active Domain Objects to interact with the database, decoupling UI from database logic.

[Page][page] is an Enum for all FXML pages. 

[TrainingApp][app] is the Application which launches the JavaFX GUI.

![UI diagram][ui diagram]

[ado]: http://www.diranieh.com/DataAccessPatterns/ActiveDomainObject.htm "Resource on Active Domain Object pattern."
[persistence diagram]: docs/persistence_classes.png "Persistence diagram"
[ui diagram]: docs/ui_classes.png "UI diagram"
[sql_script]: docs/treningsDb_v2.sql "SQL script for creating the database."
[main]: src/main/java/databaser/Main.java
[jar]: release_1.0.jar "Runnable project JAR."
[page]: src/main/java/databaser/ui/Page.java
[app]: src/main/java/databaser/ui/TrainingApp.java
