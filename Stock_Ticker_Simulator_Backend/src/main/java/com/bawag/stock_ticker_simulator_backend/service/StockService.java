package com.bawag.stock_ticker_simulator_backend.service;

import com.bawag.stock_ticker_simulator_backend.model.Stock;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class StockService {
    private final List<Stock> stocks = new ArrayList<>();

    @EventListener(ApplicationReadyEvent.class)
    public void initializeAfterStartup() {
        initialize();
    }

    public void initialize() {
        //Lesen der Testdaten aus einem json file
        Gson gson = new Gson();
        try (FileReader reader = new FileReader("C:\\Users\\chris\\IdeaProjects\\bawag_stock_ticker\\Stock_Ticker_Simulator_Backend\\src\\main\\resources\\StockData.json")) {
            Type listType = new TypeToken<List<Stock>>(){}.getType();
            List<Stock> stocks = gson.fromJson(reader, listType);
            for (Stock stock : stocks) {
                addStock(stock);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void addStock(Stock stock) {
        stocks.add(stock);
    }
    public Optional<Stock> getStock(String symbol) {
        return stocks.stream()
                .filter(stock -> symbol.equals(stock.getSymbol()))
                .findFirst();
    }
    public List<Stock> getAllStocks() {
        return stocks;
    }

}