/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uq.ilabs.library.experimentstorage.database;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.logging.Level;
import uq.ilabs.library.experimentstorage.database.types.ExperimentRecordInfo;
import uq.ilabs.library.lab.database.DBConnection;
import uq.ilabs.library.lab.utilities.Logfile;

/**
 *
 * @author uqlpayne
 */
public class ExperimentRecordsDB {

    //<editor-fold defaultstate="collapsed" desc="Constants">
    private static final String STR_ClassName = ExperimentRecordsDB.class.getName();
    private static final Level logLevel = Level.FINEST;
    /*
     * String constants for logfile messages
     */
    private static final String STRLOG_Id_arg = "Id: %d";
    private static final String STRLOG_Count_arg = "Count: %d";
    private static final String STRLOG_Success_arg = "Success: %s";
    /*
     * String constants for SQL processing
     */
    private static final String STRSQLCMD_Add = "{ ? = call ExperimentRecords_Add(?,?,?,?,?) }";
    private static final String STRSQLCMD_Delete = "{ ? = call ExperimentRecords_Delete(?) }";
    private static final String STRSQLCMD_RetrieveBy = "{ call ExperimentRecords_RetrieveBy(?,?,?,?) }";
    /*
     * Database column names
     */
    private static final String STRCOL_Id = "Id";
    private static final String STRCOL_ExperimentId = "ExperimentId";
    private static final String STRCOL_Submitter = "Submitter";
    private static final String STRCOL_RecordType = "RecordType";
    private static final String STRCOL_XmlSearchable = "XmlSearchable";
    private static final String STRCOL_SequenceNum = "SequenceNum";
    private static final String STRCOL_DateCreated = "DateCreated";
    private static final String STRCOL_Contents = "Contents";
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Variables">
    private DBConnection dbConnection;
    //</editor-fold>

    /**
     *
     * @param dbConnection
     * @throws Exception
     */
    public ExperimentRecordsDB(DBConnection dbConnection) throws Exception {
        final String methodName = "ExperimentRecordsDB";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        /*
         * Check that parameters are valid
         */
        if (dbConnection == null) {
            throw new NullPointerException(DBConnection.class.getSimpleName());
        }

        /*
         * Initialise locals
         */
        this.dbConnection = dbConnection;

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);
    }

    /**
     *
     * @param experimentRecordInfo
     * @return long
     * @throws Exception
     */
    public long Add(ExperimentRecordInfo experimentRecordInfo) throws Exception {
        final String methodName = "Add";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        long id = -1;

        try {
            /*
             * Check that parameters are valid
             */
            if (experimentRecordInfo == null) {
                throw new NullPointerException(ExperimentRecordInfo.class.getSimpleName());
            }

            Connection sqlConnection = this.dbConnection.getConnection();
            CallableStatement sqlStatement = null;

            try {
                /*
                 * Prepare the stored procedure call
                 */
                sqlStatement = sqlConnection.prepareCall(STRSQLCMD_Add);
                sqlStatement.registerOutParameter(1, Types.BIGINT);
                sqlStatement.setLong(2, experimentRecordInfo.getExperimentId());
                sqlStatement.setString(3, experimentRecordInfo.getSubmitter());
                sqlStatement.setString(4, experimentRecordInfo.getRecordType());
                sqlStatement.setBoolean(5, experimentRecordInfo.isXmlSearchable());
                sqlStatement.setString(6, experimentRecordInfo.getContents());

                /*
                 * Execute the stored procedure
                 */
                sqlStatement.execute();

                /*
                 * Get the result
                 */
                id = (long) sqlStatement.getLong(1);
            } catch (Exception ex) {
                throw ex;
            } finally {
                try {
                    if (sqlStatement != null) {
                        sqlStatement.close();
                    }
                } catch (SQLException ex) {
                    Logfile.WriteException(STR_ClassName, methodName, ex);
                }
                this.dbConnection.putConnection(sqlConnection);
            }
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
            throw ex;
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName,
                String.format(STRLOG_Id_arg, id));

        return id;
    }

    /**
     *
     * @param entryId
     * @return
     */
    public boolean Delete(long id) {
        final String methodName = "Delete";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        boolean success = false;

        try {
            Connection sqlConnection = this.dbConnection.getConnection();
            CallableStatement sqlStatement = null;

            try {
                /*
                 * Prepare the stored procedure call
                 */
                sqlStatement = sqlConnection.prepareCall(STRSQLCMD_Delete);
                sqlStatement.registerOutParameter(1, Types.BIGINT);
                sqlStatement.setLong(2, id);

                /*
                 * Execute the stored procedure
                 */
                sqlStatement.execute();

                /*
                 * Get the result
                 */
                success = (sqlStatement.getLong(1) > 0);
            } catch (Exception ex) {
                throw ex;
            } finally {
                try {
                    if (sqlStatement != null) {
                        sqlStatement.close();
                    }
                } catch (SQLException ex) {
                    Logfile.WriteException(STR_ClassName, methodName, ex);
                }
                this.dbConnection.putConnection(sqlConnection);
            }
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName,
                String.format(STRLOG_Success_arg, success));

        return success;
    }

    /**
     *
     * @param id
     * @return ExperimentRecordInfo
     */
    public ExperimentRecordInfo RetrieveById(long id) {
        ArrayList<ExperimentRecordInfo> list = this.RetrieveBy(STRCOL_Id, id, null, 0);
        return (list != null) ? list.get(0) : null;
    }

    /**
     *
     * @param experimentId
     * @param issuerGuid
     * @return ExperimentRecordInfo
     */
    public ExperimentRecordInfo[] Retrieve(long experimentId) {
        ArrayList<ExperimentRecordInfo> list = this.RetrieveBy(STRCOL_ExperimentId, experimentId, null, 0);
        ExperimentRecordInfo[] experimentRecordInfos = (list.size() > 0) ? list.toArray(new ExperimentRecordInfo[list.size()]) : null;
        return experimentRecordInfos;
    }

    /**
     *
     * @param experimentId
     * @param issuerGuid
     * @param sequenceNum
     * @return ExperimentRecordInfo
     */
    public ExperimentRecordInfo Retrieve(long experimentId, int sequenceNum) {
        ArrayList<ExperimentRecordInfo> list = this.RetrieveBy(STRCOL_SequenceNum, experimentId, null, sequenceNum);
        return (list != null) ? list.get(0) : null;
    }

    //================================================================================================================//
    /**
     *
     * @param columnName
     * @param longval
     * @param strval
     * @param intval
     * @return ExperimentRecordInfo
     */
    private ArrayList<ExperimentRecordInfo> RetrieveBy(String columnName, long longval, String strval, int intval) {
        final String methodName = "RetrieveBy";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        ArrayList<ExperimentRecordInfo> list = new ArrayList<>();

        try {
            Connection sqlConnection = this.dbConnection.getConnection();
            CallableStatement sqlStatement = null;

            try {
                /*
                 * Prepare the stored procedure call
                 */
                sqlStatement = sqlConnection.prepareCall(STRSQLCMD_RetrieveBy);
                sqlStatement.setString(1, columnName);
                sqlStatement.setLong(2, longval);
                sqlStatement.setString(3, strval);
                sqlStatement.setInt(4, intval);

                /*
                 * Execute the stored procedure
                 */
                ResultSet resultSet = sqlStatement.executeQuery();

                /*
                 * Process the results of the query - only want the first result
                 */
                while (resultSet.next() == true) {
                    ExperimentRecordInfo experimentRecordInfo = new ExperimentRecordInfo();
                    experimentRecordInfo.setId(resultSet.getLong(STRCOL_Id));
                    experimentRecordInfo.setExperimentId(resultSet.getLong(STRCOL_ExperimentId));
                    experimentRecordInfo.setSubmitter(resultSet.getString(STRCOL_Submitter));
                    experimentRecordInfo.setRecordType(resultSet.getString(STRCOL_RecordType));
                    experimentRecordInfo.setXmlSearchable(resultSet.getBoolean(STRCOL_XmlSearchable));
                    experimentRecordInfo.setSequenceNum(resultSet.getInt(STRCOL_SequenceNum));
                    experimentRecordInfo.setContents(resultSet.getString(STRCOL_Contents));

                    Timestamp timestamp = resultSet.getTimestamp(STRCOL_DateCreated);
                    if (timestamp != null) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(timestamp);
                        experimentRecordInfo.setDateCreated(calendar);
                    }

                    /*
                     * Add the info to the list
                     */
                    list.add(experimentRecordInfo);
                }
            } catch (Exception ex) {
                throw ex;
            } finally {
                try {
                    if (sqlStatement != null) {
                        sqlStatement.close();
                    }
                } catch (SQLException ex) {
                    Logfile.WriteException(STR_ClassName, methodName, ex);
                }
                this.dbConnection.putConnection(sqlConnection);
            }
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName,
                String.format(STRLOG_Count_arg, list.size()));

        return (list.size() > 0) ? list : null;
    }
}
