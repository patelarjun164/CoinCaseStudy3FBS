package com.hackmech;

import java.sql.*;
import java.util.ArrayList;
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
        }
    }

    public Coin addCoin(Coin coin) throws SQLException {

        String insertQuery = "insert into coins (country, denomination, currentvalue, yearofminting, acquiredate) values (?,?,?,?,?)";
        PreparedStatement ps = con.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, coin.getCountry());
        ps.setInt(2, coin.getDenomination());
        ps.setDouble(3, coin.getCurrentValue());
        ps.setInt(4, coin.getYearOfMinting());
        ps.setDate(5, coin.getAcquireDate());

        int res = ps.executeUpdate();
        ResultSet generatedKeys = ps.getGeneratedKeys();
        generatedKeys.next();
        int generatedCoinID = generatedKeys.getInt(1);
        coin.setcoinId(generatedCoinID);

        if (res == 1) {
            loadFromDatabase();
            return coin;
        }
        return null;
    }

    //country name
    public List<Coin> searchByCountry(String countryName) {
        return coins.stream().filter(coin -> coin.getCountry().equalsIgnoreCase(countryName)).toList();
    }

    //year
    public List<Coin> searchByYearOfMinting(int year) {
        return coins.stream().filter(c -> c.getYearOfMinting() == year).toList();
    }

    //currentValue
    public List<Coin> searchBYCurrentValue(int currentValue) {
        return coins.stream().filter(c -> c.getCurrentValue() == currentValue).toList();
    }

    //country + denomination
    public List<Coin> searchByCountryAndDenomination(String country, int denomination) {
        return coins.stream().filter(c -> c.getCountry().equalsIgnoreCase(country) && c.getDenomination() == denomination).toList();
    }

    //country + year
    public List<Coin> searchByCountryAndYear(String country, int year) {
        return coins.stream().filter(c -> c.getCountry().equalsIgnoreCase(country) && c.getYearOfMinting() == year).toList();
    }

    //Country + denomination + year
    public List<Coin> searchByCountryDenominationYear(String country, int denomination, int year) {
        return coins.stream().filter(c -> c.getCountry().equalsIgnoreCase(country) && c.getDenomination() == denomination && c.getYearOfMinting() == year).toList();
    }

    //Acquired Date + Country
    public List<Coin> searchByAcquireDateAndCountry(Date acquireDate, String country) {
        return coins.stream().filter(c -> c.getCountry().equalsIgnoreCase(country) && c.getAcquireDate() == acquireDate).toList();
    }

    public Coin updateCoin(int coinId, double currVal) {
        String selectQuery = "SELECT * from coins WHERE coinid = ?";

        try (PreparedStatement selectStmt = con.prepareStatement(selectQuery);
             PreparedStatement ps = con.prepareStatement("UPDATE coins SET currentvalue = ? WHERE coinid = ?")) {

            // Fetch the coin
            selectStmt.setInt(1, coinId);
            ResultSet rs = selectStmt.executeQuery();

            if (!rs.next()) {
                System.out.println("❌ No coin found with ID: " + coinId);
                return null;
            }

            // Extract coin details
            Coin coin = new Coin(
                    rs.getInt("coinid"),
                    rs.getString("country"),
                    rs.getInt("denomination"),
                    rs.getDouble("currentvalue"),
                    rs.getInt("yearofminting"),
                    rs.getDate("acquiredate")
            );
//            System.out.println(coin);

            //update coin
            ps.setDouble(1, currVal);
            ps.setInt(2, coinId);
            int res = ps.executeUpdate();
            if (res == 1) {
                loadFromDatabase();
            }
            return coin;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public Coin removeCoin(int coinId) {
        String deleteQuery = "DELETE FROM coins WHERE coinid = ?";
        String selectQuery = "SELECT * from coins WHERE coinid = ?";


        try (PreparedStatement selectStmt = con.prepareStatement(selectQuery);
             PreparedStatement deleteStmt = con.prepareStatement(deleteQuery)
        ) {
            // Fetch the coin
            selectStmt.setInt(1, coinId);
            ResultSet rs = selectStmt.executeQuery();

            if (!rs.next()) {
                System.out.println("❌ No coin found with ID: " + coinId);
                return null;
            }

            // Extract coin details
            Coin coin = new Coin(
                    rs.getInt("coinid"),
                    rs.getString("country"),
                    rs.getInt("denomination"),
                    rs.getDouble("currentvalue"),
                    rs.getInt("yearofminting"),
                    rs.getDate("acquiredate")
            );

            //Delete the coin
            deleteStmt.setInt(1, coinId);
            int res = deleteStmt.executeUpdate();
            if (res == 1) {
                loadFromDatabase();
            }
            return coin;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
