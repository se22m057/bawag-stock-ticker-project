package com.bawag.stock_ticker_simulator_backend.model;

import java.math.BigDecimal;
import java.time.Instant;

public class Stock {
    private String symbol;

    private BigDecimal initPrice;

    public Stock(String _symbol, BigDecimal _initPrice) {
        symbol = _symbol;
        initPrice = _initPrice;
    }

    public String getSymbol() {
        return symbol;
    }

    public BigDecimal getInitPrice() {
        return initPrice;
    }

}
