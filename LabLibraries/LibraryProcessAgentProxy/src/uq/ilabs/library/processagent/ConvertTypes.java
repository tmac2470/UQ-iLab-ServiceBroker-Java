/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uq.ilabs.library.processagent;

import java.util.Calendar;
import java.util.GregorianCalendar;
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
     * @param proxyCoupon
     * @return Coupon
     */
    public static Coupon ConvertType(uq.ilabs.processagent.Coupon proxyCoupon) {
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
     * @param xmlGregorianCalendar
     * @return Calendar
     */
    public static Calendar ConvertType(XMLGregorianCalendar xmlGregorianCalendar) {
        Calendar calendar = null;

        if (xmlGregorianCalendar != null) {
            calendar = Calendar.getInstance();
            calendar.setTime(xmlGregorianCalendar.toGregorianCalendar().getTime());
        }

        return calendar;
    }

    /**
     *
     * @param proxyProcessAgent
     * @return ProcessAgent
     */
    public static ProcessAgent ConvertType(uq.ilabs.processagent.ProcessAgent proxyProcessAgent) {
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
    public static StatusReport ConvertType(uq.ilabs.processagent.StatusReport proxyStatusReport) {
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
     * @param calendar
     * @return XMLGregorianCalendar
     */
    public static XMLGregorianCalendar ConvertType(Calendar calendar) {
        XMLGregorianCalendar xmlGregorianCalendar = null;

        if (calendar != null) {
            try {
                DatatypeFactory datatypeFactory = DatatypeFactory.newInstance();
                GregorianCalendar gregorianCalendar = new GregorianCalendar();
                gregorianCalendar.setTime(calendar.getTime());
                xmlGregorianCalendar = datatypeFactory.newXMLGregorianCalendar(gregorianCalendar);
            } catch (DatatypeConfigurationException ex) {
            }
        }

        return xmlGregorianCalendar;
    }

    /**
     *
     * @param coupon
     * @return uq.ilabs.processagent.Coupon
     */
    public static uq.ilabs.processagent.Coupon ConvertType(Coupon coupon) {
        uq.ilabs.processagent.Coupon proxyCoupon = null;

        if (coupon != null) {
            proxyCoupon = new uq.ilabs.processagent.Coupon();
            proxyCoupon.setCouponId(coupon.getCouponId());
            proxyCoupon.setIssuerGuid(coupon.getIssuerGuid());
            proxyCoupon.setPasskey(coupon.getPasskey());
        }

        return proxyCoupon;
    }

    /**
     *
     * @param processAgent
     * @return uq.ilabs.processagent.ProcessAgent
     */
    public static uq.ilabs.processagent.ProcessAgent ConvertType(ProcessAgent processAgent) {
        uq.ilabs.processagent.ProcessAgent proxyProcessAgent = null;

        if (processAgent != null) {
            proxyProcessAgent = new uq.ilabs.processagent.ProcessAgent();
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
     * @return uq.ilabs.processagent.ServiceDescription
     */
    public static uq.ilabs.processagent.ServiceDescription ConvertType(ServiceDescription serviceDescription) {
        uq.ilabs.processagent.ServiceDescription proxyServiceDescription = null;

        if (serviceDescription != null) {
            proxyServiceDescription = new uq.ilabs.processagent.ServiceDescription();
            proxyServiceDescription.setServiceProviderInfo(serviceDescription.getServiceProviderInfo());
            proxyServiceDescription.setConsumerInfo(serviceDescription.getConsumerInfo());
            proxyServiceDescription.setCoupon(ConvertType(serviceDescription.getCoupon()));
        }

        return proxyServiceDescription;
    }

    /**
     *
     * @param serviceDescriptions
     * @return uq.ilabs.processagent.ArrayOfServiceDescription
     */
    public static uq.ilabs.processagent.ArrayOfServiceDescription ConvertType(ServiceDescription[] serviceDescriptions) {
        uq.ilabs.processagent.ArrayOfServiceDescription arrayOfServiceDescription = null;

        if (serviceDescriptions != null) {
            arrayOfServiceDescription = new uq.ilabs.processagent.ArrayOfServiceDescription();
            for (ServiceDescription serviceDescription : serviceDescriptions) {
                arrayOfServiceDescription.getServiceDescription().add(ConvertType(serviceDescription));
            }
        }

        return arrayOfServiceDescription;
    }

    /**
     *
     * @param statusNotificationReport
     * @return uq.ilabs.processagent.StatusNotificationReport
     */
    public static uq.ilabs.processagent.StatusNotificationReport ConvertType(StatusNotificationReport statusNotificationReport) {
        uq.ilabs.processagent.StatusNotificationReport proxyStatusNotificationReport = null;

        if (statusNotificationReport != null) {
            proxyStatusNotificationReport = new uq.ilabs.processagent.StatusNotificationReport();
            proxyStatusNotificationReport.setAlertCode(statusNotificationReport.getAlertCode());
            proxyStatusNotificationReport.setServiceGuid(statusNotificationReport.getServiceGuid());
            proxyStatusNotificationReport.setTime(ConvertType(statusNotificationReport.getTimestamp()));
            proxyStatusNotificationReport.setPayload(statusNotificationReport.getPayload());
        }

        return proxyStatusNotificationReport;
    }
}
