package com.streaming.music.service;

 import com.streaming.music.dto.ExecutorData;
import com.streaming.music.model.Executor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
 import java.security.NoSuchAlgorithmException;
 import java.util.List;

public interface ExecutorService {
    ExecutorData saveExecutor(ExecutorData executorData);
    int deleteExecutor(Integer id);
    ExecutorData getExecutorById(Integer id);
    ExecutorData updateExecutor(ExecutorData executorData);
    String savePicture(MultipartFile file) throws IOException, NoSuchAlgorithmException;
    ExecutorData findExecutorByNameOfExecutor(String name);
    int addTrackToExecutor(int idOfOExecutor, int idOftrack);
    Executor populateExecutorEntity(ExecutorData executorData);
    List<ExecutorData> getAll(Pageable pageable);
    List<ExecutorData> getAllExecutor();
    int getCountPages(Pageable pageable);
    int addAlbumToExecutor(int idOfAlbum, int idOfExecutor);
    int addGenresToExecutor(int idOfExecutor,int... idOfGenres);
    int removeGenresFromExecutor(int idOfExecutor,int... idOfGenres);
    long getCount();
    ExecutorData getExecutorByName(String name);
    int addExecutorToUser(int idExecutor,int idUser);

    int removeExecutorFromUser(int idExecutor,int idUser);


}
