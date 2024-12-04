package com.start.ticketing;

/**
 * Vendor - Simulates a producer that adds tickets to the TicketPool.
 */
public class Vendor implements Runnable {
    private final TicketPool ticketPool;
    private final int ticketReleaseRate;

    public Vendor(TicketPool ticketPool, int ticketReleaseRate) {
        this.ticketPool = ticketPool;
        this.ticketReleaseRate = ticketReleaseRate;
    }

    @Override
    public void run() {
        try {
            int ticketId = 1;
            while (!ticketPool.allTicketsProduced()) {
                ticketPool.addTickets(ticketId++);
                Thread.sleep(1000 / ticketReleaseRate);
            }
            ticketPool.log("Vendor has finished producing all tickets.");
        } catch (InterruptedException e) {
            ticketPool.log("Vendor interrupted.");
        }
    }
}
