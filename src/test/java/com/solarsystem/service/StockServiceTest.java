package com.solarsystem.service;

import com.solarsystem.model.StockData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class StockServiceTest {

    private StockService stockService;

    @BeforeEach
    public void setup() {
        // Reset singleton instance via reflection for a clean state in each test
        try {
            java.lang.reflect.Field instanceField = StockService.class.getDeclaredField("instance");
            instanceField.setAccessible(true);
            instanceField.set(null, null);  // reset instance
        } catch (Exception e) {
            e.printStackTrace();
        }
        stockService = StockService.getInstance();
    }

    @Test
    public void testGetStocksForValidPlanet() {
        List<StockData> earthStocks = stockService.getStocksForPlanet("Earth");
        assertNotNull(earthStocks);
        assertFalse(earthStocks.isEmpty());
        assertEquals("LUNA", earthStocks.get(0).getSymbol());
    }

    @Test
    public void testGetStocksForInvalidPlanetReturnsEmptyList() {
        List<StockData> unknownStocks = stockService.getStocksForPlanet("Pluto");
        assertNotNull(unknownStocks);
        assertTrue(unknownStocks.isEmpty());
    }

    @Test
    public void testUpdateAllStocksChangesPrices() {
        List<StockData> beforeUpdate = stockService.getStocksForPlanet("Mars");
        double originalPrice = beforeUpdate.get(0).getPrice();

        stockService.updateAllStocks();

        List<StockData> afterUpdate = stockService.getStocksForPlanet("Mars");
        double updatedPrice = afterUpdate.get(0).getPrice();

        // Price should have changed after update
        assertNotEquals(originalPrice, updatedPrice);
    }

    @Test
    public void testGetAllStocksReturnsAll() {
        List<StockData> allStocks = stockService.getAllStocks();
        assertNotNull(allStocks);
        // There are 5 planets, each with 6 stocks => total 30 stocks
        assertEquals(30, allStocks.size());
    }

    @Test
    public void testGetPlanetsReturnsAllPlanetNames() {
        Set<String> planets = stockService.getPlanets();
        assertNotNull(planets);
        assertTrue(planets.contains("Earth"));
        assertTrue(planets.contains("Mars"));
        assertTrue(planets.contains("Venus"));
        assertTrue(planets.contains("Jupiter"));
        assertTrue(planets.contains("Saturn"));
    }
}
