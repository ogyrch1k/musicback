
package com.streaming.music.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.UnsupportedTagException;
import com.streaming.music.dto.ExecutorData;
import com.streaming.music.dto.NewsData;
import com.streaming.music.service.NewsService;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.persistence.EntityNotFoundException;
import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.List;

import static com.streaming.music.Utilities.FilesUtil.getFile;

@CrossOrigin
@RestController
public class NewsController {
    @Resource(name="news_service")
    private NewsService newsService;
    @Value("${upload.path.image}")
    private String uploadPath;
    @PostMapping("/news")
    public ResponseEntity<Object> createNews(@RequestParam("json") String data, @RequestParam("file") MultipartFile file)throws IOException, InvalidDataException, UnsupportedTagException {
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        Gson gson = builder.create();
        NewsData newsData = gson.fromJson(data,NewsData.class);
        NewsData n = new NewsData();
        n.setText(newsData.getText());
        n.setDateOfPost(newsData.getDateOfPost());
        try{
            String resultFileName = newsService.savePicture(file);
            n.setHashOfImage(uploadPath + "/" + resultFileName);
        }catch (IOException ex){
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        int i = newsService.saveNews(n).getId();
        return ResponseEntity.ok(i);
    }

    @DeleteMapping("/news")
    public ResponseEntity<Object> deleteNews(@RequestParam("id") int id){
        NewsData n;
        try{
            n=newsService.getNewsById(id);
        }catch (EntityNotFoundException ex){
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
        newsService.deleteNewsById(id);
        return ResponseEntity.ok().body("News deleted");
    }

    @PatchMapping("/news")
    public ResponseEntity<Object> updateNews(@RequestParam("json") String data, @RequestParam("file") MultipartFile file)throws IOException, InvalidDataException, UnsupportedTagException {
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        Gson gson = builder.create();
        NewsData newsData = gson.fromJson(data, NewsData.class);
        NewsData n;
        try{
            n = newsService.getNewsById(newsData.getId());
        }catch (EntityNotFoundException ex){
            return ResponseEntity.ok().body("News does not exist");
        }
        n.setId(newsData.getId());
        n.setText(newsData.getText());
        n.setDateOfPost(newsData.getDateOfPost());
        try{
            String resultFileName = newsService.savePicture(file);
            n.setHashOfImage(uploadPath + "/" + resultFileName);
        }catch (IOException | NoSuchAlgorithmException ex){
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
        newsService.updateNews(n);
        return ResponseEntity.ok("ok");
    }
    @GetMapping("/news")
    public ResponseEntity<Object> getNews(@RequestParam("id") int id){
        NewsData newsData = new NewsData();
        try{
            newsData = newsService.getNewsById(id);
        }catch (EntityNotFoundException ex){
            return ResponseEntity.badRequest().body("News not found");
        }
        return ResponseEntity.ok().body(newsData);
    }
    @GetMapping("/executorNews")
        public ResponseEntity<Object> getExecutorNews(@RequestParam("executors_id") int id,@RequestParam("pageNumber") int pageNumber,@RequestParam("pageSize") int pageSize){
            Pageable pageable = PageRequest.of(pageNumber,pageSize);
            long totalPages;
            List<NewsData> newsData;
            try{
                newsData = newsService.getNewsByExecutor(pageable,id);
                totalPages = newsService.getCount();
            }catch (EntityNotFoundException ex){
                return ResponseEntity.badRequest().body(ex.getMessage());
            }
        return  ResponseEntity.ok().header("X-Total-Count", String.valueOf(totalPages)).body(newsData);

        }
    @GetMapping(value = "/imageNews")
    public ResponseEntity<Object> getImage(@RequestParam ("id") int id) throws IOException {
        NewsData news = new NewsData();
        try {
            news = newsService.getNewsById(id);
        }catch (EntityNotFoundException e){
            ResponseEntity.ok().body(e.getMessage());
        }
        byte[] fileContent = FileUtils.readFileToByteArray(new File(getFile(news.getHashOfImage(),uploadPath)));
        String encodedString = Base64.getEncoder().encodeToString(fileContent);
        return ResponseEntity.ok().body(encodedString);
    }
    @PostMapping("/newsToExecutor")
    public ResponseEntity<Object> addNewsToExecutor(@RequestParam("idOfExecutor") int idOfExecutor, @RequestParam("idOfNews") int idOfNews){
        return ResponseEntity.ok().body(newsService.addNewsToExecutor(idOfExecutor,idOfNews));
    }

    @PostMapping("/newsFromExecutor")
    public ResponseEntity<Object> removeNewsFromExecutor(@RequestParam("idOfExecutor") int idOfExecutor, @RequestParam("idOfNews") int idOfNews){
        return ResponseEntity.ok().body(newsService.removeNewsFromExecutor(idOfExecutor,idOfNews));
    }
    @GetMapping("/allNewsPage")
    public ResponseEntity<Object> getAllNews(@RequestParam("pageNumber") int pageNumber,@RequestParam("pageSize") int pageSize){
        Pageable pageable = (Pageable) PageRequest.of(pageNumber,pageSize);
        List<NewsData> newsData;
        long totalPages;
        try{
            newsData = newsService.getAll(pageable);
            totalPages = newsService.getCount();
        }catch (EntityNotFoundException ex){
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
        return  ResponseEntity.ok().header("X-Total-Count", String.valueOf(totalPages)).body(newsData);
    }

}
