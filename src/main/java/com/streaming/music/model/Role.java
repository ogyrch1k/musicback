package com.streaming.music.model;

import lombok.Data;

import javax.persistence.*;


@Entity
@Table(name = "role_table")
@Data
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
}
