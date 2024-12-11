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
                synchronized (ticketPool) {
                    if (ticketPool.isAllTicketsSold()) {
                        break;
                    }
                }
                String ticket = ticketPool.removeTicket();
                if (ticket != null) {
                    ticketPool.log(customerId + " purchased " + ticket);
                }
                Thread.sleep(1000 / retrievalRate);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Customer " + customerId + " interrupted.");
        }
    }
}
