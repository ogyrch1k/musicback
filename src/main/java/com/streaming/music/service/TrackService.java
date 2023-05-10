package com.streaming.music.service;

import com.streaming.music.dto.TrackData;
import com.streaming.music.model.*;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;


public interface TrackService {
    TrackData getTrackByName(String name);
    TrackData getTrackById(int id);
    List<TrackData> getTracksById(int[] id);
    TrackData addTrack(TrackData trackdata);
    TrackData updateTrack(TrackData trackData);
    int deleteTrackById(int id);
    String saveTrack(MultipartFile file) throws IOException, NoSuchAlgorithmException;

    TrackData getTrackByIdAndTrackOfAlbum(Integer id, List<Album> trackOfAlbum);
    TrackData getTrackByNameOfTrackAndTrackOfAlbum(String nameOfTrack, List<Album> trackOfAlbum);
    TrackData getTrackByIdAndTrackOfPlaylist(Integer id, List<Playlist> trackOfPlaylist);
    TrackData getTrackByNameOfTrackAndTrackOfPlaylist(String nameOfTrack, List<Playlist> trackOfPlaylist);
    TrackData getTrackByIdAndTrackOfExecutorsIn(Integer id, List<Executor> trackOfExecutors);
    TrackData getTrackByNameOfTrackAndTrackOfExecutorsIn(String nameOfTrack, List<Executor> trackOfExecutors);
    List<TrackData> getAll(Pageable pageable);

    List<TrackData> getAllTrack();

    List<TrackData> findAllByGenreOfTrack(Genre genre , Pageable pageable);
    List<TrackData> findAllByTrackOfExecutors(Executor executor , Pageable pageable);
    List<TrackData> findAllByTrackOfPlaylist(Playlist playlist , Pageable pageable);
    List<TrackData> getAllByTrackOfAlbum(int id);
    List<TrackData> getAllByTrackOfPlaylist(int id);

    int addTrackToExecutor(int idOfTrack, int idOfExecutor);
    int removeTrackFromExecutor(int idOfTrack, int idOfExecutor);

    int addTrackToUser(int idOfTrack,int idOfUser);
    int removeTrackFromUser(int idOfTrack,int idOfUser);

    Genre getGenreOfTrack(int id);
}

