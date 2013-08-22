/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uq.ilabs.experimentstorage.client;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.logging.Level;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.ViewExpiredException;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import uq.ilabs.experimentstorage.client.types.ListItem;
import uq.ilabs.library.experimentstorage.client.Consts;
import uq.ilabs.library.experimentstorage.client.ExperimentStorageSession;
import uq.ilabs.library.experimentstorage.engine.LabConsts;
import uq.ilabs.library.lab.utilities.Logfile;
import uq.ilabs.library.processagent.database.ProcessAgentsDB;
import uq.ilabs.library.processagent.database.types.ProcessAgentInfo;
import uq.ilabs.library.processagent.database.types.SystemSupportInfo;

/**
 *
 * @author uqlpayne
 */
@Named(value = "servicesBean")
@SessionScoped
public class ServicesBean implements Serializable {

    //<editor-fold defaultstate="collapsed" desc="Constants">
    private static final String STR_ClassName = ServicesBean.class.getName();
    private static final Level logLevel = Level.CONFIG;
    /*
     * String constants
     */
    private static final String STR_Service_arg = "Service '%s' ";
    private static final String STR_ServiceNameType_arg2 = "%s:%s";
    /*
     * String constants for exception messages
     */
    private static final String STRERR_RetrieveFailed_arg = STR_Service_arg + "could not be retrieved.";
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Variables">
    private ExperimentStorageSession experimentStorageSession;
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Properties">
    private String hsomService;
    private String hitServiceName;
    private String hitServiceUrl;
    private String hitServiceGuid;
    private String hitClientUrl;
    private String hitServiceType;
    private String htaDescription;
    private String hitLocation;
    private String hitInfoUrl;
    private String hitContactName;
    private String hitContactEmail;
    //
    private ArrayList<ListItem> serviceList;
    private boolean registered;
    private String holMessage;
    private String holMessageClass;

    public String getHsomService() {
        return hsomService;
    }

    public void setHsomService(String hsomService) {
        this.hsomService = hsomService;
    }

    public String getHitServiceName() {
        return hitServiceName;
    }

    public void setHitServiceName(String hitServiceName) {
        this.hitServiceName = hitServiceName;
    }

    public String getHitServiceUrl() {
        return hitServiceUrl;
    }

    public void setHitServiceUrl(String hitServiceUrl) {
        this.hitServiceUrl = hitServiceUrl;
    }

    public String getHitServiceGuid() {
        return hitServiceGuid;
    }

    public void setHitServiceGuid(String hitServiceGuid) {
        this.hitServiceGuid = hitServiceGuid;
    }

    public String getHitClientUrl() {
        return hitClientUrl;
    }

    public void setHitClientUrl(String hitClientUrl) {
        this.hitClientUrl = hitClientUrl;
    }

    public String getHitServiceType() {
        return hitServiceType;
    }

    public void setHitServiceType(String hitServiceType) {
        this.hitServiceType = hitServiceType;
    }

    public String getHtaDescription() {
        return htaDescription;
    }

    public void setHtaDescription(String htaDescription) {
        this.htaDescription = htaDescription;
    }

    public String getHitLocation() {
        return hitLocation;
    }

    public void setHitLocation(String hitLocation) {
        this.hitLocation = hitLocation;
    }

    public String getHitInfoUrl() {
        return hitInfoUrl;
    }

    public void setHitInfoUrl(String hitInfoUrl) {
        this.hitInfoUrl = hitInfoUrl;
    }

    public String getHitContactName() {
        return hitContactName;
    }

    public void setHitContactName(String hitContactName) {
        this.hitContactName = hitContactName;
    }

    public String getHitContactEmail() {
        return hitContactEmail;
    }

    public void setHitContactEmail(String hitContactEmail) {
        this.hitContactEmail = hitContactEmail;
    }

    public ArrayList<ListItem> getServiceList() {
        return serviceList;
    }

    public boolean isRegistered() {
        return registered;
    }

    public String getHolMessage() {
        return holMessage;
    }

    public String getHolMessageClass() {
        return holMessageClass;
    }
    //</editor-fold>

    /**
     * Creates a new instance of ServicesBean
     */
    public ServicesBean() {
        this.experimentStorageSession = (ExperimentStorageSession) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(Consts.STRSSN_ExperimentStorage);
    }

    /**
     *
     */
    public void pageLoad() {
        /*
         * Check if manager is logged in
         */
        if (this.experimentStorageSession.getUserSession() == null) {
            throw new ViewExpiredException();
        }

        if (FacesContext.getCurrentInstance().isPostback() == false) {
            /*
             * Not a postback, initialise page controls
             */
            this.serviceList = this.CreateServiceList();

            /*
             * Clear the page
             */
            this.actionNew();
        }
    }

    /**
     *
     * @return
     */
    public String actionSelect() {

        if (this.hsomService != null && this.serviceList != null && this.serviceList.size() > 0) {
            String value = this.serviceList.get(0).getValue();
            if (this.hsomService.equals(value) == false) {
                this.PopulateServiceInfo();
                this.hsomService = value;
                this.ShowMessageInfo(null);
            }
        }

        /* Navigate to the current page */
        return null;
    }

    /**
     *
     * @return String
     */
    public String actionNew() {
        final String methodName = "actionNew";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        this.hsomService = (this.serviceList != null && this.serviceList.size() > 0)
                ? this.serviceList.get(0).getValue() : null;
        this.hitServiceName = null;
        this.hitServiceGuid = null;
        this.hitServiceUrl = null;
        this.hitClientUrl = null;
        this.hitServiceType = null;
        this.htaDescription = null;
        this.hitLocation = null;
        this.hitInfoUrl = null;
        this.hitContactName = null;
        this.hitContactEmail = null;

        /*
         * Update controls
         */
        this.registered = false;

        this.ShowMessageInfo(null);

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);

        /* Navigate to the current page */
        return null;
    }

    /**
     *
     * @return ArrayList of ListItem
     */
    private ArrayList<ListItem> CreateServiceList() {

        ArrayList<ListItem> itemsList = null;

        try {
            /*
             * Get the list of ProcessAgent names
             */
            ProcessAgentsDB processAgentsDB = this.experimentStorageSession.getServiceManagement().getProcessAgentsDB();
            String[] stringArray = processAgentsDB.GetListAll();
            if (stringArray != null) {
                itemsList = new ArrayList<>();
                itemsList.add(new ListItem(LabConsts.STR_MakeSelection, LabConsts.STR_MakeSelection));
                for (String string : stringArray) {
                    ProcessAgentInfo processAgentInfo = processAgentsDB.RetrieveByName(string);
                    if (processAgentInfo.isSelf() == false) {
                        String label = String.format(STR_ServiceNameType_arg2, processAgentInfo.getAgentType().name(), processAgentInfo.getAgentName());
                        itemsList.add(new ListItem(label, processAgentInfo.getAgentName()));
                    }
                }
            }
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
        }

        return itemsList;
    }

    /**
     *
     */
    private void PopulateServiceInfo() {
        final String methodName = "PopulateServiceInfo";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        try {
            /*
             * Retrieve the ProcessAgentInfo for the specified service
             */
            ProcessAgentsDB processAgentsDB = this.experimentStorageSession.getServiceManagement().getProcessAgentsDB();
            ProcessAgentInfo processAgentInfo = processAgentsDB.RetrieveByName(this.hsomService);
            if (processAgentInfo == null) {
                throw new Exception(String.format(STRERR_RetrieveFailed_arg, this.hsomService));
            }

            /*
             * Update information
             */
            this.hitServiceName = processAgentInfo.getAgentName();
            this.hitServiceGuid = processAgentInfo.getAgentGuid();
            this.hitServiceUrl = processAgentInfo.getServiceUrl();
            this.hitClientUrl = processAgentInfo.getClientUrl();
            this.hitServiceType = processAgentInfo.getAgentType().toString();

            SystemSupportInfo systemSupportInfo = processAgentInfo.getSystemSupportInfo();
            this.htaDescription = systemSupportInfo.getDescription();
            this.hitLocation = systemSupportInfo.getLocation();
            this.hitInfoUrl = systemSupportInfo.getInfoUrl();
            this.hitContactName = systemSupportInfo.getContactName();
            this.hitContactEmail = systemSupportInfo.getContactEmail();

            /*
             * Update controls
             */
            this.registered = true;
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
