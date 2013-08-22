/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uq.ilabs.library.servicebroker.engine.types;

/**
 *
 * @author uqlpayne
 */
public enum GroupTypes {

    Regular(1),
    Request(2),
    CourseStaff(3),
    ServiceAdmin(4),
    BuiltIn(5),
    Unknown(-1);
    /*
     * String constants for built-in groups
     */
    public static final String STR_BuiltInRoot = "ROOT";
    public static final String STR_BuiltInSuperUser = "SuperUser";
    public static final String STR_BuiltInNewUser = "NewUser";
    public static final String STR_BuiltInOrphanedUser = "OrphanedUser";
    public static final String STR_Request = "-Request";
    //
    private static final GroupTypes[] TYPES = {
        Regular,
        Request,
        CourseStaff,
        ServiceAdmin
    };
    //
    public static final String[] STRINGS = {
        "Regular",
        "Request",
        "CourseStaff",
        "ServiceAdmin"
    };
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
    private GroupTypes(int value) {
        this.value = value;
    }

    /**
     *
     * @param value - Integer value of the enumerated type
     * @return GroupTypes
     */
    public static GroupTypes ToType(int value) {
        for (GroupTypes groupType : GroupTypes.values()) {
            if (groupType.getValue() == value) {
                return groupType;
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
     * @return GroupTypes
     */
    public static GroupTypes ToType(String value) {
        GroupTypes groupType;
        try {
            groupType = GroupTypes.valueOf(value);
        } catch (IllegalArgumentException ex) {
            groupType = Unknown;
        }
        return groupType;
    }

    @Override
    public String toString() {
        return (this.ordinal() < STRINGS.length) ? STRINGS[this.ordinal()] : null;
    }
}
