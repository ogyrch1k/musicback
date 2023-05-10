package com.streaming.music.repository;

import com.streaming.music.model.Executor;
import com.streaming.music.model.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GenreRepository extends JpaRepository<Genre, Integer> {
    Optional<Genre> getGenreById(int id);
    Genre findGenreByNameOfGenre(String genre);
    Optional<Genre> getGenreByNameOfGenre(String name);
    int deleteById(int id);
    int deleteByNameOfGenre(String name);
    List<Genre> findAll();

    List<Genre> getAllByGenresExecutor(int idOfExecutor);

    List<Genre> getByGenresExecutor(Executor executor);
//    List<Genre> getAll();
    //List<Genre> getAllByGenresExecutor(Executor executor);
}
