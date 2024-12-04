package com.start.ticketing;

import java.util.Scanner;

/**
 * TicketingCLI - Command-Line Interface for configuring and managing
 * the Real-Time Event Ticketing System.
 */
public class TicketingCLI {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

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
                if (maxTicketCapacity > 0) break;
                else System.out.println("Max ticket capacity must be a positive number.");
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }

        TicketPool ticketPool = new TicketPool(maxTicketCapacity, totalTickets);
        Thread vendorThread = null;
        Thread customerThread = null;

        // Command Loop
        while (true) {
            System.out.print("\nEnter a command (start, stop, status, exit): ");
            String command = scanner.nextLine().trim().toLowerCase();

            switch (command) {
                case "start":
                    if (vendorThread == null || !vendorThread.isAlive()) {
                        System.out.println("System started!");
                        vendorThread = new Thread(new Vendor(ticketPool, ticketReleaseRate));
                        customerThread = new Thread(new Customer(ticketPool, customerRetrievalRate));
                        vendorThread.start();
                        customerThread.start();
                    } else {
                        System.out.println("System is already running.");
                    }
                    break;
                case "stop":
                    if (vendorThread != null && vendorThread.isAlive()) {
                        System.out.println("Stopping system...");
                        vendorThread.interrupt();
                        customerThread.interrupt();
                        vendorThread = null;
                        customerThread = null;
                    } else {
                        System.out.println("System is not running.");
                    }
                    break;
                case "status":
                    System.out.println("Current tickets in pool: " + ticketPool.getTicketCount());
                    break;
                case "exit":
                    System.out.println("Exiting the system. Goodbye!");
                    if (vendorThread != null) vendorThread.interrupt();
                    if (customerThread != null) customerThread.interrupt();
                    scanner.close();
                    System.exit(0);
                default:
                    System.out.println("Invalid command. Please try again.");
            }
        }
    }
}
