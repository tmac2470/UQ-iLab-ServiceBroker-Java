/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uq.ilabs.library.datatypes.processagent;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

/**
 *
 * @author uqlpayne
 */
public class ObjectFactory {

    private final static String STR_NamespaceURI = "http://ilab.mit.edu/iLabs/type";
    private final static QName QNAME_StatusReport = new QName(STR_NamespaceURI, "StatusReport");

    public JAXBElement<StatusReport> createStatusReport(StatusReport value) {
        return new JAXBElement<>(QNAME_StatusReport, StatusReport.class, null, value);
    }
}
