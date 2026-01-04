package com.solarsystem.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.solarsystem.model.StockData;
import com.solarsystem.service.StockService;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

public class MarqueeServlet extends HttpServlet {
    private StockService stockService;
    private ObjectMapper objectMapper;

    @Override
    public void init() throws ServletException {
        stockService = StockService.getInstance();
        objectMapper = new ObjectMapper();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Cache-Control", "no-cache");
        
        try {
            // Update stocks before returning
            stockService.updateAllStocks();
            
            List<StockData> allStocks = stockService.getAllStocks();
            String jsonResponse = objectMapper.writeValueAsString(allStocks);
            response.getWriter().write(jsonResponse);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"Internal server error\"}");
        }
    }
}