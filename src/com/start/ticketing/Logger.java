package com.start.ticketing;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Logger - Handles logging in a separate thread to avoid interfering with user input.
 */
public class Logger implements Runnable {
    private final BlockingQueue<String> logQueue = new LinkedBlockingQueue<>();
    private volatile boolean running = true;

    /**
     * Adds a log message to the queue.
     */
    public void log(String message) {
        logQueue.offer(message);
    }

    /**
     * Stops the logger.
     */
    public void stop() {
        running = false;
    }

    @Override
    public void run() {
        while (running || !logQueue.isEmpty()) {
            try {
                String message = logQueue.poll();
                if (message != null) {
                    System.out.println(message);
                }
            } catch (Exception e) {
                System.err.println("Logger error: " + e.getMessage());
            }
        }
    }
}
