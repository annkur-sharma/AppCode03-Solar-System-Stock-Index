<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.UUID" %>
<%
    String sessionGuid = null;
    Cookie[] cookies = request.getCookies();
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
        sessionCookie.setMaxAge(60 * 60 * 24);
        sessionCookie.setPath("/");
        response.addCookie(sessionCookie);
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Solar System Stock Index</title>
    <link rel="stylesheet" href="css/style.css">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Orbitron:wght@400;700;900&family=Roboto:wght@300;400;500&display=swap" rel="stylesheet">
</head>
<body>
    <div class="header">
        <h1>🚀 SOLAR SYSTEM STOCK INDEX 🚀</h1>
        <div class="header-info">
            <div class="datetime" id="datetime"></div>
            <div class="session-info">Session: <span id="session-guid"><%= sessionGuid %></span></div>
            <div class="app-ver">Application version: v1.0.0</div>
        </div>
    </div>

    <div class="container">
        <div class="planet-selector">
            <h2>Select Planet Index</h2>
            <div class="planet-buttons">
                <button class="planet-btn" data-planet="Earth">🌍 EARTH</button>
                <button class="planet-btn" data-planet="Mars">🔴 MARS</button>
                <button class="planet-btn" data-planet="Venus">🟡 VENUS</button>
                <button class="planet-btn" data-planet="Jupiter">🪐 JUPITER</button>
                <button class="planet-btn" data-planet="Saturn">🪐 SATURN</button>
            </div>
        </div>

        <div class="stock-display">
            <div class="selected-planet">
                <h2 id="selected-planet-name">Select a Planet</h2>
            </div>
            <div class="stocks-grid" id="stocks-grid">
                <div class="welcome-message">
                    <p>🌌 Welcome to the Solar System Stock Exchange! 🌌</p>
                    <p>Click on a planet above to view its celestial stock index</p>
                </div>
            </div>
        </div>
    </div>

    <div class="marquee-container">
        <div class="marquee" id="marquee">
            <span>Loading market data...</span>
        </div>
    </div>

    <script src="js/app.js"></script>
</body>
</html>