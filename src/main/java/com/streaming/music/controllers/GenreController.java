package com.streaming.music.controllers;

import com.streaming.music.dto.GenreData;
import com.streaming.music.service.GenreService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@CrossOrigin
@RestController
public class GenreController {
    @Resource(name = "genre_service")
    private GenreService genreService;

    @PostMapping("/genre")
    public ResponseEntity<Object> addGenre(@RequestParam("nameOfGenre") String nameOfGenre){
        GenreData genreData=new GenreData();
        genreData.setNameOfGenre(nameOfGenre);
        return ResponseEntity.ok().body(genreService.saveGenre(genreData));
    }

    @GetMapping("/genre")
    public ResponseEntity<Object> getGenre(@RequestParam("id") int id){
        return ResponseEntity.ok().body(genreService.getGenreById(id));
    }

    @DeleteMapping("/genre")
    public ResponseEntity<Object> delGenre(@RequestParam("id") int id){
        return ResponseEntity.ok().body(genreService.deleteById(id));
    }

    @GetMapping("/allGenres")
    public ResponseEntity<Object> allGenre(){
        return ResponseEntity.ok().body(genreService.getAllGenre());
    }

//    @GetMapping("/allgenreExecutor")
//    public ResponseEntity<Object> allgenreExecutor(@RequestParam("idExecutor") int id){
//        return ResponseEntity.ok().body(genreService.getAllByGenresExecutor(id));
//    }
}
