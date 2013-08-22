/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uq.ilabs.labsidescheduling.client.types;

/**
 *
 * @author uqlpayne
 */
public class ListItem {

    private String label;
    private String value;

    public String getLabel() {
        return label;
    }

    public String getValue() {
        return value;
    }

    public ListItem(String label, String value) {
        this.label = label;
        this.value = value;
    }
}
