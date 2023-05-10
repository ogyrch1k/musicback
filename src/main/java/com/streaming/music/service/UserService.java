package com.streaming.music.service;

import com.streaming.music.dto.UserData;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

public interface UserService{
    UserData saveUser(UserData user,String role);
    int deleteUser(final String login) ;
    UserData getUserById(final Integer userId);
    UserData updateUser(final UserData user);
    UserData findByHashOfPasswordAndLogin(String sold, String hashOfPassword);
    UserData getUserByLogin(String login) ;
    String savePicture(MultipartFile file)throws IOException;
}
