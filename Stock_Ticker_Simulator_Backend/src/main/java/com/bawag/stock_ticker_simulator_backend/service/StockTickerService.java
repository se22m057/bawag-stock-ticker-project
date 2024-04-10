package com.bawag.stock_ticker_simulator_backend.service;

import com.bawag.stock_ticker_simulator_backend.model.StockTick;
import com.bawag.stock_ticker_simulator_backend.model.StockTickAverage;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
public class StockTickerService {

    private StockService stockService;
    private final ConcurrentMap<String, List<StockTick>> recentTicks = new ConcurrentHashMap<>();

    public StockTickerService(StockService stockService) {
        this.stockService = stockService;
    }


    public void addTick(StockTick tick) {
        recentTicks.computeIfAbsent(tick.getSymbol(), k -> new ArrayList<>()).add(tick);
        // Nur die letzten 30 ticks behalten
        if (recentTicks.get(tick.getSymbol()).size() > 30) {
            recentTicks.get(tick.getSymbol()).remove(0);
        }
    }

    public BigDecimal getLastTick(String symbol) {
        List<StockTick> ticks = new ArrayList<>(recentTicks.getOrDefault(symbol, Collections.emptyList()));
//test
        if (!ticks.isEmpty()) {
            StockTick lastTick = ticks.get(ticks.size() - 1);
            return lastTick.getPrice();
        } else {
            return stockService.getStock(symbol).get().getInitPrice();
        }
    }

    public StockTickAverage calculateAverage(String symbol) {
        //Liste der letzten Ticks einer Aktie anhand des symbols erstellen
        List<StockTick> ticks = new ArrayList<>(recentTicks.getOrDefault(symbol, Collections.emptyList()));
        BigDecimal averageLast7 = calculateAverageForLastNTicks(ticks, 7);
        BigDecimal averageLast30 = calculateAverageForLastNTicks(ticks, 30);

        return new StockTickAverage(symbol, averageLast7, averageLast30);
    }

    private BigDecimal calculateAverageForLastNTicks(List<StockTick> ticks, int n) {
        int size = ticks.size();
        int startIndex = Math.max(0, size - n);

        BigDecimal sum = ticks.stream()
                .skip(startIndex)
                .map(StockTick::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        int count = Math.min(size, n);
        return count > 0 ? sum.divide(BigDecimal.valueOf(count), 4, BigDecimal.ROUND_HALF_UP) : BigDecimal.ZERO;
    }



}
