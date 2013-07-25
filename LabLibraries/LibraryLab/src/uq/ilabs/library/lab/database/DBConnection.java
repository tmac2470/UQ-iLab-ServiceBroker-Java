/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uq.ilabs.library.lab.database;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import uq.ilabs.library.lab.utilities.Logfile;

/**
 *
 * @author uqlpayne
 */
public class DBConnection {

    //<editor-fold defaultstate="collapsed" desc="Constants">
    public static final String STR_ClassName = DBConnection.class.getName();
    private static final Level logLevel = Level.FINEST;
    /*
     * String constants
     */
    public static final String STR_User = "user";
    public static final String STR_Password = "password";
    public static final String STR_DefaultUser = "LabServer";
    public static final String STR_DefaultPassword = "ilab";
    /*
     * Constants
     */
    private static final int INT_MaxConnections = 1;
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Variables">
    private Semaphore connections;
    private ArrayList<Connection> connectionList;
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Properties">
    private String driverName;
    private String url;
    private Properties properties;

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUser() {
        return this.properties.getProperty(STR_User);
    }

    public void setUser(String user) {
        this.properties.setProperty(STR_User, user);
    }

    public String getPassword() {
        return this.properties.getProperty(STR_Password);
    }

    public void setPassword(String password) {
        this.properties.setProperty(STR_Password, password);
    }
    //</editor-fold>

    /**
     *
     * @param database
     * @throws Exception
     */
    public DBConnection(String driverName, String url) throws Exception {
        final String methodName = "DBConnection";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        /*
         * Initial local variables
         */
        this.driverName = driverName;
        this.url = url;
        this.properties = new Properties();
        this.properties.setProperty(STR_User, STR_DefaultUser);
        this.properties.setProperty(STR_Password, STR_DefaultPassword);

        /*
         * Load the database driver to ensure that it exists
         */
        try {
            Class.forName(this.driverName);
        } catch (ClassNotFoundException ex) {
            Logfile.WriteError(ex.toString());
            throw ex;
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);
    }

    /**
     *
     * @return
     */
    public Connection getConnection() {
        final String methodName = "getConnection";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        Connection connection = null;

        try {
            /*
             * Check if connection pool has been created
             */
            if (this.connectionList == null) {
                /*
                 * Create connection pool
                 */
                this.connectionList = new ArrayList<>(INT_MaxConnections);
                for (int i = 0; i < INT_MaxConnections; i++) {
                    this.connectionList.add(DriverManager.getConnection(this.url, this.properties));
                }
                this.connections = new Semaphore(INT_MaxConnections, true);
            }

            /*
             * Get a connection from the pool
             */
            this.connections.acquire();
            connection = this.connectionList.remove(0);
        } catch (SQLException | InterruptedException ex) {
            Logfile.WriteError(ex.toString());
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);

        return connection;
    }

    /**
     *
     * @param connection
     */
    public void putConnection(Connection connection) {
        final String methodName = "putConnection";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        /*
         * Return the connection to the pool
         */
        this.connectionList.add(connection);
        this.connections.release();

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);
    }

    /**
     * Deregister the loaded driver
     */
    public void DeRegister() {
        try {
            Driver driver = DriverManager.getDriver(this.url);
            if (driver != null) {
                DriverManager.deregisterDriver(driver);
            }
        } catch (SQLException ex) {
            Logfile.WriteError(ex.toString());
        }
    }
}
