package com.streaming.music.service;

import com.streaming.music.dto.GenreData;
import com.streaming.music.model.Genre;
import com.streaming.music.repository.ExecutorRepository;
import com.streaming.music.repository.GenreRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service("genre_service")
public class MainGenreService implements GenreService{

    private final GenreRepository genreRepository;
    private final ExecutorRepository executorRepository;
    public MainGenreService(GenreRepository genreRepository, ExecutorRepository executorRepository) {
        this.genreRepository = genreRepository;
        this.executorRepository = executorRepository;
    }

    @Override
    public GenreData getGenreById(int id) {
        return populateGenreData(genreRepository.getGenreById(id).orElseThrow(()->new EntityNotFoundException("Genre not found")));
    }

    @Override
    public GenreData getGenreByName(String name) {
        return populateGenreData(genreRepository.getGenreByNameOfGenre(name).orElseThrow(()->new EntityNotFoundException("Genre not found")));
    }

    @Override
    public int deleteById(int id) {
        return genreRepository.deleteById(id);
    }

    @Override
    public GenreData saveGenre(GenreData genreData) {
        Genre genre =populateGenreEntity(genreData);
        return populateGenreData(genreRepository.save(genre));
    }

    @Override
    public GenreData updateGenre(GenreData genreData) {
        Genre genre =populateGenreEntity(genreData);
        return populateGenreData(genreRepository.save(genre));
    }

    @Override
    public List<GenreData> getAllGenre() {
        List<Genre> genres = genreRepository.findAll();
        List<GenreData> genreData= new ArrayList<>();
        for(Genre g:genres){
            genreData.add(populateGenreData(g));
        }
        return genreData;
    }

//    @Override
//    public List<GenreData> getAll() {
//        List<GenreData> genreData=new ArrayList<>();
//        List<Genre> genres = genreRepository.getAll();
//        for (Genre w:
//             genres) {
//            genreData.add(populateGenreData(w));
//        }
//        return genreData;
//    }

//    @Override
//    public List<GenreData> getAllByGenresExecutor(int executor) {
//        List<GenreData> genreData=new ArrayList<>();
//        List<Genre> genres = genreRepository.getAllByGenresExecutor(executorRepository.getById(executor));
//        for (Genre w:
//                genres) {
//            genreData.add(populateGenreData(w));
//        }
//        return genreData;
//    }

    private GenreData populateGenreData(final Genre genre){
        GenreData genreData = new GenreData();
        genreData.setNameOfGenre(genre.getNameOfGenre());
        genreData.setId(genre.getId());
        return genreData;
    }

    private Genre populateGenreEntity(GenreData genreData){
        Genre genre = new Genre();
        genre.setNameOfGenre(genreData.getNameOfGenre());
        try{
            genre.setId(genreData.getId());
        }catch (Exception ignored){

        }
        return genre;
    }


}
