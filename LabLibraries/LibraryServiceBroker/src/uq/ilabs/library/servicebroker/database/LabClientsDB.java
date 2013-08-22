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
import uq.ilabs.library.servicebroker.database.types.LabClientInfo;
import uq.ilabs.library.servicebroker.engine.types.LabClientTypes;

/**
 *
 * @author uqlpayne
 */
public class LabClientsDB {

    //<editor-fold defaultstate="collapsed" desc="Constants">
    private static final String STR_ClassName = LabClientsDB.class.getName();
    private static final Level logLevel = Level.FINEST;
    /*
     * String constants for logfile messages
     */
    private static final String STRLOG_LabClientId_arg = "LabClient: %d";
    private static final String STRLOG_Count_arg = "Count: %d";
    private static final String STRLOG_Success_arg = "Success: %s";
    /*
     * String constants for SQL processing
     */
    private static final String STRSQLCMD_Add = "{ ? = call LabClients_Add(?,?,?,?,?,?,?,?,?,?,?) }";
    private static final String STRSQLCMD_Delete = "{ ? = call LabClients_Delete(?) }";
    private static final String STRSQLCMD_GetListIds = "{ call LabClients_GetListIds() }";
    private static final String STRSQLCMD_GetListNames = "{ call LabClients_GetListNames() }";
    private static final String STRSQLCMD_RetrieveBy = "{ call LabClients_RetrieveBy(?,?,?) }";
    private static final String STRSQLCMD_Update = "{ ? = call LabClients_Update(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) }";
    /*
     * Database column names
     */
    private static final String STRCOL_Id = "Id";
    private static final String STRCOL_Name = "Name";
    private static final String STRCOL_Guid = "Guid";
    private static final String STRCOL_Type = "Type";
    private static final String STRCOL_Title = "Title";
    private static final String STRCOL_Version = "Version";
    private static final String STRCOL_Description = "Description";
    private static final String STRCOL_LoaderScript = "LoaderScript";
    private static final String STRCOL_AgentId = "AgentId";
    private static final String STRCOL_EssId = "EssId";
    private static final String STRCOL_UssId = "UssId";
    private static final String STRCOL_IsReentrant = "IsReentrant";
    private static final String STRCOL_ContactName = "ContactName";
    private static final String STRCOL_ContactEmail = "ContactEmail";
    private static final String STRCOL_DocumentationUrl = "DocumentationUrl";
    private static final String STRCOL_Notes = "Notes";
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
    public LabClientsDB(DBConnection dbConnection) throws Exception {
        final String methodName = "LabClientsDB";
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
     * @param labClientInfo
     * @return
     * @throws Exception
     */
    public int Add(LabClientInfo labClientInfo) throws Exception {
        final String methodName = "Add";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        int labClientId = -1;

        try {
            /*
             * Check that parameters are valid
             */
            if (labClientInfo == null) {
                throw new NullPointerException(LabClientInfo.class.getSimpleName());
            }

            Connection sqlConnection = this.dbConnection.getConnection();
            CallableStatement sqlStatement = null;

            try {
                /*
                 * Prepare the stored procedure call
                 */
                sqlStatement = sqlConnection.prepareCall(STRSQLCMD_Add);
                sqlStatement.registerOutParameter(1, Types.INTEGER);
                sqlStatement.setString(2, labClientInfo.getName());
                sqlStatement.setString(3, labClientInfo.getGuid());
                sqlStatement.setString(4, labClientInfo.getType().name());
                sqlStatement.setString(5, labClientInfo.getTitle());
                sqlStatement.setString(6, labClientInfo.getVersion());
                sqlStatement.setString(7, labClientInfo.getDescription());
                sqlStatement.setString(8, labClientInfo.getLoaderScript());
                sqlStatement.setString(9, labClientInfo.getContactName());
                sqlStatement.setString(10, labClientInfo.getContactEmail());
                sqlStatement.setString(11, labClientInfo.getDocumentationUrl());
                sqlStatement.setString(12, labClientInfo.getNotes());

                /*
                 * Execute the stored procedure
                 */
                sqlStatement.execute();

                /*
                 * Get the result
                 */
                labClientId = (int) sqlStatement.getInt(1);
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
                String.format(STRLOG_LabClientId_arg, labClientId));

        return labClientId;
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
                success = (sqlStatement.getInt(1) == id);
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
     * @return int[]
     */
    public int[] GetListOfIds() {
        final String methodName = "GetListOfNames";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        int[] listArray = null;

        try {
            ArrayList<Integer> list = new ArrayList<>();
            Connection sqlConnection = this.dbConnection.getConnection();
            CallableStatement sqlStatement = null;

            try {
                /*
                 * Prepare the stored procedure call
                 */
                sqlStatement = sqlConnection.prepareCall(STRSQLCMD_GetListIds);

                /*
                 * Execute the stored procedure
                 */
                ResultSet resultSet = sqlStatement.executeQuery();

                /*
                 * Add result to the list
                 */
                while (resultSet.next() == true) {
                    list.add(resultSet.getInt(STRCOL_Id));
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
                listArray = new int[list.size()];
                for (int i = 0; i < listArray.length; i++) {
                    listArray[i] = list.get(i).intValue();
                }
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
     * @return String[]
     */
    public String[] GetListOfNames() {
        final String methodName = "GetListOfNames";
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
                sqlStatement = sqlConnection.prepareCall(STRSQLCMD_GetListNames);

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
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName,
                String.format(STRLOG_Count_arg, (listArray != null) ? listArray.length : 0));

        return listArray;
    }

    /**
     *
     * @param id
     * @return LabClientInfo
     */
    public LabClientInfo RetrieveById(int id) {
        ArrayList<LabClientInfo> list = this.RetrieveBy(STRCOL_Id, id, null);
        return (list != null) ? list.get(0) : null;
    }

    /**
     *
     * @param name
     * @return LabClientInfo
     */
    public LabClientInfo RetrieveByName(String name) {
        ArrayList<LabClientInfo> list = this.RetrieveBy(STRCOL_Name, 0, name);
        return (list != null) ? list.get(0) : null;
    }

    /**
     *
     * @param labClientInfo
     * @return boolean
     */
    public boolean Update(LabClientInfo labClientInfo) {
        final String methodName = "Update";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        boolean success = false;

        try {
            /*
             * Check that parameters are valid
             */
            if (labClientInfo == null) {
                throw new NullPointerException(LabClientInfo.class.getSimpleName());
            }

            Connection sqlConnection = this.dbConnection.getConnection();
            CallableStatement sqlStatement = null;

            try {
                /*
                 * Prepare the stored procedure call
                 */
                sqlStatement = sqlConnection.prepareCall(STRSQLCMD_Update);
                sqlStatement.registerOutParameter(1, Types.INTEGER);
                sqlStatement.setInt(2, labClientInfo.getId());
                sqlStatement.setString(3, labClientInfo.getName());
                sqlStatement.setString(4, labClientInfo.getType().name());
                sqlStatement.setString(5, labClientInfo.getTitle());
                sqlStatement.setString(6, labClientInfo.getVersion());
                sqlStatement.setString(7, labClientInfo.getDescription());
                sqlStatement.setString(8, labClientInfo.getLoaderScript());
                sqlStatement.setInt(9, labClientInfo.getAgentId());
                sqlStatement.setInt(10, labClientInfo.getEssId());
                sqlStatement.setInt(11, labClientInfo.getUssId());
                sqlStatement.setBoolean(12, labClientInfo.isReentrant());
                sqlStatement.setString(13, labClientInfo.getContactName());
                sqlStatement.setString(14, labClientInfo.getContactEmail());
                sqlStatement.setString(15, labClientInfo.getDocumentationUrl());
                sqlStatement.setString(16, labClientInfo.getNotes());

                /*
                 * Execute the stored procedure
                 */
                sqlStatement.execute();

                /*
                 * Get the result
                 */
                success = (sqlStatement.getInt(1) == labClientInfo.getId());
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
     * @param intval
     * @param strval
     * @return
     */
    private ArrayList<LabClientInfo> RetrieveBy(String columnName, int intval, String strval) {
        final String methodName = "RetrieveBy";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        ArrayList<LabClientInfo> labClientInfoList = new ArrayList<>();

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
                    LabClientInfo labClientInfo = new LabClientInfo();
                    labClientInfo.setId(resultSet.getInt(STRCOL_Id));
                    labClientInfo.setName(resultSet.getString(STRCOL_Name));
                    labClientInfo.setGuid(resultSet.getString(STRCOL_Guid));
                    labClientInfo.setType(LabClientTypes.valueOf(resultSet.getString(STRCOL_Type)));
                    labClientInfo.setTitle(resultSet.getString(STRCOL_Title));
                    labClientInfo.setVersion(resultSet.getString(STRCOL_Version));
                    labClientInfo.setDescription(resultSet.getString(STRCOL_Description));
                    labClientInfo.setLoaderScript(resultSet.getString(STRCOL_LoaderScript));
                    labClientInfo.setAgentId(resultSet.getInt(STRCOL_AgentId));
                    labClientInfo.setEssId(resultSet.getInt(STRCOL_EssId));
                    labClientInfo.setUssId(resultSet.getInt(STRCOL_UssId));
                    labClientInfo.setReentrant(resultSet.getBoolean(STRCOL_IsReentrant));
                    labClientInfo.setContactName(resultSet.getString(STRCOL_ContactName));
                    labClientInfo.setContactEmail(resultSet.getString(STRCOL_ContactEmail));
                    labClientInfo.setDocumentationUrl(resultSet.getString(STRCOL_DocumentationUrl));
                    labClientInfo.setNotes(resultSet.getString(STRCOL_Notes));

                    Timestamp timestamp = resultSet.getTimestamp(STRCOL_DateCreated);
                    if (timestamp != null) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(timestamp);
                        labClientInfo.setDateCreated(calendar);
                    }

                    timestamp = resultSet.getTimestamp(STRCOL_DateModified);
                    if (timestamp != null) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(timestamp);
                        labClientInfo.setDateModified(calendar);
                    }

                    /*
                     * Add the LabClientInfo to the list
                     */
                    labClientInfoList.add(labClientInfo);
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
                String.format(STRLOG_Count_arg, labClientInfoList.size()));

        return (labClientInfoList.size() > 0) ? labClientInfoList : null;
    }
}
