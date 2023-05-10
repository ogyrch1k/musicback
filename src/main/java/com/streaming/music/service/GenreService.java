package com.streaming.music.service;

import com.streaming.music.dto.GenreData;
import com.streaming.music.model.Executor;
import com.streaming.music.model.Genre;

import java.util.List;

public interface GenreService {
    GenreData getGenreById(int id);
    GenreData getGenreByName(String name);
    int deleteById(int id);
    GenreData saveGenre(GenreData genreData);
    GenreData updateGenre(GenreData genreData);

    List<GenreData> getAllGenre();
}
