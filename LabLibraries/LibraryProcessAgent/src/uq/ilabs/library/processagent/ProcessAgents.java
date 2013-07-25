/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uq.ilabs.library.processagent;

import java.util.ArrayList;
import java.util.logging.Level;
import javax.annotation.PreDestroy;
import uq.ilabs.library.datatypes.processagent.ProcessAgent;
import uq.ilabs.library.datatypes.processagent.ServiceDescription;
import uq.ilabs.library.datatypes.ticketing.Coupon;
import uq.ilabs.library.lab.database.DBConnection;
import uq.ilabs.library.lab.utilities.Logfile;
import uq.ilabs.library.processagent.database.ProcessAgentsDB;
import uq.ilabs.library.processagent.database.types.ProcessAgentInfo;
import uq.ilabs.library.processagent.database.types.SystemSupportInfo;

/**
 *
 * @author uqlpayne
 */
public class ProcessAgents {

    //<editor-fold defaultstate="collapsed" desc="Constants">
    private static final String STR_ClassName = ProcessAgents.class.getName();
    private static final Level logLevel = Level.CONFIG;
    /*
     * String constants for exception messages
     */
    private static final String STRERR_TicketingThreadFailedToStart = "Ticketing thread failed to start!";
    private static final String STRERR_ServiceNotRegistered = "Service is not registered yet!";
    private static final String STRERR_InvalidServicePasskey_arg = "%s: Invalid service passkey!";
    private static final String STRERR_CreateCouponFailed_arg = "Failed to create coupon (%d)";
    private static final String STRERR_ProcessAgentAddFailed_arg = "Failed to add process agent to database! (%s)";
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Variables">
    private ProcessAgentsDB processAgentsDB;
    private Ticketing ticketing;
    //</editor-fold>

    /**
     *
     * @param dbConnection
     * @throws Exception
     */
    public ProcessAgents(DBConnection dbConnection) throws Exception {
        final String methodName = "ProcessAgents";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        try {
            this.processAgentsDB = new ProcessAgentsDB(dbConnection);
            if (this.processAgentsDB == null) {
                throw new NullPointerException(ProcessAgentsDB.class.getSimpleName());
            }
            this.ticketing = new Ticketing(dbConnection);
            if (this.ticketing == null) {
                throw new NullPointerException(Ticketing.class.getSimpleName());
            }
            if (this.ticketing.Start() == false) {
                throw new RuntimeException(STRERR_TicketingThreadFailedToStart);
            }
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
            throw ex;
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);
    }

    /**
     *
     */
    public void Close() {
        this.ticketing.Close();
    }

    /**
     *
     * @param processAgentInfo
     * @return
     */
    public int Create(ProcessAgentInfo processAgentInfo) {
        /*
         * Add ProcessAgentInfo
         */
        int agentId = this.processAgentsDB.Add(processAgentInfo);
        if (agentId > 0) {
            /*
             * Create coupons
             */
            Coupon coupon = processAgentInfo.getInCoupon();
            this.ticketing.CreateCoupon(coupon.getCouponId(), coupon.getIssuerGuid(), coupon.getPasskey());
            coupon = processAgentInfo.getOutCoupon();
            this.ticketing.CreateCoupon(coupon.getCouponId(), coupon.getIssuerGuid(), coupon.getPasskey());
        }

        return agentId;
    }

    /**
     *
     * @return
     */
    public String[] GetNamesAll() {
        return this.processAgentsDB.GetListAll();
    }

    /**
     *
     * @return
     */
    public ProcessAgentInfo RetrieveSelf() {
        return this.processAgentsDB.RetrieveSelf();
    }

    /**
     *
     * @return
     */
    public SystemSupportInfo RetrieveSystemSupportInfoSelf() {
        return this.processAgentsDB.RetrieveSystemSupportInfoSelf();
    }

    /**
     *
     * @param agentGuid
     * @return
     */
    public ProcessAgentInfo RetrieveByGuid(String agentGuid) {
        ProcessAgentInfo processAgentInfo = this.processAgentsDB.RetrieveByGuid(agentGuid);
        if (processAgentInfo != null) {
            /*
             * Retrieve coupons and add to ProcessAgentInfo
             */
            Coupon coupon = this.ticketing.RetrieveCoupon(processAgentInfo.getInCoupon().getCouponId(), processAgentInfo.getIssuerGuid());
            processAgentInfo.setInCoupon(coupon);
            coupon = this.ticketing.RetrieveCoupon(processAgentInfo.getOutCoupon().getCouponId(), processAgentInfo.getIssuerGuid());
            processAgentInfo.setOutCoupon(coupon);
        }

        return processAgentInfo;
    }

    /**
     *
     * @param agentName
     * @return
     */
    public ProcessAgentInfo RetrieveByName(String agentName) {
        ProcessAgentInfo processAgentInfo = this.processAgentsDB.RetrieveByName(agentName);
        if (processAgentInfo != null) {
            /*
             * Retrieve coupons and add to ProcessAgentInfo
             */
            Coupon coupon = this.ticketing.RetrieveCoupon(processAgentInfo.getInCoupon().getCouponId(), processAgentInfo.getIssuerGuid());
            processAgentInfo.setInCoupon(coupon);
            coupon = this.ticketing.RetrieveCoupon(processAgentInfo.getOutCoupon().getCouponId(), processAgentInfo.getIssuerGuid());
            processAgentInfo.setOutCoupon(coupon);
        }

        return processAgentInfo;
    }

    /**
     *
     * @param initPasskey
     * @param processAgent
     * @param inCoupon
     * @param outCoupon
     * @return
     */
    public ProcessAgent InstallDomainCredentials(String initPasskey, ProcessAgent processAgent, Coupon inCoupon, Coupon outCoupon) throws Exception {
        final String methodName = "InstallDomainCredentials";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        ProcessAgent selfProcessAgent = null;

        try {
            /*
             * Get ProcessAgentInfo for the Service
             */
            ProcessAgentInfo selfProcessAgentInfo = this.processAgentsDB.RetrieveSelf();
            if (selfProcessAgentInfo == null) {
                throw new RuntimeException(STRERR_ServiceNotRegistered);
            }

            /*
             * Check the service passkey
             */
            if (selfProcessAgentInfo.getIssuerGuid().equals(initPasskey) == false) {
                throw new RuntimeException(String.format(STRERR_InvalidServicePasskey_arg, initPasskey));
            }

            /*
             * Convert ProcessAgent to local ProcessAgentInfo
             */
            ProcessAgentInfo processAgentInfo = new ProcessAgentInfo();
            processAgentInfo.setProcessAgent(processAgent);

            /*
             * Add the coupons to ProcessAgentInfo
             */
            if (outCoupon != null) {
                Coupon coupon = this.ticketing.CreateCoupon(outCoupon.getCouponId(), outCoupon.getIssuerGuid(), outCoupon.getPasskey());
                if (coupon == null) {
                    throw new RuntimeException(String.format(STRERR_CreateCouponFailed_arg, outCoupon.getCouponId()));
                }
                processAgentInfo.setOutCoupon(coupon);
                processAgentInfo.setIssuerGuid(outCoupon.getIssuerGuid());
            }

            if (inCoupon != null) {
                Coupon coupon = this.ticketing.CreateCoupon(inCoupon.getCouponId(), inCoupon.getIssuerGuid(), inCoupon.getPasskey());
                if (coupon == null) {
                    throw new RuntimeException(String.format(STRERR_CreateCouponFailed_arg, inCoupon.getCouponId()));
                }
                processAgentInfo.setInCoupon(coupon);

                if (processAgentInfo.getIssuerGuid() == null) {
                    processAgentInfo.setIssuerGuid(inCoupon.getIssuerGuid());
                }
            }

            /*
             * Add ProcessAgentInfo to database
             */
            if (this.processAgentsDB.Add(processAgentInfo) < 0) {
                throw new RuntimeException(String.format(STRERR_ProcessAgentAddFailed_arg, processAgentInfo.getAgentName()));
            }

            /*
             * Update self ProcessAgentInfo in database
             */
            selfProcessAgentInfo.setDomainGuid(processAgentInfo.getAgentGuid());
            if (this.processAgentsDB.Update(selfProcessAgentInfo) == false) {
                throw new RuntimeException(String.format(STRERR_ProcessAgentAddFailed_arg, selfProcessAgentInfo.getAgentName()));
            }

            /*
             * Get ProcessAgent for self
             */
            selfProcessAgent = selfProcessAgentInfo.getProcessAgent();

        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
            throw ex;
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);

        return selfProcessAgent;
    }

    /**
     *
     * @param originalGuid
     * @param processAgent
     * @param xmlSystemSupport
     * @param inCoupon
     * @param outCoupon
     * @return
     */
    public int ModifyDomainCredentials(String originalGuid, ProcessAgent processAgent, String xmlSystemSupport, Coupon inCoupon, Coupon outCoupon) {
        final String methodName = "modifyDomainCredentials";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        int result = -1;

        try {
            ProcessAgentInfo processAgentInfo = this.processAgentsDB.RetrieveByGuid(originalGuid);
            if (processAgentInfo != null) {
                /*
                 * Update the process agent information
                 */
                processAgentInfo.setProcessAgent(processAgent);
                processAgentInfo.setInCoupon(inCoupon);
                processAgentInfo.setOutCoupon(outCoupon);
                if (this.processAgentsDB.Update(processAgentInfo) == true) {
                    result++;
                }

                /*
                 * Check if system support information has been provided
                 */
                if (xmlSystemSupport != null) {
                    /*
                     * Get the system support information
                     */
                    SystemSupportPayload systemSupportPayload = SystemSupportPayload.ToObject(xmlSystemSupport);
                    SystemSupportInfo systemSupportInfo = systemSupportPayload.ToSystemSupportInfo();

                    /*
                     * Check that system support information contains the new agent guid before updating
                     */
                    if (processAgent.getAgentGuid().equals(systemSupportInfo.getAgentGuid())) {
                        if (this.processAgentsDB.Update(systemSupportInfo) == true) {
                            result++;
                        }
                    }
                }
            }
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
            throw ex;
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);

        return result;
    }

    /**
     *
     * @param domainGuid
     * @param serviceGuid
     * @return
     */
    public int RemoveDomainCredentials(String domainGuid, String serviceGuid) {
        final String methodName = "removeDomainCredentials";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        int result = 0;

        try {
            ProcessAgentInfo processAgentInfo = this.processAgentsDB.RetrieveByGuid(serviceGuid);
            if (processAgentInfo != null) {
                /*
                 * Delete the ProcessAgent
                 */
                if (this.processAgentsDB.Delete(processAgentInfo.getAgentId()) == true) {
                    result++;
                }

                /*
                 * Delete the coupons
                 */
                Coupon coupon = processAgentInfo.getInCoupon();
                if (coupon != null) {
                    if (this.ticketing.DeleteCoupon(coupon.getCouponId(), processAgentInfo.getIssuerGuid()) == true) {
                        result++;
                    }
                }
                coupon = processAgentInfo.getOutCoupon();
                if (coupon != null) {
                    if (this.ticketing.DeleteCoupon(coupon.getCouponId(), processAgentInfo.getIssuerGuid()) == true) {
                        result++;
                    }
                }
            }
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
            throw ex;
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);

        return result;
    }

    /**
     *
     * @param registerGuid
     * @param serviceDescriptions
     */
    public ServiceDescription[] Register(String registerGuid, ServiceDescription[] serviceDescriptions) {
        final String methodName = "Register";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        ServiceDescription[] serviceDescriptionArray = null;

        try {
            if (serviceDescriptions != null) {
                ArrayList<ServiceDescription> serviceDescriptionList = new ArrayList<>();

                for (int i = 0; i < serviceDescriptions.length; i++) {
                    /*
                     * Get SystemSupportInfo and save
                     */
                    SystemSupportPayload systemSupportPayload = SystemSupportPayload.ToObject(serviceDescriptions[i].getServiceProviderInfo());
                    SystemSupportInfo systemSupportInfo = systemSupportPayload.ToSystemSupportInfo();
                    if (this.processAgentsDB.Update(systemSupportInfo) == false) {
                        throw new RuntimeException();
                    }

                    /*
                     * Check if caller is requesting SystemSupportInfo
                     */
                    String consumerInfo = serviceDescriptions[i].getConsumerInfo();
                    if (ServiceDescription.STR_RequestSystemSupport.equals(consumerInfo) == true) {
                        /*
                         * Get SystemSupportInfo for self and add to list
                         */
                        SystemSupportPayload selfSystemSupportPayload = new SystemSupportPayload(this.processAgentsDB.RetrieveSystemSupportInfoSelf());
                        String payload = selfSystemSupportPayload.ToXmlString();

                        /*
                         * Add caller's ProcessAgentInfo to register SystemSupportInfo
                         */
                        ProcessAgentInfo processAgentInfo = this.processAgentsDB.RetrieveByGuid(systemSupportPayload.getAgentGuid());
                        if (processAgentInfo != null) {
                            serviceDescriptionList.add(new ServiceDescription(payload, null, null));
                        }
                    }
                }

                /*
                 * Convert list to an array
                 */
                if (serviceDescriptionList.size() > 0) {
                    serviceDescriptionArray = serviceDescriptionList.toArray(new ServiceDescription[serviceDescriptionList.size()]);
                }
            }
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
            throw ex;
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);

        return serviceDescriptionArray;
    }
}
