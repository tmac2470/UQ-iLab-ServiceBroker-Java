/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uq.ilabs.library.processagent;

import java.util.ArrayList;
import java.util.UUID;
import java.util.logging.Level;
import uq.ilabs.library.datatypes.ticketing.Coupon;
import uq.ilabs.library.datatypes.ticketing.Ticket;
import uq.ilabs.library.datatypes.ticketing.TicketTypes;
import uq.ilabs.library.lab.database.DBConnection;
import uq.ilabs.library.lab.utilities.Delay;
import uq.ilabs.library.lab.utilities.Logfile;
import uq.ilabs.library.processagent.database.CouponsDB;
import uq.ilabs.library.processagent.database.TicketsDB;

/**
 *
 * @author uqlpayne
 */
public class Ticketing implements Runnable {

    //<editor-fold defaultstate="collapsed" desc="Constants">
    private static final String STR_ClassName = Ticketing.class.getName();
    private static final Level logLevel = Level.FINE;
    /*
     * String constants for logfile messages
     */
    private static final String STRLOG_CouponId_arg = "CouponId: %d";
    private static final String STRLOG_Success_arg = "Success: %s";
    private static final String STRLOG_DeletingTickets_arg = "Deleting tickets: %d";
    private static final String STRLOG_DeletingTicketsExpired_arg = STRLOG_DeletingTickets_arg + " expired";
    /*
     * String constants for exception messages
     */
    private static final String STRERR_ThreadFailedToStart = "Thread failed to start!";
    /*
     * Constants
     */
    private static final int INT_CheckExpiredSecs = 60;
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Variables">
    private CouponsDB couponsDB;
    private TicketsDB ticketsDB;
    private Thread thread;
    private States runState;
    private int timeUntilCheckExpired;
    private boolean running;
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Types">

    private enum States {

        Idle, CheckExpired, Done
    }
    //</editor-fold>

    /**
     *
     * @param dbConnection
     * @throws Exception
     */
    public Ticketing(DBConnection dbConnection) throws Exception {
        final String methodName = "Ticketing";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        /*
         * Check that all parameters are valid
         */
        if (dbConnection == null) {
            throw new NullPointerException(DBConnection.class.getSimpleName());
        }

        /*
         * Initialise locals
         */
        this.couponsDB = new CouponsDB(dbConnection);
        this.ticketsDB = new TicketsDB(dbConnection);

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);
    }

    /**
     *
     * @return
     */
    public boolean Start() {
        final String methodName = "Start";
        Logfile.WriteCalled(STR_ClassName, methodName);

        boolean success = false;

        try {
            /*
             * Create a new thread and start it
             */
            this.thread = new Thread(this);
            if (this.thread == null) {
                throw new NullPointerException(Thread.class.getSimpleName());
            }
            this.thread.start();

            /*
             * Give it a chance to start running and then check that it has started
             */
            for (int i = 0; i < 5; i++) {
                if ((success = this.running) == true) {
                    break;
                }

                Delay.MilliSeconds(500);
                System.out.println('!');
            }

            if (success == false) {
                throw new RuntimeException(STRERR_ThreadFailedToStart);
            }
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
        }

        Logfile.WriteCompleted(STR_ClassName, methodName,
                String.format(STRLOG_Success_arg, success));

        return success;
    }

    /**
     *
     */
    public void Close() {
        final String methodName = "Close";
        Logfile.WriteCalled(STR_ClassName, methodName);

        /*
         * Stop the LabEquipmentEngine thread
         */
        if (this.running == true) {
            this.running = false;

            try {
                this.thread.join();
            } catch (InterruptedException ex) {
                Logfile.WriteError(ex.toString());
            }
        }

        Logfile.WriteCompleted(STR_ClassName, methodName);
    }

    /**
     *
     * @param issuerGuid
     * @return Coupon
     */
    public Coupon CreateCoupon(String issuerGuid) {
        return this.CreateCoupon(issuerGuid, UUID.randomUUID().toString());
    }

    /**
     *
     * @param issuerGuid
     * @param passkey
     * @return Coupon
     */
    public Coupon CreateCoupon(String issuerGuid, String passkey) {
        Coupon coupon = null;

        try {
            Coupon newCoupon = new Coupon(0, issuerGuid, passkey);
            long couponId = this.couponsDB.Add(newCoupon);
            if (couponId > 0) {
                newCoupon.setCouponId(couponId);
                coupon = newCoupon;
            }
        } catch (Exception ex) {
        }

        return coupon;
    }

    /**
     *
     * @param couponId
     * @param issuerGuid
     * @param passkey
     * @return Coupon
     */
    public Coupon CreateCoupon(long couponId, String issuerGuid, String passkey) {
        Coupon coupon = null;

        try {
            Coupon newCoupon = new Coupon(couponId, issuerGuid, passkey);
            couponId = this.couponsDB.Add(newCoupon);
            if (couponId > 0) {
                coupon = newCoupon;
            }
        } catch (Exception ex) {
        }

        return coupon;
    }

    /**
     *
     * @param couponId
     * @param issuerGuid
     * @return
     */
    public boolean DeleteCoupon(long couponId, String issuerGuid) {
        boolean success = false;

        try {
            success = this.couponsDB.Delete(couponId, issuerGuid);
        } catch (Exception ex) {
        }

        return success;
    }

    /**
     *
     * @param couponId
     * @return Coupon
     */
    public Coupon RetrieveCoupon(long couponId, String issuerGuid) {
        Coupon coupon = null;

        try {
            coupon = this.couponsDB.Retrieve(couponId, issuerGuid);
        } catch (Exception ex) {
        }

        return coupon;
    }

    /**
     *
     * @param coupon
     * @param ticketType
     * @param redeemerGuid
     * @param sponsorGuid
     * @param duration
     * @param payload
     * @return Ticket
     */
    public Ticket CreateTicket(Coupon coupon, TicketTypes ticketType, String redeemerGuid, String sponsorGuid, long duration, String payload) {
        return this.CreateTicket(new Ticket(coupon.getCouponId(), ticketType, null, sponsorGuid, redeemerGuid, duration, payload));
    }

    /**
     *
     * @param ticket
     * @return Ticket
     */
    public Ticket CreateTicket(Ticket ticket) {
        try {
            /*
             * Check that the coupon exists
             */
//            if (coupon.equals(this.couponsDB.Retrieve(coupon.getCouponId())) == false) {
//                throw new RuntimeException();
//            }

            long ticketId = this.ticketsDB.Add(ticket);
            if (ticketId > 0) {
                ticket.setTicketId(ticketId);
            }
        } catch (Exception ex) {
            ticket = null;
        }

        return ticket;
    }

    /**
     *
     * @param coupon
     * @param ticketType
     * @param redeemerGuid
     * @return
     */
    public Ticket RetrieveTicket(Coupon coupon, TicketTypes ticketType, String redeemerGuid) {
        Ticket ticket = null;

        try {
            ticket = this.ticketsDB.Retrieve(coupon.getCouponId(), ticketType, redeemerGuid);
        } catch (Exception ex) {
        }

        return ticket;
    }

    /**
     *
     */
    @Override
    public void run() {
        final String methodName = "run";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        /*
         * Initialise state machine
         */
        this.timeUntilCheckExpired = INT_CheckExpiredSecs;
        this.runState = States.CheckExpired;
        this.running = true;

        /*
         * Allow other threads to check the state of this thread
         */
        Delay.MilliSeconds(500);

        /*
         * State machine loop
         */
        try {
            while (this.runState != States.Done) {
                switch (this.runState) {
                    case Idle:
                        /*
                         * Wait a bit
                         */
                        Delay.MilliSeconds(1000);

                        /*
                         * Check if LabEquipment is closing
                         */
                        if (this.running == false) {
                            this.runState = States.Done;
                            break;
                        }

                        /*
                         * Check timeout
                         */
                        if (--this.timeUntilCheckExpired == 0) {
                            this.timeUntilCheckExpired = INT_CheckExpiredSecs;
                            this.runState = States.CheckExpired;
                        }
                        break;

                    case CheckExpired:
                        this.DeleteExpiredTickets();

                        this.runState = States.Idle;
                        break;
                }
            }
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
        }

        /*
         * Thread is no longer running
         */
        this.running = false;

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);
    }

    /**
     *
     * @param couponId
     * @return
     */
    public int DeleteTickets(long couponId) {
        final String methodName = "DeleteTickets";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName,
                String.format(STRLOG_CouponId_arg, couponId));

        int count = 0;

        try {
            /*
             * Get a list of tickets for the specified coupon
             */
            ArrayList<Long> arrayList = this.ticketsDB.GetListCouponId(couponId);
            if (arrayList != null) {
                count = arrayList.size();
                Logfile.Write(String.format(STRLOG_DeletingTickets_arg, count));

                /*
                 * Delete each ticket in the list
                 */
                for (Long value : arrayList) {
                    this.ticketsDB.Delete(value.longValue());
                }
            }
        } catch (Exception ex) {
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);

        return count;
    }

    /**
     *
     * @return
     */
    public int DeleteExpiredTickets() {
        final String methodName = "DeleteExpiredTickets";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        int count = 0;

        try {
            ArrayList<Long> arrayList = this.ticketsDB.GetListExpired();
            if (arrayList != null) {
                count = arrayList.size();
                Logfile.Write(String.format(STRLOG_DeletingTicketsExpired_arg, count));
                for (Long value : arrayList) {
                    /*
                     * Retrieve the ticket to get the coupon Id and then delete it
                     */
                    Ticket ticket = this.ticketsDB.RetrieveByTicketId(value.longValue());
                    this.ticketsDB.Delete(ticket.getTicketId());

                    /*
                     * Delete the coupon
                     */
//                    this.couponsDB.Delete(ticket.getCouponId(), ticket.getIssuerGuid());
                }
            }
        } catch (Exception ex) {
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);

        return count;
    }
}
