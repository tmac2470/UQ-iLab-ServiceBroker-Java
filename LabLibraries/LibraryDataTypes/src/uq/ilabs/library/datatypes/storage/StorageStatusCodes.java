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
    ClosedError(0x1200),
    /*
     * Interactive status codes
     */
    InteractiveMask(0xFFF0);
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
    private StorageStatusCodes(int value) {
        this.value = value;
    }

    /**
     *
     * @param value
     * @return
     */
    public static StorageStatusCodes ToStorageStatusCode(int value) {
        /*
         * Get the interactive status code portion of the value
         */
        value &= InteractiveMask.getValue();

        /*
         * Search for the value
         */
        for (StorageStatusCodes storageStatusCode : StorageStatusCodes.values()) {
            if (storageStatusCode.getValue() == value) {
                return storageStatusCode;
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
    public static StorageStatusCodes ToCode(String value) {
        StorageStatusCodes storageStatusCode;
        try {
            storageStatusCode = StorageStatusCodes.valueOf(value);
        } catch (IllegalArgumentException ex) {
            storageStatusCode = Unknown;
        }
        return storageStatusCode;
    }
}
