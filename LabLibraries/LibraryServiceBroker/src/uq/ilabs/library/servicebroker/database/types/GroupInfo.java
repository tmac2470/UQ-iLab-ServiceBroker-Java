/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uq.ilabs.library.servicebroker.database.types;

import java.util.Calendar;
import uq.ilabs.library.servicebroker.engine.types.GroupTypes;

/**
 *
 * @author uqlpayne
 */
public class GroupInfo {

    private int id;
    private String name;
    private GroupTypes type;
    private boolean request;
    private String description;
    private Calendar dateCreated;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public GroupTypes getType() {
        return type;
    }

    public void setType(GroupTypes type) {
        this.type = type;
    }

    public boolean isRequest() {
        return request;
    }

    public void setRequest(boolean request) {
        this.request = request;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Calendar getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Calendar dateCreated) {
        this.dateCreated = dateCreated;
    }

    public GroupInfo() {
        this.id = -1;
    }

    public GroupInfo(String name, GroupTypes type, boolean request, String description) {
        this();
        this.name = name;
        this.type = type;
        this.request = request;
        this.description = description;
    }
}
