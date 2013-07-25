/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uq.ilabs.library.datatypes.ticketing;

/**
 *
 * @author uqlpayne
 */
public enum TicketTypes {

    /*
     * Abstract tickets
     */
    AdministerProcessAgent(1),
    ManageProcessAgent(2),
    Authenticate(3),
    LabServer(4),
    LabSchedulingServer(5),
    UserSchedulingServer(6),
    ExperimentStorageServer(7),
    /*
     * Authentication
     */
    AuthenticateServiceBroker(8),
    AuthenticateProcessAgent(9),
    /*
     * Redeem session
     */
    RedeemSession(10),
    /*
     * ESS
     */
    AdministerESS(11),
    AdministerExperiment(12),
    StoreRecords(13),
    RetrieveRecords(14),
    /*
     * USS
     */
    AdministerUSS(15),
    ManageUSSGroup(16),
    ScheduleSession(17),
    RedeemReservation(27),
    RevokeReservation(18),
    AllowExperimentExecution(19),
    /*
     * LSS
     */
    AdministerLSS(20),
    ManageLab(21),
    RequestReservation(22),
    RegisterLabServer(23),
    /*
     * LS
     */
    AdministerLabServer(24),
    ExecuteExperiment(25),
    CreateExperiment(26),
    AuthorizeAccess(28),
    AuthorizeClient(29),
    /*
     * Whatever's left
     */
    Unknown(-1);
    //
    private static final TicketTypes[] TYPES = {
        AdministerProcessAgent,
        ManageProcessAgent,
        Authenticate,
        LabServer,
        LabSchedulingServer,
        UserSchedulingServer,
        ExperimentStorageServer,
        AuthenticateServiceBroker,
        AuthenticateProcessAgent,
        RedeemSession,
        AdministerESS,
        AdministerExperiment,
        StoreRecords,
        RetrieveRecords,
        AdministerUSS,
        ManageUSSGroup,
        ScheduleSession,
        RedeemReservation,
        RevokeReservation,
        AllowExperimentExecution,
        AdministerLSS,
        ManageLab,
        RequestReservation,
        RegisterLabServer,
        AdministerLabServer,
        ExecuteExperiment,
        CreateExperiment,
        AuthorizeAccess,
        AuthorizeClient,
        Unknown
    };
    //
    private static final String[] STRINGS = {
        "ADMINISTER PA",
        "MANAGE PA",
        "AUTHENTICATE",
        "LS",
        "LSS",
        "USS",
        "ESS",
        "AUTHENTICATE SERVICE BROKER",
        "AUTHENTICATE AGENT",
        "REDEEM SESSION",
        "ADMINISTER ESS",
        "ADMINISTER EXPERIMENT",
        "STORE RECORDS",
        "RETRIEVE RECORDS",
        "ADMINISTER USS",
        "MANAGE USS GROUP",
        "SCHEDULE SESSION",
        "REDEEM RESERVATION",
        "REVOKE RESERVATION",
        "ALLOW EXPERIMENT EXECUTION",
        "ADMINISTER LSS",
        "MANAGE LAB",
        "REQUEST RESERVATION",
        "REGISTER LS",
        "ADMINISTER LS",
        "EXECUTE EXPERIMENT",
        "CREATE EXPERIMENT",
        "AUTHORIZE ACCESS",
        "AUTHORIZE CLIENT",
        "UNKNOWN"
    };
    //<editor-fold defaultstate="collapsed" desc="Properties">
    private final int value;

    public int getValue() {
        return value;
    }

    public boolean isAbstract() {
        return this.getValue() <= ExperimentStorageServer.getValue();
    }
    //</editor-fold>

    /**
     * Constructor
     *
     * @param value
     */
    private TicketTypes(int value) {
        this.value = value;
    }

    /**
     *
     * @param value
     * @return TicketTypes
     */
    public static TicketTypes ToType(int value) {
        /*
         * Search for the value
         */
        for (TicketTypes ticketType : TicketTypes.values()) {
            if (ticketType.getValue() == value) {
                return ticketType;
            }
        }

        /*
         * Value not found
         */
        return Unknown;
    }

    public static TicketTypes ToType(String value) {
        for (int i = 0; i < STRINGS.length; i++) {
            if (STRINGS[i].equals(value)) {
                return TYPES[i];
            }
        }
        return Unknown;
    }

    @Override
    public String toString() {
        return STRINGS[this.ordinal()];
    }
}
