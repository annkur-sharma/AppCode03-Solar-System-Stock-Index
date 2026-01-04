package com.solarsystem.servlet;

import com.solarsystem.model.StockData;
import com.solarsystem.service.StockService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class MarqueeServletTest {

    private MarqueeServlet servlet;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private StringWriter responseWriter;
    private StockService mockedStockService;

    @BeforeEach
    public void setup() throws Exception {
        servlet = new MarqueeServlet();

        // Mock StockService and inject via reflection
        mockedStockService = mock(StockService.class);

        java.lang.reflect.Field stockServiceField = MarqueeServlet.class.getDeclaredField("stockService");
        stockServiceField.setAccessible(true);
        stockServiceField.set(servlet, mockedStockService);

        // Inject ObjectMapper
        java.lang.reflect.Field objectMapperField = MarqueeServlet.class.getDeclaredField("objectMapper");
        objectMapperField.setAccessible(true);
        objectMapperField.set(servlet, new ObjectMapper());

        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);

        responseWriter = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(responseWriter));

        // Default mocked behavior
        when(mockedStockService.getAllStocks()).thenReturn(Collections.emptyList());
    }

    @Test
    public void testDoGetReturnsAllStocksAsJson() throws Exception {
        List<StockData> sampleStocks = Collections.singletonList(
                new StockData("Earth", "TechSector", 100.0)
        );
        when(mockedStockService.getAllStocks()).thenReturn(sampleStocks);

        servlet.doGet(request, response);

        String result = responseWriter.toString();
        System.out.println(">>> Response: " + result);

        assertTrue(result.contains("\"symbol\":\"Earth\""));
        assertTrue(result.contains("\"name\":\"TechSector\""));
        assertTrue(result.contains("\"price\":100.0"));
        verify(mockedStockService).updateAllStocks();
    }

    @Test
    public void testDoGetReturnsEmptyListIfNoStocks() throws Exception {
        when(mockedStockService.getAllStocks()).thenReturn(Collections.emptyList());

        servlet.doGet(request, response);

        String result = responseWriter.toString();
        System.out.println(">>> Response (Empty): " + result);

        assertEquals("[]", result.trim());
        verify(mockedStockService).updateAllStocks();
    }

    @Test
    public void testDoGetHandlesExceptionGracefully() throws Exception {
        doThrow(new RuntimeException("DB error")).when(mockedStockService).updateAllStocks();

        servlet.doGet(request, response);

        String result = responseWriter.toString();
        System.out.println(">>> Response (Error): " + result);

        verify(response).setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        assertTrue(result.contains("Internal server error"));
    }

    // NEW TEST: multiple StockData entries
    @Test
    public void testDoGetWithMultipleStocks() throws Exception {
        List<StockData> multipleStocks = Arrays.asList(
                new StockData("Earth", "TechSector", 100.0),
                new StockData("Mars", "MiningSector", 200.5),
                new StockData("Venus", "EnergySector", 150.75)
        );
        when(mockedStockService.getAllStocks()).thenReturn(multipleStocks);

        servlet.doGet(request, response);

        String result = responseWriter.toString();
        System.out.println(">>> Response (Multiple Stocks): " + result);

        // Assertions for all three entries
        assertTrue(result.contains("\"symbol\":\"Earth\""));
        assertTrue(result.contains("\"symbol\":\"Mars\""));
        assertTrue(result.contains("\"symbol\":\"Venus\""));

        assertTrue(result.contains("\"name\":\"TechSector\""));
        assertTrue(result.contains("\"name\":\"MiningSector\""));
        assertTrue(result.contains("\"name\":\"EnergySector\""));

        assertTrue(result.contains("\"price\":100.0"));
        assertTrue(result.contains("\"price\":200.5"));
        assertTrue(result.contains("\"price\":150.75"));

        verify(mockedStockService).updateAllStocks();
    }
}
