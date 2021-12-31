# Database

Any "real" server needs a way to store (durability) 

## Postgres

[Postgres](https://www.postgresql.org) is a "stand-alone" Relational Database Management System (RDBMS) that provide
Structured Query Language (SQL) for accessing information. Postgres is "stand-alone" in the sense that it runs as a 
separate application/executable to the RCCST application.

### What to know beforehand

In order for this to work, developers need to have Postgres installed and running, AND have
the "rccst" database created, using:

    create database rccst;

Creating tables and loading initial data is still done insice RCCST, but the "database" itself must be created _outside_ the 
application.

There is a pretty good tutorial [here](https://www.postgresqltutorial.com).


## SQLite

To make it a little easier to develop with, we are also providing a configuration for using [SQLite]() to act as the RDBMS. The
main difference to Postgres is that SQLIte is "embedded" inside the RCCST application itself. It still stores information to 
disk, but the actual processing is part of the application, not a separate application/executable.

### What to know beforehand

Since it is embedded in RCCST, SQLit is easier to work with. Everything needed to creat the database, the tables, and the initial
data is all built into the application.


## HugSQL

To allow us to use multiple SQL databases, we are leveraging the [HugSQL](https://www.hugsql.org) Clojure library.

HugSQL uses special annotations (embedded in comments) inside plain old SQL files which are pre-processed when your
application starts into Clojure calls are run-time. This lets us get the best of SQL and combine it with the best of
Clojure.
