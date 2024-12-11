package com.start.ticketing;

import java.util.Queue;
import java.util.LinkedList;

/**
 * TicketPool - Manages ticket operations and synchronization.
 */
public class TicketPool {
    private final Queue<String> tickets = new LinkedList<>();
    private final int maxCapacity; // Maximum tickets the pool can hold
    private int ticketsProduced = 0; // Total tickets produced
    private final int totalTickets; // Total tickets to produce
    private final Logger logger; // Logger for real-time updates

    public TicketPool(int maxCapacity, int totalTickets, Logger logger) {
        this.maxCapacity = maxCapacity;
        this.totalTickets = totalTickets;
        this.logger = logger;
    }

    public synchronized void addTickets(String ticket) throws InterruptedException {
        while (tickets.size() >= maxCapacity) {
            wait(); // Wait until space is available
        }
        tickets.add(ticket);
        ticketsProduced++;
        log("Ticket added: " + ticket); // Log addition
        notifyAll(); // Notify consumers
    }

    public synchronized String removeTicket() throws InterruptedException {
        while (tickets.isEmpty()) {
            wait(); // Wait until tickets are available
        }
        String ticket = tickets.poll();
        log("Ticket purchased: " + ticket); // Log purchase
        notifyAll(); // Notify producers
        return ticket;
    }

    public synchronized int getTicketCount() {
        return tickets.size();
    }

    public synchronized boolean allTicketsProduced() {
        return ticketsProduced >= totalTickets;
    }

    public synchronized boolean allTicketsSold() {
        return ticketsProduced >= totalTickets && tickets.isEmpty();
    }

    /**
     * Logs a message using the Logger. Now public for access by other classes.
     */
    public void log(String message) {
        logger.log(message);
    }
}
