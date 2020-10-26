package org.game.pojo;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Table(name = "user")
@Entity
@Data
public class User {

    @Id
    @Column(length = 6, nullable = false)
    @TableGenerator(
            name = "AppSeqStore",
            table = "APP_SEQ_STORE",
            pkColumnName = "APP_SEQ_NAME",
            pkColumnValue = "LISTENER_PK",
            valueColumnName = "APP_SEQ_VALUE",
            initialValue = 100000,
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "AppSeqStore")
    private Integer id;
    @Column(length = 20)
    private String userName;
    @Column(length = 20)
    private String phone;
    private String pwd;
    private String jkpwd;
    @Column(length = 20)
    private String yqm; //邀请码
    @Column(length = 2)
    private String sex;
    @Column(length = 20)
    private String bri;
    @Column(length = 20)
    private String name;
    @Column(length = 20)
    private String hy; //会员
    private BigDecimal money;
    private BigDecimal jkmoney;
    private Integer isUse; //是否启用 1 启用 0 不启用
    private String headImg; //头像

    private String yhNum;   //银行卡
    private String yhName;  //银行名称

}
