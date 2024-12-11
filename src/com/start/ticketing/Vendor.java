package com.start.ticketing;

/**
 * Vendor - Produces tickets and adds them to the TicketPool.
 */
public class Vendor implements Runnable {
    private final TicketPool ticketPool;
    private final int releaseRate; // Tickets per second
    private final String vendorId; // Vendor identifier
    private final int maxTickets;  // Maximum tickets to produce
    private int ticketsProduced = 0;

    public Vendor(TicketPool ticketPool, int releaseRate, String vendorId, int maxTickets) {
        this.ticketPool = ticketPool;
        this.releaseRate = releaseRate;
        this.vendorId = vendorId;
        this.maxTickets = maxTickets;
    }

    @Override
    public void run() {
        try {
            while (ticketsProduced < maxTickets) {
                String ticket = vendorId + "-Ticket-" + (ticketsProduced + 1);
                ticketPool.addTicket(ticket);
                ticketsProduced++;
                Thread.sleep(1000 / releaseRate); // Simulate production rate
            }
            ticketPool.log(vendorId + " has finished producing tickets.");
        } catch (InterruptedException e) {
            ticketPool.log(vendorId + " interrupted.");
        }
    }
}
