/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uq.ilabs.servicebroker.client;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
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
import uq.ilabs.library.servicebroker.database.types.LabClientInfo;
import uq.ilabs.library.servicebroker.database.types.UserGroupInfo;
import uq.ilabs.library.servicebroker.engine.types.GroupTypes;
import uq.ilabs.servicebroker.client.types.Group;

/**
 *
 * @author uqlpayne
 */
@Named(value = "myGroupsBean")
@SessionScoped
public class MyGroupsBean implements Serializable {
    //<editor-fold defaultstate="collapsed" desc="Constants">

    private static final String STR_ClassName = MyGroupsBean.class.getName();
    private static final Level logLevel = Level.FINE;
    /*
     * String constants for exception messages
     */
    private static final String STRERR_NoGroupMembership = "You are not a member of any groups.";
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Variables">
    private ServiceBrokerSession serviceBrokerSession;
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Properties">
    private ArrayList<Group> groups;
    private String holMessage;
    private String holMessageClass;

    public List<Group> getGroups() {
        return groups;
    }

    public String getHolMessage() {
        return holMessage;
    }

    public String getHolMessageClass() {
        return holMessageClass;
    }
    //</editor-fold>

    /**
     * Creates a new instance of MyGroupsBean
     */
    public MyGroupsBean() {
        final String methodName = "MyGroupsBean";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        this.serviceBrokerSession = (ServiceBrokerSession) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(Consts.STRSSN_ServiceBroker);

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);
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
            this.PopulateGroupsLabs();
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);
    }

    /**
     *
     * @param group
     * @return
     */
    public String actionSelectGroup(Group group) {
        final String methodName = "actionSelectGroup";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        try {
            /*
             * Change the group that the user belongs to
             */
            UserSession userSession = this.serviceBrokerSession.getUserSession();
            GroupInfo groupInfo = userSession.getGroupsDB().RetrieveByName(group.getName());
            if (groupInfo != null) {
                userSession.setGroupId(groupInfo.getId());
                userSession.setGroupname(groupInfo.getName());

                /*
                 * Check if this is the SuperUser group
                 */
                String redirectUrl;
                if (groupInfo.getName().equals(GroupTypes.STR_BuiltInSuperUser) == true) {
                    userSession.setAdmin(true);
                    redirectUrl = Consts.STRURL_ManageUsers;
                } else {
                    redirectUrl = (Consts.STRURL_MyLabs);
                }
                FacesContext.getCurrentInstance().getExternalContext().redirect(redirectUrl);
            }

            this.ShowMessageInfo(null);
        } catch (Exception ex) {
            this.ShowMessageError(ex.getMessage());
            Logfile.WriteError(ex.toString());
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);

        /*
         * Navigate to the specified page, if null then stay on same page
         */
        return null;
    }

    /**
     *
     */
    private void PopulateGroupsLabs() {
        final String methodName = "PopulateGroupsLabs";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        try {
            UserSession userSession = this.serviceBrokerSession.getUserSession();
            ArrayList<Group> groupList = null;

            /*
             * Get all groups that the user belongs to
             */
            ArrayList<UserGroupInfo> userGroupInfoList = userSession.getUserGroupsDB().RetrieveByUserId(userSession.getUserId());
            if (userGroupInfoList != null) {
                groupList = new ArrayList<>();

                /*
                 * Get the group information for each group
                 */
                for (UserGroupInfo userGroupInfo : userGroupInfoList) {
                    GroupInfo groupInfo = userSession.getGroupsDB().RetrieveById(userGroupInfo.getGroupId());
                    if (groupInfo != null && groupInfo.getType() == GroupTypes.Regular && groupInfo.isRequest() == false) {
                        /*
                         * Create group information for web page controls
                         */
                        Group group = new Group(groupInfo.getName(), groupInfo.getDescription());

                        /*
                         * Get all LabClients that are members of this group
                         */
                        int[] labClientIds = userSession.getLabClientGroupsDB().GetListOfLabClientIds(groupInfo.getId());
                        if (labClientIds != null) {
                            /*
                             * Get LabClient names and add to list of labs
                             */
                            String[] labs = new String[labClientIds.length];
                            for (int i = 0; i < labs.length; i++) {
                                LabClientInfo labClientInfo = userSession.getLabClientsDB().RetrieveById(labClientIds[i]);
                                if (labClientInfo != null) {
                                    labs[i] = labClientInfo.getName();
                                }
                            }
                            group.setLabs(labs);
                        }

                        /*
                         * Add group information to web page controls
                         */
                        groupList.add(group);
                    }
                }
            }
            this.groups = (groupList != null && groupList.size() > 0) ? groupList : null;
            if (this.groups == null) {
                throw new RuntimeException(STRERR_NoGroupMembership);
            }

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
