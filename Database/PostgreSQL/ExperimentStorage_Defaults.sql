/*
 * (Username, FirstName, LastName, ContactEmail, UserGroup, Password)  Default password is ilab
 */
SELECT Users_Add ('manager', 'ExperimentStorage', 'Manager', 'manager@your.email.domain', 'Manager', '3759F4FF14D8494DF3B58671FF9251A9D0C41D54');

/*
 * ProcessAgents_Add(AgentName, AgentGuid, AgentType, ServiceUrl, ClientUrl,
 *   IsSelf, DomainGuid, IssuerGuid, InCouponId, OutCouponId, ContactName, ContactEmail, BugReportEmail, Location, InfoUrl, Description)
 */
SELECT ProcessAgents_Add('ExperimentStorage', '', 'ESS', 'http://localhost:8080/ExperimentStorage/ExperimentStorageService', 'http://localhost:8080/ExperimentStorage',
    TRUE, NULL, NULL, -1, -1, 'iLab Services Manager', 'experimentstorage@your.email.domain', NULL, NULL, NULL, NULL
);
