/********************************************************************************************************************
*/

DROP FUNCTION IF EXISTS Experiments_Add
(
    bigint, varchar, varchar, varchar, timestamp
);

CREATE FUNCTION Experiments_Add
(
    ExperimentId bigint,
    IssuerGuid varchar,
    StatusCode varchar,
    BatchStatusCode varchar,
    DateScheduledClose timestamp
)
RETURNS bigint AS
$BODY$
    INSERT INTO Experiments (
        ExperimentId,
        IssuerGuid,
        StatusCode,
        BatchStatusCode,
        DateScheduledClose
    )
    VALUES (
        $1, $2, $3, $4, $5
    )
    RETURNING Id;
$BODY$
    LANGUAGE sql VOLATILE;

/********************************************************************************************************************
*/

DROP FUNCTION IF EXISTS Experiments_Delete
(
    bigint, varchar
);

CREATE FUNCTION Experiments_Delete
(
    ExperimentId bigint,
    IssuerGuid varchar
)
RETURNS bigint AS
$BODY$
    DELETE FROM Experiments
    WHERE ExperimentId = $1 AND IssuerGuid = $2
    RETURNING Id;
$BODY$
  LANGUAGE sql VOLATILE;

s/********************************************************************************************************************
*/

DROP FUNCTION IF EXISTS Experiments_RetrieveBy
(
    varchar, bigint, varchar
);

CREATE FUNCTION Experiments_RetrieveBy
(
    ColumnName varchar,
    BigIntValue bigint,
    StrValue varchar
)
RETURNS TABLE
(
    Id bigint,
    ExperimentId bigint,
    IssuerGuid varchar,
    StatusCode varchar,
    BatchStatusCode varchar,
    SequenceNum integer,
    CouponId bigint,
    DateCreated timestamp,
    DateModified timestamp,
    DateScheduledClose timestamp,
    DateClosed timestamp
) AS
$BODY$
    SELECT * FROM Experiments
    WHERE
        CASE
            WHEN lower($1) = 'experimentid' THEN
                ExperimentId = $2 AND IssuerGuid = $3
        END
$BODY$
  LANGUAGE sql VOLATILE;

/********************************************************************************************************************
*/

DROP FUNCTION IF EXISTS Experiments_Update
(
    bigint, varchar, varchar, varchar, integer, bigint, timestamp
);

CREATE FUNCTION Experiments_Update
(
    ExperimentId bigint,
    IssuerGuid varchar,
    StatusCode varchar,
    BatchStatusCode varchar,
    SequenceNum integer,
    CouponId bigint,
    DateScheduledClose timestamp
)
RETURNS bigint AS
$BODY$
    UPDATE Experiments SET (
        StatusCode, BatchStatusCode, SequenceNum, CouponId, DateScheduledClose, DateModified
    ) = (
        $3, $4, $5, $6, $7, current_timestamp
    )
    WHERE ExperimentId = $1 AND IssuerGuid = $2
    RETURNING Id;
$BODY$
    LANGUAGE sql VOLATILE;

/********************************************************************************************************************
*/

DROP FUNCTION IF EXISTS Experiments_UpdateCouponId
(
    bigint, varchar, bigint
);

CREATE FUNCTION Experiments_UpdateCouponId
(
    ExperimentId bigint,
    IssuerGuid varchar,
    CouponId bigint
)
RETURNS bigint AS
$BODY$
    UPDATE Experiments SET (
        CouponId
    ) = (
        $3
    )
    WHERE ExperimentId = $1 AND IssuerGuid = $2
    RETURNING Id;
$BODY$
    LANGUAGE sql VOLATILE;

/********************************************************************************************************************
*/

DROP FUNCTION IF EXISTS Experiments_UpdateStatus
(
    bigint, varchar, varchar, varchar
);

CREATE FUNCTION Experiments_UpdateStatus
(
    ExperimentId bigint,
    IssuerGuid varchar,
    StatusCode varchar,
    BatchStatusCode varchar
)
RETURNS bigint AS
$BODY$
    UPDATE Experiments SET (
        StatusCode, BatchStatusCode, DateClosed
    ) = (
        $3, $4, current_timestamp
    )
    WHERE ExperimentId = $1 AND IssuerGuid = $2
    RETURNING Id;
$BODY$
    LANGUAGE sql VOLATILE;

/********************************************************************************************************************
*/

DROP FUNCTION IF EXISTS ExperimentRecords_Add
(
    bigint, varchar, varchar, boolean, text
);

CREATE FUNCTION ExperimentRecords_Add
(
    ExperimentId bigint,
    Submitter varchar,
    RecordType varchar,
    XmlSearchable boolean,
    Contents text
)
RETURNS bigint AS
$BODY$
    INSERT INTO ExperimentRecords (
        ExperimentId,
        Submitter,
        RecordType,
        XmlSearchable,
        Contents,
        SequenceNum
    )
    VALUES (
        $1, $2, $3, $4, $5,
        (SELECT count(*) FROM ExperimentRecords WHERE ExperimentId = $1) + 1
    )
    RETURNING Id;
$BODY$
    LANGUAGE sql VOLATILE;

/********************************************************************************************************************
*/

DROP FUNCTION IF EXISTS ExperimentRecords_Delete
(
    bigint
);

CREATE FUNCTION ExperimentRecords_Delete
(
    Id bigint
)
RETURNS bigint AS
$BODY$
    DELETE FROM ExperimentRecords
    WHERE Id = $1
    RETURNING Id;
$BODY$
  LANGUAGE sql VOLATILE;

/********************************************************************************************************************
*/

DROP FUNCTION IF EXISTS ExperimentRecords_RetrieveBy
(
    varchar, bigint, varchar, integer
);

CREATE FUNCTION ExperimentRecords_RetrieveBy
(
    ColumnName varchar,
    BigIntValue bigint,
    StrValue varchar,
    IntValue integer
)
RETURNS TABLE
(
    Id bigint,
    ExperimentId bigint,
    Submitter varchar,
    RecordType varchar,
    XmlSearchable boolean,
    SequenceNum integer,
    DateCreated timestamp,
    Contents text
) AS
$BODY$
    SELECT * FROM ExperimentRecords
    WHERE
        CASE
            WHEN lower($1) = 'id' THEN
                Id = $2
            WHEN lower($1) = 'experimentid' THEN
                ExperimentId = $2
            WHEN lower($1) = 'sequencenum' THEN
                ExperimentId = $2 AND SequenceNum = $4
        END
    ORDER BY SequenceNum ASC
$BODY$
  LANGUAGE sql VOLATILE;

/********************************************************************************************************************
*/

DROP FUNCTION IF EXISTS Users_Add
(
    varchar, varchar, varchar, varchar, varchar,
    varchar
);

CREATE FUNCTION Users_Add
(
    Username varchar,
    FirstName varchar,
    LastName varchar,
    ContactEmail varchar,
    UserGroup varchar,

    Password varchar
)
RETURNS integer AS
$BODY$
    INSERT INTO Users (
        Username,
        FirstName,
        LastName,
        ContactEmail,
        UserGroup,

        Password
    )
    VALUES (
        $1, $2, $3, $4, $5, $6
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
            WHEN lower($1) = 'usergroup' THEN
                UserGroup = $2
        END
    ORDER BY Username ASC
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
    UserGroup varchar,
    Password varchar,
    AccountLocked boolean,
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
            WHEN lower($1) = 'usergroup' THEN
                UserGroup = $3
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
    UserGroup varchar,

    Password varchar,
    Locked boolean
)
RETURNS integer AS
$BODY$
    UPDATE Users SET (
        FirstName, LastName, ContactEmail, UserGroup, Password,
        AccountLocked, DateModified
    ) = (
        $2, $3, $4, $5, $6, $7, current_timestamp
    )
    WHERE UserId = $1
    RETURNING UserId;
$BODY$
    LANGUAGE sql VOLATILE;

