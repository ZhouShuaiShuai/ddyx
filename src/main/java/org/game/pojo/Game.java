package org.game.pojo;

import lombok.Data;
import org.game.util.MD5;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 游戏
 */
@Table(name = "game" , indexes = {@Index(name = "game_id",  columnList="id", unique = true)})
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

    private Integer number; //中奖号码

    private String numbers; //组成中奖号码的数字

    private BigDecimal winMoney;    //每把赢的奖金

    public Game(){
        startTime = new Date();
        endTime = new Date(System.currentTimeMillis()+90000);
//        endTime = new Date(System.currentTimeMillis()+10000);
        reTime = Integer.parseInt(Long.toString((endTime.getTime()-System.currentTimeMillis())/1000));
        jackpot = new BigDecimal(0);
    }

    public Game(Game game){
        this.startTime = new Date(game.getStartTime());
        this.endTime = new Date(game.getEndTime());
        this.numbers = game.getNumbers();
        this.number = game.getNumber();
        this.jackpot = game.getJackpot();
        this.id = game.getId();
        this.winNum = game.getWinNum();
        this.winMoney = game.getWinMoney();
    }

    public Integer getReTime(){
        return Integer.parseInt(Long.toString((endTime.getTime()-System.currentTimeMillis())/1000));
    }

    public Long getEndTime(){
        return endTime.getTime();
    }

    public Long getStartTime(){
        return startTime.getTime();
    }

    public void setNumber(Integer number){
        this.number = number;

        if(number == 0 ){
            numbers = "0,0,0";
        }else {
            Integer num1,num2,num3;
            if (number <= 10) {
                num1 = MD5.random.nextInt(number);
            } else {
                num1 = MD5.random.nextInt(10);
            }

            if (number - num1 >= 10) {
                num2 = MD5.random.nextInt(10);
            } else {
                num2 = MD5.random.nextInt(number - num1);
            }
            num3 = number - num1 - num2;
            while (num3>9){
                num3 --;
                if(num2<9){
                    num2++;
                }else if(num1<9) {
                    num1 ++;
                }
            }
            this.numbers = num1+","+num2+","+num3;
        }
    }

}
