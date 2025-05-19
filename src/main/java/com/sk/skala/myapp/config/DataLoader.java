// DataLoader.java
package com.sk.skala.myapp.config;

import com.sk.skala.myapp.model.Player;
import com.sk.skala.myapp.model.PlayerStock;
import com.sk.skala.myapp.model.Stock;
import com.sk.skala.myapp.model.StockConstants;
import com.sk.skala.myapp.repository.PlayerRepository;
import com.sk.skala.myapp.repository.PlayerStockRepository;
import com.sk.skala.myapp.repository.StockRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class DataLoader {

    @Value("classpath:data/stocks.txt")
    private Resource stocksFile;

    @Value("classpath:data/players.txt")
    private Resource playersFile;

    @Bean
    public CommandLineRunner loadData(
            StockRepository stockRepository,
            PlayerRepository playerRepository,
            PlayerStockRepository playerStockRepository
    ) {
        return args -> {
            // Only load from files if repositories are empty
            if (stockRepository.count() == 0) {
                loadStocks(stockRepository);
            }

            if (playerRepository.count() == 0) {
                loadPlayers(playerRepository, playerStockRepository);
            }
        };
    }

    private void loadStocks(StockRepository stockRepository) {
        List<Stock> stocks = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(stocksFile.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(StockConstants.DELIMITER);
                if (fields.length > 1) {
                    stocks.add(new Stock(fields[0], Integer.parseInt(fields[1])));
                }
            }
        } catch (Exception e) {
            // If file not found or error, load default stocks
            stocks.add(new Stock("TechCorp", 100));
            stocks.add(new Stock("GreenEnergy", 80));
            stocks.add(new Stock("HealthPlus", 120));
            stocks.add(new Stock("samsung", 300));
        }

        stockRepository.saveAll(stocks);
    }

    private void loadPlayers(PlayerRepository playerRepository, PlayerStockRepository playerStockRepository) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(playersFile.getInputStream()))) {
            String line;
            int playerCount = 0;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(StockConstants.DELIMITER);
                if (fields.length > 1) {
                    Player player = new Player();
                    player.setPlayerId(fields[0]);
                    player.setPlayerMoney(Integer.parseInt(fields[1]));

                    // 먼저 Player 저장
                    player = playerRepository.save(player);
                    playerCount++;

                    if (fields.length > 2 && fields[2].indexOf(StockConstants.STOCK_PROPS_DELIMITER) > 0) {
                        List<PlayerStock> playerStocks = parseFieldToStockList(fields[2], player);

                        // PlayerStock 저장
                        for (PlayerStock playerStock : playerStocks) {
                            playerStock.setPlayer(player);
                            playerStockRepository.save(playerStock);
                            System.out.println("INFO: 플레이어 " + player.getPlayerId() + "의 주식 저장됨 - " + playerStock.getStockName());
                        }
                    }
                }
            }
            System.out.println("INFO: 총 " + playerCount + "명의 플레이어 데이터를 로드했습니다.");
        } catch (Exception e) {
            System.out.println("ERROR: 플레이어 데이터 로드 중 오류 발생");
            e.printStackTrace();
        }
    }

    private List<PlayerStock> parseFieldToStockList(String field, Player player) {
        List<PlayerStock> list = new ArrayList<>();

        String[] stocks = field.split(StockConstants.STOCK_DELIMITER);
        for (String stockData : stocks) {
            String[] props = stockData.split(StockConstants.STOCK_PROPS_DELIMITER);
            if (props.length > 2) {
                PlayerStock playerStock = new PlayerStock(props[0], props[1], props[2]);
                playerStock.setPlayer(player);
                list.add(playerStock);
            }
        }

        return list;
    }
}
