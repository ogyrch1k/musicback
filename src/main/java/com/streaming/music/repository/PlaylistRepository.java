package com.streaming.music.repository;

import com.streaming.music.model.Playlist;
import com.streaming.music.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PlaylistRepository extends JpaRepository<Playlist, Integer> {
    Optional<Playlist> getPlaylistById(Integer integer);
    Optional<Playlist> findPlaylistByNameOfPlaylist(String name);
    void deleteById(int id);
    int deleteByNameOfPlaylist(String name);
    Optional<Playlist> getPlaylistByIdAndUser(Integer integer, User user);
    Optional<Playlist> getPlaylistByNameOfPlaylistAndUser(String name, User user);
    Optional<List<Playlist>> getAllByUser(User user);
}