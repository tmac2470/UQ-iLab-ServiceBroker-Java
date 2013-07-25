/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uq.ilabs.library.processagent.database.types;

import java.util.Calendar;
import uq.ilabs.library.datatypes.processagent.ProcessAgent;
import uq.ilabs.library.datatypes.processagent.ProcessAgentTypes;
import uq.ilabs.library.datatypes.ticketing.Coupon;

/**
 *
 * @author uqlpayne
 */
public class ProcessAgentInfo extends ProcessAgent {

    private int agentId;
    private boolean self;
    private boolean retired;
    /**
     * The coupon for outgoing messages from this local service to the specified process agent
     */
    private Coupon outCoupon;
    /**
     * The coupon for incoming messages from the specified process agent to this local service
     */
    private Coupon inCoupon;
    private String issuerGuid;
    private SystemSupportInfo systemSupportInfo;
    private Calendar dateCreated;
    private Calendar dateModified;

    public int getAgentId() {
        return agentId;
    }

    public void setAgentId(int agentId) {
        this.agentId = agentId;
    }

    public boolean isSelf() {
        return self;
    }

    public void setSelf(boolean self) {
        this.self = self;
    }

    public boolean isRetired() {
        return retired;
    }

    public void setRetired(boolean retired) {
        this.retired = retired;
    }

    public Coupon getOutCoupon() {
        return outCoupon;
    }

    public void setOutCoupon(Coupon outCoupon) {
        this.outCoupon = outCoupon;
    }

    public Coupon getInCoupon() {
        return inCoupon;
    }

    public void setInCoupon(Coupon inCoupon) {
        this.inCoupon = inCoupon;
    }

    public String getIssuerGuid() {
        return issuerGuid;
    }

    public void setIssuerGuid(String issuerGuid) {
        this.issuerGuid = issuerGuid;
    }

    public SystemSupportInfo getSystemSupportInfo() {
        return systemSupportInfo;
    }

    public void setSystemSupportInfo(SystemSupportInfo systemSupportInfo) {
        this.systemSupportInfo = systemSupportInfo;
    }

    public Calendar getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Calendar dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Calendar getDateModified() {
        return dateModified;
    }

    public void setDateModified(Calendar dateModified) {
        this.dateModified = dateModified;
    }

    public ProcessAgent getProcessAgent() {
        return new ProcessAgent(this.agentName, this.agentGuid, this.agentType, this.serviceUrl, this.clientUrl, this.domainGuid);
    }

    public void setProcessAgent(ProcessAgent processAgent) {
        if (processAgent != null) {
            this.agentName = processAgent.getAgentName();
            this.agentGuid = processAgent.getAgentGuid();
            this.agentType = processAgent.getAgentType();
            this.serviceUrl = processAgent.getServiceUrl();
            this.clientUrl = processAgent.getClientUrl();
            this.domainGuid = processAgent.getDomainGuid();
        }
    }

    public ProcessAgentInfo() {
        this.agentId = -1;
        this.systemSupportInfo = new SystemSupportInfo();
    }

    public ProcessAgentInfo(String agentName, String agentGuid, ProcessAgentTypes agentType, String serviceUrl, String clientUrl, String domainGuid) {
        super(agentName, agentGuid, agentType, serviceUrl, clientUrl, domainGuid);
        this.agentId = -1;
        this.outCoupon = new Coupon();
        this.inCoupon = new Coupon();
        this.systemSupportInfo = new SystemSupportInfo();
    }
}
