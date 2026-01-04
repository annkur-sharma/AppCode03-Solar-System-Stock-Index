package com.solarsystem.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class StockData {
    private String symbol;
    private String name;
    private double price;
    private double change;
    private double changePercent;
    private String trend; // "up", "down", "neutral"
    private long timestamp;

    public StockData(String symbol, String name, double price) {
        this.symbol = symbol;
        this.name = name;
        this.price = price;
        this.change = 0.0;
        this.changePercent = 0.0;
        this.trend = "neutral";
        this.timestamp = System.currentTimeMillis();
    }

    public void updatePrice() {
        Random random = new Random();
        double oldPrice = this.price;
        
        // Generate random price change between -10% and +10%
        double changePercent = (random.nextDouble() - 0.5) * 0.2; // -0.1 to 0.1
        double newPrice = oldPrice * (1 + changePercent);
        
        // Round to 2 decimal places
        newPrice = BigDecimal.valueOf(newPrice).setScale(2, RoundingMode.HALF_UP).doubleValue();
        
        this.change = BigDecimal.valueOf(newPrice - oldPrice).setScale(2, RoundingMode.HALF_UP).doubleValue();
        this.changePercent = BigDecimal.valueOf((this.change / oldPrice) * 100).setScale(2, RoundingMode.HALF_UP).doubleValue();
        this.price = newPrice;
        
        if (this.change > 0) {
            this.trend = "up";
        } else if (this.change < 0) {
            this.trend = "down";
        } else {
            this.trend = "neutral";
        }
        
        this.timestamp = System.currentTimeMillis();
    }

    // Getters and Setters
    public String getSymbol() { return symbol; }
    public void setSymbol(String symbol) { this.symbol = symbol; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    
    public double getChange() { return change; }
    public void setChange(double change) { this.change = change; }
    
    public double getChangePercent() { return changePercent; }
    public void setChangePercent(double changePercent) { this.changePercent = changePercent; }
    
    public String getTrend() { return trend; }
    public void setTrend(String trend) { this.trend = trend; }
    
    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
}