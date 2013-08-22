/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uq.ilabs.library.datatypes.service;

import uq.ilabs.library.datatypes.ticketing.Coupon;

/**
 *
 * @author uqlpayne
 */
public class AgentAuthHeader extends AuthenticationHeader {

    public static final String STR_AgentGuid = "agentGuid";
    //
    protected String agentGuid;

    public String getAgentGuid() {
        return agentGuid;
    }

    public void setAgentGuid(String agentGuid) {
        this.agentGuid = agentGuid;
    }

    public AgentAuthHeader() {
    }

    public AgentAuthHeader(String agentGuid) {
        this.agentGuid = agentGuid;
    }

    public AgentAuthHeader(String agentGuid, Coupon coupon) {
        super(coupon);
        this.agentGuid = agentGuid;
    }
}
