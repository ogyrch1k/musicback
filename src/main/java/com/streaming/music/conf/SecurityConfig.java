package com.streaming.music.conf;

import com.streaming.music.jwt.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtFilter jwtFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .csrf().disable().cors().and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/admin/*").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET,"/users").hasAnyRole("USER","ADMIN")
                .antMatchers(HttpMethod.DELETE,"/users").hasAnyRole("USER","ADMIN")
                .antMatchers(HttpMethod.PATCH,"/users").hasAnyRole("USER","ADMIN")
                .antMatchers(HttpMethod.GET,"/executor").hasAnyRole("USER","ADMIN")
                .antMatchers(HttpMethod.POST,"/executor").hasRole("ADMIN")
                .antMatchers(HttpMethod.PATCH,"/executor").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE,"/executor").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET,"/track").hasAnyRole("USER","ADMIN")
                .antMatchers(HttpMethod.POST,"/track").hasRole("ADMIN")
                .antMatchers(HttpMethod.PATCH,"/track").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE,"/track").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET,"/album").hasAnyRole("USER","ADMIN")
                .antMatchers(HttpMethod.POST,"/album").hasRole("ADMIN")
                .antMatchers(HttpMethod.PATCH,"/album").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE,"/album").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET,"/genre").hasAnyRole("USER","ADMIN")
                .antMatchers(HttpMethod.POST,"/genre").hasRole("ADMIN")
                .antMatchers(HttpMethod.PATCH,"/genre").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE,"/genre").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET,"/news").hasAnyRole("USER","ADMIN")
                .antMatchers(HttpMethod.POST,"/news").hasRole("ADMIN")
                .antMatchers(HttpMethod.PATCH,"/news").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE,"/news").hasRole("ADMIN")
                .antMatchers(HttpMethod.POST,"/trackToExecutor").hasRole("ADMIN")
                .antMatchers(HttpMethod.POST,"/albumExecutor").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE,"/albumExecutor").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET,"/executorNews").hasRole("ADMIN")
                .antMatchers(HttpMethod.POST,"/allExecutorsPage").hasAnyRole("USER","ADMIN")
                .antMatchers("/allExecutors").hasAnyRole("USER","ADMIN")
                .antMatchers("/allAlbum").hasAnyRole("USER","ADMIN")
                .antMatchers("/allGenres").hasAnyRole("USER","ADMIN")
                .antMatchers("/albumExecutor").hasAnyRole("USER","ADMIN")
                .antMatchers("/imageAlbum").hasAnyRole("USER","ADMIN")
                .antMatchers("/imageExecutor").hasAnyRole("USER","ADMIN")


//                .antMatchers(HttpMethod.GET,"/users").permitAll()
//                .antMatchers(HttpMethod.DELETE,"/users").permitAll()
//                .antMatchers(HttpMethod.PATCH,"/users").permitAll()
//                .antMatchers(HttpMethod.GET,"/executor").permitAll()
//                .antMatchers(HttpMethod.POST,"/executor").permitAll()
//                .antMatchers(HttpMethod.PATCH,"/executor").permitAll()
//                .antMatchers(HttpMethod.DELETE,"/executor").permitAll()
//                .antMatchers(HttpMethod.GET,"/track").permitAll()
//                .antMatchers(HttpMethod.POST,"/track").permitAll()
//                .antMatchers(HttpMethod.PATCH,"/track").permitAll()
//                .antMatchers(HttpMethod.DELETE,"/track").permitAll()
//                .antMatchers(HttpMethod.GET,"/album").permitAll()
//                .antMatchers(HttpMethod.POST,"/album").permitAll()
//                .antMatchers(HttpMethod.PATCH,"/album").permitAll()
//                .antMatchers(HttpMethod.DELETE,"/album").permitAll()
//                .antMatchers(HttpMethod.GET,"/genre").permitAll()
//                .antMatchers(HttpMethod.POST,"/genre").permitAll()
//                .antMatchers(HttpMethod.PATCH,"/genre").permitAll()
//                .antMatchers(HttpMethod.DELETE,"/genre").permitAll()
//                .antMatchers(HttpMethod.GET,"/news").permitAll()
//                .antMatchers(HttpMethod.POST,"/news").permitAll()
//                .antMatchers(HttpMethod.PATCH,"/news").permitAll()
//                .antMatchers(HttpMethod.DELETE,"/news").permitAll()
//                .antMatchers(HttpMethod.POST,"/trackToExecutor").permitAll()
//                .antMatchers(HttpMethod.POST,"/allExecutorsPage").permitAll()
//                .antMatchers("/allExecutors").permitAll()
//                .antMatchers("/allGenres").permitAll()
//                .antMatchers("/albumExecutor").permitAll()
//                .antMatchers("/imageAlbum").permitAll()
//                .antMatchers("/imageExecutor").permitAll()



                .antMatchers("/register", "/auth","/getsold","/registerAdmin").permitAll()
                .and()
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
    }

}
