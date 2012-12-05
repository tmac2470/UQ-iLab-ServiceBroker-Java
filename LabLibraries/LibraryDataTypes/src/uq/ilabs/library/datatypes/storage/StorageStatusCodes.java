/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uq.ilabs.library.datatypes.storage;

/**
 *
 * @author uqlpayne
 */
public enum StorageStatusCodes {

    /*
     * Batch status codes
     */
    BatchMask(0x000F),
    /**
     * Ready to execute
     */
    BatchReady(0),
    /**
     * Waiting in the execution queue
     */
    BatchWaiting(1),
    /**
     * Currently running
     */
    BatchRunning(2),
    /**
     * Completely normally
     */
    BatchCompleted(3),
    /**
     * Terminated with errors
     */
    BatchFailed(4),
    /**
     * Cancelled by user before execution had begun
     */
    BatchCancelled(5),
    /**
     * Unknown experimentID
     */
    BatchUnknown(6),
    /**
     * Invalid experiment
     */
    BatchInvalid(7),
    /*
     * Interactive status codes
     */
    InteractiveMask(0xFFF0),
    Unknown(0x0010),
    Initialised(0x0020),
    Open(0x0080),
    Reopened(0x00C0),
    Running(0x0100),
    Closed(0x0200),
    Timeout(0x0400),
    UserAction(0x0800),
    Error(0x1000),
    ClosedTimeout(0x0600),
    ClosedUserAction(0x0A00),
    ClosedError(0x1200);
    //
    //<editor-fold defaultstate="collapsed" desc="Properties">
    private final int value;

    public int getValue() {
        return value;
    }
    //</editor-fold>

    private StorageStatusCodes(int value) {
        this.value = value;
    }

    public static StorageStatusCodes ToStorageStatusCode(int value) {
        switch (value) {
            case 0:
                return BatchReady;
            case 1:
                return BatchWaiting;
            case 2:
                return BatchRunning;
            case 3:
                return BatchCompleted;
            case 4:
                return BatchFailed;
            case 5:
                return BatchCancelled;
            case 6:
                return BatchUnknown;
            case 7:
                return BatchInvalid;
            case 0x0010:
                return Unknown;
            case 0x0020:
                return Initialised;
            case 0x0080:
                return Open;
            case 0x00C0:
                return Reopened;
            case 0x0100:
                return Running;
            case 0x0200:
                return Closed;
            case 0x0400:
                return Timeout;
            case 0x0800:
                return UserAction;
            case 0x1000:
                return Error;
            default:
                return Unknown;
        }
    }
}
