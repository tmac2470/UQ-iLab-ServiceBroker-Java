/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uq.ilabs.experimentstorage.service;

import edu.mit.ilab.ilabs.type.AgentAuthHeader;
import edu.mit.ilab.ilabs.type.Coupon;
import edu.mit.ilab.ilabs.type.InitAuthHeader;
import edu.mit.ilab.ilabs.type.ObjectFactory;
import edu.mit.ilab.ilabs.type.OperationAuthHeader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.ProtocolException;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.MessageContext.Scope;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import uq.ilabs.library.experimentstorage.engine.ConfigProperties;
import uq.ilabs.library.experimentstorage.engine.LabConsts;
import uq.ilabs.library.lab.utilities.Logfile;

/**
 *
 * @author uqlpayne
 */
public class AuthenticateToService implements SOAPHandler<SOAPMessageContext> {

    //<editor-fold defaultstate="collapsed" desc="Constants">
    private static final String STR_ClassName = AuthenticateToService.class.getName();
    /*
     * String constants
     */
    private static final String STR_AgentGuid = "agentGuid";
    private static final String STR_Coupon = "coupon";
    private static final String STR_CouponIssuerGuid = "issuerGuid";
    private static final String STR_CouponCouponId = "couponId";
    private static final String STR_CouponPasskey = "passkey";
    private static final String STR_InitPasskey = "initPasskey";
    /*
     * String constants for logfile messages
     */
    private static final String STRLOG_LoggingLevel_arg = "LoggingLevel: %s";
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Variables">
    private static ObjectFactory objectFactory;
    private static String qnameAgentAuthHeaderLocalPart;
    private static String qnameInitAuthHeaderLocalPart;
    private static String qnameOperationAuthHeaderLocalPart;
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
                 * Check if initialisation parameters have been read from the web.xml file
                 */
                if (ExperimentStorageService.isInitialised() == false) {
                    this.GetInitParameters((ServletContext) messageContext.get(MessageContext.SERVLET_CONTEXT));

                    /*
                     * Get the authentication header names
                     */
                    objectFactory = new ObjectFactory();
                    JAXBElement<AgentAuthHeader> jaxbElementAgentAuthHeader = objectFactory.createAgentAuthHeader(new AgentAuthHeader());
                    qnameAgentAuthHeaderLocalPart = jaxbElementAgentAuthHeader.getName().getLocalPart();
                    JAXBElement<InitAuthHeader> jaxbElementInitAuthHeader = objectFactory.createInitAuthHeader(new InitAuthHeader());
                    qnameInitAuthHeaderLocalPart = jaxbElementInitAuthHeader.getName().getLocalPart();
                    JAXBElement<OperationAuthHeader> jaxbElementOperationAuthHeader = objectFactory.createOperationAuthHeader(new OperationAuthHeader());
                    qnameOperationAuthHeaderLocalPart = jaxbElementOperationAuthHeader.getName().getLocalPart();
                }

                /*
                 * Write the SOAP message to system output
                 */
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                messageContext.getMessage().writeTo(outputStream);
//                System.out.println(STR_ClassName + outputStream.toString());

                /*
                 * Process the SOAP header to get the authentication information
                 */
                ProcessSoapHeader(messageContext);

                success = true;
            } catch (SOAPException | IOException ex) {
                Logfile.WriteError(ex.getMessage());
                throw new ProtocolException(ex);
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
                if (localName.equalsIgnoreCase(qnameAgentAuthHeaderLocalPart) == true) {
                    /*
                     * AgentAuthHeader
                     */
                    AgentAuthHeader agentAuthHeader = ProcessSoapElementAgentAuthHeader(soapElement);
                    messageContext.put(qnameAgentAuthHeaderLocalPart, agentAuthHeader);
                    messageContext.setScope(qnameAgentAuthHeaderLocalPart, Scope.APPLICATION);
                } else if (localName.equalsIgnoreCase(qnameInitAuthHeaderLocalPart) == true) {
                    /*
                     * InitAuthHeader
                     */
                    InitAuthHeader initAuthHeader = ProcessSoapElementInitAuthHeader(soapElement);
                    messageContext.put(qnameInitAuthHeaderLocalPart, initAuthHeader);
                    messageContext.setScope(qnameInitAuthHeaderLocalPart, Scope.APPLICATION);
                } else if (localName.equalsIgnoreCase(qnameOperationAuthHeaderLocalPart) == true) {
                    /*
                     * OperationAuthHeader
                     */
                    OperationAuthHeader operationAuthHeader = ProcessSoapElementOperationAuthHeader(soapElement);
                    messageContext.put(qnameOperationAuthHeaderLocalPart, operationAuthHeader);
                    messageContext.setScope(qnameOperationAuthHeaderLocalPart, Scope.APPLICATION);
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
        AgentAuthHeader agentAuthHeader = objectFactory.createAgentAuthHeader();
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
                if (localName.equalsIgnoreCase(STR_AgentGuid) == true) {
                    agentAuthHeader.setAgentGuid(element.getValue());
                } else if (localName.equalsIgnoreCase(STR_Coupon) == true) {
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
        InitAuthHeader initAuthHeader = objectFactory.createInitAuthHeader();
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
                if (localName.equalsIgnoreCase(STR_InitPasskey) == true) {
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
        OperationAuthHeader operationAuthHeader = objectFactory.createOperationAuthHeader();
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
                if (localName.equalsIgnoreCase(STR_Coupon) == true) {
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
        Coupon coupon = objectFactory.createCoupon();
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
                if (localName.equalsIgnoreCase(STR_CouponCouponId) == true) {
                    coupon.setCouponId(Long.parseLong(element.getValue()));
                } else if (localName.equalsIgnoreCase(STR_CouponIssuerGuid) == true) {
                    coupon.setIssuerGuid(element.getValue());
                } else if (localName.equalsIgnoreCase(STR_CouponPasskey) == true) {
                    coupon.setPasskey(element.getValue());
                }
            }
        }

        return coupon;
    }

    /**
     *
     * @param servletContext
     */
    private void GetInitParameters(ServletContext servletContext) {
        final String methodName = "GetInitParameters";

        try {
            /*
             * Check if the logger has already been created by the client
             */
            if (ExperimentStorageService.isLoggerCreated() == false) {
                /*
                 * Get the path for the logfiles and logging level
                 */
                String logFilesPath = servletContext.getInitParameter(LabConsts.STRPRM_LogFilesPath);
                logFilesPath = servletContext.getRealPath(logFilesPath);
                String logLevel = servletContext.getInitParameter(LabConsts.STRPRM_LogLevel);

                /*
                 * Create an instance of the logger and set the logging level
                 */
                Logger logger = Logfile.CreateLogger(logFilesPath);
                ExperimentStorageService.setLoggerCreated(true);
                Level level = Level.INFO;
                try {
                    level = Level.parse(logLevel);
                } catch (Exception ex) {
                }
                logger.setLevel(level);

                Logfile.WriteCalled(STR_ClassName, methodName,
                        String.format(STRLOG_LoggingLevel_arg, logger.getLevel().toString()));
            } else {
                Logfile.WriteCalled(STR_ClassName, methodName);
            }

            /*
             * Get configuration properties from the file
             */
            String xmlConfigPropertiesPath = servletContext.getInitParameter(LabConsts.STRPRM_XmlConfigPropertiesPath);
            ConfigProperties configProperties = new ConfigProperties(servletContext.getRealPath(xmlConfigPropertiesPath));

            /*
             * Save to the service
             */
            ExperimentStorageService.setConfigProperties(configProperties);

            ExperimentStorageService.setInitialised(true);
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
        }

        Logfile.WriteCompleted(STR_ClassName, methodName);
    }
}
