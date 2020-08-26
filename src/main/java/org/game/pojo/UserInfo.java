package org.game.pojo;

import lombok.Data;

import javax.persistence.*;

/**
 * @author Zhouyf
 * @Data 2020-08-13  21:48
 * 模拟的用户数据
 */
@Data
@Table(name = "userInfo" , indexes = {@Index(name = "game_id",  columnList="gameId", unique = true)})
@Entity
public class UserInfo {

    @Id
    @Column(length = 6, nullable = false)
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

    private String userName;

    private String headImg;
    //投注
    private Integer tz;
    //获利
    private Integer hl;

    //盈利
    private Integer yl;

    private Integer gameId;

    private Integer num;

}
