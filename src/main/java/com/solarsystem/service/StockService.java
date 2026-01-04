package com.solarsystem.service;

import com.solarsystem.model.StockData;
import java.util.*;

public class StockService {
    private static StockService instance;
    private Map<String, List<StockData>> planetStocks;
    private Random random = new Random();

    private StockService() {
        initializeStocks();
    }

    public static synchronized StockService getInstance() {
        if (instance == null) {
            instance = new StockService();
        }
        return instance;
    }

    private void initializeStocks() {
        planetStocks = new HashMap<>();
        
        // Earth stocks
        List<StockData> earthStocks = Arrays.asList(
            new StockData("LUNA", "Luna", 1250.50 + random.nextDouble() * 100),
            new StockData("NEO", "Near-Earth Objects", 890.25 + random.nextDouble() * 100),
            new StockData("AST", "Earth Asteroids", 654.30 + random.nextDouble() * 100),
            new StockData("COM", "Comets", 784.10 + random.nextDouble() * 100),
            new StockData("ASSTRT", "Stars", 587.30 + random.nextDouble() * 100),
            new StockData("MET", "Meteorites", 445.80 + random.nextDouble() * 100)
        );
        planetStocks.put("Earth", earthStocks);

        // Mars stocks
        List<StockData> marsStocks = Arrays.asList(
            new StockData("PHOB", "Phobos", 2150.75 + random.nextDouble() * 100),
            new StockData("DEIM", "Deimos", 1890.40 + random.nextDouble() * 100),
            new StockData("TROJ", "Mars Trojans", 1230.60 + random.nextDouble() * 100),
            new StockData("OLYM", "Olympus Mons", 741.20 + random.nextDouble() * 100),
            new StockData("MARI", "Valles Marineris", 3642.90 + random.nextDouble() * 100),
            new StockData("DUST", "Martian Dust", 567.90 + random.nextDouble() * 100)
        );
        planetStocks.put("Mars", marsStocks);

        // Venus stocks
        List<StockData> venusStocks = Arrays.asList(
            new StockData("EXPR", "Venus Express", 3250.20 + random.nextDouble() * 100),
            new StockData("VENE", "Venera Mission", 2890.85 + random.nextDouble() * 100),
            new StockData("MAGE", "Magellan Probe", 2145.35 + random.nextDouble() * 100),
            new StockData("CLOU", "Venus Clouds", 1678.45 + random.nextDouble() * 100),
            new StockData("VEN7", "Venera 7", 753.55 + random.nextDouble() * 100),
            new StockData("MAR2", "Mariner 2", 986.75 + random.nextDouble() * 100)
        );
        planetStocks.put("Venus", venusStocks);

        // Jupiter stocks
        List<StockData> jupiterStocks = Arrays.asList(
            new StockData("EUR", "Europa", 4250.90 + random.nextDouble() * 100),
            new StockData("GANY", "Ganymede", 3890.60 + random.nextDouble() * 100),
            new StockData("IO", "Io", 3450.25 + random.nextDouble() * 100),
            new StockData("CALL", "Callisto", 3120.75 + random.nextDouble() * 100),
            new StockData("AMAL", "Amalthea", 3687.15 + random.nextDouble() * 100),
            new StockData("SPOT", "Great Red Spot", 2890.30 + random.nextDouble() * 100)
        );
        planetStocks.put("Jupiter", jupiterStocks);

        // Saturn stocks
        List<StockData> saturnStocks = Arrays.asList(
            new StockData("TIT", "Titan", 5250.40 + random.nextDouble() * 100),
            new StockData("ENCE", "Enceladus", 4590.80 + random.nextDouble() * 100),
            new StockData("RHEA", "Rhea", 3890.65 + random.nextDouble() * 100),
            new StockData("MIMA", "Mimas", 3245.20 + random.nextDouble() * 100),
            new StockData("DIO", "Dione", 4267.80 + random.nextDouble() * 100),
            new StockData("RING", "Saturn Rings", 2890.95 + random.nextDouble() * 100)
        );
        planetStocks.put("Saturn", saturnStocks);
    }

    public List<StockData> getStocksForPlanet(String planet) {
        return planetStocks.getOrDefault(planet, new ArrayList<>());
    }

    public void updateAllStocks() {
        for (List<StockData> stocks : planetStocks.values()) {
            for (StockData stock : stocks) {
                stock.updatePrice();
            }
        }
    }

    public List<StockData> getAllStocks() {
        List<StockData> allStocks = new ArrayList<>();
        for (List<StockData> stocks : planetStocks.values()) {
            allStocks.addAll(stocks);
        }
        return allStocks;
    }

    public Set<String> getPlanets() {
        return planetStocks.keySet();
    }
}