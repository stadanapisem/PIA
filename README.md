# A conference system web application
The project is a web application for managing a conference programme and communication between conference participants.
The system supports four types of users: guests, regular users, moderators and administrators. Following is a brief description of the functionalities for each type of user.

### Guest
A guest can only search the conference list by name and start/end dates. If the guest wishes to participate in a conference, he has to register first.

### Regular user
A regular user after login into the system is displayed with the conferences he participates in, chat, and the conference search form. In addition to searching by name, start/end date, a user can also search by conference area and place where the conference is taking place. After a search, a user can sign up for a conference, which requires a password. In the list of the conferences the user participates, for each entry, he can access the following: full conference agenda (all sessions, lectures and workshops happening throughout the particular conference), his agenda (all sessions and workshops the user registered for), list of all registered participants with their profiles and conference gallery.

### Moderator
A moderator is a regular user who also has some additional privileges in conferences he is in charge of. A moderator defines the conference programme and schedule. He can also add photographs to the conference gallery.

### Administrator
An administrator is a type of user with more privileges. Only an administrator can add a new conference and determine its moderators. An administrator can also delete conferences. 

### Dependencies
The project has been done using Java Server Faces framework and Primefaces library with emphasis on using ajax components. The front end has been done using bootstrap. Communication with the MySQL database has been done using the Hibernate ORM framework.
