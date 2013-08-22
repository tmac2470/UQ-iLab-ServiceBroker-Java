/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uq.ilabs.servicebroker.client.admin;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.logging.Level;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.ViewExpiredException;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import uq.ilabs.library.lab.utilities.Logfile;
import uq.ilabs.library.servicebroker.client.Consts;
import uq.ilabs.library.servicebroker.client.ServiceBrokerSession;
import uq.ilabs.library.servicebroker.client.UserSession;
import uq.ilabs.library.servicebroker.database.types.GroupInfo;
import uq.ilabs.library.servicebroker.engine.LabConsts;
import uq.ilabs.library.servicebroker.engine.types.GroupTypes;
import uq.ilabs.servicebroker.client.types.ListItem;

/**
 *
 * @author uqlpayne
 */
@Named(value = "manageGroupsBean")
@SessionScoped
public class ManageGroupsBean implements Serializable {

    //<editor-fold defaultstate="collapsed" desc="Constants">
    private static final String STR_ClassName = ManageGroupsBean.class.getName();
    private static final Level logLevel = Level.CONFIG;
    /*
     * String constants
     */
    private static final String STR_Group_arg = "Group %s: ";
    private static final String STR_Succeeded_arg = STR_Group_arg + "Information successfully ";
    private static final String STR_SaveSuccessful_arg = STR_Succeeded_arg + "saved.";
    private static final String STR_UpdateSuccessful_arg = STR_Succeeded_arg + "updated.";
    private static final String STR_DeleteSuccessful_arg = STR_Succeeded_arg + "deleted.";
    /*
     * String constants for exception messages
     */
    private static final String STRERR_AlreadyExists_arg = STR_Group_arg + "Already exists!";
    private static final String STRERR_NotSpecified_arg = "%s: Not specified!";
    private static final String STRERR_GroupName = "Group Name";
    private static final String STRERR_Description = "Description";
    private static final String STRERR_Failed_arg = STR_Group_arg + "Information could not be ";
    private static final String STRERR_RetrieveFailed_arg = STRERR_Failed_arg + "retrieved.";
    private static final String STRERR_SaveFailed_arg = STRERR_Failed_arg + "saved.";
    private static final String STRERR_UpdateFailed_arg = STRERR_Failed_arg + "updated.";
    private static final String STRERR_DeleteFailed_arg = STRERR_Failed_arg + "deleted.";
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Variables">
    private UserSession userSession;
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Properties">
    private String hsomGroupId;
    private String hitGroupName;
    private String hsomGroupType;
    private String htaDescription;
    private String hitContactEmail;
    private ArrayList<ListItem> groupList;
    private String[] groupTypes;
    private boolean registered;
    private String holMessage;
    private String holMessageClass;

    public String getHsomGroupId() {
        return hsomGroupId;
    }

    public void setHsomGroupId(String hsomGroupId) {
        this.hsomGroupId = hsomGroupId;
    }

    public String getHitGroupName() {
        return hitGroupName;
    }

    public void setHitGroupName(String hitGroupName) {
        this.hitGroupName = hitGroupName;
    }

    public String getHsomGroupType() {
        return hsomGroupType;
    }

    public void setHsomGroupType(String hsomGroupType) {
        this.hsomGroupType = hsomGroupType;
    }

    public String getHtaDescription() {
        return htaDescription;
    }

    public void setHtaDescription(String htaDescription) {
        this.htaDescription = htaDescription;
    }

    public String getHitContactEmail() {
        return hitContactEmail;
    }

    public void setHitContactEmail(String hitContactEmail) {
        this.hitContactEmail = hitContactEmail;
    }

    public ArrayList<ListItem> getGroupList() {
        return groupList;
    }

    public String[] getGroupTypes() {
        return groupTypes;
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
     * Creates a new instance of ManageGroupsBean
     */
    public ManageGroupsBean() {
        ServiceBrokerSession serviceBrokerSession = (ServiceBrokerSession) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(Consts.STRSSN_ServiceBroker);
        this.userSession = serviceBrokerSession.getUserSession();
    }

    /**
     *
     */
    public void pageLoad() {
        final String methodName = "pageLoad";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        /*
         * Check if user is logged in and is an administrator
         */
        if (this.userSession == null || this.userSession.isAdmin() == false) {
            throw new ViewExpiredException();
        }

        if (FacesContext.getCurrentInstance().isPostback() == false) {
            /*
             * Not a postback, initialise page controls
             */
            this.groupList = this.CreateGroupList();
            this.groupTypes = GroupTypes.STRINGS;

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

        int groupId = Integer.parseInt(this.hsomGroupId);
        if (groupId != GroupTypes.Unknown.getValue()) {
            this.PopulateGroupInfo(groupId);
            this.hsomGroupId = LabConsts.STR_MakeSelection;
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
        GroupInfo groupInfo = this.Parse(null);
        if (groupInfo != null) {
            try {
                /*
                 * Add information for a new group
                 */
                if (this.userSession.getGroupsDB().Add(groupInfo) < 0) {
                    throw new Exception(String.format(STRERR_SaveFailed_arg, groupInfo.getName()));
                }

                /*
                 * Refresh the group name list
                 */
                this.groupList = this.CreateGroupList();
                this.hsomGroupId = Integer.toString(groupInfo.getId());

                /*
                 * Information saved successfully
                 */
                this.registered = true;
                ShowMessageInfo(String.format(STR_SaveSuccessful_arg, groupInfo.getName()));

            } catch (Exception ex) {
                ShowMessageError(ex.getMessage());
                Logfile.WriteError(ex.toString());
            }
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);

        /*
         * Navigate to the current page
         */
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
             * Get the GroupInfo for the selected group
             */
            GroupInfo groupInfo = this.userSession.getGroupsDB().RetrieveByName(this.hitGroupName);
            if (groupInfo == null) {
                throw new Exception(String.format(STRERR_RetrieveFailed_arg, this.hitGroupName));
            }

            /*
             * Parse the web page information
             */
            groupInfo = this.Parse(groupInfo);
            if (groupInfo != null) {
                /*
                 * Update the information
                 */
                if (this.userSession.getGroupsDB().Update(groupInfo) == false) {
                    throw new Exception(String.format(STRERR_UpdateFailed_arg, groupInfo.getName()));
                }

                /*
                 * Information updated successfully
                 */
                this.ShowMessageInfo(String.format(STR_UpdateSuccessful_arg, groupInfo.getName()));
            }
        } catch (Exception ex) {
            this.ShowMessageError(ex.getMessage());
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);

        /*
         * Navigate to the current page
         */
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
             * Get the GroupInfo for the selected group
             */
            GroupInfo groupInfo = this.userSession.getGroupsDB().RetrieveByName(this.hitGroupName);
            if (groupInfo == null) {
                throw new Exception(String.format(STRERR_RetrieveFailed_arg, this.hitGroupName));
            }

            /*
             * Delete the group
             */
            if (this.userSession.getGroupsDB().Delete(groupInfo.getId()) == false) {
                throw new Exception(String.format(STRERR_DeleteFailed_arg, groupInfo.getName()));
            }

            /*
             * Refresh the group name list and clear the page
             */
            this.groupList = this.CreateGroupList();
            this.actionNew();

            /*
             * Information deleted successfully
             */
            ShowMessageInfo(String.format(STR_DeleteSuccessful_arg, groupInfo.getName()));

        } catch (Exception ex) {
            ShowMessageError(ex.getMessage());
            Logfile.WriteError(ex.toString());
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);

        /*
         * Navigate to the current page
         */
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
        this.hsomGroupId = this.groupList.get(0).getValue();
        this.hitGroupName = null;
        this.hsomGroupType = this.groupTypes[0];
        this.htaDescription = null;

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
     * @return
     */
    private ArrayList<ListItem> CreateGroupList() {

        ArrayList<ListItem> itemsList = new ArrayList<>();
        itemsList.add(new ListItem(LabConsts.STR_MakeSelection, Integer.toString(GroupTypes.Unknown.getValue())));

        try {
            /*
             * Get all groups except BuiltIn type
             */
            ArrayList<GroupInfo> groupInfoList = this.userSession.getGroupsDB().RetrieveAll();
            for (GroupInfo groupInfo : groupInfoList) {
                if (groupInfo.getType() != GroupTypes.BuiltIn) {
                    String name = groupInfo.getName();
                    if (groupInfo.isRequest() == true) {
                        name += GroupTypes.STR_Request;
                    }
                    itemsList.add(new ListItem(name, Integer.toString(groupInfo.getId())));
                }
            }
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
        }

        return itemsList;
    }

    /**
     *
     * @param groupId
     */
    private void PopulateGroupInfo(int groupId) {
        final String methodName = "PopulateGroupInfo";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        try {
            GroupInfo groupInfo = this.userSession.getGroupsDB().RetrieveById(groupId);
            if (groupInfo == null) {
                throw new Exception(String.format(STRERR_RetrieveFailed_arg, this.hsomGroupId));
            }

            /*
             * Update information
             */
            this.hitGroupName = groupInfo.getName();
            this.hsomGroupType = groupInfo.getType().toString();
            this.htaDescription = groupInfo.getDescription();

            /*
             * Update controls
             */
            this.registered = true;

            this.ShowMessageInfo(null);

        } catch (Exception ex) {
            ShowMessageError(ex.getMessage());
            Logfile.WriteError(ex.toString());
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);
    }

    /**
     *
     * @param groupInfo
     * @return GroupInfo
     */
    private GroupInfo Parse(GroupInfo groupInfo) {
        final String methodName = "Parse";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        try {
            /*
             * Check if GroupInfo has been provided
             */
            if (groupInfo == null) {
                /*
                 * Create instance of GroupInfo ready to fill in
                 */
                groupInfo = new GroupInfo();

                /*
                 * Check that GroupName has been entered
                 */
                this.hitGroupName = this.hitGroupName.trim();
                if (this.hitGroupName.isEmpty() == true) {
                    throw new Exception(String.format(STRERR_NotSpecified_arg, STRERR_GroupName));
                }
                groupInfo.setName(this.hitGroupName);

                /*
                 * Check if GroupName already exists
                 */
                if (this.userSession.getGroupsDB().RetrieveByName(this.hitGroupName) != null) {
                    throw new Exception(String.format(STRERR_AlreadyExists_arg, this.hitGroupName));
                }
            }

            /*
             * Check that Description has been entered
             */
            this.htaDescription = this.htaDescription.trim();
            if (this.htaDescription.isEmpty() == true) {
                throw new Exception(String.format(STRERR_NotSpecified_arg, STRERR_Description));
            }
            groupInfo.setDescription(this.htaDescription);

            /*
             * Get information that doesn't require checking
             */
            groupInfo.setType(GroupTypes.ToType(this.hsomGroupType));

        } catch (Exception ex) {
            this.ShowMessageError(ex.getMessage());
            groupInfo = null;
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);

        return groupInfo;
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
