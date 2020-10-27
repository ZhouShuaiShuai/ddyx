package org.game.pojo;

import lombok.Data;

import javax.persistence.*;

@Table(name = "yh")
@Entity
@Data
public class Yh {

    @Id
    private Integer id;

    private String yhName;

    private String yhNum;

    private String skName;

}
