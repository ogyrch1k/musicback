package com.streaming.music.service;

import com.streaming.music.dto.TrackData;
import com.streaming.music.model.*;
import com.streaming.music.repository.*;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import static com.streaming.music.Utilities.FilesUtil.saveMultipartFile;
import static com.streaming.music.Utilities.Hash.hashOfMultipartFile;
@Log

@Service("trackService")
@Transactional
public class MainTrackService implements TrackService{

    private final PlaylistRepository playlistRepository;
    private final TrackRepository trackRepository;
    private final ExecutorRepository executorRepository;
    private final AlbumRepository albumRepository;
    private final GenreRepository genreRepository;

    private final UserRepository userRepository;
    @Value("${upload.path.track}")
    private String uploadPath;

    public MainTrackService(PlaylistRepository playlistRepository, TrackRepository trackRepository, ExecutorRepository executorRepository, AlbumRepository albumRepository, GenreRepository genreRepository, UserRepository userRepository) {
        this.playlistRepository = playlistRepository;
        this.trackRepository = trackRepository;
        this.executorRepository = executorRepository;
        this.albumRepository = albumRepository;
        this.genreRepository = genreRepository;
        this.userRepository = userRepository;
    }

    @Override
    public TrackData getTrackByName(String name) {
        return populateTrackData(trackRepository.getTrackByNameOfTrack(name).orElseThrow(()->new EntityNotFoundException("Track not found")));
    }

    @Override
    public TrackData getTrackById(int id) {
        return populateTrackData(trackRepository.getTrackById(id));
    }

    @Override
    public List<TrackData> getTracksById(int[] id) {
        List<TrackData> trackData = new ArrayList<TrackData>();
        List<Track> track = new ArrayList<Track>();
        for (int idd:id ) {
            track.add(trackRepository.getTrackById(idd));
        }
        for (Track trackk: track ) {
            trackData.add(populateTrackData(trackk));
        }
        return trackData;
    }


    @Override
    public TrackData addTrack(TrackData trackdata) {
        Track track = populateTrackEntity(trackdata);
        return populateTrackData(trackRepository.save(track));
    }

    @Override
    public TrackData updateTrack(TrackData trackData) {
        Track oldTrack = trackRepository.getTrackById(trackData.getId());
        Track track = populateTrackEntityForUpdate(trackData,oldTrack);
        return populateTrackData(trackRepository.save(track));
    }

    @Override
    public int deleteTrackById(int id) {
        trackRepository.deleteById(id);
        return  1;
    }
    private Track populateTrackEntityForUpdate(TrackData trackData,Track track){
        track.setNameOfTrack(trackData.getNameOfTrack());
        track.setHashOFTrack(trackData.getHashOfTrack());
        track.setGenreOfTrack(trackData.getGenre());

        return track;
    }
    private TrackData populateTrackData(Track track){
        TrackData trackData = new TrackData();
        trackData.setNameOfTrack(track.getNameOfTrack());
        trackData.setHashOfTrack(track.getHashOFTrack());
        trackData.setId(track.getId());
        trackData.setGenre(track.getGenreOfTrack());
        return trackData;
    }
    private Track populateTrackEntity(TrackData trackData){
        Track track = new Track();
        track.setNameOfTrack(trackData.getNameOfTrack());
        track.setHashOFTrack(trackData.getHashOfTrack());
        track.setGenreOfTrack(trackData.getGenre());
        try{
            track.setId(trackData.getId());
        }catch (Exception ignored){

        }
        return track;
    }
    public String saveTrack(MultipartFile file) throws IOException, NoSuchAlgorithmException {
        String hashName = hashOfMultipartFile(file);
        String resultFilename = saveMultipartFile(file,uploadPath,hashName);
        return resultFilename;
    }

    @Override
    public TrackData getTrackByIdAndTrackOfAlbum(Integer id, List<Album> album) {
        return populateTrackData(trackRepository.getTrackByIdAndTrackOfAlbumIn(id, album).orElseThrow(()->new EntityNotFoundException("Track not found")));
    }

    @Override
    public TrackData getTrackByNameOfTrackAndTrackOfAlbum(String name, List<Album> album) {
        return populateTrackData(trackRepository.getTrackByNameOfTrackAndTrackOfAlbumIn(name, album).orElseThrow(()->new EntityNotFoundException("Track not found")));
    }

    @Override
    public TrackData getTrackByIdAndTrackOfPlaylist(Integer id, List<Playlist> playlist) {
        return populateTrackData(trackRepository.getTrackByIdAndTrackOfPlaylistIn(id, playlist).orElseThrow(()->new EntityNotFoundException("Track not found")));
    }

    @Override
    public TrackData getTrackByNameOfTrackAndTrackOfPlaylist(String name, List<Playlist> playlist) {
        return populateTrackData(trackRepository.getTrackByNameOfTrackAndTrackOfPlaylistIn(name, playlist).orElseThrow(()->new EntityNotFoundException("Track not found")));
    }

    @Override
    public TrackData getTrackByIdAndTrackOfExecutorsIn(Integer id, List<Executor> trackOfExecutors) {
        return populateTrackData(trackRepository.getTrackByIdAndTrackOfExecutorsIn(id, trackOfExecutors).orElseThrow(()->new EntityNotFoundException("Track not found")));
    }

    @Override
    public TrackData getTrackByNameOfTrackAndTrackOfExecutorsIn(String nameOfTrack, List<Executor> trackOfExecutors) {
        return populateTrackData(trackRepository.getTrackByNameOfTrackAndTrackOfExecutorsIn(nameOfTrack, trackOfExecutors).orElseThrow(()->new EntityNotFoundException("Track not found")));
    }

    @Override
    public  List<TrackData> getAll(Pageable pageable) {
        Page<Track> tracks=trackRepository.findAll( pageable);
        List<TrackData> trackData= new ArrayList<>();
        for (Track w:tracks ) {
            trackData.add(populateTrackData(w));
        }
        return trackData;
    }
    @Override
    public  List<TrackData> getAllTrack() {
        List<Track> track= trackRepository.findAll();
        List<TrackData> trackData= new ArrayList<>();
        for (Track w:track ) {
            trackData.add(populateTrackData(w));
        }
        return trackData;
    }

    @Override
    public List<TrackData> findAllByGenreOfTrack(Genre genre, Pageable pageable) {
        Page<Track> tracks=trackRepository.findAllByGenreOfTrack( genre, pageable);
        List<TrackData> trackData= new ArrayList<>();
        for (Track w:tracks ) {
            trackData.add(populateTrackData(w));
        }
        return trackData;
    }

    @Override
    public List<TrackData> findAllByTrackOfExecutors(Executor executor, Pageable pageable) {
        Page<Track> tracks=trackRepository.findAllByTrackOfExecutors( executor, pageable);
        List<TrackData> trackData= new ArrayList<>();
        for (Track w:tracks ) {
            trackData.add(populateTrackData(w));
        }
        return trackData;
    }

    @Override
    public List<TrackData> findAllByTrackOfPlaylist(Playlist playlist, Pageable pageable) {
        Page<Track> tracks=trackRepository.findAllByTrackOfPlaylist( playlist, pageable);
        List<TrackData> trackData= new ArrayList<>();
        for (Track w:tracks ) {
            trackData.add(populateTrackData(w));
        }
        return trackData;
    }

    @Override
    public List<TrackData> getAllByTrackOfAlbum(int id) {
        List<TrackData> trackData = new ArrayList<>();
        List<Track> tracks =trackRepository.getAllByTrackOfAlbum(albumRepository.getAlbumById(id).orElseThrow(()-> new EntityNotFoundException("Album not found")));
        for (Track w:tracks) {
            trackData.add(populateTrackData(w));
        }
        return trackData;
    }

    @Override
    public List<TrackData> getAllByTrackOfPlaylist( int id) {
        List<TrackData> trackData = new ArrayList<>();
        List<Track> tracks =trackRepository.getAllByTrackOfPlaylist(playlistRepository.getById(id));
        for (Track w:tracks) {
            trackData.add(populateTrackData(w));
        }
        return trackData;
    }

    @Override
    public int addTrackToExecutor(int idOfTrack, int idOfExecutor) {
        Executor executor = executorRepository.getById(idOfExecutor);
        List<Track> tracks = trackRepository.getAllByTrackOfExecutors(executor);
        tracks.add(trackRepository.getById(idOfTrack));
        executor.setTracksOfExecutors(tracks);
        executorRepository.save(executor);
        return 1;
    }

    @Override
    public int removeTrackFromExecutor(int idOfTrack, int idOfExecutor) {
        Executor executor = executorRepository.getById(idOfExecutor);
        List<Track> tracks = trackRepository.getAllByTrackOfExecutors(executor);
        tracks.remove(trackRepository.getById(idOfTrack));
        executor.setTracksOfExecutors(tracks);
        executorRepository.save(executor);
        return 1;
    }

    @Override
    public int addTrackToUser(int idOfTrack, int idOfUser) {
        User user = userRepository.getById(idOfUser).orElseThrow(()->new EntityNotFoundException("User not found"));
        List<Track> tracks = trackRepository.getAllByUser(user);
        tracks.add(trackRepository.getTrackById(idOfTrack));
        user.setTracks(tracks);
        userRepository.save(user);
        return 1;
    }

    @Override
    public int removeTrackFromUser(int idOfTrack, int idOfUser) {
        User user = userRepository.getById(idOfUser).orElseThrow(()->new EntityNotFoundException("User not found"));
        List<Track> tracks = trackRepository.getAllByUser(user);
        tracks.remove(trackRepository.getTrackById(idOfTrack));
        user.setTracks(tracks);
        userRepository.save(user);
        return 1;
    }

    @Override
    public Genre getGenreOfTrack(int id) {
        Executor executor =executorRepository.getByTracksOfExecutors(trackRepository.getTrackById(id)).orElseThrow();
        log.info("executor"+executor.getId() );
        List<Genre> genres = genreRepository.getByGenresExecutor(executor);
        return genres.get(0);
    }
}
