package com.start.ticketing;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Logger - Logs real-time events in a separate thread.
 */
public class Logger implements Runnable {
    private final BlockingQueue<String> logQueue = new LinkedBlockingQueue<>();
    private volatile boolean running = true;

    /**
     * Adds a log message to the queue.
     *
     * @param message The message to log.
     */
    public void log(String message) {
        logQueue.offer(message);
    }

    /**
     * Stops the logger thread.
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
                System.err.println("Logger encountered an error: " + e.getMessage());
            }
        }
    }
}
