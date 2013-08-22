/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uq.ilabs.library.labsidescheduling;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import uq.ilabs.library.datatypes.scheduling.TimePeriod;
import uq.ilabs.library.datatypes.ticketing.Coupon;

/**
 *
 * @author uqlpayne
 */
public class ConvertTypes {

    /**
     *
     * @param calendar
     * @return XMLGregorianCalendar
     */
    public static XMLGregorianCalendar Convert(Calendar calendar) {
        XMLGregorianCalendar xmlGregorianCalendar = null;

        if (calendar != null) {
            try {
                DatatypeFactory datatypeFactory = DatatypeFactory.newInstance();
                GregorianCalendar gregorianCalendar = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
                gregorianCalendar.setTime(calendar.getTime());
                xmlGregorianCalendar = datatypeFactory.newXMLGregorianCalendar(gregorianCalendar);
            } catch (DatatypeConfigurationException ex) {
            }
        }

        return xmlGregorianCalendar;
    }

    /**
     *
     * @param arrayOfTimePeriod
     * @return TimePeriod[]
     */
    public static TimePeriod[] Convert(edu.mit.ilab.ilabs.labsidescheduling.proxy.ArrayOfTimePeriod arrayOfTimePeriod) {
        TimePeriod timePeriods[] = null;

        if (arrayOfTimePeriod != null) {
            timePeriods = arrayOfTimePeriod.getTimePeriod().toArray(new TimePeriod[0]);
        }

        return timePeriods;
    }

    /**
     *
     * @param coupon
     * @return edu.mit.ilab.ilabs.labsidescheduling.proxy.Coupon
     */
    public static edu.mit.ilab.ilabs.labsidescheduling.proxy.Coupon Convert(Coupon coupon) {
        edu.mit.ilab.ilabs.labsidescheduling.proxy.Coupon proxyCoupon = null;

        if (coupon != null) {
            proxyCoupon = new edu.mit.ilab.ilabs.labsidescheduling.proxy.Coupon();
            proxyCoupon.setCouponId(coupon.getCouponId());
            proxyCoupon.setIssuerGuid(coupon.getIssuerGuid());
            proxyCoupon.setPasskey(coupon.getPasskey());
        }

        return proxyCoupon;
    }
}
