/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uq.ilabs.experimentstorage.service;

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
import uq.ilabs.library.datatypes.storage.ExperimentRecord;
import uq.ilabs.library.datatypes.storage.RecordAttribute;
import uq.ilabs.library.datatypes.storage.StorageStatus;
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
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(xmlGregorianCalendar.toGregorianCalendar().getTime());
        return calendar;
    }

    /**
     *
     * @param proxyCoupon
     * @return Coupon
     */
    public static Coupon Convert(edu.mit.ilab.ilabs.type.Coupon proxyCoupon) {
        Coupon coupon = null;

        if (proxyCoupon != null) {
            coupon = new Coupon(proxyCoupon.getCouponId(), proxyCoupon.getIssuerGuid(), proxyCoupon.getPasskey());
        }

        return coupon;
    }

    /**
     *
     * @param proxyExperimentRecord
     * @return ExperimentRecord
     */
    public static ExperimentRecord Convert(edu.mit.ilab.ilabs.type.ExperimentRecord proxyExperimentRecord) {
        ExperimentRecord experimentRecord = null;

        if (proxyExperimentRecord != null) {
            experimentRecord = new ExperimentRecord();
            experimentRecord.setRecordType(proxyExperimentRecord.getType());
            experimentRecord.setSubmitter(proxyExperimentRecord.getSubmitter());
            experimentRecord.setSequenceNum(proxyExperimentRecord.getSequenceNum());
            experimentRecord.setDateCreated(Convert(proxyExperimentRecord.getTimestamp()));
            experimentRecord.setXmlSearchable(proxyExperimentRecord.isXmlSearchable());
            experimentRecord.setContents(proxyExperimentRecord.getContents());
        }

        return experimentRecord;
    }

    /**
     *
     * @param arrayOfExperimentRecord
     * @return ExperimentRecord[]
     */
    public static ExperimentRecord[] Convert(edu.mit.ilab.ilabs.services.ArrayOfExperimentRecord arrayOfExperimentRecord) {
        ExperimentRecord[] experimentRecords = null;

        if (arrayOfExperimentRecord != null) {
            List<edu.mit.ilab.ilabs.type.ExperimentRecord> listProxyExperimentRecord = arrayOfExperimentRecord.getExperimentRecord();
            if (listProxyExperimentRecord.size() > 0) {
                experimentRecords = new ExperimentRecord[listProxyExperimentRecord.size()];
                for (int i = 0; i < experimentRecords.length; i++) {
                    experimentRecords[i] = Convert(listProxyExperimentRecord.get(i));
                }
            }
        }

        return experimentRecords;
    }

    /**
     *
     * @param proxyRecordAttribute
     * @return RecordAttribute
     */
    public static RecordAttribute Convert(edu.mit.ilab.ilabs.type.RecordAttribute proxyRecordAttribute) {
        RecordAttribute recordAttribute = null;

        if (proxyRecordAttribute != null) {
            recordAttribute = new RecordAttribute();
            recordAttribute.setName(proxyRecordAttribute.getName());
            recordAttribute.setValue(proxyRecordAttribute.getValue());
        }

        return recordAttribute;
    }

    /**
     *
     * @param arrayOfRecordAttribute
     * @return RecordAttribute[]
     */
    public static RecordAttribute[] Convert(edu.mit.ilab.ilabs.services.ArrayOfRecordAttribute arrayOfRecordAttribute) {
        RecordAttribute[] recordAttribute = null;

        if (arrayOfRecordAttribute != null) {
            List<edu.mit.ilab.ilabs.type.RecordAttribute> listProxyRecordAttribute = arrayOfRecordAttribute.getRecordAttribute();
            if (listProxyRecordAttribute.size() > 0) {
                recordAttribute = new RecordAttribute[listProxyRecordAttribute.size()];
                for (int i = 0; i < recordAttribute.length; i++) {
                    recordAttribute[i] = Convert(listProxyRecordAttribute.get(i));
                }
            }
        }

        return recordAttribute;
    }

    /**
     *
     * @param proxyProcessAgent
     * @return ProcessAgent
     */
    public static ProcessAgent Convert(edu.mit.ilab.ilabs.type.ProcessAgent proxyProcessAgent) {
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
    public static ServiceDescription Convert(edu.mit.ilab.ilabs.type.ServiceDescription proxyServiceDescription) {
        ServiceDescription serviceDescription = null;

        if (proxyServiceDescription != null) {
            serviceDescription = new ServiceDescription();
            serviceDescription.setServiceProviderInfo(proxyServiceDescription.getServiceProviderInfo());
            serviceDescription.setCoupon(Convert(proxyServiceDescription.getCoupon()));
            serviceDescription.setConsumerInfo(proxyServiceDescription.getConsumerInfo());
        }

        return serviceDescription;
    }

    /**
     *
     * @param arrayOfServiceDescription
     * @return ServiceDescription[]
     */
    public static ServiceDescription[] Convert(edu.mit.ilab.ilabs.services.ArrayOfServiceDescription arrayOfServiceDescription) {
        ServiceDescription[] serviceDescription = null;

        if (arrayOfServiceDescription != null) {
            List<edu.mit.ilab.ilabs.type.ServiceDescription> listProxyServiceDescription = arrayOfServiceDescription.getServiceDescription();
            if (listProxyServiceDescription.size() > 0) {
                serviceDescription = new ServiceDescription[listProxyServiceDescription.size()];
                for (int i = 0; i < serviceDescription.length; i++) {
                    serviceDescription[i] = Convert(listProxyServiceDescription.get(i));
                }
            }
        }

        return serviceDescription;
    }

    /**
     *
     * @param proxyStatusNotificationReport
     * @return StatusNotificationReport
     */
    public static StatusNotificationReport Convert(edu.mit.ilab.ilabs.type.StatusNotificationReport proxyStatusNotificationReport) {
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
     * @param experimentRecord
     * @return edu.mit.ilab.ilabs.type.ExperimentRecord
     */
    public static edu.mit.ilab.ilabs.type.ExperimentRecord Convert(ExperimentRecord experimentRecord) {

        edu.mit.ilab.ilabs.type.ExperimentRecord proxyExperimentRecord = null;

        if (experimentRecord != null) {
            proxyExperimentRecord = new edu.mit.ilab.ilabs.type.ExperimentRecord();
            proxyExperimentRecord.setType(experimentRecord.getRecordType());
            proxyExperimentRecord.setSubmitter(experimentRecord.getSubmitter());
            proxyExperimentRecord.setSequenceNum(experimentRecord.getSequenceNum());
            proxyExperimentRecord.setXmlSearchable(experimentRecord.isXmlSearchable());
            proxyExperimentRecord.setContents(experimentRecord.getContents());
            proxyExperimentRecord.setTimestamp(Convert(experimentRecord.getDateCreated()));
        }

        return proxyExperimentRecord;
    }

    /**
     *
     * @param experimentRecords
     * @return ArrayOfExperimentRecord
     */
    public static edu.mit.ilab.ilabs.services.ArrayOfExperimentRecord Convert(ExperimentRecord[] experimentRecords) {
        edu.mit.ilab.ilabs.services.ArrayOfExperimentRecord arrayOfExperimentRecord = null;

        if (experimentRecords != null) {
            arrayOfExperimentRecord = new edu.mit.ilab.ilabs.services.ArrayOfExperimentRecord();
            for (ExperimentRecord experimentRecord : experimentRecords) {
                arrayOfExperimentRecord.getExperimentRecord().add(Convert(experimentRecord));
            }
        }

        return arrayOfExperimentRecord;
    }

    /**
     *
     * @param processAgent
     * @return edu.mit.ilab.ilabs.type.ProcessAgent
     */
    public static edu.mit.ilab.ilabs.type.ProcessAgent Convert(ProcessAgent processAgent) {
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
    public static edu.mit.ilab.ilabs.type.StatusReport Convert(StatusReport statusReport) {
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
    public static edu.mit.ilab.ilabs.type.StorageStatus Convert(StorageStatus storageStatus) {
        edu.mit.ilab.ilabs.type.StorageStatus proxyStorageStatus = null;

        if (storageStatus != null) {
            proxyStorageStatus = new edu.mit.ilab.ilabs.type.StorageStatus();
            proxyStorageStatus.setExperimentId(storageStatus.getExperimentId());
            proxyStorageStatus.setStatus(storageStatus.getStatusCode().getValue() + storageStatus.getBatchStatusCode().getValue());
            proxyStorageStatus.setRecordCount(storageStatus.getRecordCount());
            proxyStorageStatus.setIssuerGuid(storageStatus.getIssuerGuid());
            proxyStorageStatus.setCreationTime(Convert(storageStatus.getDateCreated()));
            proxyStorageStatus.setLastModified(Convert(storageStatus.getDateModified()));
            proxyStorageStatus.setCloseTime(Convert(storageStatus.getDateClosed()));
        }

        return proxyStorageStatus;
    }
}
