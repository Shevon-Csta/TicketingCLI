package com.start.ticketing;

/**
 * Customer - Simulates a consumer that removes tickets from the TicketPool.
 */
public class Customer implements Runnable {
    private final TicketPool ticketPool;
    private final int customerRetrievalRate;

    public Customer(TicketPool ticketPool, int customerRetrievalRate) {
        this.ticketPool = ticketPool;
        this.customerRetrievalRate = customerRetrievalRate;
    }

    @Override
    public void run() {
        try {
            while (true) {
                ticketPool.removeTicket();
                Thread.sleep(1000 / customerRetrievalRate); // Wait based on retrieval rate
            }
        } catch (InterruptedException e) {
            ticketPool.log("Customer interrupted.");
        }
    }
}
