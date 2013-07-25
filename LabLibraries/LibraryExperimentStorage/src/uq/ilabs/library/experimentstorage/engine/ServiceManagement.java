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
import uq.ilabs.library.processagent.Ticketing;
import uq.ilabs.library.processagent.database.ProcessAgentsDB;
import uq.ilabs.library.processagent.database.types.ProcessAgentInfo;

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
    private DBConnection dbConnection;
    private ExperimentsDB experimentsDB;
    private ExperimentRecordsDB experimentRecordsDB;
    private ProcessAgentsDB processAgentsDB;
    private Ticketing ticketing;
    private String serviceGuid;

    public DBConnection getDbConnection() {
        return dbConnection;
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

    public Ticketing getTicketing() {
        return ticketing;
    }

    public String getServiceGuid() {
        return this.GetServiceGuid();
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
             * Create database API class instances
             */
            this.dbConnection = configProperties.getDbConnection();
            this.experimentsDB = new ExperimentsDB(this.dbConnection);
            if (this.experimentsDB == null) {
                throw new NullPointerException(ExperimentsDB.class.getSimpleName());
            }
            this.experimentRecordsDB = new ExperimentRecordsDB(this.dbConnection);
            if (this.experimentRecordsDB == null) {
                throw new NullPointerException(ExperimentRecordsDB.class.getSimpleName());
            }
            this.processAgentsDB = new ProcessAgentsDB(this.dbConnection);
            if (this.processAgentsDB == null) {
                throw new NullPointerException(ProcessAgentsDB.class.getSimpleName());
            }
            this.ticketing = new Ticketing(this.dbConnection);
            if (this.ticketing == null) {
                throw new NullPointerException(Ticketing.class.getSimpleName());
            }
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
            throw ex;
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);
    }

    /**
     *
     * @return String
     */
    private String GetServiceGuid() {
        if (this.serviceGuid == null) {
            ProcessAgentInfo processAgentInfo = this.processAgentsDB.RetrieveSelf();
            if (processAgentInfo != null) {
                this.serviceGuid = processAgentInfo.getAgentGuid();
            }
        }

        return this.serviceGuid;
    }
}
