package com.solarsystem.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class StockDataTest {

    private StockData stock;

    @BeforeEach
    public void setUp() {
        stock = new StockData("ABC", "TestStock", 100.0);
    }

    @Test
    public void testConstructorInitialValues() {
        assertEquals("ABC", stock.getSymbol());
        assertEquals("TestStock", stock.getName());
        assertEquals(100.0, stock.getPrice());
        assertEquals(0.0, stock.getChange());
        assertEquals(0.0, stock.getChangePercent());
        assertEquals("neutral", stock.getTrend());
        assertTrue(stock.getTimestamp() > 0);
    }

    @Test
    public void testUpdatePriceChangesValues() {
        double oldPrice = stock.getPrice();
        stock.updatePrice();
        double newPrice = stock.getPrice();
        
        // Price should have changed
        assertNotEquals(oldPrice, newPrice);
        
        // Change should match price difference
        assertEquals(newPrice - oldPrice, stock.getChange(), 0.01);
        
        // Change percent should match
        assertEquals((stock.getChange() / oldPrice) * 100, stock.getChangePercent(), 0.01);
        
        // Trend should be set correctly
        if (stock.getChange() > 0) {
            assertEquals("up", stock.getTrend());
        } else if (stock.getChange() < 0) {
            assertEquals("down", stock.getTrend());
        } else {
            assertEquals("neutral", stock.getTrend());
        }
        
        assertTrue(stock.getTimestamp() > 0);
    }

    @Test
    public void testSettersAndGetters() {
        stock.setSymbol("XYZ");
        stock.setName("NewStock");
        stock.setPrice(200.5);
        stock.setChange(10.0);
        stock.setChangePercent(5.0);
        stock.setTrend("up");
        stock.setTimestamp(123456789L);

        assertEquals("XYZ", stock.getSymbol());
        assertEquals("NewStock", stock.getName());
        assertEquals(200.5, stock.getPrice());
        assertEquals(10.0, stock.getChange());
        assertEquals(5.0, stock.getChangePercent());
        assertEquals("up", stock.getTrend());
        assertEquals(123456789L, stock.getTimestamp());
    }
}
