package com.start.ticketing;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class TicketingCLI {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Configuration config;

        System.out.print("Enter the name of the configuration file to load (e.g., 'config.json'): ");
        String configFile = scanner.nextLine();

        try {
            config = Configuration.loadConfiguration(configFile);
        } catch (FileNotFoundException e) {
            System.out.println("Configuration file '" + configFile + "' not found or could not be read.");
            System.out.println("Creating a new configuration...");
            config = createNewConfiguration(scanner);
            Configuration.saveConfiguration(config, configFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Logger logger = new Logger();
        Thread loggerThread = new Thread(logger);
        loggerThread.start();

        TicketPool ticketPool = new TicketPool(config.getMaxPoolSize(), logger);

        Thread[] vendors = new Thread[config.getNumVendors()];
        for (int i = 0; i < config.getNumVendors(); i++) {
            String vendorId = "Vendor-" + (i + 1);
            vendors[i] = new Thread(new Vendor(ticketPool, config.getReleaseRate(), vendorId, config.getMaxTicketsPerVendor()));
        }

        Thread[] customers = new Thread[config.getNumCustomers()];
        for (int i = 0; i < config.getNumCustomers(); i++) {
            String customerId = "Customer-" + (i + 1);
            customers[i] = new Thread(new Customer(ticketPool, config.getRetrievalRate(), customerId));
        }

        for (Thread vendor : vendors) {
            vendor.start();
        }
        for (Thread customer : customers) {
            customer.start();
        }

        try {
            for (Thread vendor : vendors) {
                vendor.join();
            }
            for (Thread customer : customers) {
                customer.join();
            }
        } catch (InterruptedException e) {
            System.err.println("Simulation interrupted: " + e.getMessage());
        }

        logger.stop();
        System.out.println("Simulation completed successfully!");
        scanner.close();
    }

    private static Configuration createNewConfiguration(Scanner scanner) {
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

        return new Configuration(numVendors, numCustomers, releaseRate, retrievalRate, maxPoolSize, maxTicketsPerVendor);
    }
}
