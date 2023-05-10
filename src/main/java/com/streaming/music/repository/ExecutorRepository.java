package com.streaming.music.repository;

import com.streaming.music.model.Album;
import com.streaming.music.model.Executor;
import com.streaming.music.model.Track;
import com.streaming.music.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ExecutorRepository extends JpaRepository<Executor, Integer> {
    Optional<Integer> deleteExecutorById(Integer id);
    Optional<Executor> getExecutorByNameOfExecutor(String name);
    Page<Executor> findAll(Pageable pageable);
    List<Executor> getAllByExecutorsAlbums(Album album);
    Optional< Executor> getByNameOfExecutor(String name);

    List<Executor> getAllByUsersExecutors(User user);

    Optional<Executor> getByTracksOfExecutors(Track track);
}