/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uq.ilabs.library.processagent.ticketing;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

/**
 *
 * @author uqlpayne
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _AdministerESSPayload_QNAME = new QName("http://ilab.mit.edu/iLabs/tickets", "AdministerESSPayload");
    private final static QName _AdministerExperimentPayload_QNAME = new QName("http://ilab.mit.edu/iLabs/tickets", "AdministerExperimentPayload");
    private final static QName _AdministerLSPayload_QNAME = new QName("http://ilab.mit.edu/iLabs/tickets", "AdministerLSPayload");
    private final static QName _AdministerLSSPayload_QNAME = new QName("http://ilab.mit.edu/iLabs/tickets", "AdministerLSSPayload");
    private final static QName _AdministerUSSPayload_QNAME = new QName("http://ilab.mit.edu/iLabs/tickets", "AdministerUSSPayload");
    private final static QName _AllowExperimentExecutionPayload_QNAME = new QName("http://ilab.mit.edu/iLabs/tickets", "AllowExperimentExecutionPayload");
    private final static QName _AuthenticateAgentPayload_QNAME = new QName("http://ilab.mit.edu/iLabs/tickets", "AuthenticateAgentPayload");
    private final static QName _AuthenticateSBPayload_QNAME = new QName("http://ilab.mit.edu/iLabs/tickets", "AuthenticateSBPayload");
    private final static QName _AuthorizeClientPayload_QNAME = new QName("http://ilab.mit.edu/iLabs/tickets", "AuthorizeClientPayload");
    private final static QName _CreateExperimentPayload_QNAME = new QName("http://ilab.mit.edu/iLabs/tickets", "CreateExperimentPayload");
    private final static QName _ExecuteExperimentPayload_QNAME = new QName("http://ilab.mit.edu/iLabs/tickets", "ExecuteExperimentPayload");
    private final static QName _ManageLabPayload_QNAME = new QName("http://ilab.mit.edu/iLabs/tickets", "ManageLabPayload");
    private final static QName _ManageUSSGroupPayload_QNAME = new QName("http://ilab.mit.edu/iLabs/tickets", "ManageUSSGroupPayload");
    private final static QName _RedeemReservationPayload_QNAME = new QName("http://ilab.mit.edu/iLabs/tickets", "RedeemReservationPayload");
    private final static QName _RedeemSessionPayload_QNAME = new QName("http://ilab.mit.edu/iLabs/tickets", "RedeemSessionPayload");
    private final static QName _RegisterLSPayload_QNAME = new QName("http://ilab.mit.edu/iLabs/tickets", "RegisterLSPayload");
    private final static QName _RequestReservationPayload_QNAME = new QName("http://ilab.mit.edu/iLabs/tickets", "RequestReservationPayload");
    private final static QName _RetrieveRecordsPayload_QNAME = new QName("http://ilab.mit.edu/iLabs/tickets", "RetrieveRecordsPayload");
    private final static QName _RevokeReservationPayload_QNAME = new QName("http://ilab.mit.edu/iLabs/tickets", "RevokeReservationPayload");
    private final static QName _ScheduleSessionPayload_QNAME = new QName("http://ilab.mit.edu/iLabs/tickets", "ScheduleSessionPayload");
    private final static QName _StoreRecordsPayload_QNAME = new QName("http://ilab.mit.edu/iLabs/tickets", "StoreRecordsPayload");

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AuthenticateSBPayload }{@code >}}
     */
    @XmlElementDecl(namespace = "http://ilab.mit.edu/iLabs/tickets", name = "AdministerESSPayload")
    public JAXBElement<AdministerESSPayload> createAdministerESSPayload(AdministerESSPayload value) {
        return new JAXBElement<>(_AdministerESSPayload_QNAME, AdministerESSPayload.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AdministerExperimentPayload }{@code >}}
     */
    @XmlElementDecl(namespace = "http://ilab.mit.edu/iLabs/tickets", name = "AdministerExperimentPayload")
    public JAXBElement<AdministerExperimentPayload> createAdministerExperimentPayload(AdministerExperimentPayload value) {
        return new JAXBElement<>(_AdministerExperimentPayload_QNAME, AdministerExperimentPayload.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AdministerLSPayload }{@code >}}
     */
    @XmlElementDecl(namespace = "http://ilab.mit.edu/iLabs/tickets", name = "AdministerLSPayload")
    public JAXBElement<AdministerLSPayload> createAdministerLSPayload(AdministerLSPayload value) {
        return new JAXBElement<>(_AdministerLSPayload_QNAME, AdministerLSPayload.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AdministerLSSPayload }{@code >}}
     */
    @XmlElementDecl(namespace = "http://ilab.mit.edu/iLabs/tickets", name = "AdministerLSSPayload")
    public JAXBElement<AdministerLSSPayload> createAdministerLSSPayload(AdministerLSSPayload value) {
        return new JAXBElement<>(_AdministerLSSPayload_QNAME, AdministerLSSPayload.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AdministerUSSPayload }{@code >}}
     */
    @XmlElementDecl(namespace = "http://ilab.mit.edu/iLabs/tickets", name = "AdministerUSSPayload")
    public JAXBElement<AdministerUSSPayload> createAdministerUSSPayload(AdministerUSSPayload value) {
        return new JAXBElement<>(_AdministerUSSPayload_QNAME, AdministerUSSPayload.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AllowExperimentExecutionPayload }{@code >}}
     */
    @XmlElementDecl(namespace = "http://ilab.mit.edu/iLabs/tickets", name = "AllowExperimentExecutionPayload")
    public JAXBElement<AllowExperimentExecutionPayload> createAllowExperimentExecutionPayload(AllowExperimentExecutionPayload value) {
        return new JAXBElement<>(_AllowExperimentExecutionPayload_QNAME, AllowExperimentExecutionPayload.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AuthenticateSBPayload }{@code >}}
     */
    @XmlElementDecl(namespace = "http://ilab.mit.edu/iLabs/tickets", name = "AuthenticateAgentPayload")
    public JAXBElement<AuthenticateAgentPayload> createAuthenticateAgentPayload(AuthenticateAgentPayload value) {
        return new JAXBElement<>(_AuthenticateAgentPayload_QNAME, AuthenticateAgentPayload.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AuthenticateSBPayload }{@code >}}
     */
    @XmlElementDecl(namespace = "http://ilab.mit.edu/iLabs/tickets", name = "AuthenticateSBPayload")
    public JAXBElement<AuthenticateSBPayload> createAuthenticateSBPayload(AuthenticateSBPayload value) {
        return new JAXBElement<>(_AuthenticateSBPayload_QNAME, AuthenticateSBPayload.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AuthorizeClientPayload }{@code >}}
     */
    @XmlElementDecl(namespace = "http://ilab.mit.edu/iLabs/tickets", name = "AuthorizeClientPayload")
    public JAXBElement<AuthorizeClientPayload> createAuthorizeClientPayload(AuthorizeClientPayload value) {
        return new JAXBElement<>(_AuthorizeClientPayload_QNAME, AuthorizeClientPayload.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CreateExperimentPayload }{@code >}}
     */
    @XmlElementDecl(namespace = "http://ilab.mit.edu/iLabs/tickets", name = "CreateExperimentPayload")
    public JAXBElement<CreateExperimentPayload> createCreateExperimentPayload(CreateExperimentPayload value) {
        return new JAXBElement<>(_CreateExperimentPayload_QNAME, CreateExperimentPayload.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ExecuteExperimentPayload }{@code >}}
     */
    @XmlElementDecl(namespace = "http://ilab.mit.edu/iLabs/tickets", name = "ExecuteExperimentPayload")
    public JAXBElement<ExecuteExperimentPayload> createExecuteExperimentPayload(ExecuteExperimentPayload value) {
        return new JAXBElement<>(_ExecuteExperimentPayload_QNAME, ExecuteExperimentPayload.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ManageLabPayload }{@code >}}
     */
    @XmlElementDecl(namespace = "http://ilab.mit.edu/iLabs/tickets", name = "ManageLabPayload")
    public JAXBElement<ManageLabPayload> createManageLabPayload(ManageLabPayload value) {
        return new JAXBElement<>(_ManageLabPayload_QNAME, ManageLabPayload.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ManageUSSGroupPayload }{@code >}}
     */
    @XmlElementDecl(namespace = "http://ilab.mit.edu/iLabs/tickets", name = "ManageUSSGroupPayload")
    public JAXBElement<ManageUSSGroupPayload> createManageUSSGroupPayload(ManageUSSGroupPayload value) {
        return new JAXBElement<>(_ManageUSSGroupPayload_QNAME, ManageUSSGroupPayload.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RedeemReservationPayload }{@code >}}
     */
    @XmlElementDecl(namespace = "http://ilab.mit.edu/iLabs/tickets", name = "RedeemReservationPayload")
    public JAXBElement<RedeemReservationPayload> createRedeemReservationPayload(RedeemReservationPayload value) {
        return new JAXBElement<>(_RedeemReservationPayload_QNAME, RedeemReservationPayload.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RedeemSessionPayload }{@code >}}
     */
    @XmlElementDecl(namespace = "http://ilab.mit.edu/iLabs/tickets", name = "RedeemSessionPayload")
    public JAXBElement<RedeemSessionPayload> createRedeemSessionPayload(RedeemSessionPayload value) {
        return new JAXBElement<>(_RedeemSessionPayload_QNAME, RedeemSessionPayload.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RegisterLSPayload }{@code >}}
     */
    @XmlElementDecl(namespace = "http://ilab.mit.edu/iLabs/tickets", name = "RegisterLSPayload")
    public JAXBElement<RegisterLSPayload> createRegisterLSPayload(RegisterLSPayload value) {
        return new JAXBElement<>(_RegisterLSPayload_QNAME, RegisterLSPayload.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RequestReservationPayload }{@code >}}
     */
    @XmlElementDecl(namespace = "http://ilab.mit.edu/iLabs/tickets", name = "RequestReservationPayload")
    public JAXBElement<RequestReservationPayload> createRequestReservationPayload(RequestReservationPayload value) {
        return new JAXBElement<>(_RequestReservationPayload_QNAME, RequestReservationPayload.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RetrieveRecordsPayload }{@code >}}
     */
    @XmlElementDecl(namespace = "http://ilab.mit.edu/iLabs/tickets", name = "RetrieveRecordsPayload")
    public JAXBElement<RetrieveRecordsPayload> createRetrieveRecordsPayload(RetrieveRecordsPayload value) {
        return new JAXBElement<>(_RetrieveRecordsPayload_QNAME, RetrieveRecordsPayload.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RevokeReservationPayload }{@code >}}
     */
    @XmlElementDecl(namespace = "http://ilab.mit.edu/iLabs/tickets", name = "RevokeReservationPayload")
    public JAXBElement<RevokeReservationPayload> createRevokeReservationPayload(RevokeReservationPayload value) {
        return new JAXBElement<>(_RevokeReservationPayload_QNAME, RevokeReservationPayload.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ScheduleSessionPayload }{@code >}}
     */
    @XmlElementDecl(namespace = "http://ilab.mit.edu/iLabs/tickets", name = "ScheduleSessionPayload")
    public JAXBElement<ScheduleSessionPayload> createScheduleSessionPayload(ScheduleSessionPayload value) {
        return new JAXBElement<>(_ScheduleSessionPayload_QNAME, ScheduleSessionPayload.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link StoreRecordsPayload }{@code >}}
     */
    @XmlElementDecl(namespace = "http://ilab.mit.edu/iLabs/tickets", name = "StoreRecordsPayload")
    public JAXBElement<StoreRecordsPayload> createStoreRecordsPayload(StoreRecordsPayload value) {
        return new JAXBElement<>(_StoreRecordsPayload_QNAME, StoreRecordsPayload.class, null, value);
    }
}
