/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uq.ilabs.library.processagent;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

/**
 *
 * @author uqlpayne
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _SystemSupportPayload_QNAME = new QName("systemSupport");

    /**
     * Create an instance of
     * {@link JAXBElement }{@code <}{@link SystemSupportPayload }{@code >}}
     */
    @XmlElementDecl(name = "SystemSupportPayload")
    public JAXBElement<SystemSupportPayload> createSystemSupportPayload(SystemSupportPayload value) {
        return new JAXBElement<>(_SystemSupportPayload_QNAME, SystemSupportPayload.class, null, value);
    }
}
