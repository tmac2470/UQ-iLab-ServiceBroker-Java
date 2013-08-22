/********************************************************************************************************************
*/

DROP TABLE IF EXISTS Experiments;

CREATE TABLE Experiments
(
    ExperimentId bigserial NOT NULL,
    StatusCode varchar(20) NOT NULL,
    BatchStatusCode varchar(16) NOT NULL,
    CouponId bigint NOT NULL,
    UserId integer NOT NULL,
    GroupId integer NOT NULL,
    AgentId integer NOT NULL,
    ClientId integer NOT NULL,
    EssId integer NOT NULL,
    RecordCount integer DEFAULT 0,
    DateToStart timestamp,
    Duration bigint,
    DateCreated timestamp DEFAULT current_timestamp,
    DateClosed timestamp,
    Annotation text,

    PRIMARY KEY(ExperimentId)
);

/********************************************************************************************************************
*/

DROP TABLE IF EXISTS Groups;

CREATE TABLE Groups
(
    Id serial NOT NULL,
    Name varchar(64) NOT NULL,
    Type varchar(32) NOT NULL,
    IsRequest boolean DEFAULT false,
    Description text NOT NULL,
    DateCreated timestamp DEFAULT current_timestamp,

    PRIMARY KEY(Id),
    UNIQUE(Name, Type, IsRequest)
);

/********************************************************************************************************************
*/

DROP TABLE IF EXISTS GroupsHierachy;

CREATE TABLE GroupsHierachy
(
    Id serial NOT NULL,
    GroupId integer,
    ParentId integer,

    PRIMARY KEY(Id),
    UNIQUE (GroupId, ParentId)
);

/********************************************************************************************************************
*/

DROP TABLE IF EXISTS IssuedCoupons;

CREATE TABLE IssuedCoupons
(
    CouponId bigserial NOT NULL,
    Passkey varchar(50) NOT NULL,
    Cancelled boolean DEFAULT false,

    PRIMARY KEY (CouponId)
);

/********************************************************************************************************************
*/

DROP TABLE IF EXISTS IssuedTickets;

CREATE TABLE IssuedTickets
(
    TicketId bigserial NOT NULL,
    TicketType integer NOT NULL,
    CouponId bigint NOT NULL,
    RedeemerGuid varchar(50) NOT NULL,
    SponsorGuid varchar(50) NOT NULL,
    DateCreated timestamp DEFAULT current_timestamp,
    Duration bigint NOT NULL,
    Cancelled boolean DEFAULT false,
    Payload text,

    PRIMARY KEY (TicketId),
    UNIQUE (TicketType, CouponId, RedeemerGuid)
);

/********************************************************************************************************************
*/

DROP TABLE IF EXISTS LabClients;

CREATE TABLE LabClients
(
    Id serial NOT NULL,
    Name varchar(64) NOT NULL,
    Guid varchar(50) NOT NULL,
    Type varchar(32) NOT NULL,
    Title varchar(64) NOT NULL,
    Version varchar(32) NOT NULL,
    Description text NOT NULL,
    LoaderScript varchar(1024) NOT NULL,
    AgentId integer DEFAULT 0,
    EssId integer DEFAULT 0,
    UssId integer DEFAULT 0,
    IsReentrant boolean DEFAULT false,
    ContactName varchar(128),
    ContactEmail varchar(128),
    DocumentationUrl varchar(256),
    Notes text,
    DateCreated timestamp DEFAULT current_timestamp,
    DateModified timestamp,

    PRIMARY KEY(Id),
    UNIQUE (Name)
);

/********************************************************************************************************************
*/

DROP TABLE IF EXISTS LabClientGroups;

CREATE TABLE LabClientGroups
(
    Id serial NOT NULL,
    LabClientId integer,
    GroupId integer,

    PRIMARY KEY(Id),
    UNIQUE (LabClientId, GroupId)
);

/********************************************************************************************************************
*/

DROP TABLE IF EXISTS Users;

CREATE TABLE Users
(
    UserId serial NOT NULL,
    Username varchar(32) NOT NULL,
    FirstName varchar(64),
    LastName varchar(64),
    ContactEmail varchar(128),
    Affiliation varchar(64),
    AuthorityId integer,
    Password varchar(40) NOT NULL,
    AccountLocked boolean DEFAULT FALSE,
    RegisterReason text,
    DateCreated timestamp DEFAULT current_timestamp,
    DateModified timestamp,

    PRIMARY KEY(UserId),
    UNIQUE(Username)
);

/********************************************************************************************************************
*/

DROP TABLE IF EXISTS UserGroups;

CREATE TABLE UserGroups
(
    Id serial NOT NULL,
    UserId integer,
    GroupId integer,

    PRIMARY KEY(Id),
    UNIQUE (UserId, GroupId)
);
