/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uq.ilabs.labsidescheduling;

import java.io.IOException;
import java.util.logging.Level;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import uq.ilabs.library.lab.utilities.Logfile;
import uq.ilabs.library.labsidescheduling.client.Consts;
import uq.ilabs.library.labsidescheduling.client.LabsideSchedulingSession;
import uq.ilabs.library.labsidescheduling.client.UserSession;

/**
 *
 * @author uqlpayne
 */
public class LogoutServlet extends HttpServlet {

    //<editor-fold defaultstate="collapsed" desc="Constants">
    private static final String STR_ClassName = LogoutServlet.class.getName();
    private static final Level logLevel = Level.FINE;
    /*
     * String constants for logfile messages
     */
    private static final String STRLOG_LogoutUserGroup_arg2 = "Logout - User: %s  Group: %s";
    //</editor-fold>

    /**
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        final String methodName = "doGet";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        /*
         * Get the LabServerSession information from the session
         */
        HttpSession httpSession = request.getSession();
        LabsideSchedulingSession labsideSchedulingSession = (LabsideSchedulingSession) httpSession.getAttribute(Consts.STRSSN_LabsideScheduling);

        /*
         * Save the ServiceBroker url
         */
        String serviceBrokerUrl = null;

        /*
         * Check if UserSession
         */
        if (labsideSchedulingSession != null) {
            UserSession userSession = labsideSchedulingSession.getUserSession();
            if (userSession != null) {
                Logfile.Write(Level.INFO, String.format(STRLOG_LogoutUserGroup_arg2, userSession.getUsername(), userSession.getGroupname()));

                /*
                 * Save the ServiceBroker url for redirection and remove UserSession
                 */
                serviceBrokerUrl = userSession.getServiceBrokerUrl();
                labsideSchedulingSession.setUserSession(null);
            }
        }

        /*
         * Determine redirect url
         */
        response.sendRedirect((serviceBrokerUrl == null)
                ? request.getContextPath() + Consts.STRURL_Faces + Consts.STRURL_Home
                : serviceBrokerUrl);

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);
    }
}
