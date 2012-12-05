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

    AdministerProcessAgent(1),
    ManageProcessAgent(2),
    AuthenticateTicket(3),
    LabServerTicket(4),
    LabSchedulingServerTicket(5),
    UserSchedulingServerTicket(6),
    ExperimentStorageServerTicket(7),
    AuthenticateServiceBroker(8),
    AuthenticateProcessAgent(9),
    RedeemSession(10),
    AdministerESS(11),
    AdministerExperiment(12),
    StoreRecords(13),
    RetrieveRecords(14),
    AdministerUSS(15),
    ManageUSSGroup(16),
    ScheduleSession(17),
    RevokeReservation(18),
    AllowExperimentExecution(19),
    AdministerLSS(20),
    ManageLab(21),
    RequestReservation(22),
    RegisterLabServer(23),
    AdministerLabServer(24),
    ExecuteExperiment(25),
    CreateExperiment(26),
    RedeemReservation(27),
    AuthorizeAccess(28),
    AuthorizeClient(29),
    Unknown(-1);
    //
    //<editor-fold defaultstate="collapsed" desc="Properties">
    private final int value;

    public int getValue() {
        return value;
    }
    //</editor-fold>

    private TicketTypes(int value) {
        this.value = value;
    }

    /**
     *
     * @param value
     * @return TicketTypes
     */
    public static TicketTypes ToTicketType(int value) {
        if (value == AdministerProcessAgent.value) {
            return AdministerProcessAgent;
        }
        if (value == ManageProcessAgent.value) {
            return ManageProcessAgent;
        }
        if (value == AuthenticateTicket.value) {
            return AuthenticateTicket;
        }
        if (value == LabServerTicket.value) {
            return LabServerTicket;
        }
        if (value == LabSchedulingServerTicket.value) {
            return LabSchedulingServerTicket;
        }
        if (value == UserSchedulingServerTicket.value) {
            return UserSchedulingServerTicket;
        }
        if (value == ExperimentStorageServerTicket.value) {
            return ExperimentStorageServerTicket;
        }
        if (value == AuthenticateServiceBroker.value) {
            return AuthenticateServiceBroker;
        }
        if (value == AuthenticateProcessAgent.value) {
            return AuthenticateProcessAgent;
        }
        if (value == RedeemSession.value) {
            return RedeemSession;
        }
        if (value == AdministerESS.value) {
            return AdministerESS;
        }
        if (value == AdministerExperiment.value) {
            return AdministerExperiment;
        }
        if (value == StoreRecords.value) {
            return StoreRecords;
        }
        if (value == RetrieveRecords.value) {
            return RetrieveRecords;
        }
        if (value == AdministerUSS.value) {
            return AdministerUSS;
        }
        if (value == ManageUSSGroup.value) {
            return ManageUSSGroup;
        }
        if (value == ScheduleSession.value) {
            return ScheduleSession;
        }
        if (value == RevokeReservation.value) {
            return RevokeReservation;
        }
        if (value == AllowExperimentExecution.value) {
            return AllowExperimentExecution;
        }
        if (value == AdministerLSS.value) {
            return AdministerLSS;
        }
        if (value == ManageLab.value) {
            return ManageLab;
        }
        if (value == RequestReservation.value) {
            return RequestReservation;
        }
        if (value == RegisterLabServer.value) {
            return RegisterLabServer;
        }
        if (value == AdministerLabServer.value) {
            return AdministerLabServer;
        }
        if (value == ExecuteExperiment.value) {
            return ExecuteExperiment;
        }
        if (value == CreateExperiment.value) {
            return CreateExperiment;
        }
        if (value == RedeemReservation.value) {
            return RedeemReservation;
        }
        if (value == AuthorizeAccess.value) {
            return AuthorizeAccess;
        }
        if (value == AuthorizeClient.value) {
            return AuthorizeClient;
        }
        return Unknown;
    }

    /**
     *
     * @param ticketType
     * @return boolean
     */
    public static boolean IsAbstract(TicketTypes ticketType) {
        return ticketType.getValue() <= ExperimentStorageServerTicket.getValue();
    }
}
