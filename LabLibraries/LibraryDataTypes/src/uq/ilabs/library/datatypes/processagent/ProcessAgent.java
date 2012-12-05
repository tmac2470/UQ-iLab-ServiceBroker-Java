/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uq.ilabs.library.datatypes.processagent;

/**
 *
 * @author uqlpayne
 */
public class ProcessAgent {

    /**
     * A meaningful human readable name for the service
     */
    protected String agentName;
    /**
     * The globally unique identifier for the service, this may not be modified
     */
    protected String agentGuid;
    /**
     * Process agent type
     */
    protected ProcessAgentTypes agentType;
    /**
     * The fully qualified web service URL
     */
    protected String serviceUrl;
    /**
     * The fully qualified web client URL
     */
    protected String clientUrl;
    /**
     * The domain server Guid, may be null during WebService Calls.
     */
    protected String domainGuid;

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public String getAgentGuid() {
        return agentGuid;
    }

    public void setAgentGuid(String agentGuid) {
        this.agentGuid = agentGuid;
    }

    public ProcessAgentTypes getAgentType() {
        return agentType;
    }

    public void setAgentType(ProcessAgentTypes agentType) {
        this.agentType = agentType;
    }

    public String getServiceUrl() {
        return serviceUrl;
    }

    public void setServiceUrl(String serviceUrl) {
        this.serviceUrl = serviceUrl;
    }

    public String getClientUrl() {
        return clientUrl;
    }

    public void setClientUrl(String clientUrl) {
        this.clientUrl = clientUrl;
    }

    public String getDomainGuid() {
        return domainGuid;
    }

    public void setDomainGuid(String domainGuid) {
        this.domainGuid = domainGuid;
    }

    /**
     *
     */
    public ProcessAgent() {
    }

    /**
     *
     * @param agentName
     * @param agentGuid
     * @param agentType
     * @param serviceUrl
     * @param clientUrl
     */
    public ProcessAgent(String agentName, String agentGuid, ProcessAgentTypes agentType, String serviceUrl, String clientUrl) {
        this.agentName = agentName;
        this.agentGuid = agentGuid;
        this.agentType = agentType;
        this.serviceUrl = serviceUrl;
        this.clientUrl = clientUrl;
    }

    @Override
    public boolean equals(Object obj) {
        boolean isEqual = false;

        if (obj instanceof ProcessAgent) {
            ProcessAgent processAgent = (ProcessAgent) obj;
            isEqual = this.agentGuid != null && this.agentGuid.equalsIgnoreCase(processAgent.getAgentGuid())
                    && this.serviceUrl != null && this.serviceUrl.equalsIgnoreCase(processAgent.getServiceUrl());
        }

        return isEqual;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + (this.agentGuid != null ? this.agentGuid.hashCode() : 0);
        hash = 53 * hash + (this.serviceUrl != null ? this.serviceUrl.hashCode() : 0);
        return hash;
    }
}
