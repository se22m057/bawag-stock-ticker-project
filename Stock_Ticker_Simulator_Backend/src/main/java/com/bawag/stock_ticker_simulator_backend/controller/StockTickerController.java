package com.bawag.stock_ticker_simulator_backend.controller;

import com.bawag.stock_ticker_simulator_backend.model.StockTick;
import com.bawag.stock_ticker_simulator_backend.model.StockTickAverage;
import com.bawag.stock_ticker_simulator_backend.service.StockService;
import com.bawag.stock_ticker_simulator_backend.service.StockTickerService;
import com.bawag.stock_ticker_simulator_backend.service.WhiteNoiseGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/ticker")
public class StockTickerController {

    private final StockTickerService stockTickerService;


    private final WhiteNoiseGenerator whiteNoiseGenerator;

    public StockTickerController(StockTickerService stockTickerService, WhiteNoiseGenerator whiteNoiseGenerator, StockService stockService) {
        this.stockTickerService = stockTickerService;
        this.whiteNoiseGenerator = whiteNoiseGenerator;
    }

    @CrossOrigin(origins = "*")
    @GetMapping(value = "/stream/{symbols}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<StockTick> streamStockTicks(@PathVariable String[] symbols) {

        return whiteNoiseGenerator.generateStream(List.of(symbols))
                .doOnNext(stockTick -> stockTickerService.addTick(stockTick));
    }

}