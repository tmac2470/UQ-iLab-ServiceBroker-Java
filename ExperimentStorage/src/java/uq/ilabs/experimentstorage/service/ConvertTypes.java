/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uq.ilabs.experimentstorage.service;

import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import uq.ilabs.library.datatypes.Criterion;
import uq.ilabs.library.datatypes.processagent.ProcessAgent;
import uq.ilabs.library.datatypes.processagent.ProcessAgentTypes;
import uq.ilabs.library.datatypes.processagent.ServiceDescription;
import uq.ilabs.library.datatypes.processagent.StatusNotificationReport;
import uq.ilabs.library.datatypes.processagent.StatusReport;
import uq.ilabs.library.datatypes.storage.Blob;
import uq.ilabs.library.datatypes.storage.Experiment;
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
    public static Coupon ConvertType(edu.mit.ilab.ilabs.type.Coupon proxyCoupon) {
        Coupon coupon = null;

        if (proxyCoupon != null) {
            coupon = new Coupon(proxyCoupon.getCouponId(), proxyCoupon.getIssuerGuid(), proxyCoupon.getPasskey());
        }

        return coupon;
    }

    /**
     *
     * @param proxyCriterion
     * @return Criterion
     */
    public static Criterion ConvertType(edu.mit.ilab.ilabs.type.Criterion proxyCriterion) {
        Criterion criterion = null;

        if (proxyCriterion != null) {
            criterion = new Criterion();
            criterion.setAttribute(proxyCriterion.getAttribute());
            criterion.setPredicate(proxyCriterion.getPredicate());
            criterion.setValue(proxyCriterion.getValue());
        }

        return criterion;
    }

    /**
     *
     * @param proxyExperimentRecord
     * @return ExperimentRecord
     */
    public static ExperimentRecord ConvertType(edu.mit.ilab.ilabs.type.ExperimentRecord proxyExperimentRecord) {
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
     * @param proxyRecordAttribute
     * @return RecordAttribute
     */
    public static RecordAttribute ConvertType(edu.mit.ilab.ilabs.type.RecordAttribute proxyRecordAttribute) {
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
     * @param blob
     * @return edu.mit.ilab.ilabs.type.Blob
     */
    public static edu.mit.ilab.ilabs.type.Blob ConvertType(Blob blob) {
        edu.mit.ilab.ilabs.type.Blob proxyBlob = null;

        if (blob != null) {
            proxyBlob = new edu.mit.ilab.ilabs.type.Blob();
            proxyBlob.setBlobId(blob.getBlobId());
            proxyBlob.setByteCount(blob.getByteCount());
            proxyBlob.setChecksum(blob.getChecksum());
            proxyBlob.setChecksumAlgorithm(blob.getChecksumAlgorithm());
            proxyBlob.setDescription(blob.getDescription());
            proxyBlob.setExperimentId(blob.getExperimentId());
            proxyBlob.setMimeType(blob.getMimeType());
            proxyBlob.setRecordNumber(blob.getRecordNumber());
            proxyBlob.setTimestamp(Convert(blob.getDateCreated()));
        }

        return proxyBlob;
    }

    /**
     *
     * @param criterion
     * @return edu.mit.ilab.ilabs.type.Criterion
     */
    public static edu.mit.ilab.ilabs.type.Criterion ConvertType(Criterion criterion) {
        edu.mit.ilab.ilabs.type.Criterion proxyCriterion = null;

        if (criterion != null) {
            proxyCriterion = new edu.mit.ilab.ilabs.type.Criterion();
            proxyCriterion.setAttribute(criterion.getAttribute());
            proxyCriterion.setPredicate(criterion.getPredicate());
            proxyCriterion.setValue(criterion.getValue());
        }

        return proxyCriterion;
    }

    /**
     *
     * @param experiment
     * @return edu.mit.ilab.ilabs.type.Experiment
     */
    public static edu.mit.ilab.ilabs.type.Experiment ConvertType(Experiment experiment) {
        edu.mit.ilab.ilabs.type.Experiment proxyExperiment = null;

        if (experiment != null) {
            proxyExperiment = new edu.mit.ilab.ilabs.type.Experiment();
            proxyExperiment.setExperimentId(experiment.getExperimentId());
            proxyExperiment.setIssuerGuid(experiment.getIssuerGuid());
            proxyExperiment.setRecords(ConvertType(experiment.getRecords()));
        }

        return proxyExperiment;
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
            proxyExperimentRecord.setTimestamp(Convert(experimentRecord.getDateCreated()));
        }

        return proxyExperimentRecord;
    }

    /**
     *
     * @param experimentRecords
     * @return edu.mit.ilab.ilabs.type.ArrayOfExperimentRecord
     */
    public static edu.mit.ilab.ilabs.type.ArrayOfExperimentRecord ConvertType(ExperimentRecord[] experimentRecords) {
        edu.mit.ilab.ilabs.type.ArrayOfExperimentRecord arrayOfExperimentRecord = null;

        if (experimentRecords != null) {
            arrayOfExperimentRecord = new edu.mit.ilab.ilabs.type.ArrayOfExperimentRecord();
            for (ExperimentRecord experimentRecord : experimentRecords) {
                arrayOfExperimentRecord.getExperimentRecord().add(ConvertType(experimentRecord));
            }
        }

        return arrayOfExperimentRecord;
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
     * @param recordAttribute
     * @return edu.mit.ilab.ilabs.type.RecordAttribute
     */
    public static edu.mit.ilab.ilabs.type.RecordAttribute ConvertType(RecordAttribute recordAttribute) {
        edu.mit.ilab.ilabs.type.RecordAttribute proxyRecordAttribute = null;

        if (recordAttribute != null) {
            proxyRecordAttribute = new edu.mit.ilab.ilabs.type.RecordAttribute();
            proxyRecordAttribute.setName(recordAttribute.getName());
            proxyRecordAttribute.setValue(recordAttribute.getValue());
        }

        return proxyRecordAttribute;
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

    /**
     *
     * @param arrayOfInt
     * @return int[]
     */
    public static int[] ConvertServices(edu.mit.ilab.ilabs.services.ArrayOfInt arrayOfInt) {
        int[] ints = null;

        if (arrayOfInt != null) {
            Integer[] integerArray = arrayOfInt.getInt().toArray(new Integer[0]);
            if (integerArray != null) {
                ints = new int[integerArray.length];
                for (int i = 0; i < integerArray.length; i++) {
                    ints[i] = integerArray[i].intValue();
                }
            }
        }

        return ints;
    }

    /**
     *
     * @param arrayOfLong
     * @return long[]
     */
    public static long[] ConvertServices(edu.mit.ilab.ilabs.services.ArrayOfLong arrayOfLong) {
        long[] longs = null;

        if (arrayOfLong != null) {
            Long[] longArray = arrayOfLong.getLong().toArray(new Long[0]);
            if (longArray != null) {
                longs = new long[longArray.length];
                for (int i = 0; i < longArray.length; i++) {
                    longs[i] = longArray[i].intValue();
                }
            }
        }

        return longs;
    }

    /**
     *
     * @param arrayOfString
     * @return String[]
     */
    public static String[] ConvertServices(edu.mit.ilab.ilabs.services.ArrayOfString arrayOfString) {
        String[] strings = null;

        if (arrayOfString != null) {
            strings = arrayOfString.getString().toArray(new String[0]);
        }

        return strings;
    }

    /**
     *
     * @param arrayOfCriterion
     * @return Criterion[]
     */
    public static Criterion[] ConvertServices(edu.mit.ilab.ilabs.services.ArrayOfCriterion arrayOfCriterion) {
        Criterion[] criterions = null;

        if (arrayOfCriterion != null) {
            edu.mit.ilab.ilabs.type.Criterion[] proxyCriterions = arrayOfCriterion.getCriterion().toArray(new edu.mit.ilab.ilabs.type.Criterion[0]);
            if (proxyCriterions != null) {
                criterions = new Criterion[proxyCriterions.length];
                for (int i = 0; i < proxyCriterions.length; i++) {
                    criterions[i] = ConvertType(proxyCriterions[i]);
                }
            }
        }

        return criterions;
    }

    /**
     *
     * @param arrayOfExperimentRecord
     * @return ExperimentRecord[]
     */
    public static ExperimentRecord[] ConvertServices(edu.mit.ilab.ilabs.services.ArrayOfExperimentRecord arrayOfExperimentRecord) {
        ExperimentRecord[] experimentRecords = null;

        if (arrayOfExperimentRecord != null) {
            List<edu.mit.ilab.ilabs.type.ExperimentRecord> listProxyExperimentRecord = arrayOfExperimentRecord.getExperimentRecord();
            if (listProxyExperimentRecord.size() > 0) {
                experimentRecords = new ExperimentRecord[listProxyExperimentRecord.size()];
                for (int i = 0; i < experimentRecords.length; i++) {
                    experimentRecords[i] = ConvertType(listProxyExperimentRecord.get(i));
                }
            }
        }

        return experimentRecords;
    }

    /**
     *
     * @param arrayOfRecordAttribute
     * @return RecordAttribute[]
     */
    public static RecordAttribute[] ConvertServices(edu.mit.ilab.ilabs.services.ArrayOfRecordAttribute arrayOfRecordAttribute) {
        RecordAttribute[] recordAttribute = null;

        if (arrayOfRecordAttribute != null) {
            List<edu.mit.ilab.ilabs.type.RecordAttribute> listProxyRecordAttribute = arrayOfRecordAttribute.getRecordAttribute();
            if (listProxyRecordAttribute.size() > 0) {
                recordAttribute = new RecordAttribute[listProxyRecordAttribute.size()];
                for (int i = 0; i < recordAttribute.length; i++) {
                    recordAttribute[i] = ConvertType(listProxyRecordAttribute.get(i));
                }
            }
        }

        return recordAttribute;
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
     * @param ints
     * @return edu.mit.ilab.ilabs.services.ArrayOfInt
     */
    public static edu.mit.ilab.ilabs.services.ArrayOfInt ConvertServices(int[] ints) {
        edu.mit.ilab.ilabs.services.ArrayOfInt arrayOfInt = null;

        if (ints != null) {
            arrayOfInt = new edu.mit.ilab.ilabs.services.ArrayOfInt();
            for (int i = 0; i < ints.length; i++) {
                arrayOfInt.getInt().add(Integer.valueOf(ints[i]));
            }
        }

        return arrayOfInt;
    }

    /**
     *
     * @param longs
     * @return edu.mit.ilab.ilabs.services.ArrayOfLong
     */
    public static edu.mit.ilab.ilabs.services.ArrayOfLong ConvertServices(long[] longs) {
        edu.mit.ilab.ilabs.services.ArrayOfLong arrayOfLong = null;

        if (longs != null) {
            arrayOfLong = new edu.mit.ilab.ilabs.services.ArrayOfLong();
            for (int i = 0; i < longs.length; i++) {
                arrayOfLong.getLong().add(Long.valueOf(longs[i]));
            }
        }

        return arrayOfLong;
    }

    /**
     *
     * @param strings
     * @return
     */
    public static edu.mit.ilab.ilabs.services.ArrayOfString ConvertServices(String[] strings) {
        edu.mit.ilab.ilabs.services.ArrayOfString arrayOfString = null;

        if (strings != null) {
            arrayOfString = new edu.mit.ilab.ilabs.services.ArrayOfString();
            arrayOfString.getString().addAll(Arrays.asList(strings));
        }

        return arrayOfString;
    }

    /**
     *
     * @param blobs
     * @return edu.mit.ilab.ilabs.services.ArrayOfBlob
     */
    public static edu.mit.ilab.ilabs.services.ArrayOfBlob ConvertServices(Blob[] blobs) {
        edu.mit.ilab.ilabs.services.ArrayOfBlob arrayOfBlob = null;

        if (blobs != null) {
            arrayOfBlob = new edu.mit.ilab.ilabs.services.ArrayOfBlob();
            for (Blob blob : blobs) {
                arrayOfBlob.getBlob().add(ConvertType(blob));
            }
        }

        return arrayOfBlob;
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

    /**
     *
     * @param recordAttributes
     * @return edu.mit.ilab.ilabs.services.ArrayOfRecordAttribute
     */
    public static edu.mit.ilab.ilabs.services.ArrayOfRecordAttribute ConvertServices(RecordAttribute[] recordAttributes) {
        edu.mit.ilab.ilabs.services.ArrayOfRecordAttribute arrayOfRecordAttribute = null;

        if (recordAttributes != null) {
            arrayOfRecordAttribute = new edu.mit.ilab.ilabs.services.ArrayOfRecordAttribute();
            for (int i = 0; i < recordAttributes.length; i++) {
                arrayOfRecordAttribute.getRecordAttribute().add(ConvertType(recordAttributes[i]));
            }
        }

        return arrayOfRecordAttribute;
    }
}
