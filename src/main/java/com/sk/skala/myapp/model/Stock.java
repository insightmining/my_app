// Stock.java
package com.sk.skala.myapp.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "stocks")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Stock {
    @Id
    private String stockName;
    private int stockPrice;

    @Override
    public String toString() {
        return stockName + ":" + stockPrice;
    }
}
