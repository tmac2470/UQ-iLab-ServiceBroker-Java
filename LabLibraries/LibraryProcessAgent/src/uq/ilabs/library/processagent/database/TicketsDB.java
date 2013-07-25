/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uq.ilabs.library.processagent.database;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.logging.Level;
import uq.ilabs.library.datatypes.ticketing.Ticket;
import uq.ilabs.library.datatypes.ticketing.TicketTypes;
import uq.ilabs.library.lab.database.DBConnection;
import uq.ilabs.library.lab.utilities.Logfile;

/**
 *
 * @author uqlpayne
 */
public class TicketsDB {

    //<editor-fold defaultstate="collapsed" desc="Constants">
    private static final String STR_ClassName = TicketsDB.class.getName();
    private static final Level logLevel = Level.FINEST;
    /*
     * String constants for logfile messages
     */
    private static final String STRLOG_TicketId_arg = "TicketId: %d";
    private static final String STRLOG_Count_arg = "Count: %d";
    private static final String STRLOG_Success_arg = "Success: %s";
    /*
     * Database column names
     */
    private static final String STRCOL_TicketId = "TicketId";
    private static final String STRCOL_TicketType = "TicketType";
    private static final String STRCOL_CouponId = "CouponId";
    private static final String STRCOL_IssuerGuid = "IssuerGuid";
    private static final String STRCOL_RedeemerGuid = "RedeemerGuid";
    private static final String STRCOL_SponsorGuid = "SponsorGuid";
    private static final String STRCOL_DateCreated = "DateCreated";
    private static final String STRCOL_Duration = "Duration";
    private static final String STRCOL_Cancelled = "Cancelled";
    private static final String STRCOL_Payload = "Payload";
    private static final String STRCOL_Expired = "Expired";
    /*
     * String constants for SQL processing
     */
    private static final String STRSQLCMD_Add = "{ ? = call Tickets_Add(?,?,?,?,?,?,?,?) }";
    private static final String STRSQLCMD_Cancel = "{ ? = call Tickets_Cancel(?) }";
    private static final String STRSQLCMD_Delete = "{ ? = call Tickets_Delete(?) }";
    private static final String STRSQLCMD_GetList = "{ call Tickets_GetList(?,?) }";
    private static final String STRSQLCMD_RetrieveBy = "{ call Tickets_RetrieveBy(?,?,?,?) }";
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Variables">
    private DBConnection dbConnection;
    //</editor-fold>

    /**
     *
     * @param dbConnection
     * @throws Exception
     */
    public TicketsDB(DBConnection dbConnection) throws Exception {
        final String methodName = "TicketsDB";
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
     * @param ticket
     * @return
     * @throws Exception
     */
    public long Add(Ticket ticket) throws Exception {
        final String methodName = "Add";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        long ticketId = -1;

        try {
            /*
             * Check that parameters are valid
             */
            if (ticket == null) {
                throw new NullPointerException(Ticket.class.getSimpleName());
            }

            Connection sqlConnection = this.dbConnection.getConnection();
            CallableStatement sqlStatement = null;

            try {
                /*
                 * Prepare the stored procedure call
                 */
                sqlStatement = sqlConnection.prepareCall(STRSQLCMD_Add);
                sqlStatement.registerOutParameter(1, Types.BIGINT);
                sqlStatement.setLong(2, ticket.getTicketId());
                sqlStatement.setInt(3, ticket.getTicketType().getValue());
                sqlStatement.setLong(4, ticket.getCouponId());
                sqlStatement.setString(5, ticket.getIssuerGuid());
                sqlStatement.setString(6, ticket.getRedeemerGuid());
                sqlStatement.setString(7, ticket.getSponsorGuid());
                sqlStatement.setLong(8, ticket.getDuration());
                sqlStatement.setString(9, ticket.getPayload());

                /*
                 * Execute the stored procedure
                 */
                sqlStatement.execute();

                /*
                 * Get the result
                 */
                ticketId = sqlStatement.getLong(1);
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
                String.format(STRLOG_TicketId_arg, ticketId));

        return ticketId;
    }

    /**
     *
     * @param ticketId
     * @return boolean
     * @throws Exception
     */
    public boolean Cancel(long ticketId) throws Exception {
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
                sqlStatement.setLong(2, ticketId);

                /*
                 * Execute the stored procedure
                 */
                sqlStatement.execute();

                /*
                 * Get the result
                 */
                success = ((int) sqlStatement.getLong(1) == ticketId);
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
     * @param ticketId
     * @return boolean
     * @throws Exception
     */
    public boolean Delete(long ticketId) throws Exception {
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
                sqlStatement.setLong(2, ticketId);

                /*
                 * Execute the stored procedure
                 */
                sqlStatement.execute();

                /*
                 * Get the result
                 */
                success = ((int) sqlStatement.getLong(1) == ticketId);
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
     * @param couponId
     * @return
     * @throws Exception
     */
    public ArrayList<Long> GetListCouponId(long couponId) throws Exception {
        return this.GetList(STRCOL_CouponId, couponId);
    }

    /**
     *
     * @return @throws Exception
     */
    public ArrayList<Long> GetListExpired() throws Exception {
        return this.GetList(STRCOL_Expired, 0);
    }

    /**
     *
     * @param ticketId
     * @return Ticket
     * @throws Exception
     */
    public Ticket RetrieveByTicketId(long ticketId) throws Exception {
        ArrayList<Ticket> list = this.RetrieveBy(STRCOL_TicketId, ticketId, null, 0);
        return (list != null) ? list.get(0) : null;
    }

    /**
     *
     * @param couponId
     * @param ticketType
     * @param redeemerGuid
     * @return
     * @throws Exception
     */
    public Ticket Retrieve(long couponId, TicketTypes ticketType, String redeemerGuid) throws Exception {
        ArrayList<Ticket> list = this.RetrieveBy(STRCOL_CouponId, couponId, redeemerGuid, ticketType.getValue());
        return (list != null) ? list.get(0) : null;
    }

    //================================================================================================================//
    /**
     *
     * @return ArrayList<Long>
     * @throws Exception
     */
    public ArrayList<Long> GetList(String columnName, long longval) throws Exception {
        final String methodName = "GetList";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        ArrayList<Long> list = new ArrayList<>();

        try {
            Connection sqlConnection = this.dbConnection.getConnection();
            CallableStatement sqlStatement = null;

            try {
                /*
                 * Prepare the stored procedure call
                 */
                sqlStatement = sqlConnection.prepareCall(STRSQLCMD_GetList);
                sqlStatement.setString(1, columnName);
                sqlStatement.setLong(2, longval);

                /*
                 * Execute the stored procedure
                 */
                ResultSet resultSet = sqlStatement.executeQuery();

                /*
                 * Add result to the list
                 */
                while (resultSet.next() == true) {
                    list.add(resultSet.getLong(STRCOL_TicketId));
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

    /**
     *
     * @param columnName
     * @param longval
     * @param strval
     * @param intval
     * @return ArrayList<Ticket>
     * @throws Exception
     */
    public ArrayList<Ticket> RetrieveBy(String columnName, long longval, String strval, int intval) throws Exception {
        final String methodName = "RetrieveBy";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        ArrayList<Ticket> list = new ArrayList<>();

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
                sqlStatement.setInt(4, intval);

                /*
                 * Execute the stored procedure
                 */
                ResultSet resultSet = sqlStatement.executeQuery();

                /*
                 * Process the results of the query - want all results
                 */
                while (resultSet.next() == true) {
                    Ticket ticket = new Ticket();
                    ticket.setTicketId(resultSet.getLong(STRCOL_TicketId));
                    ticket.setTicketType(TicketTypes.ToType(resultSet.getInt(STRCOL_TicketType)));
                    ticket.setCouponId(resultSet.getLong(STRCOL_CouponId));
                    ticket.setIssuerGuid(resultSet.getString(STRCOL_IssuerGuid));
                    ticket.setRedeemerGuid(resultSet.getString(STRCOL_RedeemerGuid));
                    ticket.setSponsorGuid(resultSet.getString(STRCOL_SponsorGuid));
                    ticket.setDuration(resultSet.getLong(STRCOL_Duration));
                    ticket.setCancelled(resultSet.getBoolean(STRCOL_Cancelled));
                    ticket.setPayload(resultSet.getString(STRCOL_Payload));
                    Calendar dateCreated = Calendar.getInstance();
                    dateCreated.setTime(resultSet.getTimestamp(STRCOL_DateCreated));
                    ticket.setDateCreated(dateCreated);

                    /*
                     * Add the Ticket to the list
                     */
                    list.add(ticket);
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
