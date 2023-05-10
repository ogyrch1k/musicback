package com.streaming.music.repository;

import com.streaming.music.model.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Integer> {
    Optional<Integer> deleteByLogin(String login);
    Optional<User> findByHashOfPasswordAndLogin(String hashOfPassword, String sold);
    Optional<User> getUserByLogin(String login);
    Optional<List<User>> getAllByUsersExecutors(Executor executor);
    Optional<List<User>> getAllByUsersAlbums(Album album);
    Optional<User> getById(int id);

}
