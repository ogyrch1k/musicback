package com.streaming.music.model;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Table(name = "playlists")
@Entity
@ToString
public class Playlist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "name_of_playlist", length = 100)
    private String nameOfPlaylist;

    @Lob
    @Column(name = "description_text")
    private String descriptionText;

    @ManyToMany(mappedBy = "playlists")
    @ToString.Exclude
    private List<User> user = new ArrayList<>();

    @ManyToMany(cascade = {
            CascadeType.PERSIST,CascadeType.MERGE
    })
    @JoinTable(name = "tracks_of_playlists",
            joinColumns = @JoinColumn(name = "id_of_playlist"),
            inverseJoinColumns = @JoinColumn(name = "id_of_track")
    )
    @ToString.Exclude
    private List<Track> tracksOfPlaylists = new ArrayList<>();
}