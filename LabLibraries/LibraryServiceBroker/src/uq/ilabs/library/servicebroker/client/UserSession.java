/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uq.ilabs.library.servicebroker.client;

import uq.ilabs.library.lab.database.DBConnection;
import uq.ilabs.library.servicebroker.database.ExperimentsDB;
import uq.ilabs.library.servicebroker.database.GroupsDB;
import uq.ilabs.library.servicebroker.database.LabClientGroupsDB;
import uq.ilabs.library.servicebroker.database.LabClientsDB;
import uq.ilabs.library.servicebroker.database.UserGroupsDB;
import uq.ilabs.library.servicebroker.database.UsersDB;

/**
 *
 * @author uqlpayne
 */
public class UserSession {

    private int userId;
    private int groupId;
    private String username;
    private String groupname;
    private int timezone;
    private boolean admin;
    private boolean serviceAdmin;
    private ExperimentsDB experimentsDB;
    private GroupsDB groupsDB;
    private UsersDB usersDB;
    private UserGroupsDB userGroupsDB;
    private LabClientsDB labClientsDB;
    private LabClientGroupsDB labClientGroupsDB;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getGroupname() {
        return groupname;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }

    public int getTimezone() {
        return timezone;
    }

    public void setTimezone(int timezone) {
        this.timezone = timezone;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public boolean isServiceAdmin() {
        return serviceAdmin;
    }

    public void setServiceAdmin(boolean serviceAdmin) {
        this.serviceAdmin = serviceAdmin;
    }

    public ExperimentsDB getExperimentsDB() {
        return experimentsDB;
    }

    public GroupsDB getGroupsDB() {
        return groupsDB;
    }

    public UsersDB getUsersDB() {
        return usersDB;
    }

    public UserGroupsDB getUserGroupsDB() {
        return userGroupsDB;
    }

    public LabClientsDB getLabClientsDB() {
        return labClientsDB;
    }

    public LabClientGroupsDB getLabClientGroupsDB() {
        return labClientGroupsDB;
    }

    public UserSession(DBConnection dbConnection) {
        try {
            this.experimentsDB = new ExperimentsDB(dbConnection);
            this.groupsDB = new GroupsDB(dbConnection);
            this.usersDB = new UsersDB(dbConnection);
            this.userGroupsDB = new UserGroupsDB(dbConnection);
            this.labClientsDB = new LabClientsDB(dbConnection);
            this.labClientGroupsDB = new LabClientGroupsDB(dbConnection);
        } catch (Exception ex) {
        }
    }
}
