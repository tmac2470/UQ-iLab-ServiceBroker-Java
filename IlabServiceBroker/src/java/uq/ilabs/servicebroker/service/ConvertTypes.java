/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uq.ilabs.servicebroker.service;

import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import uq.ilabs.library.datatypes.batch.ClientSubmissionReport;
import uq.ilabs.library.datatypes.batch.ExperimentStatus;
import uq.ilabs.library.datatypes.batch.LabExperimentStatus;
import uq.ilabs.library.datatypes.batch.LabStatus;
import uq.ilabs.library.datatypes.batch.ResultReport;
import uq.ilabs.library.datatypes.batch.ValidationReport;
import uq.ilabs.library.datatypes.batch.WaitEstimate;
import uq.ilabs.library.datatypes.processagent.ProcessAgent;
import uq.ilabs.library.datatypes.processagent.ProcessAgentTypes;
import uq.ilabs.library.datatypes.processagent.ServiceDescription;
import uq.ilabs.library.datatypes.processagent.StatusNotificationReport;
import uq.ilabs.library.datatypes.processagent.StatusReport;
import uq.ilabs.library.datatypes.storage.ExperimentRecord;
import uq.ilabs.library.datatypes.storage.StorageStatus;
import uq.ilabs.library.datatypes.ticketing.Coupon;
import uq.ilabs.library.datatypes.ticketing.Ticket;
import uq.ilabs.library.lab.utilities.Logfile;

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
     * @param strings
     * @return edu.mit.ilab.ArrayOfString
     */
    public static edu.mit.ilab.ArrayOfString Convert(String[] strings) {
        edu.mit.ilab.ArrayOfString arrayOfString = null;

        if (strings != null) {
            arrayOfString = new edu.mit.ilab.ArrayOfString();
            arrayOfString.getString().addAll(Arrays.asList(strings));
        }

        return arrayOfString;
    }

    /**
     *
     * @param experimentStatus
     * @return edu.mit.ilab.ExperimentStatus
     */
    public static edu.mit.ilab.ExperimentStatus Convert(ExperimentStatus experimentStatus) {
        edu.mit.ilab.ExperimentStatus proxyExperimentStatus = null;

        if (experimentStatus != null) {
            proxyExperimentStatus = new edu.mit.ilab.ExperimentStatus();
            proxyExperimentStatus.setEstRemainingRuntime(experimentStatus.getEstRemainingRuntime());
            proxyExperimentStatus.setEstRuntime(experimentStatus.getEstRuntime());
            proxyExperimentStatus.setStatusCode(experimentStatus.getStatusCode().getValue());
            proxyExperimentStatus.setWait(Convert(experimentStatus.getWaitEstimate()));
        }

        return proxyExperimentStatus;
    }

    /**
     *
     * @param labExperimentStatus
     * @return edu.mit.ilab.LabExperimentStatus
     */
    public static edu.mit.ilab.LabExperimentStatus Convert(LabExperimentStatus labExperimentStatus) {
        edu.mit.ilab.LabExperimentStatus proxyLabExperimentStatus = null;

        if (labExperimentStatus != null) {
            proxyLabExperimentStatus = new edu.mit.ilab.LabExperimentStatus();
            proxyLabExperimentStatus.setMinTimetoLive(labExperimentStatus.getMinTimetoLive());
            proxyLabExperimentStatus.setStatusReport(Convert(labExperimentStatus.getExperimentStatus()));
        }

        return proxyLabExperimentStatus;
    }

    /**
     *
     * @param labStatus
     * @return edu.mit.ilab.LabStatus
     */
    public static edu.mit.ilab.LabStatus Convert(LabStatus labStatus) {
        edu.mit.ilab.LabStatus proxyLabStatus = null;

        if (labStatus != null) {
            proxyLabStatus = new edu.mit.ilab.LabStatus();
            proxyLabStatus.setOnline(labStatus.isOnline());
            proxyLabStatus.setLabStatusMessage(labStatus.getLabStatusMessage());
        }

        return proxyLabStatus;
    }

    /**
     *
     * @param resultReport
     * @return edu.mit.ilab.ResultReport
     */
    public static edu.mit.ilab.ResultReport Convert(ResultReport resultReport) {
        edu.mit.ilab.ResultReport proxyResultReport = null;

        if (resultReport != null) {
            proxyResultReport = new edu.mit.ilab.ResultReport();
            proxyResultReport.setErrorMessage(resultReport.getErrorMessage());
            proxyResultReport.setExperimentResults(resultReport.getXmlExperimentResults());
            proxyResultReport.setStatusCode(resultReport.getStatusCode().getValue());
            proxyResultReport.setWarningMessages(Convert(resultReport.getWarningMessages()));
            proxyResultReport.setXmlBlobExtension(resultReport.getXmlBlobExtension());
            proxyResultReport.setXmlResultExtension(resultReport.getXmlResultExtension());
        }

        return proxyResultReport;
    }

    /**
     *
     * @param submissionReport
     * @return edu.mit.ilab.ClientSubmissionReport
     */
    public static edu.mit.ilab.ClientSubmissionReport Convert(ClientSubmissionReport clientSubmissionReport) {
        edu.mit.ilab.ClientSubmissionReport proxyClientSubmissionReport = null;

        if (clientSubmissionReport != null) {
            proxyClientSubmissionReport = new edu.mit.ilab.ClientSubmissionReport();
            proxyClientSubmissionReport.setExperimentID(clientSubmissionReport.getExperimentId());
            proxyClientSubmissionReport.setMinTimeToLive(clientSubmissionReport.getMinTimeToLive());
            proxyClientSubmissionReport.setVReport(Convert(clientSubmissionReport.getValidationReport()));
            proxyClientSubmissionReport.setWait(Convert(clientSubmissionReport.getWaitEstimate()));
        }

        return proxyClientSubmissionReport;
    }

    /**
     *
     * @param validationReport
     * @return edu.mit.ilab.ValidationReport
     */
    public static edu.mit.ilab.ValidationReport Convert(ValidationReport validationReport) {
        edu.mit.ilab.ValidationReport proxyValidationReport = null;

        if (validationReport != null) {
            proxyValidationReport = new edu.mit.ilab.ValidationReport();
            proxyValidationReport.setAccepted(validationReport.isAccepted());
            proxyValidationReport.setErrorMessage(validationReport.getErrorMessage());
            proxyValidationReport.setEstRuntime(validationReport.getEstRuntime());
            proxyValidationReport.setWarningMessages(Convert(validationReport.getWarningMessages()));
        }

        return proxyValidationReport;
    }

    /**
     *
     * @param waitEstimate
     * @return edu.mit.ilab.WaitEstimate
     */
    public static edu.mit.ilab.WaitEstimate Convert(WaitEstimate waitEstimate) {
        edu.mit.ilab.WaitEstimate proxyWaitEstimate = null;

        if (waitEstimate != null) {
            proxyWaitEstimate = new edu.mit.ilab.WaitEstimate();
            proxyWaitEstimate.setEffectiveQueueLength(waitEstimate.getEffectiveQueueLength());
            proxyWaitEstimate.setEstWait(waitEstimate.getEstWait());
        }

        return proxyWaitEstimate;
    }

    /**
     *
     * @param proxyCoupon
     * @return Coupon
     */
    public static Coupon ConvertType(edu.mit.ilab.ilabs.type.Coupon proxyCoupon) {
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
     * @param experimentRecord
     * @return edu.mit.ilab.ilabs.type.ExperimentRecord
     */
    public static edu.mit.ilab.ilabs.type.ExperimentRecord ConvertType(ExperimentRecord experimentRecord) {
        edu.mit.ilab.ilabs.type.ExperimentRecord proxyExperimentRecord = null;

        if (experimentRecord != null) {
            proxyExperimentRecord = new edu.mit.ilab.ilabs.type.ExperimentRecord();
            proxyExperimentRecord.setType(experimentRecord.getRecordType());
            proxyExperimentRecord.setSubmitter(experimentRecord.getSubmitter());
            proxyExperimentRecord.setSequenceNum(experimentRecord.getSequenceNum());
            proxyExperimentRecord.setXmlSearchable(experimentRecord.isXmlSearchable());
            proxyExperimentRecord.setContents(experimentRecord.getContents());
            /*
             * Convert the timestamps
             */
            try {
                DatatypeFactory datatypeFactory = DatatypeFactory.newInstance();
                GregorianCalendar gregorianCalendar = new GregorianCalendar();

                /*
                 * Creation time
                 */
                Calendar calendar = experimentRecord.getDateCreated();
                if (calendar != null) {
                    gregorianCalendar.setTime(calendar.getTime());
                    proxyExperimentRecord.setTimestamp(datatypeFactory.newXMLGregorianCalendar(gregorianCalendar));
                }
            } catch (DatatypeConfigurationException ex) {
                Logfile.WriteError(ex.toString());
            }
        }

        return proxyExperimentRecord;
    }

    /**
     *
     * @param coupon
     * @return edu.mit.ilab.ilabs.type.Coupon
     */
    public static edu.mit.ilab.ilabs.type.Coupon ConvertType(Coupon coupon) {
        edu.mit.ilab.ilabs.type.Coupon proxyCoupon = null;

        if (coupon != null) {
            proxyCoupon = new edu.mit.ilab.ilabs.type.Coupon();
            proxyCoupon.setCouponId(coupon.getCouponId());
            proxyCoupon.setIssuerGuid(coupon.getIssuerGuid());
            proxyCoupon.setPasskey(coupon.getPasskey());
        }

        return proxyCoupon;
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
     * @param storageStatus
     * @return edu.mit.ilab.ilabs.type.StorageStatus
     */
    public static edu.mit.ilab.ilabs.type.StorageStatus ConvertType(StorageStatus storageStatus) {
        edu.mit.ilab.ilabs.type.StorageStatus proxyStorageStatus = null;

        if (storageStatus != null) {
            /*
             * Convert to return type
             */
            proxyStorageStatus = new edu.mit.ilab.ilabs.type.StorageStatus();
            proxyStorageStatus.setExperimentId(storageStatus.getExperimentId());
            proxyStorageStatus.setStatus(storageStatus.getStatusCode().getValue() + storageStatus.getBatchStatusCode().getValue());
            proxyStorageStatus.setRecordCount(storageStatus.getRecordCount());
            proxyStorageStatus.setIssuerGuid(storageStatus.getIssuerGuid());

            /*
             * Convert the timestamps
             */
            try {
                DatatypeFactory datatypeFactory = DatatypeFactory.newInstance();
                GregorianCalendar gregorianCalendar = new GregorianCalendar();

                /*
                 * Creation time
                 */
                Calendar calendar = storageStatus.getDateCreated();
                if (calendar != null) {
                    gregorianCalendar.setTime(calendar.getTime());
                    proxyStorageStatus.setCreationTime(datatypeFactory.newXMLGregorianCalendar(gregorianCalendar));
                }

                /*
                 * Close time
                 */
                calendar = storageStatus.getDateClosed();
                if (calendar != null) {
                    gregorianCalendar.setTime(calendar.getTime());
                    proxyStorageStatus.setCloseTime(datatypeFactory.newXMLGregorianCalendar(gregorianCalendar));
                }

                /*
                 * LastModified time
                 */
                calendar = storageStatus.getDateModified();
                if (calendar != null) {
                    gregorianCalendar.setTime(calendar.getTime());
                    proxyStorageStatus.setLastModified(datatypeFactory.newXMLGregorianCalendar(gregorianCalendar));
                }
            } catch (DatatypeConfigurationException ex) {
                Logfile.WriteError(ex.toString());
            }
        }

        return proxyStorageStatus;
    }

    /**
     *
     * @param ticket
     * @return edu.mit.ilab.ilabs.type.Ticket
     */
    public static edu.mit.ilab.ilabs.type.Ticket ConvertType(Ticket ticket) {
        edu.mit.ilab.ilabs.type.Ticket proxyTicket = null;

        if (ticket != null) {
            proxyTicket = new edu.mit.ilab.ilabs.type.Ticket();
            proxyTicket.setTicketId(ticket.getTicketId());
            proxyTicket.setType(ticket.getTicketType().toString());
            proxyTicket.setCouponId(ticket.getCouponId());
            proxyTicket.setIssuerGuid(ticket.getIssuerGuid());
            proxyTicket.setSponsorGuid(ticket.getSponsorGuid());
            proxyTicket.setRedeemerGuid(ticket.getRedeemerGuid());
            proxyTicket.setCreationTime(Convert(ticket.getDateCreated()));
            proxyTicket.setDuration(ticket.getDuration());
            proxyTicket.setIsCancelled(ticket.isCancelled());
            proxyTicket.setPayload(ticket.getPayload());
        }

        return proxyTicket;
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
     * @param experimentRecords
     * @return ArrayOfExperimentRecord
     */
    public static edu.mit.ilab.ilabs.services.ArrayOfExperimentRecord ConvertServices(ExperimentRecord[] experimentRecords) {
        edu.mit.ilab.ilabs.services.ArrayOfExperimentRecord arrayOfExperimentRecord = null;

        if (experimentRecords != null) {
            arrayOfExperimentRecord = new edu.mit.ilab.ilabs.services.ArrayOfExperimentRecord();
            for (ExperimentRecord experimentRecord : experimentRecords) {
                arrayOfExperimentRecord.getExperimentRecord().add(ConvertType(experimentRecord));
            }
        }

        return arrayOfExperimentRecord;
    }
}
