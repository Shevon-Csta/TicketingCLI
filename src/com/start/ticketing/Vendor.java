package com.start.ticketing;

/**
 * Vendor - Simulates a producer that adds tickets to the TicketPool.
 */
public class Vendor implements Runnable {
    private final TicketPool ticketPool; // Shared ticket pool
    private final int ticketReleaseRate; // Tickets added per second

    public Vendor(TicketPool ticketPool, int ticketReleaseRate) {
        this.ticketPool = ticketPool;
        this.ticketReleaseRate = ticketReleaseRate;
    }

    @Override
    public void run() {
        try {
            int ticketId = 1; // Unique ID for tickets
            while (true) {
                ticketPool.addTickets(ticketId++); // Add a ticket
                Thread.sleep(1000 / ticketReleaseRate); // Wait based on release rate
            }
        } catch (InterruptedException e) {
            System.out.println("Vendor interrupted.");
        }
    }
}
