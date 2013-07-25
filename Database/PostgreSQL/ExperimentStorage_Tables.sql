/********************************************************************************************************************
*/

DROP TABLE IF EXISTS Experiments;

CREATE TABLE Experiments
(
    Id bigserial NOT NULL,
    ExperimentId bigint NOT NULL,
    IssuerGuid varchar(50) NOT NULL,
    StatusCode varchar(20) NOT NULL,
    BatchStatusCode varchar(16) NOT NULL,
    SequenceNum integer DEFAULT 0,
    CouponId bigint DEFAULT 0,
    DateCreated timestamp DEFAULT current_timestamp,
    DateModified timestamp,
    DateScheduledClose timestamp,
    DateClosed timestamp,

    PRIMARY KEY(Id)
);

/********************************************************************************************************************
*/

DROP TABLE IF EXISTS ExperimentRecords;

CREATE TABLE ExperimentRecords
(
    Id bigserial NOT NULL,
    ExperimentId bigint NOT NULL,
    Submitter varchar(50) NOT NULL,
    RecordType varchar(256) NOT NULL,
    XmlSearchable boolean NOT NULL,
    SequenceNum integer DEFAULT 0,
    DateCreated timestamp DEFAULT current_timestamp,
    Contents text,

    PRIMARY KEY(Id)
);

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

    PRIMARY KEY(Username)
);
