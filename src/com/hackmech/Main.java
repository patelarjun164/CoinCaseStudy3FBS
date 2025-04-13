package com.hackmech;

import java.sql.Date;
import java.time.LocalDate;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        CoinManagement cm = new CoinManagement();

        try {
            cm.loadFromDatabase();
            System.out.println("Database loaded successfully.");
        } catch (Exception e) {
            System.out.println("Error loading database: " + e.getMessage());
        }

        while (true) {
            try {
                System.out.println("\n==== Coin Collection Menu ====");
                System.out.println("1. Add Coin");
                System.out.println("2. Search");
                System.out.println("3. Update Coin");
                System.out.println("4. Remove Coin");
                System.out.println("5. Exit");
                System.out.print("Enter your choice: ");

                int choice = Integer.parseInt(sc.nextLine());

                switch (choice) {
                    case 1:
                        try {
                            System.out.print("Enter country: ");
                            String country = sc.nextLine();
                            if (!country.matches("[a-zA-Z ]+")) {
                                throw new IllegalArgumentException("Country name must contain only letters and spaces.");
                            }

                            System.out.print("Enter denomination (int): ");
                            int denomination = Integer.parseInt(sc.nextLine());

                            System.out.print("Enter current value (decimal): ");
                            double currentValue = Double.parseDouble(sc.nextLine());

                            System.out.print("Enter year of minting: ");
                            int yearOfMinting = Integer.parseInt(sc.nextLine());

                            System.out.print("Enter acquire date (YYYY-MM-DD): ");
                            LocalDate localDate = LocalDate.parse(sc.nextLine());
                            Date acquireDate = Date.valueOf(localDate);

                            Coin coin = new Coin(0, country, denomination, currentValue, yearOfMinting, acquireDate);
                            boolean res = cm.addCoin(coin);
                            System.out.println(res ? "Coin added successfully." : "Error: Could not add coin.");
                        } catch (Exception e) {
                            System.out.println("Invalid input: " + e.getMessage());
                        }
                        break;


                    case 2:
                        boolean backToMain = false;
                        while (!backToMain) {
                            try {
                                System.out.println("\n--- Search Menu ---");
                                System.out.println("1. Search by Country");
                                System.out.println("2. Search by Year of Minting");
                                System.out.println("3. Sort by Current Value");
                                System.out.println("4. Search by Country + Denomination");
                                System.out.println("5. Search by Country + Year");
                                System.out.println("6. Search by Country + Denomination + Year");
                                System.out.println("7. Search by Acquired Date + Country");
                                System.out.println("0. Back to Main Menu");
                                System.out.print("Enter your choice: ");

                                int searchChoice = Integer.parseInt(sc.nextLine());

                                switch (searchChoice) {
                                    case 1:
                                        System.out.print("Enter country: ");
                                        List<Coin> res1 = cm.searchByCountry(sc.nextLine());
                                        res1.forEach(System.out::println);
                                        break;

                                    case 2:
                                        System.out.print("Enter year of minting: ");
                                        int year = Integer.parseInt(sc.nextLine());
                                        List<Coin> res2 = cm.searchByYearOfMinting(year);
                                        res2.forEach(System.out::println);
                                        break;

                                    case 3:
                                        System.out.print("Enter current value of the coin: ");
                                        int currentValue = Integer.parseInt(sc.nextLine());
                                        List<Coin> res3 = cm.searchBYCurrentValue(currentValue);
                                        res3.forEach(System.out::println);
                                        break;

                                    case 4:
                                        System.out.print("Enter country: ");
                                        String c4 = sc.nextLine();
                                        System.out.print("Enter denomination: ");
                                        int d4 = Integer.parseInt(sc.nextLine());
                                        List<Coin> res4 = cm.searchByCountryAndDenomination(c4, d4);
                                        res4.forEach(System.out::println);
                                        break;

                                    case 5:
                                        System.out.print("Enter country: ");
                                        String c5 = sc.nextLine();
                                        System.out.print("Enter year of minting: ");
                                        int y5 = Integer.parseInt(sc.nextLine());
                                        List<Coin> res5 = cm.searchByCountryAndYear(c5, y5);
                                        res5.forEach(System.out::println);
                                        break;

                                    case 6:
                                        System.out.print("Enter country: ");
                                        String c6 = sc.nextLine();
                                        System.out.print("Enter denomination: ");
                                        int d6 = Integer.parseInt(sc.nextLine());
                                        System.out.print("Enter year of minting: ");
                                        int y6 = Integer.parseInt(sc.nextLine());
                                        List<Coin> res6 = cm.searchByCountryDenominationYear(c6, d6, y6);
                                        res6.forEach(System.out::println);
                                        break;

                                    case 7:
                                        System.out.print("Enter acquired date (YYYY-MM-DD): ");
                                        Date date = Date.valueOf(LocalDate.parse(sc.nextLine()));
                                        System.out.print("Enter country: ");
                                        String c7 = sc.nextLine();
                                        List<Coin> res7 = cm.searchByAcquireDateAndCountry(date, c7);
                                        res7.forEach(System.out::println);
                                        break;

                                    case 0:
                                        backToMain = true;
                                        break;

                                    default:
                                        System.out.println("Invalid choice. Try again.");
                                }
                            } catch (Exception e) {
                                System.out.println("Error: " + e.getMessage());
                            }
                        }
                        break;

                    case 3:
                        try {
                            System.out.print("Enter coin ID to update: ");
                            int coinId1 = Integer.parseInt(sc.nextLine());
                            System.out.print("Enter new current value: ");
                            double currValue1 = Double.parseDouble(sc.nextLine());
                            cm.updateCoin(coinId1, currValue1);
                        } catch (Exception e) {
                            System.out.println("Invalid input: " + e.getMessage());
                        }
                        break;

                    case 4:
                        try {
                            System.out.print("Enter coin ID to delete: ");
                            int coinId = Integer.parseInt(sc.nextLine());
                            cm.removeCoin(coinId);
                        } catch (Exception e) {
                            System.out.println("Invalid input: " + e.getMessage());
                        }
                        break;

                    case 5:
                        System.out.println("Exiting program.");
                        sc.close();
                        return;

                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            } catch (InputMismatchException e) {
                System.out.println("Input mismatch: " + e.getMessage());
                sc.nextLine(); // Clearing buffer to avoid unwanted values
            } catch (Exception e) {
                System.out.println("Unexpected error: " + e.getMessage());
            }
        }
    }
}
