/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uq.ilabs.servicebroker.client;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.logging.Level;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.ViewExpiredException;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import uq.ilabs.library.datatypes.processagent.ProcessAgentTypes;
import uq.ilabs.library.datatypes.storage.ExperimentRecord;
import uq.ilabs.library.datatypes.ticketing.Coupon;
import uq.ilabs.library.experimentstorage.ExperimentStorageAPI;
import uq.ilabs.library.lab.utilities.Logfile;
import uq.ilabs.library.processagent.database.types.ProcessAgentInfo;
import uq.ilabs.library.servicebroker.client.Consts;
import uq.ilabs.library.servicebroker.client.ServiceBrokerSession;
import uq.ilabs.library.servicebroker.client.UserSession;
import uq.ilabs.library.servicebroker.database.types.ExperimentInfo;
import uq.ilabs.library.servicebroker.database.types.GroupInfo;
import uq.ilabs.library.servicebroker.database.types.LabClientInfo;
import uq.ilabs.library.servicebroker.database.types.UserInfo;
import uq.ilabs.library.servicebroker.engine.ServiceManagement;
import uq.ilabs.library.servicebroker.ticketissuer.TicketIssuer;
import uq.ilabs.servicebroker.client.types.ListItem;

/**
 *
 * @author uqlpayne
 */
@Named(value = "myExperimentsBean")
@SessionScoped
public class MyExperimentsBean implements Serializable {

    //<editor-fold defaultstate="collapsed" desc="Constants">
    private static final String STR_ClassName = MyLabsBean.class.getName();
    private static final Level logLevel = Level.CONFIG;
    /*
     * String constants
     */
    private static final String STR_ExperimentOneLineSummary_arg3 = "%d:  %s  [%s]";
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Variables">
    private ServiceBrokerSession serviceBrokerSession;
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Properties">
    private String hsomExperimentId;
    private String hitExperimentId;
    private String hitLabClient;
    private String hitLabServer;
    private String hitUser;
    private String hitGroup;
    private String hitStatus;
    private String hitSubmissionTime;
    private String hitCompletionTime;
    private String hitTotalRecords;
    //
    private ArrayList<ListItem> experimentsList;
    private ExperimentRecord[] experimentRecords;
    private boolean hcbRecordsDisabled;
    private boolean hpgRecordsRendered;
    private String holMessage;
    private String holMessageClass;

    public String getHsomExperimentId() {
        return hsomExperimentId;
    }

    public void setHsomExperimentId(String hsomExperimentId) {
        this.hsomExperimentId = hsomExperimentId;
    }

    public String getHitExperimentId() {
        return hitExperimentId;
    }

    public void setHitExperimentId(String hitExperimentId) {
        this.hitExperimentId = hitExperimentId;
    }

    public String getHitLabClient() {
        return hitLabClient;
    }

    public void setHitLabClient(String hitLabClient) {
        this.hitLabClient = hitLabClient;
    }

    public String getHitLabServer() {
        return hitLabServer;
    }

    public void setHitLabServer(String hitLabServer) {
        this.hitLabServer = hitLabServer;
    }

    public String getHitUser() {
        return hitUser;
    }

    public void setHitUser(String hitUser) {
        this.hitUser = hitUser;
    }

    public String getHitGroup() {
        return hitGroup;
    }

    public void setHitGroup(String hitGroup) {
        this.hitGroup = hitGroup;
    }

    public String getHitStatus() {
        return hitStatus;
    }

    public void setHitStatus(String hitStatus) {
        this.hitStatus = hitStatus;
    }

    public String getHitSubmissionTime() {
        return hitSubmissionTime;
    }

    public void setHitSubmissionTime(String hitSubmissionTime) {
        this.hitSubmissionTime = hitSubmissionTime;
    }

    public String getHitCompletionTime() {
        return hitCompletionTime;
    }

    public void setHitCompletionTime(String hitCompletionTime) {
        this.hitCompletionTime = hitCompletionTime;
    }

    public String getHitTotalRecords() {
        return hitTotalRecords;
    }

    public void setHitTotalRecords(String hitTotalRecords) {
        this.hitTotalRecords = hitTotalRecords;
    }

    public ArrayList<ListItem> getExperimentsList() {
        return experimentsList;
    }

    public ExperimentRecord[] getExperimentRecords() {
        return experimentRecords;
    }

    public boolean isHcbRecordsDisabled() {
        return hcbRecordsDisabled;
    }

    public boolean isHpgRecordsRendered() {
        return hpgRecordsRendered;
    }

    public String getHolMessage() {
        return holMessage;
    }

    public String getHolMessageClass() {
        return holMessageClass;
    }
    //</editor-fold>

    /**
     * Creates a new instance of MyExperimentsBean
     */
    public MyExperimentsBean() {
        this.serviceBrokerSession = (ServiceBrokerSession) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(Consts.STRSSN_ServiceBroker);
    }

    /**
     *
     */
    public void pageLoad() {
        final String methodName = "pageLoad";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        /*
         * Check if user is logged in
         */
        if (this.serviceBrokerSession.getUserSession() == null) {
            throw new ViewExpiredException();
        }

        if (FacesContext.getCurrentInstance().isPostback() == false) {
            /*
             * Not a postback, initialise page controls
             */
            this.experimentsList = this.CreateExperimentList();

            /*
             * Clear the page
             */
            this.actionNew();
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);
    }

    /**
     *
     * @return
     */
    public String actionSelect() {

        this.ShowMessageInfo(null);

        try {
            /*
             * Get experiment information for the selected experiment
             */
            long experimentId = Long.parseLong(this.hsomExperimentId);
            this.PopulateExperimentInfo(experimentId);
        } catch (NumberFormatException ex) {
        }

        /* Navigate to the current page */
        return null;
    }

    /**
     *
     * @return
     */
    public String actionShowRecords() {

        this.ShowMessageInfo(null);

        try {
            /*
             * Get experiment information for the selected experiment
             */
            long experimentId = Long.parseLong(this.hsomExperimentId);
            ExperimentInfo experimentInfo = this.serviceBrokerSession.getUserSession().getExperimentsDB().Retrieve(experimentId);

            /*
             * Get the ESS ProcessAgentInfo for the specified experiment
             */
            ServiceManagement serviceManagement = this.serviceBrokerSession.getServiceManagement();
            ProcessAgentInfo processAgentInfo = serviceManagement.getProcessAgentsDB().RetrieveById(experimentInfo.getEssId());
            TicketIssuer ticketIssuer = serviceManagement.getTicketIssuer();
            Coupon essOutCoupon = ticketIssuer.RetrieveCoupon(processAgentInfo.getOutCoupon().getCouponId());
            Coupon issuedCoupon = ticketIssuer.RetrieveCoupon(experimentInfo.getCouponId());

            /*
             * Create an instance of the ExperimentStorageAPI
             */
            ExperimentStorageAPI experimentStorageAPI = new ExperimentStorageAPI(processAgentInfo.getServiceUrl());
            experimentStorageAPI.setAgentAuthHeaderAgentGuid(serviceManagement.getServiceGuid());
            experimentStorageAPI.setAgentAuthHeaderCoupon(essOutCoupon);
            experimentStorageAPI.setOperationAuthHeaderCoupon(issuedCoupon);

            /*
             * Retrieve the records from the ExperimentStorage
             */
            this.experimentRecords = experimentStorageAPI.GetRecords(experimentId, null);

            /*
             * Update controls
             */
            this.hpgRecordsRendered = true;

        } catch (Exception ex) {
            this.ShowMessageError(ex.getMessage());
        }

        /* Navigate to the current page */
        return null;
    }

    /**
     *
     * @return
     */
    public String actionNew() {
        final String methodName = "actionNew";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        /*
         * Clear information
         */
        this.hsomExperimentId = null;
        this.hitExperimentId = null;
        this.hitLabClient = null;
        this.hitLabServer = null;
        this.hitUser = null;
        this.hitGroup = null;
        this.hitStatus = null;
        this.hitSubmissionTime = null;
        this.hitCompletionTime = null;
        this.hitTotalRecords = null;

        /*
         * Update controls
         */
        this.hcbRecordsDisabled = true;
        this.hpgRecordsRendered = false;

        this.ShowMessageInfo(null);

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);

        /* Navigate to the current page */
        return null;
    }

    /**
     *
     * @return
     */
    private ArrayList<ListItem> CreateExperimentList() {

        ArrayList<ListItem> itemsList = null;

        try {
            /*
             * Get all experiments for this user
             */
            UserSession userSession = this.serviceBrokerSession.getUserSession();
            ArrayList<ExperimentInfo> experimentInfoList = userSession.getExperimentsDB().RetrieveByUserId(userSession.getUserId());
            if (experimentInfoList != null) {
                itemsList = new ArrayList<>();

                /*
                 * Get experiment information for each experiment
                 */
                DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);
                for (ExperimentInfo experimentInfo : experimentInfoList) {
                    /*
                     * Get the LabClient infoormation for this experiment
                     */
                    LabClientInfo labClientInfo = userSession.getLabClientsDB().RetrieveById(experimentInfo.getClientId());

                    /*
                     * Add experiment information to the list
                     */
                    String dateString = (experimentInfo.getDateCreated() != null)
                            ? dateFormat.format(experimentInfo.getDateCreated().getTime()) : "";
                    String label = String.format(STR_ExperimentOneLineSummary_arg3, experimentInfo.getExperimentId(), labClientInfo.getName(), dateString);
                    itemsList.add(new ListItem(label, Long.toString(experimentInfo.getExperimentId())));
                }
            }

        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
        }

        return itemsList;
    }

    /**
     *
     * @param experimentId
     */
    private void PopulateExperimentInfo(long experimentId) {
        final String methodName = "PopulateExperimentInfo";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        try {
            UserSession userSession = this.serviceBrokerSession.getUserSession();
            ExperimentInfo experimentInfo = userSession.getExperimentsDB().Retrieve(experimentId);

            /*
             * Update information
             */
            this.hitExperimentId = Long.toString(experimentInfo.getExperimentId());
            LabClientInfo labClientInfo = userSession.getLabClientsDB().RetrieveById(experimentInfo.getClientId());
            this.hitLabClient = labClientInfo.getName();
            ProcessAgentInfo processAgentInfo = this.serviceBrokerSession.getServiceManagement().getProcessAgentsDB().RetrieveById(experimentInfo.getAgentId());
            this.hitLabServer = processAgentInfo.getAgentName();
            UserInfo userInfo = userSession.getUsersDB().RetrieveByUserId(experimentInfo.getUserId());
            this.hitUser = userInfo.getUsername();
            GroupInfo groupInfo = userSession.getGroupsDB().RetrieveById(experimentInfo.getGroupId());
            this.hitGroup = groupInfo.getName();
            if (processAgentInfo.getAgentType() == ProcessAgentTypes.BLS) {
                this.hitStatus = experimentInfo.getBatchStatusCode().toString();
            } else {
                this.hitStatus = experimentInfo.getStatusCode().toString();
            }
            this.hitSubmissionTime = (experimentInfo.getDateCreated() != null) ? experimentInfo.getDateCreated().getTime().toString() : null;
            this.hitCompletionTime = (experimentInfo.getDateClosed() != null) ? experimentInfo.getDateClosed().getTime().toString() : null;
            this.hitTotalRecords = Integer.toString(experimentInfo.getRecordCount());

            /*
             * Update controls
             */
            this.hcbRecordsDisabled = false;
            this.hpgRecordsRendered = false;

            this.ShowMessageInfo(null);

        } catch (Exception ex) {
            ShowMessageError(ex.getMessage());
            Logfile.WriteError(ex.toString());
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);
    }

    /**
     *
     * @param message
     */
    private void ShowMessageInfo(String message) {
        this.holMessage = message;
        this.holMessageClass = Consts.STRSTL_InfoMessage;
    }

    /**
     *
     * @param message
     */
    private void ShowMessageError(String message) {
        this.holMessage = message;
        this.holMessageClass = Consts.STRSTL_ErrorMessage;
    }
}
