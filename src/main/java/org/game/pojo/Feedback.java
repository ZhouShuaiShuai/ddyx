package org.game.pojo;

import lombok.Data;

import javax.persistence.*;

/**
 * 意见反馈
 */
@Table(name = "feedback")
@Entity
@Data
public class Feedback {

    @Id
    @Column(length = 32, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String msg;
    private String phoneOrMail;
    private Integer userId;

    public Feedback(String msg, String phoneOrMail, Integer userId) {
        this.msg = msg;
        this.phoneOrMail = phoneOrMail;
        this.userId = userId;
    }

}
