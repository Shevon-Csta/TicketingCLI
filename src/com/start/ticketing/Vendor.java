package com.start.ticketing;

public class Vendor implements Runnable {
    private final TicketPool ticketPool;
    private final int releaseRate;
    private final String vendorId;
    private final int maxTickets;

    public Vendor(TicketPool ticketPool, int releaseRate, String vendorId, int maxTickets) {
        this.ticketPool = ticketPool;
        this.releaseRate = releaseRate;
        this.vendorId = vendorId;
        this.maxTickets = maxTickets;
    }

    @Override
    public void run() {
        try {
            ticketPool.incrementActiveProducers();
            for (int i = 1; i <= maxTickets; i++) {
                String ticket = vendorId + "-Ticket-" + i;
                ticketPool.addTicket(ticket);
                Thread.sleep(1000 / releaseRate);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Vendor " + vendorId + " interrupted.");
        } finally {
            ticketPool.decrementActiveProducers();
        }
    }
}
