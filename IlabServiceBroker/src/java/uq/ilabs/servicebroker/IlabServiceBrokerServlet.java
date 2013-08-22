/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uq.ilabs.servicebroker;

import java.io.IOException;
import java.util.logging.Level;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import uq.ilabs.library.lab.utilities.Logfile;
import uq.ilabs.library.processagent.database.types.ProcessAgentInfo;
import uq.ilabs.library.servicebroker.client.Consts;
import uq.ilabs.library.servicebroker.client.ServiceBrokerSession;
import uq.ilabs.library.servicebroker.engine.ServiceManagement;

/**
 *
 * @author uqlpayne
 */
public class IlabServiceBrokerServlet extends HttpServlet {

    //<editor-fold defaultstate="collapsed" desc="Constants">
    private static final String STR_ClassName = IlabServiceBrokerServlet.class.getName();
    private static final Level logLevel = Level.INFO;
    /*
     * String constants
     */
    private static final String STR_UnregisteredServiceBroker = "Unregistered ServiceBroker!";
    /*
     * String constants for logfile messages
     */
    private static final String STRLOG_UserHost_arg2 = "UserHost - IP Address: %s  Host Name: %s";
    private static final String STRLOG_TitleVersion_arg2 = "Title: '%s'  Version: '%s'";
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Variables">
    @EJB
    private IlabServiceBrokerAppBean ilabServiceBrokerAppBean;
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
            this.ilabServiceBrokerAppBean.Initialise(request.getServletContext());

            /*
             * Get the ServiceBrokerSession information from the session
             */
            HttpSession httpSession = request.getSession();
            ServiceBrokerSession serviceBrokerSession = (ServiceBrokerSession) httpSession.getAttribute(Consts.STRSSN_ServiceBroker);

            /*
             * Check if ServiceBrokerSession doesn't exist
             */
            if (serviceBrokerSession == null) {
                /*
                 * Log the caller's IP address and hostname
                 */
                Logfile.Write(logLevel, String.format(STRLOG_UserHost_arg2, request.getRemoteAddr(), request.getRemoteHost()));

                /*
                 * Create an instance of the ServiceBrokerSession ready to fill in
                 */
                serviceBrokerSession = new ServiceBrokerSession();

                /*
                 * Get ServiceManagement information and save to the session
                 */
                ServiceManagement serviceManagement = this.ilabServiceBrokerAppBean.getServiceManagement();
                serviceBrokerSession.setServiceManagement(serviceManagement);

                /*
                 * Get ProcessAgent information and save to the session
                 */
                ProcessAgentInfo processAgentInfo = serviceManagement.getProcessAgentsDB().RetrieveSelf();
                if (processAgentInfo != null) {
                    serviceBrokerSession.setTitle(processAgentInfo.getAgentName());
                    serviceBrokerSession.setContactEmail(processAgentInfo.getSystemSupportInfo().getContactEmail());
                    serviceBrokerSession.setServiceGuid(processAgentInfo.getAgentGuid());
                } else {
                    serviceBrokerSession.setTitle(STR_UnregisteredServiceBroker);
                }
                Logfile.Write(String.format(STRLOG_TitleVersion_arg2, serviceBrokerSession.getTitle(), serviceBrokerSession.getVersion()));

                /*
                 * Set ServiceBrokerSession information in the session for access by the web pages
                 */
                httpSession.setAttribute(Consts.STRSSN_ServiceBroker, serviceBrokerSession);
            }

            /*
             * Go to home page
             */
            response.sendRedirect(request.getContextPath() + Consts.STRURL_Faces + Consts.STRURL_Home);

        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
            throw new ServletException(ex.getMessage());
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);
    }
}
