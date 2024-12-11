package com.start.ticketing;

public class Customer implements Runnable {
    private final TicketPool ticketPool;
    private final int retrievalRate;
    private final String customerId;

    public Customer(TicketPool ticketPool, int retrievalRate, String customerId) {
        this.ticketPool = ticketPool;
        this.retrievalRate = retrievalRate;
        this.customerId = customerId;
    }

    @Override
    public void run() {
        try {
            while (true) {
                String ticket = ticketPool.removeTicket();
                ticketPool.log(customerId + " purchased " + ticket);
                Thread.sleep(1000 / retrievalRate); // Simulate retrieval rate
            }
        } catch (InterruptedException e) {
            ticketPool.log(customerId + " interrupted.");
        }
    }
}
