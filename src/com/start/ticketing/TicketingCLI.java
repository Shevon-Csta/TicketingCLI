package com.start.ticketing;

import java.util.Scanner;

public class TicketingCLI {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // User Inputs
        System.out.println("Welcome to the Real-Time Ticketing Simulation!");
        System.out.print("Enter number of vendors: ");
        int numVendors = scanner.nextInt();
        System.out.print("Enter number of customers: ");
        int numCustomers = scanner.nextInt();
        System.out.print("Enter ticket release rate (tickets per second): ");
        int releaseRate = scanner.nextInt();
        System.out.print("Enter ticket retrieval rate (tickets per second): ");
        int retrievalRate = scanner.nextInt();
        System.out.print("Enter maximum ticket pool size: ");
        int maxPoolSize = scanner.nextInt();
        System.out.print("Enter maximum tickets per vendor: ");
        int maxTicketsPerVendor = scanner.nextInt();

        // Initialize Logger and TicketPool
        Logger logger = new Logger();
        Thread loggerThread = new Thread(logger);
        loggerThread.start();

        TicketPool ticketPool = new TicketPool(maxPoolSize, logger);

        // Create Vendor Threads
        Thread[] vendorThreads = new Thread[numVendors];
        for (int i = 0; i < numVendors; i++) {
            String vendorId = "Vendor-" + (i + 1);
            vendorThreads[i] = new Thread(new Vendor(ticketPool, releaseRate, vendorId, maxTicketsPerVendor));
        }

        // Create Customer Threads
        Thread[] customerThreads = new Thread[numCustomers];
        for (int i = 0; i < numCustomers; i++) {
            String customerId = "Customer-" + (i + 1);
            customerThreads[i] = new Thread(new Customer(ticketPool, retrievalRate, customerId));
        }

        // Start All Threads
        for (Thread vendorThread : vendorThreads) {
            vendorThread.start();
        }
        for (Thread customerThread : customerThreads) {
            customerThread.start();
        }

        // Wait for All Threads to Finish
        try {
            for (Thread vendorThread : vendorThreads) {
                vendorThread.join();
            }
            for (Thread customerThread : customerThreads) {
                customerThread.join();
            }
        } catch (InterruptedException e) {
            System.err.println("Simulation interrupted.");
        }

        // Stop Logger
        logger.stop();
        System.out.println("Simulation complete. Goodbye!");
        scanner.close();
    }
}
