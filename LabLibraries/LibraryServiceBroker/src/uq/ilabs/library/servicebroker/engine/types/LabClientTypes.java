/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uq.ilabs.library.servicebroker.engine.types;

/**
 *
 * @author uqlpayne
 */
public enum LabClientTypes {

    BatchApplet(1),
    BatchRedirect(2),
    InteractiveApplet(3),
    InteractiveRedirect(4),
    Unknown(-1);
    //
    private static final LabClientTypes[] TYPES = {
        BatchApplet,
        BatchRedirect,
        InteractiveApplet,
        InteractiveRedirect
    };
    //
    public static final String[] STRINGS = {
        "Batch Applet",
        "Batch Redirect",
        "Interactive Applet",
        "Interactive Redirect"
    };
    //
    private final int value;

    public int getValue() {
        return value;
    }

    public boolean isInteractive() {
        return (this.getValue() >= InteractiveApplet.getValue());
    }

    /**
     * Constructor
     *
     * @param value
     */
    private LabClientTypes(int value) {
        this.value = value;
    }

    /**
     *
     * @param value - Integer value of the enumerated type
     * @return LabClientTypes
     */
    public static LabClientTypes ToType(int value) {
        /*
         * Search for the value
         */
        for (LabClientTypes labClientType : TYPES) {
            if (labClientType.getValue() == value) {
                return labClientType;
            }
        }

        /*
         * Value not found
         */
        return Unknown;
    }

    /**
     *
     * @param value
     * @return LabClientTypes
     */
    public static LabClientTypes ToType(String value) {
        /*
         * Search for the value
         */
        for (int i = 0; i < STRINGS.length; i++) {
            if (STRINGS[i].equals(value)) {
                return TYPES[i];
            }
        }

        /*
         * Value not found
         */
        return Unknown;
    }

    @Override
    public String toString() {
        return (this.ordinal() < STRINGS.length) ? STRINGS[this.ordinal()] : null;
    }
}
