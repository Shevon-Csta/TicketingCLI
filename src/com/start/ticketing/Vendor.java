package com.start.ticketing;

/**
 * Vendor - Adds tickets to the TicketPool with unique identifiers.
 */
public class Vendor implements Runnable {
    private final TicketPool ticketPool;
    private final int ticketReleaseRate; // Tickets per second
    private final String vendorId; // Identifier for the vendor (e.g., "A", "B")

    public Vendor(TicketPool ticketPool, int ticketReleaseRate, String vendorId) {
        this.ticketPool = ticketPool;
        this.ticketReleaseRate = ticketReleaseRate;
        this.vendorId = vendorId;
    }

    @Override
    public void run() {
        try {
            int ticketNum = 1;
            while (!ticketPool.allTicketsProduced()) {
                String ticket = ticketNum + "." + vendorId; // e.g., "1.A"
                ticketPool.addTickets(ticket);
                ticketNum++;
                Thread.sleep(1000 / ticketReleaseRate); // Wait based on release rate
            }
            ticketPool.log("Vendor " + vendorId + " has finished producing all tickets.");
        } catch (InterruptedException e) {
            ticketPool.log("Vendor " + vendorId + " interrupted.");
        }
    }
}
