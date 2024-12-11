package com.start.ticketing;

import java.util.LinkedList;
import java.util.Queue;

public class TicketPool {
    private final int maxPoolSize;
    private final Queue<String> tickets = new LinkedList<>();
    private final Logger logger;
    private int activeProducers = 0;

    public TicketPool(int maxPoolSize, Logger logger) {
        this.maxPoolSize = maxPoolSize;
        this.logger = logger;
    }

    public synchronized void addTicket(String ticket) throws InterruptedException {
        while (tickets.size() >= maxPoolSize) {
            wait();
        }
        tickets.add(ticket);
        logger.log("Added ticket: " + ticket + " | Current pool size: " + tickets.size());
        notifyAll();
    }

    public synchronized String removeTicket() throws InterruptedException {
        while (tickets.isEmpty() && activeProducers > 0) {
            wait();
        }
        if (tickets.isEmpty() && activeProducers == 0) {
            return null;
        }
        String ticket = tickets.poll();
        logger.log("Removed ticket: " + ticket + " | Current pool size: " + tickets.size());
        notifyAll();
        return ticket;
    }

    public synchronized void incrementActiveProducers() {
        activeProducers++;
    }

    public synchronized void decrementActiveProducers() {
        activeProducers--;
        notifyAll();
    }

    public synchronized boolean isAllTicketsSold() {
        return tickets.isEmpty() && activeProducers == 0;
    }

    public void log(String s) {
    }
}
