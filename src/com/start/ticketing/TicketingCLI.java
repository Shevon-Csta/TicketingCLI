package com.start.ticketing;

import java.util.Scanner;

/**
 * TicketingCLI - A Command-Line Interface for configuring and managing
 * the Real-Time Event Ticketing System.
 */
public class TicketingCLI {

    public static void main(String[] args) {
        // Create a Scanner object for user input
        Scanner scanner = new Scanner(System.in);

        // Variables to store system configuration
        int totalTickets = 0;
        int ticketReleaseRate = 0;
        int customerRetrievalRate = 0;
        int maxTicketCapacity = 0;

        System.out.println("Welcome to the Real-Time Event Ticketing System CLI!");

        // Input: Total Tickets
        while (true) {
            try {
                System.out.print("Enter the total number of tickets: ");
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

        // Input: Max Ticket Capacity
        while (true) {
            try {
                System.out.print("Enter the maximum ticket pool capacity: ");
                maxTicketCapacity = Integer.parseInt(scanner.nextLine());
                if (maxTicketCapacity >= totalTickets) break;
                else System.out.println("Max ticket capacity must be at least the total number of tickets.");
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }

        // Display Configuration Summary
        System.out.println("\nSystem Configuration:");
        System.out.println("Total Tickets: " + totalTickets);
        System.out.println("Ticket Release Rate: " + ticketReleaseRate);
        System.out.println("Customer Retrieval Rate: " + customerRetrievalRate);
        System.out.println("Max Ticket Capacity: " + maxTicketCapacity);

        // Placeholder for command handling
        // Command loop for controlling the system
        while (true) {
            System.out.println("\nEnter a command (start, stop, status, exit): ");
            String command = scanner.nextLine().trim().toLowerCase();

            switch (command) {
                case "start":
                    System.out.println("System started!");
                    TicketPool ticketPool = new TicketPool(maxTicketCapacity); // Initialize ticket pool
                    Thread vendorThread = new Thread(new Vendor(ticketPool, ticketReleaseRate));
                    Thread customerThread = new Thread(new Customer(ticketPool, customerRetrievalRate));
                    vendorThread.start(); // Start vendor thread
                    customerThread.start(); // Start customer thread
                    break;
                case "stop":
                    System.out.println("System stopped!");
                    // Placeholder: Implement proper thread interruption if needed
                    break;
                case "status":
                    System.out.println("Displaying system status...");
                    // Placeholder: Show current ticket pool count (e.g., ticketPool.getTicketCount())
                    break;
                case "exit":
                    System.out.println("Exiting the system. Goodbye!");
                    scanner.close();
                    System.exit(0);
                default:
                    System.out.println("Invalid command. Please try again.");
            }
        }

    }
}
