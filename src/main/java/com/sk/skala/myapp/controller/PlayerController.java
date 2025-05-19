// PlayerController.java
package com.sk.skala.myapp.controller;

import com.sk.skala.myapp.model.Player;
import com.sk.skala.myapp.model.PlayerStock;
import com.sk.skala.myapp.model.Stock;
import com.sk.skala.myapp.model.StockRequest;
import com.sk.skala.myapp.repository.StockRepository;
import com.sk.skala.myapp.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/players")
public class PlayerController {
    private final PlayerService playerService;
    private final StockRepository stockRepository;

    @Autowired
    public PlayerController(PlayerService playerService, StockRepository stockRepository) {
        this.playerService = playerService;
        this.stockRepository = stockRepository;
    }

    @GetMapping
    public ResponseEntity<List<Player>> getAllPlayers() {
        return ResponseEntity.ok(playerService.getAllPlayers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Player> getPlayerById(@PathVariable String id) {
        Player player = playerService.getPlayerById(id);
        if (player != null) {
            return ResponseEntity.ok(player);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Player> createPlayer(@RequestBody Player player) {
        return ResponseEntity.status(HttpStatus.CREATED).body(playerService.createPlayer(player));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Player> updatePlayer(@PathVariable String id, @RequestBody Player player) {
        Player updatedPlayer = playerService.updatePlayer(id, player);
        if (updatedPlayer != null) {
            return ResponseEntity.ok(updatedPlayer);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlayer(@PathVariable String id) {
        boolean deleted = playerService.deletePlayer(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/stocks")
    public ResponseEntity<List<PlayerStock>> getPlayerStocks(@PathVariable String id) {
        List<PlayerStock> playerStocks = playerService.getPlayerStocks(id);
        return ResponseEntity.ok(playerStocks);
    }

    @PostMapping("/{id}/stocks")
    public ResponseEntity<Player> addStockToPlayer(
            @PathVariable String id,
            @RequestBody StockRequest stockRequest
    ) {
        String stockName = stockRequest.getStockName();
        int quantity = stockRequest.getQuantity();

        Player player = playerService.addStockToPlayer(id, stockName, quantity);
        if (player != null) {
            return ResponseEntity.ok(player);
        }
        return ResponseEntity.notFound().build();
    }


    @PutMapping("/{id}/stocks")
    public ResponseEntity<Player> updatePlayerStock(
            @PathVariable String id,
            @RequestBody StockRequest stockRequest
    ) {

        Stock stock = stockRepository.findById(stockRequest.getStockName()).orElse(null);

        if (stock == null) {
            return ResponseEntity.notFound().build();
        }

        Player player = playerService.updatePlayerStock(id, stockRequest.getStockName(), stockRequest.getQuantity(), stock.getStockPrice());
        if (player != null) {
            return ResponseEntity.ok(player);
        }
        return ResponseEntity.notFound().build();
    }
}
