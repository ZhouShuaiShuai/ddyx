package org.game.pojo;

import lombok.Data;

import javax.persistence.*;

/**
 * @author Zhouyf
 * @Data 2020-09-01  18:58
 * 用户押注模式
 */
@Table(name = "bettingmodel")
@Entity
@Data
public class BettingModel {
    @Id
    @Column(length = 32, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer winModelId;

    private String name;

    private Integer loserModelId;

    private Integer userId;

    private String bettingMap;      //押注

    public BettingModel(){}

    public BettingModel(String name ,Integer winModelId,Integer loserModelId,Integer userId,String bettingMap){
        this.name = name;
        this.winModelId = winModelId;
        this.loserModelId = loserModelId;
        this.userId = userId;
        this.bettingMap = bettingMap;
    }

}
