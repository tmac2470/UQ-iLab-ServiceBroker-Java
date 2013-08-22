/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uq.ilabs.library.servicebroker.database.types;

/**
 *
 * @author uqlpayne
 */
public class GroupsHierachyInfo {

    private int id;
    private int groupId;
    private int parentId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public GroupsHierachyInfo() {
        this.id = -1;
    }

    public GroupsHierachyInfo(int groupId, int parentId) {
        this();
        this.groupId = groupId;
        this.parentId = parentId;
    }
}
