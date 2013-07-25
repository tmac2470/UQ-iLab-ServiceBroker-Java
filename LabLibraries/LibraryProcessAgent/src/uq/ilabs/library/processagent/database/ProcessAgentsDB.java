/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uq.ilabs.library.processagent.database;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.logging.Level;
import uq.ilabs.library.datatypes.processagent.ProcessAgentTypes;
import uq.ilabs.library.datatypes.ticketing.Coupon;
import uq.ilabs.library.lab.database.DBConnection;
import uq.ilabs.library.lab.utilities.Logfile;
import uq.ilabs.library.processagent.database.types.ProcessAgentInfo;
import uq.ilabs.library.processagent.database.types.SystemSupportInfo;

/**
 *
 * @author uqlpayne
 */
public class ProcessAgentsDB {

    //<editor-fold defaultstate="collapsed" desc="Constants">
    private static final String STR_ClassName = ProcessAgentsDB.class.getName();
    private static final Level logLevel = Level.FINEST;
    /*
     * String constants for logfile messages
     */
    private static final String STRLOG_AgentId_arg = "AgentId: %d";
    private static final String STRLOG_Count_arg = "Count: %d";
    private static final String STRLOG_Success_arg = "Success: %s";
    /*
     * String constants for SQL processing
     */
    private static final String STRSQLCMD_Add = "{ ? = call ProcessAgents_Add(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) }";
    private static final String STRSQLCMD_Delete = "{ ? = call ProcessAgents_Delete(?) }";
    private static final String STRSQLCMD_GetList = "{ call ProcessAgents_GetList(?,?) }";
    private static final String STRSQLCMD_RetrieveBy = "{ call ProcessAgents_RetrieveBy(?,?,?) }";
    private static final String STRSQLCMD_Update = "{ ? = call ProcessAgents_Update(?,?,?,?,?,?,?,?) }";
    private static final String STRSQLCMD_UpdateSystemSupport = "{ ? = call ProcessAgents_UpdateSystemSupport(?,?,?,?,?,?,?) }";
    /*
     * Database column names
     */
    private static final String STRCOL_AgentId = "AgentId";
    private static final String STRCOL_AgentName = "AgentName";
    private static final String STRCOL_AgentGuid = "AgentGuid";
    private static final String STRCOL_AgentType = "AgentType";
    private static final String STRCOL_ServiceUrl = "ServiceUrl";
    private static final String STRCOL_ClientUrl = "ClientUrl";
    private static final String STRCOL_DomainGuid = "DomainGuid";
    private static final String STRCOL_IsSelf = "IsSelf";
    private static final String STRCOL_IsRetired = "IsRetired";
    private static final String STRCOL_OutCouponId = "OutCouponId";
    private static final String STRCOL_InCouponId = "InCouponId";
    private static final String STRCOL_IssuerGuid = "IssuerGuid";
    private static final String STRCOL_ContactName = "ContactName";
    private static final String STRCOL_ContactEmail = "ContactEmail";
    private static final String STRCOL_BugReportEmail = "BugReportEmail";
    private static final String STRCOL_Location = "Location";
    private static final String STRCOL_InfoUrl = "InfoUrl";
    private static final String STRCOL_Description = "Description";
    private static final String STRCOL_DateCreated = "DateCreated";
    private static final String STRCOL_DateModified = "DateModified";
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Variables">
    private DBConnection dbConnection;
    //</editor-fold>

    /**
     *
     * @param dbConnection
     * @throws Exception
     */
    public ProcessAgentsDB(DBConnection dbConnection) throws Exception {
        final String methodName = "ProcessAgentsDB";
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
     * @param processAgent
     * @param couponIn
     * @param couponOut
     * @return
     * @throws Exception
     */
    public int Add(ProcessAgentInfo processAgentInfo) {
        final String methodName = "Add";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        int agentId = -1;

        try {
            /*
             * Check that parameters are valid
             */
            if (processAgentInfo == null) {
                throw new NullPointerException(ProcessAgentInfo.class.getSimpleName());
            }

            Connection sqlConnection = this.dbConnection.getConnection();
            CallableStatement sqlStatement = null;

            try {
                /*
                 * Prepare the stored procedure call
                 */
                sqlStatement = sqlConnection.prepareCall(STRSQLCMD_Add);
                sqlStatement.registerOutParameter(1, Types.INTEGER);
                sqlStatement.setString(2, processAgentInfo.getAgentName());
                sqlStatement.setString(3, processAgentInfo.getAgentGuid());
                sqlStatement.setString(4, processAgentInfo.getAgentType().name());
                sqlStatement.setString(5, processAgentInfo.getServiceUrl());
                sqlStatement.setString(6, processAgentInfo.getClientUrl());

                sqlStatement.setBoolean(7, processAgentInfo.isSelf());
                sqlStatement.setString(8, processAgentInfo.getDomainGuid());
                sqlStatement.setString(9, processAgentInfo.getIssuerGuid());
                sqlStatement.setLong(10, processAgentInfo.getInCoupon().getCouponId());
                sqlStatement.setLong(11, processAgentInfo.getOutCoupon().getCouponId());

                SystemSupportInfo systemSupportInfo = processAgentInfo.getSystemSupportInfo();
                sqlStatement.setString(12, systemSupportInfo.getContactName());
                sqlStatement.setString(13, systemSupportInfo.getContactEmail());
                sqlStatement.setString(14, systemSupportInfo.getBugReportEmail());
                sqlStatement.setString(15, systemSupportInfo.getLocation());
                sqlStatement.setString(16, systemSupportInfo.getInfoUrl());

                sqlStatement.setString(17, systemSupportInfo.getDescription());

                /*
                 * Execute the stored procedure
                 */
                sqlStatement.execute();

                /*
                 * Get the process agent Id
                 */
                agentId = sqlStatement.getInt(1);
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
                String.format(STRLOG_AgentId_arg, agentId));

        return agentId;
    }

    /**
     *
     * @param agentId
     * @return boolean
     */
    public boolean Delete(int agentId) {
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
                sqlStatement.registerOutParameter(1, Types.INTEGER);
                sqlStatement.setInt(2, agentId);

                /*
                 * Execute the stored procedure
                 */
                sqlStatement.execute();

                /*
                 * Get the result
                 */
                success = (sqlStatement.getInt(1) == agentId);
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
     * @return String[]
     */
    public String[] GetListAll() {
        return this.GetList(null, null);
    }

    /**
     *
     * @return String[]
     */
    public String[] GetListOfNamesByType(ProcessAgentTypes agentType) {
        return this.GetList(STRCOL_AgentType, agentType.name());
    }

    /**
     *
     * @return ProcessAgentInfo
     */
    public ProcessAgentInfo RetrieveSelf() {
        ArrayList<ProcessAgentInfo> list = this.RetrieveBy(STRCOL_IsSelf, 0, null);
        return (list != null) ? list.get(0) : null;
    }

    /**
     *
     * @param agentId
     * @return
     */
    public ProcessAgentInfo RetrieveById(int agentId) {
        ArrayList<ProcessAgentInfo> list = this.RetrieveBy(STRCOL_AgentId, agentId, null);
        return (list != null) ? list.get(0) : null;
    }

    /**
     *
     * @param agentName
     * @return ProcessAgentInfo
     */
    public ProcessAgentInfo RetrieveByName(String agentName) {
        ArrayList<ProcessAgentInfo> list = this.RetrieveBy(STRCOL_AgentName, 0, agentName);
        return (list != null) ? list.get(0) : null;
    }

    /**
     *
     * @param agentGuid
     * @return ProcessAgentInfo
     */
    public ProcessAgentInfo RetrieveByGuid(String agentGuid) {
        ArrayList<ProcessAgentInfo> list = this.RetrieveBy(STRCOL_AgentGuid, 0, agentGuid);
        return (list != null) ? list.get(0) : null;
    }

    /**
     *
     * @return SystemSupportInfo
     */
    public SystemSupportInfo RetrieveSystemSupportInfoSelf() {
        SystemSupportInfo systemSupportInfo = null;

        ProcessAgentInfo processAgentInfo = this.RetrieveSelf();
        if (processAgentInfo != null) {
            systemSupportInfo = processAgentInfo.getSystemSupportInfo();
        }

        return systemSupportInfo;
    }

    /**
     *
     * @param agentGuid
     * @return SystemSupportInfo
     */
    public SystemSupportInfo RetrieveSystemSupportInfoByGuid(String agentGuid) {
        SystemSupportInfo systemSupportInfo = null;

        ProcessAgentInfo processAgentInfo = this.RetrieveByGuid(agentGuid);
        if (processAgentInfo != null) {
            systemSupportInfo = processAgentInfo.getSystemSupportInfo();
        }

        return systemSupportInfo;
    }

    /**
     *
     * @param userInfo
     * @return
     * @throws Exception
     */
    public boolean Update(ProcessAgentInfo processAgentInfo) {
        final String methodName = "Update";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        boolean success = false;

        try {
            /*
             * Check that parameters are valid
             */
            if (processAgentInfo == null) {
                throw new NullPointerException(ProcessAgentInfo.class.getSimpleName());
            }

            Connection sqlConnection = this.dbConnection.getConnection();
            CallableStatement sqlStatement = null;

            try {
                /*
                 * Prepare the stored procedure call
                 */
                sqlStatement = sqlConnection.prepareCall(STRSQLCMD_Update);
                sqlStatement.registerOutParameter(1, Types.INTEGER);
                sqlStatement.setInt(2, processAgentInfo.getAgentId());
                sqlStatement.setString(3, processAgentInfo.getServiceUrl());
                sqlStatement.setString(4, processAgentInfo.getClientUrl());
                sqlStatement.setString(5, processAgentInfo.getDomainGuid());
                sqlStatement.setBoolean(6, processAgentInfo.isRetired());
                sqlStatement.setLong(7, processAgentInfo.getOutCoupon().getCouponId());
                sqlStatement.setLong(8, processAgentInfo.getInCoupon().getCouponId());
                sqlStatement.setString(9, processAgentInfo.getIssuerGuid());

                /*
                 * Execute the stored procedure
                 */
                sqlStatement.execute();

                /*
                 * Get the result
                 */
                success = (sqlStatement.getInt(1) == processAgentInfo.getAgentId());
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
     * @param systemSupportInfo
     * @return boolean
     * @throws Exception
     */
    public boolean Update(SystemSupportInfo systemSupportInfo) {
        final String methodName = "Update";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        boolean success = false;

        try {
            /*
             * Check that parameters are valid
             */
            if (systemSupportInfo == null) {
                throw new NullPointerException(SystemSupportInfo.class.getSimpleName());
            }

            Connection sqlConnection = this.dbConnection.getConnection();
            CallableStatement sqlStatement = null;

            try {
                /*
                 * Prepare the stored procedure call
                 */
                sqlStatement = sqlConnection.prepareCall(STRSQLCMD_UpdateSystemSupport);
                sqlStatement.registerOutParameter(1, Types.INTEGER);
                sqlStatement.setString(2, systemSupportInfo.getAgentGuid());
                sqlStatement.setString(3, systemSupportInfo.getContactName());
                sqlStatement.setString(4, systemSupportInfo.getContactEmail());
                sqlStatement.setString(5, systemSupportInfo.getBugReportEmail());
                sqlStatement.setString(6, systemSupportInfo.getLocation());
                sqlStatement.setString(7, systemSupportInfo.getInfoUrl());
                sqlStatement.setString(8, systemSupportInfo.getDescription());

                /*
                 * Execute the stored procedure
                 */
                sqlStatement.execute();

                /*
                 * Get the result
                 */
                success = ((int) sqlStatement.getInt(1) > 0);
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
     * @param strval
     * @return String[]
     */
    private String[] GetList(String columnName, String strval) {
        final String methodName = "GetList";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        String[] listArray = null;

        try {
            ArrayList<String> list = new ArrayList<>();
            Connection sqlConnection = this.dbConnection.getConnection();
            CallableStatement sqlStatement = null;

            try {
                /*
                 * Prepare the stored procedure call
                 */
                sqlStatement = sqlConnection.prepareCall(STRSQLCMD_GetList);
                sqlStatement.setString(1, columnName);
                sqlStatement.setString(2, strval);

                /*
                 * Execute the stored procedure
                 */
                ResultSet resultSet = sqlStatement.executeQuery();

                /*
                 * Add result to the list
                 */
                while (resultSet.next() == true) {
                    list.add(resultSet.getString(STRCOL_AgentName));
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

            /*
             * Convert the list to an array
             */
            if (list.size() > 0) {
                listArray = list.toArray(new String[0]);
            }
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName,
                String.format(STRLOG_Count_arg, (listArray != null) ? listArray.length : 0));

        return listArray;
    }

    /**
     *
     * @param columnName
     * @param intval
     * @param strval
     * @return
     */
    private ArrayList<ProcessAgentInfo> RetrieveBy(String columnName, int intval, String strval) {
        final String methodName = "RetrieveBy";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        ArrayList<ProcessAgentInfo> list = new ArrayList<>();

        try {
            Connection sqlConnection = this.dbConnection.getConnection();
            CallableStatement sqlStatement = null;

            try {
                /*
                 * Prepare the stored procedure call
                 */
                sqlStatement = sqlConnection.prepareCall(STRSQLCMD_RetrieveBy);
                sqlStatement.setString(1, columnName);
                sqlStatement.setInt(2, intval);
                sqlStatement.setString(3, strval);

                /*
                 * Execute the stored procedure
                 */
                ResultSet resultSet = sqlStatement.executeQuery();

                /*
                 * Process the results of the query - only want the first result
                 */
                while (resultSet.next() == true) {
                    ProcessAgentInfo processAgentInfo = new ProcessAgentInfo();
                    processAgentInfo.setAgentId(resultSet.getInt(STRCOL_AgentId));
                    processAgentInfo.setAgentName(resultSet.getString(STRCOL_AgentName));
                    processAgentInfo.setAgentGuid(resultSet.getString(STRCOL_AgentGuid));
                    processAgentInfo.setAgentType(ProcessAgentTypes.valueOf(resultSet.getString(STRCOL_AgentType)));
                    processAgentInfo.setServiceUrl(resultSet.getString(STRCOL_ServiceUrl));
                    processAgentInfo.setClientUrl(resultSet.getString(STRCOL_ClientUrl));
                    processAgentInfo.setDomainGuid(resultSet.getString(STRCOL_DomainGuid));
                    processAgentInfo.setSelf(resultSet.getBoolean(STRCOL_IsSelf));
                    processAgentInfo.setRetired(resultSet.getBoolean(STRCOL_IsRetired));
                    processAgentInfo.setOutCoupon(new Coupon(resultSet.getLong(STRCOL_OutCouponId)));
                    processAgentInfo.setInCoupon(new Coupon(resultSet.getLong(STRCOL_InCouponId)));
                    processAgentInfo.setIssuerGuid(resultSet.getString(STRCOL_IssuerGuid));

                    SystemSupportInfo systemSupportInfo = processAgentInfo.getSystemSupportInfo();
                    systemSupportInfo.setAgentGuid(processAgentInfo.getAgentGuid());
                    systemSupportInfo.setContactName(resultSet.getString(STRCOL_ContactName));
                    systemSupportInfo.setContactEmail(resultSet.getString(STRCOL_ContactEmail));
                    systemSupportInfo.setBugReportEmail(resultSet.getString(STRCOL_BugReportEmail));
                    systemSupportInfo.setLocation(resultSet.getString(STRCOL_Location));
                    systemSupportInfo.setInfoUrl(resultSet.getString(STRCOL_InfoUrl));
                    systemSupportInfo.setDescription(resultSet.getString(STRCOL_Description));
                    processAgentInfo.setSystemSupportInfo(systemSupportInfo);

                    Timestamp timestamp = resultSet.getTimestamp(STRCOL_DateCreated);
                    if (timestamp != null) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(timestamp);
                        processAgentInfo.setDateCreated(calendar);
                    }

                    timestamp = resultSet.getTimestamp(STRCOL_DateModified);
                    if (timestamp != null) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(timestamp);
                        processAgentInfo.setDateModified(calendar);
                    }

                    /*
                     * Add the info to the list
                     */
                    list.add(processAgentInfo);
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
