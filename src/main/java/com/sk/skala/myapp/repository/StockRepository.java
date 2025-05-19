// StockRepository.java
package com.sk.skala.myapp.repository;

import com.sk.skala.myapp.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface StockRepository extends JpaRepository<Stock, String> {
    // Spring Data JPA automatically provides methods like findAll(), findById(), save(), etc.

    // Custom methods if needed (Spring Data JPA will create the implementation)
    List<Stock> findAllByOrderByStockNameAsc();
}
