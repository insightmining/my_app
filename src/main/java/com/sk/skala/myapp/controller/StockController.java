// StockController.java
package com.sk.skala.myapp.controller;

import com.sk.skala.myapp.model.Stock;
import com.sk.skala.myapp.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stocks")
public class StockController {
    private final StockService stockService;

    @Autowired
    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @GetMapping
    public ResponseEntity<List<Stock>> getAllStocks() {
        return ResponseEntity.ok(stockService.getAllStocks());
    }

    @GetMapping("/{name}")
    public ResponseEntity<Stock> getStockByName(@PathVariable String name) {
        Stock stock = stockService.getStockByName(name);
        if (stock != null) {
            return ResponseEntity.ok(stock);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Stock> createStock(@RequestBody Stock stock) {
        return ResponseEntity.status(HttpStatus.CREATED).body(stockService.createStock(stock));
    }

    @PutMapping("/{name}")
    public ResponseEntity<Stock> updateStock(@PathVariable String name, @RequestBody Stock stock) {
        Stock existingStock = stockService.getStockByName(name);
        if (existingStock != null) {
            stock.setStockName(name);
            return ResponseEntity.ok(stockService.updateStock(stock));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{name}")
    public ResponseEntity<Void> deleteStock(@PathVariable String name) {
        Stock existingStock = stockService.getStockByName(name);
        if (existingStock != null) {
            stockService.deleteStock(name);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
