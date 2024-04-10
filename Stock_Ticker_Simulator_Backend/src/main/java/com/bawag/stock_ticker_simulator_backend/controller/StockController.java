package com.bawag.stock_ticker_simulator_backend.controller;

import com.bawag.stock_ticker_simulator_backend.model.Stock;
import com.bawag.stock_ticker_simulator_backend.service.StockService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/stock")
public class StockController {
    private final StockService stockService;



    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @CrossOrigin(origins = "*")
    @GetMapping(value = "/{symbol}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Optional<Stock> getStockBySymbol(@PathVariable String symbol) {
        return stockService.getStock(symbol);
    }

    @CrossOrigin(origins = "*")
    @GetMapping(value= "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Stock> getAllStocks() {
        return stockService.getAllStocks();
    }
}
