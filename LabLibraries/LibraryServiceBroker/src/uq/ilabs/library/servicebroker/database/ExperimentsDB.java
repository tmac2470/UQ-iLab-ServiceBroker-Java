/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uq.ilabs.library.servicebroker.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.logging.Level;
import uq.ilabs.library.datatypes.batch.StatusCodes;
import uq.ilabs.library.datatypes.storage.StorageStatusCodes;
import uq.ilabs.library.lab.database.DBConnection;
import uq.ilabs.library.lab.utilities.Logfile;
import uq.ilabs.library.servicebroker.database.types.ExperimentInfo;

/**
 *
 * @author uqlpayne
 */
public class ExperimentsDB {

    //<editor-fold defaultstate="collapsed" desc="Constants">
    public static final String STR_ClassName = ExperimentsDB.class.getName();
    private static final Level logLevel = Level.FINEST;
    /*
     * String constants for logfile messages
     */
    private static final String STRLOG_ExperimentId_arg = "experimentId: %s";
    private static final String STRLOG_Count_arg = "Count: %d";
    private static final String STRLOG_Success_arg = "Success: %s";
    /*
     * Database column names
     */
    private static final String STRCOL_ExperimentId = "ExperimentId";
    private static final String STRCOL_StatusCode = "StatusCode";
    private static final String STRCOL_BatchStatusCode = "BatchStatusCode";
    private static final String STRCOL_CouponId = "CouponId";
    private static final String STRCOL_UserId = "UserId";
    private static final String STRCOL_GroupId = "GroupId";
    private static final String STRCOL_AgentId = "AgentId";
    private static final String STRCOL_ClientId = "ClientId";
    private static final String STRCOL_EssId = "EssId";
    private static final String STRCOL_RecordCount = "RecordCount";
    private static final String STRCOL_DateToStart = "DateToStart";
    private static final String STRCOL_Duration = "Duration";
    private static final String STRCOL_DateCreated = "DateCreated";
    private static final String STRCOL_DateClosed = "DateClosed";
    private static final String STRCOL_Annotation = "Annotation";
    /*
     * String constants for SQL processing
     */
    private static final String STRSQLCMD_Add = "{ ? = call Experiments_Add(?,?,?,?,?,?,?,?,?,?) }";
    private static final String STRSQLCMD_Delete = "{ ? = call Experiments_Delete(?) }";
    private static final String STRSQLCMD_RetrieveBy = "{ call Experiments_RetrieveBy(?,?,?) }";
    private static final String STRSQLCMD_UpdateAnnotation = "{ ? = call Experiments_UpdateAnnotation(?,?) }";
    private static final String STRSQLCMD_UpdateStatus = "{ ? = call Experiments_UpdateStatus(?,?,?) }";
    private static final String STRSQLCMD_UpdateStatusClose = "{ ? = call Experiments_UpdateStatusClose(?,?,?,?) }";
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Variables">
    private DBConnection dbConnection;
    //</editor-fold>

    /**
     *
     * @param dbConnection
     */
    public ExperimentsDB(DBConnection dbConnection) throws SQLException {
        final String STR_MethodName = "ExperimentsDB";
        Logfile.WriteCalled(logLevel, STR_ClassName, STR_MethodName);

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

        Logfile.WriteCompleted(logLevel, STR_ClassName, STR_MethodName);
    }

    /**
     *
     * @param experimentInfo
     * @return long
     * @throws Exception
     */
    public long Add(ExperimentInfo experimentInfo) throws Exception {
        final String STR_MethodName = "Add";
        Logfile.WriteCalled(logLevel, STR_ClassName, STR_MethodName);

        long experimentId = -1;

        try {
            /*
             * Check that parameters are valid
             */
            if (experimentInfo == null) {
                throw new NullPointerException(ExperimentInfo.class.getSimpleName());
            }

            Connection sqlConnection = this.dbConnection.getConnection();
            CallableStatement sqlStatement = null;

            try {
                /*
                 * Prepare the stored procedure call
                 */
                sqlStatement = sqlConnection.prepareCall(STRSQLCMD_Add);
                sqlStatement.registerOutParameter(1, Types.BIGINT);
                sqlStatement.setString(2, experimentInfo.getStatusCode().name());
                sqlStatement.setString(3, experimentInfo.getBatchStatusCode().name());
                sqlStatement.setLong(4, experimentInfo.getCouponId());
                sqlStatement.setInt(5, experimentInfo.getUserId());
                sqlStatement.setInt(6, experimentInfo.getGroupId());
                sqlStatement.setInt(7, experimentInfo.getAgentId());
                sqlStatement.setInt(8, experimentInfo.getClientId());
                sqlStatement.setInt(9, experimentInfo.getEssId());
                sqlStatement.setTimestamp(10, experimentInfo.getDateToStart() != null
                        ? new Timestamp(experimentInfo.getDateToStart().getTimeInMillis()) : null);
                sqlStatement.setLong(11, experimentInfo.getDuration());

                /*
                 * Execute the stored procedure
                 */
                sqlStatement.execute();

                /*
                 * Get the experiment Id
                 */
                experimentId = sqlStatement.getLong(1);
            } catch (Exception ex) {
                throw ex;
            } finally {
                try {
                    if (sqlStatement != null) {
                        sqlStatement.close();
                    }
                } catch (SQLException ex) {
                    Logfile.WriteException(STR_ClassName, STR_MethodName, ex);
                }
                this.dbConnection.putConnection(sqlConnection);
            }
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
            throw ex;
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, STR_MethodName,
                String.format(STRLOG_ExperimentId_arg, experimentId));

        return experimentId;
    }

    /**
     *
     * @param experimentId
     * @return boolean
     * @throws Exception
     */
    public boolean Delete(long experimentId) throws Exception {
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
                sqlStatement.setLong(2, experimentId);

                /*
                 * Execute the stored procedure
                 */
                sqlStatement.execute();

                /*
                 * Get the result
                 */
                success = (sqlStatement.getLong(1) == experimentId);
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
                String.format(STRLOG_Success_arg, success));

        return success;
    }

    /**
     *
     * @param experimentId
     * @return ExperimentInfo
     * @throws Exception
     */
    public ExperimentInfo Retrieve(long experimentId) throws Exception {
        ArrayList<ExperimentInfo> list = this.RetrieveBy(STRCOL_ExperimentId, experimentId, null);
        return (list != null) ? list.get(0) : null;
    }

    /**
     *
     * @param userId
     * @return ArrayList of ExperimentInfo
     * @throws Exception
     */
    public ArrayList<ExperimentInfo> RetrieveByUserId(int userId) throws Exception {
        return this.RetrieveBy(STRCOL_UserId, userId, null);
    }

    /**
     *
     * @param experimentId
     * @param annotation
     * @return
     * @throws Exception
     */
    public boolean UpdateAnnotation(long experimentId, String annotation) throws Exception {
        final String methodName = "Update";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        boolean success = false;

        try {
            Connection sqlConnection = this.dbConnection.getConnection();
            CallableStatement sqlStatement = null;

            try {
                /*
                 * Prepare the stored procedure call
                 */
                sqlStatement = sqlConnection.prepareCall(STRSQLCMD_UpdateAnnotation);
                sqlStatement.registerOutParameter(1, Types.BIGINT);
                sqlStatement.setLong(2, experimentId);
                sqlStatement.setString(3, annotation);

                /*
                 * Execute the stored procedure
                 */
                sqlStatement.execute();

                /*
                 * Get the result
                 */
                success = (sqlStatement.getLong(1) == experimentId);
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
                String.format(STRLOG_Success_arg, success));

        return success;
    }

    /**
     *
     * @param experimentId
     * @param statusCode
     * @param batchStatusCode
     * @return
     * @throws Exception
     */
    public boolean UpdateStatus(long experimentId, StorageStatusCodes statusCode, StatusCodes batchStatusCode) throws Exception {
        final String methodName = "UpdateStatus";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        boolean success = false;

        try {
            Connection sqlConnection = this.dbConnection.getConnection();
            CallableStatement sqlStatement = null;

            try {
                /*
                 * Prepare the stored procedure call
                 */
                sqlStatement = sqlConnection.prepareCall(STRSQLCMD_UpdateStatus);
                sqlStatement.registerOutParameter(1, Types.BIGINT);
                sqlStatement.setLong(2, experimentId);
                sqlStatement.setString(3, statusCode.name());
                sqlStatement.setString(4, batchStatusCode.name());

                /*
                 * Execute the stored procedure
                 */
                sqlStatement.execute();

                /*
                 * Get the result
                 */
                success = (sqlStatement.getLong(1) == experimentId);
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
                String.format(STRLOG_Success_arg, success));

        return success;
    }

    /**
     *
     * @param experimentId
     * @param statusCode
     * @param batchStatusCode
     * @param recordCount
     * @return
     * @throws Exception
     */
    public boolean UpdateStatusClose(long experimentId, StorageStatusCodes statusCode, StatusCodes batchStatusCode, int recordCount) throws Exception {
        final String methodName = "UpdateStatusClose";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        boolean success = false;

        try {
            Connection sqlConnection = this.dbConnection.getConnection();
            CallableStatement sqlStatement = null;

            try {
                /*
                 * Prepare the stored procedure call
                 */
                sqlStatement = sqlConnection.prepareCall(STRSQLCMD_UpdateStatusClose);
                sqlStatement.registerOutParameter(1, Types.BIGINT);
                sqlStatement.setLong(2, experimentId);
                sqlStatement.setString(3, statusCode.name());
                sqlStatement.setString(4, batchStatusCode.name());
                sqlStatement.setInt(5, recordCount);

                /*
                 * Execute the stored procedure
                 */
                sqlStatement.execute();

                /*
                 * Get the result
                 */
                success = (sqlStatement.getLong(1) == experimentId);
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
                String.format(STRLOG_Success_arg, success));

        return success;
    }

    //================================================================================================================//
    /**
     *
     * @param columnName
     * @param longval
     * @param strval
     * @return ArrayList of ExperimentInfo
     * @throws Exception
     */
    private ArrayList<ExperimentInfo> RetrieveBy(String columnName, long longval, String strval) throws Exception {
        final String methodName = "RetrieveBy";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        ArrayList<ExperimentInfo> list = new ArrayList<>();

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

                /*
                 * Execute the stored procedure
                 */
                ResultSet resultSet = sqlStatement.executeQuery();

                /*
                 * Process the results of the query - want all results
                 */
                while (resultSet.next() == true) {
                    ExperimentInfo experimentInfo = new ExperimentInfo();
                    experimentInfo.setExperimentId(resultSet.getLong(STRCOL_ExperimentId));
                    experimentInfo.setStatusCode(StorageStatusCodes.ToCode(resultSet.getString(STRCOL_StatusCode)));
                    experimentInfo.setBatchStatusCode(StatusCodes.ToCode(resultSet.getString(STRCOL_BatchStatusCode)));
                    experimentInfo.setCouponId(resultSet.getLong(STRCOL_CouponId));
                    experimentInfo.setUserId(resultSet.getInt(STRCOL_UserId));
                    experimentInfo.setGroupId(resultSet.getInt(STRCOL_GroupId));
                    experimentInfo.setAgentId(resultSet.getInt(STRCOL_AgentId));
                    experimentInfo.setClientId(resultSet.getInt(STRCOL_ClientId));
                    experimentInfo.setEssId(resultSet.getInt(STRCOL_EssId));
                    experimentInfo.setRecordCount(resultSet.getInt(STRCOL_RecordCount));
                    experimentInfo.setDuration(resultSet.getLong(STRCOL_Duration));
                    experimentInfo.setAnnotation(resultSet.getString(STRCOL_Annotation));

                    Calendar calendar;
                    Timestamp timestamp;
                    if ((timestamp = resultSet.getTimestamp(STRCOL_DateToStart)) != null) {
                        calendar = Calendar.getInstance();
                        calendar.setTime(timestamp);
                        experimentInfo.setDateToStart(calendar);
                    }
                    if ((timestamp = resultSet.getTimestamp(STRCOL_DateCreated)) != null) {
                        calendar = Calendar.getInstance();
                        calendar.setTime(timestamp);
                        experimentInfo.setDateCreated(calendar);
                    }
                    if ((timestamp = resultSet.getTimestamp(STRCOL_DateClosed)) != null) {
                        calendar = Calendar.getInstance();
                        calendar.setTime(timestamp);
                        experimentInfo.setDateClosed(calendar);
                    }

                    /*
                     * Add to the list
                     */
                    list.add(experimentInfo);
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
            throw ex;
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName,
                String.format(STRLOG_Count_arg, list.size()));

        return (list.size() > 0) ? list : null;
    }
}
