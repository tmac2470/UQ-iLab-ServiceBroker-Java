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
import java.util.Calendar;
import java.util.logging.Level;
import uq.ilabs.library.datatypes.batch.StatusCodes;
import uq.ilabs.library.datatypes.storage.StorageStatusCodes;
import uq.ilabs.library.experimentstorage.database.types.ExperimentInfo;
import uq.ilabs.library.lab.database.DBConnection;
import uq.ilabs.library.lab.utilities.Logfile;

/**
 *
 * @author uqlpayne
 */
public class ExperimentsDB {

    //<editor-fold defaultstate="collapsed" desc="Constants">
    private static final String STR_ClassName = ExperimentsDB.class.getName();
    private static final Level logLevel = Level.FINEST;
    /*
     * String constants for logfile messages
     */
    private static final String STRLOG_EntryId_arg = "EntryId: %d";
    private static final String STRLOG_Success_arg = "Success: %s";
    /*
     * String constants for SQL processing
     */
    private static final String STRSQLCMD_Add = "{ ? = call Experiments_Add(?,?,?,?,?) }";
    private static final String STRSQLCMD_Delete = "{ ? = call Experiments_Delete(?,?) }";
    private static final String STRSQLCMD_RetrieveBy = "{ call Experiments_RetrieveBy(?,?,?) }";
    private static final String STRSQLCMD_Update = "{ ? = call Experiments_Update(?,?,?,?,?,?,?) }";
    private static final String STRSQLCMD_UpdateCouponId = "{ ? = call Experiments_UpdateCouponId(?,?,?) }";
    private static final String STRSQLCMD_UpdateStatus = "{ ? = call Experiments_UpdateStatus(?,?,?,?) }";
    /*
     * Database column names
     */
    private static final String STRCOL_Id = "Id";
    private static final String STRCOL_ExperimentId = "ExperimentId";
    private static final String STRCOL_IssuerGuid = "IssuerGuid";
    private static final String STRCOL_StatusCode = "StatusCode";
    private static final String STRCOL_BatchStatusCode = "BatchStatusCode";
    private static final String STRCOL_SequenceNum = "SequenceNum";
    private static final String STRCOL_CouponId = "CouponId";
    private static final String STRCOL_DateCreated = "DateCreated";
    private static final String STRCOL_DateModified = "DateModified";
    private static final String STRCOL_DateScheduledClose = "DateScheduledClose";
    private static final String STRCOL_DateClosed = "DateClosed";
    /*
     * String constants for SQL result sets
     */
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Variables">
    private DBConnection dbConnection;
    //</editor-fold>

    /**
     *
     * @param dbConnection
     * @throws Exception
     */
    public ExperimentsDB(DBConnection dbConnection) throws Exception {
        final String methodName = "ExperimentsDB";
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
     * @param experimentInfo
     * @return long
     * @throws Exception
     */
    public long Add(ExperimentInfo experimentInfo) throws Exception {
        final String methodName = "Add";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        long entryId = -1;

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
                sqlStatement.setLong(2, experimentInfo.getExperimentId());
                sqlStatement.setString(3, experimentInfo.getIssuerGuid());
                sqlStatement.setString(4, experimentInfo.getStatusCode().toString());
                sqlStatement.setString(5, experimentInfo.getBatchStatusCode().toString());
                sqlStatement.setTimestamp(6, new Timestamp(experimentInfo.getDateScheduledClose().getTimeInMillis()));

                /*
                 * Execute the stored procedure
                 */
                sqlStatement.execute();

                /*
                 * Get the result
                 */
                entryId = (long) sqlStatement.getLong(1);
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
                String.format(STRLOG_EntryId_arg, entryId));

        return entryId;
    }

    /**
     *
     * @param experimentId
     * @param issuerGuid
     * @return boolean
     */
    public boolean Delete(long experimentId, String issuerGuid) {
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
                sqlStatement.setString(3, issuerGuid);

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
     * @param experimentId
     * @param issuerGuid
     * @return ExperimentInfo
     */
    public ExperimentInfo Retrieve(long experimentId, String issuerGuid) {
        return this.RetrieveBy(STRCOL_ExperimentId, experimentId, issuerGuid);
    }

    /**
     *
     * @param experimentInfo
     * @return boolean
     */
    public boolean Update(ExperimentInfo experimentInfo) {
        final String methodName = "Update";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        boolean success = false;

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
                sqlStatement = sqlConnection.prepareCall(STRSQLCMD_Update);
                sqlStatement.registerOutParameter(1, Types.BIGINT);
                sqlStatement.setLong(2, experimentInfo.getExperimentId());
                sqlStatement.setString(3, experimentInfo.getIssuerGuid());
                sqlStatement.setString(4, experimentInfo.getStatusCode().toString());
                sqlStatement.setString(5, (experimentInfo.getBatchStatusCode() != null) ? experimentInfo.getBatchStatusCode().toString() : null);
                sqlStatement.setInt(6, experimentInfo.getSequenceNo());
                sqlStatement.setLong(7, experimentInfo.getCouponId());
                sqlStatement.setTimestamp(8, new Timestamp(experimentInfo.getDateScheduledClose().getTimeInMillis()));

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
     * @param experimentInfo
     * @return boolean
     */
    public boolean UpdateCouponId(ExperimentInfo experimentInfo) {
        final String methodName = "UpdateCouponId";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        boolean success = false;

        try {
            Connection sqlConnection = this.dbConnection.getConnection();
            CallableStatement sqlStatement = null;

            try {
                /*
                 * Prepare the stored procedure call
                 */
                sqlStatement = sqlConnection.prepareCall(STRSQLCMD_UpdateCouponId);
                sqlStatement.registerOutParameter(1, Types.BIGINT);
                sqlStatement.setLong(2, experimentInfo.getExperimentId());
                sqlStatement.setString(3, experimentInfo.getIssuerGuid());
                sqlStatement.setLong(4, experimentInfo.getCouponId());

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
     * @param experimentInfo
     * @return boolean
     */
    public boolean UpdateStatus(ExperimentInfo experimentInfo) {
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
                sqlStatement.setLong(2, experimentInfo.getExperimentId());
                sqlStatement.setString(3, experimentInfo.getIssuerGuid());
                sqlStatement.setString(4, experimentInfo.getStatusCode().toString());
                sqlStatement.setString(5, experimentInfo.getBatchStatusCode().toString());

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

    //================================================================================================================//
    /**
     *
     * @param columnName
     * @param longval
     * @param strval
     * @return ExperimentInfo
     */
    private ExperimentInfo RetrieveBy(String columnName, long longval, String strval) {
        final String methodName = "RetrieveBy";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        ExperimentInfo experimentInfo = null;

        try {
            Connection sqlConnection = this.dbConnection.getConnection();
            CallableStatement sqlStatement = null;

            try {
                /*
                 * Prepare the stored procedure call
                 */
                sqlStatement = sqlConnection.prepareCall(STRSQLCMD_RetrieveBy);
                sqlStatement.setString(1, columnName.toLowerCase());
                sqlStatement.setLong(2, longval);
                sqlStatement.setString(3, strval);

                /*
                 * Execute the stored procedure
                 */
                ResultSet resultSet = sqlStatement.executeQuery();

                /*
                 * Process the results of the query - only want the first result
                 */
                if (resultSet.next() == true) {
                    experimentInfo = new ExperimentInfo();
                    experimentInfo.setId(resultSet.getLong(STRCOL_Id));
                    experimentInfo.setExperimentId(resultSet.getLong(STRCOL_ExperimentId));
                    experimentInfo.setIssuerGuid(resultSet.getString(STRCOL_IssuerGuid));
                    experimentInfo.setStatusCode(StorageStatusCodes.ToCode(resultSet.getString(STRCOL_StatusCode)));
                    experimentInfo.setBatchStatusCode(StatusCodes.ToCode(resultSet.getString(STRCOL_BatchStatusCode)));
                    experimentInfo.setSequenceNo(resultSet.getInt(STRCOL_SequenceNum));
                    experimentInfo.setCouponId(resultSet.getLong(STRCOL_CouponId));

                    Timestamp timestamp = resultSet.getTimestamp(STRCOL_DateCreated);
                    if (timestamp != null) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(timestamp);
                        experimentInfo.setDateCreated(calendar);
                    }

                    timestamp = resultSet.getTimestamp(STRCOL_DateModified);
                    if (timestamp != null) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(timestamp);
                        experimentInfo.setDateModified(calendar);
                    }

                    timestamp = resultSet.getTimestamp(STRCOL_DateScheduledClose);
                    if (timestamp != null) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(timestamp);
                        experimentInfo.setDateScheduledClose(calendar);
                    }

                    timestamp = resultSet.getTimestamp(STRCOL_DateClosed);
                    if (timestamp != null) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(timestamp);
                        experimentInfo.setDateClosed(calendar);
                    }
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
            experimentInfo = null;
            Logfile.WriteError(ex.toString());
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);

        return experimentInfo;
    }
}
