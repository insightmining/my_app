package com.sk.skala.myapp.model;

/**
 * class final은 상속을 막기 위해 사용한다.
 * private StockConstants()는 인스턴스화 방지를 위해 사용한다.
 */
public final class StockConstants {
    private StockConstants() {
    }

    // txt file 내 컬럼를 구분 하기 위한 구분자
    public static final String DELIMITER = ",";

    // stock.txt 내 종목명, 가격, 수량을 구분 하기 위한 구분자 (종목명:가격:수량)
    public static final String STOCK_PROPS_DELIMITER = ":";

    // stock.txt 내 종목을 구분 하기 위한 구분자 (종목명:가격:수량|종목명:가격:수량|...)
    public static final String STOCK_DELIMITER = "\\|";
}
