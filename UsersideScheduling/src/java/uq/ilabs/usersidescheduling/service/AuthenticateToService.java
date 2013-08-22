/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uq.ilabs.usersidescheduling.service;

import java.io.ByteArrayOutputStream;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import javax.ejb.EJB;
import javax.servlet.ServletContext;
import javax.xml.namespace.QName;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFactory;
import javax.xml.soap.SOAPFault;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.MessageContext.Scope;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import javax.xml.ws.soap.SOAPFaultException;
import uq.ilabs.library.datatypes.service.AgentAuthHeader;
import uq.ilabs.library.datatypes.service.AuthenticationHeader;
import uq.ilabs.library.datatypes.service.InitAuthHeader;
import uq.ilabs.library.datatypes.service.OperationAuthHeader;
import uq.ilabs.library.datatypes.ticketing.Coupon;
import uq.ilabs.library.lab.utilities.Logfile;
import uq.ilabs.usersidescheduling.UsersideSchedulingAppBean;

/**
 *
 * @author uqlpayne
 */
public class AuthenticateToService implements SOAPHandler<SOAPMessageContext> {

    //<editor-fold defaultstate="collapsed" desc="Variables">
    @EJB
    private UsersideSchedulingAppBean usersideSchedulingBean;
    //</editor-fold>

    @Override
    public boolean handleMessage(SOAPMessageContext messageContext) {
        /*
         * Assume this will fail
         */
        boolean success = false;

        /*
         * Process the header info for an inbound message if authentication is required
         */
        if ((Boolean) messageContext.get(SOAPMessageContext.MESSAGE_OUTBOUND_PROPERTY) == false) {
            try {
                /*
                 * Start the service if not done already
                 */
                this.usersideSchedulingBean.StartService((ServletContext) messageContext.get(MessageContext.SERVLET_CONTEXT));

                /*
                 * Write the SOAP message to system output
                 */
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                messageContext.getMessage().writeTo(outputStream);
//                System.out.println(AuthenticateToService.class.getSimpleName());
//                System.out.println(outputStream.toString());

                /*
                 * Process the SOAP header to get the authentication information
                 */
                ProcessSoapHeader(messageContext);

                success = true;
            } catch (Exception ex) {
                /*
                 * Create a SOAPFaultException to be thrown back to the caller
                 */
                try {
                    SOAPFault fault = SOAPFactory.newInstance().createFault();
                    fault.setFaultString(ex.getMessage());
                    throw new SOAPFaultException(fault);
                } catch (SOAPException e) {
                    Logfile.WriteError(e.getMessage());
                }
            }
        }

        return success;
    }

    @Override
    public Set<QName> getHeaders() {
        return Collections.emptySet();
    }

    @Override
    public boolean handleFault(SOAPMessageContext messageContext) {
        return true;
    }

    @Override
    public void close(MessageContext context) {
    }

    /**
     *
     * @param messageContext
     * @throws SOAPException
     */
    private void ProcessSoapHeader(SOAPMessageContext messageContext) throws SOAPException {
        /*
         * Get the SOAP header
         */
        SOAPMessage soapMessage = messageContext.getMessage();
        SOAPEnvelope soapEnvelope = soapMessage.getSOAPPart().getEnvelope();
        SOAPHeader soapHeader = soapEnvelope.getHeader();

        /*
         * Scan through the header's child elements looking for the authentication header
         */
        Iterator iterator = soapHeader.getChildElements();
        while (iterator.hasNext()) {
            /*
             * Get the next child looking for a SOAPElement type
             */
            Object object = iterator.next();
            if (object instanceof SOAPElement) {
                /*
                 * Get the SOAPElement looking for the authentication header
                 */
                SOAPElement soapElement = (SOAPElement) object;
                Name elementName = soapElement.getElementName();
                String localName = elementName.getLocalName();

                /*
                 * Process the authentication header and pass to the web service through the
                 * message context. The scope has to be changed from HANDLER to APPLICATION so
                 * that the web service can see the message context map
                 */
                if (localName.equalsIgnoreCase(QnameFactory.getAgentAuthHeaderLocalPart()) == true) {
                    /*
                     * AgentAuthHeader
                     */
                    AgentAuthHeader agentAuthHeader = ProcessSoapElementAgentAuthHeader(soapElement);
                    String agentAuthHeaderName = AgentAuthHeader.class.getSimpleName();
                    messageContext.put(agentAuthHeaderName, agentAuthHeader);
                    messageContext.setScope(agentAuthHeaderName, Scope.APPLICATION);

                }
                if (localName.equalsIgnoreCase(QnameFactory.getInitAuthHeaderLocalPart()) == true) {
                    /*
                     * InitAuthHeader
                     */
                    InitAuthHeader initAuthHeader = ProcessSoapElementInitAuthHeader(soapElement);
                    String initAuthHeaderName = InitAuthHeader.class.getSimpleName();
                    messageContext.put(initAuthHeaderName, initAuthHeader);
                    messageContext.setScope(initAuthHeaderName, Scope.APPLICATION);

                }
                if (localName.equalsIgnoreCase(QnameFactory.getOperationAuthHeaderLocalPart()) == true) {
                    /*
                     * OperationAuthHeader
                     */
                    OperationAuthHeader operationAuthHeader = ProcessSoapElementOperationAuthHeader(soapElement);
                    String operationAuthHeaderName = OperationAuthHeader.class.getSimpleName();
                    messageContext.put(operationAuthHeaderName, operationAuthHeader);
                    messageContext.setScope(operationAuthHeaderName, Scope.APPLICATION);
                }
            }
        }
    }

    /**
     *
     * @param soapElement
     * @return AgentAuthHeader
     */
    private AgentAuthHeader ProcessSoapElementAgentAuthHeader(SOAPElement soapElement) {
        AgentAuthHeader agentAuthHeader = new AgentAuthHeader();
        Iterator iterator = soapElement.getChildElements();
        while (iterator.hasNext()) {
            /*
             * Get the next child looking for a SOAPElement type
             */
            Object object = iterator.next();
            if (object instanceof SOAPElement) {
                /*
                 * Get the SOAPElement looking for the authentication information
                 */
                SOAPElement element = (SOAPElement) object;
                Name elementName = element.getElementName();
                String localName = elementName.getLocalName();

                /*
                 * Check if localName matches a specified string
                 */
                if (localName.equalsIgnoreCase(AgentAuthHeader.STR_AgentGuid) == true) {
                    agentAuthHeader.setAgentGuid(element.getValue());
                } else if (localName.equalsIgnoreCase(AuthenticationHeader.STR_Coupon) == true) {
                    Coupon coupon = ProcessSoapElementCoupon(element);
                    agentAuthHeader.setCoupon(coupon);
                }
            }
        }

        return agentAuthHeader;
    }

    /**
     *
     * @param soapElement
     * @return InitAuthHeader
     */
    private InitAuthHeader ProcessSoapElementInitAuthHeader(SOAPElement soapElement) {
        InitAuthHeader initAuthHeader = new InitAuthHeader();
        Iterator iterator = soapElement.getChildElements();
        while (iterator.hasNext()) {
            /*
             * Get the next child looking for a SOAPElement type
             */
            Object object = iterator.next();
            if (object instanceof SOAPElement) {
                /*
                 * Get the SOAPElement looking for the authentication information
                 */
                SOAPElement element = (SOAPElement) object;
                Name elementName = element.getElementName();
                String localName = elementName.getLocalName();

                /*
                 * Check if localName matches a specified string
                 */
                if (localName.equalsIgnoreCase(InitAuthHeader.STR_InitPasskey) == true) {
                    initAuthHeader.setInitPasskey(element.getValue());
                }
            }
        }

        return initAuthHeader;
    }

    /**
     *
     * @param soapElement
     * @return OperationAuthHeader
     */
    private OperationAuthHeader ProcessSoapElementOperationAuthHeader(SOAPElement soapElement) {
        OperationAuthHeader operationAuthHeader = new OperationAuthHeader();
        Iterator iterator = soapElement.getChildElements();
        while (iterator.hasNext()) {
            /*
             * Get the next child looking for a SOAPElement type
             */
            Object object = iterator.next();
            if (object instanceof SOAPElement) {
                /*
                 * Get the SOAPElement looking for the authentication information
                 */
                SOAPElement element = (SOAPElement) object;
                Name elementName = element.getElementName();
                String localName = elementName.getLocalName();

                /*
                 * Check if localName matches a specified string
                 */
                if (localName.equalsIgnoreCase(AuthenticationHeader.STR_Coupon) == true) {
                    Coupon coupon = ProcessSoapElementCoupon(element);
                    operationAuthHeader.setCoupon(coupon);
                }
            }
        }

        return operationAuthHeader;
    }

    /**
     *
     * @param soapElement
     * @return Coupon
     */
    private Coupon ProcessSoapElementCoupon(SOAPElement soapElement) {
        Coupon coupon = new Coupon();
        Iterator iterator = soapElement.getChildElements();
        while (iterator.hasNext()) {
            /*
             * Get the next child looking for a SOAPElement type
             */
            Object object = iterator.next();
            if (object instanceof SOAPElement) {
                /*
                 * Get the SOAPElement looking for the authentication information
                 */
                SOAPElement element = (SOAPElement) object;
                Name elementName = element.getElementName();
                String localName = elementName.getLocalName();

                /*
                 * Check if localName matches a specified string
                 */
                if (localName.equalsIgnoreCase(AuthenticationHeader.STR_CouponId) == true) {
                    coupon.setCouponId(Long.parseLong(element.getValue()));
                } else if (localName.equalsIgnoreCase(AuthenticationHeader.STR_IssuerGuid) == true) {
                    coupon.setIssuerGuid(element.getValue());
                } else if (localName.equalsIgnoreCase(AuthenticationHeader.STR_Passkey) == true) {
                    coupon.setPasskey(element.getValue());
                }
            }
        }

        return coupon;
    }
}
