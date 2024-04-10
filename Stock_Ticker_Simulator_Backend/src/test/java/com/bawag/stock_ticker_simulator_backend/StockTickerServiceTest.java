package com.bawag.stock_ticker_simulator_backend;


import com.bawag.stock_ticker_simulator_backend.model.Stock;
import com.bawag.stock_ticker_simulator_backend.model.StockTick;
import com.bawag.stock_ticker_simulator_backend.model.StockTickAverage;
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
import java.time.Instant;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
public class StockTickerServiceTest {
    @Mock
    private StockService stockService;

    @InjectMocks
    private StockTickerService stockTickerService;

    @BeforeEach
    void setUp() {
        // Optional: Setup, das vor jedem Testfall ausgeführt wird.
    }


    @Test
    void testGetLastTickWhenTicksExist() {
        String symbol = "AAPL";
        StockTick tick1 = new StockTick("AAPL", new BigDecimal("150"), Instant.now(), new StockTickAverage("AAPL",new BigDecimal("0"),new BigDecimal("0")));
        stockTickerService.addTick(tick1);

        assertEquals(new BigDecimal("150"), stockTickerService.getLastTick(symbol));
    }

    @Test
    void testCalculateAverage() {
        String symbol = "AAPL";
        for (int i = 1; i <= 10; i++) {
            stockTickerService.addTick(new StockTick(symbol, BigDecimal.valueOf(i * 10), Instant.now(), new StockTickAverage("AAPL",new BigDecimal("0"),new BigDecimal("0"))));
        }

        StockTickAverage average = stockTickerService.calculateAverage(symbol);

        assertEquals(new BigDecimal("70.0000"), average.getAverageLast7());
        assertEquals(new BigDecimal("55.0000"), average.getAverageLast30());
    }
}
