package org.game.pojo;

import lombok.Data;

import javax.persistence.*;

/**
 * @author Zhouyf
 * @Data 2020-08-07  20:58
 */
@Table(name = "betting")
@Entity
@Data
public class Betting {

    @Id
    @Column(length = 32, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer userId;

    private Integer gameId;

    private String bettingMap;

    public Betting(){}

    public Betting(Integer userId,Integer gameId,String bettingMap){
        this.userId = userId;
        this.gameId = gameId;
        this.bettingMap = bettingMap;
    }


}
