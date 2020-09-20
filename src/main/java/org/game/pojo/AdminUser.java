package org.game.pojo;

import lombok.Data;

import javax.persistence.*;

/**
 * @author Zhouyf
 * @Data 2020-09-18  15:28
 */
@Table(name = "admin_user")
@Entity
@Data
public class AdminUser{

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

    private String userName;

    private String pwd;

    private String role;

}
