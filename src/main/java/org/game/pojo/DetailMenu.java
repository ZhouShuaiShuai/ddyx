package org.game.pojo;


import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Table(name = "detail_menu")
@Entity
@Data
@EntityListeners(AuditingEntityListener.class)
public class DetailMenu {

    @Id
    @Column(length = 32, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String userName;

    private String userYhNum;

    private String userYhName;

    private BigDecimal jdNum;

    private Integer ytype;   //类型。体现还是充值

    private Integer ystate;     //状态 1.提交 2.通过 3.驳回

    private String detailss;  //备注

    @CreatedDate
    private Date createDate;
    @LastModifiedDate
    private Date updateDate;
}
