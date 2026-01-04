package com.solarsystem.servlet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.mockito.Mockito.*;
import com.solarsystem.service.StockService;
import java.util.Collections;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PlanetStockServletTest {

    private PlanetStockServlet servlet;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private StringWriter responseWriter;

    @BeforeEach
    public void setup() throws Exception {
        servlet = new PlanetStockServlet();

        // Initialize private fields via reflection
        java.lang.reflect.Field stockServiceField = PlanetStockServlet.class.getDeclaredField("stockService");
        stockServiceField.setAccessible(true);
        stockServiceField.set(servlet, mock(StockService.class));

        java.lang.reflect.Field objectMapperField = PlanetStockServlet.class.getDeclaredField("objectMapper");
        objectMapperField.setAccessible(true);
        objectMapperField.set(servlet, new com.fasterxml.jackson.databind.ObjectMapper());

        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        responseWriter = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(responseWriter));

        // Mock StockService behavior
        StockService mockedService = (StockService) stockServiceField.get(servlet);
        when(mockedService.getStocksForPlanet(anyString())).thenReturn(Collections.emptyList());
    }

    @Test
    public void testValidPlanetReturnsJsonResponse() throws Exception {
        when(request.getParameter("planet")).thenReturn("Earth");

        servlet.doGet(request, response);

        String result = responseWriter.toString();
        System.out.println(">>> Response (Valid Planet): " + result);

        assertTrue(result.contains("\"planet\":\"Earth\""));
        assertTrue(result.contains("\"stocks\":[]"));
        assertTrue(result.contains("\"sessionGuid\""));
    }

    @Test
    public void testInvalidPlanetReturnsJsonResponse() throws Exception {
        when(request.getParameter("planet")).thenReturn("InvalidPlanet");

        servlet.doGet(request, response);

        String result = responseWriter.toString();
        System.out.println(">>> Response (Invalid Planet): " + result);

        assertTrue(result.contains("\"planet\":\"InvalidPlanet\""));
        assertTrue(result.contains("\"stocks\":[]"));
        assertTrue(result.contains("\"sessionGuid\""));
    }

    @Test
    public void testNullPlanetTriggersBadRequest() throws Exception {
        when(request.getParameter("planet")).thenReturn(null);

        servlet.doGet(request, response);

        String result = responseWriter.toString();
        System.out.println(">>> Response (Null Planet): " + result);

        assertTrue(result.contains("Planet parameter is required"));
    }

    @Test
    public void testSessionGuidCookieCreatedWhenNoCookieExists() throws Exception {
        when(request.getParameter("planet")).thenReturn("Mars");
        when(request.getCookies()).thenReturn(null);

        servlet.doGet(request, response);

        ArgumentCaptor<Cookie> cookieCaptor = ArgumentCaptor.forClass(Cookie.class);
        verify(response).addCookie(cookieCaptor.capture());

        Cookie sessionCookie = cookieCaptor.getValue();
        assertEquals("SESSION_GUID", sessionCookie.getName());
        assertTrue(sessionCookie.getValue() != null && !sessionCookie.getValue().isEmpty());

        System.out.println(">>> Created SESSION_GUID: " + sessionCookie.getValue());
    }

    @Test
    public void testSessionGuidCookieReusedIfExists() throws Exception {
        Cookie existingCookie = new Cookie("SESSION_GUID", "existing-guid-123");
        when(request.getParameter("planet")).thenReturn("Venus");
        when(request.getCookies()).thenReturn(new Cookie[]{existingCookie});

        servlet.doGet(request, response);

        // Verify no new cookie was added
        verify(response, Mockito.never()).addCookie(Mockito.any());
        String result = responseWriter.toString();
        System.out.println(">>> Response with existing SESSION_GUID: " + result);

        assertTrue(result.contains("sessionGuid"));
        assertTrue(result.contains("existing-guid-123"));
    }
}
