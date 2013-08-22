/*
 * ProcessAgents_Add(AgentName, AgentGuid, AgentType, ServiceUrl, ClientUrl,
 *   IsSelf, DomainGuid, IssuerGuid, InCouponId, OutCouponId, ContactName, ContactEmail, BugReportEmail, Location, InfoUrl, Description)
 */
SELECT ProcessAgents_Add('Template JLS', '2CD01113C51C4ca997B059531CD9469D', 'BLS', 'http://localhost:8080/TemplateLabServer/LabServerWebService', NULL,
    FALSE, NULL, NULL, -1, (SELECT issuedcoupons_add('fd3cf16cc855484fb06801379f475837')), NULL, NULL, NULL, NULL, NULL, NULL
);

/*
 * LabClients_Add(Name, Guid, Type, Title, Version, Description,
 *   LoaderScript,
 *   ContactName, ContactEmail, DocumentationUrl, Notes)
 */
SELECT LabClients_Add('Template JLC', '', 'BatchRedirect', 'Template', '4.1', 'JavaServer Faces version of the Template LabClient',
    'http://localhost:8080/TemplateLabClient/LabClient.do?serviceUrl=http://localhost:8080/iLabServiceBroker/iLabServiceBrokerService&labServerId=2CD01113C51C4ca997B059531CD9469D',
    NULL, NULL, NULL, NULL
);
UPDATE LabClients SET (AgentId, EssId)
    = ((SELECT AgentId FROM ProcessAgents WHERE AgentName = 'Template JLS'), (SELECT AgentId FROM ProcessAgents WHERE AgentName = 'ExperimentStorage'))
    WHERE Name = 'Template JLC';
SELECT LabClientGroups_Add(
    (SELECT Id FROM LabClients WHERE Name = 'Template JLC'),
    (SELECT Id FROM Groups WHERE Name = 'Experiments' AND Type = 'Regular' AND IsRequest = FALSE)
);
