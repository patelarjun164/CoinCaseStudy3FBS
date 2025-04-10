package com.hackmech;

import java.sql.Date;

public class Coin {
    private int coinId;
    private String country;
    private int denomination;
    private double currentValue;
    private int yearOfMinting;
    private Date acquireDate;

    public Coin() {
        
    }

    public Coin(int coinId, String country, int denomination, double currentValue, int yearOfMinting, Date acquireDate) {
        this.coinId = coinId;
        this.country = country;
        this.denomination = denomination;
        this.currentValue = currentValue;
        this.yearOfMinting = yearOfMinting;
        this.acquireDate = acquireDate;
    }

    public int getcoinId() {
        return coinId;
    }

    public void setcoinId(int coinId) {
        this.coinId = coinId;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getDenomination() {
        return denomination;
    }

    public void setDenomination(int denomination) {
        this.denomination = denomination;
    }

    public double getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(double currentValue) {
        this.currentValue = currentValue;
    }

    public int getYearOfMinting() {
        return yearOfMinting;
    }

    public void setYearOfMinting(int yearOfMinting) {
        this.yearOfMinting = yearOfMinting;
    }

    public Date getAcquireDate() {
        return acquireDate;
    }

    public void setAcquireDate(Date acquireDate) {
        this.acquireDate = acquireDate;
    }

    @Override
    public String toString() {
        return "Coin{" +
                "coinId=" + coinId +
                ", country='" + country + '\'' +
                ", denomination=" + denomination +
                ", currentValue=" + currentValue +
                ", yearOfMinting=" + yearOfMinting +
                ", acquireDate=" + acquireDate +
                '}';
    }
}
