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

