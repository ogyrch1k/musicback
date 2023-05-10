package com.streaming.music.service;

import com.streaming.music.dto.PlaylistData;
import com.streaming.music.model.User;
import java.util.List;

public interface PlaylistService {
    PlaylistData getPlaylistById(int id);
    PlaylistData getPlaylistByName(String name);
    int deleteById(int id);
    int deleteByName(String name);
    PlaylistData createPlaylist(PlaylistData playlistData);
    PlaylistData updatePlaylist(PlaylistData playlistData);
    int addTrackToPlaylist(int idOfPlaylist, int... idOfTrack);
    int delTrackToPlaylist(int idOfPlaylist, int... idOfTrack);
    PlaylistData getPlaylistByNameOfPlaylistAndUser(String name, User user);
    List<PlaylistData> getAllByUser(String login);
    int addPlaylistToUser(int idPlaylist ,String idUser);

}
