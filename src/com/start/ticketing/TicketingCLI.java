package com.start.ticketing;

import java.util.Scanner;

/**
 * TicketingCLI - Configures and starts the ticketing simulation.
 */
public class TicketingCLI {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

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

        Logger logger = new Logger();
        Thread loggerThread = new Thread(logger);
        loggerThread.start();

        TicketPool ticketPool = new TicketPool(maxPoolSize, logger);

        Thread[] vendorThreads = new Thread[numVendors];
        for (int i = 0; i < numVendors; i++) {
            String vendorId = "Vendor-" + (i + 1);
            vendorThreads[i] = new Thread(new Vendor(ticketPool, releaseRate, vendorId, maxTicketsPerVendor));
        }

        Thread[] customerThreads = new Thread[numCustomers];
        for (int i = 0; i < numCustomers; i++) {
            String customerId = "Customer-" + (i + 1);
            customerThreads[i] = new Thread(new Customer(ticketPool, retrievalRate, customerId));
        }

        for (Thread vendorThread : vendorThreads) {
            vendorThread.start();
        }

        for (Thread customerThread : customerThreads) {
            customerThread.start();
        }

        try {
            for (Thread vendorThread : vendorThreads) {
                vendorThread.join();
            }
            ticketPool.setVendorsFinished(); // Notify customers that vendors are done
            for (Thread customerThread : customerThreads) {
                customerThread.join();
            }
        } catch (InterruptedException e) {
            System.err.println("Simulation interrupted.");
        }

        logger.stop();
        System.out.println("Simulation complete. Goodbye!");
        scanner.close();
    }
}
