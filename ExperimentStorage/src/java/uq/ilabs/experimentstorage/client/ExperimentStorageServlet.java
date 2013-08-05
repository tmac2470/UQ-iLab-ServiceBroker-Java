/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uq.ilabs.experimentstorage.client;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import uq.ilabs.experimentstorage.service.ExperimentStorageService;
import uq.ilabs.library.experimentstorage.client.Consts;
import uq.ilabs.library.experimentstorage.client.ExperimentStorageSession;
import uq.ilabs.library.experimentstorage.engine.ConfigProperties;
import uq.ilabs.library.lab.database.DBConnection;
import uq.ilabs.library.lab.utilities.Logfile;
import uq.ilabs.library.processagent.database.ProcessAgentsDB;
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
    private static final String STRLOG_LoggingLevel_arg = "LoggingLevel: %s";
    private static final String STRLOG_UserHost_arg2 = "UserHost - IP Address: %s  Host Name: %s";
    private static final String STRLOG_TitleVersion_arg2 = "Title: '%s'  Version: '%s'";
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
         * Get the ExperimentStorageSession information from the session
         */
        HttpSession httpSession = request.getSession();
        ExperimentStorageSession experimentStorageSession = (ExperimentStorageSession) httpSession.getAttribute(Consts.STRSSN_ExperimentStorage);

        /*
         * Check if ExperimentStorageSession doesn't exist
         */
        if (experimentStorageSession == null) {
            try {
                /*
                 * Check if the logger has already been created by the ExperimentStorage service
                 */
                if (ExperimentStorageService.isLoggerCreated() == false) {
                    /*
                     * Get the path for the logfiles and logging level
                     */
                    String logFilesPath = getServletContext().getInitParameter(Consts.STRPRM_LogFilesPath);
                    logFilesPath = getServletContext().getRealPath(logFilesPath);
                    String initLogLevel = getServletContext().getInitParameter(Consts.STRPRM_LogLevel);

                    /*
                     * Create an instance of the logger and set the logging level
                     */
                    Logger logger = Logfile.CreateLogger(logFilesPath);
                    ExperimentStorageService.setLoggerCreated(true);
                    Level level = Level.INFO;
                    try {
                        level = Level.parse(initLogLevel);
                    } catch (Exception ex) {
                    }
                    logger.setLevel(level);

                    Logfile.WriteCalled(logLevel, STR_ClassName, methodName,
                            String.format(STRLOG_LoggingLevel_arg, logger.getLevel().toString()));
                } else {
                    Logfile.WriteCalled(STR_ClassName, methodName);
                }

                /*
                 * Log the caller's IP address and hostname
                 */
                Logfile.Write(logLevel, String.format(STRLOG_UserHost_arg2, request.getRemoteAddr(), request.getRemoteHost()));

                /*
                 * Get configuration properties from the file
                 */
                String xmlConfigPropertiesPath = getServletContext().getInitParameter(Consts.STRPRM_XmlConfigPropertiesPath);
                ConfigProperties configProperties = new ConfigProperties(getServletContext().getRealPath(xmlConfigPropertiesPath));

                /*
                 * Create an instance of the ExperimentStorageSession ready to fill in
                 */
                experimentStorageSession = new ExperimentStorageSession();

                /*
                 * Get information from ConfigProperties and save to the session
                 */
                DBConnection dbConnection = configProperties.getDbConnection();
                experimentStorageSession.setDbConnection(dbConnection);

                /*
                 * Get ProcessAgent information and save to the session
                 */
                ProcessAgentsDB processAgentsDB = new ProcessAgentsDB(dbConnection);
                ProcessAgentInfo processAgentInfo = processAgentsDB.RetrieveSelf();
                if (processAgentInfo != null) {
                    experimentStorageSession.setTitle(processAgentInfo.getAgentName());
                    experimentStorageSession.setGuid(processAgentInfo.getAgentGuid());
                    experimentStorageSession.setContactEmail(processAgentInfo.getSystemSupportInfo().getContactEmail());
                } else {
                    experimentStorageSession.setTitle(STR_UnregisteredService);
                }

                /*
                 * Get information from ConfigProperties and save to the session
                 */
                experimentStorageSession.setVersion(configProperties.getVersion());
                experimentStorageSession.setNavmenuPhotoUrl(configProperties.getNavMenuPhotoUrl());
                Logfile.Write(String.format(STRLOG_TitleVersion_arg2, experimentStorageSession.getTitle(), experimentStorageSession.getVersion()));

                /*
                 * Set ExperimentStorageSession information in the session for access by the web pages
                 */
                httpSession.setAttribute(Consts.STRSSN_ExperimentStorage, experimentStorageSession);

                /*
                 * Go to home page
                 */
                response.sendRedirect(request.getContextPath() + Consts.STRURL_Faces + Consts.STRURL_Home);

            } catch (Exception ex) {
                Logfile.WriteError(ex.toString());
                throw new ServletException(ex.getMessage());
            }
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);
    }
}
