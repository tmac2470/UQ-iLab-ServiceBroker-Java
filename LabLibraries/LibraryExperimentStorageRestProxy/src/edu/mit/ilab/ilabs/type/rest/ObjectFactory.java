/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mit.ilab.ilabs.type.rest;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

/**
 *
 * @author uqlpayne
 */
public class ObjectFactory {

    private final static String STR_NamespaceURI = "http://ilab.mit.edu/iLabs/type";
    private final static QName QNAME_StorageStatus = new QName(STR_NamespaceURI, "StorageStatus");

    public JAXBElement<StorageStatus> createStorageStatus(StorageStatus value) {
        return new JAXBElement<>(QNAME_StorageStatus, StorageStatus.class, null, value);
    }
}
