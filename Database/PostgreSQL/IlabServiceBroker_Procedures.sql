/********************************************************************************************************************
*/

DROP FUNCTION IF EXISTS Experiments_Add
(
    varchar, varchar, bigint, integer, integer,
    integer, integer, integer, timestamp, bigint
);

CREATE FUNCTION Experiments_Add
(
    StatusCode varchar,
    BatchStatusCode varchar,
    CouponId bigint,
    UserId integer,
    GroupId integer,

    AgentId integer,
    ClientId integer,
    EssId integer,
    DateToStart timestamp,
    Duration bigint
)
RETURNS bigint AS
$BODY$
    INSERT INTO Experiments (
        StatusCode, BatchStatusCode, CouponId, UserId, GroupId,
        AgentId, ClientId, EssId, DateToStart, Duration,
        DateCreated
    )
    VALUES (
        $1, $2, $3, $4, $5, $6, $7, $8, $9, $10, current_timestamp
    )
    RETURNING ExperimentId;
$BODY$
    LANGUAGE sql VOLATILE;

/********************************************************************************************************************
*/

DROP FUNCTION IF EXISTS Experiments_Delete
(
    bigint
);

CREATE FUNCTION Experiments_Delete
(
    ExperimentId bigint
)
RETURNS bigint AS
$BODY$
    DELETE FROM Experiments
    WHERE ExperimentId = $1
    RETURNING ExperimentId;
$BODY$
  LANGUAGE sql VOLATILE;

/********************************************************************************************************************
*/

DROP FUNCTION IF EXISTS Experiments_RetrieveBy
(
    varchar, bigint, varchar
);

CREATE FUNCTION Experiments_RetrieveBy
(
    ColumnName varchar,
    LongValue bigint,
    StrValue varchar
)
RETURNS TABLE
(
    ExperimentId bigint,
    StatusCode varchar,
    BatchStatusCode varchar,
    CouponId bigint,
    UserId integer,
    GroupId integer,
    AgentId integer,
    ClientId integer,
    EssId integer,
    RecordCount integer,
    DateToStart timestamp,
    Duration bigint,
    DateCreated timestamp,
    DateClosed timestamp,
    Annotation text
) AS
$BODY$
    SELECT * FROM Experiments
    WHERE
        CASE
            WHEN $1 IS NULL THEN
                TRUE
            WHEN lower($1) = 'experimentid' THEN
                ExperimentId = $2
            WHEN lower($1) = 'userid' THEN
                UserId = $2
        END
    ORDER BY ExperimentId
$BODY$
    LANGUAGE sql VOLATILE;

/********************************************************************************************************************
*/

DROP FUNCTION IF EXISTS Experiments_UpdateAnnotation
(
    bigint, text
);

CREATE FUNCTION Experiments_UpdateAnnotation
(
    ExperimentId bigint,
    Annotation text
)
RETURNS bigint AS
$BODY$
    UPDATE Experiments SET (
        Annotation
    ) = (
        $2
    )
    WHERE ExperimentId = $1
    RETURNING ExperimentId;
$BODY$
    LANGUAGE sql VOLATILE;

/********************************************************************************************************************
*/

DROP FUNCTION IF EXISTS Experiments_UpdateStatus
(
    bigint, varchar, varchar
);

CREATE FUNCTION Experiments_UpdateStatus
(
    ExperimentId bigint,
    StatusCode varchar,
    BatchStatusCode varchar
)
RETURNS bigint AS
$BODY$
    UPDATE Experiments SET (
        StatusCode, BatchStatusCode
    ) = (
        $2, $3
    )
    WHERE ExperimentId = $1
    RETURNING ExperimentId;
$BODY$
    LANGUAGE sql VOLATILE;

/********************************************************************************************************************
*/

DROP FUNCTION IF EXISTS Experiments_UpdateStatusClose
(
    bigint, varchar, varchar, integer
);

CREATE FUNCTION Experiments_UpdateStatusClose
(
    ExperimentId bigint,
    StatusCode varchar,
    BatchStatusCode varchar,
    RecordCount integer
)
RETURNS bigint AS
$BODY$
    UPDATE Experiments SET (
        StatusCode, BatchStatusCode, RecordCount, DateClosed
    ) = (
        $2, $3, $4, current_timestamp
    )
    WHERE ExperimentId = $1
    RETURNING ExperimentId;
$BODY$
    LANGUAGE sql VOLATILE;

/********************************************************************************************************************
*/

DROP FUNCTION IF EXISTS IssuedCoupons_Add
(
    varchar
);

CREATE FUNCTION IssuedCoupons_Add
(
    Passkey varchar
)
RETURNS bigint AS
$BODY$
    INSERT INTO IssuedCoupons (
        Passkey
    )
    VALUES (
        $1
    )
    RETURNING CouponId;
$BODY$
    LANGUAGE sql VOLATILE;

/********************************************************************************************************************
*/

DROP FUNCTION IF EXISTS IssuedCoupons_Cancel
(
    bigint
);

CREATE FUNCTION IssuedCoupons_Cancel
(
    CouponId bigint
)
RETURNS bigint AS
$BODY$
    UPDATE IssuedCoupons SET (
        Cancelled
    ) = (
        true
    )
    WHERE CouponId = $1
    RETURNING CouponId;
$BODY$
    LANGUAGE sql VOLATILE;

/********************************************************************************************************************
*/

DROP FUNCTION IF EXISTS IssuedCoupons_Delete
(
    bigint
);

CREATE FUNCTION IssuedCoupons_Delete
(
    CouponId bigint
)
RETURNS bigint AS
$BODY$
    DELETE FROM IssuedCoupons
    WHERE CouponId = $1
    RETURNING CouponId;
$BODY$
  LANGUAGE sql VOLATILE;

/********************************************************************************************************************
*/

DROP FUNCTION IF EXISTS IssuedCoupons_RetrieveBy
(
    varchar, bigint, varchar
);

CREATE FUNCTION IssuedCoupons_RetrieveBy
(
    ColumnName varchar,
    LongValue bigint,
    StrValue varchar
)
RETURNS TABLE
(
    CouponId bigint,
    Passkey varchar,
    Cancelled boolean
) AS
$BODY$
    SELECT * FROM IssuedCoupons
    WHERE
        CASE
            WHEN $1 IS NULL THEN
                TRUE
            WHEN lower($1) = 'couponid' THEN
                CouponId = $2
            WHEN lower($1) = 'passkey' THEN
                Passkey = $3
        END
    ORDER BY CouponId ASC
$BODY$
    LANGUAGE sql VOLATILE;

/********************************************************************************************************************
*/

DROP FUNCTION IF EXISTS IssuedCoupons_Update
(
    bigint, varchar
);

CREATE FUNCTION IssuedCoupons_Update
(
    CouponId bigint,
    Passkey varchar
)
RETURNS bigint AS
$BODY$
    UPDATE IssuedCoupons SET (
        Passkey
    ) = (
        $2
    )
    WHERE CouponId = $1
    RETURNING CouponId;
$BODY$
    LANGUAGE sql VOLATILE;

/********************************************************************************************************************
*/

DROP FUNCTION IF EXISTS IssuedTickets_Add
(
    integer, bigint, varchar, varchar, bigint,
    text
);

CREATE FUNCTION IssuedTickets_Add
(
    TicketType integer,
    CouponId bigint,
    RedeemerGuid varchar,
    SponsorGuid varchar,
    Duration bigint,

    Payload text
)
RETURNS bigint AS
$BODY$
    INSERT INTO IssuedTickets (
        TicketType, CouponId, RedeemerGuid, SponsorGuid, Duration,
        Payload, DateCreated
    )
    VALUES (
        $1, $2, $3, $4, $5,
        $6, current_timestamp
    )
    RETURNING TicketId;
$BODY$
    LANGUAGE sql VOLATILE;

/********************************************************************************************************************
*/

DROP FUNCTION IF EXISTS IssuedTickets_Cancel
(
    bigint
);

CREATE FUNCTION IssuedTickets_Cancel
(
    TicketId bigint
)
RETURNS bigint AS
$BODY$
    UPDATE IssuedTickets SET (
        Cancelled
    ) = (
        true
    )
    WHERE TicketId = $1
    RETURNING TicketId;
$BODY$
    LANGUAGE sql VOLATILE;

/********************************************************************************************************************
*/

DROP FUNCTION IF EXISTS IssuedTickets_Delete
(
    bigint
);

CREATE FUNCTION IssuedTickets_Delete
(
    TicketId bigint
)
RETURNS bigint AS
$BODY$
    DELETE FROM IssuedTickets
    WHERE TicketId = $1
    RETURNING TicketId;
$BODY$
  LANGUAGE sql VOLATILE;

/********************************************************************************************************************
*/

DROP FUNCTION IF EXISTS IssuedTickets_GetListExpired
(
);

CREATE FUNCTION IssuedTickets_GetListExpired
(
)
RETURNS TABLE
(
    TicketId bigint
) AS
$BODY$
    SELECT TicketId FROM IssuedTickets
    WHERE (Cancelled = true) or (DateCreated + Duration * interval '1 second') < now()
    ORDER BY TicketId ASC
$BODY$
    LANGUAGE sql VOLATILE;

/********************************************************************************************************************
*/

DROP FUNCTION IF EXISTS IssuedTickets_RetrieveBy
(
    varchar, bigint, integer, varchar
);

CREATE FUNCTION IssuedTickets_RetrieveBy
(
    ColumnName varchar,
    LongValue bigint,
    IntValue integer,
    StrValue varchar
)
RETURNS TABLE
(
    TicketId bigint,
    TicketType integer,
    CouponId bigint,
    RedeemerGuid varchar,
    SponsorGuid varchar,
    DateCreated timestamp,
    Duration bigint,
    Cancelled boolean,
    Payload text
) AS
$BODY$
    SELECT * FROM IssuedTickets
    WHERE
        CASE
            WHEN $1 IS NULL THEN
                TRUE
            WHEN lower($1) = 'ticketid' THEN
                TicketId = $2
            WHEN lower($1) = 'tickettype_couponid_redeemerguid' THEN
                TicketType = $3 AND CouponId = $2 AND RedeemerGuid = $4
        END
    ORDER BY TicketId ASC
$BODY$
    LANGUAGE sql VOLATILE;

/********************************************************************************************************************
*/

DROP FUNCTION IF EXISTS LabClients_Add
(
    varchar, varchar, varchar, varchar, varchar,
    text, varchar, varchar, varchar, varchar,
    text
);

CREATE FUNCTION LabClients_Add
(
    Name varchar,
    Guid varchar,
    Type varchar,
    Title varchar,
    Version varchar,

    Description text,
    LoaderScript varchar,
    ContactName varchar,
    ContactEmail varchar,
    DocumentationUrl varchar,

    Notes text
)
RETURNS integer AS
$BODY$
    INSERT INTO LabClients (
        Name, Guid, Type, Title, Version,
        Description, LoaderScript, ContactName, ContactEmail, DocumentationUrl,
        Notes
    )
    VALUES (
        $1, $2, $3, $4, $5, $6, $7, $8, $9, $10, $11
    )
    RETURNING Id;
$BODY$
    LANGUAGE sql VOLATILE;

/********************************************************************************************************************
*/

DROP FUNCTION IF EXISTS LabClients_Delete
(
    integer
);

CREATE FUNCTION LabClients_Delete
(
    Id integer
)
RETURNS integer AS
$BODY$
    DELETE FROM LabClients
    WHERE Id = $1
    RETURNING Id;
$BODY$
  LANGUAGE sql VOLATILE;

/********************************************************************************************************************
*/

DROP FUNCTION IF EXISTS LabClients_GetListIds
(
);

CREATE FUNCTION LabClients_GetListIds
(
)
RETURNS TABLE
(
    Id integer
) AS
$BODY$
    SELECT Id FROM LabClients
    ORDER BY Id ASC
$BODY$
    LANGUAGE sql VOLATILE;

/********************************************************************************************************************
*/

DROP FUNCTION IF EXISTS LabClients_GetListNames
(
);

CREATE FUNCTION LabClients_GetListNames
(
)
RETURNS TABLE
(
    Name varchar
) AS
$BODY$
    SELECT Name FROM LabClients
    ORDER BY Name ASC
$BODY$
    LANGUAGE sql VOLATILE;

/********************************************************************************************************************
*/

DROP FUNCTION IF EXISTS LabClients_RetrieveBy
(
    varchar, integer, varchar
);

CREATE FUNCTION LabClients_RetrieveBy
(
    ColumnName varchar,
    IntValue integer,
    StrValue varchar
)
RETURNS TABLE
(
    Id integer,
    Name varchar,
    Guid varchar,
    Type varchar,
    Title varchar,
    Version varchar,
    Description text,
    LoaderScript varchar,
    AgentId integer,
    EssId integer,
    UssId integer,
    IsReentrant boolean,
    ContactName varchar,
    ContactEmail varchar,
    DocumentationUrl varchar,
    Notes text,
    DateCreated timestamp,
    DateModified timestamp
) AS
$BODY$
    SELECT * FROM LabClients
    WHERE
        CASE
            WHEN lower($1) = 'id' THEN
                Id = $2
            WHEN lower($1) = 'name' THEN
                Name = $3
        END
    ORDER BY Id ASC
$BODY$
    LANGUAGE sql VOLATILE;

/********************************************************************************************************************
*/

DROP FUNCTION IF EXISTS LabClients_Update
(
    integer,
    varchar, varchar, varchar, varchar, text,
    varchar, integer, integer, integer, boolean,
    varchar, varchar, varchar, text
);

CREATE FUNCTION LabClients_Update
(
    Id integer,

    Name varchar,
    Type varchar,
    Title varchar,
    Version varchar,
    Description text,

    LoaderScript varchar,
    AgentId integer,
    EssId integer,
    UssId integer,
    IsReentrant boolean,

    ContactName varchar,
    ContactEmail varchar,
    DocumentationUrl varchar,
    Notes text
)
RETURNS integer AS
$BODY$
    UPDATE LabClients SET (
        Name, Type, Title, Version, Description,
        LoaderScript, AgentId, EssId, UssId, IsReentrant,
        ContactName, ContactEmail, DocumentationUrl, Notes, DateModified
    ) = (
        $2, $3, $4, $5, $6,
        $7, $8, $9, $10, $11,
        $12, $13, $14, $15, current_timestamp
    )
    WHERE Id = $1
    RETURNING Id;
$BODY$
    LANGUAGE sql VOLATILE;

/********************************************************************************************************************
*/

DROP FUNCTION IF EXISTS Users_Add
(
    varchar, varchar, varchar, varchar, varchar,
    integer, varchar, text
);

CREATE FUNCTION Users_Add
(
    Username varchar,
    FirstName varchar,
    LastName varchar,
    ContactEmail varchar,
    Affiliation varchar,

    AuthorityId integer,
    Password varchar,
    RegisterReason text
)
RETURNS integer AS
$BODY$
    INSERT INTO Users (
        Username,
        FirstName,
        LastName,
        ContactEmail,
        Affiliation,

        AuthorityId,
        Password,
        RegisterReason
    )
    VALUES (
        $1, $2, $3, $4, $5, $6, $7, $8
    )
    RETURNING UserId;
$BODY$
    LANGUAGE sql VOLATILE;

/********************************************************************************************************************
*/

DROP FUNCTION IF EXISTS Users_Delete
(
    integer
);

CREATE FUNCTION Users_Delete
(
    UserId integer
)
RETURNS integer AS
$BODY$
    DELETE FROM Users
    WHERE UserId = $1
    RETURNING UserId;
$BODY$
  LANGUAGE sql VOLATILE;

/********************************************************************************************************************
*/

DROP FUNCTION IF EXISTS Users_GetList
(
    varchar, varchar
);

CREATE FUNCTION Users_GetList
(
    ColumnName varchar,
    StrValue varchar
)
RETURNS TABLE
(
    Username varchar
) AS
$BODY$
    SELECT Username FROM Users
    WHERE
        CASE
            WHEN lower($1) = 'username' THEN
                TRUE
            WHEN lower($1) = 'affiliation' THEN
                Affiliation = $2
        END
    ORDER BY Username ASC
$BODY$
    LANGUAGE sql VOLATILE;

/********************************************************************************************************************
*/

DROP FUNCTION IF EXISTS Users_GetRecordCount
(
);

CREATE FUNCTION Users_GetRecordCount
(
)
RETURNS bigint AS
$BODY$
    SELECT COUNT(*) FROM Users
$BODY$
    LANGUAGE sql VOLATILE;

/********************************************************************************************************************
*/

DROP FUNCTION IF EXISTS Users_RetrieveBy
(
    varchar, integer, varchar
);

CREATE FUNCTION Users_RetrieveBy
(
    ColumnName varchar,
    IntValue integer,
    StrValue varchar
)
RETURNS TABLE
(
    UserId integer,
    Username varchar,
    FirstName varchar,
    LastName varchar,
    ContactEmail varchar,
    Affiliation varchar,
    AuthorityId int,
    Password varchar,
    AccountLocked boolean,
    RegisterReason text,
    DateCreated timestamp,
    DateModified timestamp
) AS
$BODY$
    SELECT * FROM Users
    WHERE
        CASE
            WHEN lower($1) = 'userid' THEN
                UserId = $2
            WHEN lower($1) = 'username' THEN
                Username = $3
            WHEN lower($1) = 'affiliation' THEN
                Affiliation = $3
        END
    ORDER BY UserId ASC
$BODY$
    LANGUAGE sql VOLATILE;

/********************************************************************************************************************
*/

DROP FUNCTION IF EXISTS Users_Update
(
    integer, varchar, varchar, varchar, varchar,
    varchar, boolean
);

CREATE FUNCTION Users_Update
(
    UserId integer,
    FirstName varchar,
    LastName varchar,
    ContactEmail varchar,
    Affiliation varchar,

    Password varchar,
    AccountLocked boolean
)
RETURNS integer AS
$BODY$
    UPDATE Users SET (
        FirstName, LastName, ContactEmail, Affiliation, Password,
        AccountLocked, DateModified
    ) = (
        $2, $3, $4, $5, $6, $7, current_timestamp
    )
    WHERE UserId = $1
    RETURNING UserId;
$BODY$
    LANGUAGE sql VOLATILE;

/********************************************************************************************************************
*/

DROP FUNCTION IF EXISTS UserGroups_Add
(
    integer, integer
);

CREATE FUNCTION UserGroups_Add
(
    UserId integer,
    GroupId integer
)
RETURNS integer AS
$BODY$
    INSERT INTO UserGroups (
        UserId,
        GroupId
    )
    VALUES (
        $1, $2
    )
    RETURNING Id;
$BODY$
    LANGUAGE sql VOLATILE;

/********************************************************************************************************************
*/

DROP FUNCTION IF EXISTS UserGroups_Delete
(
    integer
);

CREATE FUNCTION UserGroups_Delete
(
    Id integer
)
RETURNS integer AS
$BODY$
    DELETE FROM UserGroups
    WHERE Id = $1
    RETURNING Id;
$BODY$
  LANGUAGE sql VOLATILE;

/********************************************************************************************************************
*/

DROP FUNCTION IF EXISTS UserGroups_RetrieveBy
(
    varchar, integer, varchar
);

CREATE FUNCTION UserGroups_RetrieveBy
(
    ColumnName varchar,
    IntValue integer,
    StrValue varchar
)
RETURNS TABLE
(
    Id integer,
    UserId integer,
    GroupId integer
) AS
$BODY$
    SELECT * FROM UserGroups
    WHERE
        CASE
            WHEN $1 IS NULL THEN
                TRUE
            WHEN lower($1) = 'userid' THEN
                UserId = $2
            WHEN lower($1) = 'groupid' THEN
                GroupId = $2
        END
    ORDER BY Id ASC
$BODY$
    LANGUAGE sql VOLATILE;

/********************************************************************************************************************
*/

DROP FUNCTION IF EXISTS Groups_Add
(
    varchar, varchar, boolean, text
);

CREATE FUNCTION Groups_Add
(
    Name varchar,
    Type varchar,
    IsRequest boolean,
    Description text
)
RETURNS integer AS
$BODY$
    INSERT INTO Groups (
        Name, Type, IsRequest, Description
    )
    VALUES (
        $1, $2, $3, $4
    )
    RETURNING Id;
$BODY$
    LANGUAGE sql VOLATILE;

/********************************************************************************************************************
*/

DROP FUNCTION IF EXISTS Groups_Delete
(
    integer
);

CREATE FUNCTION Groups_Delete
(
    Id integer
)
RETURNS integer AS
$BODY$
    DELETE FROM Groups
    WHERE Id = $1
    RETURNING Id;
$BODY$
  LANGUAGE sql VOLATILE;

/********************************************************************************************************************
*/

DROP FUNCTION IF EXISTS Groups_GetList
(
    varchar, varchar
);

CREATE FUNCTION Groups_GetList
(
    ColumnName varchar,
    StrValue varchar
)
RETURNS TABLE
(
    Name varchar
) AS
$BODY$
    SELECT Name FROM Groups
    WHERE
        CASE
            WHEN $1 IS NULL THEN
                TRUE
            WHEN lower($1) = 'type' THEN
                Type = $2
            WHEN lower($1) = 'isrequest' THEN
                IsRequest = true
        END
    ORDER BY Name ASC
$BODY$
    LANGUAGE sql VOLATILE;

/********************************************************************************************************************
*/

DROP FUNCTION IF EXISTS Groups_RetrieveBy
(
    varchar, integer, varchar
);

CREATE FUNCTION Groups_RetrieveBy
(
    ColumnName varchar,
    IntValue integer,
    StrValue varchar
)
RETURNS TABLE
(
    Id integer,
    Name varchar,
    Type varchar,
    IsRequest boolean,
    Description text,
    DateCreated timestamp
) AS
$BODY$
    SELECT * FROM Groups
    WHERE
        CASE
            WHEN $1 IS NULL THEN
                TRUE
            WHEN lower($1) = 'id' THEN
                Id = $2
            WHEN lower($1) = 'name' THEN
                Name = $3
            WHEN lower($1) = 'type' THEN
                Type = $3
            WHEN lower($1) = 'isrequest' THEN
                IsRequest = true
        END
    ORDER BY Id ASC
$BODY$
    LANGUAGE sql VOLATILE;

/********************************************************************************************************************
*/

DROP FUNCTION IF EXISTS Groups_Update
(
    integer, varchar, varchar, boolean, text
);

CREATE FUNCTION Groups_Update
(
    Id integer,
    Name varchar,
    Type varchar,
    IsRequest boolean,
    Description text
)
RETURNS integer AS
$BODY$
    UPDATE Groups SET (
        Name, Type, IsRequest, Description
    ) = (
        $2, $3, $4, $5
    )
    WHERE Id = $1
    RETURNING Id;
$BODY$
    LANGUAGE sql VOLATILE;

/********************************************************************************************************************
*/

DROP FUNCTION IF EXISTS GroupsHierachy_Add
(
    integer, integer
);

CREATE FUNCTION GroupsHierachy_Add
(
    GroupId integer,
    ParentId integer
)
RETURNS integer AS
$BODY$
    INSERT INTO GroupsHierachy (
        GroupId,
        ParentId
    )
    VALUES (
        $1, $2
    )
    RETURNING Id;
$BODY$
    LANGUAGE sql VOLATILE;

/********************************************************************************************************************
*/

DROP FUNCTION IF EXISTS GroupsHierachy_Delete
(
    integer
);

CREATE FUNCTION GroupsHierachy_Delete
(
    Id integer
)
RETURNS integer AS
$BODY$
    DELETE FROM GroupsHierachy
    WHERE Id = $1
    RETURNING Id;
$BODY$
  LANGUAGE sql VOLATILE;

/********************************************************************************************************************
*/

DROP FUNCTION IF EXISTS GroupsHierachy_RetrieveBy
(
    varchar, integer, varchar
);

CREATE FUNCTION GroupsHierachy_RetrieveBy
(
    ColumnName varchar,
    IntValue integer
)
RETURNS TABLE
(
    Id integer,
    GroupId integer,
    ParentId integer
) AS
$BODY$
    SELECT * FROM GroupsHierachy
    WHERE
        CASE
            WHEN $1 IS NULL THEN
                TRUE
            WHEN lower($1) = 'groupid' THEN
                GroupId = $2
            WHEN lower($1) = 'parentid' THEN
                ParentId = $2
        END
    ORDER BY Id ASC
$BODY$
    LANGUAGE sql VOLATILE;

/********************************************************************************************************************
*/

DROP FUNCTION IF EXISTS LabClientGroups_Add
(
    integer, integer
);

CREATE FUNCTION LabClientGroups_Add
(
    LabClientId integer,
    GroupId integer
)
RETURNS integer AS
$BODY$
    INSERT INTO LabClientGroups (
        LabClientId,
        GroupId
    )
    VALUES (
        $1, $2
    )
    RETURNING Id;
$BODY$
    LANGUAGE sql VOLATILE;

/********************************************************************************************************************
*/

DROP FUNCTION IF EXISTS LabClientGroups_Delete
(
    integer
);

CREATE FUNCTION LabClientGroups_Delete
(
    Id integer
)
RETURNS integer AS
$BODY$
    DELETE FROM LabClientGroups
    WHERE Id = $1
    RETURNING Id;
$BODY$
  LANGUAGE sql VOLATILE;

/********************************************************************************************************************
*/

DROP FUNCTION IF EXISTS LabClientGroups_RetrieveBy
(
    varchar, integer, integer
);

CREATE FUNCTION LabClientGroups_RetrieveBy
(
    ColumnName varchar,
    IntValue integer,
    IntValue2 integer
)
RETURNS TABLE
(
    Id integer,
    LabClientId integer,
    GroupId integer
) AS
$BODY$
    SELECT * FROM LabClientGroups
    WHERE
        CASE
            WHEN $1 IS NULL THEN
                TRUE
            WHEN lower($1) = 'id' THEN
                LabClientId = $2 AND GroupId = $3
            WHEN lower($1) = 'labclientid' THEN
                LabClientId = $2
            WHEN lower($1) = 'groupid' THEN
                GroupId = $2
        END
    ORDER BY Id ASC
$BODY$
    LANGUAGE sql VOLATILE;

/********************************************************************************************************************
*/

DROP FUNCTION IF EXISTS UserGroups_Add
(
    integer, integer
);

CREATE FUNCTION UserGroups_Add
(
    UserId integer,
    GroupId integer
)
RETURNS integer AS
$BODY$
    INSERT INTO UserGroups (
        UserId,
        GroupId
    )
    VALUES (
        $1, $2
    )
    RETURNING Id;
$BODY$
    LANGUAGE sql VOLATILE;

/********************************************************************************************************************
*/

DROP FUNCTION IF EXISTS UserGroups_Delete
(
    integer
);

CREATE FUNCTION UserGroups_Delete
(
    Id integer
)
RETURNS integer AS
$BODY$
    DELETE FROM UserGroups
    WHERE Id = $1
    RETURNING Id;
$BODY$
  LANGUAGE sql VOLATILE;

/********************************************************************************************************************
*/

DROP FUNCTION IF EXISTS UserGroups_RetrieveBy
(
    varchar, integer, varchar
);

CREATE FUNCTION UserGroups_RetrieveBy
(
    ColumnName varchar,
    IntValue integer,
    StrValue varchar
)
RETURNS TABLE
(
    Id integer,
    UserId integer,
    GroupId integer
) AS
$BODY$
    SELECT * FROM UserGroups
    WHERE
        CASE
            WHEN $1 IS NULL THEN
                TRUE
            WHEN lower($1) = 'userid' THEN
                UserId = $2
            WHEN lower($1) = 'groupid' THEN
                GroupId = $2
        END
    ORDER BY Id ASC
$BODY$
    LANGUAGE sql VOLATILE;

