package com.bawag.stock_ticker_simulator_backend.service;

import com.bawag.stock_ticker_simulator_backend.model.StockTick;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class WhiteNoiseGenerator {

    private final Random random = new Random();
    private final StockService stockService;
    private  final StockTickerService stockTickerService;

    public WhiteNoiseGenerator(StockService stockService, StockTickerService stockTickerService) {
        this.stockService = stockService;
        this.stockTickerService = stockTickerService;
    }

    public Flux<StockTick> generateStream(List<String> symbols) {
        //Für jede Aktie wird ein Flux generiert
        //Diese Streams (Fluxes) werden mit flatMap in einen Flux gemerged, sodass nur noch ein Datenstrom existiert
        return Flux.fromIterable(symbols)
                .flatMap(symbol ->
                        stockService.getStock(symbol)
                                .map(stock -> Optional.ofNullable(stock.getInitPrice()))
                                .map(price -> generateStreamForSymbol(symbol, price.get()))
                                .orElseGet(Flux::empty) // Fallback, wenn kein Wert vorhanden ist
                );
    }

    private Flux<StockTick> generateStreamForSymbol(String symbol, BigDecimal initialPrice) {
        //Flux erstellen der für eine Aktie die Ticks generiert
        return Flux.interval(Duration.ofSeconds(random.nextInt(3,10)))
                .map(i -> new StockTick(symbol, fluctuatePrice(stockTickerService.getLastTick(symbol)), Instant.now(), stockTickerService.calculateAverage(symbol)));
    }

    private BigDecimal fluctuatePrice(BigDecimal lastPrice) {
        //Aktualisierten Preis berechnen
        double change = lastPrice.doubleValue() * 0.015 * (random.nextDouble() - 0.5);
        return lastPrice.add(BigDecimal.valueOf(change));
    }
}