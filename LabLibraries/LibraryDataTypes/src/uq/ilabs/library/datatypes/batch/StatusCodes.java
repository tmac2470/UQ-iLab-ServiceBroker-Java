package uq.ilabs.library.datatypes.batch;

/**
 *
 * @author uqlpayne
 */
public enum StatusCodes {

    /**
     * Ready to execute
     */
    Ready(0),
    /**
     * Waiting in the execution queue
     */
    Waiting(1),
    /**
     * Currently running
     */
    Running(2),
    /**
     * Completely normally
     */
    Completed(3),
    /**
     * Terminated with errors
     */
    Failed(4),
    /**
     * Cancelled by user before execution had begun
     */
    Cancelled(5),
    /**
     * Unknown experimentID
     */
    Unknown(6),
    /**
     * Invalid experiment
     */
    Invalid(7),
    /*
     * Batch status codes
     */
    BatchMask(0x000F);
    //
    //<editor-fold defaultstate="collapsed" desc="Properties">
    private final int value;

    public int getValue() {
        return value;
    }
    //</editor-fold>

    /**
     * Constructor
     *
     * @param value
     */
    private StatusCodes(int value) {
        this.value = value;
    }

    /**
     *
     * @param value
     * @return
     */
    public static StatusCodes ToStatusCode(int value) {
        /*
         * Get the batch status code portion of the value
         */
        value &= BatchMask.getValue();

        /*
         * Search for the value
         */
        for (StatusCodes statusCode : StatusCodes.values()) {
            if (statusCode.getValue() == value) {
                return statusCode;
            }
        }

        /*
         * Value not found
         */
        return Unknown;
    }

    /**
     * 
     * @param value
     * @return 
     */
    public static StatusCodes ToCode(String value) {
        StatusCodes statusCode;
        try {
            statusCode = StatusCodes.valueOf(value);
        } catch (IllegalArgumentException ex) {
            statusCode = Unknown;
        }
        return statusCode;
    }
}
