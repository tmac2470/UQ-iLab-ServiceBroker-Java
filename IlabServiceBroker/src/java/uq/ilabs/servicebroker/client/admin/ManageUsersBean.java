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
import uq.ilabs.library.lab.utilities.Password;
import uq.ilabs.library.servicebroker.client.Consts;
import uq.ilabs.library.servicebroker.client.ServiceBrokerSession;
import uq.ilabs.library.servicebroker.client.UserSession;
import uq.ilabs.library.servicebroker.database.GroupsDB;
import uq.ilabs.library.servicebroker.database.UserGroupsDB;
import uq.ilabs.library.servicebroker.database.UsersDB;
import uq.ilabs.library.servicebroker.database.types.GroupInfo;
import uq.ilabs.library.servicebroker.database.types.UserGroupInfo;
import uq.ilabs.library.servicebroker.database.types.UserInfo;
import uq.ilabs.library.servicebroker.engine.LabConsts;
import uq.ilabs.library.servicebroker.engine.types.AffiliationTypes;
import uq.ilabs.library.servicebroker.engine.types.GroupTypes;
import uq.ilabs.servicebroker.client.types.ListItem;

/**
 *
 * @author uqlpayne
 */
@Named(value = "manageUsersBean")
@SessionScoped
public class ManageUsersBean implements Serializable {

    //<editor-fold defaultstate="collapsed" desc="Constants">
    private static final String STR_ClassName = ManageUsersBean.class.getName();
    private static final Level logLevel = Level.CONFIG;
    /*
     * String constants
     */
    private static final String STR_User_arg = "User %s: ";
    private static final String STR_Succeeded_arg = STR_User_arg + "Information successfully ";
    private static final String STR_SaveSuccessful_arg = STR_Succeeded_arg + "saved.";
    private static final String STR_UpdateSuccessful_arg = STR_Succeeded_arg + "updated.";
    private static final String STR_DeleteSuccessful_arg = STR_Succeeded_arg + "deleted.";
    private static final String STR_Group_arg = "Group '%s' ";
    private static final String STR_GroupSuccessful_arg = STR_Group_arg + "successfully ";
    private static final String STR_GroupAddSuccessful_arg = STR_GroupSuccessful_arg + "added.";
    private static final String STR_GroupRemoveSuccessful_arg = STR_GroupSuccessful_arg + "removed.";
    /*
     * String constants for exception messages
     */
    private static final String STRERR_AlreadyExists_arg = STR_User_arg + "Already exists!";
    private static final String STRERR_NotSpecified_arg = "%s: Not specified!";
    private static final String STRERR_Username = "Userame";
    private static final String STRERR_FirstName = "First name";
    private static final String STRERR_LastName = "Last name";
    private static final String STRERR_ContactEmail = "Contact email";
    private static final String STRERR_Password = "Password";
    private static final String STRERR_ConfirmPassword = "Confirm Password";
    private static final String STRERR_PasswordMismatch = "Passwords are different!";
    private static final String STRERR_AffiliationNotSelected = "Affiliation not selected!";
    private static final String STRERR_Failed_arg = STR_User_arg + "Information could not be ";
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
    private String hsomUsername;
    private String hitUsername;
    private String hitFirstName;
    private String hitLastname;
    private String hitContactEmail;
    private String hisPassword;
    private String hisConfirmPassword;
    private String hsomAffiliation;
    private boolean hcbLockAccount;
    private String hsomMembershipGroupId;
    private String hsomAvailableGroupId;
    //
    private ArrayList<ListItem> groupList;
    private String[] usernames;
    private String[] affiliations;
    private ArrayList<ListItem> membershipGroupList;
    private ArrayList<ListItem> availableGroupList;
    private boolean hcbSelectUserDisabled;
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

    public String getHsomUsername() {
        return hsomUsername;
    }

    public void setHsomUsername(String hsomUsername) {
        this.hsomUsername = hsomUsername;
    }

    public String getHitUsername() {
        return hitUsername;
    }

    public void setHitUsername(String hitUsername) {
        this.hitUsername = hitUsername;
    }

    public String getHitFirstName() {
        return hitFirstName;
    }

    public void setHitFirstName(String hitFirstName) {
        this.hitFirstName = hitFirstName;
    }

    public String getHitLastname() {
        return hitLastname;
    }

    public void setHitLastname(String hitLastname) {
        this.hitLastname = hitLastname;
    }

    public String getHitContactEmail() {
        return hitContactEmail;
    }

    public void setHitContactEmail(String hitContactEmail) {
        this.hitContactEmail = hitContactEmail;
    }

    public String getHisPassword() {
        return hisPassword;
    }

    public void setHisPassword(String hisPassword) {
        this.hisPassword = hisPassword;
    }

    public String getHisConfirmPassword() {
        return hisConfirmPassword;
    }

    public void setHisConfirmPassword(String hisConfirmPassword) {
        this.hisConfirmPassword = hisConfirmPassword;
    }

    public String getHsomAffiliation() {
        return hsomAffiliation;
    }

    public void setHsomAffiliation(String hsomAffiliation) {
        this.hsomAffiliation = hsomAffiliation;
    }

    public boolean isHcbLockAccount() {
        return hcbLockAccount;
    }

    public void setHcbLockAccount(boolean hcbLockAccount) {
        this.hcbLockAccount = hcbLockAccount;
    }

    public String getHsomMembershipGroupId() {
        return hsomMembershipGroupId;
    }

    public void setHsomMembershipGroupId(String hsomMembershipGroupId) {
        this.hsomMembershipGroupId = hsomMembershipGroupId;
    }

    public String getHsomAvailableGroupId() {
        return hsomAvailableGroupId;
    }

    public void setHsomAvailableGroupId(String hsomAvailableGroupId) {
        this.hsomAvailableGroupId = hsomAvailableGroupId;
    }

    public ArrayList<ListItem> getGroupList() {
        return groupList;
    }

    public String[] getUsernames() {
        return usernames;
    }

    public String[] getAffiliations() {
        return affiliations;
    }

    public ArrayList<ListItem> getMembershipGroupList() {
        return membershipGroupList;
    }

    public ArrayList<ListItem> getAvailableGroupList() {
        return availableGroupList;
    }

    public boolean isHcbSelectUserDisabled() {
        return hcbSelectUserDisabled;
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
     * Creates a new instance of ManageUsersBean
     */
    public ManageUsersBean() {
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
            this.hsomGroupId = (this.groupList != null && this.groupList.size() > 0) ? this.groupList.get(0).getLabel() : null;
            this.affiliations = this.CreateAffiliationsList();

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
    public String actionSelectGroup() {

        this.ShowMessageInfo(null);

        /*
         * Check if a valid selection has been made
         */
        int groupId = Integer.parseInt(this.hsomGroupId);
        if (groupId != GroupTypes.Unknown.getValue()) {
            this.usernames = this.CreateUserNamesList(groupId);
            this.hsomUsername = (this.usernames != null) ? this.usernames[0] : null;

            /*
             * Update controls
             */
            this.hcbSelectUserDisabled = false;
        }

        /*
         * Clear the page
         */
        this.actionNew();

        /* Navigate to the current page */
        return null;
    }

    /**
     *
     * @return
     */
    public String actionSelectUser() {

        if (this.hsomUsername != null && this.hsomUsername.equals(this.usernames[0]) == false) {
            this.PopulateUserInfo();
        }
        this.ShowMessageInfo(null);

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
        UserInfo userInfo = this.Parse(null);
        if (userInfo != null) {
            try {
                /*
                 * Add information for a new user
                 */
                if (this.userSession.getUsersDB().Add(userInfo) < 0) {
                    throw new Exception(String.format(STRERR_SaveFailed_arg, userInfo.getUsername()));
                }

                /*
                 * Information saved successfully
                 */
                this.registered = true;
                ShowMessageInfo(String.format(STR_SaveSuccessful_arg, userInfo.getUsername()));

            } catch (Exception ex) {
                this.ShowMessageError(ex.getMessage());
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
             * Get the UserInfo for the selected Username
             */
            UserInfo userInfo = this.userSession.getUsersDB().RetrieveByUsername(this.hitUsername);
            if (userInfo == null) {
                throw new Exception(String.format(STRERR_RetrieveFailed_arg, this.hitUsername));
            }

            /*
             * Parse the web page information
             */
            userInfo = this.Parse(userInfo);
            if (userInfo != null) {
                /*
                 * Update the information
                 */
                if (this.userSession.getUsersDB().Update(userInfo) == false) {
                    throw new RuntimeException(String.format(STRERR_UpdateFailed_arg, userInfo.getUsername()));
                }

                /*
                 * Information updated successfully
                 */
                ShowMessageInfo(String.format(STR_UpdateSuccessful_arg, userInfo.getUsername()));
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
     * @return
     */
    public String actionDelete() {
        try {
            /*
             * Get the user Id
             */
            UsersDB usersDB = this.userSession.getUsersDB();
            UserInfo userInfo = usersDB.RetrieveByUsername(this.hitUsername);
            if (userInfo == null) {
                throw new RuntimeException(STRERR_UpdateFailed_arg);
            }

            /*
             * Get all of the groups that the user has membership
             */
            UserGroupsDB userGroupsDB = this.userSession.getUserGroupsDB();
            ArrayList<UserGroupInfo> userGroupInfoList = userGroupsDB.RetrieveByUserId(userInfo.getUserId());

            /*
             * Remove the user from each group
             */
            for (UserGroupInfo userGroupInfo : userGroupInfoList) {
                if (userGroupsDB.Delete(userGroupInfo.getId()) == false) {
                    throw new RuntimeException(String.format(STRERR_DeleteFailed_arg, userInfo.getUsername()));
                }
            }

            /*
             * Delete the user
             */
            if (usersDB.Delete(userInfo.getUserId()) == false) {
                throw new RuntimeException(String.format(STRERR_DeleteFailed_arg, userInfo.getUsername()));
            }

            /*
             * Clear the page
             */
            this.actionNew();

            /*
             * User deleted successfully
             */
            ShowMessageInfo(String.format(STR_DeleteSuccessful_arg, userInfo.getUsername()));

        } catch (Exception ex) {
            this.ShowMessageError(ex.getMessage());
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

        /*
         * Clear information
         */
        this.hitUsername = null;
        this.hitFirstName = null;
        this.hitLastname = null;
        this.hitContactEmail = null;
        this.hisPassword = null;
        this.hisConfirmPassword = null;
        this.hsomAffiliation = (this.affiliations != null && this.affiliations.length > 0) ? this.affiliations[0] : null;
        this.hcbLockAccount = false;

        /*
         * Update controls
         */
        this.hsomUsername = (this.usernames != null) ? this.usernames[0] : null;
        this.registered = false;

        this.ShowMessageInfo(null);

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
        int groupId = Integer.parseInt(this.hsomAvailableGroupId);
        if (groupId != GroupTypes.Unknown.getValue()) {
            try {
                /*
                 * Get the user Id
                 */
                UserInfo userInfo = this.userSession.getUsersDB().RetrieveByUsername(this.hitUsername);
                if (userInfo == null) {
                    throw new RuntimeException(STRERR_UpdateFailed_arg);
                }

                /*
                 * Add user to group
                 */
                UserGroupInfo userGroupInfo = new UserGroupInfo(userInfo.getUserId(), groupId);
                if (this.userSession.getUserGroupsDB().Add(userGroupInfo) < 0) {
                    throw new RuntimeException(STRERR_UpdateFailed_arg);
                }

                /*
                 * Information updated successfully
                 */
                GroupsDB groupsDB = this.userSession.getGroupsDB();
                GroupInfo groupInfo = groupsDB.RetrieveById(groupId);
                if (groupInfo == null) {
                    throw new RuntimeException(STRERR_UpdateFailed_arg);
                }
                ShowMessageInfo(String.format(STR_GroupAddSuccessful_arg, groupInfo.getName()));

                /*
                 * Repopulate membership groups
                 */
                this.CreateUserGroupLists(userInfo.getUserId());

            } catch (Exception ex) {
                this.ShowMessageError(ex.getMessage());
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
        /*
         * Check if a valid selection has been made
         */
        int groupId = Integer.parseInt(this.hsomMembershipGroupId);
        if (groupId != GroupTypes.Unknown.getValue()) {
            try {
                /*
                 * Get the user Id
                 */
                UserInfo userInfo = this.userSession.getUsersDB().RetrieveByUsername(this.hitUsername);
                if (userInfo == null) {
                    throw new RuntimeException(STRERR_UpdateFailed_arg);
                }

                /*
                 * Get all of the groups that the user has membership
                 */
                UserGroupsDB userGroupsDB = this.userSession.getUserGroupsDB();
                ArrayList<UserGroupInfo> userGroupInfoList = userGroupsDB.RetrieveByUserId(userInfo.getUserId());

                /*
                 * Search for the specified group
                 */
                for (UserGroupInfo userGroupInfo : userGroupInfoList) {
                    if (userGroupInfo.getGroupId() == groupId) {
                        /*
                         * Remove the user
                         */
                        if (userGroupsDB.Delete(userGroupInfo.getId()) == false) {
                            throw new RuntimeException(STRERR_UpdateFailed_arg);
                        }

                        /*
                         * If that was the only group then add the user to the orphaned group
                         */
                        if (userGroupInfoList.size() == 1) {
                            GroupInfo groupInfo = this.userSession.getGroupsDB().RetrieveByName(GroupTypes.STR_BuiltInOrphanedUser);
                            userGroupInfo.setGroupId(groupInfo.getId());
                            if (userGroupsDB.Add(userGroupInfo) < 0) {
                                throw new RuntimeException(STRERR_UpdateFailed_arg);
                            }
                        }
                        break;
                    }
                }

                /*
                 * Information updated successfully
                 */
                GroupsDB groupsDB = this.userSession.getGroupsDB();
                GroupInfo groupInfo = groupsDB.RetrieveById(groupId);
                if (groupInfo == null) {
                    throw new RuntimeException(STRERR_UpdateFailed_arg);
                }
                ShowMessageInfo(String.format(STR_GroupRemoveSuccessful_arg, groupInfo.getName()));

                /*
                 * Repopulate membership groups
                 */
                this.CreateUserGroupLists(userInfo.getUserId());

            } catch (Exception ex) {
                this.ShowMessageError(ex.getMessage());
            }
        }

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
             * Get all groups except ROOT
             */
            ArrayList<GroupInfo> groupInfoList = this.userSession.getGroupsDB().RetrieveAll();
            for (GroupInfo groupInfo : groupInfoList) {
                if (groupInfo.getName().equals(GroupTypes.STR_BuiltInRoot) == false) {
                    String name = groupInfo.getName();
                    if (groupInfo.isRequest() == true) {
                        name += GroupTypes.STR_Request;
                    }
                    itemsList.add(new ListItem(name, Integer.toString(groupInfo.getId())));
                }
            }

            /*
             * Update controls
             */
            this.usernames = null;
            this.hcbSelectUserDisabled = true;

        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
        }

        return itemsList;
    }

    /**
     *
     * @return String[]
     */
    private String[] CreateUserNamesList(int groupId) {

        String[] strings = null;

        try {
            /*
             * Get users that are members of this group although there may be none
             */
            ArrayList<UserGroupInfo> userGroupInfoList = this.userSession.getUserGroupsDB().RetrieveByGroupId(groupId);
            if (userGroupInfoList != null) {
                ArrayList<String> stringList = new ArrayList<>();
                stringList.add(LabConsts.STR_MakeSelection);

                for (UserGroupInfo userGroupInfo : userGroupInfoList) {
                    UserInfo userInfo = this.userSession.getUsersDB().RetrieveByUserId(userGroupInfo.getUserId());
                    stringList.add(userInfo.getUsername());
                }

                /*
                 * Convert the list to an array
                 */
                if (stringList.size() > 0) {
                    strings = stringList.toArray(new String[stringList.size()]);
                }
            }
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
        }

        return strings;
    }

    /**
     *
     * @return
     */
    private String[] CreateAffiliationsList() {

        String[] affiliationsList = null;

        try {
            /*
             * Get the list of affiliations
             */
            String[] stringArray = AffiliationTypes.STRINGS;
            if (stringArray != null) {
                affiliationsList = new String[stringArray.length + 1];
                System.arraycopy(stringArray, 0, affiliationsList, 1, stringArray.length);
                affiliationsList[0] = LabConsts.STR_MakeSelection;
            } else {
                affiliationsList = new String[]{""};
            }
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
        }

        return affiliationsList;
    }

    /**
     *
     * @return
     */
    private void CreateUserGroupLists(int userId) {

        try {
            ArrayList<ListItem> itemsList = null;

            /*
             * Create membership group list
             */
            ArrayList<UserGroupInfo> userGroupInfoList = this.userSession.getUserGroupsDB().RetrieveByUserId(userId);
            if (userGroupInfoList != null) {
                itemsList = new ArrayList<>();
//                itemsList.add(new ListItem(LabConsts.STR_MakeSelection, Integer.toString(GroupTypes.Unknown.getValue())));

                for (UserGroupInfo userGroupInfo : userGroupInfoList) {
                    GroupInfo groupInfo = this.userSession.getGroupsDB().RetrieveById(userGroupInfo.getGroupId());
                    String name = groupInfo.getName();
                    if (groupInfo.isRequest() == true) {
                        name += GroupTypes.STR_Request;
                    }
                    itemsList.add(new ListItem(name, Integer.toString(groupInfo.getId())));
                }
            }
            this.hsomMembershipGroupId = (itemsList != null && itemsList.size() > 0) ? itemsList.get(0).getValue() : null;
            this.membershipGroupList = itemsList;

            /*
             * Get all groups except ROOT
             */
            ArrayList<GroupInfo> groupInfoList = this.userSession.getGroupsDB().RetrieveAll();
            if (groupInfoList != null) {
                itemsList = new ArrayList<>();
                itemsList.add(new ListItem(LabConsts.STR_MakeSelection, Integer.toString(GroupTypes.Unknown.getValue())));

                for (GroupInfo groupInfo : groupInfoList) {
                    /*
                     * Don't want builtin root group
                     */
                    if (groupInfo.getName().equals(GroupTypes.STR_BuiltInRoot) == false) {
                        /*
                         * Add group to available group list if not in the membership group list
                         */
                        boolean found = false;
                        for (UserGroupInfo userGroupInfo : userGroupInfoList) {
                            if (groupInfo.getId() == userGroupInfo.getGroupId()) {
                                found = true;
                                break;
                            }
                        }
                        if (found == false) {
                            String name = groupInfo.getName();
                            if (groupInfo.isRequest() == true) {
                                name += GroupTypes.STR_Request;
                            }
                            itemsList.add(new ListItem(name, Integer.toString(groupInfo.getId())));
                        }
                    }
                }
            }
            this.hsomAvailableGroupId = (itemsList != null && itemsList.size() > 0) ? itemsList.get(0).getLabel() : null;
            this.availableGroupList = itemsList;

        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
        }
    }

    /**
     *
     */
    private void PopulateUserInfo() {
        final String methodName = "PopulateUserInfo";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        try {
            UserInfo userInfo = this.userSession.getUsersDB().RetrieveByUsername(this.hsomUsername);
            if (userInfo == null) {
                throw new Exception(String.format(STRERR_RetrieveFailed_arg, this.hsomUsername));
            }

            /*
             * Update information
             */
            this.hitUsername = userInfo.getUsername();
            this.hitFirstName = userInfo.getFirstName();
            this.hitLastname = userInfo.getLastName();
            this.hitContactEmail = userInfo.getContactEmail();
            this.hsomAffiliation = userInfo.getAffiliation();
            this.hcbLockAccount = userInfo.isAccountLocked();

            /*
             * Populate membership groups
             */
            this.CreateUserGroupLists(userInfo.getUserId());

            /*
             * Populate available groups
             */

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
     * @param userInfo
     * @return UserInfo
     */
    private UserInfo Parse(UserInfo userInfo) {
        final String methodName = "Parse";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        try {
            /*
             * Check if UserInfo has been provided
             */
            if (userInfo == null) {
                /*
                 * Create instance of UserInfo ready to fill in
                 */
                userInfo = new UserInfo();

                /*
                 * Check that Username has been entered
                 */
                this.hitUsername = this.hitUsername.toLowerCase().trim();
                if (this.hitUsername.isEmpty() == true) {
                    throw new IllegalArgumentException(String.format(STRERR_NotSpecified_arg, STRERR_Username));
                }
                userInfo.setUsername(this.hitUsername);

                /*
                 * Check if Username already exists
                 */
                if (this.userSession.getUsersDB().RetrieveByUsername(this.hitUsername) != null) {
                    throw new Exception(String.format(STRERR_AlreadyExists_arg, this.hitUsername));
                }
            }

            /*
             * Check that a first name has been entered
             */
            this.hitFirstName = this.hitFirstName.trim();
            if (this.hitFirstName.isEmpty() == true) {
                throw new IllegalArgumentException(String.format(STRERR_NotSpecified_arg, STRERR_FirstName));
            }
            userInfo.setFirstName(this.hitFirstName);

            /*
             * Check that a last name has been entered
             */
            this.hitLastname = this.hitLastname.trim();
            if (this.hitLastname.isEmpty() == true) {
                throw new IllegalArgumentException(String.format(STRERR_NotSpecified_arg, STRERR_LastName));
            }
            userInfo.setLastName(this.hitLastname);

            /*
             * Check that a contact email has been entered
             */
            this.hitContactEmail = this.hitContactEmail.toLowerCase().trim();
            if (this.hitContactEmail.isEmpty() == true) {
                throw new IllegalArgumentException(String.format(STRERR_NotSpecified_arg, STRERR_ContactEmail));
            }
            userInfo.setContactEmail(this.hitContactEmail);

            /*
             * Check if a password has been entered
             */
            this.hisPassword = this.hisPassword.trim();
            if (this.hisPassword.isEmpty() == false) {
                /*
                 * Check that a confirm password has been entered
                 */
                this.hisConfirmPassword = this.hisConfirmPassword.trim();
                if (this.hisConfirmPassword.isEmpty() == true) {
                    throw new IllegalArgumentException(String.format(STRERR_NotSpecified_arg, STRERR_ConfirmPassword));
                }
            }

            /*
             * Check if a confirm password has been entered
             */
            this.hisConfirmPassword = this.hisConfirmPassword.trim();
            if (this.hisConfirmPassword.isEmpty() == false) {
                /*
                 * Check that a password has been entered
                 */
                this.hisPassword = this.hisPassword.trim();
                if (this.hisPassword.isEmpty() == true) {
                    throw new IllegalArgumentException(String.format(STRERR_NotSpecified_arg, STRERR_Password));
                }
            }

            /*
             * If a password has been entered, check that the passwords match
             */
            if (this.hisPassword.isEmpty() == false) {
                if (this.hisPassword.equals(this.hisConfirmPassword) == false) {
                    throw new IllegalArgumentException(STRERR_PasswordMismatch);
                }
                userInfo.setPassword(Password.ToHash(this.hisPassword));
            }

            /*
             * Check that an affiliation has been selected
             */
            if (this.hsomAffiliation.equals(this.affiliations[0]) == true) {
                throw new IllegalArgumentException(STRERR_AffiliationNotSelected);
            }
            userInfo.setAffiliation(this.hsomAffiliation);

            /*
             * Get information that does not need checking
             */
            userInfo.setAccountLocked(this.hcbLockAccount);

        } catch (Exception ex) {
            this.ShowMessageError(ex.getMessage());
            userInfo = null;
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);

        return userInfo;
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
