package com.start.ticketing;

/**
 * Customer - Simulates a consumer that removes tickets from the TicketPool.
 */
public class Customer implements Runnable {
    private final TicketPool ticketPool; // Shared ticket pool
    private final int customerRetrievalRate; // Tickets purchased per second

    public Customer(TicketPool ticketPool, int customerRetrievalRate) {
        this.ticketPool = ticketPool;
        this.customerRetrievalRate = customerRetrievalRate;
    }

    @Override
    public void run() {
        try {
            while (true) {
                ticketPool.removeTicket(); // Purchase a ticket
                Thread.sleep(1000 / customerRetrievalRate); // Wait based on retrieval rate
            }
        } catch (InterruptedException e) {
            System.out.println("Customer interrupted.");
        }
    }
}
