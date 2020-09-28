package org.game.pojo;


import lombok.Data;

import javax.persistence.*;

@Table(name = "nrb")
@Entity
@Data
public class Nrb {

    @Id
    @Column(length = 32, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String head_img;

    private long hl;

    private String name;

    private String type;

}
