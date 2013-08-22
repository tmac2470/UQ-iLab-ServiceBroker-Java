/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uq.ilabs.servicebroker.client.types;

/**
 *
 * @author uqlpayne
 */
public class Group {

    private String name;
    private String description;
    private String[] labs;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String[] getLabs() {
        return labs;
    }

    public void setLabs(String[] labs) {
        this.labs = labs;
    }

    public Group() {
    }

    public Group(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
