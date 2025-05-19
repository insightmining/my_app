// PlayerRepository.java
package com.sk.skala.myapp.repository;

import com.sk.skala.myapp.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerRepository extends JpaRepository<Player, String> {
    // Spring Data JPA automatically provides methods like findAll(), findById(), save(), etc.
}
