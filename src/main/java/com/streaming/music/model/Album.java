package com.streaming.music.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Data
@Table(name = "albums")
@Entity
@ToString
public class Album {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "name_of_album", length = 100)
    private String nameOfAlbum;

    @Column(name = "date_Of_create")
    private LocalDate dateOfCreate;

    @Column(name = "hash_of_avatar", length = 100)
    private String hashOfAvatar;

    @Lob
    @Column(name = "description_text")
    private String descriptionText;

    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(name = "tracks_of_albums",
            joinColumns = @JoinColumn(name = "id_of_album"),
            inverseJoinColumns = @JoinColumn(name = "id_of_track")
    )
    @ToString.Exclude
    private List<Track> tracksOfAlbums = new ArrayList<>();

    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(name = "albums_of_executors",
            joinColumns = @JoinColumn(name = "id_of_album"),
            inverseJoinColumns = @JoinColumn(name = "id_of_executor")
    )
    @ToString.Exclude
    private List<Executor> albumsOFExecutor = new ArrayList<>();


    @ManyToOne
    @JoinColumn(name = "genre")
    private Genre genre;
    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(name = "users_albums",
            joinColumns = @JoinColumn(name = "id_of_album"),
            inverseJoinColumns = @JoinColumn(name = "id_of_user"))
    private List<User> usersAlbums = new ArrayList<>();

}