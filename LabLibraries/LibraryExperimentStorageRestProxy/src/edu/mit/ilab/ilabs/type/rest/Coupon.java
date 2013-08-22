package edu.mit.ilab.ilabs.type.rest;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for Coupon complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="Coupon">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="couponId" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="issuerGuid" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="passkey" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Coupon", propOrder = {
    "couponId",
    "issuerGuid",
    "passkey"
})
public class Coupon {

    protected long couponId;
    protected String issuerGuid;
    protected String passkey;

    /**
     * Gets the value of the couponId property.
     *
     */
    public long getCouponId() {
        return couponId;
    }

    /**
     * Sets the value of the couponId property.
     *
     */
    public void setCouponId(long value) {
        this.couponId = value;
    }

    /**
     * Gets the value of the issuerGuid property.
     *
     * @return possible object is {@link String }
     *
     */
    public String getIssuerGuid() {
        return issuerGuid;
    }

    /**
     * Sets the value of the issuerGuid property.
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setIssuerGuid(String value) {
        this.issuerGuid = value;
    }

    /**
     * Gets the value of the passkey property.
     *
     * @return possible object is {@link String }
     *
     */
    public String getPasskey() {
        return passkey;
    }

    /**
     * Sets the value of the passkey property.
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setPasskey(String value) {
        this.passkey = value;
    }
}
