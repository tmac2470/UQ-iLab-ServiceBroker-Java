/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uq.ilabs.servicebroker.client.admin;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;
import java.util.logging.Level;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.ViewExpiredException;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import uq.ilabs.library.datatypes.processagent.ProcessAgentTypes;
import uq.ilabs.library.lab.utilities.Logfile;
import uq.ilabs.library.processagent.database.ProcessAgentsDB;
import uq.ilabs.library.processagent.database.types.ProcessAgentInfo;
import uq.ilabs.library.servicebroker.client.Consts;
import uq.ilabs.library.servicebroker.client.ServiceBrokerSession;
import uq.ilabs.library.servicebroker.client.UserSession;
import uq.ilabs.library.servicebroker.database.types.GroupInfo;
import uq.ilabs.library.servicebroker.database.types.LabClientGroupInfo;
import uq.ilabs.library.servicebroker.database.types.LabClientInfo;
import uq.ilabs.library.servicebroker.engine.LabConsts;
import uq.ilabs.library.servicebroker.engine.types.GroupTypes;
import uq.ilabs.library.servicebroker.engine.types.LabClientTypes;

/**
 *
 * @author uqlpayne
 */
@Named(value = "manageLabClientsBean")
@SessionScoped
public class ManageLabClientsBean implements Serializable {

    //<editor-fold defaultstate="collapsed" desc="Constants">
    private static final String STR_ClassName = ManageLabClientsBean.class.getName();
    private static final Level logLevel = Level.CONFIG;
    /*
     * String constants
     */
    private static final String STR_SaveSuccessful_arg = "LabClient '%s' saved successfully.";
    private static final String STR_UpdateSuccessful_arg = "LabClient '%s' updated successfully.";
    private static final String STR_DeleteSuccessful_arg = "LabClient '%s' deleted successfully.";
    /*
     * String constants for exception messages
     */
    private static final String STRERR_ClientName = "Client Name";
    private static final String STRERR_ClientGuid = "Client Guid";
    private static final String STRERR_Title = "Title";
    private static final String STRERR_Version = "Version";
    private static final String STRERR_Description = "Description";
    private static final String STRERR_LoaderScript = "Loader Script";
    private static final String STRERR_NotSpecified_arg = "%s: Not specified!";
    private static final String STRERR_AlreadyExists_arg = "LabClient '%s' already exists!";
    private static final String STRERR_RetrieveFailed_arg = "LabClient '%s' could not be retrieved.";
    private static final String STRERR_SaveFailed_arg = "LabClient '%s' could not be saved.";
    private static final String STRERR_UpdateFailed_arg = "LabClient '%s' could not be updated.";
    private static final String STRERR_DeleteFailed_arg = "LabClient '%s' could not be deleted.";
    private static final String STRERR_AddGroupFailed_arg = "Group '%s' could not be added.";
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Variables">
    private ServiceBrokerSession serviceBrokerSession;
    private String oldClientType;
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Properties">
    private String hsomLabClient;
    private String hitClientName;
    private String hitClientGuid;
    private String hitVersion;
    private String hitTitle;
    private String htaDescription;
    private String hsomClientType;
    private String htaLoaderScript;
    private String hitLabServer;
    private String hitEssProcessAgent;
    private String hitUssProcessAgent;
    private String hitContactName;
    private String hitContactEmail;
    private String hitDocumentationUrl;
    private String htaNotes;
    private String hsomLsProcessAgent;
    private String hsomEssProcessAgent;
    private String hsomUssProcessAgent;
    private String hsomAssociatedGroup;
    private String hsomAvailableGroup;
    private boolean hcbNeedsESS;
    private boolean hcbNeedsUSS;
    private boolean hcbReentrant;
    private String[] labClients;
    private String[] clientTypes;
    private String[] blsProcessAgents;
    private String[] ilsProcessAgents;
    private String[] essProcessAgents;
    private String[] ussProcessAgents;
    private ArrayList<String> associatedGroupList;
    private ArrayList<String> availableGroupList;
    private boolean registered;
    private boolean interactive;
    private boolean lsRegistered;
    private boolean essRegistered;
    private boolean ussRegistered;
    private boolean deleteDisabled;
    private String holMessage;
    private String holMessageClass;

    public String getHsomLabClient() {
        return hsomLabClient;
    }

    public void setHsomLabClient(String hsomLabClient) {
        this.hsomLabClient = hsomLabClient;
    }

    public String getHitClientName() {
        return hitClientName;
    }

    public void setHitClientName(String hitClientName) {
        this.hitClientName = hitClientName;
    }

    public String getHitClientGuid() {
        return hitClientGuid;
    }

    public void setHitClientGuid(String hitClientGuid) {
        this.hitClientGuid = hitClientGuid;
    }

    public String getHitVersion() {
        return hitVersion;
    }

    public void setHitVersion(String hitVersion) {
        this.hitVersion = hitVersion;
    }

    public String getHitTitle() {
        return hitTitle;
    }

    public void setHitTitle(String hitTitle) {
        this.hitTitle = hitTitle;
    }

    public String getHtaDescription() {
        return htaDescription;
    }

    public void setHtaDescription(String htaDescription) {
        this.htaDescription = htaDescription;
    }

    public String getHsomClientType() {
        return hsomClientType;
    }

    public void setHsomClientType(String hsomClientType) {
        this.hsomClientType = hsomClientType;
    }

    public String getHtaLoaderScript() {
        return htaLoaderScript;
    }

    public void setHtaLoaderScript(String htaLoaderScript) {
        this.htaLoaderScript = htaLoaderScript;
    }

    public String getHitLabServer() {
        return hitLabServer;
    }

    public void setHitLabServer(String hitLabServer) {
        this.hitLabServer = hitLabServer;
    }

    public String getHitEssProcessAgent() {
        return hitEssProcessAgent;
    }

    public void setHitEssProcessAgent(String hitEssProcessAgent) {
        this.hitEssProcessAgent = hitEssProcessAgent;
    }

    public String getHitUssProcessAgent() {
        return hitUssProcessAgent;
    }

    public void setHitUssProcessAgent(String hitUssProcessAgent) {
        this.hitUssProcessAgent = hitUssProcessAgent;
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

    public String getHitDocumentationUrl() {
        return hitDocumentationUrl;
    }

    public void setHitDocumentationUrl(String hitDocumentationUrl) {
        this.hitDocumentationUrl = hitDocumentationUrl;
    }

    public String getHtaNotes() {
        return htaNotes;
    }

    public void setHtaNotes(String htaNotes) {
        this.htaNotes = htaNotes;
    }

    public String getHsomLsProcessAgent() {
        return hsomLsProcessAgent;
    }

    public void setHsomLsProcessAgent(String hsomLsProcessAgent) {
        this.hsomLsProcessAgent = hsomLsProcessAgent;
    }

    public String getHsomEssProcessAgent() {
        return hsomEssProcessAgent;
    }

    public void setHsomEssProcessAgent(String hsomEssProcessAgent) {
        this.hsomEssProcessAgent = hsomEssProcessAgent;
    }

    public String getHsomUssProcessAgent() {
        return hsomUssProcessAgent;
    }

    public void setHsomUssProcessAgent(String hsomUssProcessAgent) {
        this.hsomUssProcessAgent = hsomUssProcessAgent;
    }

    public String getHsomAssociatedGroup() {
        return hsomAssociatedGroup;
    }

    public void setHsomAssociatedGroup(String hsomAssociatedGroup) {
        this.hsomAssociatedGroup = hsomAssociatedGroup;
    }

    public String getHsomAvailableGroup() {
        return hsomAvailableGroup;
    }

    public void setHsomAvailableGroup(String hsomAvailableGroup) {
        this.hsomAvailableGroup = hsomAvailableGroup;
    }

    public boolean isHcbNeedsESS() {
        return hcbNeedsESS;
    }

    public void setHcbNeedsESS(boolean hcbNeedsESS) {
        this.hcbNeedsESS = hcbNeedsESS;
    }

    public boolean isHcbNeedsUSS() {
        return hcbNeedsUSS;
    }

    public void setHcbNeedsUSS(boolean hcbNeedsUSS) {
        this.hcbNeedsUSS = hcbNeedsUSS;
    }

    public boolean isHcbReentrant() {
        return hcbReentrant;
    }

    public void setHcbReentrant(boolean hcbReentrant) {
        this.hcbReentrant = hcbReentrant;
    }

    public String[] getLabClients() {
        return labClients;
    }

    public String[] getClientTypes() {
        return clientTypes;
    }

    public String[] getBlsProcessAgents() {
        return blsProcessAgents;
    }

    public String[] getIlsProcessAgents() {
        return ilsProcessAgents;
    }

    public String[] getEssProcessAgents() {
        return essProcessAgents;
    }

    public String[] getUssProcessAgents() {
        return ussProcessAgents;
    }

    public ArrayList<String> getAssociatedGroupList() {
        return associatedGroupList;
    }

    public ArrayList<String> getAvailableGroupList() {
        return availableGroupList;
    }

    public boolean isRegistered() {
        return registered;
    }

    public boolean isInteractive() {
        return interactive;
    }

    public boolean isLsRegistered() {
        return lsRegistered;
    }

    public void setLsRegistered(boolean lsRegistered) {
        this.lsRegistered = lsRegistered;
    }

    public boolean isEssRegistered() {
        return essRegistered;
    }

    public boolean isUssRegistered() {
        return ussRegistered;
    }

    public boolean isDeleteDisabled() {
        return deleteDisabled;
    }

    public String getHolMessage() {
        return holMessage;
    }

    public String getHolMessageClass() {
        return holMessageClass;
    }
    //</editor-fold>

    /**
     * Creates a new instance of ManageLabClientsBean
     */
    public ManageLabClientsBean() {
        this.serviceBrokerSession = (ServiceBrokerSession) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(Consts.STRSSN_ServiceBroker);
    }

    /**
     *
     */
    public void pageLoad() {
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
            this.labClients = this.CreateLabClientList();
            this.blsProcessAgents = this.CreateProcessAgentList(ProcessAgentTypes.BLS);
            this.ilsProcessAgents = this.CreateProcessAgentList(ProcessAgentTypes.ILS);
            this.essProcessAgents = this.CreateProcessAgentList(ProcessAgentTypes.ESS);
            this.ussProcessAgents = this.CreateProcessAgentList(ProcessAgentTypes.USS);
            this.clientTypes = LabClientTypes.STRINGS;

            /*
             * Clear the page
             */
            this.actionNew();
        }

        /*
         * Check if the client type has changed
         */
        if (this.hsomClientType.equals(this.oldClientType) == false) {
            this.interactive = LabClientTypes.ToType(this.hsomClientType).isInteractive();
            this.oldClientType = this.hsomClientType;
        }
    }

    /**
     *
     * @return
     */
    public String actionSelect() {

        if (this.hsomLabClient != null && this.hsomLabClient.equals(this.labClients[0]) == false) {
            this.PopulateLabClientInfo();
            this.hsomLabClient = this.labClients[0];
        }

        /*
         * Navigate to the current page
         */
        return null;
    }

    /**
     *
     * @return
     */
    public String actionCreateGuid() {

        this.hitClientGuid = UUID.randomUUID().toString();

        /* Navigate to the current page */
        return null;
    }

    public String actionSelectLabServer() {

        if (this.hsomLsProcessAgent != null && this.hsomLsProcessAgent.equals(this.blsProcessAgents[0]) == false) {
            this.hitLabServer = this.hsomLsProcessAgent;
            this.lsRegistered = true;
        }

        /* Navigate to the current page */
        return null;
    }

    public String actionRemoveLabServer() {

        this.hsomLsProcessAgent = this.blsProcessAgents[0];
        this.lsRegistered = false;

        /* Navigate to the current page */
        return null;
    }

    public String actionSelectEss() {

        if (this.hsomEssProcessAgent != null && this.hsomEssProcessAgent.equals(this.essProcessAgents[0]) == false) {
            this.hitEssProcessAgent = this.hsomEssProcessAgent;
            this.essRegistered = true;
        }

        /* Navigate to the current page */
        return null;
    }

    public String actionRemoveEss() {

        this.hsomEssProcessAgent = this.essProcessAgents[0];
        this.essRegistered = false;

        /* Navigate to the current page */
        return null;
    }

    public String actionSelectUss() {

        if (this.hsomUssProcessAgent != null && this.hsomUssProcessAgent.equals(this.ussProcessAgents[0]) == false) {
            this.hitUssProcessAgent = this.hsomUssProcessAgent;
            this.ussRegistered = true;
        }

        /* Navigate to the current page */
        return null;
    }

    public String actionRemoveUss() {

        this.hsomUssProcessAgent = this.ussProcessAgents[0];
        this.ussRegistered = false;

        /* Navigate to the current page */
        return null;
    }

    /**
     *
     * @return
     */
    public String actionAddGroup() {
        /*
         * Check if a valid selection has been made
         */
        if (this.hsomAvailableGroup != null && this.hsomAvailableGroup.equals(this.availableGroupList.get(0)) == false) {
            try {
                /*
                 * Get the LabClient Id and Group Id
                 */
                UserSession userSession = serviceBrokerSession.getUserSession();
                LabClientInfo labClientInfo = userSession.getLabClientsDB().RetrieveByName(this.hitClientName);
                if (labClientInfo != null) {
                    GroupInfo groupInfo = userSession.getGroupsDB().RetrieveByName(this.hsomAvailableGroup);
                    if (groupInfo != null) {
                        /*
                         * Add the Group to the LabClient
                         */
                        LabClientGroupInfo labClientGroupInfo = new LabClientGroupInfo(labClientInfo.getId(), groupInfo.getId());
                        if (userSession.getLabClientGroupsDB().Add(labClientGroupInfo) < 0) {
                            throw new RuntimeException(String.format(STRERR_AddGroupFailed_arg, groupInfo.getName()));
                        }

                        /*
                         * Repopulate group lists
                         */
                        this.CreateGroupLists();
                    }
                }
            } catch (Exception ex) {
                Logfile.WriteError(ex.toString());
            }
        }

        /* Navigate to the current page */
        return null;
    }

    /**
     *
     * @return
     */
    public String actionRemoveGroup() {
        try {
            /*
             * Get the LabClient Id and Group Id
             */
            UserSession userSession = serviceBrokerSession.getUserSession();
            LabClientInfo labClientInfo = userSession.getLabClientsDB().RetrieveByName(this.hitClientName);
            if (labClientInfo != null) {
                GroupInfo groupInfo = userSession.getGroupsDB().RetrieveByName(this.hsomAssociatedGroup);
                if (groupInfo != null) {
                    /*
                     * Remove the Group from the LabClient
                     */
                    LabClientGroupInfo labClientGroupInfo = userSession.getLabClientGroupsDB().RetrieveByLabClientIdGroupId(labClientInfo.getId(), groupInfo.getId());
                    if (labClientGroupInfo != null) {
                        if (userSession.getLabClientGroupsDB().Delete(labClientGroupInfo.getId()) == false) {
                            throw new RuntimeException(String.format(STRERR_AddGroupFailed_arg, groupInfo.getName()));
                        }
                    }

                    /*
                     * Repopulate group lists
                     */
                    this.CreateGroupLists();
                }
            }
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
        }

        /* Navigate to the current page */
        return null;
    }

    /**
     *
     * @return String
     */
    public String actionSave() {
        final String methodName = "actionSave";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        /*
         * Parse the web page information
         */
        LabClientInfo labClientInfo = this.Parse(null);
        if (labClientInfo != null) {
            try {
                /*
                 * Check if LabClient Name already exists
                 */
                UserSession userSession = serviceBrokerSession.getUserSession();
                if (userSession.getLabClientsDB().RetrieveByName(labClientInfo.getName()) != null) {
                    throw new RuntimeException(String.format(STRERR_AlreadyExists_arg, labClientInfo.getName()));
                }

                /*
                 * Save the information
                 */
                if (userSession.getLabClientsDB().Add(labClientInfo) < 0) {
                    throw new RuntimeException(String.format(STRERR_SaveFailed_arg, labClientInfo.getName()));
                }

                /*
                 * Refresh the LabClient name list
                 */
                this.labClients = this.CreateLabClientList();
                this.hsomLabClient = labClientInfo.getName();

                /*
                 * Information saved successfully
                 */
                this.registered = true;
                ShowMessageInfo(String.format(STR_SaveSuccessful_arg, labClientInfo.getName()));

            } catch (Exception ex) {
                ShowMessageError(ex.getMessage());
                Logfile.WriteError(ex.toString());
            }
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);

        /* Navigate to the current page */
        return null;
    }

    /**
     *
     * @return String
     */
    public String actionUpdate() {
        final String methodName = "actionUpdate";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        try {
            /*
             * Get the LabClientInfo for the selected LabClient
             */
            UserSession userSession = serviceBrokerSession.getUserSession();
            LabClientInfo labClientInfo = userSession.getLabClientsDB().RetrieveByName(this.hitClientName);
            if (labClientInfo == null) {
                throw new Exception(String.format(STRERR_RetrieveFailed_arg, this.hitClientName));
            }

            /*
             * Parse the web page information
             */
            labClientInfo = this.Parse(labClientInfo);
            if (labClientInfo != null) {
                /*
                 * Update the information
                 */
                if (userSession.getLabClientsDB().Update(labClientInfo) == false) {
                    throw new Exception(String.format(STRERR_UpdateFailed_arg, labClientInfo.getName()));
                }

                /*
                 * Information updated successfully
                 */
                this.ShowMessageInfo(String.format(STR_UpdateSuccessful_arg, labClientInfo.getName()));
            }
        } catch (Exception ex) {
            this.ShowMessageError(ex.getMessage());
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);

        /* Navigate to the current page */
        return null;
    }

    /**
     *
     * @return String
     */
    public String actionDelete() {
        final String methodName = "actionDelete";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        try {
            /*
             * Get the LabClientInfo for the selected LabClient
             */
            UserSession userSession = serviceBrokerSession.getUserSession();
            LabClientInfo labClientInfo = userSession.getLabClientsDB().RetrieveByName(this.hitClientName);
            if (labClientInfo == null) {
                throw new Exception(String.format(STRERR_RetrieveFailed_arg, this.hitClientName));
            }

            /*
             * Delete the LabClient
             */
            if (userSession.getLabClientsDB().Delete(labClientInfo.getId()) == false) {
                throw new Exception(String.format(STRERR_DeleteFailed_arg, labClientInfo.getName()));
            }

            /*
             * Refresh the LabClient list and clear the page
             */
            this.labClients = this.CreateLabClientList();
            this.actionNew();

            /*
             * Information deleted successfully
             */
            ShowMessageInfo(String.format(STR_DeleteSuccessful_arg, labClientInfo.getName()));

        } catch (Exception ex) {
            ShowMessageError(ex.getMessage());
            Logfile.WriteError(ex.toString());
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);

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
        this.hsomLabClient = this.labClients[0];
        this.hitClientName = null;
        this.hitClientGuid = null;
        this.hitVersion = null;
        this.hitTitle = null;
        this.htaDescription = null;
        this.hsomClientType = this.clientTypes[0];
        this.htaLoaderScript = null;
        this.hitContactName = null;
        this.hitContactEmail = null;
        this.hitDocumentationUrl = null;
        this.htaNotes = null;
        this.hcbNeedsESS = false;
        this.hcbNeedsUSS = false;
        this.hcbReentrant = false;

        /*
         * Update controls
         */
        this.registered = false;
        this.deleteDisabled = true;

        this.ShowMessageInfo(null);

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);

        /* Navigate to the current page */
        return null;
    }

    /**
     *
     * @return String[]
     */
    private String[] CreateLabClientList() {

        String[] labClientList = null;

        try {
            /*
             * Get the list of LabClient names
             */
            UserSession userSession = serviceBrokerSession.getUserSession();
            String[] stringArray = userSession.getLabClientsDB().GetListOfNames();
            if (stringArray != null) {
                labClientList = new String[stringArray.length + 1];
                System.arraycopy(stringArray, 0, labClientList, 1, stringArray.length);
                labClientList[0] = LabConsts.STR_MakeSelection;
            } else {
                labClientList = new String[]{""};
            }
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
        }

        return labClientList;
    }

    /**
     *
     * @param processAgentType
     * @return String[]
     */
    private String[] CreateProcessAgentList(ProcessAgentTypes processAgentType) {

        String[] processAgentList = null;

        try {
            /*
             * Get the list of ProcessAgent names
             */
            ProcessAgentsDB processAgentsDB = this.serviceBrokerSession.getServiceManagement().getProcessAgentsDB();
            String[] stringArray = processAgentsDB.GetListOfNamesByType(processAgentType);
            if (stringArray != null) {
                processAgentList = new String[stringArray.length + 1];
                System.arraycopy(stringArray, 0, processAgentList, 1, stringArray.length);
                processAgentList[0] = LabConsts.STR_MakeSelection;
            } else {
                processAgentList = new String[]{""};
            }
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
        }

        return processAgentList;
    }

    private void CreateGroupLists() {

        try {
            /*
             * Create associated group list
             */
            UserSession userSession = serviceBrokerSession.getUserSession();
            LabClientInfo labClientInfo = userSession.getLabClientsDB().RetrieveByName(this.hitClientName);
            ArrayList<LabClientGroupInfo> labClientGroupInfoList = userSession.getLabClientGroupsDB().RetrieveByLabClientId(labClientInfo.getId());
            if (labClientGroupInfoList != null) {
                this.associatedGroupList = new ArrayList<>();
                for (LabClientGroupInfo labClientGroupInfo : labClientGroupInfoList) {
                    GroupInfo groupInfo = userSession.getGroupsDB().RetrieveById(labClientGroupInfo.getGroupId());
                    this.associatedGroupList.add(groupInfo.getName());
                }
            } else {
                this.associatedGroupList = null;
            }

            /*
             * Create available group list
             */
            String[] namesArray = userSession.getGroupsDB().GetListOfNamesByType(GroupTypes.Regular);
            if (namesArray != null && namesArray.length > 0) {
                this.availableGroupList = new ArrayList<>();
                this.availableGroupList.add(LabConsts.STR_MakeSelection);

                /*
                 * Check if in associated group list
                 */
                if (this.associatedGroupList == null) {
                    this.availableGroupList.addAll(Arrays.asList(namesArray));
                } else {
                    for (String name : namesArray) {
                        if (this.associatedGroupList.contains(name) == false) {
                            this.availableGroupList.add(name);
                        }
                    }
                }
            }

            /*
             * Check if any available groups
             */
            if (this.availableGroupList != null && this.availableGroupList.size() == 1) {
                this.availableGroupList = null;
                this.hsomAvailableGroup = null;
            }
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
        }
    }

    /**
     *
     */
    private void PopulateLabClientInfo() {
        final String methodName = "PopulateLabClientInfo";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        try {
            UserSession userSession = serviceBrokerSession.getUserSession();
            LabClientInfo labClientInfo = userSession.getLabClientsDB().RetrieveByName(this.hsomLabClient);
            if (labClientInfo == null) {
                throw new Exception(String.format(STRERR_RetrieveFailed_arg, this.hsomLabClient));
            }

            /*
             * Update information
             */
            this.hitClientName = labClientInfo.getName();
            this.hitClientGuid = labClientInfo.getGuid();
            this.hitVersion = labClientInfo.getVersion();
            this.hitTitle = labClientInfo.getTitle();
            this.htaDescription = labClientInfo.getDescription();
            this.hsomClientType = labClientInfo.getType().toString();
            this.htaLoaderScript = labClientInfo.getLoaderScript();
            this.hitContactName = labClientInfo.getContactName();
            this.hitContactEmail = labClientInfo.getContactEmail();
            this.hitDocumentationUrl = labClientInfo.getDocumentationUrl();
            this.htaNotes = labClientInfo.getNotes();

            this.lsRegistered = false;
            ProcessAgentsDB processAgentsDB = this.serviceBrokerSession.getServiceManagement().getProcessAgentsDB();
            if (labClientInfo.getAgentId() > 0) {
                ProcessAgentInfo processAgentInfo = processAgentsDB.RetrieveById(labClientInfo.getAgentId());
                if (processAgentInfo != null) {
                    this.hitLabServer = processAgentInfo.getAgentName();
                    this.lsRegistered = true;
                }
            }
            this.essRegistered = false;
            if (labClientInfo.getEssId() > 0) {
                ProcessAgentInfo processAgentInfo = processAgentsDB.RetrieveById(labClientInfo.getEssId());
                if (processAgentInfo != null) {
                    this.hitEssProcessAgent = processAgentInfo.getAgentName();
                    this.essRegistered = true;
                }
            }
            this.ussRegistered = false;
            if (labClientInfo.getUssId() > 0) {
                ProcessAgentInfo processAgentInfo = processAgentsDB.RetrieveById(labClientInfo.getEssId());
                if (processAgentInfo != null) {
                    this.hitUssProcessAgent = processAgentInfo.getAgentName();
                    this.ussRegistered = true;
                }
            }
            this.hcbReentrant = labClientInfo.isReentrant();

            /*
             * Update controls
             */
            this.registered = true;
            this.deleteDisabled = false;
            this.CreateGroupLists();

            this.ShowMessageInfo(null);
        } catch (Exception ex) {
            ShowMessageError(ex.getMessage());
            Logfile.WriteError(ex.toString());
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);
    }

    /**
     *
     * @param labClientInfo
     * @return LabClientInfo
     */
    private LabClientInfo Parse(LabClientInfo labClientInfo) {
        final String methodName = "Parse";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        try {
            /*
             * Check if LabClientInfo has been provided
             */
            if (labClientInfo == null) {
                /*
                 * Create instance of LabClientInfo ready to fill in
                 */
                labClientInfo = new LabClientInfo();

                /*
                 * Check that LabClient Name has been entered
                 */
                this.hitClientName = this.hitClientName.trim();
                if (this.hitClientName.isEmpty() == true) {
                    throw new Exception(String.format(STRERR_NotSpecified_arg, STRERR_ClientName));
                }
                labClientInfo.setName(this.hitClientName);

                /*
                 * Check that Guid has been entered for an interactive LabClient
                 */
                this.hitClientGuid = this.hitClientGuid.trim();
                if (this.hitClientGuid.isEmpty() == true) {
                    if (LabClientTypes.ToType(this.hsomClientType).isInteractive() == true) {
                        throw new Exception(String.format(STRERR_NotSpecified_arg, STRERR_ClientGuid));
                    }
                }
                labClientInfo.setGuid(this.hitClientGuid);
            }

            /*
             * Check that Title has been entered
             */
            this.hitTitle = this.hitTitle.trim();
            if (this.hitTitle.isEmpty() == true) {
                throw new Exception(String.format(STRERR_NotSpecified_arg, STRERR_Title));
            }
            labClientInfo.setTitle(this.hitTitle);

            /*
             * Check that Version has been entered
             */
            this.hitVersion = this.hitVersion.trim();
            if (this.hitVersion.isEmpty() == true) {
                throw new Exception(String.format(STRERR_NotSpecified_arg, STRERR_Version));
            }
            labClientInfo.setVersion(this.hitVersion);

            /*
             * Check that Description has been entered
             */
            this.htaDescription = this.htaDescription.trim();
            if (this.htaDescription.isEmpty() == true) {
                throw new Exception(String.format(STRERR_NotSpecified_arg, STRERR_Description));
            }
            labClientInfo.setDescription(this.htaDescription);

            /*
             * Get client type
             */
            labClientInfo.setType(LabClientTypes.ToType(this.hsomClientType));

            /*
             * Check that Loader Script has been entered
             */
            this.htaLoaderScript = this.htaLoaderScript.trim();
            if (this.htaLoaderScript.isEmpty() == true) {
                throw new Exception(String.format(STRERR_NotSpecified_arg, STRERR_LoaderScript));
            }
            labClientInfo.setLoaderScript(this.htaLoaderScript);

            /*
             * Associated Services
             */
            ProcessAgentsDB processAgentsDB = this.serviceBrokerSession.getServiceManagement().getProcessAgentsDB();
            ProcessAgentInfo processAgentInfo = processAgentsDB.RetrieveByName(this.hitLabServer);
            if (processAgentInfo != null) {
                labClientInfo.setAgentId(processAgentInfo.getAgentId());
            }
            processAgentInfo = processAgentsDB.RetrieveByName(this.hitEssProcessAgent);
            if (processAgentInfo != null) {
                labClientInfo.setEssId(processAgentInfo.getAgentId());
            }
            processAgentInfo = processAgentsDB.RetrieveByName(this.hitUssProcessAgent);
            if (processAgentInfo != null) {
                labClientInfo.setUssId(processAgentInfo.getAgentId());
            }
            labClientInfo.setReentrant(this.hcbReentrant);

            /*
             * Optional Information
             */
            this.hitContactName = this.hitContactName.trim();
            labClientInfo.setContactName(this.hitContactName.isEmpty() ? null : this.hitContactName);
            this.hitContactEmail = this.hitContactEmail.trim();
            labClientInfo.setContactEmail(this.hitContactEmail.isEmpty() ? null : this.hitContactEmail);
            this.hitDocumentationUrl = this.hitDocumentationUrl.trim();
            labClientInfo.setDocumentationUrl(this.hitDocumentationUrl.isEmpty() ? null : this.hitDocumentationUrl);
            this.htaNotes = this.htaNotes.trim();
            labClientInfo.setNotes(this.htaNotes.isEmpty() ? null : this.htaNotes);

        } catch (Exception ex) {
            labClientInfo = null;
            this.ShowMessageError(ex.getMessage());
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);

        return labClientInfo;
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
