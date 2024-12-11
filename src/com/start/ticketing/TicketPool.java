package com.start.ticketing;

import java.util.LinkedList;
import java.util.Queue;

/**
 * TicketPool - Manages the ticket operations with thread safety.
 */
public class TicketPool {
    private final Queue<String> tickets = new LinkedList<>();
    private final int maxCapacity;  // Maximum size of the ticket pool
    private final Logger logger;   // Logger for logging events
    private boolean vendorsFinished = false;

    /**
     * Constructor for TicketPool.
     *
     * @param maxCapacity Maximum capacity of the ticket pool.
     * @param logger      Logger instance for logging events.
     */
    public TicketPool(int maxCapacity, Logger logger) {
        this.maxCapacity = maxCapacity;
        this.logger = logger;
    }

    /**
     * Adds a ticket to the pool.
     *
     * @param ticket The ticket to be added.
     * @throws InterruptedException If the thread is interrupted while waiting.
     */
    public synchronized void addTicket(String ticket) throws InterruptedException {
        try {
            while (tickets.size() >= maxCapacity) {
                wait(); // Wait if the pool is full
            }
            tickets.add(ticket);
            logger.log("Added ticket: " + ticket);
            notifyAll(); // Notify waiting customers
        } catch (InterruptedException e) {
            logger.log("Error while adding ticket: " + ticket + ". Thread interrupted.");
            throw e;
        }
    }

    /**
     * Removes a ticket from the pool.
     *
     * @return The removed ticket, or null if no tickets are available and vendors are finished.
     * @throws InterruptedException If the thread is interrupted while waiting.
     */
    public synchronized String removeTicket() throws InterruptedException {
        try {
            while (tickets.isEmpty()) {
                if (vendorsFinished) {
                    return null; // No more tickets will be added
                }
                wait(); // Wait if no tickets are available
            }
            int currentCapacity = tickets.size(); // Capture pool size before removal
            String ticket = tickets.poll();
            logger.log("Removed ticket: " + ticket + " | Current pool size: " + (currentCapacity - 1));
            notifyAll(); // Notify waiting vendors
            return ticket;
        } catch (InterruptedException e) {
            logger.log("Error while removing ticket. Thread interrupted.");
            throw e;
        }
    }

    /**
     * Marks vendors as finished producing tickets.
     */
    public synchronized void setVendorsFinished() {
        vendorsFinished = true;
        notifyAll(); // Notify all waiting customers
        logger.log("All vendors have finished producing tickets.");
    }

    /**
     * Checks if all tickets are sold and vendors have finished producing.
     *
     * @return True if all tickets are sold and vendors are done; otherwise, false.
     */
    public synchronized boolean isVendorsFinished() {
        return vendorsFinished && tickets.isEmpty();
    }

    /**
     * Logs a message through the Logger.
     *
     * @param message The message to log.
     */
    public void log(String message) {
        logger.log(message);
    }
}
