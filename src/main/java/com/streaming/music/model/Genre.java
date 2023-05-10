package com.streaming.music.model;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
@Data
@Entity
@ToString
@Table(name = "genres")
public class Genre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "name_of_genre", length = 100)
    private String nameOfGenre;

    @ManyToMany(mappedBy = "genresOfExecutors")
    @ToString.Exclude
    private List<Executor> genresExecutor = new ArrayList<>();
}