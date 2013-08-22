/*
 * ProcessAgents_Add(AgentName, AgentGuid, AgentType, ServiceUrl, ClientUrl,
 *   IsSelf, DomainGuid, IssuerGuid, InCouponId, OutCouponId, ContactName, ContactEmail, BugReportEmail, Location, InfoUrl, Description)
 */
SELECT ProcessAgents_Add('Radioactivity JLS', 'FF464E3507564f28A64F156282BB912E', 'BLS', 'http://localhost:8080/RadioactivityLabServer/LabServerWebService', NULL,
    FALSE, NULL, NULL, -1, (SELECT issuedcoupons_add('fd3cf16cc855484fb06801379f475837')), NULL, NULL, NULL, NULL, NULL, NULL
);

/*
 * LabClients_Add(Name, Guid, Type, Title, Version, Description,
 *   LoaderScript,
 *   ContactName, ContactEmail, DocumentationUrl, Notes)
 */
SELECT LabClients_Add('Radioactivity JLC', '', 'BatchRedirect', 'Radioactivity', '4.1', 'JavaServer Faces version of the Radioactivity LabClient using SOAP communication',
    'http://localhost:8080/RadioactivityLabClient/LabClient.do?serviceUrl=http://localhost:8080/iLabServiceBroker/iLabServiceBrokerService&labServerId=FF464E3507564f28A64F156282BB912E',
    NULL, NULL, NULL, NULL
);
UPDATE LabClients SET (AgentId, EssId)
    = ((SELECT AgentId FROM ProcessAgents WHERE AgentName = 'Radioactivity JLS'), (SELECT AgentId FROM ProcessAgents WHERE AgentType = 'ESS'))
    WHERE Name = 'Radioactivity JLC';
SELECT LabClientGroups_Add(
    (SELECT Id FROM LabClients WHERE Name = 'Radioactivity JLC'),
    (SELECT Id FROM Groups WHERE Name = 'Experiments' AND Type = 'Regular' AND IsRequest = FALSE)
);

SELECT LabClients_Add('Radioactivity JLCR', '', 'BatchRedirect', 'Radioactivity', '4.1', 'JavaServer Faces version of the Radioactivity LabClient using REST communication',
    'http://localhost:8080/RadioactivityLabClientRest/LabClient.do?serviceUrl=http://localhost:8080/iLabServiceBroker/iLabServiceBrokerRest&labServerId=FF464E3507564f28A64F156282BB912E',
    NULL, NULL, NULL, NULL
);
UPDATE LabClients SET (AgentId, EssId)
    = ((SELECT AgentId FROM ProcessAgents WHERE AgentName = 'Radioactivity JLS'), (SELECT AgentId FROM ProcessAgents WHERE AgentName = 'ExperimentStorage'))
    WHERE Name = 'Radioactivity JLCR';
SELECT LabClientGroups_Add(
    (SELECT Id FROM LabClients WHERE Name = 'Radioactivity JLCR'),
    (SELECT Id FROM Groups WHERE Name = 'Experiments' AND Type = 'Regular' AND IsRequest = FALSE)
);
