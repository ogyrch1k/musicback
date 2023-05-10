package com.streaming.music.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.streaming.music.dto.UserData;
import com.streaming.music.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.annotation.Resource;
import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

@CrossOrigin
@RestController
//@RequestMapping("/users")
public class UserController {

    @Resource(name = "userService")
    private UserService userService ;

    @Value("${upload.path.image}")
    private String uploadPath;

    @GetMapping("/users")
    public ResponseEntity<Object> getUser(@RequestParam("login")String login){
        UserData user;
        try{
          user= userService.getUserByLogin(login);
        }
        catch (EntityNotFoundException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok().body(user);
    }

    @PatchMapping("/users")
    public ResponseEntity<Object> updateUser(@RequestParam("json") String data, @RequestParam("file") MultipartFile file) throws NoSuchAlgorithmException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        UserData o = mapper.readValue(data,UserData.class);
        UserData u;
        try{
             u = userService.getUserByLogin(o.getLogin());
        }catch (EntityNotFoundException e){
            return ResponseEntity.ok().body("user does not exist");
        }
        u.setSold(o.getSold());
        u.setHashOfPassword(o.getHashOfPassword());
        u.setNick(o.getNick());
        u.setLogin(o.getLogin());
        try{
            String resultFilename =userService.savePicture(file);
            u.setPathToAvatar(uploadPath + "/" + resultFilename);
        }catch (IOException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        userService.updateUser(u);
        return ResponseEntity.ok("ok");
    }

    @DeleteMapping("/users")
    public ResponseEntity<Object> deleteUser(@RequestParam("login")String login) {
        UserData u;
        try{
            u = userService.getUserByLogin(login);
        }catch (EntityNotFoundException e){
            return ResponseEntity.ok().body(e.getMessage());
        }
        userService.deleteUser(login);
        return ResponseEntity.ok().body("User deleted");
    }
}
