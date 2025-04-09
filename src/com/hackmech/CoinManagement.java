package com.hackmech;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class CoinManagement {

    static Connection con;

    static {
        try {
            con = Connectivity.getConnection();
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void addCoin(Coin coin) throws SQLException {

        String insertQuery = "insert into coins (coinid ,country, denomination, currentvalue, yearofminting, acquiredate) values (?,?,?,?,?,?)";
        PreparedStatement ps = con.prepareStatement(insertQuery);
        ps.setInt(1, coin.getcoinId());
        ps.setString(2, coin.getCountry());
        ps.setInt(3, coin.getDenomination());
        ps.setDouble(4, coin.getCurrentValue());
        ps.setInt(5, coin.getYearOfMinting());
        ps.setDate(6,coin.getAcquireDate());

        int res = ps.executeUpdate();
        if(res==1){
            System.out.println("Coin added Successfully");
        } else {
            System.out.println("Error in adding coin, Pls enter detail correctly");
        }
    }

    public void updateCoin(Coin coin){

    }

    public void removeCoin(Coin coin){

    }


}
