package com.hackmech;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CoinManagement {

    private Connection con;
    private List<Coin> coins = new ArrayList<>();

    public CoinManagement() {
        try {
            this.con = Connectivity.getConnection();
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Error while connecting to DB: " + e.getMessage());
        }
    }

    public void loadFromDatabase() {
        coins.clear();
        String sql = "SELECT * FROM coins";

        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Coin coin = new Coin(
                        rs.getInt("coinid"),
                        rs.getString("country"),
                        rs.getInt("denomination"),
                        rs.getDouble("currentvalue"),
                        rs.getInt("yearofminting"),
                        rs.getDate("acquiredate")
                );
                coins.add(coin);
            }

        } catch (SQLException e) {
            System.err.println("Error loading coins from database: " + e.getMessage());
            // You can log it or throw a custom exception if needed
        }
    }


    public boolean addCoin(Coin coin) throws SQLException {

        String insertQuery = "insert into coins (country, denomination, currentvalue, yearofminting, acquiredate) values (?,?,?,?,?)";
        PreparedStatement ps = con.prepareStatement(insertQuery);
        ps.setString(1, coin.getCountry());
        ps.setInt(2, coin.getDenomination());
        ps.setDouble(3, coin.getCurrentValue());
        ps.setInt(4, coin.getYearOfMinting());
        ps.setDate(5,coin.getAcquireDate());

        int res = ps.executeUpdate();
        return res == 1;
    }

    public List<Coin> searchByCountry(String countryName){
        try {
            List<Coin> resList = new ArrayList<>();
            String query = "SELECT * FROM coins WHERE LOWER(country) = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, countryName.toLowerCase());
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                resList.add(new Coin(
                        rs.getInt("coinid"),
                        rs.getString("country"),
                        rs.getInt("denomination"),
                        rs.getDouble("currentvalue"),
                        rs.getInt("yearofminting"),
                        rs.getDate("acquiredate")
                ));
            }

            return resList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Coin> searchByYearOfMinting(int year){
        try {
            List<Coin> resList = new ArrayList<>();
            String query = "SELECT * FROM coins WHERE yearofminting = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, year);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                resList.add(new Coin(
                        rs.getInt("coinid"),
                        rs.getString("country"),
                        rs.getInt("denomination"),
                        rs.getDouble("currentvalue"),
                        rs.getInt("yearofminting"),
                        rs.getDate("acquiredate")
                ));
            }

            return resList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Coin> searchByCurrentValue(){

        try {
            List<Coin> resList = new ArrayList<>();
//            String query = "SELECT * FROM coins WHERE currentvalue = ?";
//            PreparedStatement ps = con.prepareStatement(query);
//            ps.setDouble(1, currValue);
//            ResultSet rs = ps.executeQuery();
//
//            while(rs.next()){
//                resList.add(new Coin(
//                        rs.getInt("coinid"),
//                        rs.getString("country"),
//                        rs.getInt("denomination"),
//                        rs.getDouble("currentvalue"),
//                        rs.getInt("yearofminting"),
//                        rs.getDate("acquiredate")
//                ));
//            }

            Collections.sort(coins, Comparator.comparingDouble(Coin::getCurrentValue));

            return coins;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // i. Country + Denomination
    public List<Coin> searchByCountryAndDenomination(String country, int denomination) {
        try {
            List<Coin> resList = new ArrayList<>();
            String query = "SELECT * FROM coins WHERE LOWER(country) = ? and denomination = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, country.toLowerCase());
            ps.setInt(2, denomination);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                resList.add(new Coin(
                        rs.getInt("coinid"),
                        rs.getString("country"),
                        rs.getInt("denomination"),
                        rs.getDouble("currentvalue"),
                        rs.getInt("yearofminting"),
                        rs.getDate("acquiredate")
                ));
            }

            return resList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
//        List<Coin> result = new ArrayList<>();
//        for (Coin c : coins) {
//            if (c.getCountry().equalsIgnoreCase(country) && c.getDenomination() == denomination) {
//                result.add(c);
//            }
//        }
//        return result;
    }

    // ii. Country + Year of Minting
    public List<Coin> searchByCountryAndYear(String country, int year) {
        try {
            List<Coin> resList = new ArrayList<>();
            String query = "SELECT * FROM coins WHERE LOWER(country) = ? and yearofminting = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, country.toLowerCase());
            ps.setInt(2, year);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                resList.add(new Coin(
                        rs.getInt("coinid"),
                        rs.getString("country"),
                        rs.getInt("denomination"),
                        rs.getDouble("currentvalue"),
                        rs.getInt("yearofminting"),
                        rs.getDate("acquiredate")
                ));
            }

            return resList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
//        List<Coin> result = new ArrayList<>();
//        for (Coin c : coins) {
//            if (c.getCountry().equalsIgnoreCase(country) && c.getYearOfMinting() == year) {
//                result.add(c);
//            }
//        }
//        return result;
    }

    // iii. Country + Denomination + Year of Minting
    public List<Coin> searchByCountryDenominationYear(String country, int denomination, int year) {
//        List<Coin> result = new ArrayList<>();
//        for (Coin c : coins) {
//            if (c.getCountry().equalsIgnoreCase(country)
//                    && c.getDenomination() == denomination
//                    && c.getYearOfMinting() == year) {
//                result.add(c);
//            }
//        }
//        return result;
        try {
            List<Coin> resList = new ArrayList<>();
            String query = "SELECT * FROM coins WHERE LOWER(country) = ? and denomination = ? and yearofminting=?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, country.toLowerCase());
            ps.setInt(2, denomination);
            ps.setInt(3, year);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                resList.add(new Coin(
                        rs.getInt("coinid"),
                        rs.getString("country"),
                        rs.getInt("denomination"),
                        rs.getDouble("currentvalue"),
                        rs.getInt("yearofminting"),
                        rs.getDate("acquiredate")
                ));
            }

            return resList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // iv. Acquired Date + Country
    public List<Coin> searchByAcquireDateAndCountry(Date acquireDate, String country) {
//        List<Coin> result = new ArrayList<>();
//        for (Coin c : coins) {
//            if (c.getAcquireDate().equals(acquireDate)
//                    && c.getCountry().equalsIgnoreCase(country)) {
//                result.add(c);
//            }
//        }
//        return result;
        try {
            List<Coin> resList = new ArrayList<>();
            String query = "SELECT * FROM coins WHERE LOWER(country) = ? and acquiredate = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, country.toLowerCase());
            ps.setDate(2, acquireDate);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                resList.add(new Coin(
                        rs.getInt("coinid"),
                        rs.getString("country"),
                        rs.getInt("denomination"),
                        rs.getDouble("currentvalue"),
                        rs.getInt("yearofminting"),
                        rs.getDate("acquiredate")
                ));
            }

            return resList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public void updateCoin(Coin coin){

    }

    public void removeCoin(Coin coin){

    }


}
