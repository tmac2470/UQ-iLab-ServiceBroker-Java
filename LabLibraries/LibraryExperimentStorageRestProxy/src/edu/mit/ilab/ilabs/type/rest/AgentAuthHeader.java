package edu.mit.ilab.ilabs.type.rest;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for AgentAuthHeader complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="AgentAuthHeader">
 *   &lt;complexContent>
 *     &lt;extension base="{http://ilab.mit.edu/iLabs/type}AuthenticationHeader">
 *       &lt;sequence>
 *         &lt;element name="agentGuid" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;anyAttribute/>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AgentAuthHeader", propOrder = {
    "agentGuid"
})
public class AgentAuthHeader
        extends AuthenticationHeader {

    protected String agentGuid;

    /**
     * Gets the value of the agentGuid property.
     *
     * @return possible object is {@link String }
     *
     */
    public String getAgentGuid() {
        return agentGuid;
    }

    /**
     * Sets the value of the agentGuid property.
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setAgentGuid(String value) {
        this.agentGuid = value;
    }
}
