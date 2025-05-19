// PlayerService.java
package com.sk.skala.myapp.service;

import com.sk.skala.myapp.model.Player;
import com.sk.skala.myapp.model.PlayerStock;
import com.sk.skala.myapp.model.Stock;
import com.sk.skala.myapp.repository.PlayerRepository;
import com.sk.skala.myapp.repository.PlayerStockRepository;
import com.sk.skala.myapp.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PlayerService {
    private final PlayerRepository playerRepository;
    private final StockRepository stockRepository;
    private final PlayerStockRepository playerStockRepository;

    @Autowired
    public PlayerService(
            PlayerRepository playerRepository,
            StockRepository stockRepository,
            PlayerStockRepository playerStockRepository
    ) {
        this.playerRepository = playerRepository;
        this.stockRepository = stockRepository;
        this.playerStockRepository = playerStockRepository;
    }

    @Transactional(readOnly = true)
    public List<Player> getAllPlayers() {
        return playerRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Player getPlayerById(String playerId) {
        return playerRepository.findById(playerId).orElse(null);
    }

    @Transactional
    public Player createPlayer(Player player) {
        return playerRepository.save(player);
    }

    @Transactional
    public Player updatePlayer(String playerId, Player playerDetails) {
        return playerRepository.findById(playerId)
                .map(existingPlayer -> {
                    existingPlayer.setPlayerMoney(playerDetails.getPlayerMoney());
                    return playerRepository.save(existingPlayer);
                })
                .orElse(null);
    }

    @Transactional
    public boolean deletePlayer(String playerId) {
        if (playerRepository.existsById(playerId)) {
            // 관련된 PlayerStock 먼저 삭제
            List<PlayerStock> playerStocks = playerStockRepository.findByPlayerPlayerId(playerId);
            playerStockRepository.deleteAll(playerStocks);

            // Player 삭제
            playerRepository.deleteById(playerId);
            return true;
        }
        return false;
    }

    @Transactional(readOnly = true)
    public List<PlayerStock> getPlayerStocks(String playerId) {
        return playerStockRepository.findByPlayerPlayerId(playerId);
    }

    @Transactional(readOnly = true)
    public PlayerStock getPlayerStock(String playerId, int stockIndex) {
        List<PlayerStock> playerStocks = playerStockRepository.findByPlayerPlayerId(playerId);
        if (stockIndex >= 0 && stockIndex < playerStocks.size()) {
            return playerStocks.get(stockIndex);
        }
        return null;
    }

    @Transactional
    public Player addStockToPlayer(String playerId, String stockName, int quantity) {
        Player player = playerRepository.findById(playerId).orElse(null);
        Stock stock = stockRepository.findById(stockName).orElse(null);

        if (player != null && stock != null) {
            Optional<PlayerStock> existingPlayerStockOpt =
                    playerStockRepository.findByPlayerPlayerIdAndStockName(playerId, stockName);

            if (existingPlayerStockOpt.isPresent()) {
                // 이미 보유한 주식이 있는 경우
                PlayerStock existingPlayerStock = existingPlayerStockOpt.get();
                existingPlayerStock.setStockPrice(stock.getStockPrice());
                existingPlayerStock.setStockQuantity(existingPlayerStock.getStockQuantity() + quantity);
                playerStockRepository.save(existingPlayerStock);
            } else {
                // 새로운 주식 추가
                PlayerStock newPlayerStock = new PlayerStock(stock, quantity);
                newPlayerStock.setPlayer(player);
                playerStockRepository.save(newPlayerStock);
            }

            return player;
        }
        return null;
    }

    @Transactional
    public Player updatePlayerStock(String playerId, String stockName, int quantity, int price) {
        Player player = playerRepository.findById(playerId).orElse(null);

        if (player != null) {
            Optional<PlayerStock> existingPlayerStockOpt =
                    playerStockRepository.findByPlayerPlayerIdAndStockName(playerId, stockName);

            if (existingPlayerStockOpt.isPresent()) {
                PlayerStock existingPlayerStock = existingPlayerStockOpt.get();
                existingPlayerStock.setStockPrice(price);

                if (quantity <= 0) {
                    // 소유 주식 개수가 0이면 삭제
                    playerStockRepository.delete(existingPlayerStock);
                } else {
                    existingPlayerStock.setStockQuantity(quantity);
                    playerStockRepository.save(existingPlayerStock);
                }

                return player;
            }
        }
        return null;
    }

    // 이전 코드와의 호환성을 위한 메서드
    @Transactional
    public Player addStockToPlayer(String playerId, PlayerStock playerStock) {
        Player player = playerRepository.findById(playerId).orElse(null);

        if (player != null) {
            Optional<PlayerStock> existingPlayerStockOpt =
                    playerStockRepository.findByPlayerPlayerIdAndStockName(playerId, playerStock.getStockName());

            if (existingPlayerStockOpt.isPresent()) {
                PlayerStock existingPlayerStock = existingPlayerStockOpt.get();
                existingPlayerStock.setStockPrice(playerStock.getStockPrice());
                existingPlayerStock.setStockQuantity(existingPlayerStock.getStockQuantity() + playerStock.getStockQuantity());
                playerStockRepository.save(existingPlayerStock);
            } else {
                playerStock.setPlayer(player);
                playerStockRepository.save(playerStock);
            }

            return player;
        }
        return null;
    }

    // 이전 코드와의 호환성을 위한 메서드
    @Transactional
    public Player updatePlayerStock(String playerId, PlayerStock playerStock) {
        Player player = playerRepository.findById(playerId).orElse(null);

        if (player != null) {
            Optional<PlayerStock> existingPlayerStockOpt =
                    playerStockRepository.findByPlayerPlayerIdAndStockName(playerId, playerStock.getStockName());

            if (existingPlayerStockOpt.isPresent()) {
                PlayerStock existingPlayerStock = existingPlayerStockOpt.get();
                existingPlayerStock.setStockPrice(playerStock.getStockPrice());

                if (playerStock.getStockQuantity() <= 0) {
                    playerStockRepository.delete(existingPlayerStock);
                } else {
                    existingPlayerStock.setStockQuantity(playerStock.getStockQuantity());
                    playerStockRepository.save(existingPlayerStock);
                }

                return player;
            }
        }
        return null;
    }

    // Player가 가진 Stock 목록을 출력하기 위한 메서드
    @Transactional(readOnly = true)
    public String getPlayerStocksForMenu(String playerId) {
        List<PlayerStock> playerStocks = playerStockRepository.findByPlayerPlayerId(playerId);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < playerStocks.size(); i++) {
            sb.append(i + 1);
            sb.append(". ");
            sb.append(playerStocks.get(i).toString());
            sb.append(System.lineSeparator());
        }
        return sb.toString();
    }
}
