/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uq.ilabs.library.processagent.database;

import java.sql.*;
import java.util.logging.Level;
import uq.ilabs.library.datatypes.ticketing.Coupon;
import uq.ilabs.library.lab.database.DBConnection;
import uq.ilabs.library.lab.utilities.Logfile;

/**
 *
 * @author uqlpayne
 */
public class CouponsDB {

    //<editor-fold defaultstate="collapsed" desc="Constants">
    private static final String STR_ClassName = CouponsDB.class.getName();
    private static final Level logLevel = Level.FINEST;
    /*
     * String constants for logfile messages
     */
    private static final String STRLOG_CouponId_arg = "CouponId: %d";
    private static final String STRLOG_Success_arg = "Success: %s";
    /*
     * Database column names
     */
    private static final String STRCOL_CouponId = "CouponId";
    private static final String STRCOL_IssuerGuid = "IssuerGuid";
    private static final String STRCOL_Passkey = "Passkey";
    private static final String STRCOL_Cancelled = "Cancelled";
    private static final String STRCOL_CouponIdIssuerGuid = "CouponIdIssuerGuid";
    /*
     * String constants for SQL processing
     */
    private static final String STRSQLCMD_Add = "{ ? = call Coupons_Add(?,?,?) }";
    private static final String STRSQLCMD_Cancel = "{ ? = call Coupons_Cancel(?,?) }";
    private static final String STRSQLCMD_Delete = "{ ? = call Coupons_Delete(?,?) }";
    private static final String STRSQLCMD_RetrieveBy = "{ call Coupons_RetrieveBy(?,?,?) }";
    private static final String STRSQLCMD_Update = "{ ? = call Coupons_Update(?,?,?,?) }";
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Variables">
    private DBConnection dbConnection;
    //</editor-fold>

    /**
     *
     * @param dbConnection
     * @throws Exception
     */
    public CouponsDB(DBConnection dbConnection) throws Exception {
        final String methodName = "CouponsDB";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        /*
         * Check that all parameters are valid
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
     * @param coupon
     * @return long
     * @throws Exception
     */
    public long Add(Coupon coupon) throws Exception {
        final String methodName = "Add";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        long couponId = -1;

        try {
            /*
             * Check that parameters are valid
             */
            if (coupon == null) {
                throw new NullPointerException(Coupon.class.getSimpleName());
            }

            Connection sqlConnection = this.dbConnection.getConnection();
            CallableStatement sqlStatement = null;

            try {
                /*
                 * Prepare the stored procedure call
                 */
                sqlStatement = sqlConnection.prepareCall(STRSQLCMD_Add);
                sqlStatement.registerOutParameter(1, Types.BIGINT);
                sqlStatement.setLong(2, coupon.getCouponId());
                sqlStatement.setString(3, coupon.getIssuerGuid());
                sqlStatement.setString(4, coupon.getPasskey());

                /*
                 * Execute the stored procedure
                 */
                sqlStatement.execute();

                /*
                 * Get the coupon Id
                 */
                couponId = sqlStatement.getLong(1);
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
                String.format(STRLOG_CouponId_arg, couponId));

        return couponId;
    }

    /**
     *
     * @param couponId
     * @return boolean
     */
    public boolean Cancel(long couponId, String issuerGuid) {
        final String methodName = "Cancel";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        boolean success = false;

        try {
            Connection sqlConnection = this.dbConnection.getConnection();
            CallableStatement sqlStatement = null;

            try {
                /*
                 * Prepare the stored procedure call
                 */
                sqlStatement = sqlConnection.prepareCall(STRSQLCMD_Cancel);
                sqlStatement.registerOutParameter(1, Types.BIGINT);
                sqlStatement.setLong(2, couponId);
                sqlStatement.setString(3, issuerGuid);

                /*
                 * Execute the stored procedure
                 */
                sqlStatement.execute();

                /*
                 * Get the result
                 */
                success = (sqlStatement.getLong(1) == couponId);
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
     * @param couponId
     * @return boolean
     */
    public boolean Delete(long couponId, String issuerGuid) {
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
                sqlStatement.setLong(2, couponId);
                sqlStatement.setString(3, issuerGuid);

                /*
                 * Execute the stored procedure
                 */
                sqlStatement.execute();

                /*
                 * Get the result
                 */
                success = (sqlStatement.getLong(1) == couponId);
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
     * @param couponId
     * @return Coupon
     * @throws Exception
     */
    public Coupon Retrieve(long couponId, String issuerGuid) throws Exception {
        return this.RetrieveBy(STRCOL_CouponIdIssuerGuid, couponId, issuerGuid);
    }

    //================================================================================================================//
    /**
     *
     * @param columnName
     * @param intval
     * @param strval
     * @return Coupon
     * @throws Exception
     */
    private Coupon RetrieveBy(String columnName, long longval, String strval) throws Exception {
        final String methodName = "RetrieveBy";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        Coupon coupon = null;

        try {
            Connection sqlConnection = this.dbConnection.getConnection();
            CallableStatement sqlStatement = null;

            try {
                /*
                 * Prepare the stored procedure call
                 */
                sqlStatement = sqlConnection.prepareCall(STRSQLCMD_RetrieveBy);
                sqlStatement.setString(1, (columnName != null ? columnName.toLowerCase() : null));
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
                    coupon = new Coupon();
                    coupon.setCouponId(resultSet.getInt(STRCOL_CouponId));
                    coupon.setIssuerGuid(resultSet.getString(STRCOL_IssuerGuid));
                    coupon.setPasskey(resultSet.getString(STRCOL_Passkey));
                    coupon.setCancelled(resultSet.getBoolean(STRCOL_Cancelled));
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

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);

        return coupon;
    }
}
