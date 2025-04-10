package com.hackmech;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        CoinManagement cm = new CoinManagement();

        cm.loadFromDatabase();
        System.out.println("Database loaded successfully.");

        while (true) {
            System.out.println("\n==== Coin Collection Menu ====");
            System.out.println("1. Add Coin");
            System.out.println("2. Search");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");
            int choice = sc.nextInt();
            sc.nextLine();  // consume newline

            switch (choice) {
                case 1:
                    try {
                        System.out.print("Enter country: ");
                        String country = sc.nextLine();

                        System.out.print("Enter denomination (int): ");
                        int denomination = sc.nextInt();

                        System.out.print("Enter current value (decimal): ");
                        double currentValue = sc.nextDouble();

                        System.out.print("Enter year of minting: ");
                        int yearOfMinting = sc.nextInt();

                        System.out.print("Enter acquire date (YYYY-MM-DD): ");
                        sc.nextLine(); // consume newline
                        String dateStr = sc.nextLine();
                        LocalDate localDate = LocalDate.parse(dateStr);
                        Date acquireDate = Date.valueOf(localDate);

                        Coin coin = new Coin(0, country, denomination, currentValue, yearOfMinting, acquireDate);
                        boolean res = cm.addCoin(coin);
                        if (res) System.out.println("Coin added successfully.");
                        else System.out.println("Error: Could not add coin.");
                    } catch (Exception e) {
                        System.out.println("Invalid input: " + e.getMessage());
                        sc.nextLine(); // clear buffer
                    }
                    break;

                case 2:
                    System.out.println("\n--- Search Menu ---");
                    System.out.println("1. Search by Country");
                    System.out.println("2. Search by Year of Minting");
                    System.out.println("3. Search by Current Value (sorted)");
                    System.out.print("Enter your choice: ");
                    int searchChoice = sc.nextInt();
                    sc.nextLine();  // consume newline

                    switch (searchChoice) {
                        case 1:
                            System.out.print("Enter country to search: ");
                            String searchCountry = sc.nextLine();
                            List<Coin> countryResults = cm.searchByCountry(searchCountry);
                            if (countryResults.isEmpty()) {
                                System.out.println("No coins found for country: " + searchCountry);
                            } else {
                                countryResults.forEach(System.out::println);
                            }
                            break;

                        case 2:
                            System.out.print("Enter year of minting to search: ");
                            int year = sc.nextInt();
                            List<Coin> yearResults = cm.searchByYearOfMinting(year);
                            if (yearResults.isEmpty()) {
                                System.out.println("No coins found for year: " + year);
                            } else {
                                yearResults.forEach(System.out::println);
                            }
                            break;

                        case 3:
//                            List<Coin> sortedResults = cm.searchByYearOfMinting();
//                            sortedResults.forEach(System.out::println);
                            break;

                        default:
                            System.out.println("Invalid search option.");
                    }
                    break;

                case 3:
                    System.out.println("Exiting program.");
                    sc.close();
                    return;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}