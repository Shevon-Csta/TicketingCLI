package com.start.ticketing;

import java.util.LinkedList;
import java.util.Queue;

public class TicketPool {
    private final Queue<String> tickets = new LinkedList<>();
    private final int maxCapacity;
    private final Logger logger;

    public TicketPool(int maxCapacity, Logger logger) {
        this.maxCapacity = maxCapacity;
        this.logger = logger;
    }

    public synchronized void addTicket(String ticket) throws InterruptedException {
        while (tickets.size() >= maxCapacity) {
            wait(); // Wait until space is available
        }
        tickets.add(ticket);
        logger.log("Added ticket: " + ticket);
        notifyAll(); // Notify waiting customers
    }

    public synchronized String removeTicket() throws InterruptedException {
        while (tickets.isEmpty()) {
            wait(); // Wait until tickets are available
        }
        String ticket = tickets.poll();
        logger.log("Removed ticket: " + ticket);
        notifyAll(); // Notify waiting vendors
        return ticket;
    }

    public synchronized int getTicketCount() {
        return tickets.size();
    }

    public void log(String message) {
        logger.log(message); // Forward log messages to the Logger
    }
}
