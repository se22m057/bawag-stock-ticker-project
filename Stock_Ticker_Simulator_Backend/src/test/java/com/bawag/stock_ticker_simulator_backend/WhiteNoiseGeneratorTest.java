package com.bawag.stock_ticker_simulator_backend;

import com.bawag.stock_ticker_simulator_backend.model.Stock;
import com.bawag.stock_ticker_simulator_backend.model.StockTick;
import com.bawag.stock_ticker_simulator_backend.service.StockService;
import com.bawag.stock_ticker_simulator_backend.service.StockTickerService;
import com.bawag.stock_ticker_simulator_backend.service.WhiteNoiseGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WhiteNoiseGeneratorTest {
    @Mock
    private StockService stockService;

    @Mock
    private StockTickerService stockTickerService;

    @InjectMocks
    private WhiteNoiseGenerator whiteNoiseGenerator;

    @BeforeEach
    void setUp() {
    }

    @Test
    void testStreamGenerierungMitGültigenSymbolen() {
        var symbol = "AAPL";
        var initialPrice = new BigDecimal("150");
        when(stockService.getStock(symbol)).thenReturn(Optional.of(new Stock(symbol, initialPrice))); // Stock muss deiner Implementierung entsprechen
        when(stockTickerService.getLastTick(symbol)).thenReturn(new BigDecimal("150"));

        Flux<StockTick> result = whiteNoiseGenerator.generateStream(Arrays.asList(symbol));

        StepVerifier.create(result)
                .expectNextCount(1) // erwartete Anzahl der StockTick Elemente = 1
                .thenCancel()
                .verify();
    }

    @Test
    void testMitLeererSymbolListe() {
        Flux<StockTick> result = whiteNoiseGenerator.generateStream(Arrays.asList());

        StepVerifier.create(result)
                .expectNextCount(0) // Erwartet, dass kein StockTick generiert wird
                .verifyComplete();
    }

    @Test
    void testMitUngültigenSymbolen() {
        var invalidSymbol = "INVALID";
        when(stockService.getStock(invalidSymbol)).thenReturn(Optional.empty());

        Flux<StockTick> result = whiteNoiseGenerator.generateStream(Arrays.asList(invalidSymbol));

        StepVerifier.create(result)
                .expectNextCount(0) // Erwartet, dass kein StockTick generiert wird
                .verifyComplete();
    }

    @Test
    void testStreamGenerierungMitVerändertemPreis() {
        var symbol = "AAPL";
        var initialPrice = new BigDecimal("150");
        when(stockService.getStock(symbol)).thenReturn(Optional.of(new Stock(symbol, initialPrice))); // Stock muss deiner Implementierung entsprechen
        when(stockTickerService.getLastTick(symbol)).thenReturn(initialPrice);

        Flux<StockTick> result = whiteNoiseGenerator.generateStream(Arrays.asList(symbol));

        StepVerifier.create(result)
                .expectNextMatches(stockTick -> !stockTick.getPrice().equals(initialPrice))
                .thenCancel() // Beendet das Abonnement nach der Überprüfung des ersten Elements
                .verify();
    }
}
