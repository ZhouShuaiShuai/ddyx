package org.game.pojo;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

/**
 * @author Zhouyf
 * @Data 2020-08-13  21:48
 * 模拟的用户数据
 */
@Data
@Table(name = "userInfo" , indexes = {@Index(name = "game_id",  columnList="gameId")})
@Entity
@EntityListeners(AuditingEntityListener.class)
public class UserInfo {

    @Id
    @Column(length = 6, nullable = false)
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

    @CreatedDate
    private Date createDate;

    private String userName;

    private String headImg;
    //投注
    private long tz;
    //获利
//    private Integer hl;
    private long hl;

    //盈利
    private long yl;

    private Integer gameId;

    private Integer num;

}
