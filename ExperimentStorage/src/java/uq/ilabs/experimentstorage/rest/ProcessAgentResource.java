/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uq.ilabs.experimentstorage.rest;

import java.util.Calendar;
import javax.ejb.EJB;
import javax.servlet.ServletContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import uq.ilabs.experimentstorage.ExperimentStorageAppBean;
import uq.ilabs.library.datatypes.processagent.StatusReport;
import uq.ilabs.library.lab.exceptions.UnauthorizedException;
import uq.ilabs.library.lab.utilities.Logfile;

/**
 * REST Web Service
 *
 * @author uqlpayne
 */
@Path("")
public class ProcessAgentResource {

    //<editor-fold defaultstate="collapsed" desc="Constants">
    private static final String STR_ClassName = ProcessAgentResource.class.getName();
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Variables">
    @EJB
    private ExperimentStorageAppBean experimentStorageBean;
//    @Context
//    private HttpHeaders httpHeaders;
    @Context
    private ServletContext servletContext;
    //</editor-fold>

    /**
     * Creates a new instance of ProcessAgentResource
     */
    public ProcessAgentResource() {
    }

    /**
     * GET method for ServiceTime
     *
     * @return text/plain
     */
    @Path("ServiceTime")
    @GET
    @Produces("text/plain")
    public String getServiceTime() {
        String plainString = null;

        this.GetNoHeader();

        try {
            Calendar calendar = this.experimentStorageBean.getProcessAgentHandler().getServiceTime();
            plainString = calendar.getTime().toString();
        } catch (UnauthorizedException ex) {
            this.ThrowUnauthorizedException(ex);
        } catch (Exception ex) {
            this.ThrowException(ex);
        }

        return plainString;
    }

    /**
     * GET method for Status
     *
     * @return application/xml
     */
    @Path("Status")
    @GET
    @Produces("application/xml")
    public String getStatus() {
        String xmlString = null;

        this.GetNoHeader();

        try {
            StatusReport statusReport = this.experimentStorageBean.getProcessAgentHandler().getStatus();
            xmlString = statusReport.ToXmlString();
        } catch (UnauthorizedException ex) {
            this.ThrowUnauthorizedException(ex);
        } catch (Exception ex) {
            this.ThrowException(ex);
        }

        return xmlString;
    }

    //================================================================================================================//
    /**
     *
     * @return AuthHeader
     */
    private void GetNoHeader() {
        final String methodName = "GetNoHeader";

        try {
            /*
             * Start the service if not done already
             */
            this.experimentStorageBean.StartService(this.servletContext);

        } catch (Exception ex) {
            Logfile.WriteException(STR_ClassName, methodName, ex);
        }
    }

    /**
     *
     * @param ex
     */
    private void ThrowUnauthorizedException(Exception ex) {
        throw new WebApplicationException(Response.status(Response.Status.UNAUTHORIZED).type(MediaType.TEXT_PLAIN_TYPE).entity(ex.getMessage()).build());
    }

    /**
     *
     * @param ex
     */
    private void ThrowException(Exception ex) {
        throw new WebApplicationException(Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.TEXT_PLAIN_TYPE).entity(ex.getMessage()).build());
    }
}
