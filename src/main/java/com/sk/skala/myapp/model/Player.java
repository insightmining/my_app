// Player.java
package com.sk.skala.myapp.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "players")
@Data
@NoArgsConstructor
public class Player {
    @Id
    private String playerId;
    private int playerMoney;

    // @OneToMany 관계 제거 - PlayerStock에서만 Player를 참조함

    public Player(String id) {
        this.playerId = id;
        this.playerMoney = 10_000;
    }
}
