package com.streaming.music.auth;

import lombok.Data;

@Data
public class RegistrationRequest {
    public String login;
    public String nick;
    public String password;
}
