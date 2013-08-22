/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uq.ilabs.library.batchservicebroker;

import edu.mit.ilab.ilabs.batchservicebroker.proxy.ObjectFactory;
import edu.mit.ilab.ilabs.batchservicebroker.proxy.SbAuthHeader;
import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

/**
 *
 * @author uqlpayne
 */
public class QnameFactory {

    private static ObjectFactory objectFactory;
    private static QName sbAuthHeader;

    /**
     *
     * @return edu.mit.ilab.ilabs.batchservicebroker.proxy.ObjectFactory
     */
    public static ObjectFactory getObjectFactory() {
        if (objectFactory == null) {
            objectFactory = new ObjectFactory();
        }
        return objectFactory;
    }

    /**
     *
     * @return QName
     */
    public static QName getSbAuthHeader() {
        if (sbAuthHeader == null) {
            JAXBElement<SbAuthHeader> jaxbElement = getObjectFactory().createSbAuthHeader(new SbAuthHeader());
            sbAuthHeader = jaxbElement.getName();
        }
        return sbAuthHeader;
    }
}
