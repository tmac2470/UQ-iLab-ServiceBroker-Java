/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uq.ilabs.library.experimentstorage.engine;

import java.util.logging.Level;
import uq.ilabs.library.experimentstorage.database.ExperimentRecordsDB;
import uq.ilabs.library.experimentstorage.database.ExperimentsDB;
import uq.ilabs.library.lab.database.DBConnection;
import uq.ilabs.library.lab.utilities.Logfile;
import uq.ilabs.library.processagent.ProcessAgents;
import uq.ilabs.library.processagent.Ticketing;
import uq.ilabs.library.processagent.database.ProcessAgentsDB;

/**
 *
 * @author uqlpayne
 */
public class ServiceManagement {

    //<editor-fold defaultstate="collapsed" desc="Constants">
    private static final String STR_ClassName = ServiceManagement.class.getName();
    private static final Level logLevel = Level.CONFIG;
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Properties">
    private ConfigProperties configProperties;
    private ExperimentsDB experimentsDB;
    private ExperimentRecordsDB experimentRecordsDB;
    private ProcessAgentsDB processAgentsDB;
    private ProcessAgents processAgents;
    private Ticketing ticketing;
    private String serviceGuid;

    public DBConnection getDbConnection() {
        return (configProperties != null) ? configProperties.getDbConnection() : null;
    }

    public String getVersion() {
        return (configProperties != null) ? configProperties.getVersion() : null;
    }

    public String getNavMenuPhotoUrl() {
        return (configProperties != null) ? configProperties.getNavMenuPhotoUrl() : null;
    }

    public ExperimentsDB getExperimentsDB() {
        return experimentsDB;
    }

    public ExperimentRecordsDB getExperimentRecordsDB() {
        return experimentRecordsDB;
    }

    public ProcessAgentsDB getProcessAgentsDB() {
        return processAgentsDB;
    }

    public ProcessAgents getProcessAgents() {
        return processAgents;
    }

    public Ticketing getTicketing() {
        return ticketing;
    }

    public String getServiceGuid() {
        return serviceGuid;
    }

    public void setServiceGuid(String serviceGuid) {
        this.serviceGuid = serviceGuid;
    }
    //</editor-fold>

    /**
     *
     * @param configProperties
     * @throws Exception
     */
    public ServiceManagement(ConfigProperties configProperties) throws Exception {
        final String methodName = "ServiceManagement";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        try {
            /*
             * Check that parameters are valid
             */
            if (configProperties == null) {
                throw new NullPointerException(ConfigProperties.class.getSimpleName());
            }

            /*
             * Save to local variables
             */
            this.configProperties = configProperties;

            /*
             * Create database API class instances
             */
            DBConnection dbConnection = configProperties.getDbConnection();
            this.experimentsDB = new ExperimentsDB(dbConnection);
            if (this.experimentsDB == null) {
                throw new NullPointerException(ExperimentsDB.class.getSimpleName());
            }
            this.experimentRecordsDB = new ExperimentRecordsDB(dbConnection);
            if (this.experimentRecordsDB == null) {
                throw new NullPointerException(ExperimentRecordsDB.class.getSimpleName());
            }
            this.processAgentsDB = new ProcessAgentsDB(dbConnection);
            if (this.processAgentsDB == null) {
                throw new NullPointerException(ProcessAgentsDB.class.getSimpleName());
            }
            this.processAgents = new ProcessAgents(dbConnection);
            if (this.processAgents == null) {
                throw new NullPointerException(ProcessAgents.class.getSimpleName());
            }
            this.ticketing = new Ticketing(dbConnection);
            if (this.ticketing == null) {
                throw new NullPointerException(Ticketing.class.getSimpleName());
            }
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
            throw ex;
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);
    }
}
