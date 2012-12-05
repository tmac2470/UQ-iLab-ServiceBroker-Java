/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uq.ilabs.library.datatypes;

/**
 *
 * @author uqlpayne
 */
public class Criterion {

    /**
     * A named attribute
     */
    private String attribute;
    /**
     * A predicate expressed as a string
     */
    private String predicate;
    /**
     * A constant value expressed as a string
     */
    private String value;

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public String getPredicate() {
        return predicate;
    }

    public void setPredicate(String predicate) {
        this.predicate = predicate;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Criterion() {
    }

    public Criterion(String attribute, String predicate, String value) {
        this.attribute = attribute;
        this.predicate = predicate;
        this.value = value;
    }

    /**
     *
     * @return
     */
    public String ToSQL() {
        return String.format("( %s %s '%s') ", this.attribute, this.predicate, this.value);
    }
}
