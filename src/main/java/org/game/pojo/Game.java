package org.game.pojo;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 游戏
 */
@Table(name = "game")
@Entity
@Data
public class Game {

    @Id
    @Column(length = 8, nullable = false)
    @TableGenerator(
            name = "GameSeqStore",
            table = "GAME_SEQ_STORE",
            pkColumnName = "GAME_SEQ_NAME",
            pkColumnValue = "LISTENER_PK",
            valueColumnName = "GAME_SEQ_VALUE",
            initialValue = 10000000,
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "GameSeqStore")
    private Integer id;

    private Date startTime;

    private Date endTime;

    private Integer reTime; //剩余秒数

    private Integer winNum; //中奖人数

    private BigDecimal jackpot; //奖池

    private Integer number;

    public Game(){
        startTime = new Date();
        endTime = new Date(System.currentTimeMillis()+90000);
        reTime = Integer.parseInt(Long.toString((endTime.getTime()-System.currentTimeMillis())/1000));
        jackpot = new BigDecimal(0);
    }

    public Integer getReTime(){
        return Integer.parseInt(Long.toString((endTime.getTime()-System.currentTimeMillis())/1000));
    }


}
