package com.start.ticketing;

import java.util.Scanner;

/**
 * TicketingCLI - Allows the user to configure and start the ticketing simulation.
 */
public class TicketingCLI {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int totalTickets = 0;
        int ticketReleaseRate = 0;
        int customerRetrievalRate = 0;
        int maxTicketCapacity = 0;
        int numVendors = 0;

        System.out.println("Welcome to the Real-Time Ticketing Simulation!");

        // Input: Total Tickets
        while (true) {
            try {
                System.out.print("Enter the total number of tickets to produce: ");
                totalTickets = Integer.parseInt(scanner.nextLine());
                if (totalTickets > 0) break;
                else System.out.println("Total tickets must be a positive number.");
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }

        // Input: Ticket Release Rate
        while (true) {
            try {
                System.out.print("Enter the ticket release rate (tickets per second): ");
                ticketReleaseRate = Integer.parseInt(scanner.nextLine());
                if (ticketReleaseRate > 0) break;
                else System.out.println("Ticket release rate must be a positive number.");
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }

        // Input: Customer Retrieval Rate
        while (true) {
            try {
                System.out.print("Enter the customer retrieval rate (tickets per second): ");
                customerRetrievalRate = Integer.parseInt(scanner.nextLine());
                if (customerRetrievalRate > 0) break;
                else System.out.println("Customer retrieval rate must be a positive number.");
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }

        // Input: Max Ticket Pool Capacity
        while (true) {
            try {
                System.out.print("Enter the maximum ticket pool capacity: ");
                maxTicketCapacity = Integer.parseInt(scanner.nextLine());
                if (maxTicketCapacity > 0) break;
                else System.out.println("Max ticket pool capacity must be a positive number.");
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }

        // Input: Number of Vendors
        while (true) {
            try {
                System.out.print("Enter the number of vendors: ");
                numVendors = Integer.parseInt(scanner.nextLine());
                if (numVendors > 0) break;
                else System.out.println("Number of vendors must be a positive number.");
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }

        Logger logger = new Logger();
        Thread loggerThread = new Thread(logger);
        loggerThread.start();

        TicketPool ticketPool = new TicketPool(maxTicketCapacity, totalTickets, logger);

        // Create vendor threads with unique identifiers
        Thread[] vendorThreads = new Thread[numVendors];
        for (int i = 0; i < numVendors; i++) {
            String vendorId = String.valueOf((char) ('A' + i)); // Assign IDs "A", "B", "C", etc.
            vendorThreads[i] = new Thread(new Vendor(ticketPool, ticketReleaseRate, vendorId));
        }

        // Create customer thread
        Thread customerThread = new Thread(new Customer(ticketPool, customerRetrievalRate));

        // Wait for user to start the process
        System.out.println("Press ENTER to start the ticketing simulation...");
        scanner.nextLine();

        // Start all threads
        System.out.println("Starting simulation...");
        for (Thread vendorThread : vendorThreads) {
            vendorThread.start();
        }
        customerThread.start();

        // Wait for threads to complete
        try {
            for (Thread vendorThread : vendorThreads) {
                vendorThread.join();
            }
            customerThread.join();
        } catch (InterruptedException e) {
            System.err.println("Simulation interrupted.");
        }

        // Stop the logger
        logger.stop();
        System.out.println("Simulation complete. Goodbye!");
        scanner.close();
    }
}
