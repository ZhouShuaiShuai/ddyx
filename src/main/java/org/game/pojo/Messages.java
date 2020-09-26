package org.game.pojo;

import lombok.Data;

import javax.persistence.*;

@Table(name = "messages")
@Entity
@Data
public class Messages {

    @Id
    @Column(length = 32, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String message;

    private Integer is_use;

    public Messages(){}

}
