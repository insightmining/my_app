// PlayerStockRepository.java
package com.sk.skala.myapp.repository;

import com.sk.skala.myapp.model.Player;
import com.sk.skala.myapp.model.PlayerStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface PlayerStockRepository extends JpaRepository<PlayerStock, Long> {
    List<PlayerStock> findByPlayer(Player player);
    List<PlayerStock> findByPlayerPlayerId(String playerId);
    Optional<PlayerStock> findByPlayerAndStockName(Player player, String stockName);
    Optional<PlayerStock> findByPlayerPlayerIdAndStockName(String playerId, String stockName);
}
