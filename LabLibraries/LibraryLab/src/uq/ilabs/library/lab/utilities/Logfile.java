/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uq.ilabs.library.lab.utilities;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 *
 * @author uqlpayne
 */
public class Logfile implements Runnable {

    //<editor-fold defaultstate="collapsed" desc="Constants">
    private static final String STR_ClassName = Logfile.class.getName();
    /*
     * String constants
     */
    public static final String STRLOG_Newline = System.getProperty("line.separator");
    public static final String STRLOG_Spacer = "  ";
    public static final String STRLOG_Quote = "'";
    //
    private static final String STR_FilenameDateFormat = "YYYMMdd";
    private static final String STR_FilenameExtension = ".log";
    private static final String STR_TimestampFormat = "hh:mm:ss.SSS a";
    private static final String STR_TimestampMessage_arg3 = "%s - %s: %s";
    /*
     * String constants for logfile messages
     */
    private static final String STRLOG_LogfileName_arg = "Logfile Name: %s";
    private static final String STRLOG_LoggingLevel_arg = "Logging Level: %s";
    private static final String STRLOG_Called = "(): Called";
    private static final String STRLOG_CalledMarker = " >> ";
    private static final String STRLOG_Completed = "(): Completed";
    private static final String STRLOG_CompletedMarker = " << ";
    /*
     * String constants for exception messages
     */
    private static final String STRERR_ThreadFailedToStart = "Thread failed to start!";
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Variables">
    private static final Logger logger = Logger.getAnonymousLogger();
    private static Logfile instance;
    private Level level;
    private String logfilePath;
    private LinkedList<String> queue;
    private final Object queueLock;
    private Thread thread;
    private boolean running;
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Properties">

    public static void setLevel(Level level) {
        logger.setLevel(level);
        if (instance != null) {
            instance.level = level;
        }
    }
    //</editor-fold>

    /**
     * Create a logger to write log information to the specified filename. The filename may be specified with a relative
     * or absolute path.
     *
     * @param filename Filename for the log file.
     */
    public static Logger CreateLogger(String filename) {
        try {
            /*
             * Get the absolute path for the specified filename
             */
            File file = new File(filename);
            String absoluteFilename = file.getAbsolutePath();

            /*
             * Send logger output to a file handler which appends to the file
             */
            FileHandler loggerFileHandler = new FileHandler(absoluteFilename, true);
            loggerFileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(loggerFileHandler);
            logger.setLevel(Level.INFO);

            /*
             * Log information
             */
            logger.info(String.format(STRLOG_LogfileName_arg, absoluteFilename));
            logger.info(String.format(STRLOG_LoggingLevel_arg, logger.getLevel().toString()));
        } catch (IOException | SecurityException ex) {
            logger.log(Level.SEVERE, null, ex);
        }

        return logger;
    }

    /**
     *
     */
    public static void CloseLogger() {
        final String methodName = "CloseLogger";
        WriteCalled(STR_ClassName, methodName);

        Handler[] handlers = logger.getHandlers();
        for (int i = 0; i < handlers.length; i++) {
            handlers[i].close();
            logger.removeHandler(handlers[i]);
        }
    }

    /**
     *
     * @param logfilePath
     * @return
     */
    public static Logfile getInstance(String logfilePath) {
        /*
         * Create an instance of this class
         */
        instance = new Logfile(logfilePath);
        instance.Start();

        return instance;
    }

    public static String Write() {
        return Write("");
    }

    public static String Write(String message) {
        return Write(Level.INFO, message);
    }

    public static String Write(Level level, String message) {
        String logMessage = message;
        log(level, "", null, logMessage);

        return logMessage;
    }

    public static String WriteError(String message) {
        log(Level.SEVERE, "", null, message);
        return message;
    }

    public static void WriteException(String sourceClass, String sourceMethod, Throwable thrown) {
        logger.throwing(sourceClass, sourceMethod, thrown);
    }

    public static String WriteCalled(String sourceClass, String sourceMethod) {
        return WriteCalled(Level.INFO, sourceClass, sourceMethod);
    }

    public static String WriteCalled(String sourceClass, String sourceMethod, String message) {
        return WriteCalled(Level.INFO, sourceClass, sourceMethod, message);
    }

    public static String WriteCalled(Level level, String sourceClass, String sourceMethod) {
        return WriteCalled(level, sourceClass, sourceMethod, "");
    }

    public static String WriteCalled(Level level, String sourceClass, String sourceMethod, String message) {
        String logMessage = null;

        /*
         * If the source method exists then add it to the log
         */
        if (sourceMethod != null && sourceMethod.length() > 0) {
            logMessage = sourceMethod + STRLOG_Called;
        }

        /*
         * If the source class exists then prepend it to the log
         */
        if (sourceClass != null && sourceClass.length() > 0 && logMessage != null) {
            logMessage = sourceClass + "." + logMessage;
        }

        /*
         * Check that the class name and/or method name have been specified
         */
        if (logMessage != null) {
            /*
             * If the message exists then add it to the log
             */
            if (message != null && message.length() > 0) {
                logMessage += STRLOG_Newline + STRLOG_CalledMarker + message;
            }
            log(level, "", null, logMessage);
        }

        return logMessage;
    }

    public static String WriteCompleted(String sourceClass, String sourceMethod) {
        return WriteCompleted(Level.INFO, sourceClass, sourceMethod, "");
    }

    public static String WriteCompleted(String sourceClass, String sourceMethod, String message) {
        return WriteCompleted(Level.INFO, sourceClass, sourceMethod, message);
    }

    public static String WriteCompleted(Level level, String sourceClass, String sourceMethod) {
        return WriteCompleted(level, sourceClass, sourceMethod, "");
    }

    public static String WriteCompleted(Level level, String sourceClass, String sourceMethod, String message) {
        String logMessage = null;

        /*
         * If the source method exists then add it to the log
         */
        if (sourceMethod != null && sourceMethod.length() > 0) {
            logMessage = sourceMethod + STRLOG_Completed;
        }

        /*
         * If the source class exists then prepend it to the log
         */
        if (sourceClass != null && sourceClass.length() > 0 && logMessage != null) {
            logMessage = sourceClass + "." + logMessage;
        }

        /*
         * Check that the class name and/or method name have been specified
         */
        if (logMessage != null) {
            /*
             * If the message exists then add it to the log
             */
            if (message != null && message.length() > 0) {
                logMessage += STRLOG_Newline + STRLOG_CompletedMarker + message;
            }
            log(level, "", null, logMessage);
        }

        return logMessage;
    }

    /**
     *
     * @param level
     * @param sourceClass
     * @param sourceMethod
     * @param msg
     */
    private static synchronized void log(Level level, String sourceClass, String sourceMethod, String message) {
        /*
         * Determine where to send the message
         */
        if (Logfile.instance != null) {
            logger.log(level, message);
        } else {
            logger.logp(level, sourceClass, sourceMethod, message);
        }
    }

    /**
     *
     * @param level
     * @param message
     */
    private static void log(Level level, String message) {
        Logfile thisInstance = Logfile.instance;
        if (level.intValue() >= thisInstance.level.intValue() && thisInstance.level != Level.OFF) {
            synchronized (thisInstance.queueLock) {
                thisInstance.queue.add(String.format(STR_TimestampMessage_arg3, thisInstance.CreateTimestamp(), level.toString(), message));
            }
        }
    }

    /**
     *
     * @param logfilePath
     */
    public Logfile(String logfilePath) {
        /*
         * Save to local variables
         */
        this.logfilePath = logfilePath;
        this.running = false;
        this.level = Level.INFO;

        /*
         * Create a queue for logfile messages
         */
        this.queueLock = new Object();
        this.queue = new LinkedList<>();
        this.queue.add(String.format(STRLOG_LogfileName_arg, CreateDatedFilename(logfilePath)));
        this.queue.add(String.format(STRLOG_LoggingLevel_arg, this.level.toString()));
    }

    /**
     *
     */
    public void Close() {
        /*
         * Stop the thread
         */
        if (this.running == true) {
            this.running = false;

            try {
                this.thread.join();
            } catch (InterruptedException ex) {
            }
        }
    }

    /**
     *
     * @return
     */
    private boolean Start() {
        System.out.println("Logfile.Start" + STRLOG_Called);

        boolean success = false;

        try {
            /*
             * Create a new thread and start it
             */
            this.thread = new Thread(this);
            if (this.thread == null) {
                throw new NullPointerException(Thread.class.getSimpleName());
            }
            this.thread.start();

            /*
             * Give it a chance to start running and then check that it has started
             */
            for (int i = 0; i < 5; i++) {
                if ((success = this.running) == true) {
                    break;
                }

                Delay.MilliSeconds(500);
                System.out.println('?');
            }

            if (success == false) {
                throw new RuntimeException(STRERR_ThreadFailedToStart);
            }
        } catch (Exception ex) {
            System.err.println(ex.toString());
        }

        System.out.println("Logfile.Start" + STRLOG_Completed);

        return success;
    }

    @Override
    public void run() {
        System.out.println("Logfile.run" + STRLOG_Called);

        this.running = true;

        try {
            while (running == true) {
                Delay.MilliSeconds(1000);
                System.out.print('*');

                /*
                 * Check for queued messages
                 */
                if (this.queue.size() == 0) {
                    continue;
                }

                /*
                 * Write queued messages to logfile
                 */
                synchronized (this.queueLock) {
                    BufferedWriter out = null;
                    try {
                        /*
                         * Open logfile for appending
                         */
                        String filename = CreateDatedFilename(this.logfilePath);
                        out = new BufferedWriter(new FileWriter(filename, true));

                        /*
                         * Write all queued messages
                         */
                        while (this.queue.size() > 0) {
                            out.write(this.queue.removeFirst() + STRLOG_Newline);
                        }
                    } catch (IOException x) {
                        System.err.println(x);
                    } finally {
                        if (out != null) {
                            /*
                             * Close the logfile
                             */
                            try {
                                out.flush();
                                out.close();
                            } catch (IOException ex) {
                            }
                        }
                    }

                }
            }
        } catch (Exception ex) {
        }

        System.out.println("Logfile.run" + STRLOG_Completed);
    }

    /**
     *
     * @param path
     * @return String
     */
    private String CreateDatedFilename(String path) {
        /*
         * The current date becomes the name of the file
         */
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(STR_FilenameDateFormat);
        String filename = simpleDateFormat.format(Calendar.getInstance().getTime()) + STR_FilenameExtension;
        if (path != null) {
            filename = Paths.get(path).resolve(filename).toString();
        }

        return filename;
    }

    /**
     *
     * @return
     */
    private String CreateTimestamp() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(STR_TimestampFormat);
        String timestamp = simpleDateFormat.format(Calendar.getInstance().getTime());

        return timestamp;
    }
}
