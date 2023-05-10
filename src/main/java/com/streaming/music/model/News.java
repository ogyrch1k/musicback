package com.streaming.music.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "news")
public class News {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "date_of_post")
    private LocalDate dateOfPost;

    @Lob
    @Column(name = "text")
    private String text;

    @Column(name = "hash_of_image", length = 200)
    private String hashOfImage;

   // @ManyToMany
    //@JoinTable(name = "news_of_executor",
           // joinColumns = @JoinColumn(name = "id_of_news"),
           // inverseJoinColumns = @JoinColumn(name = "id_of_executor"))
   // private List<Executor> executors =new ArrayList<>();

    //@ManyToOne
    //@JoinTable(name="executor_of_news" )
    //private Executor executorOfNews;

    @ManyToMany
    @ToString.Exclude
    private List<Executor> executor= new ArrayList<>();


/*    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getDateOfPost() {
        return dateOfPost;
    }

    public void setDateOfPost(LocalDate dateOfPost) {
        this.dateOfPost = dateOfPost;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getPathOfImage() {
        return pathOfImage;
    }

    public void setPathOfImage(String pathOfImage) {
        this.pathOfImage = pathOfImage;
    }

    public Executor getExecutor() {
        return (Executor) executorsOfNews;
    }

    public void setExecutor(Executor executor) {
        this.executorsOfNews = (List<Executor>) executor;
    }*/

}