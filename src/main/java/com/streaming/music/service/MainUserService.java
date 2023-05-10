package com.streaming.music.service;

import com.streaming.music.dto.UserData;
import com.streaming.music.model.Role;
import com.streaming.music.model.User;
import com.streaming.music.repository.RoleRepository;
import com.streaming.music.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service("userService")
@Transactional
public class MainUserService implements UserService{

    @Value("${upload.path.image}")
    private String uploadPath;
    
    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    public MainUserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public UserData saveUser(UserData user,String role) {
        Role userRole = roleRepository.findByName(role);
        User userModel = populateUserEntity(user);
        userModel.setRole(userRole);
        System.out.println(userModel);
        return populateUserData(userRepository.save(userModel));
    }

    @Override
    public int deleteUser(String login) {
        return  userRepository.deleteByLogin(login).orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    @Override
    public UserData getUserById(Integer userId) {
        return populateUserData( userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found")));
    }

    @Override
    public UserData updateUser(UserData userData) {
        User oldUser = populateUserEntity(userData);
        User user =populateUserEntityForUpdate(userData,oldUser);
        return populateUserData(userRepository.save(user));
    }

    private User populateUserEntityForUpdate(UserData userData,User user){
        Role userRole = roleRepository.findByName("ROLE_USER");
        user.setRole(userRole);
        user.setLogin(userData.getLogin());
        user.setNick(userData.getNick());
        user.setPathToAvatar(userData.getPathToAvatar());
        user.setHashOfPassword(userData.getHashOfPassword());
        user.setSold(userData.getSold());
        return user;
    }

    @Override
    public UserData findByHashOfPasswordAndLogin(String sold, String hashOfPassword) {
        return populateUserData( userRepository.findByHashOfPasswordAndLogin(hashOfPassword,sold).orElseThrow(() -> new EntityNotFoundException("User not found")));
    }
    @Async
    @Override
    public UserData getUserByLogin(String login) {
            return populateUserData( userRepository.getUserByLogin(login).orElseThrow(() -> new EntityNotFoundException("User not found")));
    }


    private UserData populateUserData(final User user){
        UserData userData = new UserData();
        userData.setLogin(user.getLogin());
        userData.setNick(user.getNick());
        userData.setPathToAvatar(user.getPathToAvatar());
        userData.setHashOfPassword(user.getHashOfPassword());
        userData.setSold(user.getSold());
        userData.setRole(user.getRole());
        userData.setId(user.getId());
        return  userData;
    }


    private User populateUserEntity(UserData userData){
        User user = new User();
        user.setLogin(userData.getLogin());
        user.setNick(userData.getNick());
        user.setPathToAvatar(userData.getPathToAvatar());
        user.setHashOfPassword(userData.getHashOfPassword());
        user.setSold(userData.getSold());
        try{
            user.setId(userData.getId());
        }catch (Exception ignored){

        }
        return user;
    }

    public String savePicture(MultipartFile file) throws IOException {
        String resultFilename = null;
        if (file != null && !file.getOriginalFilename().isEmpty()) {
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }
            String uuidFile = UUID.randomUUID().toString();
            resultFilename = uuidFile + "." + file.getOriginalFilename();
            file.transferTo(new File(uploadPath + "/" + resultFilename));
            return resultFilename;
       }
        return resultFilename;
    }
}
