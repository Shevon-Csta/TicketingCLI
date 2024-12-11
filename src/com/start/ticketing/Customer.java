package com.start.ticketing;

/**
 * Customer - Retrieves tickets from the TicketPool.
 */
public class Customer implements Runnable {
    private final TicketPool ticketPool;
    private final int retrievalRate; // Tickets per second
    private final String customerId; // Customer identifier

    public Customer(TicketPool ticketPool, int retrievalRate, String customerId) {
        this.ticketPool = ticketPool;
        this.retrievalRate = retrievalRate;
        this.customerId = customerId;
    }

    @Override
    public void run() {
        try {
            while (true) {
                String ticket;
                synchronized (ticketPool) {
                    ticket = ticketPool.removeTicket(); // Retrieve ticket
                    if (ticket == null) {
                        break; // Exit if no more tickets are available
                    }
                }
                ticketPool.log(customerId + " purchased " + ticket); // Log after retrieving
                Thread.sleep(1000 / retrievalRate); // Simulate retrieval rate
            }
        } catch (InterruptedException e) {
            ticketPool.log(customerId + " interrupted.");
        }
    }
}
