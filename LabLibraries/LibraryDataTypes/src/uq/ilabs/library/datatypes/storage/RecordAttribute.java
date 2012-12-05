/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uq.ilabs.library.datatypes.storage;

/**
 *
 * @author uqlpayne
 */
public class RecordAttribute {

    /**
     * The attribute name
     */
    private String name;
    /**
     * The attribute value
     */
    private String value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public RecordAttribute() {
    }

    public RecordAttribute(String name, String value) {
        this.name = name;
        this.value = value;
    }
}
