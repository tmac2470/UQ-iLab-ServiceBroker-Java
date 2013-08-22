/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uq.ilabs.usersidescheduling;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import uq.ilabs.library.lab.utilities.Logfile;
import uq.ilabs.library.processagent.database.types.ProcessAgentInfo;
import uq.ilabs.library.usersidescheduling.client.Consts;
import uq.ilabs.library.usersidescheduling.client.UserSession;
import uq.ilabs.library.usersidescheduling.client.UsersideSchedulingSession;
import uq.ilabs.library.usersidescheduling.engine.ServiceManagement;

/**
 *
 * @author uqlpayne
 */
public class UsersideSchedulingServlet extends HttpServlet {

    //<editor-fold defaultstate="collapsed" desc="Constants">
    private static final String STR_ClassName = UsersideSchedulingServlet.class.getName();
    private static final Level logLevel = Level.INFO;
    /*
     * String constants
     */
    private static final String STR_UsernameAdministration = "Administration";
    private static final String STR_UsernameManagement = "Management";
    private static final String STR_UnregisteredService = "Unregistered Service!";
    /*
     * String constants for logfile messages
     */
    private static final String STRLOG_LoggingLevel_arg = "LoggingLevel: %s";
    private static final String STRLOG_UserHost_arg2 = "UserHost - IP Address: %s  Host Name: %s";
    private static final String STRLOG_TitleVersion_arg2 = "Title: '%s'  Version: '%s'";
    /*
     * String constants for exception messages
     */
    private static final String STRERR_UnknownServlet_arg = "Unknown Servlet: %s";
    private static final String STRERR_NotSpecified_arg = "%s - Not specified!";
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Variables">
    @EJB
    private UsersideSchedulingAppBean usersideSchedulingBean;
    //</editor-fold>

    /**
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        final String methodName = "doGet";

        /*
         * Initialise the service if not done already
         */
        this.usersideSchedulingBean.Initialise(request.getServletContext());

        /*
         * Get the UsersideSchedulingSession information from the session
         */
        HttpSession httpSession = request.getSession();
        UsersideSchedulingSession usersideSchedulingSession = (UsersideSchedulingSession) httpSession.getAttribute(Consts.STRSSN_UsersideScheduling);

        try {
            /*
             * Check if UsersideSchedulingSession doesn't exist
             */
            if (usersideSchedulingSession == null) {
                /*
                 * Log the caller's IP address and hostname
                 */
                Logfile.Write(logLevel, String.format(STRLOG_UserHost_arg2, request.getRemoteAddr(), request.getRemoteHost()));

                /*
                 * Create an instance of UsersideSchedulingSession ready to fill in
                 */
                usersideSchedulingSession = new UsersideSchedulingSession();

                /*
                 * Get ServiceManagement information and save to the session
                 */
                ServiceManagement serviceManagement = this.usersideSchedulingBean.getServiceManagement();
                usersideSchedulingSession.setServiceManagement(serviceManagement);

                /*
                 * Get ProcessAgent information and save to the session
                 */
                ProcessAgentInfo processAgentInfo = serviceManagement.getProcessAgentsDB().RetrieveSelf();
                if (processAgentInfo != null) {
                    usersideSchedulingSession.setTitle(processAgentInfo.getAgentName());
                    usersideSchedulingSession.setContactEmail(processAgentInfo.getSystemSupportInfo().getContactEmail());
                    usersideSchedulingSession.setServiceGuid(processAgentInfo.getAgentGuid());
                } else {
                    usersideSchedulingSession.setTitle(STR_UnregisteredService);
                }

                Logfile.Write(String.format(STRLOG_TitleVersion_arg2, usersideSchedulingSession.getTitle(), usersideSchedulingSession.getVersion()));

                /*
                 * Set UsersideSchedulingSession information in the session for access by the web pages
                 */
                httpSession.setAttribute(Consts.STRSSN_UsersideScheduling, usersideSchedulingSession);
            }

            /*
             * Get request parameters
             */
            int couponId = 0;
            String passkey = null;
            String issuerGuid = null;
            String serviceBrokerUrl = null;
            String servlet = null;

            Map<String, String[]> parameterMap = request.getParameterMap();
            for (String key : parameterMap.keySet()) {
                if (key.equalsIgnoreCase(Consts.STRREQ_CouponId)) {
                    try {
                        couponId = Integer.parseInt(request.getParameter(key));
                    } catch (NumberFormatException ex) {
                    }
                } else if (key.equalsIgnoreCase(Consts.STRREQ_Passkey)) {
                    passkey = request.getParameter(key);
                } else if (key.equalsIgnoreCase(Consts.STRREQ_IssuerGuid)) {
                    issuerGuid = request.getParameter(key);
                } else if (key.equalsIgnoreCase(Consts.STRREQ_SbUrl)) {
                    serviceBrokerUrl = request.getParameter(key);
                } else if (key.equalsIgnoreCase(Consts.STRREQ_Servlet)) {
                    servlet = request.getParameter(key);
                }
            }

            /*
             * Check if caller is a ServiceBroker
             */
            if (servlet != null) {
                /*
                 * Caller is a ServiceBroker, create UserSession ready to fill in
                 */
                UserSession userSession = new UserSession();

                /*
                 * Determine servlet where call originated
                 */
                if (AdministrationServlet.class.getSimpleName().equals(servlet) == true) {
                    userSession.setUsername(STR_UsernameAdministration);
                    userSession.setAdministration(true);
                } else if (ManagementServlet.class.getSimpleName().equals(servlet) == true) {
                    userSession.setUsername(STR_UsernameManagement);
                    userSession.setManagement(true);
                } else {
                    throw new RuntimeException(String.format(STRERR_UnknownServlet_arg, servlet));
                }

                /*
                 * Check that all required request parameters have been specified
                 */
                if (couponId == 0) {
                    throw new IllegalArgumentException(String.format(STRERR_NotSpecified_arg, Consts.STRREQ_CouponId));
                }
                if (passkey == null) {
                    throw new NullPointerException(String.format(STRERR_NotSpecified_arg, Consts.STRREQ_Passkey));
                }
                if (issuerGuid == null) {
                    throw new NullPointerException(String.format(STRERR_NotSpecified_arg, Consts.STRREQ_IssuerGuid));
                }

                /*
                 * Authenticate
                 Coupon coupon = new Coupon(couponId, issuerGuid, passkey);

                 TicketIssuerAPI ticketIssuerAPI = new TicketIssuerAPI(serviceBrokerUrl);
                 ticketIssuerAPI.RedeemTicket(coupon, TicketTypes.AdministerUSS, usersideSchedulingSession.getServiceGuid());
                 ;
                 */

                /*
                 * Add request parameters to UserSession
                 */
                userSession.setCouponId(couponId);
                userSession.setIssuerGuid(issuerGuid);
                userSession.setPasskey(passkey);
                userSession.setServiceBrokerUrl(serviceBrokerUrl);

                /*
                 * Add UserSession information to the session
                 */
                usersideSchedulingSession.setUserSession(userSession);
            }

            /*
             * Go to home page
             */
            response.sendRedirect(request.getContextPath() + Consts.STRURL_Faces + Consts.STRURL_Home);

        } catch (RuntimeException | IOException ex) {
            Logfile.WriteError(ex.toString());
            throw new ServletException(ex.getMessage());
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);
    }
}
