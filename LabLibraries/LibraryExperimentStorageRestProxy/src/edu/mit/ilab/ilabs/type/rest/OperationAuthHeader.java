package edu.mit.ilab.ilabs.type.rest;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for OperationAuthHeader complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="OperationAuthHeader">
 *   &lt;complexContent>
 *     &lt;extension base="{http://ilab.mit.edu/iLabs/type}AuthenticationHeader">
 *       &lt;anyAttribute/>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OperationAuthHeader")
public class OperationAuthHeader
        extends AuthenticationHeader {
}
