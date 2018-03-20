# Training Diary

JDBC project for TDT4145 Data Modelling, Databases and Database Management Systems

By Ask Sommervoll, Claus Martinsen, Henrik Fauskanger and Nora Bodin

## About

For this small project we implemented a [Active Domain Object][ado] pattern for database
access, providing a simple programmatic interface for retrieving and operating on
the data.

## Classes

### Persistence

Our Active Domain Object classes represent the entities we care about in the database
and provides the necessary methods for interacting with them.

[Active Domain Object class diagram][persistence diagram]

### UI

We use JavaFX for our GUI and have one Controller class for each screen in the
application. They use our Active Domain Objects to interact with the database. 

<!--- !![UI diagram][ui diagram] --->

[ado]: http://www.diranieh.com/DataAccessPatterns/ActiveDomainObject.htm
[persistence diagram]: docs/persistence_classes.png "Persistence class diagram"
<!--- [ui diagram]: docs/ui_classes.png "UI class diagram" --->
