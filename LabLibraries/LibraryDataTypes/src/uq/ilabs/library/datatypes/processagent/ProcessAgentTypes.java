/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uq.ilabs.library.datatypes.processagent;

/**
 *
 * @author uqlpayne
 */
public enum ProcessAgentTypes {

    GPA(0),
    NOT(1),
    ISB(4),
    BSB(5),
    RSB(6),
    ILS(8),
    BLS(9),
    ESS(16),
    USS(32),
    LSS(64),
    AUTH(128),
    UNKNOWN(256);
    //
    private static final ProcessAgentTypes[] TYPES = {
        GPA,
        NOT,
        ISB,
        BSB,
        RSB,
        ILS,
        BLS,
        ESS,
        USS,
        LSS,
        AUTH,
        UNKNOWN
    };
    private static final String[] STRINGS = {
        "GENERIC PA",
        "NOT A PA",
        "SERVICE BROKER",
        "BATCH SERVICE BROKER",
        "REMOTE SERVICE BROKER",
        "LAB SERVER",
        "BATCH LAB SERVER",
        "EXPERIMENT STORAGE SERVER",
        "SCHEDULING SERVER",
        "LAB SCHEDULING SERVER",
        "AUTHORIZATION SERVICE",
        "UNKNOWN"
    };
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
    private ProcessAgentTypes(int value) {
        this.value = value;
    }

    /**
     *
     * @param value
     * @return ProcessAgentTypes
     */
    public static ProcessAgentTypes ToType(int value) {
        for (ProcessAgentTypes type : ProcessAgentTypes.values()) {
            if (type.getValue() == value) {
                return type;
            }
        }
        return UNKNOWN;
    }

    /**
     * 
     * @param value
     * @return ProcessAgentTypes
     */
    public static ProcessAgentTypes ToType(String value) {
        for (int i = 0; i < STRINGS.length; i++) {
            if (STRINGS[i].equals(value)) {
                return TYPES[i];
            }
        }
        return UNKNOWN;
    }

    @Override
    public String toString() {
        return STRINGS[this.ordinal()];
    }
}
