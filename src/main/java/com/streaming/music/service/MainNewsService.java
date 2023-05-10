package com.streaming.music.service;
import com.streaming.music.dto.NewsData;
import com.streaming.music.model.Executor;
import com.streaming.music.model.News;
import com.streaming.music.repository.ExecutorRepository;
import com.streaming.music.repository.NewsRepository;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import static com.streaming.music.Utilities.FilesUtil.saveMultipartFile;
import static com.streaming.music.Utilities.Hash.hashOfMultipartFile;

@Service("news_service")
@Log
public class MainNewsService implements NewsService{

    @Value("${upload.path.image}")
    private String uploadPath;
    private final NewsRepository newsRepository;
    private final ExecutorRepository executorRepository;

    public MainNewsService(NewsRepository newsRepository, ExecutorRepository executorRepository) {
        this.newsRepository = newsRepository;
        this.executorRepository =executorRepository;
    }

    @Override
    public NewsData saveNews(NewsData newsData){
        News news = populateNewsEntity( newsData);
        log.info("Added news"+ news.getText().substring(0,10));
        return populateNewsDataEntity(newsRepository.save(news));
    }

    @Override
    public int deleteNewsById(Integer id) {
        return newsRepository.deleteNewsById(id).orElseThrow(()->new EntityNotFoundException("News not found"));
    }

    @Override
    public NewsData getNewsById(Integer id) {
        return populateNewsDataEntity(newsRepository.getNewsById(id).orElseThrow(()->new EntityNotFoundException("News not found")));
    }

    @Override
    public List<NewsData> getNewsByExecutor(Pageable pageable, int id) {
        Executor executor= executorRepository.getById(id);
        Page<News> newsS = newsRepository.getAllByExecutor(pageable, executor);
        List<NewsData> news =new ArrayList<>();
        for(News n:newsS){
            news.add(populateNewsDataEntity(n));
        }
        return news;
    }


    @Override
    public NewsData updateNews(NewsData newsData) {
        News news = populateNewsEntity(newsData);
        return populateNewsDataEntity(newsRepository.save(news));
    }

    @Override
    public String savePicture(MultipartFile file) throws IOException, NoSuchAlgorithmException {
        String hashName = hashOfMultipartFile(file);
        String resultFilename = saveMultipartFile(file,uploadPath,hashName);
        return resultFilename;
    }

    @Override
    public NewsData findNewsByText(String text) {
        return populateNewsDataEntity(newsRepository.getNewsByText(text).orElseThrow(()->new EntityNotFoundException()));
    }

    @Override
    public long getCount() {
        return newsRepository.count();
    }

    @Override
    public int addNewsToExecutor(int idOfExecutor, int... idOfNews) {
        Executor executor = executorRepository.getById(idOfExecutor);
        List<News> news=newsRepository.getAllByExecutor(idOfExecutor);
        for(int id:idOfNews){
            news.add(newsRepository.getById(id));
        }
        executor.setNews(news);
        executorRepository.save(executor);
        return 1;
    }

    @Override
    public int removeNewsFromExecutor(int idOfExecutor, int... idOfNews) {
        Executor executor = executorRepository.getById(idOfExecutor);
        List<News> news=newsRepository.getAllByExecutor(idOfExecutor);
        for(int id:idOfNews){
            news.remove(newsRepository.getById(id));
        }
        executor.setNews(news);
        executorRepository.save(executor);
        return 1;
    }

    @Override
    public List<NewsData> getAll(Pageable pageable) {
        Page<News> news=newsRepository.findAll(pageable);
        List<NewsData> newsData = new ArrayList<>();
        for(News w:news){
            newsData.add(populateNewsDataEntity(w));
        }
        return newsData;
    }

    private News populateNewsEntity(NewsData newsData){
        News news;
        news = newsRepository.getNewsById(newsData.getId()).orElseThrow(()->new EntityNotFoundException("News not found"));
        news.setText(newsData.getText());
        news.setDateOfPost(newsData.getDateOfPost());
        news.setHashOfImage(newsData.getHashOfImage());
        try{
            news.setId(newsData.getId());

        }
        catch (Exception exeption){

        }
        return news;
    }

    private NewsData populateNewsDataEntity(News news){
        NewsData newsData = new NewsData();
        newsData.setId(news.getId());
        newsData.setText(news.getText());
        newsData.setDateOfPost(news.getDateOfPost());
        newsData.setHashOfImage(news.getHashOfImage());
        return newsData;
    }

/*    @Override
    public List<NewsData> getNewsByExecutor(int id) {
        List<NewsData> newsData= new ArrayList<>();
        List<News> news = newsRepository.getAllByExecutor(executorRepository.getById(id)).orElseThrow(()-> new EntityNotFoundException("Executor not found"));
        return null;
    }*/

}
