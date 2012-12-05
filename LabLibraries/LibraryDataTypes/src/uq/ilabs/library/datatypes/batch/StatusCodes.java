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
    Invalid(7);
    //
    //<editor-fold defaultstate="collapsed" desc="Properties">
    private final int value;

    public int getValue() {
        return value;
    }
    //</editor-fold>

    private StatusCodes(int value) {
        this.value = value;
    }

    /**
     *
     * @param value
     * @return
     */
    public static StatusCodes ToStatusCode(int value) {
        if (value == Ready.value) {
            return Ready;
        }
        if (value == Waiting.value) {
            return Waiting;
        }
        if (value == Running.value) {
            return Running;
        }
        if (value == Completed.value) {
            return Completed;
        }
        if (value == Failed.value) {
            return Failed;
        }
        if (value == Cancelled.value) {
            return Cancelled;
        }
        if (value == Invalid.value) {
            return Invalid;
        }
        return Unknown;
    }
}
