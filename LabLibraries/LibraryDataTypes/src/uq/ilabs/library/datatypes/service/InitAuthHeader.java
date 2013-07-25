/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uq.ilabs.library.datatypes.service;

/**
 *
 * @author uqlpayne
 */
public class InitAuthHeader {

    protected String initPasskey;

    public String getInitPasskey() {
        return initPasskey;
    }

    public void setInitPasskey(String initPasskey) {
        this.initPasskey = initPasskey;
    }

    public InitAuthHeader() {
    }

    public InitAuthHeader(String initPasskey) {
        this.initPasskey = initPasskey;
    }
}
