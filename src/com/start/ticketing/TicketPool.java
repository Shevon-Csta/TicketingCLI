package com.start.ticketing;

import java.util.Queue;
import java.util.LinkedList;

/**
 * TicketPool - Manages ticket operations in the system.
 * Ensures thread safety for producers (vendors) and consumers (customers).
 */
public class TicketPool {
    private final Queue<Integer> tickets = new LinkedList<>();
    private final int maxCapacity; // Maximum ticket pool capacity
    private int ticketsProduced = 0; // Total tickets produced
    private final int totalTickets; // Total tickets to produce
    private final Logger logger; // Logger for real-time updates

    // Constructor to initialize the ticket pool
    public TicketPool(int maxCapacity, int totalTickets, Logger logger) {
        this.maxCapacity = maxCapacity;
        this.totalTickets = totalTickets;
        this.logger = logger;
    }

    /**
     * Adds tickets to the pool. Waits if the pool is full.
     */
    public synchronized void addTickets(int ticket) throws InterruptedException {
        while (tickets.size() >= maxCapacity) {
            wait(); // Wait until space is available
        }
        tickets.add(ticket);
        ticketsProduced++;
        log("Ticket added: " + ticket); // Log the addition
        notifyAll(); // Notify consumers that tickets are available
    }

    /**
     * Removes a ticket from the pool. Waits if the pool is empty.
     */
    public synchronized int removeTicket() throws InterruptedException {
        while (tickets.isEmpty()) {
            wait(); // Wait until tickets are available
        }
        int ticket = tickets.poll();
        log("Ticket purchased: " + ticket); // Log the purchase
        notifyAll(); // Notify producers that space is available
        return ticket;
    }

    /**
     * Returns the current number of tickets in the pool.
     */
    public synchronized int getTicketCount() {
        return tickets.size();
    }

    /**
     * Returns whether all tickets have been produced.
     */
    public synchronized boolean allTicketsProduced() {
        return ticketsProduced >= totalTickets;
    }

    /**
     * Logs a message using the Logger. Now public for access by other classes.
     */
    public void log(String message) {
        logger.log(message);
    }
}
