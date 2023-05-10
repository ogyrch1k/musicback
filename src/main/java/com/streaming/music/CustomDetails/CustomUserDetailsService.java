package com.streaming.music.CustomDetails;

import com.streaming.music.dto.UserData;
import com.streaming.music.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserService userService;

    @Override
    public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserData userData = null;

            userData = userService.getUserByLogin(username);

        return CustomUserDetails.fromUserEntityToCustomUserDetails(userData);
    }
}
