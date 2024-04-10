package com.bawag.stock_ticker_simulator_backend.model;

import java.math.BigDecimal;
import java.time.Instant;

public class StockTick {
    private String symbol;

    private BigDecimal price;
    private Instant timestamp;

    private StockTickAverage stockTickAverage;

    public StockTick(String _symbol, BigDecimal _price, Instant _timestamp, StockTickAverage _stockTickAverage) {
        symbol = _symbol;
        price = _price;
        timestamp = _timestamp;
        stockTickAverage = _stockTickAverage;
    }

    public String getSymbol() {
        return symbol;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public StockTickAverage getStockTickAverage(){return stockTickAverage;}

    // Constructors, getters, and setters
}