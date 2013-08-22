/********************************************************************************************************************
*/

DROP TABLE IF EXISTS Users;

CREATE TABLE Users
(
    UserId serial NOT NULL,
    Username varchar(32) NOT NULL,
    FirstName varchar(64) NOT NULL,
    LastName varchar(64) NOT NULL,
    ContactEmail varchar(128) NOT NULL,
    UserGroup varchar(32) NOT NULL,
    Password varchar(40) NOT NULL,
    AccountLocked boolean DEFAULT FALSE,
    DateCreated timestamp DEFAULT current_timestamp,
    DateModified timestamp,

    PRIMARY KEY(UserId),
    UNIQUE(Username)
);
