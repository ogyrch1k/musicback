package com.streaming.music.service;

import com.streaming.music.dto.PlaylistData;
import com.streaming.music.model.Playlist;
import com.streaming.music.model.Track;
import com.streaming.music.model.User;
import com.streaming.music.repository.PlaylistRepository;
import com.streaming.music.repository.TrackRepository;
import com.streaming.music.repository.UserRepository;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
@Log
@Service("playlist_service")
public class MainPlaylistService implements PlaylistService{

    @Resource(name = "userService")
    private UserService userService;

    private final UserRepository userRepository;
    private final PlaylistRepository playlistRepository;
    private final TrackRepository trackRepository;
    public MainPlaylistService(UserRepository userRepository, PlaylistRepository playlistRepository, TrackRepository trackRepository) {
        this.userRepository = userRepository;
        this.playlistRepository = playlistRepository;
        this.trackRepository = trackRepository;
    }

    @Override
    public PlaylistData getPlaylistById(int id) {
        return populatePlaylistData(playlistRepository.getPlaylistById(id).orElseThrow(()-> new  EntityNotFoundException("Playlist not found")));
    }

    @Override
    public PlaylistData getPlaylistByName(String name) {
        return populatePlaylistData(playlistRepository.findPlaylistByNameOfPlaylist(name).orElseThrow(()-> new EntityNotFoundException("Playlist not found")));
    }

    @Override
    public int deleteById(int id) {
        playlistRepository.getPlaylistById(id).orElseThrow(()->new EntityNotFoundException("Playlist not found"));
        playlistRepository.deleteById(id);
        return  1;
    }

    @Override
    public int deleteByName(String name) {
        return playlistRepository.deleteByNameOfPlaylist(name);
    }

    @Override
    public PlaylistData createPlaylist(PlaylistData playlistData) {
        Playlist playlist = populatePlaylistEntity(playlistData);
        PlaylistData playlistData1=populatePlaylistData(playlistRepository.save(playlist));
        log.info(playlistData1.getId().toString());
        return playlistData1;
    }

    @Override
    public PlaylistData updatePlaylist(PlaylistData playlistData) {
        Playlist oldPlayLists =playlistRepository.getById(playlistData.getId());
        Playlist playlist = populatePlaylistEntityForUpdate(playlistData,oldPlayLists);
        return populatePlaylistData(playlistRepository.save(playlist));
    }

    private Playlist populatePlaylistEntityForUpdate(PlaylistData playlistData, Playlist playlist){
        playlist.setNameOfPlaylist(playlistData.getNameOfPlaylist());
        playlist.setDescriptionText(playlistData.getDescriptionText());
        return playlist;
    }

    private Playlist populatePlaylistEntity(PlaylistData playlistData){
        Playlist playlist = new Playlist();
        playlist.setNameOfPlaylist(playlistData.getNameOfPlaylist());
        playlist.setDescriptionText(playlistData.getDescriptionText());
        try{
            playlist.setId(playlistData.getId());
        }catch (Exception ignored){

        }
        return playlist;
    }

    private PlaylistData populatePlaylistData(Playlist playlist){
        PlaylistData playlistData = new PlaylistData();
        playlistData.setNameOfPlaylist(playlist.getNameOfPlaylist());
        playlistData.setDescriptionText(playlist.getDescriptionText());
        playlistData.setId(playlist.getId());
        return playlistData;
    }

    @Override
    public int addTrackToPlaylist(int idOfPlaylist, int... idOfTrack) {
        Playlist playlist =playlistRepository.getById(idOfPlaylist);
        List<Track> list = trackRepository.getAllByTrackOfPlaylist(playlistRepository.getById(idOfPlaylist));
        for (int id:idOfTrack) {
            list.add(trackRepository.getTrackById(id));
        }
        playlist.setTracksOfPlaylists((List<Track>) list);
        playlistRepository.save(playlist);
        return 1;
    }

    @Override
    public int delTrackToPlaylist(int idOfPlaylist, int... idOfTrack) {
        Playlist playlist =playlistRepository.getById(idOfPlaylist);
        List<Track> list = trackRepository.getAllByTrackOfPlaylist(playlistRepository.getById(idOfPlaylist));
        for (int id:idOfTrack) {
            list.remove(trackRepository.getTrackById(id));
        }
        playlist.setTracksOfPlaylists((List<Track>) list);
        playlistRepository.save(playlist);
        return 1;
    }

    @Override
    public PlaylistData getPlaylistByNameOfPlaylistAndUser(String name, User user) {
        return populatePlaylistData(playlistRepository.getPlaylistByNameOfPlaylistAndUser(name,user).orElseThrow(()->new EntityNotFoundException("Playlist not found")));
    }

    @Override
    public List<PlaylistData> getAllByUser(String login) {
        User user = userRepository.getUserByLogin(login).orElseThrow(()-> new EntityNotFoundException("User not found"));
        List<Playlist> playlists = playlistRepository.getAllByUser(user).orElseThrow(()-> new EntityNotFoundException("Play not found"));
        List<PlaylistData> playlistData = new ArrayList<>();
        for (Playlist w:playlists) {
            playlistData.add(populatePlaylistData(w));
        }
        return playlistData;
    }

    @Override
    public int addPlaylistToUser(int idPlaylist, String loginUser) {
        User user= userRepository.getUserByLogin(loginUser).orElseThrow(()-> new EntityNotFoundException("User not found"));
        List<Playlist> playlists= playlistRepository.getAllByUser(user).orElseThrow(()-> new EntityNotFoundException("Play not found"));
        playlists.add(playlistRepository.getById(idPlaylist));
        user.setPlaylists((List<Playlist>) playlists);
        userRepository.save(user);
        return 1;
    }

}
