package com.streaming.music.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class NewsData {
    private Integer id;
    private LocalDate dateOfPost;
    private String text;
    private String hashOfImage;
}
