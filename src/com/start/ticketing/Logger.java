package com.start.ticketing;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Logger - Handles real-time logging in a separate thread.
 */
public class Logger implements Runnable {
    private final BlockingQueue<String> logQueue = new LinkedBlockingQueue<>();
    private volatile boolean running = true;

    public void log(String message) {
        logQueue.offer(message);
    }

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
