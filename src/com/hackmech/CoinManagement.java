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
        ps.setDate(5, coin.getAcquireDate());

        int res = ps.executeUpdate();
        return res == 1;
    }

    public List<Coin> searchByCountry(String countryName) {
        try {
            List<Coin> resList = new ArrayList<>();
            String query = "SELECT * FROM coins WHERE LOWER(country) = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, countryName.toLowerCase());
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
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

    public List<Coin> searchByYearOfMinting(int year) {
        try {
            List<Coin> resList = new ArrayList<>();
            String query = "SELECT * FROM coins WHERE yearofminting = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, year);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
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

    public List<Coin> searchByCurrentValue() {

        try {
            List<Coin> resList = new ArrayList<>();
            String query = "SELECT * FROM coins order by currentvalue";
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                resList.add(new Coin(
                        rs.getInt("coinid"),
                        rs.getString("country"),
                        rs.getInt("denomination"),
                        rs.getDouble("currentvalue"),
                        rs.getInt("yearofminting"),
                        rs.getDate("acquiredate")
                ));
            }

//            List<int> ids = (2,5,1,4,9);
//            Collections.sort(ids);
//            (1,2,4,5,9)

//            Collections.sort(coins, Comparator.comparingDouble(Coin::getCurrentValue));

            return resList;
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

            while (rs.next()) {
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

            while (rs.next()) {
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

        try {
            List<Coin> resList = new ArrayList<>();
            String query = "SELECT * FROM coins WHERE LOWER(country) = ? and denomination = ? and yearofminting=?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, country.toLowerCase());
            ps.setInt(2, denomination);
            ps.setInt(3, year);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
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

        try {
            List<Coin> resList = new ArrayList<>();
            String query = "SELECT * FROM coins WHERE LOWER(country) = ? and acquiredate = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, country.toLowerCase());
            ps.setDate(2, acquireDate);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
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


    public void updateCoin(int coinId, double currVal) {
        //check weather coin is available with that coinID in db
        //if yes then we will update its currValue to new one
        //if no then will throw an error
        try {
            String selectQuery = "select * from coins where coinid = ?";
            PreparedStatement ps = con.prepareStatement(selectQuery);
            ps.setInt(1, coinId);
            ResultSet rs = ps.executeQuery();

//            rs ---> 0
//rs.next() -->>> 1  //if record exits
            if (rs.next()) {
                String updateQuery = "UPDATE coins SET currentvalue = ? where coinid = ?";

                PreparedStatement ps1 = con.prepareStatement(updateQuery);
                ps1.setDouble(1, currVal);
                ps1.setInt(2, coinId);
                int res = ps1.executeUpdate();
                if (res == 1) {
                    System.out.println("Coin updated Successfully");
                } else {
                    System.out.println("Error in updating coin, Pls enter detail correctly");
                }
            }
            else {
                System.out.println("Coin id not found in database");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void removeCoin(int coinid) {

        String insertQuery = "DELETE FROM coin where coinid = ?";
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(insertQuery);

            ps.setInt(1, coinid);
            int res = ps.executeUpdate();
            if (res == 1) {
                System.out.println("Coin updated Successfully");
            } else {
                System.out.println("Error in updating coin, Pls enter detail correctly");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
