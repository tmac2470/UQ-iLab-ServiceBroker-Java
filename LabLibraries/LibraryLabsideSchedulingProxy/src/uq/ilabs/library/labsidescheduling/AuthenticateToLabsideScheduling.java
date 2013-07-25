/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uq.ilabs.library.labsidescheduling;

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
import uq.ilabs.labsidescheduling.AgentAuthHeader;
import uq.ilabs.labsidescheduling.Coupon;
import uq.ilabs.labsidescheduling.ObjectFactory;
import uq.ilabs.labsidescheduling.OperationAuthHeader;
import uq.ilabs.library.lab.utilities.Logfile;

/**
 *
 * @author uqlpayne
 */
public class AuthenticateToLabsideScheduling implements SOAPHandler<SOAPMessageContext> {

    //<editor-fold defaultstate="collapsed" desc="Constants">
    /*
     * String constants
     */
    private static final String STR_AgentGuid = "agentGuid";
    private static final String STR_Coupon = "coupon";
    private static final String STR_CouponIssuerGuid = "issuerGuid";
    private static final String STR_CouponCouponId = "couponId";
    private static final String STR_CouponPasskey = "passkey";
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Variables">
    private static boolean initialised = false;
    private static SOAPFactory soapFactory;
    private static ObjectFactory objectFactory;
    private static QName qnameAgentAuthHeader;
    private static QName qnameOperationAuthHeader;
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
                    JAXBElement<OperationAuthHeader> jaxbElementOperationAuthHeader = objectFactory.createOperationAuthHeader(new OperationAuthHeader());
                    qnameOperationAuthHeader = jaxbElementOperationAuthHeader.getName();

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
                System.out.println(outputStream.toString());

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
        object = messageContext.get(qnameOperationAuthHeader.getLocalPart());
        if (object != null && object instanceof OperationAuthHeader) {
            /*
             * OperationAuthHeader
             */
            this.ProcessOperationAuthHeader((OperationAuthHeader) object, qnameOperationAuthHeader, soapHeader);
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
     * @param operationAuthHeader
     * @param qnameAuthHeader
     * @param soapHeader
     * @return SOAPHeaderElement
     */
    private SOAPHeaderElement ProcessOperationAuthHeader(OperationAuthHeader operationAuthHeader, QName qnameAuthHeader, SOAPHeader soapHeader) {

        SOAPHeaderElement headerElement;

        try {
            /*
             * Create the authentication header element
             */
            headerElement = soapHeader.addHeaderElement(qnameAuthHeader);

            /*
             * Check if Coupon is specified
             */
            if (operationAuthHeader.getCoupon() != null) {
                /*
                 * Get the coupon element and add to the header
                 */
                SOAPElement couponElement = this.ProcessCoupon(operationAuthHeader.getCoupon(), qnameAuthHeader, soapFactory);
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
