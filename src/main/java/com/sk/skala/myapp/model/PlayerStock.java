// PlayerStock.java
package com.sk.skala.myapp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "player_stocks")
@Getter
@Setter
@NoArgsConstructor
public class PlayerStock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String stockName;
    private int stockPrice;
    private int stockQuantity;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id")
    private Player player;

    public PlayerStock(Stock stock, int quantity) {
        this.stockName = stock.getStockName();
        this.stockPrice = stock.getStockPrice();
        this.stockQuantity = quantity;
    }

    // 파일에서 읽은 정보로 객체를 생성하기 위한 constructor
    public PlayerStock(String name, String price, String quantity) {
        this.stockName = name;
        this.stockPrice = Integer.parseInt(price);
        this.stockQuantity = Integer.parseInt(quantity);
    }

    @Override
    public String toString() {
        return stockName + ":" + stockPrice + ":" + this.stockQuantity;
    }
}
