/********************************************************************************************************************
*/

DROP FUNCTION IF EXISTS ProcessAgents_Add
(
    varchar, varchar, varchar, varchar, varchar,
    boolean, varchar, varchar, bigint, bigint,
    varchar, varchar, varchar, varchar, varchar,
    text
);

CREATE FUNCTION ProcessAgents_Add
(
    AgentName varchar,
    AgentGuid varchar,
    AgentType varchar,
    ServiceUrl varchar,
    ClientUrl varchar,

    IsSelf boolean,
    DomainGuid varchar,
    IssuerGuid varchar,
    InCouponId bigint,
    OutCouponId bigint,

    ContactName varchar,
    ContactEmail varchar,
    BugReportEmail varchar,
    Location varchar,
    InfoUrl varchar,

    Description text
)
RETURNS integer AS
$BODY$
    INSERT INTO ProcessAgents (
        AgentName, AgentGuid, AgentType, ServiceUrl, ClientUrl,
        IsSelf, DomainGuid, IssuerGuid, InCouponId, OutCouponId,
        ContactName, ContactEmail, BugReportEmail, Location, InfoUrl,
        Description
    )
    VALUES (
        $1, $2, $3, $4, $5,
        $6, $7, $8, $9, $10,
        $11, $12, $13, $14, $15,
        $16
    )
    RETURNING AgentId;
$BODY$
    LANGUAGE sql VOLATILE;

/********************************************************************************************************************
*/

DROP FUNCTION IF EXISTS ProcessAgents_Delete
(
    integer
);

CREATE FUNCTION ProcessAgents_Delete
(
    AgentId integer
)
RETURNS integer AS
$BODY$
    DELETE FROM ProcessAgents
    WHERE AgentId = $1
    RETURNING AgentId;
$BODY$
  LANGUAGE sql VOLATILE;

/********************************************************************************************************************
*/

DROP FUNCTION IF EXISTS ProcessAgents_GetList
(
    varchar, varchar
);

CREATE FUNCTION ProcessAgents_GetList
(
    ColumnName varchar,
    StrValue varchar
)
RETURNS TABLE
(
    AgentName varchar
) AS
$BODY$
    SELECT AgentName FROM ProcessAgents
    WHERE
        CASE
            WHEN $1 IS NULL THEN
                TRUE
            WHEN lower($1) = 'agentname' THEN
                AgentName = $2
            WHEN lower($1) = 'agenttype' THEN
                AgentType = $2
        END
    ORDER BY AgentName ASC
$BODY$
    LANGUAGE sql VOLATILE;

/********************************************************************************************************************
*/

DROP FUNCTION IF EXISTS ProcessAgents_RetrieveBy
(
    varchar, integer, varchar
);

CREATE FUNCTION ProcessAgents_RetrieveBy
(
    ColumnName varchar,
    IntValue integer,
    StrValue varchar
)
RETURNS TABLE
(
    AgentId integer,
    AgentName varchar,
    AgentGuid varchar,
    AgentType varchar,
    ServiceUrl varchar,
    ClientUrl varchar,
    IsSelf boolean,
    IsRetired boolean,
    DomainGuid varchar,
    IssuerGuid varchar,
    InCouponId bigint,
    OutCouponId bigint,
    ContactName varchar,
    ContactEmail varchar,
    BugReportEmail varchar,
    Location varchar,
    InfoUrl varchar,
    Description text,
    DateCreated timestamp,
    DateModified timestamp
) AS
$BODY$
    SELECT * FROM ProcessAgents
    WHERE
        CASE
            WHEN $1 IS NULL THEN
                TRUE
            WHEN lower($1) = 'isself' THEN
                IsSelf = true
            WHEN lower($1) = 'agentid' THEN
                AgentId = $2
            WHEN lower($1) = 'agentname' THEN
                AgentName = $3
            WHEN lower($1) = 'agentguid' THEN
                AgentGuid = $3
        END
    ORDER BY AgentId ASC
$BODY$
    LANGUAGE sql VOLATILE;

/********************************************************************************************************************
*/

DROP FUNCTION IF EXISTS ProcessAgents_Update
(
    integer, varchar, varchar, varchar, boolean,
    bigint, bigint, varchar
);

CREATE FUNCTION ProcessAgents_Update
(
    AgentId integer,
    ServiceUrl varchar,
    ClientUrl varchar,
    DomainGuid varchar,
    IsRetired boolean,

    OutCouponId bigint,
    InCouponId bigint,
    IssuerGuid varchar
)
RETURNS integer AS
$BODY$
    UPDATE ProcessAgents SET (
        ServiceUrl, ClientUrl, DomainGuid, IsRetired,
        OutCouponId, InCouponId, IssuerGuid, DateModified
    ) = (
        $2, $3, $4, $5,
        $6, $7, $8, current_timestamp
    )
    WHERE AgentId = $1
    RETURNING AgentId;
$BODY$
    LANGUAGE sql VOLATILE;

/********************************************************************************************************************
*/

DROP FUNCTION IF EXISTS ProcessAgents_UpdateSystemSupport
(
    varchar, varchar, varchar, varchar, varchar,
    varchar, text
);

CREATE FUNCTION ProcessAgents_UpdateSystemSupport
(
    AgentGuid varchar,
    ContactName varchar,
    ContactEmail varchar,
    BugReportEmail varchar,
    Location varchar,

    InfoUrl varchar,
    Description text
)
RETURNS integer AS
$BODY$
    UPDATE ProcessAgents SET (
        ContactName, ContactEmail, BugReportEmail, Location,
        InfoUrl, Description, DateModified
    ) = (
        $2, $3, $4, $5,
        $6, $7, current_timestamp
    )
    WHERE AgentGuid = $1
    RETURNING AgentId;
$BODY$
    LANGUAGE sql VOLATILE;

/********************************************************************************************************************
*/

DROP FUNCTION IF EXISTS Coupons_Add
(
    bigint, varchar, varchar
);

CREATE FUNCTION Coupons_Add
(
    CouponId bigint,
    IssuerGuid varchar,
    Passkey varchar
)
RETURNS bigint AS
$BODY$
    INSERT INTO Coupons (
        CouponId, IssuerGuid, Passkey
    )
    VALUES (
        $1, $2, $3
    )
    RETURNING CouponId;
$BODY$
    LANGUAGE sql VOLATILE;

/********************************************************************************************************************
*/

DROP FUNCTION IF EXISTS Coupons_Cancel
(
    varchar, bigint
);

CREATE FUNCTION Coupons_Cancel
(
    IssuerGuid varchar,
    CouponId bigint
)
RETURNS bigint AS
$BODY$
    UPDATE Coupons SET (
        Cancelled
    )
    = (true)
    WHERE IssuerGuid = $1 AND CouponId = $2
    RETURNING CouponId;
$BODY$
    LANGUAGE sql VOLATILE;

/********************************************************************************************************************
*/

DROP FUNCTION IF EXISTS Coupons_Delete
(
    bigint, varchar
);

CREATE FUNCTION Coupons_Delete
(
    CouponId bigint,
    IssuerGuid varchar
)
RETURNS bigint AS
$BODY$
    DELETE FROM Coupons
    WHERE CouponId = $1 AND IssuerGuid = $2
    RETURNING CouponId;
$BODY$
  LANGUAGE sql VOLATILE;

/********************************************************************************************************************
*/

DROP FUNCTION IF EXISTS Coupons_RetrieveBy
(
    varchar, bigint, varchar
);

CREATE FUNCTION Coupons_RetrieveBy
(
    ColumnName varchar,
    LongValue bigint,
    StrValue varchar
)
RETURNS TABLE
(
    CouponId bigint,
    IssuerGuid varchar,
    Passkey varchar,
    Cancelled boolean
) AS
$BODY$
    SELECT * FROM Coupons
    WHERE
        CASE
            WHEN $1 IS NULL THEN
                TRUE
            WHEN lower($1) = 'couponidissuerguid' THEN
                CouponId = $2 AND IssuerGuid = $3
        END
    ORDER BY CouponId ASC
$BODY$
    LANGUAGE sql VOLATILE;

/********************************************************************************************************************
*/

DROP FUNCTION IF EXISTS Tickets_Add
(
    bigint, integer, bigint, varchar, varchar,
    varchar, bigint, text
);

CREATE FUNCTION Tickets_Add
(
    TicketId bigint,
    TicketType integer,
    CouponId bigint,
    IssuerGuid varchar,
    RedeemerGuid varchar,

    SponsorGuid varchar,
    Duration bigint,
    Payload text
)
RETURNS bigint AS
$BODY$
    INSERT INTO Tickets (
        TicketId, TicketType, CouponId, IssuerGuid, RedeemerGuid,
        SponsorGuid, Duration, Payload, DateCreated
    )
    VALUES (
        $1, $2, $3, $4, $5,
        $6, $7, $8, current_timestamp
    )
    RETURNING TicketId;
$BODY$
    LANGUAGE sql VOLATILE;

/********************************************************************************************************************
*/

DROP FUNCTION IF EXISTS Tickets_Cancel
(
    bigint
);

CREATE FUNCTION Tickets_Cancel
(
    TicketId bigint
)
RETURNS bigint AS
$BODY$
    UPDATE Tickets SET (
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

DROP FUNCTION IF EXISTS Tickets_Delete
(
    bigint
);

CREATE FUNCTION Tickets_Delete
(
    TicketId bigint
)
RETURNS bigint AS
$BODY$
    DELETE FROM Tickets
    WHERE TicketId = $1
    RETURNING TicketId;
$BODY$
  LANGUAGE sql VOLATILE;


/********************************************************************************************************************
*/

DROP FUNCTION IF EXISTS Tickets_GetList
(
    varchar, bigint
);

CREATE FUNCTION Tickets_GetList
(
    ColumnName varchar,
    LongValue bigint
)
RETURNS TABLE
(
    TicketId bigint
) AS
$BODY$
    SELECT TicketId FROM Tickets
    WHERE
        CASE
            WHEN lower($1) = 'couponid' THEN
                CouponId = $2
            WHEN lower($1) = 'expired' THEN
                (Cancelled = true) OR ((Duration > 0) AND (DateCreated + Duration * interval '1 second') < now())
        END
    ORDER BY TicketId ASC
$BODY$
    LANGUAGE sql VOLATILE;

/********************************************************************************************************************
*/

DROP FUNCTION IF EXISTS Tickets_RetrieveBy
(
    varchar, bigint, varchar, integer
);

CREATE FUNCTION Tickets_RetrieveBy
(
    ColumnName varchar,
    LongValue bigint,
    StrValue varchar,
    IntValue integer
)
RETURNS TABLE
(
    TicketId bigint,
    TicketType integer,
    CouponId bigint,
    IssuerGuid varchar,
    RedeemerGuid varchar,
    SponsorGuid varchar,
    DateCreated timestamp,
    Duration bigint,
    Cancelled boolean,
    Payload text
) AS
$BODY$
    SELECT * FROM Tickets
    WHERE
        CASE
            WHEN $1 IS NULL THEN
                TRUE
            WHEN lower($1) = 'ticketid' THEN
                TicketId = $2
            WHEN lower($1) = 'couponid' THEN
                CouponId = $2 AND RedeemerGuid = $3 AND TicketType = $4
        END
    ORDER BY TicketId ASC
$BODY$
    LANGUAGE sql VOLATILE;
