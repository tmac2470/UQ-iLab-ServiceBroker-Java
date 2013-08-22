/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uq.ilabs.servicebroker.client;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.logging.Level;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.ViewExpiredException;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import uq.ilabs.library.datatypes.ticketing.Coupon;
import uq.ilabs.library.datatypes.ticketing.Ticket;
import uq.ilabs.library.datatypes.ticketing.TicketTypes;
import uq.ilabs.library.lab.utilities.Logfile;
import uq.ilabs.library.processagent.ticketing.RedeemSessionPayload;
import uq.ilabs.library.servicebroker.client.Consts;
import uq.ilabs.library.servicebroker.client.ServiceBrokerSession;
import uq.ilabs.library.servicebroker.client.UserSession;
import uq.ilabs.library.servicebroker.database.types.LabClientInfo;
import uq.ilabs.library.servicebroker.ticketissuer.TicketIssuer;

/**
 *
 * @author uqlpayne
 */
@Named(value = "myLabsBean")
@SessionScoped
public class MyLabsBean implements Serializable {

    //<editor-fold defaultstate="collapsed" desc="Constants">
    private static final String STR_ClassName = MyLabsBean.class.getName();
    private static final Level logLevel = Level.CONFIG;
    /*
     * String constants
     */
    private static final String STR_MakeSelection = "-- Make Selection --";
    private static final String STR_LaunchScript_arg = "<script type=\"text/javascript\">window.open('%s', 'LabClient');</script>";
    private static final String STR_RequestParams_arg2 = "couponId=%d&passkey=%s";
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Variables">
    private ServiceBrokerSession serviceBrokerSession;
    private LabClientInfo labClientInfo;
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Properties">
    private ArrayList<LabClientInfo> labClientInfoList;
    private String holTitle;
    private String holVersion;
    private String holDescription;
    private String holLaunchScript;
    private String holMessage;
    private String holMessageClass;

    public ArrayList<LabClientInfo> getLabClientInfoList() {
        return labClientInfoList;
    }

    public String getHolTitle() {
        return holTitle;
    }

    public String getHolVersion() {
        return holVersion;
    }

    public String getHolDescription() {
        return holDescription;
    }

    public String getHolLaunchScript() {
        return holLaunchScript;
    }

    public String getHolMessage() {
        return holMessage;
    }

    public String getHolMessageClass() {
        return holMessageClass;
    }
    //</editor-fold>

    /**
     * Creates a new instance of MyLabsBean
     */
    public MyLabsBean() {
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
            this.PopulateLabs();
            this.holTitle = null;
            this.holLaunchScript = null;
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);
    }

    /**
     *
     * @return String
     */
    public String actionSelectLabClient(LabClientInfo labClientInfo) {

        try {
            this.labClientInfo = labClientInfo;
            this.holTitle = labClientInfo.getTitle();
            this.holVersion = labClientInfo.getVersion();
            this.holDescription = labClientInfo.getDescription();
            this.holLaunchScript = null;

            this.ShowMessageInfo(null);
        } catch (Exception ex) {
            this.ShowMessageError(ex.getMessage());
            Logfile.WriteError(ex.toString());
        }

        /* Navigate to the current page */
        return null;
    }

    /**
     *
     * @return String
     */
    public String actionLaunchLabClient() {
        final String methodName = "actionLaunch";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        try {
            /*
             * Create a coupon and a ticket to redeem the session
             */
            TicketIssuer ticketIssuer = new TicketIssuer(this.serviceBrokerSession.getDbConnection());
            Coupon coupon = ticketIssuer.CreateCoupon(this.serviceBrokerSession.getServiceGuid());

            /*
             * Create a ticket to redeem the session that lasts for one day
             */
            UserSession userSession = this.serviceBrokerSession.getUserSession();
            RedeemSessionPayload redeemSessionPayload = new RedeemSessionPayload(
                    userSession.getUserId(), userSession.getGroupId(), this.labClientInfo.getId(), userSession.getUsername(), userSession.getGroupname());
            String payload = redeemSessionPayload.ToXmlString();
            String redeemerGuid = this.serviceBrokerSession.getServiceGuid();
            String sponsorGuid = this.serviceBrokerSession.getServiceGuid();
            long duration = 60 * 60 * 24;
            Ticket ticket = ticketIssuer.AddTicket(coupon, TicketTypes.RedeemSession, redeemerGuid, sponsorGuid, duration, payload);
            if (ticket != null) {
                switch (this.labClientInfo.getType()) {

                    case BatchRedirect:
                        /*
                         * Create launch script
                         */
                        String requestParams = String.format(STR_RequestParams_arg2, coupon.getCouponId(), coupon.getPasskey());
                        String launchScript = this.labClientInfo.getLoaderScript();
                        launchScript += (launchScript.contains("?") ? "&" : "?") + requestParams;
                        this.holLaunchScript = String.format(STR_LaunchScript_arg, launchScript);
                        break;

                    case BatchApplet:
                        break;

                    case InteractiveRedirect:
                        break;

                    case InteractiveApplet:
                        break;
                }
            }
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);

        /* Navigate to the current page */
        return null;
    }

    /**
     *
     */
    private void PopulateLabs() {
        final String methodName = "PopulateLabs";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        try {
            UserSession userSession = this.serviceBrokerSession.getUserSession();
            this.labClientInfoList = new ArrayList<>();

            /*
             * Get all LabClients that are members of this group
             */
            int[] labClientIds = userSession.getLabClientGroupsDB().GetListOfLabClientIds(userSession.getGroupId());
            if (labClientIds != null) {
                /*
                 * Get LabClient names and add to list of labs
                 */
                for (int i = 0; i < labClientIds.length; i++) {
                    LabClientInfo _labClientInfo = userSession.getLabClientsDB().RetrieveById(labClientIds[i]);
                    if (_labClientInfo != null) {
                        this.labClientInfoList.add(_labClientInfo);
                    }
                }
            }

            this.ShowMessageInfo(null);
        } catch (Exception ex) {
            this.ShowMessageError(ex.getMessage());
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
