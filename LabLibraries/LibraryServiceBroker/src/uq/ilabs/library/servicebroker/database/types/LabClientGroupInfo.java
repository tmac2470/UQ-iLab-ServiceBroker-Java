/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uq.ilabs.library.servicebroker.database.types;

/**
 *
 * @author uqlpayne
 */
public class LabClientGroupInfo {

    private int id;
    private int labClientId;
    private int groupId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLabClientId() {
        return labClientId;
    }

    public void setLabClientId(int labClientId) {
        this.labClientId = labClientId;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public LabClientGroupInfo() {
    }

    public LabClientGroupInfo(int labClientId, int groupId) {
        this.id = -1;
        this.labClientId = labClientId;
        this.groupId = groupId;
    }
}
