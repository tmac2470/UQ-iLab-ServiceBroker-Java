/********************************************************************************************************************
*/

DROP TABLE IF EXISTS ProcessAgents;

CREATE TABLE ProcessAgents
(
    AgentId serial NOT NULL,
    AgentName varchar(64) NOT NULL,
    AgentGuid varchar(50) NOT NULL,
    AgentType varchar(8) NOT NULL,
    ServiceUrl varchar(256) NOT NULL,
    ClientUrl varchar(256),
    IsSelf boolean DEFAULT false,
    IsRetired boolean DEFAULT false,
    DomainGuid varchar(50),
    IssuerGuid varchar(50),
    InCouponId bigint DEFAULT -1,
    OutCouponId bigint DEFAULT -1,
    ContactName varchar(128),
    ContactEmail varchar(128),
    BugReportEmail varchar(128),
    Location varchar(256),
    InfoUrl varchar(256),
    Description text,
    DateCreated timestamp DEFAULT current_timestamp,
    DateModified timestamp,

    PRIMARY KEY (AgentId),
    UNIQUE (AgentName),
    UNIQUE (AgentGuid)
);

/********************************************************************************************************************
*/

DROP TABLE IF EXISTS Coupons;

CREATE TABLE Coupons
(
    CouponId bigint NOT NULL,
    IssuerGuid varchar(50) NOT NULL,
    Passkey varchar(50) NOT NULL,
    Cancelled boolean DEFAULT false,

    PRIMARY KEY (CouponId),
    UNIQUE (CouponId, IssuerGuid)
);

/********************************************************************************************************************
*/

DROP TABLE IF EXISTS Tickets;

CREATE TABLE Tickets
(
    TicketId bigint NOT NULL,
    TicketType integer NOT NULL,
    CouponId bigint NOT NULL,
    IssuerGuid varchar(50),
    RedeemerGuid varchar(50) NOT NULL,
    SponsorGuid varchar(50) NOT NULL,
    DateCreated timestamp NOT NULL,
    Duration bigint NOT NULL,
    Cancelled boolean DEFAULT false,
    Payload text,

    PRIMARY KEY (TicketId),
    UNIQUE (TicketId, IssuerGuid),
    UNIQUE (TicketType, CouponId, IssuerGuid, RedeemerGuid)
);
