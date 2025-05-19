// StockService.java
package com.sk.skala.myapp.service;

import com.sk.skala.myapp.model.Stock;
import com.sk.skala.myapp.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StockService {
    private final StockRepository stockRepository;

    @Autowired
    public StockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
        // Initialize default stocks if none exist
        if (stockRepository.count() == 0) {
            initializeDefaultStocks();
        }
    }

    private void initializeDefaultStocks() {
        List<Stock> defaultStocks = List.of(
                new Stock("TechCorp", 100),
                new Stock("GreenEnergy", 80),
                new Stock("HealthPlus", 120),
                new Stock("samsung", 300)
        );
        stockRepository.saveAll(defaultStocks);
    }

    @Transactional(readOnly = true)
    public List<Stock> getAllStocks() {
        return stockRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Stock getStockByName(String name) {
        return stockRepository.findById(name).orElse(null);
    }

    @Transactional(readOnly = true)
    public Stock getStockByIndex(int index) {
        List<Stock> stocks = stockRepository.findAll();
        if (index >= 0 && index < stocks.size()) {
            return stocks.get(index);
        }
        return null;
    }

    @Transactional
    public Stock createStock(Stock stock) {
        return stockRepository.save(stock);
    }

    @Transactional
    public Stock updateStock(Stock stock) {
        return stockRepository.save(stock);
    }

    @Transactional
    public void deleteStock(String name) {
        stockRepository.deleteById(name);
    }

    @Transactional(readOnly = true)
    public String getStockListForMenu() {
        List<Stock> stocks = stockRepository.findAll();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < stocks.size(); i++) {
            sb.append(i + 1);
            sb.append(". ");
            sb.append(stocks.get(i).toString());
            sb.append(System.lineSeparator());
        }
        return sb.toString();
    }
}
