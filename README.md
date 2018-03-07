#Ad Auction Dashboard - Group 33

The Ad Auction Dashboard is an application that will be used for easier evaluation of custom made campaigns.
The project is currently under development and it only includes functionality that we included in our **Sprint One** Backlog.

##Getting Started
The instructions below will guide you through the steps for running the code.

###Prerequisites
Make sure you have ``postgres`` installed. If not follow these steps:
* Go to [PostgreSQL website](https://www.postgresql.org/download/) and install the latest version available for your system.
    * If you have ``apt`` on your system you can run 
    ```bash
    sudo apt install postgresql
    ```
    
* After installation, set up a *username* and *password* for your account with the correct permissions:
    * Open a terminal as the system user **postgres** and use the command:
    ``` bash
    psql postgres
    ```
    to run postgres as admin.
    * To create user, use:
    ```SQL
    CREATE ROLE <DB_USER> WITH LOGIN PASSWORD <DB_PASSWORD>;
    ```
    * To give your user database creation permissions, use:
    ``` SQL
    ALTER ROLE <username> CREATEDB;
    ```
    * These *username* and *password* have to be added to the corresponding fields in the ``config.properties`` file in the SEG directory, following the setup:
    ```
    DB_HOST = jdbc:postgresql://localhost:5432/DB_NAME
    DB_USER = DB_USER
    DB_PASSWORD = DB_PASSWORD
    ```
* Quit postgres and run:
    ``` bash
    psql  -U <DB_USER>
    ```
* Once you enter your password run the command below to create the database for importing:
````SQL
CREATE DATABASE <DB_NAME>;
````
* By default the postgres server is listening on port 5432, so DB_HOST in the above mentioned ``config.properties`` file should be “jdbc:postgresql://localhost:5432/DBNAME”, where DBNAME is the name you’ve selected for the database.

* To run the file:
    * Build from source by running: ``mvn install``
    * Execute the *.jar* file by running: ``java -jar SEG.jar``

##Built With
* [TravisCI](http://www.dropwizard.io/1.0.2/docs/) - Distributed Continuous Integration Service
* [Maven](https://maven.apache.org/) - Dependency Management

