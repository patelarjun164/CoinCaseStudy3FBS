package com.hackmech;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CoinManagement {

    static Connection con;
    private List<Coin> coins = new ArrayList<>();

    static {
        try {
            con = Connectivity.getConnection();
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadFromDatabase() {
        coins.clear();
        String sql = "SELECT * FROM coins";
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                coins.add(new Coin(
                        rs.getInt("coinid"),
                        rs.getString("country"),
                        rs.getInt("denomination"),
                        rs.getDouble("currentvalue"),
                        rs.getInt("yearofminting"),
                        rs.getDate("acquiredate")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean addCoin(Coin coin) throws SQLException {

        String insertQuery = "insert into coins (country, denomination, currentvalue, yearofminting, acquiredate) values (?,?,?,?,?)";
        PreparedStatement ps = con.prepareStatement(insertQuery);
//        ps.setInt(1, coin.getcoinId());
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

    public void updateCoin(Coin coin){

    }

    public void removeCoin(Coin coin){

    }


}
