/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uq.ilabs.library.servicebroker.database;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.logging.Level;
import uq.ilabs.library.lab.database.DBConnection;
import uq.ilabs.library.lab.utilities.Logfile;
import uq.ilabs.library.servicebroker.database.types.LabClientGroupInfo;

/**
 *
 * @author uqlpayne
 */
public class LabClientGroupsDB {

    //<editor-fold defaultstate="collapsed" desc="Constants">
    private static final String STR_ClassName = LabClientGroupsDB.class.getName();
    private static final Level logLevel = Level.FINEST;
    /*
     * String constants for logfile messages
     */
    private static final String STRLOG_Id_arg = "Id: %d";
    private static final String STRLOG_Count_arg = "Count: %d";
    private static final String STRLOG_Success_arg = "Success: %s";
    /*
     * Database column names
     */
    private static final String STRCOL_Id = "Id";
    private static final String STRCOL_LabClientId = "LabClientId";
    private static final String STRCOL_GroupId = "GroupId";
    /*
     * String constants for SQL processing
     */
    private static final String STRSQLCMD_Add = "{ ? = call LabClientGroups_Add(?,?) }";
    private static final String STRSQLCMD_Delete = "{ ? = call LabClientGroups_Delete(?) }";
    private static final String STRSQLCMD_RetrieveBy = "{ call LabClientGroups_RetrieveBy(?,?,?) }";
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Variables">
    private DBConnection dbConnection;
    //</editor-fold>

    /**
     *
     * @param dbConnection
     * @throws Exception
     */
    public LabClientGroupsDB(DBConnection dbConnection) throws Exception {
        final String methodName = "LabClientGroupsDB";
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
     * @param userId
     * @param groupId
     * @return
     * @throws Exception
     */
    public int Add(LabClientGroupInfo labClientGroupInfo) throws Exception {
        final String methodName = "Add";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        int id = -1;

        try {
            /*
             * Check that parameters are valid
             */
            if (labClientGroupInfo == null) {
                throw new NullPointerException(LabClientGroupInfo.class.getSimpleName());
            }

            Connection sqlConnection = this.dbConnection.getConnection();
            CallableStatement sqlStatement = null;

            try {
                /*
                 * Prepare the stored procedure call
                 */
                sqlStatement = sqlConnection.prepareCall(STRSQLCMD_Add);
                sqlStatement.registerOutParameter(1, Types.INTEGER);
                sqlStatement.setInt(2, labClientGroupInfo.getLabClientId());
                sqlStatement.setInt(3, labClientGroupInfo.getGroupId());

                /*
                 * Execute the stored procedure
                 */
                sqlStatement.execute();

                /*
                 * Get the result
                 */
                id = (int) sqlStatement.getInt(1);
            } catch (NullPointerException | SQLException ex) {
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
        } catch (NullPointerException | SQLException ex) {
            Logfile.WriteError(ex.toString());
            throw ex;
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName,
                String.format(STRLOG_Id_arg, id));

        return id;
    }

    /**
     *
     * @param id
     * @return boolean
     */
    public boolean Delete(int id) {
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
                sqlStatement.setInt(2, id);

                /*
                 * Execute the stored procedure
                 */
                sqlStatement.execute();

                /*
                 * Get the result
                 */
                success = ((int) sqlStatement.getInt(1) == id);
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
     * @param groupId
     * @return
     * @throws Exception
     */
    public int[] GetListOfLabGroupIds(int labClientId) throws Exception {
        int[] groupIds = null;

        ArrayList<LabClientGroupInfo> list = this.RetrieveBy(STRCOL_LabClientId, labClientId, 0);
        if (list != null && list.size() > 0) {
            groupIds = new int[list.size()];
            for (int i = 0; i < list.size(); i++) {
                groupIds[i] = list.get(i).getGroupId();
            }
        }

        return groupIds;
    }

    /**
     *
     * @param groupId
     * @return
     * @throws Exception
     */
    public int[] GetListOfLabClientIds(int groupId) throws Exception {
        int[] labClientIds = null;

        ArrayList<LabClientGroupInfo> list = this.RetrieveBy(STRCOL_GroupId, groupId, 0);
        if (list != null && list.size() > 0) {
            labClientIds = new int[list.size()];
            for (int i = 0; i < list.size(); i++) {
                labClientIds[i] = list.get(i).getLabClientId();
            }
        }

        return labClientIds;
    }

    /**
     *
     * @param groupId
     * @return
     * @throws Exception
     */
    public ArrayList<LabClientGroupInfo> RetrieveByGroupId(int groupId) throws Exception {
        return this.RetrieveBy(STRCOL_GroupId, groupId, 0);
    }

    /**
     *
     * @param labClientId
     * @return
     * @throws Exception
     */
    public ArrayList<LabClientGroupInfo> RetrieveByLabClientId(int labClientId) throws Exception {
        return this.RetrieveBy(STRCOL_LabClientId, labClientId, 0);
    }

    /**
     *
     * @param labClientId
     * @param groupId
     * @return
     * @throws Exception
     */
    public LabClientGroupInfo RetrieveByLabClientIdGroupId(int labClientId, int groupId) throws Exception {
        ArrayList<LabClientGroupInfo> list = this.RetrieveBy(STRCOL_Id, labClientId, groupId);
        return (list != null) ? list.get(0) : null;
    }

    //================================================================================================================//
    /**
     *
     * @param columnName
     * @param intval
     * @param strval
     * @return
     * @throws Exception
     */
    private ArrayList<LabClientGroupInfo> RetrieveBy(String columnName, int intval, int intval2) throws Exception {
        final String methodName = "RetrieveBy";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        ArrayList<LabClientGroupInfo> arrayList = null;

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
                sqlStatement.setInt(3, intval2);

                /*
                 * Execute the stored procedure
                 */
                ResultSet resultSet = sqlStatement.executeQuery();

                /*
                 * Process the results of the query - want all results
                 */
                arrayList = new ArrayList<>();
                while (resultSet.next() == true) {
                    LabClientGroupInfo labClientGroupInfo = new LabClientGroupInfo();
                    labClientGroupInfo.setId(resultSet.getInt(STRCOL_Id));
                    labClientGroupInfo.setLabClientId(resultSet.getInt(STRCOL_LabClientId));
                    labClientGroupInfo.setGroupId(resultSet.getInt(STRCOL_GroupId));

                    /*
                     * Add the LabClientGroupInfo to the list
                     */
                    arrayList.add(labClientGroupInfo);
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
                String.format(STRLOG_Count_arg, (arrayList != null) ? arrayList.size() : 0));

        return (arrayList != null && arrayList.size() > 0) ? arrayList : null;
    }
}
