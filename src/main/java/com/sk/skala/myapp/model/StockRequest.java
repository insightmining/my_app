package com.sk.skala.myapp.model;

import lombok.Data;

@Data
public class StockRequest {
    private String stockName;
    private int quantity;
}
