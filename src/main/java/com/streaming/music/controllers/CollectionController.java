package com.streaming.music.controllers;

import com.streaming.music.service.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
@CrossOrigin
@RestController
public class CollectionController {
    @Resource(name = "playlist_service")
    private PlaylistService playlistService;
    @Resource(name = "userService")
    private UserService userService ;
    @Resource(name="trackService")
    private TrackService trackService;
    @Resource(name="album_service")
    private AlbumService albumService;
    @Resource(name = "executor_service")
    private ExecutorService executorService;

    @RequestMapping(value = "/collectionTrack", method = RequestMethod.POST)
    public ResponseEntity<Object> addTrackToUser(@RequestParam("idOfTrack") int idOfTrack, @RequestParam("idOfUser") int idOfUser){
        return ResponseEntity.ok().body(trackService.addTrackToUser(idOfTrack,idOfUser));
    }
    @DeleteMapping("/collectionTrack")
    public ResponseEntity<Object> removeTrackFromUser(@RequestParam("idOfTrack") int idOfTrack, @RequestParam("idOfUser") int idOfUser){
        return ResponseEntity.ok().body(trackService.removeTrackFromUser(idOfTrack,idOfUser));
    }
    @PostMapping("/collectionAlbum")
    public ResponseEntity<Object> addAlbumUser(@RequestParam ("idOfAlbum") int idOfAlbum, @RequestParam ("idOfUser") int idOfUser){
        return ResponseEntity.ok().body(albumService.addAlbumToUser(idOfAlbum,idOfUser));
    }
    @DeleteMapping("/collectionAlbum")
    public ResponseEntity<Object> removeAlbumUser(@RequestParam ("idOfAlbum") int idOfAlbum, @RequestParam ("idOfUser") int idOfUser){
        return ResponseEntity.ok().body(albumService.removeAlbumFromUser(idOfAlbum,idOfUser));
    }
    @PostMapping("/collectionExecutor")
    public ResponseEntity<Object> addExecutorToUser(@RequestParam("idOfExecutor") int idOfExecutor, @RequestParam("idOfUser") int idOfUser){
        return ResponseEntity.ok().body(executorService.addExecutorToUser(idOfExecutor,idOfUser));
    }
    @DeleteMapping("/collectionExecutor")
    public ResponseEntity<Object> removeExecutorFromUser(@RequestParam("idOfExecutor") int idOfExecutor, @RequestParam("idOfUser") int idOfUser){
        return ResponseEntity.ok().body(executorService.removeExecutorFromUser(idOfExecutor,idOfUser));
    }
    @PostMapping("/collectionPlaylist")
    public ResponseEntity<Object> addPlaylistToUser(@RequestParam("idOfPlaylist") int idOfPlaylist, @RequestParam("loginUser") String loginUser){
        return ResponseEntity.ok().body(playlistService.addPlaylistToUser(idOfPlaylist,loginUser));
    }

}
