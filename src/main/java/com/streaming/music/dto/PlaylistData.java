package com.streaming.music.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PlaylistData {
    private Integer id;
    private String nameOfPlaylist;
    private String descriptionText;
}
