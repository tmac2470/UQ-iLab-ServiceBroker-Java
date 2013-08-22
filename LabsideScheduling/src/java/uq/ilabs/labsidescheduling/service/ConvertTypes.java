/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uq.ilabs.labsidescheduling.service;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import uq.ilabs.library.datatypes.processagent.ProcessAgent;
import uq.ilabs.library.datatypes.processagent.ProcessAgentTypes;
import uq.ilabs.library.datatypes.processagent.ServiceDescription;
import uq.ilabs.library.datatypes.processagent.StatusNotificationReport;
import uq.ilabs.library.datatypes.processagent.StatusReport;
import uq.ilabs.library.datatypes.scheduling.TimePeriod;
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
    public static Calendar Convert(javax.xml.datatype.XMLGregorianCalendar xmlGregorianCalendar) {
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
            } catch (Exception ex) {
            }
        }

        return xmlGregorianCalendar;
    }

    /**
     *
     * @param proxyCoupon
     * @return Coupon
     */
    public static Coupon ConvertType(edu.mit.ilab.ilabs.type.Coupon proxyCoupon) {
        Coupon coupon = null;

        if (proxyCoupon != null) {
            coupon = new Coupon(proxyCoupon.getCouponId(), proxyCoupon.getIssuerGuid(), proxyCoupon.getPasskey());
        }

        return coupon;
    }

    /**
     *
     * @param proxyProcessAgent
     * @return ProcessAgent
     */
    public static ProcessAgent ConvertType(edu.mit.ilab.ilabs.type.ProcessAgent proxyProcessAgent) {
        ProcessAgent processAgent = null;

        if (proxyProcessAgent != null) {
            processAgent = new ProcessAgent();
            processAgent.setAgentName(proxyProcessAgent.getAgentName());
            processAgent.setAgentGuid(proxyProcessAgent.getAgentGuid());
            processAgent.setAgentType(ProcessAgentTypes.ToType(proxyProcessAgent.getType()));
            processAgent.setClientUrl(proxyProcessAgent.getCodeBaseUrl());
            processAgent.setServiceUrl(proxyProcessAgent.getWebServiceUrl());
            processAgent.setDomainGuid(proxyProcessAgent.getDomainGuid());
        }

        return processAgent;
    }

    /**
     *
     * @param proxyServiceDescription
     * @return ServiceDescription
     */
    public static ServiceDescription ConvertType(edu.mit.ilab.ilabs.type.ServiceDescription proxyServiceDescription) {
        ServiceDescription serviceDescription = null;

        if (proxyServiceDescription != null) {
            serviceDescription = new ServiceDescription();
            serviceDescription.setServiceProviderInfo(proxyServiceDescription.getServiceProviderInfo());
            serviceDescription.setCoupon(ConvertType(proxyServiceDescription.getCoupon()));
            serviceDescription.setConsumerInfo(proxyServiceDescription.getConsumerInfo());
        }

        return serviceDescription;
    }

    /**
     *
     * @param proxyStatusNotificationReport
     * @return StatusNotificationReport
     */
    public static StatusNotificationReport ConvertType(edu.mit.ilab.ilabs.type.StatusNotificationReport proxyStatusNotificationReport) {
        StatusNotificationReport statusNotificationReport = null;

        if (proxyStatusNotificationReport != null) {
            statusNotificationReport = new StatusNotificationReport();
            statusNotificationReport.setAlertCode(proxyStatusNotificationReport.getAlertCode());
            statusNotificationReport.setServiceGuid(proxyStatusNotificationReport.getServiceGuid());
            statusNotificationReport.setTimestamp(Convert(proxyStatusNotificationReport.getTime()));
            statusNotificationReport.setPayload(proxyStatusNotificationReport.getPayload());
        }

        return statusNotificationReport;
    }

    /**
     *
     * @param processAgent
     * @return edu.mit.ilab.ilabs.type.ProcessAgent
     */
    public static edu.mit.ilab.ilabs.type.ProcessAgent ConvertType(ProcessAgent processAgent) {
        edu.mit.ilab.ilabs.type.ProcessAgent proxyProcessAgent = null;

        if (processAgent != null) {
            proxyProcessAgent = new edu.mit.ilab.ilabs.type.ProcessAgent();
            proxyProcessAgent.setAgentGuid(processAgent.getAgentGuid());
            proxyProcessAgent.setAgentName(processAgent.getAgentName());
            proxyProcessAgent.setType(processAgent.getAgentType().toString());
            proxyProcessAgent.setDomainGuid(processAgent.getDomainGuid());
            proxyProcessAgent.setCodeBaseUrl(processAgent.getClientUrl());
            proxyProcessAgent.setWebServiceUrl(processAgent.getServiceUrl());
        }

        return proxyProcessAgent;
    }

    /**
     *
     * @param statusReport
     * @return edu.mit.ilab.ilabs.type.StatusReport
     */
    public static edu.mit.ilab.ilabs.type.StatusReport ConvertType(StatusReport statusReport) {
        edu.mit.ilab.ilabs.type.StatusReport proxyStatusReport = null;

        if (statusReport != null) {
            proxyStatusReport = new edu.mit.ilab.ilabs.type.StatusReport();
            proxyStatusReport.setOnline(statusReport.isOnline());
            proxyStatusReport.setServiceGuid(statusReport.getServiceGuid());
            proxyStatusReport.setPayload(statusReport.getPayload());
        }

        return proxyStatusReport;
    }

    /**
     *
     * @param timePeriod
     * @return edu.mit.ilab.ilabs.type.TimePeriod
     */
    public static edu.mit.ilab.ilabs.type.TimePeriod ConvertType(TimePeriod timePeriod) {
        edu.mit.ilab.ilabs.type.TimePeriod proxyTimePeriod = null;

        if (timePeriod != null) {
            proxyTimePeriod = new edu.mit.ilab.ilabs.type.TimePeriod();
            proxyTimePeriod.setDuration(timePeriod.getDuration());
            proxyTimePeriod.setQuantum(timePeriod.getQuantum());
            proxyTimePeriod.setStartTime(Convert(timePeriod.getStartTime()));
        }

        return proxyTimePeriod;
    }

    /**
     *
     * @param arrayOfServiceDescription
     * @return ServiceDescription[]
     */
    public static ServiceDescription[] ConvertServices(edu.mit.ilab.ilabs.services.ArrayOfServiceDescription arrayOfServiceDescription) {
        ServiceDescription[] serviceDescription = null;

        if (arrayOfServiceDescription != null) {
            List<edu.mit.ilab.ilabs.type.ServiceDescription> listProxyServiceDescription = arrayOfServiceDescription.getServiceDescription();
            if (listProxyServiceDescription.size() > 0) {
                serviceDescription = new ServiceDescription[listProxyServiceDescription.size()];
                for (int i = 0; i < serviceDescription.length; i++) {
                    serviceDescription[i] = ConvertType(listProxyServiceDescription.get(i));
                }
            }
        }

        return serviceDescription;
    }

    /**
     *
     * @param timePeriods
     * @return edu.mit.ilab.ilabs.services.ArrayOfTimePeriod
     */
    public static edu.mit.ilab.ilabs.services.ArrayOfTimePeriod ConvertServices(TimePeriod[] timePeriods) {
        edu.mit.ilab.ilabs.services.ArrayOfTimePeriod arrayOfTimePeriod = null;

        if (timePeriods != null) {
            arrayOfTimePeriod = new edu.mit.ilab.ilabs.services.ArrayOfTimePeriod();
            for (TimePeriod timePeriod : timePeriods) {
                arrayOfTimePeriod.getTimePeriod().add(ConvertType(timePeriod));
            }
        }

        return arrayOfTimePeriod;
    }
}
