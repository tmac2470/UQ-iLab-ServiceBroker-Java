/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uq.ilabs.library.processagent.proxy;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import uq.ilabs.library.datatypes.processagent.ProcessAgent;
import uq.ilabs.library.datatypes.processagent.ProcessAgentTypes;
import uq.ilabs.library.datatypes.processagent.ServiceDescription;
import uq.ilabs.library.datatypes.processagent.StatusNotificationReport;
import uq.ilabs.library.datatypes.processagent.StatusReport;
import uq.ilabs.library.datatypes.ticketing.Coupon;

/**
 *
 * @author uqlpayne
 */
public class ConvertTypes {

    /**
     *
     * @param xmlGregorianCalendar
     * @return Calendar
     */
    public static Calendar Convert(XMLGregorianCalendar xmlGregorianCalendar) {
        Calendar calendar = null;

        if (xmlGregorianCalendar != null) {
            calendar = Calendar.getInstance();
            calendar.setTime(xmlGregorianCalendar.toGregorianCalendar().getTime());
        }

        return calendar;
    }

    /**
     *
     * @param calendar
     * @return XMLGregorianCalendar
     */
    public static XMLGregorianCalendar Convert(Calendar calendar) {
        XMLGregorianCalendar xmlGregorianCalendar = null;

        if (calendar != null) {
            try {
                DatatypeFactory datatypeFactory = DatatypeFactory.newInstance();
                GregorianCalendar gregorianCalendar = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
                gregorianCalendar.setTime(calendar.getTime());
                xmlGregorianCalendar = datatypeFactory.newXMLGregorianCalendar(gregorianCalendar);
            } catch (DatatypeConfigurationException ex) {
            }
        }

        return xmlGregorianCalendar;
    }

    /**
     *
     * @param proxyCoupon
     * @return Coupon
     */
    public static Coupon Convert(edu.mit.ilab.ilabs.processagent.proxy.Coupon proxyCoupon) {
        Coupon coupon = null;

        if (proxyCoupon != null) {
            coupon = new Coupon();
            coupon.setCouponId(proxyCoupon.getCouponId());
            coupon.setIssuerGuid(proxyCoupon.getIssuerGuid());
            coupon.setPasskey(proxyCoupon.getPasskey());
        }

        return coupon;
    }

    /**
     *
     * @param proxyProcessAgent
     * @return ProcessAgent
     */
    public static ProcessAgent Convert(edu.mit.ilab.ilabs.processagent.proxy.ProcessAgent proxyProcessAgent) {
        ProcessAgent processAgent = null;

        if (proxyProcessAgent != null) {
            processAgent = new ProcessAgent();
            processAgent.setAgentGuid(proxyProcessAgent.getAgentGuid());
            processAgent.setAgentName(proxyProcessAgent.getAgentName());
            processAgent.setAgentType(ProcessAgentTypes.ToType(proxyProcessAgent.getType()));
            processAgent.setServiceUrl(proxyProcessAgent.getWebServiceUrl());
            processAgent.setClientUrl(proxyProcessAgent.getCodeBaseUrl());
            processAgent.setDomainGuid(proxyProcessAgent.getDomainGuid());
        }

        return processAgent;
    }

    /**
     *
     * @param proxyStatusReport
     * @return StatusReport
     */
    public static StatusReport Convert(edu.mit.ilab.ilabs.processagent.proxy.StatusReport proxyStatusReport) {
        StatusReport statusReport = null;

        if (proxyStatusReport != null) {
            statusReport = new StatusReport();
            statusReport.setOnline(proxyStatusReport.isOnline());
            statusReport.setServiceGuid(proxyStatusReport.getServiceGuid());
            statusReport.setPayload(proxyStatusReport.getPayload());
        }

        return statusReport;
    }

    /**
     *
     * @param coupon
     * @return edu.mit.ilab.ilabs.processagent.proxy.Coupon
     */
    public static edu.mit.ilab.ilabs.processagent.proxy.Coupon Convert(Coupon coupon) {
        edu.mit.ilab.ilabs.processagent.proxy.Coupon proxyCoupon = null;

        if (coupon != null) {
            proxyCoupon = new edu.mit.ilab.ilabs.processagent.proxy.Coupon();
            proxyCoupon.setCouponId(coupon.getCouponId());
            proxyCoupon.setIssuerGuid(coupon.getIssuerGuid());
            proxyCoupon.setPasskey(coupon.getPasskey());
        }

        return proxyCoupon;
    }

    /**
     *
     * @param processAgent
     * @return edu.mit.ilab.ilabs.processagent.proxy.ProcessAgent
     */
    public static edu.mit.ilab.ilabs.processagent.proxy.ProcessAgent Convert(ProcessAgent processAgent) {
        edu.mit.ilab.ilabs.processagent.proxy.ProcessAgent proxyProcessAgent = null;

        if (processAgent != null) {
            proxyProcessAgent = new edu.mit.ilab.ilabs.processagent.proxy.ProcessAgent();
            proxyProcessAgent.setAgentGuid(processAgent.getAgentGuid());
            proxyProcessAgent.setAgentName(processAgent.getAgentName());
            proxyProcessAgent.setType(processAgent.getAgentType() != null ? processAgent.getAgentType().toString() : null);
            proxyProcessAgent.setWebServiceUrl(processAgent.getServiceUrl());
            proxyProcessAgent.setCodeBaseUrl(processAgent.getClientUrl());
            proxyProcessAgent.setDomainGuid(processAgent.getDomainGuid());
        }

        return proxyProcessAgent;
    }

    /**
     *
     * @param serviceDescription
     * @return edu.mit.ilab.ilabs.processagent.proxy.ServiceDescription
     */
    public static edu.mit.ilab.ilabs.processagent.proxy.ServiceDescription Convert(ServiceDescription serviceDescription) {
        edu.mit.ilab.ilabs.processagent.proxy.ServiceDescription proxyServiceDescription = null;

        if (serviceDescription != null) {
            proxyServiceDescription = new edu.mit.ilab.ilabs.processagent.proxy.ServiceDescription();
            proxyServiceDescription.setServiceProviderInfo(serviceDescription.getServiceProviderInfo());
            proxyServiceDescription.setConsumerInfo(serviceDescription.getConsumerInfo());
            proxyServiceDescription.setCoupon(Convert(serviceDescription.getCoupon()));
        }

        return proxyServiceDescription;
    }

    /**
     *
     * @param serviceDescriptions
     * @return edu.mit.ilab.ilabs.processagent.proxy.ArrayOfServiceDescription
     */
    public static edu.mit.ilab.ilabs.processagent.proxy.ArrayOfServiceDescription Convert(ServiceDescription[] serviceDescriptions) {
        edu.mit.ilab.ilabs.processagent.proxy.ArrayOfServiceDescription arrayOfServiceDescription = null;

        if (serviceDescriptions != null) {
            arrayOfServiceDescription = new edu.mit.ilab.ilabs.processagent.proxy.ArrayOfServiceDescription();
            for (ServiceDescription serviceDescription : serviceDescriptions) {
                arrayOfServiceDescription.getServiceDescription().add(Convert(serviceDescription));
            }
        }

        return arrayOfServiceDescription;
    }

    /**
     *
     * @param statusNotificationReport
     * @return edu.mit.ilab.ilabs.processagent.proxy.StatusNotificationReport
     */
    public static edu.mit.ilab.ilabs.processagent.proxy.StatusNotificationReport Convert(StatusNotificationReport statusNotificationReport) {
        edu.mit.ilab.ilabs.processagent.proxy.StatusNotificationReport proxyStatusNotificationReport = null;

        if (statusNotificationReport != null) {
            proxyStatusNotificationReport = new edu.mit.ilab.ilabs.processagent.proxy.StatusNotificationReport();
            proxyStatusNotificationReport.setAlertCode(statusNotificationReport.getAlertCode());
            proxyStatusNotificationReport.setServiceGuid(statusNotificationReport.getServiceGuid());
            proxyStatusNotificationReport.setTime(Convert(statusNotificationReport.getTimestamp()));
            proxyStatusNotificationReport.setPayload(statusNotificationReport.getPayload());
        }

        return proxyStatusNotificationReport;
    }
}
