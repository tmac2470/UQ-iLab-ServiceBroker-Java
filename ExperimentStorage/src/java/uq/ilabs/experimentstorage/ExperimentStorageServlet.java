/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uq.ilabs.experimentstorage;

import java.io.IOException;
import java.util.logging.Level;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import uq.ilabs.library.experimentstorage.client.Consts;
import uq.ilabs.library.experimentstorage.client.ExperimentStorageSession;
import uq.ilabs.library.experimentstorage.engine.ServiceManagement;
import uq.ilabs.library.lab.utilities.Logfile;
import uq.ilabs.library.processagent.database.types.ProcessAgentInfo;

/**
 *
 * @author uqlpayne
 */
public class ExperimentStorageServlet extends HttpServlet {

    //<editor-fold defaultstate="collapsed" desc="Constants">
    private static final String STR_ClassName = ExperimentStorageServlet.class.getName();
    private static final Level logLevel = Level.INFO;
    /*
     * String constants
     */
    private static final String STR_UnregisteredService = "Unregistered Service!";
    /*
     * String constants for logfile messages
     */
    private static final String STRLOG_UserHost_arg2 = "UserHost - IP Address: %s  Host Name: %s";
    private static final String STRLOG_TitleVersion_arg2 = "Title: '%s'  Version: '%s'";
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Variables">
    @EJB
    private ExperimentStorageAppBean experimentStorageBean;
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

        try {
            /*
             * Initialise the service if not done already
             */
            this.experimentStorageBean.Initialise(request.getServletContext());

            /*
             * Get the ExperimentStorageSession information from the session
             */
            HttpSession httpSession = request.getSession();
            ExperimentStorageSession experimentStorageSession = (ExperimentStorageSession) httpSession.getAttribute(Consts.STRSSN_ExperimentStorage);

            /*
             * Check if ExperimentStorageSession doesn't exist
             */
            if (experimentStorageSession == null) {
                /*
                 * Log the caller's IP address and hostname
                 */
                Logfile.Write(logLevel, String.format(STRLOG_UserHost_arg2, request.getRemoteAddr(), request.getRemoteHost()));

                /*
                 * Create an instance of the ExperimentStorageSession ready to fill in
                 */
                experimentStorageSession = new ExperimentStorageSession();

                /*
                 * Get ServiceManagement information and save to the session
                 */
                ServiceManagement serviceManagement = this.experimentStorageBean.getServiceManagement();
                experimentStorageSession.setServiceManagement(serviceManagement);

                /*
                 * Get ProcessAgent information and save to the session
                 */
                ProcessAgentInfo processAgentInfo = serviceManagement.getProcessAgentsDB().RetrieveSelf();
                if (processAgentInfo != null) {
                    experimentStorageSession.setTitle(processAgentInfo.getAgentName());
                    experimentStorageSession.setContactEmail(processAgentInfo.getSystemSupportInfo().getContactEmail());
                    experimentStorageSession.setServiceGuid(processAgentInfo.getAgentGuid());
                } else {
                    experimentStorageSession.setTitle(STR_UnregisteredService);
                }

                Logfile.Write(String.format(STRLOG_TitleVersion_arg2, experimentStorageSession.getTitle(), experimentStorageSession.getVersion()));

                /*
                 * Set ExperimentStorageSession information in the session for access by the web pages
                 */
                httpSession.setAttribute(Consts.STRSSN_ExperimentStorage, experimentStorageSession);

                /*
                 * Go to home page
                 */
                response.sendRedirect(request.getContextPath() + Consts.STRURL_Faces + Consts.STRURL_Home);
            }
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
            throw new ServletException(ex.getMessage());
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);
    }
}
