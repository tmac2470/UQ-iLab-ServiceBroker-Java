/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uq.ilabs.library.servicebroker.engine.types;

/**
 *
 * @author uqlpayne
 */
public enum AffiliationTypes {

    Student(1),
    Faculty(2),
    Guest(3),
    Other(4),
    Unknown(-1);
    //
    private static final AffiliationTypes[] TYPES = {
        Student,
        Faculty,
        Guest,
        Other
    };
    //
    public static final String[] STRINGS = {
        "Student",
        "Faculty/Staff",
        "Guest",
        "Other"
    };
    //
    //<editor-fold defaultstate="collapsed" desc="Properties">
    private final int value;

    public int getValue() {
        return value;
    }
    //</editor-fold>

    /**
     * Constructor
     *
     * @param value
     */
    private AffiliationTypes(int value) {
        this.value = value;
    }

    /**
     *
     * @param value
     * @return AffiliationTypes
     */
    public static AffiliationTypes ToType(int value) {
        /*
         * Search for the value
         */
        for (AffiliationTypes affiliationType : TYPES) {
            if (affiliationType.getValue() == value) {
                return affiliationType;
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
     * @return AffiliationTypes
     */
    public static AffiliationTypes ToType(String value) {
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
