package com.hackmech;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello, World!");

        LocalDate tempDate = LocalDate.of(2024, 4, 9);
        Date acquireDate = Date.valueOf(tempDate);


        CoinManagement cm = new CoinManagement();
        Coin coin = new Coin(1, "India", 20, 500, 2005, acquireDate);

        try {
            cm.addCoin(coin);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}