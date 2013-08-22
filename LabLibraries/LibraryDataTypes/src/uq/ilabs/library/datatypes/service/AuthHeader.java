package uq.ilabs.library.datatypes.service;

/**
 *
 * @author uqlpayne
 */
public class AuthHeader {

    public static final String STR_Identifier = "identifier";
    public static final String STR_Passkey = "passKey";
    //
    protected String identifier;
    protected String passkey;

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getPasskey() {
        return passkey;
    }

    public void setPasskey(String passkey) {
        this.passkey = passkey;
    }
}
