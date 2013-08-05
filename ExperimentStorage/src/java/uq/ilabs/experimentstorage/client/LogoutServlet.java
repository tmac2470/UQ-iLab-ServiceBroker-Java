/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uq.ilabs.experimentstorage.client;

import java.io.IOException;
import java.util.logging.Level;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import uq.ilabs.library.lab.utilities.Logfile;
import uq.ilabs.library.experimentstorage.client.Consts;
import uq.ilabs.library.experimentstorage.client.ExperimentStorageSession;
import uq.ilabs.library.experimentstorage.client.UserSession;

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
        ExperimentStorageSession experimentStorageSession = (ExperimentStorageSession) httpSession.getAttribute(Consts.STRSSN_ExperimentStorage);

        /*
         * Remove the user's session
         */
        if (experimentStorageSession != null) {
            UserSession userSession = experimentStorageSession.getUserSession();
            if (userSession != null) {
                Logfile.Write(Level.INFO, String.format(STRLOG_LogoutUserGroup_arg2, userSession.getUsername(), userSession.getGroupname()));

                experimentStorageSession.setUserSession(null);
            }
        }

        /*
         * Go to the LabServer's home page
         */
        response.sendRedirect(getServletContext().getContextPath() + Consts.STRURL_Faces + Consts.STRURL_Home);

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);
    }
}
