/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uq.ilabs.usersidescheduling;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.servlet.ServletContext;
import uq.ilabs.library.lab.utilities.Logfile;
import uq.ilabs.library.processagent.database.types.ProcessAgentInfo;
import uq.ilabs.library.usersidescheduling.engine.ConfigProperties;
import uq.ilabs.library.usersidescheduling.engine.LabConsts;
import uq.ilabs.library.usersidescheduling.engine.ServiceManagement;

/**
 *
 * @author uqlpayne
 */
@Singleton
public class UsersideSchedulingAppBean {

    //<editor-fold defaultstate="collapsed" desc="Constants">
    private static final String STR_ClassName = UsersideSchedulingAppBean.class.getName();
    private static final Level logLevel = Level.INFO;
    /*
     * String constants for logfile messages
     */
    private static final String STRLOG_LoggingLevel_arg = "LoggingLevel: %s";
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Properties">
    private boolean initialised = false;
    private boolean serviceStarted = false;
    private ServiceManagement serviceManagement;
    private UsersideSchedulingHandler usersideSchedulingHandler;
    private ProcessAgentHandler processAgentHandler;

    public boolean isInitialised() {
        return initialised;
    }

    public boolean isServiceStarted() {
        return serviceStarted;
    }

    public ServiceManagement getServiceManagement() {
        return serviceManagement;
    }

    public UsersideSchedulingHandler getUsersideSchedulingHandler() {
        return usersideSchedulingHandler;
    }

    public ProcessAgentHandler getProcessAgentHandler() {
        return processAgentHandler;
    }
    //</editor-fold>

    /**
     * Creates a new instance of UsersideSchedulingAppBean
     */
    public UsersideSchedulingAppBean() {
    }

    /**
     * Check if the service has been initialised and if not, initialise the service using the servlet context to
     * retrieve configuration information from the web.xml file.
     *
     * @param servletContext
     */
    public synchronized void Initialise(ServletContext servletContext) {
        final String methodName = "Initialise";

        try {
            /*
             * Check if initialisation has been done
             */
            if (this.initialised == false) {
                /*
                 * Get the path for the logfiles and logging level
                 */
                String logFilesPath = servletContext.getInitParameter(LabConsts.STRPRM_LogFilesPath);
                logFilesPath = servletContext.getRealPath(logFilesPath);
                String strLogLevel = servletContext.getInitParameter(LabConsts.STRPRM_LogLevel);

                /*
                 * Create an instance of the logger and set the logging level
                 */
                Logger logger = Logfile.CreateLogger(logFilesPath);
                Level level = Level.INFO;
                try {
                    level = Level.parse(strLogLevel);
                } catch (Exception ex) {
                }
                logger.setLevel(level);

                Logfile.WriteCalled(logLevel, STR_ClassName, methodName,
                        String.format(STRLOG_LoggingLevel_arg, logger.getLevel().toString()));

                /*
                 * Get configuration properties from the file
                 */
                String xmlConfigPropertiesPath = servletContext.getInitParameter(LabConsts.STRPRM_XmlConfigPropertiesPath);
                ConfigProperties configProperties = new ConfigProperties(servletContext.getRealPath(xmlConfigPropertiesPath));

                /*
                 * Create local variable instances
                 */
                this.serviceManagement = new ServiceManagement(configProperties);
                this.usersideSchedulingHandler = new UsersideSchedulingHandler(serviceManagement);
                this.processAgentHandler = new ProcessAgentHandler(serviceManagement);

                /*
                 * Initialisation complete
                 */
                this.initialised = true;
            }
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);
    }

    /**
     * Check if the service has been started. If not, initialise the service using the servlet context to retrieve
     * configuration information from the web.xml file and then start the service running.
     *
     * @param servletContext
     */
    public synchronized void StartService(ServletContext servletContext) throws Exception {
        final String methodName = "StartService";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        try {
            /*
             * Check if service has been started
             */
            if (this.serviceStarted == false) {

                /*
                 * Initialise if not done already
                 */
                this.Initialise(servletContext);

                /*
                 * Set the service guid
                 */
                ProcessAgentInfo processAgentInfo = this.serviceManagement.getProcessAgentsDB().RetrieveSelf();
                this.serviceManagement.setServiceGuid(processAgentInfo.getAgentGuid());

                /*
                 * Create and start threads here
                 */
                // Nothing to do here yet

                /*
                 * Service started
                 */
                this.serviceStarted = true;
            }
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
            throw ex;
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);
    }

    /**
     *
     */
    @PreDestroy
    private void preDestroy() {
        final String methodName = "preDestroy";

        /*
         * Prevent from being called more than once
         */
        if (this.initialised == true) {
            Logfile.WriteCalled(Level.INFO, STR_ClassName, methodName);

            /*
             * Deregister the database driver
             */
            this.serviceManagement.getDbConnection().DeRegister();

            /*
             * Close the logfile
             */
            Logfile.CloseLogger();

            this.initialised = false;
        }

        Logfile.WriteCompleted(Level.INFO, STR_ClassName, methodName);
    }
}
