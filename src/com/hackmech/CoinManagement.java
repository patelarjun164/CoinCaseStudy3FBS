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

    public boolean addCoin(Coin coin) throws SQLException {

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


        String msg = String.format(
                "New Coin Added to your collection!\n" +
                        "Coin ID: %d\n" +
                        "Country: %s\n" +
                        "Denomination: ₹%d\n" +
                        "Current Value: ₹%.2f\n" +
                        "Year of Minting: %d\n" +
                        "Acquired On: %s",
                generatedCoinID,
                coin.getCountry(),
                coin.getDenomination(),
                coin.getCurrentValue(),
                coin.getYearOfMinting(),
                coin.getAcquireDate().toString()
        );
        if (res == 1) {
            SMSSender.sendSms("+919081884526", msg);
        }
        return res == 1;
    }

    private List<Coin> convertResultSetToCoins(ResultSet rs) throws SQLException {
        List<Coin> resultList = new ArrayList<>();

        while (rs.next()) {
            resultList.add(new Coin(
                    rs.getInt("coinid"),
                    rs.getString("country"),
                    rs.getInt("denomination"),
                    rs.getDouble("currentvalue"),
                    rs.getInt("yearofminting"),
                    rs.getDate("acquiredate")
            ));
        }
        return resultList;
    }

    private List<Coin> executeSearchQuery(String query, Object... params) {
        try (PreparedStatement ps = con.prepareStatement(query)) {
            //setting all paramter like setInt, setDouble, setString using generalised method setObject
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }

            //execute query and get result, add every coin object in coin's list
            try (ResultSet rs = ps.executeQuery()) {
                return convertResultSetToCoins(rs);
            }
        } catch (SQLException se) {
            System.out.println("Query Failed: " + se.getMessage());
        }
        return null;
    }

    public List<Coin> searchByCountry(String countryName) {
        return executeSearchQuery("SELECT * FROM coins WHERE country = ?", countryName);
    }

    public List<Coin> searchByYearOfMinting(int year) {
        return executeSearchQuery("SELECT * FROM coins WHERE yearofminting = ?", year);
        //change to save SQL code in new branch
    }

    public List<Coin> searchBYCurrentValue(int currentValue) {

        return executeSearchQuery("SELECT * FROM coins where currentvalue = ?", currentValue);
    }

    public List<Coin> searchByCountryAndDenomination(String country, int denomination) {
        return executeSearchQuery("SELECT * FROM coins WHERE country = ? and denomination = ?", country, denomination);
    }

    public List<Coin> searchByCountryAndYear(String country, int year) {
        return executeSearchQuery("SELECT * FROM coins WHERE country= ? and yearofminting = ?", country, year);
    }

    public List<Coin> searchByCountryDenominationYear(String country, int denomination, int year) {
        return executeSearchQuery("SELECT * FROM coins WHERE LOWER(country) = ? and denomination = ? and yearofminting=?", country, denomination, year);
    }

    // iv. Acquired Date + Country
    public List<Coin> searchByAcquireDateAndCountry(Date acquireDate, String country) {
        return executeSearchQuery("SELECT * FROM coins WHERE acquiredate = ? and country = ?", acquireDate, country);
    }

    public Coin updateCoin(int coinId, double currVal) {
        String selectQuery = "SELECT * from coins WHERE coinid = ?";

        try (   PreparedStatement selectStmt = con.prepareStatement(selectQuery);
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

            //update coin
            ps.setDouble(1, currVal);
            ps.setInt(2, coinId);
            ps.executeUpdate();
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
            deleteStmt.executeUpdate();
            return coin;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
