package com.bawag.stock_ticker_simulator_backend.model;

import java.math.BigDecimal;

public class StockTickAverage {

    private String symbol;
    private BigDecimal averageLast7;
    private BigDecimal averageLast30;

    public StockTickAverage(String _symbol, BigDecimal _averageLast7, BigDecimal _averageLast30){
        symbol = _symbol;
        averageLast7 = _averageLast7;
        averageLast30 = _averageLast30;
    }

    public String getSymbol() {
        return symbol;
    }

    public BigDecimal getAverageLast7() {
        return averageLast7;
    }

    public BigDecimal getAverageLast30() {
        return averageLast30;
    }
}
