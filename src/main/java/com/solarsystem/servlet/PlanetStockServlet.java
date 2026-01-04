package com.solarsystem.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.solarsystem.model.StockData;
import com.solarsystem.service.StockService;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class PlanetStockServlet extends HttpServlet {
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
        
        // Update session GUID
        String sessionGuid = getOrCreateSessionGuid(request, response);
        
        String planet = request.getParameter("planet");
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Cache-Control", "no-cache");
        
        try {
            if (planet != null && !planet.trim().isEmpty()) {
                // Update stocks before returning
                stockService.updateAllStocks();
                
                List<StockData> stocks = stockService.getStocksForPlanet(planet);
                
                // Create response object
                ResponseData responseData = new ResponseData();
                responseData.planet = planet;
                responseData.stocks = stocks;
                responseData.sessionGuid = sessionGuid;
                responseData.timestamp = System.currentTimeMillis();
                
                String jsonResponse = objectMapper.writeValueAsString(responseData);
                response.getWriter().write(jsonResponse);
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"error\": \"Planet parameter is required\"}");
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"Internal server error\"}");
        }
    }

    private String getOrCreateSessionGuid(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        String sessionGuid = null;
        
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("SESSION_GUID".equals(cookie.getName())) {
                    sessionGuid = cookie.getValue();
                    break;
                }
            }
        }
        
        if (sessionGuid == null) {
            sessionGuid = UUID.randomUUID().toString();
            Cookie sessionCookie = new Cookie("SESSION_GUID", sessionGuid);
            sessionCookie.setMaxAge(60 * 60 * 24); // 1 day
            sessionCookie.setPath("/");
            response.addCookie(sessionCookie);
        }
        
        return sessionGuid;
    }

    private static class ResponseData {
        public String planet;
        public List<StockData> stocks;
        public String sessionGuid;
        public long timestamp;
    }
}