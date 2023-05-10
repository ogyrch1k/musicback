package com.streaming.music.service;

import com.streaming.music.dto.NewsData;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface NewsService {
    NewsData saveNews(NewsData newsData);
    int deleteNewsById(Integer id);
    NewsData getNewsById(Integer id);
    List<NewsData> getNewsByExecutor(Pageable pageable, int id);
    NewsData updateNews(NewsData newsData);
    String savePicture(MultipartFile file) throws IOException, NoSuchAlgorithmException;
    NewsData findNewsByText(String text);
    long getCount();
    int addNewsToExecutor(int idOfExecutor,int... idOfNews);
    int removeNewsFromExecutor(int idOfExecutor,int... idOfNews);

    List<NewsData> getAll(Pageable pageable);

}
