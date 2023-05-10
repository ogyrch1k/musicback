package com.streaming.music.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.streaming.music.dto.TrackData;
import com.streaming.music.dto.UserData;
import com.streaming.music.model.Genre;
import com.streaming.music.service.ExecutorService;
import com.streaming.music.service.TrackService;
import com.streaming.music.service.UserService;
import static com.streaming.music.Utilities.FilesUtil.getFile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import lombok.extern.java.Log;
import javax.annotation.Resource;
import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
@CrossOrigin
@Log
@RestController
public class TrackController {

    @Resource(name="trackService")
    private TrackService trackService;
    @Resource(name="executor_service")
    private ExecutorService executorService;
    @Resource(name = "userService")
    private UserService userService ;
    @Value("${upload.path.track}")
    private String uploadPath;

    @RequestMapping(path="/track", method = RequestMethod.POST)
    public ResponseEntity<Object> addTrack(@RequestParam("json") String data, @RequestParam("file") MultipartFile file) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        TrackData trackData = mapper.readValue(data, TrackData.class);
        TrackData t = new TrackData();
        try{
            String resultFilename =trackService.saveTrack(file);
            t.setHashOfTrack(resultFilename);
        }catch (IOException | NoSuchAlgorithmException ex){
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
        t.setNameOfTrack(trackData.getNameOfTrack());
        trackService.addTrack(t);
        return ResponseEntity.ok().body(t);
    }

    @RequestMapping(path="/track", method = RequestMethod.PATCH)
    public ResponseEntity<Object> updateTrack(@RequestParam("json") String data, @RequestParam("file") MultipartFile file) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        TrackData trackData = mapper.readValue(data, TrackData.class);
        TrackData t ;
        try{
            t = trackService.getTrackById(trackData.getId());
        }catch (EntityNotFoundException e){
            return ResponseEntity.ok().body("track does not exist");
        }
        try{
            String resultFilename =trackService.saveTrack(file);
            t.setHashOfTrack(resultFilename);
        }catch (IOException | NoSuchAlgorithmException ex){
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
        t.setNameOfTrack(trackData.getNameOfTrack());
        trackService.addTrack(t);
        return ResponseEntity.ok().body(t);
    }

    @RequestMapping(path="/track", method = RequestMethod.GET)
    public ResponseEntity<Object> getTrack(@RequestParam("id") int id){
        TrackData trackData;
        try{
             trackData = trackService.getTrackById(id);
        }catch (EntityNotFoundException ex){
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
        return  ResponseEntity.ok().body(trackData);
    }
    @RequestMapping(path="/tracks", method = RequestMethod.GET)
    public ResponseEntity<Object> getTracks(@RequestParam("id") int[] id){
        List<TrackData> trackData;
        try{
            trackData = trackService.getTracksById(id);
        }catch (EntityNotFoundException ex){
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
        return  ResponseEntity.ok().body(trackData);
    }

    @RequestMapping(path="/track", method = RequestMethod.DELETE)
    public ResponseEntity<Object> deleteTrack(@RequestParam("id") int id){
        TrackData t;
        try{
            t= trackService.getTrackById(id);
        }catch (EntityNotFoundException ex){
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
        trackService.deleteTrackById(id);
        return ResponseEntity.ok().body("Track deleted");
    }

//    @RequestMapping(path="/track", method = RequestMethod.GET)
//    public ResponseEntity<Object> getTrackByExecutor(@RequestParam("id") int id,@RequestParam("executor") int executor){
//        TrackData trackData;
//        try{
//            trackData = trackService.getTrackByIdAndTrackOfExecutorsIn(id,new ArrayList<Executor>((Collection<? extends Executor>) executorService.populateExecutorEntity(executorService.getExecutorById(executor))));
//        }catch (EntityNotFoundException ex){
//            return ResponseEntity.badRequest().body(ex.getMessage());
//        }
//        return  ResponseEntity.ok().body(trackData);
//    }
    @RequestMapping(path="/alltracks", method = RequestMethod.GET)
    public ResponseEntity<Object> getAllTracks(@RequestParam("pageNumber") int pageNumber,@RequestParam("pageSize") int pageSize){
        Pageable contactsPageable = (Pageable) PageRequest.of(pageNumber, pageSize);
        List<TrackData> trackData;
        try{
            trackData = trackService.getAll(contactsPageable);
        }catch (EntityNotFoundException ex){
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
        return  ResponseEntity.ok().body(trackData);
    }

    @GetMapping("/trackAlbum")
    public ResponseEntity<Object> getTrackAlbum(@RequestParam("idAlbum") int id){
        List<TrackData> trackData =trackService.getAllByTrackOfAlbum(id);
        return ResponseEntity.ok().body(trackData);
    }

    @GetMapping("/trackPlaylist")
    public ResponseEntity<Object> getTrackPlaylist(@RequestParam("idPlaylist") int id){
        List<TrackData> trackData =trackService.getAllByTrackOfPlaylist(id);
        return ResponseEntity.ok().body(trackData);
    }


    @RequestMapping(value = "/getSong", method = RequestMethod.GET, produces = {
        MediaType.APPLICATION_OCTET_STREAM_VALUE })
    public ResponseEntity playAudio(HttpServletRequest request, HttpServletResponse response, @RequestParam("id")  int id) throws FileNotFoundException {
        TrackData trackData;
        try{
            trackData = trackService.getTrackById(id);
        }catch (EntityNotFoundException ex){
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
        String file = getFile(trackData.getHashOfTrack(),uploadPath);
        log.info(file);
        long length = new File(file).length();
        InputStreamResource inputStreamResource = new InputStreamResource( new FileInputStream(file));
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentLength(length);
        httpHeaders.setCacheControl(CacheControl.noCache().getHeaderValue());
        return new ResponseEntity(inputStreamResource, httpHeaders, HttpStatus.OK);
    }

    @GetMapping("/getAllTrack")
    public ResponseEntity<Object> allrtack(){
        return ResponseEntity.ok().body(trackService.getAllTrack());
    }

    @GetMapping("/getGenreTrack")
    public ResponseEntity<Object> genretrack(@RequestParam("id") int id)
    {
        Genre genre = trackService.getGenreOfTrack(id);
        return ResponseEntity.ok().body(genre);
    }


}
