/*
 * GroupId = Groups_Add(GroupName, GroupType, Description)
 * GroupsHierachy_Add(GroupId, ParentId)
 */
SELECT GroupsHierachy_Add(
    (SELECT Groups_Add('ROOT', 'BuiltIn', FALSE, 'Root Group')),
    -1
);
SELECT GroupsHierachy_Add(
    (SELECT Groups_Add('SuperUser', 'BuiltIn', FALSE, 'Administrators')),
    (SELECT Id FROM Groups WHERE Name = 'ROOT')
);
SELECT GroupsHierachy_Add(
    (SELECT Groups_Add('NewUser', 'BuiltIn', FALSE, 'New registered users who have not been moved to any group yet')),
    (SELECT Id FROM Groups WHERE Name = 'ROOT')
);
SELECT GroupsHierachy_Add(
    (SELECT Groups_Add('OrphanedUser', 'BuiltIn', FALSE, 'Users who no longer belong to any group')),
    (SELECT Id FROM Groups WHERE Name = 'ROOT')
);

SELECT GroupsHierachy_Add(
    (SELECT Groups_Add('Experiments', 'Regular', FALSE, 'Group for running experiments')),
    (SELECT Id FROM Groups WHERE Name = 'ROOT')
);

SELECT GroupsHierachy_Add(
    (SELECT Groups_Add('Experiments', 'Regular', TRUE, 'Request group for running experiments')),
    (SELECT Id FROM Groups WHERE Name = 'ROOT')
);

/*
 * UserId = Users_Add(Username, FirstName, LastName, ContactEmail, Affiliation, AuthorityId, Password, RegisterReason)  Default password is ilab
 * UserGroups_Add(UserId, GroupId)
 */
SELECT UserGroups_Add(
    (SELECT Users_Add('superuser', 'Super', 'User', 'superuser@your.email.domain', 'Other', 0, '3759F4FF14D8494DF3B58671FF9251A9D0C41D54', NULL)),
    (SELECT Id FROM Groups WHERE Name = 'SuperUser')
);
SELECT UserGroups_Add(
    (SELECT Users_Add('testuser', 'Test', 'User', 'testuser@your.email.domain', 'Guest', 0, '3759F4FF14D8494DF3B58671FF9251A9D0C41D54', NULL)),
    (SELECT Id FROM Groups WHERE Name = 'Experiments' AND Type = 'Regular' AND IsRequest = FALSE)
);

/*
 * ProcessAgents_Add(AgentName, AgentGuid, AgentType, ServiceUrl, ClientUrl,
 *   IsSelf, DomainGuid, IssuerGuid, InCouponId, OutCouponId, ContactName, ContactEmail, BugReportEmail, Location, InfoUrl, Description)
 */
SELECT ProcessAgents_Add('iLabServiceBroker', '', 'ISB', 'http://localhost:8080/iLabServiceBroker/iLabServiceBrokerService', 'http://localhost:8080/iLabServiceBroker',
    TRUE, NULL, NULL, -1, -1, 'iLab Administrator', 'ilabservicebroker@your.email.domain', NULL, NULL, NULL, NULL
);
