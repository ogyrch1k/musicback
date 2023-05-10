package com.streaming.music.model;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Table(name = "users", indexes = {
        @Index(name = "role_id", columnList = "role_id"),
        @Index(name = "login", columnList = "login", unique = true)
})
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "nick", length = 100)
    private String nick;

    @Column(name = "path_to_avatar", length = 100)
    private String pathToAvatar;

    @Column(name = "login", length = 100)
    private String login;

    @Column(name = "hash_of_password", length = 100)
    private String hashOfPassword;

    @Column(name = "sold", length = 100)
    private String sold;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(name = "users_playlists",
            joinColumns = @JoinColumn(name = "id_of_user"),
            inverseJoinColumns = @JoinColumn(name = "id_of_playlist")
    )
    private List<Playlist> playlists = new ArrayList<>();

    @ManyToMany(mappedBy = "usersAlbums")
    @ToString.Exclude
    private List<Album> usersAlbums = new ArrayList<>();

    @ManyToMany(mappedBy = "usersExecutors")
    @ToString.Exclude
    private List<Executor> usersExecutors = new ArrayList<>();

//    @ManyToMany(mappedBy = "usersTrack")
//    @ToString.Exclude
//    private List<Track> usersTracks = new ArrayList<>();


    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
        @JoinTable(name = "tracks_of_users",
            joinColumns = @JoinColumn(name = "id_of_user"),
            inverseJoinColumns = @JoinColumn(name = "id_of_track"))
    private  List<Track> tracks =new ArrayList<>();
}