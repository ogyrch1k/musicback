package com.streaming.music.dto;

import com.streaming.music.model.Role;
import lombok.Data;

@Data
public class UserData {
    public Integer id;
    public String nick;
    public String pathToAvatar;
    public String login;
    public String hashOfPassword;
    public String sold;
    public Role role;
}
