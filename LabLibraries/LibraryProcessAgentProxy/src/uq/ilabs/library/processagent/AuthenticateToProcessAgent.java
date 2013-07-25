/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uq.ilabs.library.processagent;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Set;
import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFactory;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPHeaderElement;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import uq.ilabs.library.lab.utilities.Logfile;
import uq.ilabs.processagent.AgentAuthHeader;
import uq.ilabs.processagent.Coupon;
import uq.ilabs.processagent.InitAuthHeader;
import uq.ilabs.processagent.ObjectFactory;

/**
 *
 * @author uqlpayne
 */
public class AuthenticateToProcessAgent implements SOAPHandler<SOAPMessageContext> {

    //<editor-fold defaultstate="collapsed" desc="Constants">
    private static final String STR_ClassName = AuthenticateToProcessAgent.class.getName();
    /*
     * String constants
     */
    private static final String STR_AgentGuid = "agentGuid";
    private static final String STR_Coupon = "coupon";
    private static final String STR_CouponIssuerGuid = "issuerGuid";
    private static final String STR_CouponCouponId = "couponId";
    private static final String STR_CouponPasskey = "passkey";
    private static final String STR_InitPasskey = "initPasskey";
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Variables">
    private static boolean initialised = false;
    private static SOAPFactory soapFactory;
    private static ObjectFactory objectFactory;
    private static QName qnameAgentAuthHeader;
    private static QName qnameInitAuthHeader;
    //</editor-fold>

    @Override
    public boolean handleMessage(SOAPMessageContext messageContext) {
        boolean success = false;

        /*
         * Process the SOAP header for an outbound message
         */
        if ((Boolean) messageContext.get(SOAPMessageContext.MESSAGE_OUTBOUND_PROPERTY) == true) {
            try {
                /*
                 * Check if local static variables have been initialised
                 */
                if (initialised == false) {
                    soapFactory = SOAPFactory.newInstance();
                    objectFactory = new ObjectFactory();
                    JAXBElement<AgentAuthHeader> jaxbElementAgentAuthHeader = objectFactory.createAgentAuthHeader(new AgentAuthHeader());
                    qnameAgentAuthHeader = jaxbElementAgentAuthHeader.getName();
                    JAXBElement<InitAuthHeader> jaxbElementInitAuthHeader = objectFactory.createInitAuthHeader(new InitAuthHeader());
                    qnameInitAuthHeader = jaxbElementInitAuthHeader.getName();

                    initialised = true;
                }

                /*
                 * Process the SOAP header to add the authentication information
                 */
                this.ProcessSoapHeader(messageContext);

                /*
                 * Write the finished SOAP message to system output
                 */
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                messageContext.getMessage().writeTo(outputStream);
                System.out.println(STR_ClassName + outputStream.toString());

                success = true;
            } catch (SOAPException | IOException ex) {
                Logfile.WriteError(ex.toString());
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
         * Get the SOAP envelope and SOAP factory
         */
        SOAPMessage soapMessage = messageContext.getMessage();
        SOAPEnvelope soapEnvelope = soapMessage.getSOAPPart().getEnvelope();
        SOAPHeader soapHeader = soapEnvelope.getHeader();
        if (soapHeader == null) {
            soapHeader = soapEnvelope.addHeader();
        }

        /*
         * Get authentication header information and process the information according to the authentication header type
         */
        Object object = messageContext.get(qnameAgentAuthHeader.getLocalPart());
        if (object != null && object instanceof AgentAuthHeader) {
            /*
             * AgentAuthHeader
             */
            this.ProcessAgentAuthHeader((AgentAuthHeader) object, qnameAgentAuthHeader, soapHeader);
        }
        object = messageContext.get(qnameInitAuthHeader.getLocalPart());
        if (object != null && object instanceof InitAuthHeader) {
            /*
             * InitAuthHeader
             */
            this.ProcessInitAuthHeader((InitAuthHeader) object, qnameInitAuthHeader, soapHeader);
        }
    }

    /**
     *
     * @param agentAuthHeader
     * @param qnameAuthHeader
     * @param soapHeader
     * @return SOAPHeaderElement
     */
    private SOAPHeaderElement ProcessAgentAuthHeader(AgentAuthHeader agentAuthHeader, QName qnameAuthHeader, SOAPHeader soapHeader) {

        SOAPHeaderElement headerElement;

        try {
            /*
             * Create the authentication header element
             */
            headerElement = soapHeader.addHeaderElement(qnameAuthHeader);

            /*
             * Check if AgentGuid is specified
             */
            if (agentAuthHeader.getAgentGuid() != null) {
                /*
                 * Create AgentGuid element
                 */
                QName qName = new QName(qnameAuthHeader.getNamespaceURI(), STR_AgentGuid, qnameAuthHeader.getPrefix());
                SOAPElement element = soapFactory.createElement(qName);
                element.addTextNode(agentAuthHeader.getAgentGuid());
                headerElement.addChildElement(element);
            }

            /*
             * Check if Coupon is specified
             */
            if (agentAuthHeader.getCoupon() != null) {
                /*
                 * Get the coupon element and add to the header
                 */
                SOAPElement couponElement = this.ProcessCoupon(agentAuthHeader.getCoupon(), qnameAuthHeader, soapFactory);
                if (couponElement != null) {
                    headerElement.addChildElement(couponElement);
                }
            }
        } catch (SOAPException ex) {
            headerElement = null;
        }

        return headerElement;
    }

    /**
     *
     * @param initAuthHeader
     * @param qnameAuthHeader
     * @param soapHeader
     * @return SOAPHeaderElement
     */
    private SOAPHeaderElement ProcessInitAuthHeader(InitAuthHeader initAuthHeader, QName qnameAuthHeader, SOAPHeader soapHeader) {

        SOAPHeaderElement headerElement;

        try {
            /*
             * Create the authentication header element
             */
            headerElement = soapHeader.addHeaderElement(qnameAuthHeader);

            /*
             * Check if InitPasskey is specified
             */
            if (initAuthHeader.getInitPasskey() != null) {
                /*
                 * Create InitPasskey element
                 */
                QName qName = new QName(qnameAuthHeader.getNamespaceURI(), STR_InitPasskey, qnameAuthHeader.getPrefix());
                SOAPElement element = soapFactory.createElement(qName);
                element.addTextNode(initAuthHeader.getInitPasskey());
                headerElement.addChildElement(element);
            }
        } catch (SOAPException ex) {
            headerElement = null;
        }

        return headerElement;
    }

    /**
     *
     * @param coupon
     * @param soapFactory
     * @return SOAPElement
     */
    private SOAPElement ProcessCoupon(Coupon coupon, QName qNameAuthHeader, SOAPFactory soapFactory) {

        SOAPElement couponElement;

        try {
            /*
             * Create the coupon element
             */
            QName qNameCoupon = new QName(qNameAuthHeader.getNamespaceURI(), STR_Coupon, qNameAuthHeader.getPrefix());
            couponElement = soapFactory.createElement(qNameCoupon);

            /*
             * Create couponId element and add to the coupon element
             */
            QName qName = new QName(qNameCoupon.getNamespaceURI(), STR_CouponCouponId, qNameCoupon.getPrefix());
            SOAPElement element = soapFactory.createElement(qName);
            element.addTextNode(Long.toString(coupon.getCouponId()));
            couponElement.addChildElement(element);

            /*
             * Create the issuerGuid element and add to the coupon element
             */
            qName = new QName(qNameCoupon.getNamespaceURI(), STR_CouponIssuerGuid, qNameCoupon.getPrefix());
            element = soapFactory.createElement(qName);
            element.addTextNode(coupon.getIssuerGuid());
            couponElement.addChildElement(element);

            /*
             * Create passkey element and add to the coupon element
             */
            qName = new QName(qNameCoupon.getNamespaceURI(), STR_CouponPasskey, qNameCoupon.getPrefix());
            element = soapFactory.createElement(qName);
            element.addTextNode(coupon.getPasskey());
            couponElement.addChildElement(element);

        } catch (SOAPException ex) {
            couponElement = null;
        }

        return couponElement;
    }
}
