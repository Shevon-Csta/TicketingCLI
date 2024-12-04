package com.start.ticketing;

import java.util.Queue;
import java.util.LinkedList;

/**
 * TicketPool - Manages ticket operations in the system.
 * Ensures thread safety for producers (vendors) and consumers (customers).
 */
public class TicketPool {
    private final Queue<Integer> tickets = new LinkedList<>(); // Ticket storage
    private final int maxCapacity; // Maximum ticket pool capacity

    // Constructor to initialize the max capacity
    public TicketPool(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    /**
     * Adds tickets to the pool.
     * Waits if the pool is full.
     */
    public synchronized void addTickets(int ticket) throws InterruptedException {
        while (tickets.size() >= maxCapacity) {
            wait(); // Wait until space is available
        }
        tickets.add(ticket);
        System.out.println("Ticket added: " + ticket);
        notifyAll(); // Notify consumers that tickets are available
    }

    /**
     * Removes a ticket from the pool.
     * Waits if the pool is empty.
     */
    public synchronized int removeTicket() throws InterruptedException {
        while (tickets.isEmpty()) {
            wait(); // Wait until tickets are available
        }
        int ticket = tickets.poll();
        System.out.println("Ticket purchased: " + ticket);
        notifyAll(); // Notify producers that space is available
        return ticket;
    }

    /**
     * Returns the current number of tickets in the pool.
     */
    public synchronized int getTicketCount() {
        return tickets.size();
    }
}