/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uq.ilabs.library.servicebroker.engine;

/**
 *
 * @author uqlpayne
 */
public class LabConsts {

    /*
     * Initialisation parameters
     */
    public static final String STRPRM_LogFilesPath = "LogFilesPath";
    public static final String STRPRM_LogLevel = "LogLevel";
    public static final String STRPRM_XmlConfigPropertiesPath = "XmlConfigPropertiesPath";
    /*
     * Result string download response
     */
    public static final String STRRSP_ContentType_TextXml = "text/xml";
    public static final String STRRSP_ContentType_Csv = "Application/x-msexcel";
    public static final String STRRSP_Disposition = "content-disposition";
    public static final String STRRSP_Attachment_Csv_arg = "attachment; filename=\"%s.csv\"";
    /*
     * String constants
     */
    public static final String STR_MakeSelection = "-- Make Selection --";
}
