package com.streaming.music.dto;

import com.streaming.music.model.Album;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class ExecutorData {
    private Integer id;
    private String nameOfExecutor;
    private String hashOfAvatar;
    private LocalDate dateOfFormation;
    private LocalDate disbandmentDate;
    private String descriptionExt;
}
