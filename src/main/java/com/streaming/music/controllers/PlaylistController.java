package com.streaming.music.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.streaming.music.dto.PlaylistData;
import com.streaming.music.service.PlaylistService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.persistence.EntityNotFoundException;
import java.util.List;

@CrossOrigin
@RestController
public class PlaylistController {
    @Resource(name = "playlist_service")
    private PlaylistService playlistService;

    @PostMapping("/playlist")
    public ResponseEntity<Object> createPlaylist(@RequestParam("json") String data){
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        Gson gson = builder.create();
        PlaylistData playlistData = gson.fromJson(data, PlaylistData.class);
        return ResponseEntity.ok().body( playlistService.createPlaylist(playlistData));
    }

    @GetMapping("/playlist")
    public ResponseEntity<Object> getPlaylist(@RequestParam("id") int id){
        PlaylistData playlistData;
        try{
            playlistData=playlistService.getPlaylistById(id);
        }catch (EntityNotFoundException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok().body(playlistData);
    }
    @PatchMapping("/playlist")
    public ResponseEntity<Object> updatePlaylist(@RequestParam("json") String data){
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        Gson gson = builder.create();
        PlaylistData playlistData = gson.fromJson(data, PlaylistData.class);
        playlistService.updatePlaylist(playlistData);
        return ResponseEntity.ok().body(playlistData);
    }
    @DeleteMapping("playlist")
    public ResponseEntity<Object> deletePlaylist(@RequestParam("id") int id){
        try{
            playlistService.deleteById(id);
        }catch (EntityNotFoundException ex){
            return ResponseEntity.notFound().build();
        }
        return  ResponseEntity.ok().body(1);
    }

    @GetMapping("/allPlaylist")
    public ResponseEntity<Object> getAll(@RequestParam("login") String login){
        List<PlaylistData> playlistData = playlistService.getAllByUser(login);
        return ResponseEntity.ok().body(playlistData);
    }


}
