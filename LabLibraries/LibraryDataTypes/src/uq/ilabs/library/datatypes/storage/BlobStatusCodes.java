/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uq.ilabs.library.datatypes.storage;

/**
 *
 * @author uqlpayne
 */
public enum BlobStatusCodes {

    NotRequested(0),
    Requested(1),
    Downloading(2),
    Cancelled(3),
    Failed(4),
    Corrupt(5),
    Complete(6),
    Deleted(7),
    Unknown(-1);
    //
    //<editor-fold defaultstate="collapsed" desc="Properties">
    private final int value;

    public int getValue() {
        return value;
    }
    //</editor-fold>

    private BlobStatusCodes(int value) {
        this.value = value;
    }

    public static BlobStatusCodes ToBlobStatusCode(int value) {
        switch (value) {
            case 0:
                return NotRequested;
            case 1:
                return Requested;
            case 2:
                return Downloading;
            case 3:
                return Cancelled;
            case 4:
                return Failed;
            case 5:
                return Corrupt;
            case 6:
                return Complete;
            case 7:
                return Deleted;
            default:
                return Unknown;
        }
    }
}
