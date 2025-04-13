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
        PreparedStatement ps = con.prepareStatement(insertQuery);
        ps.setString(1, coin.getCountry());
        ps.setInt(2, coin.getDenomination());
        ps.setDouble(3, coin.getCurrentValue());
        ps.setInt(4, coin.getYearOfMinting());
        ps.setDate(5, coin.getAcquireDate());

        int res = ps.executeUpdate();
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

            //execute query and get result
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
    }

    public List<Coin> searchByCurrentValue() {
        return executeSearchQuery("SELECT * FROM coins order by currentvalue");
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
        return executeSearchQuery("SELECT * FROM coins WHERE country = ? and denomination = ? and yearofminting=?", acquireDate, country);
    }

    public void updateCoin(int coinId, double currVal) {
        try (PreparedStatement ps = con.prepareStatement("UPDATE coins SET currentvalue = ? WHERE coinid = ?")) {
            ps.setDouble(1, currVal);
            ps.setInt(2, coinId);
            int rowsUpdated = ps.executeUpdate();

            if (rowsUpdated == 1) {
                System.out.println("Coin updated successfully");
            } else {
                System.out.println("Coin ID not found in database");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void removeCoin(int coinId) {
        String deleteQuery = "DELETE FROM coins WHERE coinid = ?";

        try (PreparedStatement ps = con.prepareStatement(deleteQuery)) {
            ps.setInt(1, coinId);
            int rowsDeleted = ps.executeUpdate();

            if (rowsDeleted == 1) {
                System.out.println("Coin removed successfully");
            } else {
                System.out.println("Coin not found or could not be removed");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
