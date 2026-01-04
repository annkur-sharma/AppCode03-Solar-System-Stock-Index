class SolarSystemStockApp {
    constructor() {
        this.currentPlanet = null;
        this.refreshInterval = null;
        this.marqueeInterval = null;
        this.sessionGuid = this.getSessionGuid();
        
        this.initEventListeners();
        this.updateDateTime();
        this.startMarquee();
        
        // Update date/time every second
        setInterval(() => this.updateDateTime(), 1000);
    }

    getSessionGuid() {
        const cookies = document.cookie.split(';');
        for (let cookie of cookies) {
            const [name, value] = cookie.trim().split('=');
            if (name === 'SESSION_GUID') {
                return value;
            }
        }
        return 'N/A';
    }

    initEventListeners() {
        const planetBtns = document.querySelectorAll('.planet-btn');
        planetBtns.forEach(btn => {
            btn.addEventListener('click', (e) => {
                const planet = e.target.dataset.planet;
                this.selectPlanet(planet);
            });
        });
    }

    selectPlanet(planet) {
        // Update active button
        document.querySelectorAll('.planet-btn').forEach(btn => {
            btn.classList.remove('active');
        });
        document.querySelector(`[data-planet="${planet}"]`).classList.add('active');

        this.currentPlanet = planet;
        document.getElementById('selected-planet-name').textContent = `${planet} Stock Index`;

        // Clear existing refresh interval
        if (this.refreshInterval) {
            clearInterval(this.refreshInterval);
        }

        // Load stocks immediately
        this.loadPlanetStocks(planet);

        // Set up auto-refresh every 3 seconds
        this.refreshInterval = setInterval(() => {
            this.loadPlanetStocks(planet);
        }, 3000);
    }

    async loadPlanetStocks(planet) {
        try {
            const response = await fetch(`api/stocks?planet=${planet}`);
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }

            const data = await response.json();
            this.renderStocks(data.stocks);
            
            // Update session GUID if provided
            if (data.sessionGuid) {
                document.getElementById('session-guid').textContent = data.sessionGuid;
            }
        } catch (error) {
            console.error('Error loading stocks:', error);
            this.showError('Failed to load stock data');
        }
    }

    renderStocks(stocks) {
        const stocksGrid = document.getElementById('stocks-grid');
        
        if (!stocks || stocks.length === 0) {
            stocksGrid.innerHTML = '<div class="welcome-message"><p>No stocks available for this planet</p></div>';
            return;
        }

        stocksGrid.innerHTML = stocks.map(stock => `
            <div class="stock-card ${stock.trend}" data-symbol="${stock.symbol}">
                <div class="stock-symbol">${stock.symbol}</div>
                <div class="stock-name">${stock.name}</div>
                <div class="stock-price">$${stock.price.toFixed(2)}</div>
                <div class="stock-change ${stock.trend}">
                    ${stock.change >= 0 ? '+' : ''}${stock.change.toFixed(2)} 
                    (${stock.changePercent >= 0 ? '+' : ''}${stock.changePercent.toFixed(2)}%)
                </div>
            </div>
        `).join('');

        // Add animation for updated stocks
        this.animateStockUpdates();
    }

    animateStockUpdates() {
        const stockCards = document.querySelectorAll('.stock-card');
        stockCards.forEach((card, index) => {
            setTimeout(() => {
                card.style.opacity = '0';
                card.style.transform = 'scale(0.95)';
                
                setTimeout(() => {
                    card.style.transition = 'all 0.3s ease';
                    card.style.opacity = '1';
                    card.style.transform = 'scale(1)';
                }, 50);
            }, index * 50);
        });
    }

    async startMarquee() {
        // Load initial marquee data
        await this.updateMarquee();
        
        // Update marquee every 5 seconds
        this.marqueeInterval = setInterval(() => {
            this.updateMarquee();
        }, 5000);
    }

    async updateMarquee() {
        try {
            const response = await fetch('api/marquee');
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }

            const stocks = await response.json();
            this.renderMarquee(stocks);
        } catch (error) {
            console.error('Error loading marquee data:', error);
        }
    }

    renderMarquee(stocks) {
        const marquee = document.getElementById('marquee');
        
        if (!stocks || stocks.length === 0) {
            marquee.innerHTML = '<span>No market data available</span>';
            return;
        }

        const marqueeItems = stocks.map(stock => `
            <span class="marquee-item">
                <span class="marquee-symbol">${stock.symbol}</span>
                <span class="marquee-price">$${stock.price.toFixed(2)}</span>
                <span class="marquee-change ${stock.trend}">
                    ${stock.change >= 0 ? '+' : ''}${stock.change.toFixed(2)}
                    (${stock.changePercent >= 0 ? '+' : ''}${stock.changePercent.toFixed(2)}%)
                </span>
            </span>
        `).join('');

        marquee.innerHTML = marqueeItems;
    }

    updateDateTime() {
        const now = new Date();
        const options = {
            year: 'numeric',
            month: '2-digit',
            day: '2-digit',
            hour: '2-digit',
            minute: '2-digit',
            second: '2-digit',
            hour12: false
        };
        
        const formattedDate = now.toLocaleDateString('en-US', options);
        document.getElementById('datetime').textContent = formattedDate.replace(',', '');
    }

    showError(message) {
        const stocksGrid = document.getElementById('stocks-grid');
        stocksGrid.innerHTML = `
            <div class="welcome-message">
                <p style="color: #ff4757;">⚠️ Error</p>
                <p>${message}</p>
            </div>
        `;
    }
}

// Initialize the app when DOM is loaded
document.addEventListener('DOMContentLoaded', () => {
    new SolarSystemStockApp();
});