/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uq.ilabs.library.experimentstorage;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import uq.ilabs.library.datatypes.Criterion;
import uq.ilabs.library.datatypes.batch.StatusCodes;
import uq.ilabs.library.datatypes.storage.Blob;
import uq.ilabs.library.datatypes.storage.Experiment;
import uq.ilabs.library.datatypes.storage.ExperimentRecord;
import uq.ilabs.library.datatypes.storage.RecordAttribute;
import uq.ilabs.library.datatypes.storage.StorageStatus;
import uq.ilabs.library.datatypes.storage.StorageStatusCodes;
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
     * @param arrayOfInt
     * @return int[]
     */
    public static int[] Convert(edu.mit.ilab.ilabs.experimentstorage.proxy.ArrayOfInt arrayOfInt) {
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
    public static long[] Convert(edu.mit.ilab.ilabs.experimentstorage.proxy.ArrayOfLong arrayOfLong) {
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
    public static String[] Convert(edu.mit.ilab.ilabs.experimentstorage.proxy.ArrayOfString arrayOfString) {
        String[] strings = null;

        if (arrayOfString != null) {
            strings = arrayOfString.getString().toArray(new String[0]);
        }

        return strings;
    }

    /**
     *
     * @param proxyBlob
     * @return Blob
     */
    public static Blob Convert(edu.mit.ilab.ilabs.experimentstorage.proxy.Blob proxyBlob) {
        Blob blob = null;

        if (proxyBlob != null) {
            blob = new Blob();
            blob.setBlobId(proxyBlob.getBlobId());
            blob.setExperimentId(proxyBlob.getExperimentId());
            blob.setRecordNumber(proxyBlob.getRecordNumber());
            blob.setDateCreated(Convert(proxyBlob.getTimestamp()));
            blob.setDescription(proxyBlob.getDescription());
            blob.setMimeType(proxyBlob.getMimeType());
            blob.setByteCount(proxyBlob.getByteCount());
            blob.setChecksum(proxyBlob.getChecksum());
            blob.setChecksumAlgorithm(proxyBlob.getChecksumAlgorithm());
        }

        return blob;
    }

    /**
     *
     * @param arrayOfBlob
     * @return Blob[]
     */
    public static Blob[] Convert(edu.mit.ilab.ilabs.experimentstorage.proxy.ArrayOfBlob arrayOfBlob) {
        Blob[] blobs = null;

        if (arrayOfBlob != null) {
            edu.mit.ilab.ilabs.experimentstorage.proxy.Blob[] proxyBlobs = arrayOfBlob.getBlob().toArray(new edu.mit.ilab.ilabs.experimentstorage.proxy.Blob[0]);
            if (proxyBlobs != null) {
                blobs = new Blob[proxyBlobs.length];
                for (int i = 0; i < proxyBlobs.length; i++) {
                    blobs[i] = Convert(proxyBlobs[i]);
                }
            }
        }

        return blobs;
    }

    /**
     *
     * @param proxyExperiment
     * @return Experiment
     */
    public static Experiment Convert(edu.mit.ilab.ilabs.experimentstorage.proxy.Experiment proxyExperiment) {
        Experiment experiment = null;

        if (proxyExperiment != null) {
            experiment = new Experiment();
            experiment.setExperimentId(proxyExperiment.getExperimentId());
            experiment.setIssuerGuid(proxyExperiment.getIssuerGuid());
            experiment.setRecords(Convert(proxyExperiment.getRecords()));
        }

        return experiment;
    }

    /**
     *
     * @param proxyExperimentRecord
     * @return ExperimentRecord
     */
    public static ExperimentRecord Convert(edu.mit.ilab.ilabs.experimentstorage.proxy.ExperimentRecord proxyExperimentRecord) {
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
    public static ExperimentRecord[] Convert(edu.mit.ilab.ilabs.experimentstorage.proxy.ArrayOfExperimentRecord arrayOfExperimentRecord) {
        ExperimentRecord[] experimentRecords = null;

        if (arrayOfExperimentRecord != null) {
            edu.mit.ilab.ilabs.experimentstorage.proxy.ExperimentRecord[] proxyExperimentRecords = arrayOfExperimentRecord.getExperimentRecord().toArray(new edu.mit.ilab.ilabs.experimentstorage.proxy.ExperimentRecord[0]);
            if (proxyExperimentRecords != null) {
                experimentRecords = new ExperimentRecord[proxyExperimentRecords.length];
                for (int i = 0; i < proxyExperimentRecords.length; i++) {
                    experimentRecords[i] = Convert(proxyExperimentRecords[i]);
                }
            }
        }

        return experimentRecords;
    }

    /**
     *
     * @param arrayOfExperimentRecord
     * @return ExperimentRecord[]
     */
    public static ExperimentRecord[] Convert(edu.mit.ilab.ilabs.experimentstorage.proxy.ArrayOfExperimentRecord1 arrayOfExperimentRecord) {
        ExperimentRecord[] experimentRecords = null;

        if (arrayOfExperimentRecord != null) {
            edu.mit.ilab.ilabs.experimentstorage.proxy.ExperimentRecord[] proxyExperimentRecords = arrayOfExperimentRecord.getExperimentRecord().toArray(new edu.mit.ilab.ilabs.experimentstorage.proxy.ExperimentRecord[0]);
            if (proxyExperimentRecords != null) {
                experimentRecords = new ExperimentRecord[proxyExperimentRecords.length];
                for (int i = 0; i < proxyExperimentRecords.length; i++) {
                    experimentRecords[i] = Convert(proxyExperimentRecords[i]);
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
    public static RecordAttribute Convert(edu.mit.ilab.ilabs.experimentstorage.proxy.RecordAttribute proxyRecordAttribute) {
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
    public static RecordAttribute[] Convert(edu.mit.ilab.ilabs.experimentstorage.proxy.ArrayOfRecordAttribute arrayOfRecordAttribute) {
        RecordAttribute[] recordAttributes = null;

        if (arrayOfRecordAttribute != null) {
            edu.mit.ilab.ilabs.experimentstorage.proxy.RecordAttribute[] proxyRecordAttributes = arrayOfRecordAttribute.getRecordAttribute().toArray(new edu.mit.ilab.ilabs.experimentstorage.proxy.RecordAttribute[0]);
            if (proxyRecordAttributes != null) {
                recordAttributes = new RecordAttribute[proxyRecordAttributes.length];
                for (int i = 0; i < proxyRecordAttributes.length; i++) {
                    recordAttributes[i] = Convert(proxyRecordAttributes[i]);
                }
            }
        }

        return recordAttributes;
    }

    /**
     *
     * @param proxyStorageStatus
     * @return StorageStatus
     */
    public static StorageStatus Convert(edu.mit.ilab.ilabs.experimentstorage.proxy.StorageStatus proxyStorageStatus) {
        StorageStatus storageStatus = null;

        if (proxyStorageStatus != null) {
            storageStatus = new StorageStatus();
            storageStatus.setExperimentId(proxyStorageStatus.getExperimentId());
            storageStatus.setStatusCode(StorageStatusCodes.ToStorageStatusCode(proxyStorageStatus.getStatus()));
            storageStatus.setBatchStatusCode(StatusCodes.ToStatusCode(proxyStorageStatus.getStatus()));
            storageStatus.setRecordCount(proxyStorageStatus.getRecordCount());
            storageStatus.setDateCreated(Convert(proxyStorageStatus.getCreationTime()));
            storageStatus.setDateClosed(Convert(proxyStorageStatus.getCloseTime()));
            storageStatus.setDateModified(Convert(proxyStorageStatus.getLastModified()));
            storageStatus.setIssuerGuid(proxyStorageStatus.getIssuerGuid());
        }

        return storageStatus;
    }

    /**
     *
     * @param ints
     * @return edu.mit.ilab.ilabs.experimentstorage.proxy.ArrayOfInt
     */
    public static edu.mit.ilab.ilabs.experimentstorage.proxy.ArrayOfInt Convert(int[] ints) {
        edu.mit.ilab.ilabs.experimentstorage.proxy.ArrayOfInt arrayOfInt = null;

        if (ints != null) {
            arrayOfInt = new edu.mit.ilab.ilabs.experimentstorage.proxy.ArrayOfInt();
            for (int i = 0; i < ints.length; i++) {
                arrayOfInt.getInt().add(Integer.valueOf(ints[i]));
            }
        }

        return arrayOfInt;
    }

    /**
     *
     * @param longs
     * @return edu.mit.ilab.ilabs.experimentstorage.proxy.ArrayOfLong
     */
    public static edu.mit.ilab.ilabs.experimentstorage.proxy.ArrayOfLong Convert(long[] longs) {
        edu.mit.ilab.ilabs.experimentstorage.proxy.ArrayOfLong arrayOfLong = null;

        if (longs != null) {
            arrayOfLong = new edu.mit.ilab.ilabs.experimentstorage.proxy.ArrayOfLong();
            for (int i = 0; i < longs.length; i++) {
                arrayOfLong.getLong().add(Long.valueOf(longs[i]));
            }
        }

        return arrayOfLong;
    }

    /**
     *
     * @param coupon
     * @return edu.mit.ilab.ilabs.experimentstorage.proxy.Coupon
     */
    public static edu.mit.ilab.ilabs.experimentstorage.proxy.Coupon Convert(Coupon coupon) {
        edu.mit.ilab.ilabs.experimentstorage.proxy.Coupon proxyCoupon = null;

        if (coupon != null) {
            proxyCoupon = new edu.mit.ilab.ilabs.experimentstorage.proxy.Coupon();
            proxyCoupon.setCouponId(coupon.getCouponId());
            proxyCoupon.setIssuerGuid(coupon.getIssuerGuid());
            proxyCoupon.setPasskey(coupon.getPasskey());
        }

        return proxyCoupon;
    }

    /**
     *
     * @param criterion
     * @return edu.mit.ilab.ilabs.experimentstorage.proxy.Criterion
     */
    public static edu.mit.ilab.ilabs.experimentstorage.proxy.Criterion Convert(Criterion criterion) {
        edu.mit.ilab.ilabs.experimentstorage.proxy.Criterion proxyCriterion = null;

        if (criterion != null) {
            proxyCriterion = new edu.mit.ilab.ilabs.experimentstorage.proxy.Criterion();
            proxyCriterion.setAttribute(criterion.getAttribute());
            proxyCriterion.setPredicate(criterion.getPredicate());
            proxyCriterion.setValue(criterion.getValue());
        }

        return proxyCriterion;
    }

    /**
     *
     * @param criterions
     * @return ArrayOfCriterion
     */
    public static edu.mit.ilab.ilabs.experimentstorage.proxy.ArrayOfCriterion Convert(Criterion[] criterions) {
        edu.mit.ilab.ilabs.experimentstorage.proxy.ArrayOfCriterion arrayOfCriterion = null;

        if (criterions != null) {
            arrayOfCriterion = new edu.mit.ilab.ilabs.experimentstorage.proxy.ArrayOfCriterion();
            for (int i = 0; i < criterions.length; i++) {
                arrayOfCriterion.getCriterion().add(Convert(criterions[i]));
            }
        }

        return arrayOfCriterion;
    }

    /**
     *
     * @param experimentRecord
     * @return edu.mit.ilab.ilabs.experimentstorage.proxy.ExperimentRecord
     */
    public static edu.mit.ilab.ilabs.experimentstorage.proxy.ExperimentRecord Convert(ExperimentRecord experimentRecord) {
        edu.mit.ilab.ilabs.experimentstorage.proxy.ExperimentRecord proxyExperimentRecord = null;

        if (experimentRecord != null) {
            proxyExperimentRecord = new edu.mit.ilab.ilabs.experimentstorage.proxy.ExperimentRecord();
            proxyExperimentRecord.setType(experimentRecord.getRecordType());
            proxyExperimentRecord.setSubmitter(experimentRecord.getSubmitter());
            proxyExperimentRecord.setSequenceNum(experimentRecord.getSequenceNum());
            proxyExperimentRecord.setTimestamp(Convert(experimentRecord.getDateCreated()));
            proxyExperimentRecord.setXmlSearchable(experimentRecord.isXmlSearchable());
            proxyExperimentRecord.setContents(experimentRecord.getContents());
        }

        return proxyExperimentRecord;
    }

    /**
     *
     * @param experimentRecords
     * @return ArrayOfExperimentRecord
     */
    public static edu.mit.ilab.ilabs.experimentstorage.proxy.ArrayOfExperimentRecord Convert(ExperimentRecord[] experimentRecords) {
        edu.mit.ilab.ilabs.experimentstorage.proxy.ArrayOfExperimentRecord arrayOfExperimentRecord = null;

        if (experimentRecords != null) {
            arrayOfExperimentRecord = new edu.mit.ilab.ilabs.experimentstorage.proxy.ArrayOfExperimentRecord();
            for (int i = 0; i < experimentRecords.length; i++) {
                arrayOfExperimentRecord.getExperimentRecord().add(Convert(experimentRecords[i]));
            }
        }

        return arrayOfExperimentRecord;
    }

    /**
     *
     * @param recordAttribute
     * @return edu.mit.ilab.ilabs.experimentstorage.proxy.RecordAttribute
     */
    public static edu.mit.ilab.ilabs.experimentstorage.proxy.RecordAttribute Convert(RecordAttribute recordAttribute) {
        edu.mit.ilab.ilabs.experimentstorage.proxy.RecordAttribute proxyRecordAttribute = null;

        if (recordAttribute != null) {
            proxyRecordAttribute = new edu.mit.ilab.ilabs.experimentstorage.proxy.RecordAttribute();
            proxyRecordAttribute.setName(recordAttribute.getName());
            proxyRecordAttribute.setValue(recordAttribute.getValue());
        }

        return proxyRecordAttribute;
    }

    /**
     *
     * @param recordAttributes
     * @return ArrayOfRecordAttribute
     */
    public static edu.mit.ilab.ilabs.experimentstorage.proxy.ArrayOfRecordAttribute Convert(RecordAttribute[] recordAttributes) {
        edu.mit.ilab.ilabs.experimentstorage.proxy.ArrayOfRecordAttribute arrayOfRecordAttribute = null;

        if (recordAttributes != null) {
            arrayOfRecordAttribute = new edu.mit.ilab.ilabs.experimentstorage.proxy.ArrayOfRecordAttribute();
            for (int i = 0; i < recordAttributes.length; i++) {
                arrayOfRecordAttribute.getRecordAttribute().add(Convert(recordAttributes[i]));
            }
        }

        return arrayOfRecordAttribute;
    }
}
