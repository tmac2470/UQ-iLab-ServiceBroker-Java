/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uq.ilabs.library.servicebroker.database.types;

/**
 *
 * @author uqlpayne
 */
public class UserGroupInfo {

    private int id;
    private int userId;
    private int groupId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public UserGroupInfo() {
    }

    public UserGroupInfo(int userId, int groupId) {
        this.id = -1;
        this.userId = userId;
        this.groupId = groupId;
    }
}
