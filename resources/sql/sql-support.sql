--------------
-- Database --
--------------


-- :name create-database!
-- :command :execute
-- :result :raw
-- :doc Create the rccst database
CREATE DATABASE rccst;


-----------
-- USERS --
-----------

-- :name create-user-table!
-- :command :execute
-- :result :raw
-- :doc Create users table
create table users (
    username        TEXT PRIMARY KEY,
    uuid            TEXT,
    pass            TEXT);


-- :name drop-users-table :!
-- :doc Drop users table if exists
drop table if exists users


-- :name create-new-user! :! :n
-- :doc Creates a new user in the database
INSERT INTO users (username, uuid, pass)
VALUES (:username, :uuid, :pass);


-- :name get-user :? :1
-- :doc retrieves a user record given the id
SELECT * FROM users
WHERE username = :username;


-- :name verify-credentials :? :1
-- :doc Take in username and password and verify they are in db
SELECT * FROM users
WHERE (username = :username AND
       pass = :pass);


-- :name get-users :? :*
-- :doc retrieves a user record given the id
SELECT * FROM users;


-- :name delete-user! :! :n
-- :doc deletes a user record given the id
DELETE FROM users
WHERE username = :username;
