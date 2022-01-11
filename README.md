# health-data-java

This is a simple app which provides the logic for database CRUD actions. The idea for the app came around to store the
health related data, specially the regular tests and checkups (and other tests) at a centralized repository. Over the
years, such data have been scattered in various web portals of different service providers, so this app will act as a
centralized database if there is a need to look up the history of test results.

This app forms the service layer to provide the CRUD functionality, and the endpoints will be used by a front-end
application to view or manipulate data.

Obviously, and unfortunately, the data need to be manually entered to the system.

This app is a scaled up version of `health-data-java-simple` app found
here: https://github.com/bibekaryal86/health-data-java-simple. The two apps do the same thing, but are different in how they
are implemented.

This app uses Spring Boot with JPA framework to do the exact same function as `health-data-java-simple`. However, the
other `simple` app does not use any kind of Java or JDBC frameworks.

Because of absence of any frameworks, the footprint of that app is very grounded (~4 MB jar archive and ~100 MB runtime
JVM memory) as opposed to when using Spring Boot (~45 MB archive and ~350 MB memory). And, as a result, that app can be
deployed and continuously run 24/7 on Google Cloud Platform App Engine's free tier.

The app is one of the two repos used to save-retrieve-display data:

* https://github.com/bibekaryal86/health-data-java (save/retrieve data) (this)
* https://github.com/bibekaryal86/health-data-spa (view data)

To run the app, we need to supply the following environment variables:

* Port
    * This is optional, and if it is not provided port defaults to 8080
* IBM DB2 Cloud Database Details for REST API
    * DB2_USR: database username
    * DB2_PWD: database password
    * DB2_HST: db2 cloud host
    * DB2_DEP: db2 database port
* Authentication header for simple app security
    * BASIC_AUTH_USR: some username
    * BASIC_AUTH_PWD: some password

This app is not deployed to GCP or other cloud providers because of the high RAM/CPU requirements that are not available
in the free tier. However, the `health-data-java-simple` app, with exact same functionality, has been deployed to GCP:

* https://healthdatajava.appspot.com/health-data/tests/ping
