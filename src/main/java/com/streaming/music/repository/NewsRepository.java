package com.streaming.music.repository;

import com.streaming.music.model.News;
import com.streaming.music.model.Executor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;;

public interface NewsRepository extends JpaRepository<News,Integer> {
    Optional<Integer> deleteNewsById(Integer id);
    Page<News> findAll(Pageable pageable);
    Optional<News> getNewsById(Integer id);
    Optional<News> getNewsByText(String text);
    Page<News> getAllByExecutor(Pageable pageable, Executor executor);
    List<News> getAllByExecutor(int idOfExecutor);
}
