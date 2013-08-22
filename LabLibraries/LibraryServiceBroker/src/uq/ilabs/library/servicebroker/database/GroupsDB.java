/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uq.ilabs.library.servicebroker.database;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.logging.Level;
import uq.ilabs.library.lab.database.DBConnection;
import uq.ilabs.library.lab.utilities.Logfile;
import uq.ilabs.library.servicebroker.database.types.GroupInfo;
import uq.ilabs.library.servicebroker.engine.types.GroupTypes;

/**
 *
 * @author uqlpayne
 */
public class GroupsDB {

    //<editor-fold defaultstate="collapsed" desc="Constants">
    private static final String STR_ClassName = GroupsDB.class.getName();
    private static final Level logLevel = Level.FINEST;
    /*
     * String constants for logfile messages
     */
    private static final String STRLOG_GroupId_arg = "Group: %d";
    private static final String STRLOG_Count_arg = "Count: %d";
    private static final String STRLOG_Success_arg = "Success: %s";
    /*
     * Database column names
     */
    private static final String STRCOL_Id = "Id";
    private static final String STRCOL_Name = "Name";
    private static final String STRCOL_Type = "Type";
    private static final String STRCOL_IsRequest = "IsRequest";
    private static final String STRCOL_Description = "Description";
    private static final String STRCOL_DateCreated = "DateCreated";
    /*
     * String constants for SQL processing
     */
    private static final String STRSQLCMD_Add = "{ ? = call Groups_Add(?,?,?,?) }";
    private static final String STRSQLCMD_Delete = "{ ? = call Groups_Delete(?) }";
    private static final String STRSQLCMD_GetList = "{ call Groups_GetList(?,?) }";
    private static final String STRSQLCMD_RetrieveBy = "{ call Groups_RetrieveBy(?,?,?) }";
    private static final String STRSQLCMD_Update = "{ ? = call Groups_Update(?,?,?,?,?,?) }";
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Variables">
    private DBConnection dbConnection;
    //</editor-fold>

    /**
     *
     * @param dbConnection
     * @throws Exception
     */
    public GroupsDB(DBConnection dbConnection) throws Exception {
        final String methodName = "GroupsDB";
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
     * @param groupInfo
     * @return int
     * @throws Exception
     */
    public int Add(GroupInfo groupInfo) throws Exception {
        final String methodName = "Add";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        int groupId = -1;

        try {
            /*
             * Check that parameters are valid
             */
            if (groupInfo == null) {
                throw new NullPointerException(GroupInfo.class.getSimpleName());
            }

            Connection sqlConnection = this.dbConnection.getConnection();
            CallableStatement sqlStatement = null;

            try {
                /*
                 * Prepare the stored procedure call
                 */
                sqlStatement = sqlConnection.prepareCall(STRSQLCMD_Add);
                sqlStatement.registerOutParameter(1, Types.INTEGER);
                sqlStatement.setString(2, groupInfo.getName());
                sqlStatement.setString(3, groupInfo.getType().name());
                sqlStatement.setBoolean(4, groupInfo.isRequest());
                sqlStatement.setString(4, groupInfo.getDescription());

                /*
                 * Execute the stored procedure
                 */
                sqlStatement.execute();

                /*
                 * Get the result
                 */
                groupId = (int) sqlStatement.getInt(1);
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
                String.format(STRLOG_GroupId_arg, groupId));

        return groupId;
    }

    /**
     *
     * @param groupId
     * @return boolean
     */
    public boolean Delete(int groupId) {
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
                sqlStatement.setInt(2, groupId);

                /*
                 * Execute the stored procedure
                 */
                sqlStatement.execute();

                /*
                 * Get the result
                 */
                success = ((int) sqlStatement.getInt(1) == groupId);
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
    public String[] GetListOfNames() throws Exception {
        return this.GetList(null, null);
    }

    /**
     *
     * @param groupType
     * @return String[]
     * @throws Exception
     */
    public String[] GetListOfNamesByType(GroupTypes type) throws Exception {
        return this.GetList(STRCOL_Type, type.name());
    }

    /**
     *
     * @return String[]
     * @throws Exception
     */
    public String[] GetListOfNamesByIsRequest() throws Exception {
        return this.GetList(STRCOL_IsRequest, null);
    }

    /**
     *
     * @param id
     * @return
     * @throws Exception
     */
    public GroupInfo RetrieveById(int id) throws Exception {
        ArrayList<GroupInfo> list = this.RetrieveBy(STRCOL_Id, id, null);
        return (list != null) ? list.get(0) : null;
    }

    /**
     *
     * @param name
     * @return
     * @throws Exception
     */
    public GroupInfo RetrieveByName(String name) throws Exception {
        ArrayList<GroupInfo> list = this.RetrieveBy(STRCOL_Name, 0, name);
        return (list != null) ? list.get(0) : null;
    }

    /**
     *
     * @return @throws Exception
     */
    public ArrayList<GroupInfo> RetrieveAllByIsRequest() throws Exception {
        return this.RetrieveBy(STRCOL_IsRequest, 0, null);
    }

    /**
     *
     * @param type
     * @return
     * @throws Exception
     */
    public ArrayList<GroupInfo> RetrieveByType(GroupTypes type) throws Exception {
        return this.RetrieveBy(STRCOL_Type, 0, type.name());
    }

    /**
     *
     * @return @throws Exception
     */
    public ArrayList<GroupInfo> RetrieveAll() throws Exception {
        return this.RetrieveBy(null, 0, null);
    }

    /**
     *
     * @param groupInfo
     * @return boolean
     * @throws Exception
     */
    public boolean Update(GroupInfo groupInfo) throws Exception {
        final String methodName = "Update";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        boolean success = false;

        try {
            /*
             * Check that parameters are valid
             */
            if (groupInfo == null) {
                throw new NullPointerException(GroupInfo.class.getSimpleName());
            }

            Connection sqlConnection = this.dbConnection.getConnection();
            CallableStatement sqlStatement = null;

            try {
                /*
                 * Prepare the stored procedure call
                 */
                sqlStatement = sqlConnection.prepareCall(STRSQLCMD_Update);
                sqlStatement.registerOutParameter(1, Types.INTEGER);
                sqlStatement.setInt(2, groupInfo.getId());
                sqlStatement.setString(3, groupInfo.getType().name());
                sqlStatement.setBoolean(4, groupInfo.isRequest());
                sqlStatement.setString(5, groupInfo.getDescription());

                /*
                 * Execute the stored procedure
                 */
                sqlStatement.execute();

                /*
                 * Get the result
                 */
                success = (sqlStatement.getInt(1) == groupInfo.getId());
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
     * @param strval
     * @return String[]
     */
    private String[] GetList(String columnName, String strval) throws Exception {
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
                    list.add(resultSet.getString(STRCOL_Name));
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
            throw ex;
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
     * @return ArrayList<GroupInfo>
     */
    private ArrayList<GroupInfo> RetrieveBy(String columnName, int intval, String strval) throws Exception {
        final String methodName = "RetrieveBy";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        ArrayList<GroupInfo> list = new ArrayList<>();

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
                 * Process the results of the query - want all results
                 */
                while (resultSet.next() == true) {
                    GroupInfo groupInfo = new GroupInfo();
                    groupInfo.setId(resultSet.getInt(STRCOL_Id));
                    groupInfo.setName(resultSet.getString(STRCOL_Name));
                    groupInfo.setType(GroupTypes.ToType(resultSet.getString(STRCOL_Type)));
                    groupInfo.setRequest(resultSet.getBoolean(STRCOL_IsRequest));
                    groupInfo.setDescription(resultSet.getString(STRCOL_Description));

                    Timestamp timestamp;
                    if ((timestamp = resultSet.getTimestamp(STRCOL_DateCreated)) != null) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(timestamp);
                        groupInfo.setDateCreated(calendar);
                    }

                    /*
                     * Add the info to the list
                     */
                    list.add(groupInfo);
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
