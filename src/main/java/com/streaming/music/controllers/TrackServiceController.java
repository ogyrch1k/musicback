package com.streaming.music.controllers;

import com.streaming.music.dto.*;
import com.streaming.music.service.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.persistence.EntityNotFoundException;
@CrossOrigin
@RestController
public class TrackServiceController {
    @Resource(name="trackService")
    private TrackService trackService;
    @Resource(name="playlist_service")
    private PlaylistService playlistService;

    @Resource(name="executor_service")
    private ExecutorService executorService;

    @Resource(name="album_service")
    private AlbumService albumService;

    @Resource(name = "userService")
    private UserService userService ;
    @Value("${upload.path.track}")
    private String uploadPath;

    @PostMapping("/trackExecutor")
    public ResponseEntity<Object> addTrackToExecutor(@RequestParam("idOfTrack") int idOftrack, @RequestParam("idOfExecutor") int idOfOExecutor){
        try{
            TrackData trackData = trackService.getTrackById(idOftrack);
            ExecutorData executorData = executorService.getExecutorById(idOfOExecutor);
        }catch (EntityNotFoundException ex){
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
        return ResponseEntity.ok().body(executorService.addTrackToExecutor(idOfOExecutor,idOftrack));
    }

    @PostMapping("/trackAlbum")
    public ResponseEntity<Object> addTrackToAlbum(@RequestParam("idOfTrack") int idOfTrack, @RequestParam("idOfAlbum") int idOfOAlbum){
        try{
            AlbumData albumData = albumService.getAlbumById(idOfOAlbum);
            TrackData trackData = trackService.getTrackById(idOfTrack);
        }catch (EntityNotFoundException ex){
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
        return ResponseEntity.ok().body(albumService.addTrackToAlbum(idOfOAlbum,idOfTrack));
    }

    @PostMapping("/trackPLaylist")
    public ResponseEntity<Object> addTrackToPlaylist(@RequestParam("idOfTrack") int idOfTrack,@RequestParam("idOfPlaylist") int idOfPlaylist){
        try{
            PlaylistData playlistData = playlistService.getPlaylistById(idOfPlaylist);
            TrackData trackData = trackService.getTrackById(idOfTrack);
        }catch (EntityNotFoundException ex){
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
        return ResponseEntity.ok().body(playlistService.addTrackToPlaylist(idOfPlaylist,idOfTrack));
    }

    @DeleteMapping("/trackPLaylist")
    public ResponseEntity<Object> removeTrackFromPLaylist(@RequestParam("idOfTrack") int idOfTrack,@RequestParam("idOfPlaylist") int idOfPlaylist){
        try{
            PlaylistData playlistData = playlistService.getPlaylistById(idOfPlaylist);
            TrackData trackData = trackService.getTrackById(idOfTrack);
        }catch (EntityNotFoundException ex){
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
        return ResponseEntity.ok().body(playlistService.delTrackToPlaylist(idOfPlaylist,idOfTrack));
    }

    @DeleteMapping("/trackAlbum")
    public ResponseEntity<Object> removeTrackFromAlbum(@RequestParam("idOfTrack") int idOfTrack,@RequestParam("idOfAlbum") int idOfAlbum){
        try{
            AlbumData albumData = albumService.getAlbumById(idOfAlbum);
            TrackData trackData = trackService.getTrackById(idOfTrack);
        }catch (EntityNotFoundException ex){
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
        return ResponseEntity.ok().body(albumService.removeTrackOfAlbum(idOfAlbum,idOfTrack));
    }

    @RequestMapping(value = "/trackUser", method = RequestMethod.POST)
    public ResponseEntity<Object> addTrackToUser(@RequestParam("idOfTrack") int idOfTrack, @RequestParam("idOfUser") int idOfUser){
        System.out.println(idOfTrack);
        System.out.println(idOfUser);
        try{
            UserData userData = userService.getUserById(idOfUser);
            TrackData trackData = trackService.getTrackById(idOfTrack);
        }catch (EntityNotFoundException ex){
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
        return ResponseEntity.ok().body(trackService.addTrackToUser(idOfTrack,idOfUser));
    }

    @DeleteMapping("/trackUser")
    public ResponseEntity<Object> removeTrackFromUser(@RequestParam("idOfTrack") int idOfTrack, @RequestParam("idOfUser") int idOfUser){
        try{
            UserData userData = userService.getUserById(idOfUser);
            TrackData trackData = trackService.getTrackById(idOfTrack);
        }catch (EntityNotFoundException ex){
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
        return ResponseEntity.ok().body(trackService.removeTrackFromUser(idOfTrack,idOfUser));
    }
}
