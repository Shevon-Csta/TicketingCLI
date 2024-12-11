package com.start.ticketing;

/**
 * Customer - Removes tickets from the TicketPool.
 */
public class Customer implements Runnable {
    private final TicketPool ticketPool;
    private final int customerRetrievalRate; // Tickets per second

    public Customer(TicketPool ticketPool, int customerRetrievalRate) {
        this.ticketPool = ticketPool;
        this.customerRetrievalRate = customerRetrievalRate;
    }

    @Override
    public void run() {
        try {
            while (!ticketPool.allTicketsSold()) {
                ticketPool.removeTicket();
                Thread.sleep(1000 / customerRetrievalRate); // Simulate ticket consumption rate
            }
            ticketPool.log("Customer has purchased all tickets.");
        } catch (InterruptedException e) {
            ticketPool.log("Customer interrupted.");
        }
    }
}
