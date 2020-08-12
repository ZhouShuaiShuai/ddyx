package org.game.pojo;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 转账的账单
 */
@Table(name = "yebill")
@Entity
@Data
@EntityListeners(AuditingEntityListener.class)
public class YeBill {

    @Id
    @Column(length = 32, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @CreatedDate
    private Date createDate;

    private BigDecimal money;

    private String type;

    private Integer userId;

    private BigDecimal balance; //余额

    private Integer gameId; //游戏期号

    private BigDecimal gameMoney;   //使用的金钱 中了是正，押注是负

    public YeBill(){}

    public YeBill(BigDecimal money, String type, User user) {
        this.money = money;
        this.type = type;
        this.userId = user.getId();
        this.balance = user.getMoney();
    }

}
