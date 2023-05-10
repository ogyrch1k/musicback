package com.streaming.music.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.streaming.music.Utilities.Hash;
import com.streaming.music.auth.AuthRequest;
import com.streaming.music.auth.AuthResponse;
import com.streaming.music.dto.UserData;
import com.streaming.music.jwt.JwtProvider;
import com.streaming.music.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
@CrossOrigin
@RestController
public class AuthController {
    @Autowired
    private UserService userService;
    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private Hash hashpassword;



    @PostMapping("/register")
    public ResponseEntity<Object> registerUser(@RequestParam("json") String data) throws IOException, NoSuchAlgorithmException{
        System.out.println("qwe");
        ObjectMapper mapper = new ObjectMapper();
        UserData o = mapper.readValue(data,UserData.class);
        try{
            UserData u = userService.getUserByLogin(o.getLogin());
        }catch (EntityNotFoundException e){
            UserData u = new UserData();
            u.setSold(o.getSold());
            u.setHashOfPassword(o.getHashOfPassword());
            u.setNick(o.getNick());
            u.setLogin(o.getLogin());
//            try{
//                String resultFilename =userService.savePicture(file);
//                u.setPathToAvatar(uploadPath + "/" + resultFilename);
//            }catch (IOException ex){
//                System.out.println("test");
//                return ResponseEntity.badRequest().body(ex.getMessage());
//            }
            userService.saveUser(u,"ROLE_USER");
            return ResponseEntity.ok("ok");

        }
        return ResponseEntity.badRequest().body("login used");
    }

    @PostMapping("/registerAdmin")
    public ResponseEntity<Object> registerAdmin(@RequestParam("json") String data) throws IOException, NoSuchAlgorithmException{
        System.out.println("qwe");
        ObjectMapper mapper = new ObjectMapper();
        UserData o = mapper.readValue(data,UserData.class);
        try{
            UserData u = userService.getUserByLogin(o.getLogin());
        }catch (EntityNotFoundException e){
            UserData u = new UserData();
            u.setSold(o.getSold());
            u.setHashOfPassword(o.getHashOfPassword());
            u.setNick(o.getNick());
            u.setLogin(o.getLogin());
//            try{
//                String resultFilename =userService.savePicture(file);
//                u.setPathToAvatar(uploadPath + "/" + resultFilename);
//            }catch (IOException ex){
//                return ResponseEntity.badRequest().body(ex.getMessage());
//            }
            userService.saveUser(u,"ROLE_ADMIN");
            return ResponseEntity.ok("ok");

        }
        return ResponseEntity.ok().body("login used");
    }

    @GetMapping("/getSold")
    public ResponseEntity<Object> getsold(@RequestParam("login") String login) throws NoSuchAlgorithmException {
          return ResponseEntity.ok().body(hashpassword.getSold(login));
    }
    @PostMapping("/auth")
    public ResponseEntity<Object> auth(@RequestParam("auth") String request) throws NoSuchAlgorithmException, JsonProcessingException {
        UserData user;
        ObjectMapper mapper = new ObjectMapper();
        AuthRequest o = mapper.readValue(request,AuthRequest.class);
        try{
            user = userService.findByHashOfPasswordAndLogin(o.getLogin(),o.getPassword());
        }
        catch (EntityNotFoundException e){
            return ResponseEntity.badRequest().body("user not found");
        }
        String token = jwtProvider.generateToken(user.getLogin());
        //System.out.println(token.length());
        return ResponseEntity.ok(new AuthResponse(token));
    }
}