/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uq.ilabs.library.labsidescheduling.client;

import uq.ilabs.library.labsidescheduling.engine.LabConsts;

/**
 *
 * @author uqlpayne
 */
public class Consts extends LabConsts {

    /*
     * Session variables
     */
    public static final String STRSSN_LabsideScheduling = "LabsideScheduling";
    public static final String STRPRM_Timezone = "Timezone";
    /*
     * Webpage URLs
     */
    public static final String STRURL_Faces = "/faces/";
    public static final String STRURL_LabsideSchedulingServlet = "/LabsideScheduling.do";
    public static final String STRURL_Home = "Home.xhtml";
    /*
     * Webpage request parameters
     */
    public static final String STRREQ_CouponId = "coupon_id";
    public static final String STRREQ_Passkey = "passkey";
    public static final String STRREQ_IssuerGuid = "issuer_guid";
    public static final String STRREQ_SbUrl = "sb_url";
    public static final String STRREQ_Servlet = "servlet";
    /*
     * Webpage style classes
     */
    public static final String STRSTL_InfoMessage = "infomessage";
    public static final String STRSTL_WarningMessage = "warningmessage";
    public static final String STRSTL_ErrorMessage = "errormessage";
}
