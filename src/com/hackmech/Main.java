package com.hackmech;

import java.sql.Date;
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
            System.out.println("3. Update Coin");
            System.out.println("4. Remove Coin");
            System.out.println("5. Exit");
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

                case 2: {
                    boolean backToMain = false;
                    while (!backToMain) {
                        System.out.println("\n--- Search Menu ---");
                        System.out.println("1. Search by Country");
                        System.out.println("2. Search by Year of Minting");
                        System.out.println("3. Search by Current Value (Sorted)");
                        System.out.println("4. Search by Country + Denomination");
                        System.out.println("5. Search by Country + Year");
                        System.out.println("6. Search by Country + Denomination + Year");
                        System.out.println("7. Search by Acquired Date + Country");
                        System.out.println("0. Back to Main Menu");
                        System.out.print("Enter your choice: ");
                        int searchChoice = sc.nextInt();
                        sc.nextLine(); // consume newline

                        switch (searchChoice) {
                            case 1:
                                System.out.print("Enter country: ");
                                String country = sc.nextLine();
                                List<Coin> res1 = cm.searchByCountry(country);
                                res1.forEach(System.out::println);
                                break;

                            case 2:
                                System.out.print("Enter year of minting: ");
                                int year = sc.nextInt();
                                sc.nextLine();
                                List<Coin> res2 = cm.searchByYearOfMinting(year);
                                res2.forEach(System.out::println);
                                break;

                            case 3:
                                List<Coin> res3 = cm.searchByCurrentValue();
                                res3.forEach(c -> System.out.println(c.getcoinId() + " " + c.getCountry() + " " + c.getDenomination()));
                                break;

                            case 4:
                                System.out.print("Enter country: ");
                                String c4 = sc.nextLine();
                                System.out.print("Enter denomination: ");
                                int d4 = sc.nextInt();
                                sc.nextLine();
                                List<Coin> res4 = cm.searchByCountryAndDenomination(c4, d4);
                                res4.forEach(System.out::println);
                                break;

                            case 5:
                                System.out.print("Enter country: ");
                                String c5 = sc.nextLine();
                                System.out.print("Enter year of minting: ");
                                int y5 = sc.nextInt();
                                sc.nextLine();
                                List<Coin> res5 = cm.searchByCountryAndYear(c5, y5);
                                res5.forEach(System.out::println);
                                break;

                            case 6:
                                System.out.print("Enter country: ");
                                String c6 = sc.nextLine();
                                System.out.print("Enter denomination: ");
                                int d6 = sc.nextInt();
                                System.out.print("Enter year of minting: ");
                                int y6 = sc.nextInt();
                                sc.nextLine();
                                List<Coin> res6 = cm.searchByCountryDenominationYear(c6, d6, y6);
                                res6.forEach(System.out::println);
                                break;

                            case 7:
                                System.out.print("Enter acquired date (YYYY-MM-DD): ");
                                String dateStr = sc.nextLine();
                                Date date = Date.valueOf(LocalDate.parse(dateStr));
                                System.out.print("Enter country: ");
                                String c7 = sc.nextLine();
                                List<Coin> res7 = cm.searchByAcquireDateAndCountry(date, c7);
                                res7.forEach(System.out::println);
                                break;

                            case 0:
                                backToMain = true; // exit search menu
                                break;

                            default:
                                System.out.println("Invalid choice. Try again.");
                        }
                    }
                    break;
                }

                case 3:
                    System.out.println("Enter id you want to delete");
                    int coinId = sc.nextInt();
                    cm.removeCoin(coinId);

                case 4:
                    System.out.println("Enter id you want to update");
                    int coinId1 = sc.nextInt();
                    System.out.println("Enter new currentValue amount");
                    int currValue1 = sc.nextInt();
                    cm.updateCoin(coinId1, currValue1);

                case 5:
                    System.out.println("Exiting program.");
                    sc.close();
                    return;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}