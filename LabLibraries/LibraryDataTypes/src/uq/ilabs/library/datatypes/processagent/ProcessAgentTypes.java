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

    /**
     * Generic Process Agent
     */
    GPA(0),
    /**
     * NOT a process agent
     */
    NOT(1),
    /**
     * Interactive ServiceBroker
     */
    ISB(4),
    /**
     * Batch ServiceBroker
     */
    BSB(5),
    /**
     * Remote ServiceBroker
     */
    RSB(6),
    /**
     * Interactive LabServer
     */
    ILS(8),
    /**
     * Batch LabServer
     */
    BLS(9),
    /**
     * Experiment Storage Server
     */
    ESS(16),
    /**
     * User Scheduling Server
     */
    USS(32),
    /**
     * Lab Scheduling Server
     */
    LSS(64),
    /**
     * Authorization Service
     */
    AUTH(128);
    //
    //<editor-fold defaultstate="collapsed" desc="Properties">
    private final int value;

    public int getValue() {
        return value;
    }
    //</editor-fold>

    private ProcessAgentTypes(int value) {
        this.value = value;
    }

    /**
     *
     * @param value
     * @return ProcessAgentTypes
     */
    public static ProcessAgentTypes ToProcessAgentType(int value) {
        switch (value) {
            case 0:
                return GPA;
            case 1:
                return NOT;
            case 4:
                return ISB;
            case 5:
                return BSB;
            case 6:
                return RSB;
            case 8:
                return ILS;
            case 9:
                return BLS;
            case 16:
                return ESS;
            case 32:
                return USS;
            case 64:
                return LSS;
            case 128:
                return AUTH;
            default:
                return NOT;
        }
    }

    /*
     * String constants
     */
    private static final String STR_TypeName_GPA = "GENERIC PA";
    private static final String STR_TypeName_NOT = "NOT A PA";
    private static final String STR_TypeName_ISB = "SERVICE BROKER";
    private static final String STR_TypeName_BSB = "BATCH SERVICE BROKER";
    private static final String STR_TypeName_RSB = "REMOTE SERVICE BROKER";
    private static final String STR_TypeName_ILS = "LAB SERVER";
    private static final String STR_TypeName_BLS = "BATCH LAB SERVER";
    private static final String STR_TypeName_ESS = "EXPERIMENT STORAGE SERVER";
    private static final String STR_TypeName_USS = "SCHEDULING SERVER";
    private static final String STR_TypeName_LSS = "LAB SCHEDULING SERVER";
    private static final String STR_TypeName_AUTH = "AUTHORIZATION SERVICE";

    /**
     *
     * @param processAgentType
     * @return String
     */
    public static String ToProcessAgentTypeName(ProcessAgentTypes processAgentType) {
        switch (processAgentType) {
            case GPA:
                return STR_TypeName_GPA;
            case NOT:
                return STR_TypeName_NOT;
            case ISB:
                return STR_TypeName_ISB;
            case BSB:
                return STR_TypeName_BSB;
            case RSB:
                return STR_TypeName_RSB;
            case ILS:
                return STR_TypeName_ILS;
            case BLS:
                return STR_TypeName_BLS;
            case ESS:
                return STR_TypeName_ESS;
            case USS:
                return STR_TypeName_USS;
            case LSS:
                return STR_TypeName_LSS;
            case AUTH:
                return STR_TypeName_AUTH;
            default:
                return null;
        }
    }

    /**
     *
     * @param typeName
     * @return
     */
    public static ProcessAgentTypes ToProcessAgentType(String typeName) {
        switch (typeName) {
            case STR_TypeName_GPA:
                return GPA;
            case STR_TypeName_NOT:
                return NOT;
            case STR_TypeName_ISB:
                return ISB;
            case STR_TypeName_BSB:
                return BSB;
            case STR_TypeName_RSB:
                return RSB;
            case STR_TypeName_ILS:
                return ILS;
            case STR_TypeName_BLS:
                return BLS;
            case STR_TypeName_ESS:
                return ESS;
            case STR_TypeName_USS:
                return USS;
            case STR_TypeName_LSS:
                return LSS;
            case STR_TypeName_AUTH:
                return AUTH;
            default:
                return NOT;
        }
    }
}
