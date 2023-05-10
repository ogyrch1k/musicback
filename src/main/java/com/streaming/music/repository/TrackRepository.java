package com.streaming.music.repository;

import com.streaming.music.model.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface TrackRepository extends PagingAndSortingRepository<Track, Integer> {
    Optional<Track> getTrackByNameOfTrack(String name);
    Track getTrackById(int id);
    List<Track> getTracksById(int[] id);
    Optional<Track> getTrackByIdAndTrackOfAlbumIn(Integer id, List<Album> trackOfAlbum);
    Optional<Track> getTrackByNameOfTrackAndTrackOfAlbumIn(String nameOfTrack, List<Album> trackOfAlbum);
    Optional<Track> getTrackByIdAndTrackOfPlaylistIn(Integer id, List<Playlist> trackOfPlaylist);
    Optional<Track> getTrackByNameOfTrackAndTrackOfPlaylistIn(String nameOfTrack, List<Playlist> trackOfPlaylist);
    Page<Track> findAll(Pageable pageable);
    Page<Track> findAllByGenreOfTrack(Genre genre , Pageable pageable);
    Page<Track> findAllByTrackOfExecutors(Executor executor , Pageable pageable);
    Page<Track> findAllByTrackOfPlaylist(Playlist playlist , Pageable pageable);
    Optional<Track> getTrackByIdAndTrackOfExecutorsIn(Integer id, List<Executor> trackOfExecutors);
    Optional<Track> getTrackByNameOfTrackAndTrackOfExecutorsIn(String nameOfTrack, List<Executor> trackOfExecutors);
    List<Track> getAllByTrackOfAlbum(Album album);
    List<Track> getAllByTrackOfPlaylist(Playlist playlist);
    List<Track> getAllByTrackOfExecutors(Executor executor);
    void deleteById(Integer id);

    void deleteAllByTrackOfAlbum(Album album);
    List<Track> findAll();


    Page<Track> getAllByUser(User user,Pageable pageable);

    List<Track> getAllByUser(User user);
    Track getById(int idOfTrack);


}